package xyz.yuurai.seekr

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.util.Duration
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

class MainWindowModel(private val initialDir: Path) {
    private val currentDirProperty by lazy { SimpleObjectProperty(initialDir) }
    fun currentDirProperty() = currentDirProperty
    private var currentDir: Path
        get() = currentDirProperty.get()
        set(value) = currentDirProperty.set(value)

    val currentDirContents: ObservableList<Path> = FXCollections.observableArrayList()

    val quickAccessDirs: ObservableList<Path> = FXCollections.observableArrayList(
        Paths.get("""C:\"""),
        Paths.get("""C:\Program Files"""),
        HOME_DIRECTORY,
        DESKTOP_DIRECTORY
    )


    private val directoryWatcher = DirectoryWatcher(Duration.seconds(0.1), this::refreshContents)

    init {
        refreshContents()
    }

    fun navigateTo(destination: Path) {
        currentDir = destination

        currentDirContents.clear()
        Files.list(currentDir).use { files ->
            val filteredHidden = files.toList()
                .processIf(!ConfigStore.config.showHiddenItems) {
                    filter { !it.toFile().isHidden }
                }

            val sortedByDirsFirst = filteredHidden
                .processIf(ConfigStore.config.sortByDirsFirst) {
                    partition { it.toFile().isDirectory }
                        .let { (dirs, nonDirs) -> dirs + nonDirs }
                }

            currentDirContents.addAll(sortedByDirsFirst)
        }

        directoryWatcher.watch(currentDir)
    }

    fun refreshContents() = navigateTo(currentDir)

    fun navigateUp() {
        if (currentDir.parent != null) {
            navigateTo(currentDir.parent)
        }
    }

    fun rename(target: Path, newName: String) {
        Files.move(target, target.resolveSibling(newName))
        refreshContents()
    }

    fun delete(target: Path) {
        Desktop.getDesktop().moveToTrash(target.toFile())
        refreshContents()
    }

    fun open(target: Path) {
        if (target.toFile().isDirectory) {
            navigateTo(target)
        } else {
            Desktop.getDesktop().open(target.toFile())
        }
    }
}

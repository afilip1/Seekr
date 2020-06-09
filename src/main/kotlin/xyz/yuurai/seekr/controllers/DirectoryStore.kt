package xyz.yuurai.seekr.controllers

import javafx.util.Duration
import tornadofx.*
import xyz.yuurai.seekr.HOME_DIRECTORY
import xyz.yuurai.seekr.runIf
import xyz.yuurai.seekr.services.DirectoryWatcher
import xyz.yuurai.seekr.services.NavigationHistory
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

class DirectoryStore(initDir: Path = HOME_DIRECTORY) : Controller() {
    private val options: OptionsStore by inject()

    val currentDirProperty = objectProperty(initDir)
    var currentDir: Path by currentDirProperty; private set

    val filesInCurrentDir = nonNullObjectBinding(
        currentDirProperty,
        options.excludeHiddenFilesProperty,
        options.sortByDirsFirstProperty
    ) {
        Files.list(value)
            .use { files ->
                files.toList<Path>()
            }
            .runIf(options.excludeHiddenFiles) {
                filterNot { Files.isHidden(it) }
            }
            .runIf(options.sortByDirsFirst) {
                val (dirs, nonDirs) = partition { Files.isDirectory(it) }
                dirs + nonDirs
            }
            .asObservable()
    }

    private val dirWatcher =
        DirectoryWatcher(
            currentDir,
            Duration.seconds(0.1),
            onChange = this::refreshFiles
        )

    private val navHistory = NavigationHistory(currentDir)

    private fun refreshFiles() {
        filesInCurrentDir.invalidate()
        dirWatcher.watch(currentDir)
    }

    fun navigateTo(destination: Path) {
        currentDir = destination
        navHistory.push(currentDir)
        dirWatcher.watch(currentDir)
    }

    fun navigateUp() {
        navigateTo(currentDir.parent ?: return)
    }

    fun navigateBack() {
        currentDir = navHistory.back() ?: return
        dirWatcher.watch(currentDir)
    }

    fun navigateForward() {
        currentDir = navHistory.forward() ?: return
        dirWatcher.watch(currentDir)
    }

    fun open(target: Path) {
        if (Files.isDirectory(target)) {
            navigateTo(target)
        } else {
            Desktop.getDesktop().open(target.toFile())
        }
    }

    fun rename(target: Path, newName: String) {
        val destination = target.resolveSibling(newName)
        Files.move(target, destination)
    }
}
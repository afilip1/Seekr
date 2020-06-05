package xyz.yuurai.seekr.controllers

import javafx.beans.property.ReadOnlyProperty
import javafx.util.Duration
import tornadofx.*
import xyz.yuurai.seekr.HOME_DIRECTORY
import xyz.yuurai.seekr.services.DirectoryWatcher
import xyz.yuurai.seekr.services.NavigationHistory
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

class DirectoryStore(initDir: Path = HOME_DIRECTORY) : Controller() {
    var currentDir: Path by property(initDir); private set
    fun currentDirProperty(): ReadOnlyProperty<Path> = getProperty(DirectoryStore::currentDir)

    val filesInCurrentDir = nonNullObjectBinding(currentDirProperty()) {
        Files.list(value).use { files ->
            files.toList().asObservable()
        }
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
}
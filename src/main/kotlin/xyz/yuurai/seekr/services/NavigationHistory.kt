package xyz.yuurai.seekr.services

import java.nio.file.Path

class NavigationHistory(initDir: Path) {
    private var history = mutableListOf(initDir)
    private var currentIndex = 0

    fun push(dir: Path) {
        if (currentIndex != history.lastIndex) {
            //TODO: performance?
            history = history.slice(0..currentIndex).toMutableList()
        }
        history.add(dir)
        currentIndex = history.lastIndex
    }

    fun back(): Path? {
        if (currentIndex == 0) return null
        return history[--currentIndex]
    }

    fun forward(): Path? {
        if (currentIndex == history.lastIndex) return null
        return history[++currentIndex]
    }
}
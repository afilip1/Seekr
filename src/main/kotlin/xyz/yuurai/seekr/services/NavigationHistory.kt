package xyz.yuurai.seekr.services

import java.nio.file.Path

class NavigationHistory(initDir: Path) {
    private var history = mutableListOf(initDir)
    private var currentIndex = 0

    /**
     * Adds a new history entry after the current one and sets current position to it, discarding previously existing entries (if any).
     */
    fun push(dir: Path) {
        if (currentIndex != history.lastIndex) {
            //TODO: performance?
            history = history.slice(0..currentIndex).toMutableList()
        }
        history.add(dir)
        currentIndex = history.lastIndex
    }

    /**
     * Moves the current position in history back by one and returns the associated directory.
     *
     * @return `null` if already at oldest item.
     */
    fun back(): Path? {
        if (currentIndex == 0) return null
        return history[--currentIndex]
    }

    /**
     * Moves the current position in history forward by one and returns the associated directory.
     *
     * @return `null` if already at most recent item.
     */
    fun forward(): Path? {
        if (currentIndex == history.lastIndex) return null
        return history[++currentIndex]
    }
}
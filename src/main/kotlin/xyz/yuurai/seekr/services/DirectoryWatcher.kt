package xyz.yuurai.seekr.services

import javafx.concurrent.ScheduledService
import javafx.concurrent.Task
import javafx.util.Duration
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.WatchKey

class DirectoryWatcher(initDir: Path, pollPeriod: Duration, onChange: () -> Unit) {
    private val watchService = FileSystems.getDefault().newWatchService()
    private var watchKey: WatchKey? = null

    @Suppress("unused")
    private val changePoller = ChangePoller().apply {
        period = pollPeriod
        setOnSucceeded {
            if (lastValue == true) {
                onChange()
            }
        }
        start()
    }

    init {
        watch(initDir)
    }

    fun watch(path: Path) {
        watchKey?.cancel()
        watchKey = path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)
    }

    private inner class ChangePoller : ScheduledService<Boolean>() {
        override fun createTask() = object : Task<Boolean>() {
            override fun call() =
                watchKey?.isValid == true && watchService.poll() != null
        }
    }
}

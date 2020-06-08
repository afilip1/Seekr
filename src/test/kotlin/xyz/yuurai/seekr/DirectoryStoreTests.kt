package xyz.yuurai.seekr

import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumeFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import org.testfx.api.FxToolkit
import xyz.yuurai.seekr.app.SeekrApp
import xyz.yuurai.seekr.controllers.DirectoryStore
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectoryStoreTests {
    init {
        FxToolkit.registerPrimaryStage()
        FxToolkit.setupApplication(SeekrApp::class.java)
    }

    @Test
    fun `current directory changes on navigation`(@TempDir tempDir: Path) {
        val (initDir, targetDir) = getTempDirs(tempDir)

        val store = DirectoryStore(initDir)
        assertEquals(initDir, store.currentDir)

        store.navigateTo(targetDir)
        assertEquals(targetDir, store.currentDir)
    }

    @Test
    fun `directory listing updates on navigation`(@TempDir tempDir: Path) {
        val (initDir, targetDir) = getTempDirs(tempDir)

        val store = DirectoryStore(initDir)
        assertTrue(store.filesInCurrentDir.value.isEmpty())

        val testFile = Files.createFile(targetDir.resolve("foo.txt"))
        store.navigateTo(targetDir)

        assertTrue(store.filesInCurrentDir.value.let {
            it.size == 1 && it.single() == testFile
        })
    }

    @Test
    fun `directory listing updates on detecting external change`(@TempDir tempDir: Path) {
        val store = DirectoryStore(tempDir)
        assertTrue(store.filesInCurrentDir.value.isEmpty())

        val testFile = Files.createFile(tempDir.resolve("foo.txt"))

        await().until { store.filesInCurrentDir.value.isNotEmpty() }

        assertTrue(store.filesInCurrentDir.value.let {
            it.size == 1 && it.single() == testFile
        })
    }

    @Test
    fun `opening files launches an associated program`(@TempDir tempDir: Path) {
        fun processExists(command: String): Boolean {
            return ProcessHandle.allProcesses().use { procs ->
                procs.toList().any { proc ->
                    proc.info().command().map { it.contains(command) }.orElse(false)
                }
            }
        }

        val store = DirectoryStore(tempDir)

        assumeFalse(processExists("notepad.exe"))

        val testFile = Files.createFile(tempDir.resolve("foo.txt"))
        store.open(testFile)

        await().until { processExists("notepad.exe") }
        assertTrue(processExists("notepad.exe"))

        Runtime.getRuntime().exec("taskkill /F /IM notepad.exe")
    }

    private fun getTempDirs(tempDir: Path): Pair<Path, Path> {
        val initDir = Files.createDirectory(tempDir.resolve("init"))
        val targetDir = Files.createDirectory(tempDir.resolve("target"))

        return initDir to targetDir
    }
}
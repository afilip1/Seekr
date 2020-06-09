package xyz.yuurai.seekr

import java.nio.file.Path
import java.nio.file.Paths

val HOME_DIRECTORY: Path by lazy { Paths.get(System.getProperty("user.home")) }
val DESKTOP_DIRECTORY: Path by lazy { HOME_DIRECTORY.resolve("Desktop") }

/**
 * Applies transformation to the list if the condition is true, otherwise returns it as is.
 */
fun <T> List<T>.runIf(condition: Boolean, transform: List<T>.() -> List<T>): List<T> {
    return if (condition) {
        this.transform()
    } else {
        this
    }
}
package xyz.yuurai.seekr

import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import java.nio.file.Path
import java.nio.file.Paths

val HOME_DIRECTORY: Path by lazy { Paths.get(System.getProperty("user.home")) }
val DESKTOP_DIRECTORY: Path by lazy { HOME_DIRECTORY.resolve("Desktop") }

internal fun MouseEvent.isDoubleClick(): Boolean =
    button == MouseButton.PRIMARY && clickCount == 2

/**
 * Applies transformation to the iterable if the condition is true, otherwise returns it as is.
 */
fun <T> Iterable<T>.processIf(condition: Boolean, transform: Iterable<T>.() -> Iterable<T>): Iterable<T> {
    return if (condition) {
        this.transform()
    } else {
        this
    }
}
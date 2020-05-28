package xyz.yuurai.seekr

import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import java.nio.file.Path
import java.nio.file.Paths

val HOME_DIRECTORY: Path by lazy { Paths.get(System.getProperty("user.home")) }
val DESKTOP_DIRECTORY: Path by lazy { HOME_DIRECTORY.resolve("Desktop") }

internal fun MouseEvent.isDoubleClick(): Boolean =
        button == MouseButton.PRIMARY && clickCount == 2
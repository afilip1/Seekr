package xyz.yuurai.seekr.views

import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import tornadofx.View
import tornadofx.borderpane
import tornadofx.setStageIcon
import xyz.yuurai.seekr.controllers.DirectoryStore

class MainView : View() {
    private val directoryStore: DirectoryStore by inject()

    init {
        val icon = Image(javaClass.getResourceAsStream("/img/icon.png"))

        title = "Seekr"
        setStageIcon(icon)
    }

    override val root = borderpane {
        prefHeight = 400.0
        prefWidth = 800.0

        top(UrlBar::class)
        left(QuickAccess::class)
        center(DirectoryListing::class)

        addEventFilter(MouseEvent.MOUSE_CLICKED) { e ->
            if (e.button == MouseButton.BACK) {
                directoryStore.navigateBack()
            } else if (e.button == MouseButton.FORWARD) {
                directoryStore.navigateForward()
            }
        }
    }
}
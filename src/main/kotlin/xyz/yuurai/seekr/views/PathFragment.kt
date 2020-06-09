package xyz.yuurai.seekr.views

import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import tornadofx.*
import xyz.yuurai.seekr.controllers.DirectoryStore
import xyz.yuurai.seekr.models.PathModel
import java.nio.file.Path

class PathFragment : ListCellFragment<Path>() {
    private val directoryStore: DirectoryStore by inject()
    private val model = PathModel(itemProperty)

    override val root = hbox {
        spacing = 10.0

        imageview(model.iconImage) {
            opacityProperty().bind(model.iconOpacity)
        }

        label(model.displayName) {
            removeWhen(editingProperty)
        }

        textfield {
            removeWhen(editingProperty.not())
            whenVisible {
                text = model.displayName.value

                requestFocus()

                val fileExtIndex = text.indexOf('.', 1) // don't trip on dotfiles
                if (fileExtIndex != -1) {
                    selectRange(0, fileExtIndex)
                }
            }

            action {
                directoryStore.rename(item, text)
            }
        }

        setOnMouseClicked { e ->
            if (e.isDoubleClick()) {
                directoryStore.open(item)
            }
            e.consume()
        }
    }
}

private fun MouseEvent.isDoubleClick() = button == MouseButton.PRIMARY && clickCount == 2


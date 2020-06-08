package xyz.yuurai.seekr.views

import tornadofx.*
import xyz.yuurai.seekr.controllers.DirectoryStore
import xyz.yuurai.seekr.isDoubleClick
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
        label(model.displayName)

        setOnMouseClicked { e ->
            if (e.isDoubleClick()) {
                directoryStore.open(item)
            }
            e.consume()
        }
    }
}


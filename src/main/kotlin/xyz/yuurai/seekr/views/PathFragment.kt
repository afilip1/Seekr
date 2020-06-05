package xyz.yuurai.seekr.views

import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import tornadofx.*
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.filechooser.FileSystemView

class PathFragment : ListCellFragment<Path>() {
    private val displayName = itemProperty.stringBinding {
        if (it == null) return@stringBinding null

        it.fileName?.toString() ?: it.toString()
    }

    private val iconImage = itemProperty.objectBinding {
        if (it == null) return@objectBinding null

        val file = it.toFile()
        val icon = FileSystemView.getFileSystemView().getSystemIcon(file)

        val bufferedImage = BufferedImage(icon.iconWidth, icon.iconHeight, BufferedImage.TYPE_INT_ARGB)
        icon.paintIcon(null, bufferedImage.graphics, 0, 0)

        SwingFXUtils.toFXImage(bufferedImage, null) as Image
    }

    private val isHidden = itemProperty.booleanBinding {
        Files.isHidden(it ?: return@booleanBinding false)
    }

    override val root = hbox {
        spacing = 10.0

        imageview(iconImage) {
            opacityProperty().bind(
                isHidden.doubleBinding { if (it == true) 0.5 else 1.0 }
            )
        }
        label(displayName)
    }
}
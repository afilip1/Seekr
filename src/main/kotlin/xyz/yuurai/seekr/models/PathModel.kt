package xyz.yuurai.seekr.models

import javafx.beans.property.ObjectProperty
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import tornadofx.*
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.filechooser.FileSystemView

class PathModel(property: ObjectProperty<Path>) : ItemViewModel<Path>(itemProperty = property) {
    private val isHidden = itemProperty.booleanBinding { it != null && Files.isHidden(it) }

    val displayName = itemProperty.stringBinding { it?.fileName?.toString() ?: it?.toString() }
    val iconImage = itemProperty.objectBinding { fetchIcon(it) }
    val iconOpacity = isHidden.doubleBinding { if (it == true) 0.5 else 1.0 }

    // https://stackoverflow.com/questions/28034432/javafx-file-listview-with-icon-and-file-name
    private fun fetchIcon(target: Path?): Image? {
        val file = target?.toFile() ?: return null
        val icon = FileSystemView.getFileSystemView().getSystemIcon(file)

        val bufferedImage = BufferedImage(
            icon.iconWidth,
            icon.iconHeight,
            BufferedImage.TYPE_INT_ARGB
        )
        icon.paintIcon(null, bufferedImage.graphics, 0, 0)

        return SwingFXUtils.toFXImage(bufferedImage, null)
    }
}
//package xyz.yuurai.seekr
//
//import javafx.embed.swing.SwingFXUtils
//import javafx.fxml.FXMLLoader
//import javafx.scene.Parent
//import javafx.scene.control.ContentDisplay
//import javafx.scene.control.ListCell
//import javafx.scene.control.TextField
//import javafx.scene.image.ImageView
//import javafx.scene.input.KeyCode
//import javafx.scene.input.KeyEvent
//import java.awt.image.BufferedImage
//import java.nio.file.Path
//import javax.swing.filechooser.FileSystemView
//
//class PathListCell : ListCell<Path>() {
//    lateinit var iconImageView: ImageView
//    lateinit var fileNameTextField: TextField
//
//    init {
//        val loader = FXMLLoader(javaClass.getResource("/fxml/PathListCell.fxml"))
//        loader.setController(this)
//        graphic = loader.load<Parent>()
//
//        fileNameTextField.managedProperty().bind(fileNameTextField.visibleProperty())
//    }
//
//    fun handleTextFieldKeyPressed(e: KeyEvent) {
//        if (e.code == KeyCode.ESCAPE) {
//            cancelEdit()
//        }
//    }
//
//    fun handleTextFieldAction() {
//        commitEdit(item.resolveSibling(fileNameTextField.text))
//        fileNameTextField.isVisible = false
//        contentDisplay = ContentDisplay.LEFT
//    }
//
//    private fun fetchIcon(target: Path) {
//        //FIXME: see https://stackoverflow.com/questions/28034432/javafx-file-listview-with-icon-and-file-name
//        // Mixing Swing and JavaFX in a single thread because asynchronously populating icons draws them in empty
//        // cells as well and I don't know how to fix it, so #YOLO
//        val file = target.toFile()
//        val icon = FileSystemView.getFileSystemView().getSystemIcon(file)
//
//        val bufferedImage = BufferedImage(icon.iconWidth, icon.iconHeight, BufferedImage.TYPE_INT_ARGB)
//        icon.paintIcon(null, bufferedImage.graphics, 0, 0)
//
//        val fxImage = SwingFXUtils.toFXImage(bufferedImage, null)
//        iconImageView.image = fxImage
//        iconImageView.opacity = if (target.toFile().isHidden) { 0.5 } else { 1.0 }
//    }
//
//    override fun updateItem(item: Path?, empty: Boolean) {
//        super.updateItem(item, empty)
//
//        if (empty || item == null) {
//            text = null
//            iconImageView.image = null
//        } else {
//            text = item.fileName?.toString() ?: item.toString()
//            fetchIcon(item)
//        }
//    }
//
//    override fun startEdit() {
//        super.startEdit()
//
//        contentDisplay = ContentDisplay.GRAPHIC_ONLY
//        fileNameTextField.isVisible = true
//
//        fileNameTextField.apply {
//            text = this@PathListCell.text
//            requestFocus()
//            selectAll()
//        }
//    }
//
//    override fun cancelEdit() {
//        super.cancelEdit()
//
//        fileNameTextField.isVisible = false
//        contentDisplay = ContentDisplay.LEFT
//    }
//}
package xyz.yuurai.seekr

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import java.nio.file.Path
import java.nio.file.Paths

class MainWindowController {
    lateinit var currentDirTextField: TextField
    lateinit var quickAccessListView: ListView<Path>
    lateinit var currentDirContentsListView: ListView<Path>

    private val model = MainWindowModel(
            initialDir = DESKTOP_DIRECTORY)

    @FXML
    fun initialize() {
        currentDirTextField.textProperty().bind(model.currentDirProperty().asString())

        quickAccessListView.apply {
            items = model.quickAccessDirs
            setCellFactory { PathListCell() }
            focusedProperty().addListener { _, _, isFocused ->
                if (!isFocused) {
                    selectionModel.clearSelection()
                }
            }
        }

        currentDirContentsListView.apply {
            items = model.currentDirContents
            setCellFactory {
                val cell = PathListCell()

                val contextMenu = ContextMenu()

                val renameItem = MenuItem("Rename (F2)")
                renameItem.setOnAction { startRenameFile() }

                val deleteItem = MenuItem("Delete (Del)")
                deleteItem.setOnAction { deleteFile() }

                contextMenu.items.addAll(renameItem, deleteItem)

                cell.emptyProperty().addListener { _, _, isNowEmpty ->
                    if (isNowEmpty) {
                        cell.contextMenu = null
                    } else {
                        cell.contextMenu = contextMenu
                    }
                }
                cell.prefWidth = 0.0 // fixes random horizontal scroll

                cell
            }
            setOnEditCommit { e ->
                commitRenameFile(e)
                isEditable = false
            }
            setOnEditCancel {
                isEditable = false
            }
        }

        Platform.runLater { currentDirContentsListView.requestFocus() }
    }

    //FIXME: binding stuff
    fun handleUrlFieldKeyPress(e: KeyEvent) {
        if (e.code == KeyCode.ENTER) {
            handleGoButtonPress()
        }
    }

    //FIXME: binding stuff
    fun handleGoButtonPress() {
        val destination = Paths.get(currentDirTextField.text)
        if (!destination.toFile().exists() || !destination.toFile().isDirectory) {
            Alert(Alert.AlertType.NONE, "This path is not valid", ButtonType.OK).showAndWait()
        } else {
            model.navigateTo(destination)
        }
    }

    fun handleUpButtonPress() = model.navigateUp()

    fun handleQuickAccessClick(e: MouseEvent) {
        if (e.isDoubleClick()) {
            val destination = quickAccessListView.selectionModel.selectedItem
            model.navigateTo(destination)
            quickAccessListView.scrollTo(0)
            quickAccessListView.selectionModel.clearSelection()
        }
    }

    fun handleQuickAccessKeyPress(e: KeyEvent) {
        if (e.code == KeyCode.ENTER) {
            val destination = quickAccessListView.selectionModel.selectedItem
            model.navigateTo(destination)
        }
    }

    fun handleContentsClick(e: MouseEvent) {
        if (e.isDoubleClick()) {
            openFile()
        } else if (e.button == MouseButton.BACK) {
            //TODO: proper navigation history
            model.navigateUp()
        }
    }

    fun handleContentsKeyPress(e: KeyEvent) {
        @Suppress("NON_EXHAUSTIVE_WHEN")
        when (e.code) {
            KeyCode.ENTER -> openFile()
            KeyCode.F2 -> startRenameFile()
            KeyCode.BACK_SPACE -> model.navigateUp()
        }
    }

    private fun openFile() {
        val target = currentDirContentsListView.selectionModel.selectedItem
        model.open(target)
    }

    private fun deleteFile() {
        val target = currentDirContentsListView.selectionModel.selectedItem
        model.delete(target)
    }

    private fun startRenameFile() {
        currentDirContentsListView.apply {
            isEditable = true
            edit(selectionModel.selectedIndex)
        }
    }

    private fun commitRenameFile(e: ListView.EditEvent<Path>) {
        val target = currentDirContentsListView.selectionModel.selectedItem
        val destination = e.newValue.fileName.toString()
        model.rename(target, destination)
        currentDirContentsListView.requestFocus()
        //MAYBE: rename in place without repopulating file listing (like in windows explorer)
    }
}
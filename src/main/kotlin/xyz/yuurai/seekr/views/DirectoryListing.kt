package xyz.yuurai.seekr.views

import javafx.scene.input.KeyCode
import tornadofx.View
import tornadofx.listview
import xyz.yuurai.seekr.controllers.DirectoryStore

class DirectoryListing : View() {
    private val directoryStore: DirectoryStore by inject()

    override val root = listview(directoryStore.filesInCurrentDir) {
        cellFragment(PathFragment::class)

        @Suppress("NON_EXHAUSTIVE_WHEN")
        setOnKeyPressed { e ->
            when {
                e.code == KeyCode.ENTER -> directoryStore.open(selectionModel.selectedItem)
                e.code == KeyCode.BACK_SPACE -> directoryStore.navigateBack()
                e.code == KeyCode.LEFT && e.isAltDown -> directoryStore.navigateBack()
                e.code == KeyCode.RIGHT && e.isAltDown -> directoryStore.navigateForward()
                e.code == KeyCode.F2 -> {
                    isEditable = true
                    edit(selectionModel.selectedIndex)
                }
                e.code == KeyCode.DELETE -> directoryStore.delete(selectionModel.selectedItem)
            }
        }

        setOnEditCommit { isEditable = false }
        setOnEditCancel { isEditable = false }
    }
}

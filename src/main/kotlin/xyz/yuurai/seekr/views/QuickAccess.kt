package xyz.yuurai.seekr.views

import tornadofx.View
import tornadofx.listview
import xyz.yuurai.seekr.controllers.QuickAccessStore
import xyz.yuurai.seekr.controllers.DirectoryStore
import xyz.yuurai.seekr.isDoubleClick

class QuickAccess : View() {
    private val directoryStore: DirectoryStore by inject()
    private val quickAccessStore: QuickAccessStore by inject()

    override val root = listview(quickAccessStore.quickAccessDirs) {
        cellFragment(PathFragment::class)

        setOnMouseClicked { e ->
            if (e.isDoubleClick()) {
                val destination = selectionModel.selectedItem
                directoryStore.navigateTo(destination)
            }
        }
    }
}
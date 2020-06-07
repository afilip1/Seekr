package xyz.yuurai.seekr.controllers

import javafx.beans.property.SimpleBooleanProperty
import tornadofx.Controller
import tornadofx.getValue
import tornadofx.onChange

class OptionsStore : Controller() {
    private val showHiddenFilesKey = "SHOW_HIDDEN_FILES"
    private val sortByDirsFirstKey = "SORT_BY_DIRS_FIRST"

    val showHiddenFilesProperty = SimpleBooleanProperty(
        app.config.boolean(showHiddenFilesKey, false)
    )
    val showHiddenFiles by showHiddenFilesProperty

    // handy conversion
    private val hideHiddenFilesProperty = showHiddenFilesProperty.not()
    val hideHiddenFiles by hideHiddenFilesProperty

    val sortByDirsFirstProperty = SimpleBooleanProperty(
        app.config.boolean(sortByDirsFirstKey, true)
    )
    val sortByDirsFirst by sortByDirsFirstProperty

    init {
        showHiddenFilesProperty.onChange { value ->
            with(app.config) {
                set(showHiddenFilesKey to value)
                save()
            }
        }

        sortByDirsFirstProperty.onChange { value ->
            with(app.config) {
                set(sortByDirsFirstKey to value)
                save()
            }
        }
    }
}
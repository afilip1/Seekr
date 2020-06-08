package xyz.yuurai.seekr.controllers

import javafx.beans.property.SimpleBooleanProperty
import tornadofx.Controller
import tornadofx.booleanProperty
import tornadofx.getValue
import tornadofx.onChange

class OptionsStore : Controller() {
    private val showHiddenFilesKey = "show_hidden_files"
    private val sortByDirsFirstKey = "sort_by_dirs_first"

    val excludeHiddenFilesProperty = booleanProperty(
        app.config.boolean(showHiddenFilesKey, true)
    )
    val excludeHiddenFiles by excludeHiddenFilesProperty

    val sortByDirsFirstProperty = booleanProperty(
        app.config.boolean(sortByDirsFirstKey, true)
    )
    val sortByDirsFirst by sortByDirsFirstProperty

    init {
        excludeHiddenFilesProperty.onChange { value ->
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
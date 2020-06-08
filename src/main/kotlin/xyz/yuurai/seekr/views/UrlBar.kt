package xyz.yuurai.seekr.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory
import javafx.scene.layout.Priority
import tornadofx.*
import xyz.yuurai.seekr.controllers.DirectoryStore
import xyz.yuurai.seekr.controllers.OptionsStore

class UrlBar : View() {
    private val directoryStore: DirectoryStore by inject()
    private val options: OptionsStore by inject()

    override val root = hbox {
        textfield {
            hgrow = Priority.ALWAYS

            bind(directoryStore.currentDirProperty, readonly = true)
        }

        button("Up") {
            graphic = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.LEVEL_UP)
            action {
                directoryStore.navigateUp()
            }
        }

        menubar {
            menu("Options") {
                graphic = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.WRENCH)

                checkmenuitem("Exclude hidden items") {
                    bind(options.excludeHiddenFilesProperty)
                }
                checkmenuitem("Sort by directories first") {
                    bind(options.sortByDirsFirstProperty)
                }
            }
        }
    }
}
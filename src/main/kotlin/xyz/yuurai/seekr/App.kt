package xyz.yuurai.seekr

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import java.nio.file.Path
import java.nio.file.Paths

class App : Application() {
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("/fxml/MainWindow.fxml"))
        val icon = Image(javaClass.getResourceAsStream("/img/icon.png"))

        primaryStage.apply {
            title = "Seekr"
            icons.add(icon)
            scene = Scene(root)
            show()
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
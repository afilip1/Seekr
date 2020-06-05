//package xyz.yuurai.seekr
//
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.json.JsonConfiguration
//import java.io.File
//import kotlin.properties.Delegates.observable
//
//@Serializable
//data class Config(
//    val showHiddenItems: Boolean = false,
//    val sortByDirsFirst: Boolean = true
//)
//
//class ConfigStore {
//    companion object {
//        var config: Config by observable(Config()) { _, _, _ -> writeToDisk() }
//
//        private val configFile: File
//
//        init {
//            val configDir = HOME_DIRECTORY.resolve(".seekr").toFile()
//            if (!configDir.exists()) {
//                configDir.mkdir()
//            }
//
//            val json = Json(JsonConfiguration.Stable)
//
//            configFile = configDir.resolve("config.json")
//            if (!configFile.exists()) {
//                config = Config()
//                val defaultConfigJson = json.stringify(Config.serializer(), config)
//                configFile.writeText(defaultConfigJson)
//            } else {
//                val configFileContents = configFile.readText()
//                config = json.parse(Config.serializer(), configFileContents)
//            }
//        }
//
//        private fun writeToDisk() {
//            val json = Json(JsonConfiguration.Stable)
//            val configJson = json.stringify(Config.serializer(), config)
//            configFile.writeText(configJson)
//        }
//    }
//}
package xyz.yuurai.seekr.controllers

import tornadofx.Controller
import tornadofx.asObservable
import xyz.yuurai.seekr.DESKTOP_DIRECTORY
import xyz.yuurai.seekr.HOME_DIRECTORY
import java.nio.file.Paths

class QuickAccessStore : Controller() {
    val quickAccessDirs = listOf(
        Paths.get("C:\\"),
        Paths.get("C:\\Program Files"),
        HOME_DIRECTORY,
        DESKTOP_DIRECTORY
    ).asObservable()
}
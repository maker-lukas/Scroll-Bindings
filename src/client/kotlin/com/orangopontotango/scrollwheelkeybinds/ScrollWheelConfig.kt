package com.orangopontotango.scrollwheelkeybinds

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import java.io.File

object ScrollWheelConfig {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val configFile: File = FabricLoader.getInstance()
        .configDir.resolve("scroll_wheel_keybinds.json").toFile()

    data class ConfigData(
        var scrollWheelMode: String = "KEYBINDS"
    )

    fun load(): ScrollWheelMode {
        if (!configFile.exists()) {
            save(ScrollWheelMode.KEYBINDS)
            return ScrollWheelMode.KEYBINDS
        }
        return try {
            val data = gson.fromJson(configFile.readText(), ConfigData::class.java)
            ScrollWheelMode.valueOf(data.scrollWheelMode)
        } catch (e: Exception) {
            ScrollWheelMode.KEYBINDS
        }
    }

    fun save(mode: ScrollWheelMode) {
        configFile.writeText(gson.toJson(ConfigData(mode.name)))
    }
}
package com.orangopontotango.scrollbindings

import net.fabricmc.api.ClientModInitializer
import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraft.client.gui.components.debug.DebugScreenEntries
import net.minecraft.resources.Identifier
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object ScrollBindingsClient : ClientModInitializer {
    const val SCROLL_UP_CODE = 100
    const val SCROLL_DOWN_CODE = 101

    val SCROLL_UP_KEY: InputConstants.Key by lazy {
        InputConstants.Type.MOUSE.getOrCreate(SCROLL_UP_CODE)
    }

    val SCROLL_DOWN_KEY: InputConstants.Key by lazy {
        InputConstants.Type.MOUSE.getOrCreate(SCROLL_DOWN_CODE)
    }

    private var scrollUpPressed = false
    private var scrollDownPressed = false

    var scrollWheelMode = ScrollWheelMode.KEYBINDS
        set(value) {
            field = value
            ScrollWheelConfig.save(value)
        }

    fun onScrollInGame(vertical: Double): Boolean {
        if (vertical > 0) {
            KeyMapping.set(SCROLL_UP_KEY, true)
            KeyMapping.click(SCROLL_UP_KEY)
            DebugEntryCps.recordScrollUp()
            scrollUpPressed = true
            return true
        } else if (vertical < 0) {
            KeyMapping.set(SCROLL_DOWN_KEY, true)
            KeyMapping.click(SCROLL_DOWN_KEY)
            DebugEntryCps.recordScrollDown()
            scrollDownPressed = true
            return true
        }
        return false
    }

    fun isScrollKeyBound(): Boolean {
        val client = net.minecraft.client.Minecraft.getInstance() ?: return false
        return client.options.keyMappings.any { it.saveString() == SCROLL_UP_KEY.name }
                || client.options.keyMappings.any { it.saveString() == SCROLL_DOWN_KEY.name }
    }

    override fun onInitializeClient() {
        scrollWheelMode = ScrollWheelConfig.load()

        DebugScreenEntries.register(
            Identifier.parse("scroll_bindings:cps"),
            DebugEntryCps()
        )

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (scrollUpPressed) {
                KeyMapping.set(SCROLL_UP_KEY, false)
                scrollUpPressed = false
            }
            if (scrollDownPressed) {
                KeyMapping.set(SCROLL_DOWN_KEY, false)
                scrollDownPressed = false
            }
        }
    }
}

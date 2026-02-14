package com.orangopontotango.scrollwheelkeybinds

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.util.InputUtil
import org.slf4j.LoggerFactory
import net.minecraft.client.option.KeyBinding
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object ScrollWheelKeybindsClient : ClientModInitializer {
    const val SCROLL_UP_CODE = 100
    const val SCROLL_DOWN_CODE = 101

    val SCROLL_UP_KEY: InputUtil.Key by lazy {
        InputUtil.Type.MOUSE.createFromCode(SCROLL_UP_CODE)
    }

    val SCROLL_DOWN_KEY: InputUtil.Key by lazy {
        InputUtil.Type.MOUSE.createFromCode(SCROLL_DOWN_CODE)
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
            KeyBinding.setKeyPressed(SCROLL_UP_KEY, true)
            KeyBinding.onKeyPressed(SCROLL_UP_KEY)
            scrollUpPressed = true
            return true
        } else if (vertical < 0) {
            KeyBinding.setKeyPressed(SCROLL_DOWN_KEY, true)
            KeyBinding.onKeyPressed(SCROLL_DOWN_KEY)
            scrollDownPressed = true
            return true
        }
        return false
    }

    fun isScrollKeyBound(): Boolean {
        val client = net.minecraft.client.MinecraftClient.getInstance() ?: return false
        return client.options.allKeys.any { it.boundKeyTranslationKey == SCROLL_UP_KEY.translationKey }
                || client.options.allKeys.any { it.boundKeyTranslationKey == SCROLL_DOWN_KEY.translationKey }
    }

    override fun onInitializeClient() {
        scrollWheelMode = ScrollWheelConfig.load()

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (scrollUpPressed) {
                KeyBinding.setKeyPressed(SCROLL_UP_KEY, false)
                scrollUpPressed = false
            }
            if (scrollDownPressed) {
                KeyBinding.setKeyPressed(SCROLL_DOWN_KEY, false)
                scrollDownPressed = false
            }
        }
    }
}

package com.orangopontotango.scrollbindings.mixin.client;

import com.orangopontotango.scrollbindings.ScrollBindingsClient;
import com.orangopontotango.scrollbindings.ScrollWheelMode;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.MouseSettingsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseSettingsScreen.class)
public abstract class MouseOptionsScreenMixin extends OptionsSubScreen {

    protected MouseOptionsScreenMixin(Screen parent, Options gameOptions, Component title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void addScrollWheelOption(CallbackInfo ci) {
        CycleButton<ScrollWheelMode> button = CycleButton
            .builder(MouseOptionsScreenMixin::getModeName, ScrollBindingsClient.INSTANCE.getScrollWheelMode())
            .withValues(ScrollWheelMode.values())
            .create(
                Component.translatable("scroll_bindings.option.mode"),
                (widget, mode) -> ScrollBindingsClient.INSTANCE.setScrollWheelMode(mode)
            );
        this.list.addSmall(button, null);
    }

    private static Component getModeName(ScrollWheelMode mode) {
        return switch (mode) {
            case NORMAL -> Component.translatable("scroll_bindings.mode.normal");
            case KEYBINDS -> Component.translatable("scroll_bindings.mode.keybinds");
            case DISABLED -> Component.translatable("scroll_bindings.mode.disabled");
        };
    }

}

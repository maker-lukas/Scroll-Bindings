package com.orangopontotango.scrollwheelkeybinds.mixin.client;

import com.orangopontotango.scrollwheelkeybinds.ScrollWheelKeybindsClient;
import com.orangopontotango.scrollwheelkeybinds.ScrollWheelMode;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.MouseOptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseOptionsScreen.class)
public abstract class MouseOptionsScreenMixin extends GameOptionsScreen {

    protected MouseOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void addScrollWheelOption(CallbackInfo ci) {
        CyclingButtonWidget<ScrollWheelMode> button = CyclingButtonWidget
            .builder(MouseOptionsScreenMixin::getModeName, ScrollWheelKeybindsClient.INSTANCE.getScrollWheelMode())
            .values(ScrollWheelMode.values())
            .build(
                Text.translatable("scroll_wheel_keybinds.option.mode"),
                (widget, mode) -> ScrollWheelKeybindsClient.INSTANCE.setScrollWheelMode(mode)
            );
        this.body.addWidgetEntry(button, null);
    }

    private static Text getModeName(ScrollWheelMode mode) {
        return switch (mode) {
            case NORMAL -> Text.translatable("scroll_wheel_keybinds.mode.normal");
            case KEYBINDS -> Text.translatable("scroll_wheel_keybinds.mode.keybinds");
            case DISABLED -> Text.translatable("scroll_wheel_keybinds.mode.disabled");
        };
    }

}

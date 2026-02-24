package com.orangopontotango.scrollbindings.mixin.client;

import com.orangopontotango.scrollbindings.ScrollBindingsClient;
import com.orangopontotango.scrollbindings.ScrollWheelMode;
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
            .<ScrollWheelMode>builder(MouseOptionsScreenMixin::getModeName)
            .values(ScrollWheelMode.values())
            .initially(ScrollBindingsClient.INSTANCE.getScrollWheelMode())
            .build(
                Text.translatable("scroll_bindings.option.mode"),
                (widget, mode) -> ScrollBindingsClient.INSTANCE.setScrollWheelMode(mode)
            );
        this.body.addWidgetEntry(button, null);
    }

    private static Text getModeName(ScrollWheelMode mode) {
        return switch (mode) {
            case NORMAL -> Text.translatable("scroll_bindings.mode.normal");
            case KEYBINDS -> Text.translatable("scroll_bindings.mode.keybinds");
            case DISABLED -> Text.translatable("scroll_bindings.mode.disabled");
        };
    }

}

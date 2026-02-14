package com.orangopontotango.scrollbindings.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.orangopontotango.scrollbindings.ScrollBindingsClient;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.orangopontotango.scrollbindings.ScrollWheelMode;

@Mixin(Mouse.class)
public class MouseScrollMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (window != this.client.getWindow().getHandle()) {
            return;
        }
        if (this.client.currentScreen != null || this.client.getOverlay() != null) {
            return;
        }
        if (this.client.player == null) {
            return;
        }

        ScrollWheelMode mode = ScrollBindingsClient.INSTANCE.getScrollWheelMode();

        if (mode == ScrollWheelMode.DISABLED) {
            ci.cancel();
        } else if (mode == ScrollWheelMode.KEYBINDS) {
            if (ScrollBindingsClient.INSTANCE.onScrollInGame(vertical)) {
                ci.cancel();
            }
        }
    }
}

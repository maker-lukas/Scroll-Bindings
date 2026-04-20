package com.orangopontotango.scrollbindings.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.orangopontotango.scrollbindings.ScrollBindingsClient;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.orangopontotango.scrollbindings.ScrollWheelMode;

@Mixin(MouseHandler.class)
public class MouseScrollMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (window != this.minecraft.getWindow().handle()) return;
        if (this.minecraft.screen != null || this.minecraft.getOverlay() != null) return;
        if (this.minecraft.player == null) return;

        if (isZoomifyZooming()) return;

        ScrollWheelMode mode = ScrollBindingsClient.INSTANCE.getScrollWheelMode();

        if (mode == ScrollWheelMode.DISABLED) {
            ci.cancel();
        } else if (mode == ScrollWheelMode.KEYBINDS) {
            if (ScrollBindingsClient.INSTANCE.onScrollInGame(vertical)) {
                ci.cancel();
            }
        }
    }

    private static boolean isZoomifyZooming() {
        try {
            Class<?> zoomifyClass = Class.forName("dev.isxander.zoomify.Zoomify");
            Object instance = zoomifyClass.getField("INSTANCE").get(null);
            return (boolean) zoomifyClass.getMethod("getZooming").invoke(instance);
        } catch (ClassNotFoundException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}

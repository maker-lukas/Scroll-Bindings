package com.orangopontotango.scrollbindings.mixin.client;

import com.orangopontotango.scrollbindings.DebugEntryCps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseClickMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "onPress", at = @At("HEAD"))
    private void onMouseClick(long window, int button, int action, int mods, CallbackInfo ci) {
        if (window != this.minecraft.getWindow().handle()) return;
        if (action != 1) return; // only on press, not release

        if (button == 0) {
            DebugEntryCps.recordLeftClick();
        } else if (button == 1) {
            DebugEntryCps.recordRightClick();
        }
    }
}

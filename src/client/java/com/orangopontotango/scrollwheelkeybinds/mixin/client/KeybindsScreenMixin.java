package com.orangopontotango.scrollwheelkeybinds.mixin.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.screen.option.ControlsListWidget;

import com.orangopontotango.scrollwheelkeybinds.ScrollWheelKeybindsClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Util;

import com.orangopontotango.scrollwheelkeybinds.ScrollWheelMode;

@Mixin(KeybindsScreen.class)
public abstract class KeybindsScreenMixin extends Screen {

    protected KeybindsScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    public KeyBinding selectedKeyBinding;

    @Shadow
    public long lastKeyCodeUpdateTime;

    @Shadow
    private ControlsListWidget controlsList;

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.selectedKeyBinding != null
            && ScrollWheelKeybindsClient.INSTANCE.getScrollWheelMode() == ScrollWheelMode.KEYBINDS) {
            if (verticalAmount > 0) {
                this.selectedKeyBinding.setBoundKey(
                    InputUtil.Type.MOUSE.createFromCode(ScrollWheelKeybindsClient.SCROLL_UP_CODE)
                );
            } else if (verticalAmount < 0) {
                this.selectedKeyBinding.setBoundKey(
                    InputUtil.Type.MOUSE.createFromCode(ScrollWheelKeybindsClient.SCROLL_DOWN_CODE)
                );
            } else {
                return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
            }
            this.selectedKeyBinding = null;
            this.lastKeyCodeUpdateTime = Util.getMeasuringTimeMs();
            this.controlsList.update();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}

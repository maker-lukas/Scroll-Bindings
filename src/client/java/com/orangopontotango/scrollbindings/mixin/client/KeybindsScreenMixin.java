package com.orangopontotango.scrollbindings.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import com.mojang.blaze3d.platform.InputConstants;
import com.orangopontotango.scrollbindings.ScrollBindingsClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

import com.orangopontotango.scrollbindings.ScrollWheelMode;

@Mixin(KeyBindsScreen.class)
public abstract class KeybindsScreenMixin extends Screen {

    protected KeybindsScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    public KeyMapping selectedKey;

    @Shadow
    public long lastKeySelection;

    @Shadow
    private KeyBindsList keyBindsList;

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (ScrollBindingsClient.INSTANCE.getScrollWheelMode() != ScrollWheelMode.KEYBINDS) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        InputConstants.Key scrollKey = null;
        if (verticalAmount > 0) {
            scrollKey = InputConstants.Type.MOUSE.getOrCreate(ScrollBindingsClient.SCROLL_UP_CODE);
        } else if (verticalAmount < 0) {
            scrollKey = InputConstants.Type.MOUSE.getOrCreate(ScrollBindingsClient.SCROLL_DOWN_CODE);
        }

        if (scrollKey == null) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        if (this.selectedKey == null && trySetMultiKeyBinding(scrollKey)) {
            this.lastKeySelection = Util.getMillis();
            this.keyBindsList.resetMappingAndUpdateButtons();
            return true;
        }

        if (this.selectedKey != null) {
            this.selectedKey.setKey(scrollKey);
            this.selectedKey = null;
            this.lastKeySelection = Util.getMillis();
            this.keyBindsList.resetMappingAndUpdateButtons();
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private boolean trySetMultiKeyBinding(InputConstants.Key scrollKey) {
        try {
            Class<?> mkbScreenClass = Class.forName("us.kenny.core.MultiKeyBindingScreen");
            if (!mkbScreenClass.isInstance(this)) {
                return false;
            }

            Object selectedMulti = mkbScreenClass.getMethod("getSelectedMultiKeyBinding").invoke(this);
            if (selectedMulti == null) {
                return false;
            }

            Class<?> managerClass = Class.forName("us.kenny.MultiKeyBindingManager");
            Class<?> mkbClass = Class.forName("us.kenny.core.MultiKeyBinding");
            managerClass.getMethod("setKeyBinding", mkbClass, InputConstants.Key.class)
                    .invoke(null, selectedMulti, scrollKey);

            mkbScreenClass.getMethod("setSelectedMultiKeyBinding", mkbClass).invoke(this, (Object) null);

            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}

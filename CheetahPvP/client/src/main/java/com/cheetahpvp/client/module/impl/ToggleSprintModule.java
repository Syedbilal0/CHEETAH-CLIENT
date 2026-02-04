package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import com.cheetahpvp.client.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * Toggle Sprint - Auto/Toggle sprint functionality
 */
public class ToggleSprintModule extends Module {

    private boolean autoSprint = false;
    private boolean sprinting = false;
    private boolean showHUD = true;

    public ToggleSprintModule() {
        super("Toggle Sprint", "Toggle or auto sprint", Category.MOVEMENT, Keyboard.KEY_R);
    }

    @Override
    public void onEnable() {
        sprinting = true;
    }

    @Override
    public void onDisable() {
        sprinting = false;
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null)
            return;

        if (autoSprint) {
            // Auto sprint when moving forward
            if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking() &&
                    !mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                mc.thePlayer.setSprinting(true);
            }
        } else if (sprinting) {
            // Manual toggle sprint
            if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking() &&
                    !mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                mc.thePlayer.setSprinting(true);
            }
        }
    }

    @Override
    public void onRenderHUD() {
        if (!showHUD)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null)
            return;

        String mode = autoSprint ? "[Auto Sprint]" : "[Sprint: " + (sprinting ? "ON" : "OFF") + "]";
        int color = mc.thePlayer.isSprinting() ? 0xFF2ECC71 : 0xFF888888;

        RenderUtils.drawStringWithShadow(mode, getPosX(), getPosY(), color, getScale(), getOpacity());
    }

    public void toggleAutoSprint() {
        autoSprint = !autoSprint;
    }

    public void setShowHUD(boolean show) {
        this.showHUD = show;
    }

    public boolean isAutoSprint() {
        return autoSprint;
    }
}

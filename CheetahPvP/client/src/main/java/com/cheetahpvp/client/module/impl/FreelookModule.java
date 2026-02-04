package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Freelook Module - Look around without changing movement direction
 */
public class FreelookModule extends Module {

    private float originalYaw;
    private float originalPitch;
    private float cameraYaw;
    private float cameraPitch;

    private float sensitivity = 1.0f;
    private boolean smoothCamera = true;
    private float smoothness = 0.3f;

    private boolean isActive = false;

    public FreelookModule() {
        super("Freelook", "Look around without changing direction", Category.MOVEMENT, Keyboard.KEY_LMENU);
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null)
            return;

        originalYaw = mc.thePlayer.rotationYaw;
        originalPitch = mc.thePlayer.rotationPitch;
        cameraYaw = originalYaw;
        cameraPitch = originalPitch;
        isActive = true;
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null && isActive) {
            // Option to snap back or keep current view
            // mc.thePlayer.rotationYaw = originalYaw;
            // mc.thePlayer.rotationPitch = originalPitch;
        }
        isActive = false;
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null)
            return;

        // Check if key is held (hold mode)
        if (!Keyboard.isKeyDown(getKeyBind())) {
            if (isActive) {
                setEnabled(false);
            }
            return;
        }

        // Get mouse movement
        float deltaX = Mouse.getDX() * sensitivity * 0.15f;
        float deltaY = Mouse.getDY() * sensitivity * 0.15f;

        if (smoothCamera) {
            cameraYaw += deltaX * smoothness;
            cameraPitch -= deltaY * smoothness;
        } else {
            cameraYaw += deltaX;
            cameraPitch -= deltaY;
        }

        // Clamp pitch
        cameraPitch = Math.max(-90, Math.min(90, cameraPitch));
    }

    /**
     * Get the freelook camera yaw (for rendering)
     */
    public float getCameraYaw() {
        return isActive ? cameraYaw : Minecraft.getMinecraft().thePlayer.rotationYaw;
    }

    /**
     * Get the freelook camera pitch (for rendering)
     */
    public float getCameraPitch() {
        return isActive ? cameraPitch : Minecraft.getMinecraft().thePlayer.rotationPitch;
    }

    /**
     * Check if freelook is currently active
     */
    public boolean isFreelookActive() {
        return isActive && isEnabled();
    }

    // Settings
    public void setSensitivity(float sensitivity) {
        this.sensitivity = Math.max(0.1f, Math.min(3.0f, sensitivity));
    }

    public void setSmoothCamera(boolean smooth) {
        this.smoothCamera = smooth;
    }

    public void setSmoothness(float smoothness) {
        this.smoothness = Math.max(0.1f, Math.min(1.0f, smoothness));
    }

    public void resetView() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            cameraYaw = mc.thePlayer.rotationYaw;
            cameraPitch = mc.thePlayer.rotationPitch;
        }
    }
}

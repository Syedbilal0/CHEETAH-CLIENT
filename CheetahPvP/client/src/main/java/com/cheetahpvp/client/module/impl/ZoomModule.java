package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Zoom Module - OptiFine-style zoom with scroll wheel control
 */
public class ZoomModule extends Module {

    private float originalFOV = 70f;
    private float targetFOV = 30f;
    private float currentFOV = 70f;
    private float zoomSpeed = 0.15f;

    private float minZoom = 5f;
    private float maxZoom = 60f;

    private boolean holdMode = true; // false = toggle mode
    private boolean wasZooming = false;
    private float originalSensitivity;
    private boolean smoothZoom = true;

    public ZoomModule() {
        super("Zoom", "OptiFine-style zoom with scroll control", Category.VISUAL, Keyboard.KEY_C);
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        originalFOV = mc.gameSettings.fovSetting;
        originalSensitivity = mc.gameSettings.mouseSensitivity;
        wasZooming = true;
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (wasZooming) {
            mc.gameSettings.fovSetting = originalFOV;
            mc.gameSettings.mouseSensitivity = originalSensitivity;
            wasZooming = false;
        }
        currentFOV = originalFOV;
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null)
            return;

        // Handle hold mode
        if (holdMode && !Keyboard.isKeyDown(getKeyBind())) {
            setEnabled(false);
            return;
        }

        // Handle scroll wheel zoom adjustment
        int scroll = Mouse.getDWheel();
        if (scroll != 0) {
            float zoomChange = scroll > 0 ? -5f : 5f;
            targetFOV = Math.max(minZoom, Math.min(maxZoom, targetFOV + zoomChange));
        }

        // Smooth zoom interpolation
        if (smoothZoom) {
            currentFOV += (targetFOV - currentFOV) * zoomSpeed;
            if (Math.abs(currentFOV - targetFOV) < 0.1f) {
                currentFOV = targetFOV;
            }
        } else {
            currentFOV = targetFOV;
        }

        mc.gameSettings.fovSetting = currentFOV;

        // Reduce sensitivity while zooming
        float zoomRatio = currentFOV / originalFOV;
        mc.gameSettings.mouseSensitivity = originalSensitivity * zoomRatio;
    }

    // Settings
    public void setHoldMode(boolean holdMode) {
        this.holdMode = holdMode;
    }

    public boolean isHoldMode() {
        return holdMode;
    }

    public void setSmoothZoom(boolean smooth) {
        this.smoothZoom = smooth;
    }

    public void setZoomLevel(float fov) {
        this.targetFOV = Math.max(minZoom, Math.min(maxZoom, fov));
    }

    public void setZoomSpeed(float speed) {
        this.zoomSpeed = Math.max(0.05f, Math.min(1f, speed));
    }
}

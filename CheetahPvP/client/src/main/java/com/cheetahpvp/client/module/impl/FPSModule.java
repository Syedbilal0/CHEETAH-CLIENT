package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import com.cheetahpvp.client.util.RenderUtils;
import net.minecraft.client.Minecraft;

/**
 * FPS Counter - Displays current frames per second
 */
public class FPSModule extends Module {

    public FPSModule() {
        super("FPS", "Display your current FPS", Category.HUD);
        setEnabled(true); // Enable by default
    }

    @Override
    public void onRenderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        int fps = Minecraft.getDebugFPS();

        // Color based on FPS
        int color;
        if (fps >= 60) {
            color = 0xFF2ECC71; // Green
        } else if (fps >= 30) {
            color = 0xFFF39C12; // Orange
        } else {
            color = 0xFFE74C3C; // Red
        }

        String text = "FPS: " + fps;

        // Apply module settings
        float scale = getScale();
        float opacity = getOpacity();

        RenderUtils.drawStringWithShadow(text, getPosX(), getPosY(), color, scale, opacity);
    }
}

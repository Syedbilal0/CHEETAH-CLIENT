package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import com.cheetahpvp.client.util.RenderUtils;
import net.minecraft.client.Minecraft;

/**
 * Reach Display - Shows last hit distance
 */
public class ReachModule extends Module {

    private float lastReach = 0;
    private long lastHitTime = 0;
    private static final long DISPLAY_DURATION = 3000; // Show for 3 seconds after hit

    public ReachModule() {
        super("Reach Display", "Show hit distance", Category.HUD);
        setPosX(5);
        setPosY(65);
    }

    public void onHit(float distance) {
        lastReach = distance;
        lastHitTime = System.currentTimeMillis();
    }

    @Override
    public void onRenderHUD() {
        long now = System.currentTimeMillis();
        if (now - lastHitTime > DISPLAY_DURATION)
            return;

        // Fade out animation
        float fade = 1.0f;
        long timeSinceHit = now - lastHitTime;
        if (timeSinceHit > DISPLAY_DURATION - 500) {
            fade = (DISPLAY_DURATION - timeSinceHit) / 500f;
        }

        String text = String.format("Reach: %.2fm", lastReach);
        int color = getColor().getRGB() | 0xFF000000;

        RenderUtils.drawStringWithShadow(text, getPosX(), getPosY(), color, getScale(), getOpacity() * fade);
    }
}

package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import com.cheetahpvp.client.util.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

/**
 * CPS Counter - Displays clicks per second for left and right mouse buttons
 */
public class CPSModule extends Module {

    private static final int SAMPLE_WINDOW = 1000; // 1 second window

    private long[] leftClicks = new long[100];
    private long[] rightClicks = new long[100];
    private int leftIndex = 0;
    private int rightIndex = 0;

    private boolean leftDown = false;
    private boolean rightDown = false;

    public CPSModule() {
        super("CPS", "Display your clicks per second", Category.HUD);
        setPosX(5);
        setPosY(20);
    }

    @Override
    public void onTick() {
        long now = System.currentTimeMillis();

        // Track left clicks
        if (Mouse.isButtonDown(0)) {
            if (!leftDown) {
                leftClicks[leftIndex % leftClicks.length] = now;
                leftIndex++;
                leftDown = true;
            }
        } else {
            leftDown = false;
        }

        // Track right clicks
        if (Mouse.isButtonDown(1)) {
            if (!rightDown) {
                rightClicks[rightIndex % rightClicks.length] = now;
                rightIndex++;
                rightDown = true;
            }
        } else {
            rightDown = false;
        }
    }

    @Override
    public void onRenderHUD() {
        long now = System.currentTimeMillis();
        long cutoff = now - SAMPLE_WINDOW;

        // Count clicks in the last second
        int leftCPS = 0;
        for (long click : leftClicks) {
            if (click > cutoff)
                leftCPS++;
        }

        int rightCPS = 0;
        for (long click : rightClicks) {
            if (click > cutoff)
                rightCPS++;
        }

        String text = leftCPS + " | " + rightCPS + " CPS";
        int color = getColor().getRGB() | 0xFF000000;

        RenderUtils.drawStringWithShadow(text, getPosX(), getPosY(), color, getScale(), getOpacity());
    }
}

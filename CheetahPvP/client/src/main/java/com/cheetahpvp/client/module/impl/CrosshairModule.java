package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import com.cheetahpvp.client.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

/**
 * Crosshair Customizer - Custom crosshair styles and colors
 */
public class CrosshairModule extends Module {

    public enum CrosshairStyle {
        DEFAULT,
        DOT,
        CROSS,
        CIRCLE,
        PLUS
    }

    private CrosshairStyle style = CrosshairStyle.CROSS;
    private int size = 6;
    private int thickness = 2;
    private int gap = 3;
    private boolean dynamicGap = true;

    public CrosshairModule() {
        super("Crosshair", "Customize your crosshair", Category.VISUAL);
    }

    @Override
    public void onRenderHUD() {
        if (style == CrosshairStyle.DEFAULT)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);

        int centerX = sr.getScaledWidth() / 2;
        int centerY = sr.getScaledHeight() / 2;

        int color = getColor().getRGB() | 0xFF000000;
        color = withOpacity(color, getOpacity());

        int currentGap = gap;
        if (dynamicGap && mc.thePlayer != null && mc.thePlayer.isSprinting()) {
            currentGap += 2;
        }

        switch (style) {
            case DOT:
                RenderUtils.drawRect(centerX - 1, centerY - 1, 2, 2, color);
                break;

            case CROSS:
                // Vertical line
                RenderUtils.drawRect(centerX - thickness / 2, centerY - size - currentGap,
                        thickness, size, color);
                RenderUtils.drawRect(centerX - thickness / 2, centerY + currentGap,
                        thickness, size, color);
                // Horizontal line
                RenderUtils.drawRect(centerX - size - currentGap, centerY - thickness / 2,
                        size, thickness, color);
                RenderUtils.drawRect(centerX + currentGap, centerY - thickness / 2,
                        size, thickness, color);
                break;

            case CIRCLE:
                RenderUtils.drawCircle(centerX, centerY, size, color, thickness);
                break;

            case PLUS:
                // Simple plus shape
                RenderUtils.drawRect(centerX - size, centerY - 1, size * 2, 2, color);
                RenderUtils.drawRect(centerX - 1, centerY - size, 2, size * 2, color);
                break;
        }
    }

    private int withOpacity(int color, float opacity) {
        int alpha = (int) ((color >> 24 & 0xFF) * opacity);
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    // Setters for customization
    public void setStyle(CrosshairStyle style) {
        this.style = style;
    }

    public void setSize(int size) {
        this.size = Math.max(1, Math.min(20, size));
    }

    public void setThickness(int thickness) {
        this.thickness = Math.max(1, Math.min(5, thickness));
    }

    public void setGap(int gap) {
        this.gap = Math.max(0, Math.min(10, gap));
    }

    public void setDynamicGap(boolean dynamicGap) {
        this.dynamicGap = dynamicGap;
    }
}

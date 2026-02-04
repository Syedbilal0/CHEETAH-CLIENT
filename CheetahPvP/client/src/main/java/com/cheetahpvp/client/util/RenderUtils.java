package com.cheetahpvp.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * Utility class for 2D rendering operations
 */
public class RenderUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Draw a basic rectangle
     */
    public static void drawRect(int x, int y, int width, int height, int color) {
        float alpha = (color >> 24 & 255) / 255.0f;
        float red = (color >> 16 & 255) / 255.0f;
        float green = (color >> 8 & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x, y + height, 0.0D).endVertex();
        worldRenderer.pos(x + width, y + height, 0.0D).endVertex();
        worldRenderer.pos(x + width, y, 0.0D).endVertex();
        worldRenderer.pos(x, y, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draw a rounded rectangle
     */
    public static void drawRoundedRect(int x, int y, int width, int height, int radius, int color) {
        float alpha = (color >> 24 & 255) / 255.0f;
        float red = (color >> 16 & 255) / 255.0f;
        float green = (color >> 8 & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        // Draw main body
        drawRect(x + radius, y, width - radius * 2, height, color);
        drawRect(x, y + radius, radius, height - radius * 2, color);
        drawRect(x + width - radius, y + radius, radius, height - radius * 2, color);

        // Draw corners
        drawArc(x + radius, y + radius, radius, 180, 270, color);
        drawArc(x + width - radius, y + radius, radius, 270, 360, color);
        drawArc(x + radius, y + height - radius, radius, 90, 180, color);
        drawArc(x + width - radius, y + height - radius, radius, 0, 90, color);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draw an arc for rounded corners
     */
    private static void drawArc(int x, int y, int radius, int startAngle, int endAngle, int color) {
        float alpha = (color >> 24 & 255) / 255.0f;
        float red = (color >> 16 & 255) / 255.0f;
        float green = (color >> 8 & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        GlStateManager.color(red, green, blue, alpha);
        worldRenderer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x, y, 0).endVertex();

        for (int i = startAngle; i <= endAngle; i += 5) {
            double angle = Math.toRadians(i);
            worldRenderer.pos(x + Math.cos(angle) * radius, y + Math.sin(angle) * radius, 0).endVertex();
        }

        tessellator.draw();
    }

    /**
     * Draw a circle
     */
    public static void drawCircle(int x, int y, int radius, int color, int thickness) {
        float alpha = (color >> 24 & 255) / 255.0f;
        float red = (color >> 16 & 255) / 255.0f;
        float green = (color >> 8 & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(thickness);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        worldRenderer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        for (int i = 0; i < 360; i += 5) {
            double angle = Math.toRadians(i);
            worldRenderer.pos(x + Math.cos(angle) * radius, y + Math.sin(angle) * radius, 0).endVertex();
        }
        tessellator.draw();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draw string with shadow and optional scale/opacity
     */
    public static void drawStringWithShadow(String text, int x, int y, int color, float scale, float opacity) {
        FontRenderer fr = mc.fontRendererObj;

        // Apply opacity to color
        int alpha = (int) ((color >> 24 & 255) * opacity);
        color = (alpha << 24) | (color & 0x00FFFFFF);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1);

        fr.drawStringWithShadow(text, 0, 0, color);

        GlStateManager.popMatrix();
    }

    /**
     * Draw centered string
     */
    public static void drawCenteredString(String text, int x, int y, int color) {
        FontRenderer fr = mc.fontRendererObj;
        fr.drawStringWithShadow(text, x - fr.getStringWidth(text) / 2, y, color);
    }

    /**
     * Start scissor (clipping) region
     */
    public static void startScissor(int x, int y, int width, int height) {
        int scale = mc.gameSettings.guiScale;
        if (scale == 0)
            scale = 1;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x * scale, mc.displayHeight - (y + height) * scale, width * scale, height * scale);
    }

    /**
     * End scissor region
     */
    public static void endScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}

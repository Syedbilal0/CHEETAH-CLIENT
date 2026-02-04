package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import com.cheetahpvp.client.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Keystrokes - Displays WASD keys and mouse buttons with real-time feedback
 */
public class KeystrokesModule extends Module {

    private static final int KEY_SIZE = 22;
    private static final int KEY_SPACING = 2;
    private static final int CORNER_RADIUS = 3;

    public KeystrokesModule() {
        super("Keystrokes", "Display WASD keys and mouse buttons", Category.HUD);
        setPosX(5);
        setPosY(50);
    }

    @Override
    public void onRenderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null)
            return;

        int baseX = getPosX();
        int baseY = getPosY();
        float scale = getScale();
        float opacity = getOpacity();

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, 1);

        int scaledX = (int) (baseX / scale);
        int scaledY = (int) (baseY / scale);

        // Get key states
        boolean w = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
        boolean a = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
        boolean s = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
        boolean d = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
        boolean space = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
        boolean lmb = Mouse.isButtonDown(0);
        boolean rmb = Mouse.isButtonDown(1);

        // Draw W key
        drawKey("W", scaledX + KEY_SIZE + KEY_SPACING, scaledY, w, opacity);

        // Draw A, S, D keys
        drawKey("A", scaledX, scaledY + KEY_SIZE + KEY_SPACING, a, opacity);
        drawKey("S", scaledX + KEY_SIZE + KEY_SPACING, scaledY + KEY_SIZE + KEY_SPACING, s, opacity);
        drawKey("D", scaledX + (KEY_SIZE + KEY_SPACING) * 2, scaledY + KEY_SIZE + KEY_SPACING, d, opacity);

        // Draw mouse buttons
        int mouseY = scaledY + (KEY_SIZE + KEY_SPACING) * 2;
        drawMouseButton("LMB", scaledX, mouseY, lmb, opacity);
        drawMouseButton("RMB", scaledX + KEY_SIZE + KEY_SPACING + KEY_SIZE / 2 + 1, mouseY, rmb, opacity);

        // Draw spacebar
        drawSpacebar(scaledX, scaledY + (KEY_SIZE + KEY_SPACING) * 3, space, opacity);

        GL11.glPopMatrix();
    }

    private void drawKey(String label, int x, int y, boolean pressed, float opacity) {
        int bgColor = pressed ? withOpacity(0xFFF39C12, opacity) : withOpacity(0x80000000, opacity);
        int textColor = pressed ? withOpacity(0xFFFFFFFF, opacity) : withOpacity(0xFFAAAAAA, opacity);

        RenderUtils.drawRoundedRect(x, y, KEY_SIZE, KEY_SIZE, CORNER_RADIUS, bgColor);
        RenderUtils.drawCenteredString(label, x + KEY_SIZE / 2, y + KEY_SIZE / 2 - 4, textColor);
    }

    private void drawMouseButton(String label, int x, int y, boolean pressed, float opacity) {
        int width = KEY_SIZE + KEY_SPACING / 2;
        int bgColor = pressed ? withOpacity(0xFFF39C12, opacity) : withOpacity(0x80000000, opacity);
        int textColor = pressed ? withOpacity(0xFFFFFFFF, opacity) : withOpacity(0xFFAAAAAA, opacity);

        RenderUtils.drawRoundedRect(x, y, width, KEY_SIZE, CORNER_RADIUS, bgColor);
        RenderUtils.drawCenteredString(label, x + width / 2, y + KEY_SIZE / 2 - 4, textColor);
    }

    private void drawSpacebar(int x, int y, boolean pressed, float opacity) {
        int width = KEY_SIZE * 3 + KEY_SPACING * 2;
        int height = KEY_SIZE - 4;
        int bgColor = pressed ? withOpacity(0xFFF39C12, opacity) : withOpacity(0x80000000, opacity);

        RenderUtils.drawRoundedRect(x, y, width, height, CORNER_RADIUS, bgColor);
    }

    private int withOpacity(int color, float opacity) {
        int alpha = (int) ((color >> 24 & 0xFF) * opacity);
        return (alpha << 24) | (color & 0x00FFFFFF);
    }
}

package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import com.cheetahpvp.client.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.opengl.GL11;

import java.util.Collection;

/**
 * Potion Status - Displays active potion effects with duration
 */
public class PotionStatusModule extends Module {

    public PotionStatusModule() {
        super("Potion Status", "Display active potion effects", Category.HUD);
        setPosX(5);
        setPosY(230);
    }

    @Override
    public void onRenderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null)
            return;

        Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
        if (effects.isEmpty())
            return;

        int x = getPosX();
        int y = getPosY();
        float scale = getScale();
        float opacity = getOpacity();

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, 1);

        int scaledX = (int) (x / scale);
        int scaledY = (int) (y / scale);

        int offsetY = 0;
        for (PotionEffect effect : effects) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (potion == null)
                continue;

            String name = I18n.format(potion.getName());
            int amplifier = effect.getAmplifier();
            if (amplifier > 0) {
                name += " " + toRoman(amplifier + 1);
            }

            String duration = formatDuration(effect.getDuration());
            String text = name + " §7" + duration;

            // Get potion color
            int potionColor = potion.getLiquidColor() | 0xFF000000;
            potionColor = withOpacity(potionColor, opacity);

            // Draw background
            int textWidth = mc.fontRendererObj.getStringWidth(name + " " + duration);
            RenderUtils.drawRoundedRect(scaledX - 2, scaledY + offsetY - 1, textWidth + 6, 12, 3,
                    withOpacity(0x80000000, opacity));

            // Draw potion indicator
            RenderUtils.drawRect(scaledX - 2, scaledY + offsetY - 1, 2, 12, potionColor);

            // Draw text
            RenderUtils.drawStringWithShadow(text, scaledX + 2, scaledY + offsetY,
                    withOpacity(0xFFFFFFFF, opacity), 1.0f, opacity);

            offsetY += 14;
        }

        GL11.glPopMatrix();
    }

    private String formatDuration(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private String toRoman(int num) {
        String[] romans = { "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };
        return num < romans.length ? romans[num] : String.valueOf(num);
    }

    private int withOpacity(int color, float opacity) {
        int alpha = (int) ((color >> 24 & 0xFF) * opacity);
        return (alpha << 24) | (color & 0x00FFFFFF);
    }
}

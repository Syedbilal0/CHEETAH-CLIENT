package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import com.cheetahpvp.client.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

/**
 * Armor HUD - Displays equipped armor with durability bars
 */
public class ArmorHUDModule extends Module {

    private static final int ITEM_SIZE = 16;
    private static final int SPACING = 2;

    public ArmorHUDModule() {
        super("Armor HUD", "Display your equipped armor", Category.HUD);
        setPosX(5);
        setPosY(150);
    }

    @Override
    public void onRenderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null)
            return;

        ScaledResolution sr = new ScaledResolution(mc);

        int x = getPosX();
        int y = getPosY();
        float scale = getScale();
        float opacity = getOpacity();

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, 1);

        int scaledX = (int) (x / scale);
        int scaledY = (int) (y / scale);

        // Draw background
        int bgColor = (int) (opacity * 128) << 24;
        RenderUtils.drawRoundedRect(scaledX - 2, scaledY - 2, ITEM_SIZE + 28, (ITEM_SIZE + SPACING) * 4 + 2, 4,
                bgColor);

        // Draw armor pieces (helmet to boots)
        for (int i = 3; i >= 0; i--) {
            ItemStack armor = mc.thePlayer.inventory.armorInventory[i];
            int itemY = scaledY + (3 - i) * (ITEM_SIZE + SPACING);

            if (armor != null) {
                // Draw item
                RenderHelper.enableGUIStandardItemLighting();
                mc.getRenderItem().renderItemAndEffectIntoGUI(armor, scaledX, itemY);
                RenderHelper.disableStandardItemLighting();

                // Draw durability bar
                if (armor.isItemStackDamageable()) {
                    int maxDamage = armor.getMaxDamage();
                    int currentDamage = armor.getItemDamage();
                    float durability = 1.0f - (float) currentDamage / maxDamage;

                    int barWidth = 24;
                    int barHeight = 3;
                    int barX = scaledX + ITEM_SIZE + 2;
                    int barY = itemY + ITEM_SIZE / 2 - 1;

                    // Background
                    RenderUtils.drawRect(barX, barY, barWidth, barHeight, 0x80000000);

                    // Durability bar with color
                    int barColor = getDurabilityColor(durability, opacity);
                    RenderUtils.drawRect(barX, barY, (int) (barWidth * durability), barHeight, barColor);
                }
            } else {
                // Draw empty slot indicator
                int emptyY = scaledY + (3 - i) * (ITEM_SIZE + SPACING);
                RenderUtils.drawRect(scaledX + 4, emptyY + 4, 8, 8, (int) (opacity * 60) << 24);
            }
        }

        GL11.glPopMatrix();
    }

    private int getDurabilityColor(float durability, float opacity) {
        int alpha = (int) (opacity * 255);
        if (durability > 0.6f) {
            return (alpha << 24) | 0x2ECC71; // Green
        } else if (durability > 0.3f) {
            return (alpha << 24) | 0xF39C12; // Orange
        } else {
            return (alpha << 24) | 0xE74C3C; // Red
        }
    }
}

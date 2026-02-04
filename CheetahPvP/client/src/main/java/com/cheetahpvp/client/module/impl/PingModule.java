package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import com.cheetahpvp.client.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Ping Display - Shows server latency
 */
public class PingModule extends Module {

    public PingModule() {
        super("Ping", "Display your server ping", Category.HUD);
        setPosX(5);
        setPosY(35);
    }

    @Override
    public void onRenderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.getNetHandler() == null)
            return;

        int ping = 0;
        try {
            ping = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
        } catch (Exception e) {
            ping = -1;
        }

        // Color based on ping
        int color;
        if (ping < 0) {
            color = 0xFF888888; // Unknown
        } else if (ping < 50) {
            color = 0xFF2ECC71; // Green - excellent
        } else if (ping < 100) {
            color = 0xFF27AE60; // Light green - good
        } else if (ping < 150) {
            color = 0xFFF39C12; // Orange - okay
        } else if (ping < 250) {
            color = 0xFFE67E22; // Dark orange - poor
        } else {
            color = 0xFFE74C3C; // Red - bad
        }

        String text = ping >= 0 ? "Ping: " + ping + "ms" : "Ping: N/A";

        RenderUtils.drawStringWithShadow(text, getPosX(), getPosY(), color, getScale(), getOpacity());
    }
}

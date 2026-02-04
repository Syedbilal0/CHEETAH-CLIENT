package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

/**
 * FullBright Module - Maximum gamma for seeing in dark areas
 */
public class FullBrightModule extends Module {

    private float originalGamma;
    private boolean useNightVision = false;

    public FullBrightModule() {
        super("FullBright", "See clearly in dark areas", Category.VISUAL, Keyboard.KEY_B);
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        originalGamma = mc.gameSettings.gammaSetting;

        if (useNightVision && mc.thePlayer != null) {
            // Apply night vision effect (client-side only)
            mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 999999, 0, false, false));
        } else {
            mc.gameSettings.gammaSetting = 100f;
        }
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getMinecraft();

        if (useNightVision && mc.thePlayer != null) {
            mc.thePlayer.removePotionEffect(Potion.nightVision.id);
        }

        mc.gameSettings.gammaSetting = originalGamma;
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();

        // Maintain max gamma
        if (!useNightVision && mc.gameSettings.gammaSetting < 100f) {
            mc.gameSettings.gammaSetting = 100f;
        }

        // Maintain night vision effect
        if (useNightVision && mc.thePlayer != null) {
            if (!mc.thePlayer.isPotionActive(Potion.nightVision)) {
                mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 999999, 0, false, false));
            }
        }
    }

    public void setUseNightVision(boolean use) {
        this.useNightVision = use;
        if (isEnabled()) {
            onDisable();
            onEnable();
        }
    }

    public boolean isUsingNightVision() {
        return useNightVision;
    }
}

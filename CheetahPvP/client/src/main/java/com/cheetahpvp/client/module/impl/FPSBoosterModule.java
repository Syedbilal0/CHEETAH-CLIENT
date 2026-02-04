package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import net.minecraft.client.Minecraft;

/**
 * FPS Booster Module - Performance optimizations for low-end PCs
 */
public class FPSBoosterModule extends Module {

    // Optimization settings
    private boolean reducedParticles = true;
    private boolean entityCulling = true;
    private boolean fastRender = true;
    private boolean disableBlockAnimations = false;
    private boolean lowFireAnimation = true;
    private boolean reducedBeaconBeam = true;
    private boolean backgroundFPSLimit = true;
    private int backgroundFPS = 30;

    // Entity culling distance
    private int maxEntityRenderDistance = 64;

    public FPSBoosterModule() {
        super("FPS Boost", "Performance optimizations", Category.PERFORMANCE);
        setEnabled(true); // Enable by default
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();

        // Limit FPS when in background
        if (backgroundFPSLimit && !mc.inGameHasFocus) {
            mc.gameSettings.limitFramerate = backgroundFPS;
        }
    }

    // Getters for optimization hooks

    public boolean shouldReduceParticles() {
        return isEnabled() && reducedParticles;
    }

    public boolean shouldCullEntities() {
        return isEnabled() && entityCulling;
    }

    public boolean shouldUseFastRender() {
        return isEnabled() && fastRender;
    }

    public boolean shouldDisableBlockAnimations() {
        return isEnabled() && disableBlockAnimations;
    }

    public boolean shouldUseLowFireAnimation() {
        return isEnabled() && lowFireAnimation;
    }

    public boolean shouldReduceBeaconBeam() {
        return isEnabled() && reducedBeaconBeam;
    }

    public int getMaxEntityRenderDistance() {
        return maxEntityRenderDistance;
    }

    public int getBackgroundFPS() {
        return backgroundFPS;
    }

    // Setters for customization

    public void setReducedParticles(boolean enabled) {
        this.reducedParticles = enabled;
    }

    public void setEntityCulling(boolean enabled) {
        this.entityCulling = enabled;
    }

    public void setFastRender(boolean enabled) {
        this.fastRender = enabled;
    }

    public void setDisableBlockAnimations(boolean enabled) {
        this.disableBlockAnimations = enabled;
    }

    public void setLowFireAnimation(boolean enabled) {
        this.lowFireAnimation = enabled;
    }

    public void setReduceBeaconBeam(boolean enabled) {
        this.reducedBeaconBeam = enabled;
    }

    public void setMaxEntityRenderDistance(int distance) {
        this.maxEntityRenderDistance = Math.max(16, Math.min(128, distance));
    }

    public void setBackgroundFPS(int fps) {
        this.backgroundFPS = Math.max(5, Math.min(60, fps));
    }
}

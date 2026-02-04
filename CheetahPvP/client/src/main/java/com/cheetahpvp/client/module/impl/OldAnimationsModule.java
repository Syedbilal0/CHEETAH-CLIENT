package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;
import org.lwjgl.input.Keyboard;

/**
 * Old Animations Module - 1.7 style animations for PvP
 */
public class OldAnimationsModule extends Module {

    // Animation settings
    private boolean oldBlocking = true;
    private boolean oldSwing = true;
    private boolean oldItemPositions = true;
    private boolean oldBowPull = true;
    private boolean oldRodAnimation = true;
    private boolean alwaysSwing = true;
    private boolean noSwingDelay = false;

    // Item position offsets
    private float itemPosX = 0.0f;
    private float itemPosY = 0.0f;
    private float itemPosZ = 0.0f;

    public OldAnimationsModule() {
        super("1.7 Animations", "Classic PvP animations", Category.VISUAL, Keyboard.KEY_O);
    }

    // Getters for animation settings (used by mixins/hooks)

    public boolean shouldUseOldBlocking() {
        return isEnabled() && oldBlocking;
    }

    public boolean shouldUseOldSwing() {
        return isEnabled() && oldSwing;
    }

    public boolean shouldUseOldItemPositions() {
        return isEnabled() && oldItemPositions;
    }

    public boolean shouldUseOldBowPull() {
        return isEnabled() && oldBowPull;
    }

    public boolean shouldUseOldRodAnimation() {
        return isEnabled() && oldRodAnimation;
    }

    public boolean shouldAlwaysSwing() {
        return isEnabled() && alwaysSwing;
    }

    public boolean shouldNoSwingDelay() {
        return isEnabled() && noSwingDelay;
    }

    public float getItemPosX() {
        return itemPosX;
    }

    public float getItemPosY() {
        return itemPosY;
    }

    public float getItemPosZ() {
        return itemPosZ;
    }

    // Setters for customization

    public void setOldBlocking(boolean enabled) {
        this.oldBlocking = enabled;
    }

    public void setOldSwing(boolean enabled) {
        this.oldSwing = enabled;
    }

    public void setOldItemPositions(boolean enabled) {
        this.oldItemPositions = enabled;
    }

    public void setOldBowPull(boolean enabled) {
        this.oldBowPull = enabled;
    }

    public void setOldRodAnimation(boolean enabled) {
        this.oldRodAnimation = enabled;
    }

    public void setAlwaysSwing(boolean enabled) {
        this.alwaysSwing = enabled;
    }

    public void setNoSwingDelay(boolean enabled) {
        this.noSwingDelay = enabled;
    }

    public void setItemPosition(float x, float y, float z) {
        this.itemPosX = x;
        this.itemPosY = y;
        this.itemPosZ = z;
    }
}

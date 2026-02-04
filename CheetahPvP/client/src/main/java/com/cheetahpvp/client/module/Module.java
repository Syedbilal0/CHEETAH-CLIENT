package com.cheetahpvp.client.module;

import org.lwjgl.input.Keyboard;

import java.awt.Color;

/**
 * Base class for all CheetahPvP modules
 */
public abstract class Module {

    private final String name;
    private final String description;
    private final Category category;

    private boolean enabled;
    private int keyBind;

    // Customization options
    private Color color = new Color(243, 156, 18); // Orange default
    private float scale = 1.0f;
    private float opacity = 1.0f;
    private int posX = 5;
    private int posY = 5;

    public enum Category {
        COMBAT("Combat", 0xFFE74C3C),
        MOVEMENT("Movement", 0xFF3498DB),
        VISUAL("Visual", 0xFF9B59B6),
        HUD("HUD", 0xFF2ECC71),
        PERFORMANCE("Performance", 0xFFF39C12),
        OTHER("Other", 0xFF95A5A6);

        private final String displayName;
        private final int color;

        Category(String displayName, int color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getColor() {
            return color;
        }
    }

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
        this.keyBind = Keyboard.KEY_NONE;
    }

    public Module(String name, String description, Category category, int defaultKey) {
        this(name, description, category);
        this.keyBind = defaultKey;
    }

    /**
     * Called when the module is enabled
     */
    public void onEnable() {
    }

    /**
     * Called when the module is disabled
     */
    public void onDisable() {
    }

    /**
     * Called every client tick when enabled
     */
    public void onTick() {
    }

    /**
     * Called every render frame when enabled
     */
    public void onRender(float partialTicks) {
    }

    /**
     * Called for HUD rendering
     */
    public void onRenderHUD() {
    }

    /**
     * Toggle the module on/off
     */
    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = Math.max(0.5f, Math.min(3.0f, scale));
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = Math.max(0f, Math.min(1f, opacity));
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public String getKeyBindName() {
        if (keyBind == Keyboard.KEY_NONE) {
            return "None";
        }
        return Keyboard.getKeyName(keyBind);
    }
}

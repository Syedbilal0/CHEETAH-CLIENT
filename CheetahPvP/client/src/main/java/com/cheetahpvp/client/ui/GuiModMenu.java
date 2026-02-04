package com.cheetahpvp.client.ui;

import com.cheetahpvp.client.CheetahPvP;
import com.cheetahpvp.client.module.Module;
import com.cheetahpvp.client.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Modern animated mod menu - Opens with Right Shift
 */
public class GuiModMenu extends GuiScreen {

    // Menu dimensions
    private int menuX, menuY;
    private int menuWidth = 500;
    private int menuHeight = 360;

    // Animation
    private float animation = 0;
    private float targetAnimation = 1;
    private boolean closing = false;

    // Categories and selection
    private Module.Category selectedCategory = Module.Category.HUD;
    private List<ModulePanel> modulePanels = new ArrayList<>();
    private int scrollOffset = 0;

    // Search
    private String searchQuery = "";
    private boolean searchFocused = false;

    // Dragging
    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    // Colors
    private static final int BG_COLOR = 0xE0101020;
    private static final int ACCENT_COLOR = 0xFFF39C12;
    private static final int PANEL_COLOR = 0xC0181828;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int TEXT_DIM = 0xFF888888;

    @Override
    public void initGui() {
        super.initGui();

        // Center menu
        ScaledResolution sr = new ScaledResolution(mc);
        menuX = (sr.getScaledWidth() - menuWidth) / 2;
        menuY = (sr.getScaledHeight() - menuHeight) / 2;

        animation = 0;
        closing = false;

        updateModulePanels();
    }

    private void updateModulePanels() {
        modulePanels.clear();

        List<Module> modules;
        if (!searchQuery.isEmpty()) {
            // Search all modules
            modules = new ArrayList<>();
            for (Module m : CheetahPvP.INSTANCE.getModuleManager().getModules()) {
                if (m.getName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        m.getDescription().toLowerCase().contains(searchQuery.toLowerCase())) {
                    modules.add(m);
                }
            }
        } else {
            modules = CheetahPvP.INSTANCE.getModuleManager().getModulesByCategory(selectedCategory);
        }

        int y = 0;
        for (Module module : modules) {
            modulePanels.add(new ModulePanel(module, y));
            y += 52;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Update animation
        if (closing) {
            animation -= 0.15f;
            if (animation <= 0) {
                mc.displayGuiScreen(null);
                return;
            }
        } else {
            animation += (targetAnimation - animation) * 0.2f;
        }

        // Handle dragging
        if (dragging) {
            menuX = mouseX - dragOffsetX;
            menuY = mouseY - dragOffsetY;
        }

        GL11.glPushMatrix();

        // Scale animation
        float scale = 0.8f + 0.2f * animation;
        float centerX = menuX + menuWidth / 2f;
        float centerY = menuY + menuHeight / 2f;

        GL11.glTranslatef(centerX, centerY, 0);
        GL11.glScalef(scale, scale, 1);
        GL11.glTranslatef(-centerX, -centerY, 0);

        // Apply opacity
        float alpha = animation;

        // Draw background with rounded corners
        drawBackground(alpha);

        // Draw title bar
        drawTitleBar(mouseX, mouseY, alpha);

        // Draw category tabs
        drawCategoryTabs(mouseX, mouseY, alpha);

        // Draw search bar
        drawSearchBar(alpha);

        // Draw module panels
        drawModulePanels(mouseX, mouseY, alpha);

        GL11.glPopMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawBackground(float alpha) {
        int color = withOpacity(BG_COLOR, alpha);
        RenderUtils.drawRoundedRect(menuX, menuY, menuWidth, menuHeight, 10, color);

        // Border
        int borderColor = withOpacity(0x40FFFFFF, alpha);
        // Simple border effect by drawing slightly larger rect behind
    }

    private void drawTitleBar(int mouseX, int mouseY, float alpha) {
        // Title
        String title = "⚡ CheetahPvP";
        int titleColor = withOpacity(ACCENT_COLOR, alpha);
        mc.fontRendererObj.drawStringWithShadow(title, menuX + 15, menuY + 12, titleColor);

        // Version
        String version = "v" + CheetahPvP.VERSION;
        mc.fontRendererObj.drawStringWithShadow(version, menuX + 15 + mc.fontRendererObj.getStringWidth(title) + 10,
                menuY + 12, withOpacity(TEXT_DIM, alpha));

        // Close button
        int closeX = menuX + menuWidth - 25;
        int closeY = menuY + 8;
        boolean closeHover = mouseX >= closeX && mouseX <= closeX + 15 && mouseY >= closeY && mouseY <= closeY + 15;
        int closeColor = withOpacity(closeHover ? 0xFFE74C3C : TEXT_DIM, alpha);
        mc.fontRendererObj.drawStringWithShadow("✕", closeX + 4, closeY + 3, closeColor);
    }

    private void drawCategoryTabs(int mouseX, int mouseY, float alpha) {
        int tabY = menuY + 35;
        int tabX = menuX + 15;

        for (Module.Category category : Module.Category.values()) {
            String name = category.getDisplayName();
            int textWidth = mc.fontRendererObj.getStringWidth(name);
            int tabWidth = textWidth + 16;

            boolean hover = mouseX >= tabX && mouseX <= tabX + tabWidth && mouseY >= tabY && mouseY <= tabY + 20;
            boolean selected = category == selectedCategory;

            // Tab background
            int tabColor;
            if (selected) {
                tabColor = withOpacity(ACCENT_COLOR, alpha);
            } else if (hover) {
                tabColor = withOpacity(0x40FFFFFF, alpha);
            } else {
                tabColor = withOpacity(0x20FFFFFF, alpha);
            }

            RenderUtils.drawRoundedRect(tabX, tabY, tabWidth, 20, 5, tabColor);

            // Tab text
            int textColor = withOpacity(selected ? 0xFF000000 : TEXT_COLOR, alpha);
            mc.fontRendererObj.drawString(name, tabX + 8, tabY + 6, textColor);

            tabX += tabWidth + 5;
        }
    }

    private void drawSearchBar(float alpha) {
        int searchX = menuX + 15;
        int searchY = menuY + 62;
        int searchWidth = menuWidth - 30;
        int searchHeight = 22;

        // Background
        int bgColor = withOpacity(searchFocused ? 0x60FFFFFF : 0x30FFFFFF, alpha);
        RenderUtils.drawRoundedRect(searchX, searchY, searchWidth, searchHeight, 5, bgColor);

        // Icon
        mc.fontRendererObj.drawString("🔍", searchX + 6, searchY + 6, withOpacity(TEXT_DIM, alpha));

        // Text or placeholder
        String displayText = searchQuery.isEmpty() ? "Search modules..." : searchQuery;
        int textColor = withOpacity(searchQuery.isEmpty() ? TEXT_DIM : TEXT_COLOR, alpha);
        mc.fontRendererObj.drawString(displayText, searchX + 22, searchY + 7, textColor);

        // Cursor when focused
        if (searchFocused && System.currentTimeMillis() % 1000 < 500) {
            int cursorX = searchX + 22 + mc.fontRendererObj.getStringWidth(searchQuery);
            mc.fontRendererObj.drawString("|", cursorX, searchY + 7, withOpacity(TEXT_COLOR, alpha));
        }
    }

    private void drawModulePanels(int mouseX, int mouseY, float alpha) {
        int panelAreaX = menuX + 15;
        int panelAreaY = menuY + 92;
        int panelAreaWidth = menuWidth - 30;
        int panelAreaHeight = menuHeight - 107;

        // Clip to panel area
        RenderUtils.startScissor(panelAreaX, panelAreaY, panelAreaWidth, panelAreaHeight);

        for (ModulePanel panel : modulePanels) {
            int panelY = panelAreaY + panel.yOffset - scrollOffset;

            // Skip if outside visible area
            if (panelY + 48 < panelAreaY || panelY > panelAreaY + panelAreaHeight)
                continue;

            panel.draw(panelAreaX, panelY, panelAreaWidth, mouseX, mouseY, alpha);
        }

        RenderUtils.endScissor();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RSHIFT) {
            closing = true;
            return;
        }

        if (searchFocused) {
            if (keyCode == Keyboard.KEY_BACK && searchQuery.length() > 0) {
                searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
                updateModulePanels();
            } else if (Character.isLetterOrDigit(typedChar) || typedChar == ' ') {
                searchQuery += typedChar;
                updateModulePanels();
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // Title bar dragging
        if (mouseY >= menuY && mouseY <= menuY + 30 && mouseX >= menuX && mouseX <= menuX + menuWidth - 30) {
            dragging = true;
            dragOffsetX = mouseX - menuX;
            dragOffsetY = mouseY - menuY;
        }

        // Close button
        int closeX = menuX + menuWidth - 25;
        int closeY = menuY + 8;
        if (mouseX >= closeX && mouseX <= closeX + 15 && mouseY >= closeY && mouseY <= closeY + 15) {
            closing = true;
            return;
        }

        // Category tabs
        int tabY = menuY + 35;
        int tabX = menuX + 15;
        for (Module.Category category : Module.Category.values()) {
            int tabWidth = mc.fontRendererObj.getStringWidth(category.getDisplayName()) + 16;
            if (mouseX >= tabX && mouseX <= tabX + tabWidth && mouseY >= tabY && mouseY <= tabY + 20) {
                selectedCategory = category;
                scrollOffset = 0;
                searchQuery = "";
                updateModulePanels();
                return;
            }
            tabX += tabWidth + 5;
        }

        // Search bar
        int searchX = menuX + 15;
        int searchY = menuY + 62;
        searchFocused = mouseX >= searchX && mouseX <= searchX + menuWidth - 30 &&
                mouseY >= searchY && mouseY <= searchY + 22;

        // Module panels
        int panelAreaY = menuY + 92;
        for (ModulePanel panel : modulePanels) {
            int panelY = panelAreaY + panel.yOffset - scrollOffset;
            if (panel.handleClick(menuX + 15, panelY, menuWidth - 30, mouseX, mouseY, mouseButton)) {
                return;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            int maxScroll = Math.max(0, modulePanels.size() * 52 - (menuHeight - 107));
            scrollOffset -= scroll / 5;
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private int withOpacity(int color, float opacity) {
        int alpha = (int) ((color >> 24 & 0xFF) * opacity);
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    /**
     * Module panel for rendering individual modules in the menu
     */
    private class ModulePanel {
        Module module;
        int yOffset;
        float hoverAnimation = 0;

        ModulePanel(Module module, int yOffset) {
            this.module = module;
            this.yOffset = yOffset;
        }

        void draw(int x, int y, int width, int mouseX, int mouseY, float alpha) {
            boolean hover = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 48;

            // Hover animation
            if (hover) {
                hoverAnimation += (1 - hoverAnimation) * 0.3f;
            } else {
                hoverAnimation *= 0.8f;
            }

            // Panel background
            int bgColor = withOpacity(PANEL_COLOR, alpha);
            if (hoverAnimation > 0.01f) {
                bgColor = blendColors(bgColor, withOpacity(0x40FFFFFF, alpha), hoverAnimation * 0.3f);
            }
            RenderUtils.drawRoundedRect(x, y, width, 48, 6, bgColor);

            // Enabled indicator
            int indicatorColor = withOpacity(module.isEnabled() ? 0xFF2ECC71 : 0xFF888888, alpha);
            RenderUtils.drawRoundedRect(x + 8, y + 18, 4, 12, 2, indicatorColor);

            // Module name
            int nameColor = withOpacity(module.isEnabled() ? ACCENT_COLOR : TEXT_COLOR, alpha);
            mc.fontRendererObj.drawStringWithShadow(module.getName(), x + 20, y + 10, nameColor);

            // Description
            mc.fontRendererObj.drawString(module.getDescription(), x + 20, y + 24, withOpacity(TEXT_DIM, alpha));

            // Keybind
            String keyName = module.getKeyBindName();
            int keyWidth = mc.fontRendererObj.getStringWidth(keyName) + 10;
            int keyX = x + width - keyWidth - 55;
            RenderUtils.drawRoundedRect(keyX, y + 14, keyWidth, 18, 4, withOpacity(0x30FFFFFF, alpha));
            mc.fontRendererObj.drawString(keyName, keyX + 5, y + 18, withOpacity(TEXT_DIM, alpha));

            // Toggle button
            drawToggleButton(x + width - 48, y + 14, module.isEnabled(), alpha);
        }

        void drawToggleButton(int x, int y, boolean enabled, float alpha) {
            int width = 40;
            int height = 18;

            int bgColor = withOpacity(enabled ? 0xFF2ECC71 : 0xFF555555, alpha);
            RenderUtils.drawRoundedRect(x, y, width, height, 9, bgColor);

            // Knob
            int knobX = enabled ? x + width - 16 : x + 2;
            RenderUtils.drawRoundedRect(knobX, y + 2, 14, 14, 7, withOpacity(0xFFFFFFFF, alpha));
        }

        boolean handleClick(int x, int y, int width, int mouseX, int mouseY, int button) {
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 48) {
                // Toggle button area
                if (mouseX >= x + width - 48) {
                    module.toggle();
                    return true;
                }
            }
            return false;
        }

        private int blendColors(int color1, int color2, float ratio) {
            int a1 = (color1 >> 24) & 0xFF;
            int r1 = (color1 >> 16) & 0xFF;
            int g1 = (color1 >> 8) & 0xFF;
            int b1 = color1 & 0xFF;

            int a2 = (color2 >> 24) & 0xFF;
            int r2 = (color2 >> 16) & 0xFF;
            int g2 = (color2 >> 8) & 0xFF;
            int b2 = color2 & 0xFF;

            int a = (int) (a1 + (a2 - a1) * ratio);
            int r = (int) (r1 + (r2 - r1) * ratio);
            int g = (int) (g1 + (g2 - g1) * ratio);
            int b = (int) (b1 + (b2 - b1) * ratio);

            return (a << 24) | (r << 16) | (g << 8) | b;
        }
    }
}

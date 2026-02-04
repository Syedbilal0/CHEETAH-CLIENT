package com.cheetahpvp.client.ui;

import com.cheetahpvp.client.CheetahPvP;
import com.cheetahpvp.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Manages HUD element rendering
 */
public class HUDRenderer {

    private boolean editMode = false;
    private Module selectedModule = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null)
            return;

        // Don't render HUD elements in the mod menu
        if (mc.currentScreen instanceof GuiModMenu)
            return;

        // Render all enabled HUD modules
        for (Module module : CheetahPvP.INSTANCE.getModuleManager().getModules()) {
            if (module.isEnabled() && module.getCategory() == Module.Category.HUD) {
                try {
                    module.onRenderHUD();
                } catch (Exception e) {
                    CheetahPvP.LOGGER.error("Error rendering HUD: " + module.getName(), e);
                }
            }
        }

        // Also render non-HUD modules that have HUD components
        for (Module module : CheetahPvP.INSTANCE.getModuleManager().getModules()) {
            if (module.isEnabled() && module.getCategory() != Module.Category.HUD) {
                try {
                    module.onRenderHUD();
                } catch (Exception e) {
                    // Ignore - not all modules have HUD rendering
                }
            }
        }
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public Module getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(Module module) {
        this.selectedModule = module;
    }

    public void startDragging(Module module, int mouseX, int mouseY) {
        this.selectedModule = module;
        this.dragOffsetX = mouseX - module.getPosX();
        this.dragOffsetY = mouseY - module.getPosY();
    }

    public void updateDragging(int mouseX, int mouseY) {
        if (selectedModule != null) {
            selectedModule.setPosX(mouseX - dragOffsetX);
            selectedModule.setPosY(mouseY - dragOffsetY);
        }
    }

    public void stopDragging() {
        selectedModule = null;
    }
}

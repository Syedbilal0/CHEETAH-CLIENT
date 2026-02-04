package com.cheetahpvp.client.event;

import com.cheetahpvp.client.CheetahPvP;
import com.cheetahpvp.client.ui.GuiModMenu;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Handles keyboard input for opening mod menu
 */
public class KeyboardHandler {

    private static final int MENU_KEY = Keyboard.KEY_RSHIFT;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState())
            return;

        int key = Keyboard.getEventKey();
        Minecraft mc = Minecraft.getMinecraft();

        // Open mod menu with Right Shift
        if (key == MENU_KEY && mc.theWorld != null && mc.currentScreen == null) {
            mc.displayGuiScreen(new GuiModMenu());
        }
    }
}

package com.cheetahpvp.client.module;

import com.cheetahpvp.client.CheetahPvP;
import com.cheetahpvp.client.module.impl.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Central registry and manager for all client modules
 */
public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();
    private final Map<Module.Category, List<Module>> categoryMap = new HashMap<>();

    public void init() {
        // HUD Modules
        register(new FPSModule());
        register(new CPSModule());
        register(new KeystrokesModule());
        register(new ArmorHUDModule());
        register(new PotionStatusModule());
        register(new PingModule());
        register(new ReachModule());
        register(new CrosshairModule());

        // Movement Modules
        register(new ToggleSprintModule());
        register(new FreelookModule());

        // Visual Modules
        register(new ZoomModule());
        register(new FullBrightModule());
        register(new OldAnimationsModule());

        // Performance Modules
        register(new FPSBoosterModule());

        CheetahPvP.LOGGER.info("Loaded {} modules", modules.size());

        // Build category map
        for (Module.Category category : Module.Category.values()) {
            categoryMap.put(category, modules.stream()
                    .filter(m -> m.getCategory() == category)
                    .collect(Collectors.toList()));
        }
    }

    private void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Module.Category category) {
        return categoryMap.getOrDefault(category, new ArrayList<>());
    }

    public Module getModule(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        return modules.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElse(null);
    }

    public List<Module> getEnabledModules() {
        return modules.stream()
                .filter(Module::isEnabled)
                .collect(Collectors.toList());
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        for (Module module : modules) {
            if (module.isEnabled()) {
                try {
                    module.onTick();
                } catch (Exception e) {
                    CheetahPvP.LOGGER.error("Error in module tick: " + module.getName(), e);
                }
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState())
            return;

        int key = Keyboard.getEventKey();

        for (Module module : modules) {
            if (module.getKeyBind() == key) {
                module.toggle();
            }
        }
    }
}

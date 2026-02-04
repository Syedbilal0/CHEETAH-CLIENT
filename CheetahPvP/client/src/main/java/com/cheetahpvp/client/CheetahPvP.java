package com.cheetahpvp.client;

import com.cheetahpvp.client.config.ConfigManager;
import com.cheetahpvp.client.module.ModuleManager;
import com.cheetahpvp.client.ui.HUDRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * CheetahPvP Client - Main Mod Class
 * A professional Minecraft PvP client with performance and competitive features
 */
@Mod(modid = CheetahPvP.MODID, name = CheetahPvP.NAME, version = CheetahPvP.VERSION)
public class CheetahPvP {

    public static final String MODID = "cheetahpvp";
    public static final String NAME = "CheetahPvP";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static CheetahPvP INSTANCE;

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    private File configDir;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private HUDRenderer hudRenderer;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("CheetahPvP Pre-Initialization...");

        // Setup config directory
        configDir = new File(event.getModConfigurationDirectory(), MODID);
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        // Initialize config manager
        configManager = new ConfigManager(configDir);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("CheetahPvP Initialization...");

        // Initialize module manager
        moduleManager = new ModuleManager();
        moduleManager.init();

        // Initialize HUD renderer
        hudRenderer = new HUDRenderer();

        // Register event handlers
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(moduleManager);
        MinecraftForge.EVENT_BUS.register(hudRenderer);
        MinecraftForge.EVENT_BUS.register(new com.cheetahpvp.client.event.KeyboardHandler());

        LOGGER.info("Registered {} modules", moduleManager.getModules().size());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("CheetahPvP Post-Initialization complete!");
        LOGGER.info("Press RIGHT-SHIFT to open the mod menu");
    }

    public File getConfigDir() {
        return configDir;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public HUDRenderer getHudRenderer() {
        return hudRenderer;
    }
}

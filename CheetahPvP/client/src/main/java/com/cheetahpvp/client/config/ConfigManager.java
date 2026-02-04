package com.cheetahpvp.client.config;

import com.cheetahpvp.client.CheetahPvP;
import com.cheetahpvp.client.module.Module;
import com.google.gson.*;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Configuration manager for saving/loading module settings
 */
public class ConfigManager {

    private final File configDir;
    private final File modulesFile;
    private final Gson gson;

    public ConfigManager(File configDir) {
        this.configDir = configDir;
        this.modulesFile = new File(configDir, "modules.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Save all module settings to file
     */
    public void saveConfig() {
        try {
            JsonObject root = new JsonObject();

            for (Module module : CheetahPvP.INSTANCE.getModuleManager().getModules()) {
                JsonObject moduleObj = new JsonObject();

                moduleObj.addProperty("enabled", module.isEnabled());
                moduleObj.addProperty("keyBind", module.getKeyBind());
                moduleObj.addProperty("posX", module.getPosX());
                moduleObj.addProperty("posY", module.getPosY());
                moduleObj.addProperty("scale", module.getScale());
                moduleObj.addProperty("opacity", module.getOpacity());

                // Save color
                Color color = module.getColor();
                JsonObject colorObj = new JsonObject();
                colorObj.addProperty("r", color.getRed());
                colorObj.addProperty("g", color.getGreen());
                colorObj.addProperty("b", color.getBlue());
                moduleObj.add("color", colorObj);

                root.add(module.getName(), moduleObj);
            }

            try (FileWriter writer = new FileWriter(modulesFile)) {
                gson.toJson(root, writer);
            }

            CheetahPvP.LOGGER.info("Configuration saved");

        } catch (Exception e) {
            CheetahPvP.LOGGER.error("Failed to save configuration", e);
        }
    }

    /**
     * Load all module settings from file
     */
    public void loadConfig() {
        if (!modulesFile.exists())
            return;

        try (FileReader reader = new FileReader(modulesFile)) {
            JsonObject root = gson.fromJson(reader, JsonObject.class);
            if (root == null)
                return;

            for (Module module : CheetahPvP.INSTANCE.getModuleManager().getModules()) {
                if (!root.has(module.getName()))
                    continue;

                JsonObject moduleObj = root.getAsJsonObject(module.getName());

                if (moduleObj.has("enabled")) {
                    module.setEnabled(moduleObj.get("enabled").getAsBoolean());
                }
                if (moduleObj.has("keyBind")) {
                    module.setKeyBind(moduleObj.get("keyBind").getAsInt());
                }
                if (moduleObj.has("posX")) {
                    module.setPosX(moduleObj.get("posX").getAsInt());
                }
                if (moduleObj.has("posY")) {
                    module.setPosY(moduleObj.get("posY").getAsInt());
                }
                if (moduleObj.has("scale")) {
                    module.setScale(moduleObj.get("scale").getAsFloat());
                }
                if (moduleObj.has("opacity")) {
                    module.setOpacity(moduleObj.get("opacity").getAsFloat());
                }

                // Load color
                if (moduleObj.has("color")) {
                    JsonObject colorObj = moduleObj.getAsJsonObject("color");
                    int r = colorObj.get("r").getAsInt();
                    int g = colorObj.get("g").getAsInt();
                    int b = colorObj.get("b").getAsInt();
                    module.setColor(new Color(r, g, b));
                }
            }

            CheetahPvP.LOGGER.info("Configuration loaded");

        } catch (Exception e) {
            CheetahPvP.LOGGER.error("Failed to load configuration", e);
        }
    }
}

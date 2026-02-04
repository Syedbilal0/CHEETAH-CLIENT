package com.cheetahpvp.launcher.launch;

import com.cheetahpvp.launcher.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles launching Minecraft with the CheetahPvP client
 */
public class GameLauncher {

    private static final String MINECRAFT_DIR = System.getProperty("user.home") + "/.minecraft";

    public static void launch(String username, String version) {
        try {
            System.out.println("Launching Minecraft " + version + " as " + username);

            List<String> command = new ArrayList<>();

            // Find Java
            String javaPath = findJava();
            command.add(javaPath);

            // JVM Arguments
            command.add("-Xmx2G");
            command.add("-Xms512M");
            command.add("-XX:+UseG1GC");
            command.add("-XX:+UnlockExperimentalVMOptions");
            command.add("-XX:G1NewSizePercent=20");
            command.add("-XX:G1ReservePercent=20");
            command.add("-XX:MaxGCPauseMillis=50");
            command.add("-XX:G1HeapRegionSize=32M");

            // Natives path
            String nativesPath = MINECRAFT_DIR + "/versions/" + version + "/natives";
            command.add("-Djava.library.path=" + nativesPath);

            // Classpath
            command.add("-cp");
            command.add(buildClasspath(version));

            // Main class
            command.add("net.minecraft.client.main.Main");

            // Game arguments
            command.add("--username");
            command.add(username);
            command.add("--version");
            command.add(version);
            command.add("--gameDir");
            command.add(MINECRAFT_DIR);
            command.add("--assetsDir");
            command.add(MINECRAFT_DIR + "/assets");
            command.add("--assetIndex");
            command.add(getAssetIndex(version));
            command.add("--accessToken");
            command.add("0");
            command.add("--userType");
            command.add("legacy");

            // Build and start process
            ProcessBuilder pb = new ProcessBuilder(command);
            File workingDir = new File(MINECRAFT_DIR);
            if (workingDir.exists()) {
                pb.directory(workingDir);
            } else {
                System.out.println("Warning: .minecraft directory not found, using current directory");
                workingDir.mkdirs(); // Try creating it
                pb.directory(workingDir);
            }
            pb.inheritIO();
            pb.start();

            System.out.println("Game process started!");

        } catch (Exception e) {
            System.err.println("Failed to launch game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String findJava() {
        String javaHome = System.getProperty("java.home");
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return javaHome + "/bin/javaw.exe";
        } else {
            return javaHome + "/bin/java";
        }
    }

    private static String buildClasspath(String version) {
        StringBuilder cp = new StringBuilder();
        String separator = System.getProperty("os.name").toLowerCase().contains("win") ? ";" : ":";

        // Add version jar
        cp.append(MINECRAFT_DIR).append("/versions/").append(version).append("/").append(version).append(".jar");

        // Add libraries (simplified - in production, parse version.json)
        File librariesDir = new File(MINECRAFT_DIR + "/libraries");
        if (librariesDir.exists()) {
            addJarsRecursively(librariesDir, cp, separator);
        }

        // Add CheetahPvP client mod
        File clientMod = new File(Main.DATA_DIR, "mods/CheetahPvP-" + Main.VERSION + ".jar");
        if (clientMod.exists()) {
            cp.append(separator).append(clientMod.getAbsolutePath());
        }

        return cp.toString();
    }

    private static void addJarsRecursively(File dir, StringBuilder cp, String separator) {
        File[] files = dir.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                addJarsRecursively(file, cp, separator);
            } else if (file.getName().endsWith(".jar")) {
                cp.append(separator).append(file.getAbsolutePath());
            }
        }
    }

    private static String getAssetIndex(String version) {
        // Map version to asset index
        if (version.startsWith("1.7"))
            return "1.7.10";
        if (version.startsWith("1.8"))
            return "1.8";
        if (version.startsWith("1.12"))
            return "1.12";
        if (version.startsWith("1.16"))
            return "1.16";
        if (version.startsWith("1.19"))
            return "1.19";
        return "1.8";
    }
}

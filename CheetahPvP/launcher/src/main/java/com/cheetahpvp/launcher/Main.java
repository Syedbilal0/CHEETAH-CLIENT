package com.cheetahpvp.launcher;

import com.cheetahpvp.launcher.ui.MainWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

/**
 * CheetahPvP Launcher - Main Entry Point
 * A modern Minecraft PvP client launcher with offline/cracked account support
 */
public class Main extends Application {

    public static final String APP_NAME = "CheetahPvP";
    public static final String VERSION = "1.0.0";
    public static File DATA_DIR;

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        // Setup data directory
        DATA_DIR = new File(System.getProperty("user.home"), ".cheetahpvp");
        if (!DATA_DIR.exists()) {
            DATA_DIR.mkdirs();
        }

        // Create main window
        MainWindow mainWindow = new MainWindow();
        Scene scene = new Scene(mainWindow, 1000, 650);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/styles/dark-theme.css").toExternalForm());

        // Configure stage
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(APP_NAME + " Launcher");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Center on screen
        stage.centerOnScreen();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

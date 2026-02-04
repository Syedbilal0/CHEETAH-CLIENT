package com.cheetahpvp.launcher.ui;

import com.cheetahpvp.launcher.Main;
import com.cheetahpvp.launcher.account.Account;
import com.cheetahpvp.launcher.account.AccountManager;
import com.cheetahpvp.launcher.launch.GameLauncher;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Main launcher window with modern UI
 */
public class MainWindow extends BorderPane {

    private double xOffset = 0;
    private double yOffset = 0;

    private TextField usernameField;
    private ComboBox<String> versionSelector;
    private ComboBox<Account> accountSelector;
    private Label statusLabel;
    private Button playButton;
    private ProgressBar progressBar;

    public MainWindow() {
        getStyleClass().add("main-window");

        // Make window draggable
        setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        setOnMouseDragged(e -> {
            Main.getPrimaryStage().setX(e.getScreenX() - xOffset);
            Main.getPrimaryStage().setY(e.getScreenY() - yOffset);
        });

        // Rounded corners
        Rectangle clip = new Rectangle(1000, 650);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        setClip(clip);

        // Drop shadow effect
        setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.5)));

        // Build UI
        setTop(createTitleBar());
        setCenter(createMainContent());
        setBottom(createStatusBar());

        // Entrance animation
        playEntranceAnimation();
    }

    private HBox createTitleBar() {
        HBox titleBar = new HBox();
        titleBar.getStyleClass().add("title-bar");
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(10, 15, 10, 15));
        titleBar.setSpacing(10);

        // Logo/Title
        Label title = new Label("⚡ " + Main.APP_NAME);
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        Label version = new Label("v" + Main.VERSION);
        version.getStyleClass().add("version-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Window controls
        Button minimizeBtn = createWindowButton("—", () -> Main.getPrimaryStage().setIconified(true));
        Button closeBtn = createWindowButton("✕", () -> System.exit(0));
        closeBtn.getStyleClass().add("close-button");

        titleBar.getChildren().addAll(title, version, spacer, minimizeBtn, closeBtn);
        return titleBar;
    }

    private Button createWindowButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.getStyleClass().add("window-button");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private VBox createMainContent() {
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("main-content");

        // Large logo text
        Label logoLabel = new Label("CHEETAH");
        logoLabel.getStyleClass().add("logo-text");
        logoLabel.setFont(Font.font("Segoe UI", FontWeight.BLACK, 72));

        Label subLabel = new Label("P V P   C L I E N T");
        subLabel.getStyleClass().add("sub-logo-text");

        // Account section
        VBox accountBox = createAccountSection();

        // Version selector
        HBox versionBox = createVersionSection();

        // Play button
        playButton = new Button("▶  PLAY");
        playButton.getStyleClass().add("play-button");
        playButton.setPrefSize(280, 60);
        playButton.setOnAction(e -> launchGame());

        // Add hover animation to play button
        playButton.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), playButton);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        playButton.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), playButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        // Progress bar (hidden by default)
        progressBar = new ProgressBar(0);
        progressBar.getStyleClass().add("launch-progress");
        progressBar.setPrefWidth(280);
        progressBar.setVisible(false);

        content.getChildren().addAll(logoLabel, subLabel, accountBox, versionBox, playButton, progressBar);
        return content;
    }

    private VBox createAccountSection() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(350);

        Label accountLabel = new Label("Account");
        accountLabel.getStyleClass().add("section-label");

        // Account type tabs
        HBox tabs = new HBox(0);
        tabs.setAlignment(Pos.CENTER);

        ToggleGroup accountType = new ToggleGroup();
        ToggleButton offlineTab = new ToggleButton("Offline / Cracked");
        offlineTab.setToggleGroup(accountType);
        offlineTab.setSelected(true);
        offlineTab.getStyleClass().add("tab-button");

        ToggleButton premiumTab = new ToggleButton("Premium");
        premiumTab.setToggleGroup(accountType);
        premiumTab.getStyleClass().add("tab-button");

        tabs.getChildren().addAll(offlineTab, premiumTab);

        // Username field for offline
        usernameField = new TextField();
        usernameField.setPromptText("Enter username...");
        usernameField.getStyleClass().add("text-input");
        usernameField.setMaxWidth(300);

        // Account selector (for saved accounts)
        accountSelector = new ComboBox<>();
        accountSelector.setPromptText("Or select saved account");
        accountSelector.getStyleClass().add("combo-input");
        accountSelector.setMaxWidth(300);
        accountSelector.setVisible(false);

        // Load saved accounts
        loadAccounts();

        // Save account button
        Button saveAccountBtn = new Button("+ Save Account");
        saveAccountBtn.getStyleClass().add("secondary-button");
        saveAccountBtn.setOnAction(e -> saveAccount());

        box.getChildren().addAll(accountLabel, tabs, usernameField, accountSelector, saveAccountBtn);

        // Toggle visibility based on tab
        offlineTab.setOnAction(e -> {
            usernameField.setVisible(true);
            usernameField.setManaged(true);
        });

        return box;
    }

    private HBox createVersionSection() {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER);

        Label versionLabel = new Label("Version:");
        versionLabel.getStyleClass().add("section-label");

        versionSelector = new ComboBox<>();
        versionSelector.getStyleClass().add("combo-input");
        versionSelector.getItems().addAll(
                "1.8.9 (Recommended)",
                "1.7.10",
                "1.12.2",
                "1.16.5",
                "1.19.4");
        versionSelector.setValue("1.8.9 (Recommended)");
        versionSelector.setPrefWidth(200);

        box.getChildren().addAll(versionLabel, versionSelector);
        return box;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.getStyleClass().add("status-bar");
        statusBar.setPadding(new Insets(10, 20, 10, 20));
        statusBar.setAlignment(Pos.CENTER_LEFT);

        statusLabel = new Label("Ready to play");
        statusLabel.getStyleClass().add("status-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label creditsLabel = new Label("CheetahPvP © 2024");
        creditsLabel.getStyleClass().add("credits-text");

        statusBar.getChildren().addAll(statusLabel, spacer, creditsLabel);
        return statusBar;
    }

    private void loadAccounts() {
        accountSelector.getItems().clear();
        for (Account account : AccountManager.getInstance().getAccounts()) {
            accountSelector.getItems().add(account);
        }
    }

    private void saveAccount() {
        String username = usernameField.getText().trim();
        if (!username.isEmpty()) {
            Account account = new Account(username, Account.Type.OFFLINE);
            AccountManager.getInstance().addAccount(account);
            loadAccounts();
            statusLabel.setText("Account saved: " + username);
        }
    }

    private void launchGame() {
        String username = usernameField.getText().trim();

        if (username.isEmpty() && accountSelector.getValue() == null) {
            statusLabel.setText("Please enter a username!");
            shakeField(usernameField);
            return;
        }

        if (username.isEmpty() && accountSelector.getValue() != null) {
            username = accountSelector.getValue().getUsername();
        }

        // Show progress
        playButton.setDisable(true);
        progressBar.setVisible(true);
        statusLabel.setText("Launching " + versionSelector.getValue() + "...");

        // Animate progress
        Timeline progressAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(progressBar.progressProperty(), 1)));

        String finalUsername = username;
        progressAnim.setOnFinished(e -> {
            GameLauncher.launch(finalUsername, getSelectedVersion());
            statusLabel.setText("Game launched!");
            playButton.setDisable(false);
            progressBar.setVisible(false);
        });
        progressAnim.play();
    }

    private String getSelectedVersion() {
        String selected = versionSelector.getValue();
        if (selected.contains("1.8.9"))
            return "1.8.9";
        if (selected.contains("1.7.10"))
            return "1.7.10";
        if (selected.contains("1.12.2"))
            return "1.12.2";
        if (selected.contains("1.16.5"))
            return "1.16.5";
        if (selected.contains("1.19.4"))
            return "1.19.4";
        return "1.8.9";
    }

    private void shakeField(TextField field) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), field);
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }

    private void playEntranceAnimation() {
        setOpacity(0);
        setScaleX(0.9);
        setScaleY(0.9);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(opacityProperty(), 0),
                        new KeyValue(scaleXProperty(), 0.9),
                        new KeyValue(scaleYProperty(), 0.9)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(opacityProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(scaleXProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(scaleYProperty(), 1, Interpolator.EASE_OUT)));
        timeline.play();
    }
}

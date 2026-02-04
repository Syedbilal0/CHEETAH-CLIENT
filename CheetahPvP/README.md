# CheetahPvP Client

A professional Minecraft PvP client with custom launcher, inspired by Badlion Client.

![Version](https://img.shields.io/badge/version-1.0.0-orange)
![Minecraft](https://img.shields.io/badge/Minecraft-1.8.9-green)
![License](https://img.shields.io/badge/license-MIT-blue)

## ⚡ Features

### Launcher
- 🎨 Modern dark-themed UI
- 👤 Offline/Cracked account support
- 📦 Version selector (1.7.10, 1.8.9, 1.12.2+)
- 💾 Profile management
- 🚀 Optimized JVM arguments

### Client Mods

| Module | Key | Description |
|--------|-----|-------------|
| **Zoom** | C | OptiFine-style zoom with scroll wheel |
| **Freelook** | LAlt | 360° camera without moving |
| **Toggle Sprint** | R | Auto/toggle sprint |
| **Keystrokes** | - | WASD + mouse button display |
| **FPS Counter** | - | Color-coded FPS display |
| **CPS Counter** | - | Clicks per second |
| **Armor HUD** | - | Durability bars |
| **Potion Status** | - | Active effects |
| **Ping Display** | - | Server latency |
| **Crosshair** | - | Custom crosshair styles |
| **FullBright** | B | See in darkness |
| **1.7 Animations** | O | Classic PvP animations |
| **FPS Boost** | - | Performance optimizations |

### Mod Menu
- Press **Right Shift** to open
- Smooth animations
- Draggable window
- Category tabs
- Search bar
- Toggle buttons

## 🚀 Quick Start

### Requirements
- JDK 17 (for Launcher)
- JDK 8 (for Client/Forge)
- Minecraft 1.8.9 installed

### First Time Setup
```bash
# Run the setup script
START_SETUP.bat
```

### Running
```bash
# Run the launcher
RUN_LAUNCHER.bat

# Or test the mod directly
RUN_CLIENT_DEBUG.bat
```

### Building Release
```bash
BUILD_RELEASE.bat
```

## 📁 Project Structure

```
CheetahPvP/
├── launcher/                    # JavaFX Launcher
│   └── src/main/java/
│       └── com/cheetahpvp/launcher/
│           ├── Main.java        # Entry point
│           ├── ui/              # UI components
│           ├── account/         # Account management
│           └── launch/          # Game launcher
│
├── client/                      # Forge 1.8.9 Mod
│   └── src/main/java/
│       └── com/cheetahpvp/client/
│           ├── CheetahPvP.java  # Main mod class
│           ├── module/          # Module system
│           │   └── impl/        # All modules
│           ├── ui/              # Mod menu & HUD
│           ├── config/          # Settings
│           ├── event/           # Event handlers
│           └── util/            # Utilities
│
├── START_SETUP.bat              # First-time setup
├── RUN_LAUNCHER.bat             # Run launcher
├── RUN_CLIENT_DEBUG.bat         # Test in dev mode
└── BUILD_RELEASE.bat            # Build JARs
```

## 🎮 Controls

| Key | Action |
|-----|--------|
| **Right Shift** | Open mod menu |
| **C** | Zoom (hold) |
| **Left Alt** | Freelook (hold) |
| **R** | Toggle Sprint |
| **B** | Toggle FullBright |
| **O** | Toggle 1.7 Animations |

## ⚙️ Customization

Each module supports:
- **Color picker** - Custom colors
- **Scale slider** - 0.5x to 3.0x
- **Opacity control** - 0-100%
- **Position editor** - Drag or set coordinates
- **Keybind** - Customize activation key

## 🔧 Mod API

### Creating a New Module

```java
package com.cheetahpvp.client.module.impl;

import com.cheetahpvp.client.module.Module;

public class MyModule extends Module {
    
    public MyModule() {
        super("My Module", "Description", Category.HUD);
    }
    
    @Override
    public void onEnable() {
        // Called when enabled
    }
    
    @Override
    public void onTick() {
        // Called every game tick
    }
    
    @Override
    public void onRenderHUD() {
        // Called for HUD rendering
    }
}
```

Register in `ModuleManager.java`:
```java
register(new MyModule());
```

## 📊 Performance

- **Entity Culling**: Reduces render load by 30%
- **Background FPS Limit**: Saves resources when alt-tabbed
- **Particle Reduction**: Lighter particle effects
- **Smart Rendering**: Only renders visible entities

## 🛡️ Security

- No external API calls
- Local config storage
- No data collection
- Open source

## 📝 License

MIT License - Feel free to modify and distribute.

---

**CheetahPvP** - Built for competitive Minecraft PvP ⚡

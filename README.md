# Lumberjack — Spigot Plugin for Minecraft 1.21.11
![Spigot](https://img.shields.io/badge/Spigot-Plugin-orange?style=for-the-badge)
![Minecraft](https://img.shields.io/badge/Minecraft-Java%20Edition-3C8527?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-100%25-blue?style=for-the-badge&logo=openjdk&logoColor=white)
![Status](https://img.shields.io/badge/Status-Active-success?style=for-the-badge)


A lightweight Spigot plugin for Minecraft that removes the tedium of chopping down trees one block at a time. Break any log block and the entire connected tree is automatically chopped down — no more punching logs one by one.

---
## Features

- **Instant tree chopping** — breaking one log block removes the full connected tree
- **Full log support** — works on connected log blocks in natural trees
- **Creative mode support** — in Creative, the tree is removed without drops
- **Zero configuration** — no config files or commands needed; just drop in the jar and go.
---
## Requirements

- **Minecraft / Spigot:** 1.21.1
- **Java:** 21+
- **API:** Spigot API `1.21.11
---
## Installation

1. Build the plugin (see [Building](#building)) or download the latest release jar.
2. Place the `.jar` file into your server's `plugins/` folder.
3. Start or reload your server.
4. No further configuration required.
---
## Building

This project uses Maven. To build from source:

```bash
git clone https://github.com/USMCsky/Lumberjack_Spigot_Plugin.git
cd Lumberjack_Spigot_Plugin
mvn clean package
```

The compiled jar will be output to the `target/` directory.
---
## How It Works

When a player breaks a log block, the plugin performs a **breadth-first search** across connected log blocks to find every part of the tree. All connected logs are then removed automatically.

In **Creative mode**, the tree is cleared without generating drops.
---
## Plugin Info

| Field   | Value |
|---------|-------|
| Name    | Lumberjack |
| Version | 1.21.1 |
| Author  | USMCsky |
| Main    | `com.usmcsky.lumberjack.Lumberjack` |

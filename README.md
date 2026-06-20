# Lumberjack — Spigot Plugin for Minecraft 1.21.11

A lightweight Spigot plugin for Minecraft that removes the tedium of chopping down trees one block at a time. Break a log block and the plugin automatically removes the entire connected tree — making tree farming faster, cleaner, and much less repetitive.

---

## Features

- **Instant tree felling** — breaking one log block chops down the full connected tree
- **Supports natural tree shapes** — removes connected logs so you can clear trees quickly and consistently
- **Creative mode support** — in Creative, the tree is cleared without drops
- **Simple and lightweight** — built to do one job with minimal overhead
- **Zero configuration** — no commands or config files required; just install the jar and play

---

## Requirements

- **Minecraft / Spigot:** 1.21.1
- **Java:** 21+
- **API:** Spigot API `1.21.11-R0.1-SNAPSHOT`

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

When a player breaks a log block, the plugin searches the surrounding connected blocks to find the rest of the tree and removes it automatically. This makes chopping down trees fast while still feeling natural in-game.

In **Creative mode**, the tree is removed without generating drops.

---

## Plugin Info

| Field   | Value |
|---------|-------|
| Name    | Lumberjack |
| Version | 1.21.11 |
| Author  | USMCsky |
| Main    | `com.usmcsky.lumberjack.Lumberjack` |
| API     | 1.21 |

package com.altacraft.randomwelcome;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Levi Muniz on 5/3/20.
 * Copyright (c) Levi Muniz. All Rights Reserved.
 */
class PlayerStorage implements Listener {
    private final File dataFolder;
    private final HashMap<Player, Boolean> onlinePlayerSettings;

    public PlayerStorage(File dataFolder) {
        this.dataFolder = dataFolder;
        onlinePlayerSettings = new HashMap<>();
    }


    public boolean isNewcomer(Player player) {
        return !new File(dataFolder, player.getUniqueId().toString() + ".yml").exists();
    }

    public boolean isMuted(Player player) {
        if (onlinePlayerSettings.containsKey(player)) return onlinePlayerSettings.get(player);

        return loadPlayerConfig(player).getBoolean("muted");
    }

    public void setMuted(Player player, boolean muted) {
        if (!onlinePlayerSettings.containsKey(player) || onlinePlayerSettings.get(player) != muted) {
            onlinePlayerSettings.put(player, muted);
            savePlayerData(player);
        }
    }

    public void savePlayerData(Player player) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("muted", onlinePlayerSettings.get(player));

        try {
            config.save(new File(dataFolder, player.getUniqueId().toString() + ".yml"));
        } catch (IOException e) {
            RandomWelcome.logger.warning("Error saving " + player.getDisplayName() + "'s data!");
        }
    }

    public YamlConfiguration loadPlayerConfig(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId().toString() + ".yml");
        return YamlConfiguration.loadConfiguration(playerFile);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLogin(PlayerJoinEvent e) {
        RandomWelcome.greet(e.getPlayer());

        if (isNewcomer(e.getPlayer())) {
            setMuted(e.getPlayer(), false);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        onlinePlayerSettings.remove(e.getPlayer());
    }
}

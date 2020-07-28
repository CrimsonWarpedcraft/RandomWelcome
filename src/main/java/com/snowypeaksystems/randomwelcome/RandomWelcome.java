package com.snowypeaksystems.randomwelcome;

import io.papermc.lib.PaperLib;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;


/**
 * Created by Levi Muniz on 4/30/20.
 * Copyright (c) Levi Muniz. All Rights Reserved.
 */
public class RandomWelcome extends JavaPlugin {
    private static RandomWelcome plugin;
    private boolean messagesEnabled = true;
    private PlayerStorage ps;
    static Logger logger;


    @Override
    public void onEnable() {
        plugin = this;
        messagesEnabled = true;
        logger = getLogger();

        PaperLib.suggestPaper(this);

        saveDefaultConfig();

        File playerDataFolder = new File(getDataFolder(), "data/");
        if (!playerDataFolder.exists()) playerDataFolder.mkdirs();
        ps = new PlayerStorage(playerDataFolder);

        PluginCommand cmd = getCommand("randomwelcome");
        CommandHandler ch = new CommandHandler();
        cmd.setExecutor(ch);
        cmd.setTabCompleter(ch);

        getServer().getPluginManager().registerEvents(ps, this);

    }

    public static void setMessagesEnabled(boolean b) {
        plugin.messagesEnabled = b;
    }

    public static boolean getMessagesEnabled() {
        return plugin.messagesEnabled;
    }

    public static void greet(Player player) {
        greet(player, getFormattedRandomGreeting());
    }

    public static void greet(Player player, String message) {
        if (getMessagesEnabled() && canGreet(player)) {
            String[] messages;

            String rawNewcomerMessage = plugin.getConfig().getString("newcomer_message", "");
            if (isNewcomer(player) && !rawNewcomerMessage.isEmpty()) {
                messages = new String[2];
                String formattedNewcomerMessage = ChatColor.translateAlternateColorCodes('&', rawNewcomerMessage);
                messages[1] = replaceName(formattedNewcomerMessage, player.getDisplayName(), getNamePrefix());
            } else {
                messages = new String[1];
            }

            messages[0] = replaceName(message, player.getDisplayName(), getNamePrefix());

            if (!isMuted(player)) {
                player.sendMessage(messages);
            }


            if (plugin.getConfig().getBoolean("broadcast_publicly", true)) {
                Collection<? extends Player> players =  plugin.getServer().getOnlinePlayers();

                Runnable broadcast = () -> {
                    for (Player p : players) {
                        if (p != player && p.hasPermission("randomwelcome.welcome.others") && !isMuted(p)) {
                            BukkitRunnable send = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.sendMessage(messages);
                                }
                            };
                            send.runTask(plugin);
                        }
                    }
                };

                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, broadcast);
            }
        }
    }

    public static boolean canGreet(Player player) {
        if (player.hasPermission("randomwelcome.welcome")) {
            if (plugin.getConfig().getBoolean("newcomers_only", false)) {
                return isNewcomer(player);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isNewcomer(Player player) {
        return plugin.ps.isNewcomer(player);
    }

    public static boolean isMuted(Player player) {
        return plugin.ps.isMuted(player);
    }

    public static void setMuted(Player player, boolean b) {
        plugin.ps.setMuted(player, b);
    }

    public static boolean getVanilaJoinMessageEnabled() {
        return plugin.getConfig().getBoolean("vanilla_join_message", false);
    }

    public static String getNamePrefix() {
        return plugin.getConfig().getString("player_name_prefix", "");
    }

    public static String getFormattedRandomGreeting() {
        return ChatColor.translateAlternateColorCodes('&', getRandomGreeting());
    }

    public static String getRandomGreeting() {
        Random r = new Random();
        List<String> possibleMessages = plugin.getConfig().getStringList("messages");
        return possibleMessages.get(r.nextInt(possibleMessages.size()));
    }

    public static String replaceName(String message, String name, String namePrefix) {
        String lastColors = ChatColor.getLastColors(message);
        String formattedPlayername = ChatColor.translateAlternateColorCodes(
                '&', namePrefix + name + "&r") + lastColors;

        return message.replaceAll("%player%", formattedPlayername);
    }

    public static void reload() {
        plugin.reloadConfig();
    }
}

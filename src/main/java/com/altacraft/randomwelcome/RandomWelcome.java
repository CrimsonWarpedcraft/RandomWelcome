package com.altacraft.randomwelcome;

import io.papermc.lib.PaperLib;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

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
        greet(player, getGreeting(player));
    }

    public static void greet(Player player, String message) {
        if (getMessagesEnabled() && canGreet(player)) {
            String[] messages;
            String prefix = plugin.getConfig().getString("prefix");
            String newcomerMessage = ChatColor.translateAlternateColorCodes('&',
                    prefix + plugin.getConfig().getString("newcomer_message"))
                    .replaceAll("%player%", player.getDisplayName());

            if (!newcomerMessage.equals("") && isNewcomer(player)) {
                messages = new String[]{message, newcomerMessage};
            } else {
                messages = new String[]{message};
            }

            if (!isMuted(player)) {
                player.sendMessage(messages);
            }

            BukkitScheduler scheduler = plugin.getServer().getScheduler();

            if (plugin.getConfig().getBoolean("broadcast_publicly")) {
                Collection<? extends Player> players =  plugin.getServer().getOnlinePlayers();

                Runnable broadcast = () -> {
                    for (Player p : players) {
                        if (p != player && (p.hasPermission("randomwelcome.*")
                                || p.hasPermission("randomwelcome.welcome.others")) && !isMuted(p)) {
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

                scheduler.runTaskAsynchronously(plugin, broadcast);
            }
        }
    }

    public static boolean canGreet(Player player) {
        if (player.hasPermission("randomwelcome.*") || player.hasPermission("randomwelcome.welcome")) {
            if (plugin.getConfig().getBoolean("newcomers_only")) {
                return isNewcomer(player);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static String getGreeting() {
        return getGreeting("");
    }

    public static String getGreeting(Player player) {
        return getGreeting(player.getDisplayName());
    }

    public static String getGreeting(String player) {
        Random r = new Random();
        List<String> possibleMessages = plugin.getConfig().getStringList("messages");
        String prefix = plugin.getConfig().getString("prefix");
        String randomMessage = possibleMessages.get(r.nextInt(possibleMessages.size()));
        String fullMessage = prefix + randomMessage.replaceAll("%player%", player);
        return ChatColor.translateAlternateColorCodes('&', fullMessage);
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

    public static boolean getJoinMessageEnabled() {
        return plugin.getConfig().getBoolean("player_joined_message");
    }

    public static void reload() {
        plugin.reloadConfig();
    }


}

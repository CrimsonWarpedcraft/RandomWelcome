package com.altacraft.randomwelcome;

import io.papermc.lib.PaperLib;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
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
        if (getMessagesEnabled() && canGreet(player)) {
            String[] messages;
            String rawPrefix = plugin.getConfig().getString("prefix");
            String prefix = ChatColor.translateAlternateColorCodes('&', rawPrefix);
            String message = prefix + getGreeting(player);
            String newcomerMessage = plugin.getConfig().getString("newcomer_message");

            if (!newcomerMessage.equals("") && isNewcomer(player)) {
                messages = new String[]{message, prefix + newcomerMessage};
            } else {
                messages = new String[]{message};
            }

            if (!isMuted(player)) {
                player.sendMessage(messages);
            }

            if (plugin.getConfig().getBoolean("broadcast_publicly")) {
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    if (p == player) continue;

                    if ((p.hasPermission("randomwelcome.*")
                            || p.hasPermission("randomwelcome.welcome.others"))
                            && !isMuted(p)) {
                        p.sendMessage(messages);
                    }
                }
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

    public static String getGreeting(Player player) {
        Random r = new Random();
        List<String> possibleMessages = plugin.getConfig().getStringList("messages");
        String randomMessage = possibleMessages.get(r.nextInt(possibleMessages.size()))
                .replaceAll("%player%", player.getDisplayName());
        return randomMessage;
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

    public static void reload() {
        plugin.reloadConfig();
    }


}

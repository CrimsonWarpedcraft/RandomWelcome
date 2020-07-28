package com.snowypeaksystems.randomwelcome;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Levi Muniz on 5/3/20.
 * Copyright (c) Levi Muniz. All Rights Reserved.
 */
public class CommandHandler implements TabExecutor {
    private final static String[] HELP = {
            "Usage: /rw <subcommand>",
            "/rw enable - Enable random welcome messages",
            "/rw disable - Disable random welcome messages",
            "/rw mute - Stop receiving welcome messages",
            "/rw unmute - Start receiving welcome messages",
            "/rw reload - Reloads the plugin's configuration",
            "/rw help - Shows this message"
    };
    private final static String[] SUBCOMMANDS = {"enable", "disable", "mute", "unmute", "reload", "help"};


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("randomwelcome")) {
            if (args.length == 1) {
                switch (args[0]) {
                    case "enable":
                        if (sender.hasPermission("randomwelcome.enable")) {
                            RandomWelcome.setMessagesEnabled(true);
                            sender.sendMessage("Enabled random welcome messages!");
                        } else {
                            sender.sendMessage("Lacking permission randomwelcome.enable");
                        }
                        break;
                    case "disable":
                        if (sender.hasPermission("randomwelcome.disable")) {
                            RandomWelcome.setMessagesEnabled(false);
                            sender.sendMessage("Disabled random welcome messages!");
                        } else {
                            sender.sendMessage("Lacking permission randomwelcome.disable");
                        }
                        break;
                    case "mute":
                        if (sender.hasPermission("randomwelcome.mute")) {
                            if (sender instanceof Player) {
                                RandomWelcome.setMuted((Player) sender, true);
                                sender.sendMessage("Muted random welcome messages!");
                            } else {
                                sender.sendMessage("This command can only be run by in-game players!");
                            }
                        } else {
                            sender.sendMessage("Lacking permission randomwelcome.mute");
                        }
                        break;
                    case "unmute":
                        if (sender.hasPermission("randomwelcome.unmute")) {
                            if (sender instanceof Player) {
                                RandomWelcome.setMuted((Player) sender, false);
                                sender.sendMessage("Unmuted random welcome messages!");
                            } else {
                                sender.sendMessage("This command can only be run by in-game players!");
                            }
                        } else {
                            sender.sendMessage("Lacking permission randomwelcome.unmute");
                        }
                        break;
                    case "reload":
                        if (sender.hasPermission("randomwelcome.reload")) {
                            RandomWelcome.reload();
                            sender.sendMessage("Config reloaded!");
                        } else {
                            sender.sendMessage("Lacking permission randomwelcome.reload");
                        }
                        break;
                    default:
                        printHelp(sender);
                        break;
                }
            } else {
                printHelp(sender);
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        if (command.getName().equals("randomwelcome")) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList(SUBCOMMANDS), completions);
            Collections.sort(completions);
        }
        return completions;
    }

    private void printHelp(CommandSender player) {
        if (player.hasPermission("randomwelcome")) {
            player.sendMessage(HELP);
        }
    }
}

# RandomWelcome

Simple [PaperMC](https://papermc.io/)/[Spigot](https://www.spigotmc.org/wiki/about-spigot/) Minecraft server plugin to welcome players to a server by sending them a random message of your choosing!

I created this plugin to teach myself how to develop a [PaperAPI](https://github.com/PaperMC/Paper) Minecraft server plugin and compile it using the [Gradle](https://gradle.org/) build tool.

## Simple and Stable

Native Version: **1.15**  
Tested Minecraft Versions: **1.15, 1.16**

## Features

### Commands and Permissions

Below is a small table to help explain all of the RandomWelcome commands and permissions

| Command | Permission | Description |
| --- | --- | --- |
| /rw [help] | randomwelcome | RandomWelcome help command and root permission |
| /rw mute | randomwelcome.mute | Player command to mute welcome messages |
| /rw unmute | randomwelcome.unmute | Player command to unmute welcome messages |
| /rw enable | randomwelcome.enable | Admin command to enable welcome messages |
| /rw disable | randomwelcome.disable | Admin command to disable welcome messages |
| /rw reload | randomwelcome.reload | Admin command to reload plugin config |
| /rw reload | randomwelcome.reload | Admin command to reload plugin config |
| | randomwelcome.welcome | Permission node to allow the player to be welcomed |
| | randomwelcome.welcome.others | Permission node to allow the player to see other player's being welcomed |

### Configuration

Configuration should be pretty straight-forward, but feel free to create an [issue](https://github.com/leviem1/RandomWelcome/issues) if you have any questions.

```yaml
# Choose whether to welcome only newcomers, or all players on join
newcomers_only: true

# Choose whether to broadcast the message
broadcast_publicly: true

# Choose whether or not to enable the vanilla join message
player_joined_message: true

# Prefix for welcome messages, great for formatting codes
prefix: ""

# Added after welcome message when newcomers join
newcomer_message: "Welcome them to the server!"

# List of possible welcome messages
# Use %player% for the player's name
messages:
  - "Howdy Doody, %player%! Welcome to the server!"
```

## Installation

Simply drop into your server's plugin folder, and we'll generate your config for you. Restart your server or run `/rw reload` to load any changes you make or messages you add!

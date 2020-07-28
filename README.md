# RandomWelcome

Simple [PaperMC](https://papermc.io/) Minecraft server plugin to welcome players to a server by sending them a random message of your choosing!

I created this plugin to teach myself how to develop a [PaperAPI](https://github.com/PaperMC/Paper) Minecraft server plugin and compile it using the [Gradle](https://gradle.org/) build tool. Additionally, I use [GitHub Actions](https://github.com/features/actions) to build artifacts directly from git.

## Simple and Stable

Works with Paper, Spigot, and Bukkit servers!

Native Version: **1.16**  
Tested Minecraft Versions: **1.15, 1.16**

## Features

### Commands and Permissions

Below is a small table to help explain all the RandomWelcome commands and permissions

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
| | randomwelcome.* | Permission node to grant all other permissions |

### Configuration

Configuration should be pretty straight-forward, but feel free to create an [issue](https://github.com/leviem1/RandomWelcome/issues) if you have any questions.

```yaml
# Choose whether to broadcast the message to everyone, or just the joining player
broadcast_publicly: true

# Choose whether or not to enable the vanilla join message
vanilla_join_message: false

# Choose whether to welcome only newcomers, or all players on join
newcomers_only: false

# Text that should be prefixed to the player's name (perfect for formatting)
# Feel free to use Minecraft's formatting codes (e.g. &0, &1, ..., &f)
player_name_prefix: "&4"

# Added after welcome message when newcomers join
# Feel free to use Minecraft's formatting codes (e.g. &0, &1, ..., &f)
newcomer_message: "&6%player% is new! Welcome them to the server!"

# List of possible welcome messages
# Use %player% for the player's name
# Feel free to use Minecraft's formatting codes (e.g. &0, &1, ..., &f)
messages:
  - "&eHowdy Doody, %player%! Welcome to the server!"
  - "&eWelcome, %player%! We were expecting you ( ͡° ͜ʖ ͡°)"
  - "&eWelcome, %player%! We hope you brought pizza."
  - "&eWelcome, %player%! Leave your weapons by the door."
  - "&eA wild %player% appeared."
  - "&e%player% is here, as the prophecy foretold."
  - "&eBrace yourselves. %player% just joined the server."
  - "&e%player% just joined. Hide your bananas."
  - "&eWelcome %player%, watch out for the harbormaster!"
  - "&e&kBlahblah %player%, blahb bla bla bla blahblahbl!"
```

## Installation

Simply drop into your server's plugin folder, and we'll generate your config for you. Restart your server or run `/rw reload` to load any changes you make or messages you add!

## Building From Source

If you're interested in contributing, you can build with [Gradle](https://gradle.org/) using the following command.
```bash
gradlew shadowJar
```

# Skript
Looking for the official repository and 1.9+ releases ? Click [here](https://github.com/SkriptLang/Skript/releases).

Skript is a plugin for Bukkit/Spigot, which allows server owners and other people
to modify their servers without learning Java. It can also be useful if you
*do* know Java; some tasks are quicker to do with Skript, and so it can be used
for prototyping etc.

This GitHub fork of Skript is based on the SkriptLang fork, based on Mirreski's improvements
which was built on Njol's original Skript.

## Requirements
Skript requires **Spigot** to work. You heard it right, Bukkit does *not* work.
**Paper**, which is a fork of Spigot, is recommended; it is required for some
parts of Skript to be available, though might not be stable below 1.9.

This fork of Skript is only available for Minecraft 1.8. Check above if you're
looking for 1.9+ compatibility.

Also, it doesn't support hooks to FAWE or WorldGuard yet.

## Download
You can find the downloads in the [releases page](https://github.com/Matocolotoe/Skript/releases).

## Upgrading from an older version to this version
If you're upgrading from Skript 2.2 or older to this plugin, you need to change things in your scripts.

In the case of downgrading from 1.9+ (with an official release) to 1.8 (with this plugin), a big error with "END_ROD" might appear. If so,
this is because the plugin folder still contains 1.9+ materials. To fix this, do the following :

- backup your scripts, config and .csv files (e.g. copy or download your Skript folder somewhere on your computer not to lose anything)

- stop the server and delete the Skript folder

- start the server and let the plugin load and create its files

- stop the server and upload your scripts, config.sk and .csv files back where they were

- restart the server and everything should be working


**1. The lore separator `||` isn't available anymore, you will have to use a list of texts.**


For example, `1st line||2nd line||3rd line` will have to be `"1st line", "2nd line", "3rd line"`.


If you have a lot of lores to update, just use CTRL+F to replace `||` by `", "` in all the files you want.


This is not a complete fix, a lot warnings regarding `"and" missing` might appear.

To disable them, set the `disable variable missing and/or warnings` to `true` in your `config.sk` file.


**2. A new aliases system is out, so you might need to change them**


The new aliases are available in the [skript-aliases](https://github.com/SkriptLang/skript-aliases) repository.
This fork provides 1.8 aliases for splash potions (which changed in 1.9+), see details
[here](https://github.com/Matocolotoe/Skript-1.8/tree/master/skript-aliases/brewing.sk).

Also, if you had custom aliases, backup them and delete your `aliases-english.sk` and `aliases-german.sk`, they aren't used anymore.

To register custom aliases, you'll have to put this at the top of your script using them (example below).
```
aliases:
   pvp items = any swords, bow, arrow
```


Data values like `oak log:12` are not available anymore, a new expression as been introduced to support data values.

Most blocks now have aliases like 6-faces logs, for example `oak bark`.

However, if it does not have one, here is a working example : `set event-block to cauldron with data value 1`.


**3. You might also be worried regarding the loading time of your scripts. This is a known issue, especially if you are
using items with long lores.**

## Documentation
Documentation is available [here](https://skriptlang.github.io/Skript) for the
latest version of Skript.

## Issues and other stuff
Since this fork only provides retro-compatibility, issues regarding Skript will have to be posted
on the [official repository](https://github.com/SkriptLang/Skript) of the plugin.

## Relevant Links
* [skUnity forums](https://forums.skunity.com)
* [Add-on releases at skUnity](https://forums.skunity.com/forums/addon-releases)
* [Skript Chat Discord invite](https://discord.gg/0lx4QhQvwelCZbEX)
* [Skript Hub](https://skripthub.net)
* [Original Skript at Bukkit](https://dev.bukkit.org/bukkit-plugins/skript) (inactive)

Note that these resources are not maintained by Skript's developers. Don't
contact us about any problems you might have with them.

## Developers
Current team behind Skript:

* [bensku](https://github.com/bensku) (Skript maintainer/developer)
* [FranKusmiruk](https://github.com/FranKusmiruk) (Skript developer)
* [Pikachu920](https://github.com/Pikachu920) (Skript developer)
* [Nicofisi](https://github.com/Nicofisi) (Skript developer)
* [TheBentoBox](https://github.com/TheBentoBox) (issue tracker manager, aliases developer)

We should also thank [Njol](https://github.com/Njol) for creating
Skript and [Mirreski](https://github.com/Mirreski) for maintaining it for a
long time.

And of course, Skript has received lots of pull requests over time.
You can find all contributors [here](https://github.com/SkriptLang/Skript/graphs/contributors).

All code is owned by its writer, licensed for others under GPLv3 (see LICENSE)
unless otherwise specified.

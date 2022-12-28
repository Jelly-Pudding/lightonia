package com.minecraftonline.lightonia.commands;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Help {

	static void listCommands(Player player) {
		player.sendMessage(Text.of(TextColors.GOLD, "*** ")
				.concat(Text.of(TextColors.LIGHT_PURPLE, "Lightonia Commands")
				.concat(Text.of(TextColors.GOLD, " ***")
				.concat(Text.of(TextColors.LIGHT_PURPLE, "\n/Lightonia list <type>: ")
				.concat(Text.of(TextColors.WHITE, "Lists available backups. Type can be either 'Worlds' or 'Players'.")
				.concat(Text.of(TextColors.LIGHT_PURPLE, "\n/Lightonia select <type> <backup>: ")
				.concat(Text.of(TextColors.WHITE, "Select which backup to use. Type can be either 'World' or 'Player'.\"")
				.concat(Text.of(TextColors.LIGHT_PURPLE, "\n/Lightonia invsee <player>: ")
				.concat(Text.of(TextColors.WHITE, "Opens up the player's inventory from the selected player backup. This includes their armour and their off-hand slot.")
				.concat(Text.of(TextColors.LIGHT_PURPLE, "\n/Lightonia import: ")
				.concat(Text.of(TextColors.WHITE, "The backup region file corresponding to the one you are standing in gets imported into Lightonia.")
				.concat(Text.of(TextColors.LIGHT_PURPLE, "\n/Lightonia create: ")
				.concat(Text.of(TextColors.WHITE, "Creates the Lightonia world. Note: Use /Lightonia import first.")
				.concat(Text.of(TextColors.LIGHT_PURPLE, "\n/Lightonia tp: ")
				.concat(Text.of(TextColors.WHITE, "Teleports you into Lightonia.")
				.concat(Text.of(TextColors.LIGHT_PURPLE, "\n/Lightonia tpb: ")
				.concat(Text.of(TextColors.WHITE, "Teleports you back to your original position.")
				.concat(Text.of(TextColors.LIGHT_PURPLE, "\n/Lightonia unload: ")
				.concat(Text.of(TextColors.WHITE, "Unloads all loaded chunks in Lightonia.")
				.concat(Text.of(TextColors.LIGHT_PURPLE, "\n/Lightonia destroy: ")
				.concat(Text.of(TextColors.WHITE, "The Lightonia world is unloaded and its files are deleted asynchronously from the disk."))))))))))))))))))))));
	}
}
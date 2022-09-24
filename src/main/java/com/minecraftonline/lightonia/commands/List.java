package com.minecraftonline.lightonia.commands;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.minecraftonline.lightonia.ConfigFile;
import com.minecraftonline.lightonia.util.FileFunctions;

public class List {
	
	static void listBackups(Player player, Optional<String> option1) {
		if (option1.isPresent()) {
			if (option1.get().toLowerCase().equals("worlds")) {
				FileFunctions.listContents(player, ConfigFile.worldBackupPath, "World");
			} else if (option1.get().toLowerCase().equals("players")) {
				FileFunctions.listContents(player, ConfigFile.playerBackupPath, "Player");
			} else {
				player.sendMessage(Text.of(TextColors.DARK_RED, "You must do either /Lightonia list worlds or /Lightonia list players."));
			}
		} else {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Specify which backups to list (worlds or players)."));
		}
	}
}

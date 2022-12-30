package com.minecraftonline.lightonia.commands;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.minecraftonline.lightonia.util.FileFunctions;

public class Select {

	static void selectBackup(Player player, Optional<String> option1, Optional<String> option2) {
		if (option1.isPresent()) {
			if (option1.get().toLowerCase().equals("world")) {
				if (option2.isPresent()) {
					if (FileFunctions.checkFolderExists(option2.get(), true)) {
						if (FileFunctions.getDirectory(player, true, FileFunctions.selectedWorldBackup, new String[]{"region","DIM1","DIM-1"})) {
							if (FileFunctions.tarFileWorld) {
								player.sendMessage(Text.of(TextColors.DARK_GREEN, "Successfully found world backup inside the tar file\n" + FileFunctions.selectedWorldBackup + ": " + FileFunctions.tarDirectoryWorld));
							} else if (FileFunctions.tarCompressedFileWorld) {
								player.sendMessage(Text.of(TextColors.DARK_GREEN, "Successfully found world backup inside the compressed tar file\n" + FileFunctions.selectedWorldBackup + ": " + FileFunctions.tarDirectoryWorld));
							} else {
								player.sendMessage(Text.of(TextColors.DARK_GREEN, "Successfully found world backup:\n" + FileFunctions.selectedWorldBackup));
							}
						} else {
							if (FileFunctions.tarFileWorld) {
								player.sendMessage(Text.of(TextColors.DARK_RED, "This tar file does not contain any region folders."));
							} else if (FileFunctions.tarCompressedFileWorld) {
								player.sendMessage(Text.of(TextColors.DARK_RED, "This compressed tar file does not contain any region folders."));
							} else {
								player.sendMessage(Text.of(TextColors.DARK_RED, "Although this directory exists, it does not contain any region folders inside."));
							}
						}
					} else {
						player.sendMessage(Text.of(TextColors.DARK_RED, "Invalid world backup. Choose one from /Lightonia list worlds and check your spelling."));
					}
				} else {
					player.sendMessage(Text.of(TextColors.DARK_RED, "Remember to also select a world backup!"));
				}
			} else if (option1.get().toLowerCase().equals("player")) {
				if (option2.isPresent()) {
					if (FileFunctions.checkFolderExists(option2.get(), false)) {
						if (FileFunctions.getDirectory(player, false, FileFunctions.selectedPlayerBackup, new String[]{"playerdata"})) {
							if (FileFunctions.tarFilePlayer) {
								player.sendMessage(Text.of(TextColors.DARK_GREEN, "Successfully found player backup inside the tar file\n" + FileFunctions.selectedPlayerBackup + ": " + FileFunctions.tarDirectoryPlayer));
							} else if (FileFunctions.tarCompressedFilePlayer) {
								player.sendMessage(Text.of(TextColors.DARK_GREEN, "Successfully found player backup inside the compressed tar file\n" + FileFunctions.selectedPlayerBackup + ": " + FileFunctions.tarDirectoryPlayer));
							} else {
								player.sendMessage(Text.of(TextColors.DARK_GREEN, "Successfully found player backup inside:\n" + FileFunctions.selectedPlayerBackup));
							}
						} else {
							if (FileFunctions.tarFilePlayer) {
								player.sendMessage(Text.of(TextColors.DARK_RED, "This TAR file does not contain a playerdata folder."));
							} else if (FileFunctions.tarCompressedFilePlayer) {
								player.sendMessage(Text.of(TextColors.DARK_RED, "This compressed tar file does not contain a playerdata folder."));
							} else {
								player.sendMessage(Text.of(TextColors.DARK_RED, "Although this directory exists, it does not contain a playerdata folder."));
							}
						}
					} else {
						player.sendMessage(Text.of(TextColors.DARK_RED, "Invalid player backup. Choose one from /Lightonia list players and check your spelling."));
					}
				} else {
					player.sendMessage(Text.of(TextColors.DARK_RED, "Remember to specify a player backup! See the backup list with /Lightonia list players."));
				}
			} else {
				player.sendMessage(Text.of(TextColors.DARK_RED, "Choose either /Lightonia select world <backup> or /Lightonia select player <backup>."));
			}
		} else {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Usage: /Lightonia select <type> <backup>. See /Lightonia help."));
		}
	}
}


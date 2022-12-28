package com.minecraftonline.lightonia.commands;

import java.io.IOException;
import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.minecraftonline.lightonia.ConfigFile;
import com.minecraftonline.lightonia.Lightonia;
import com.minecraftonline.lightonia.util.FileFunctions;
import com.minecraftonline.lightonia.util.PlayerFunctions;

public class Invsee {
	
	static void seeEverything(Player player, Optional<String> option1) {
			if (option1.isPresent()) {
				if (!FileFunctions.selectedPlayerBackup.isEmpty()) {
					try {
						String stringUUID = PlayerFunctions.getUUID(player, option1.get());
						if (!stringUUID.isEmpty()) {
							Lightonia.getLogger().warn(player.getName() + " is using /Lightonia invsee. The server may lag.");
							player.sendMessage(Text.of(TextColors.GRAY, "Retrieving UUID.dat file..."));
							PlayerFunctions.shiftDummyArray();
							String dummyUUID = PlayerFunctions.dummyUUIDArray[0];
							if (FileFunctions.tarFilePlayer || FileFunctions.tarCompressedFilePlayer) {
								FileFunctions.transferFromTar(player, false, FileFunctions.selectedPlayerBackup, FileFunctions.tarDirectoryPlayer + "playerdata", 
										                      stringUUID + ".dat", ConfigFile.playerBackupPath, "player", "/playerdata/" + dummyUUID + ".dat", option1, dummyUUID);
							} else {
								if (FileFunctions.transferFiles(player, false, FileFunctions.selectedPlayerBackup, 
										                    "/playerdata/" + stringUUID + ".dat", "/playerdata/" + dummyUUID + ".dat", "playerdata")) {
									invseeAfterAsync(player, option1, dummyUUID);
								};
							}
						}
					} catch (IOException e) {
						player.sendMessage(Text.of(TextColors.DARK_RED, "Error when getting player UUID: " + e));
					}
				} else {
					player.sendMessage(Text.of(TextColors.DARK_RED, "Please select a player backup with /Lightonia select player <backup>."));
				}
			} else {
				player.sendMessage(Text.of(TextColors.DARK_RED, "Remember to specify a player!"));
			}
		}
	public static void invseeAfterAsync(Player player, Optional<String> option1, String dummyUUID) {
		Optional<User> dummyUser = PlayerFunctions.getUser(dummyUUID);
		if (dummyUser.isPresent()) {
			PlayerFunctions.seeInventory(player, dummyUser.get(), option1.get().toString());
			FileFunctions.deleteFile(ConfigFile.worldPath + "/playerdata/" + dummyUUID + ".dat");
		} else {
				player.sendMessage(Text.of(TextColors.DARK_RED, "Failed to get dummy user matching the UUID " + dummyUUID));
		}
	}
}

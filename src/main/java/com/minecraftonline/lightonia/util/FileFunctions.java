package com.minecraftonline.lightonia.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.minecraftonline.lightonia.ConfigFile;

public class FileFunctions {

	public static String selectedWorldBackup = "";
	public static String selectedPlayerBackup = "";
	public static String tarDirectoryWorld = "";
	public static String tarDirectoryPlayer = "";
	public static boolean tarFileWorld = false;
	public static boolean tarCompressedFileWorld = false;
	public static boolean tarFilePlayer = false;
	public static boolean tarCompressedFilePlayer = false;

	public static boolean checkWorldPath(Player player) {
		if (new File(ConfigFile.worldPath).exists()) {
			File[] listOfFiles = new File(ConfigFile.worldPath).listFiles();
			for (File file : listOfFiles) {
				if (file.isDirectory() && (file.getName().equals("region") || file.getName().equals("DIM1") || file.getName().equals("DIM-1"))) {
					// This worldPath is most likely correct.
					return true;
				}
			}
			player.sendMessage(Text.of(TextColors.DARK_RED, "The worldPath provided in lightonia.conf does not appear to be correct!"));
		} else {
			player.sendMessage(Text.of(TextColors.DARK_RED, "The worldPath provided in lightonia.conf does not exist!"));
		}
		return false;
	}
	
	static boolean isLightoniaEmpty() {
		// Creates parent directories if they do not already exist.
		File lightoniaRegionFolder = new File(ConfigFile.worldPath + "/Lightonia/region");
		if (!lightoniaRegionFolder.exists() || lightoniaRegionFolder.list().length == 0) {
			return true;
		}
		return false;
	}

	public static void listContents(Player player, String filePath, String type) {
		File folder = new File(filePath);
		if (folder.exists()) {
			List<Text> contents = new ArrayList<>();
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				if (file.isDirectory() || (file.getName().length() >= 5 && file.getName().endsWith(".tar")) ||
				   (file.getName().length() >= 8 && file.getName().endsWith(".tar.gz")) || (file.getName().length() >= 5 && file.getName().endsWith(".tgz"))) {
						contents.add(Text.of(TextColors.DARK_RED, file.getName()));
				}
			}
			PaginationList.builder()
				.contents(contents)
				.linesPerPage(20)
				.padding(Text.of(TextColors.GOLD, "-"))
				.title(Text.of(TextColors.DARK_GREEN, type + " backups"))
				.sendTo(player);
		} else {
			player.sendMessage(Text.of(TextColors.DARK_RED, "The Directory '" + filePath + "' does not exist."));
			player.sendMessage(Text.of(TextColors.DARK_RED, "This can be changed in lightonia.conf. Make sure to restart the server afterwards."));
		}
	}
	
	public static void deleteFile(String fileName) {
		new File(fileName).delete();
	}
	
	static void deleteDirectory(File directory) {
		File[] allContents = directory.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				FileFunctions.deleteDirectory(file);
			}
		}
		directory.delete();
	}

	public static boolean checkFolderExists(String playerGivenName, boolean world) {
		if (world) {
			File folder = new File(ConfigFile.worldBackupPath + "/" + playerGivenName);
			if (folder.isDirectory()) {
				FileFunctions.selectedWorldBackup = folder.getAbsolutePath();
				FileFunctions.tarFileWorld = false;
				FileFunctions.tarCompressedFileWorld = false;
				return true;
			} else if (folder.exists() && playerGivenName.length() >= 5 && playerGivenName.endsWith(".tar")) {
				FileFunctions.selectedWorldBackup = folder.getAbsolutePath();
				FileFunctions.tarFileWorld = true;
				FileFunctions.tarCompressedFileWorld = false;
				return true;
			} else if (folder.exists() && ((playerGivenName.length() >= 8 && playerGivenName.endsWith(".tar.gz")) || (playerGivenName.length() >= 5 && playerGivenName.endsWith(".tgz")))) {
				FileFunctions.selectedWorldBackup = folder.getAbsolutePath();
				FileFunctions.tarFileWorld = false;
				FileFunctions.tarCompressedFileWorld = true;
				return true;
			}
			return false;
		} else {
			File folder = new File(ConfigFile.playerBackupPath + "/" + playerGivenName);
			if (folder.isDirectory()) {
				FileFunctions.selectedPlayerBackup = folder.getAbsolutePath();
				FileFunctions.tarFilePlayer = false;
				FileFunctions.tarCompressedFilePlayer = false;
				return true;
			} else if (folder.exists() && playerGivenName.length() >= 5 && playerGivenName.endsWith(".tar")) {
				FileFunctions.selectedPlayerBackup = folder.getAbsolutePath();
				FileFunctions.tarFilePlayer = true;
				FileFunctions.tarCompressedFilePlayer = false;
				return true;
			} else if (folder.exists() && ((playerGivenName.length() >= 8 && playerGivenName.endsWith(".tar.gz")) || (playerGivenName.length() >= 5 && playerGivenName.endsWith(".tgz")))) {
				FileFunctions.selectedPlayerBackup = folder.getAbsolutePath();
				FileFunctions.tarFilePlayer = false;
				FileFunctions.tarCompressedFilePlayer = true;
				return true;
			}
			return false;
		}
	}
	
	public static boolean getDirectory(Player player, boolean world, String directory, String[] acceptedFolders) {
		if ((world && !FileFunctions.tarFileWorld && !FileFunctions.tarCompressedFileWorld) || (!world && !FileFunctions.tarFilePlayer && !FileFunctions.tarCompressedFilePlayer)) {
			File folder = new File(directory);
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				// For playerdata, ignore the DIM1 and DIM-1 folders if they exist.
				if (!world && file.isDirectory() && (file.getName().equals("DIM1") || file.getName().equals("DIM-1"))) {
					continue;
				}
				if (file.isDirectory()) {
					if (Arrays.asList(acceptedFolders).contains(file.getName())) {
						if (world) {
							FileFunctions.selectedWorldBackup = folder.getAbsolutePath();
						} else {
							FileFunctions.selectedPlayerBackup = folder.getAbsolutePath();
						}
						return true;
					} else {
						if (getDirectory(player, world, file.getAbsolutePath(), acceptedFolders)) {
							return true;
						}
					}
				}
			}
			return false;
		} else {
			boolean done = false;
			try {
				TarArchiveInputStream tarInput = null;
				if ((world && FileFunctions.tarFileWorld) || (!world && FileFunctions.tarFilePlayer)) {
					tarInput = new TarArchiveInputStream(new BufferedInputStream (new FileInputStream(directory)));
				} else {
					tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream (new FileInputStream(directory))));
				}
				TarArchiveEntry entry;
				while ((entry = tarInput.getNextTarEntry()) != null) {
					if (entry.isDirectory()) {
						// DIM1 is four characters, and playerdata 10 characters.
						if ((world && entry.getName().length() >= 4) || (!world && entry.getName().length() >= 10)) {
							String entryMinusSlash = entry.getName().substring(0, entry.getName().length() - 1);
							String folderName = entryMinusSlash.substring(entryMinusSlash.lastIndexOf('/') + 1);
							if (Arrays.asList(acceptedFolders).contains(folderName)) {
								if (world) {
									FileFunctions.tarDirectoryWorld = entryMinusSlash.substring(0, entryMinusSlash.length() - folderName.length());
								} else {
									String parentPath = entryMinusSlash.substring(0, entryMinusSlash.length() - folderName.length());
									if (!parentPath.isEmpty()) {
										String parentMinusSlash = parentPath.substring(0, parentPath.length() - 1);
										String parentFolder = parentMinusSlash.substring(parentMinusSlash.lastIndexOf('/') + 1);
										// For playerdata, we once again ignore DIM1 and DIM-1 folders.
										if (parentFolder.equals("DIM1") || parentFolder.equals("DIM-1")) {
											continue;
										}
									}
									FileFunctions.tarDirectoryPlayer = parentPath;
								}
								done = true;
								break;
							}
						}
					}
				}
				tarInput.close();
			} catch (IOException e) {
				player.sendMessage(Text.of(TextColors.DARK_RED, "Error while reading Tar file: \n" + e));
			}
			return done;
		}
	}
	
	public static boolean transferFromTar(Player player, boolean world, String selectedDirectory, String desiredDirectory, String desiredFile, String backupPath, String type, String destination) {
		boolean done = false;
		TarArchiveInputStream tarInput = null;
		try {
			if ((world && FileFunctions.tarFileWorld) || (!world && FileFunctions.tarFilePlayer)) {
				tarInput = new TarArchiveInputStream(new BufferedInputStream (new FileInputStream(selectedDirectory)));
			} else {
				tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream (new FileInputStream(selectedDirectory))));
			}
			TarArchiveEntry entry = null;
			while ((entry = tarInput.getNextTarEntry()) != null) {
				if (entry.getName().equals(desiredDirectory + "/" + desiredFile)) {
					// Ensures created directory will be unique.
					long unixTimeNow = System.currentTimeMillis() / 1000L;
					new File(backupPath, "/" + unixTimeNow + "lightonia_temp_" + type + "/" + desiredDirectory).mkdirs();
					File outputFile = new File(backupPath, "/" + unixTimeNow + "lightonia_temp_" + type + "/" + desiredDirectory + "/" + desiredFile);
					OutputStream outputFileStream = new FileOutputStream(outputFile);
					IOUtils.copy(tarInput, outputFileStream);
					outputFileStream.close();
					if (world) {
						// Ensures the directory exists.
						new File(ConfigFile.worldPath + "/Lightonia/region").mkdirs();
					}
					File destinationFile = new File(ConfigFile.worldPath + destination);
					Files.copy(outputFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					File parentFolder = new File(backupPath, "/" + unixTimeNow + "lightonia_temp_" + type);
					FileFunctions.deleteDirectory(parentFolder);
					player.sendMessage(Text.of(TextColors.DARK_GREEN, "Done!"));
					done = true;
					break;
				}
			}
			
		} catch (IOException e) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Error while reading and copying from Tar file: \n" + e));
		}
		try {
			tarInput.close();
		} catch (IOException e) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Error while closing archive input stream: \n" + e));
		}
		if (!done) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Error: " + type + " file\n(" + desiredDirectory + "/" + desiredFile + ")\n does not exist in the Tar file:\n" + selectedDirectory));
		}
		return done;
	}
	
	public static boolean transferFiles(Player player, boolean world, String selectedBackup, String backupPath, String destinationPath, String type) {
		if (world) {
			// Creates parent directories if they do not already exist.
			new File(ConfigFile.worldPath + "/Lightonia/region").mkdirs();
		}
		File source = new File(selectedBackup + backupPath);
		File destination = new File(ConfigFile.worldPath + destinationPath);
		if (source.exists()) {
			try {
				Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
				player.sendMessage(Text.of(TextColors.DARK_GREEN, "Done!"));
				return true;
			} catch (IOException e) {
				player.sendMessage(Text.of(TextColors.DARK_RED, "Copy file error:\n" + e));
			}
		} else {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Error: The " + type + " file\n(" + source.getAbsolutePath() + ")\n does not exist."));
		}
		return false;
	}

}
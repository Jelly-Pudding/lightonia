package com.minecraftonline.lightonia;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ConfigFile {

	public static String worldPath = "";
	public static String worldBackupPath = "";
	public static String playerBackupPath = "";
	public static String dummyUUID0 = "";
	public static String dummyUUID1 = "";
	public static String dummyUUID2 = "";
	public static String dummyUUID3 = "";
	public static String dummyUUID4 = "";

	static void getPaths() {
		// Creates parent directories if they do not already exist.
		new File("config/lightonia").mkdirs();
		String filePath = new File("config/lightonia").getAbsolutePath() + "/lightonia.conf";
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			worldPath = lines.get(1).split("=")[1];
			worldBackupPath = lines.get(2).split("=")[1];
			playerBackupPath = lines.get(3).split("=")[1];
			dummyUUID0 = lines.get(5).split("=")[1];
			dummyUUID1 = lines.get(6).split("=")[1];
			dummyUUID2 = lines.get(7).split("=")[1];
			dummyUUID3 = lines.get(8).split("=")[1];
			dummyUUID4 = lines.get(9).split("=")[1];
		} catch (IOException | IndexOutOfBoundsException e) {
			// If there are any errors, the configuration file gets completely reset.
			resetConfigFile(filePath);
		}
	}

	private static void resetConfigFile(String filePath) {
		// First the file gets deleted.
		try {
			Files.delete(Paths.get(filePath));
		} catch (IOException e) {
			// Left intentionally blank.
		}
		try {
			// Creates and appends to the new configuration file.
			Files.write(Paths.get(filePath),
					("# APART FROM ONES ALREADY PRESENT, COMMENTS ARE NOT SUPPORTED. DO NOT ADD LINES ANYWHERE. DO NOT ADD A / AT THE END OF ABSOLUTE PATHS."
					 + "\nworldPath=/home"
					 + "\nworldBackupPath=/home"
					 + "\nplayerBackupPath=/home"
					 + "\n# Choose player UUIDs of people who won't join your server. By default, these player UUIDS belong to notch, jeb_, Kappische, xlson, and hideous_."
					 + "\ndummyUUID0=069a79f4-44e9-4726-a5be-fca90e38aaf5"
					 + "\ndummyUUID1=853c80ef-3c37-49fd-aa49-938b674adae6"
					 + "\ndummyUUID2=c7f0141d-f239-4ff3-8130-10cd0462c7a6"
					 + "\ndummyUUID3=b9583ca4-3e64-488a-9c8c-4ab27e482255"
					 + "\ndummyUUID4=0c5a016f-236c-4785-98ea-c5092a042b5f")
							.getBytes(),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			worldPath = "/home";
			worldBackupPath = "/home";
			playerBackupPath = "/home";
			dummyUUID0 = "069a79f4-44e9-4726-a5be-fca90e38aaf5";
			dummyUUID1 = "853c80ef-3c37-49fd-aa49-938b674adae6";
			dummyUUID2 = "c7f0141d-f239-4ff3-8130-10cd0462c7a6";
			dummyUUID3 = "b9583ca4-3e64-488a-9c8c-4ab27e482255";
			dummyUUID4 = "0c5a016f-236c-4785-98ea-c5092a042b5f";
			
		} catch (IOException e) {
			// Left intentionally blank
		}
	}
}
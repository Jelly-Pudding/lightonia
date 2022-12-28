package com.minecraftonline.lightonia.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.minecraftonline.lightonia.ConfigFile;
import com.minecraftonline.lightonia.Lightonia;

public class PlayerFunctions {
	
	public static String [] dummyUUIDArray = new String [] {ConfigFile.dummyUUID0,
								ConfigFile.dummyUUID1,
								ConfigFile.dummyUUID2,
								ConfigFile.dummyUUID3,
								ConfigFile.dummyUUID4};
	
	public static String getUUID(Player player, String name) throws IOException {
		URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		int status = con.getResponseCode();
		if (status == 204) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Failed to find player's UUID."));
			return "";
		}
		InputStream is = con.getInputStream();
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		int len;
		byte[] bytes = new byte[200];
		while ((len = is.read(bytes)) != -1) {
			writer.write(bytes, 0, len);
		}
		is.close();
		con.disconnect();
		String response = new String(writer.toByteArray());
		response = response.substring(1, response.length() - 1);
		String[] keyValuePairs = response.split(",");
		String stringUUID = "";
		for(String pair: keyValuePairs) {
			String[] entry = pair.split(":");
			if (entry[0].trim().equals("\"id\"")) {
				stringUUID = entry[1].trim().substring(1, entry[1].length() - 1);
			}
		}
		stringUUID = stringUUID.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
		return stringUUID;
	}
	
	public static Optional<User> getUser(String stringUUID) {
		UUID uuid = UUID.fromString(stringUUID);
		Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
		return userStorage.get().get(uuid);
	}
	
	public static void seeInventory(Player player, User user, String targetName) {
		Inventory build = Inventory.builder()
				.of(InventoryArchetypes.DOUBLE_CHEST)
				.build(Lightonia.getPlugin());
        
		for(Inventory slot : user.getInventory().slots()){
			if (slot.peek().isPresent()) {
				build.offer(slot.peek().get());
			}
		}

		player.openInventory(build.parent(), Text.of(TextColors.DARK_RED, targetName + "'s " + "inventory"));      
	}
	
	public static void shiftDummyArray() {
		String temp = PlayerFunctions.dummyUUIDArray[4];
		for(int i=4; i>0; i--) {
			PlayerFunctions.dummyUUIDArray[i]=PlayerFunctions.dummyUUIDArray[i-1];
		}
		PlayerFunctions.dummyUUIDArray[0]=temp;
	}
}

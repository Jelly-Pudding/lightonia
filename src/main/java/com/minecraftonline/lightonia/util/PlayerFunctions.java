package com.minecraftonline.lightonia.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
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

import com.google.gson.Gson;
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
		String stringUUID = "";
		String inputLine = null;
		Gson gson = new Gson();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		while ((inputLine = in.readLine()) != null) {
			Map<?, ?> map = gson.fromJson(inputLine, Map.class);
			if (map.containsKey("id")) {
				stringUUID = map.get("id").toString();
			}
		}
		in.close();
		con.disconnect();
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

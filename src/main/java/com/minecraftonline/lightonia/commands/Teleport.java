package com.minecraftonline.lightonia.commands;

import java.util.HashMap;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.flowpowered.math.vector.Vector3d;
import com.minecraftonline.lightonia.util.WorldFunctions;

public class Teleport {
	
	static HashMap<String, Object[]> positionBeforeTeleport = new HashMap<String, Object[]>();
	static void lightoniaTeleport(Player player) {
		if (player.getWorld().getName().equals("Lightonia")) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "You are already in Lightonia!"));
			return;
		}
		if (WorldFunctions.doesLightoniaExist()) {
			player.sendMessage(Text.of(TextColors.DARK_GRAY, "Teleporting to Lightonia..."));
			Sponge.getServer().loadWorld("Lightonia");
			Vector3d playerPosition = player.getPosition();
			positionBeforeTeleport.put(player.getName(), new Object[] {player.getWorld().getName(), playerPosition});
			player.transferToWorld("Lightonia", playerPosition);
			return;
		}
		player.sendMessage(Text.of(TextColors.DARK_RED, "The world Lightonia does not exist! Try /Lightonia create and also check out /Lightonia help."));
	}
	
	static void defaultWorldTeleport(Player player) {	
		if (positionBeforeTeleport.containsKey(player.getName())) {
			player.sendMessage(Text.of(TextColors.DARK_GRAY, "Teleporting to original position..."));
			player.transferToWorld((String) positionBeforeTeleport.get(player.getName())[0], (Vector3d) positionBeforeTeleport.get(player.getName())[1]);
		} else {		
			player.sendMessage(Text.of(TextColors.DARK_GRAY, "It appears you did not use /Lightonia tp. Teleporting you to the default world..."));
			Vector3d playerPosition = player.getPosition();
			player.transferToWorld(Sponge.getServer().getDefaultWorldName(), playerPosition);
		}
	}
}
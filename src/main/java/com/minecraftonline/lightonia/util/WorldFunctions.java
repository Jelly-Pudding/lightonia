package com.minecraftonline.lightonia.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.WorldArchetypes;
import org.spongepowered.api.world.storage.WorldProperties;

public class WorldFunctions {
	
	public static boolean doesLightoniaExist() {
		Collection<WorldProperties> allWorldProperties = Sponge.getServer().getAllWorldProperties();
		for (WorldProperties properties: allWorldProperties) {
		    	if (properties.getWorldName().equals("Lightonia")) {
		    		return true;
		    	}
		    }
		return false;
	}
	
	public static boolean isLightoniaLoaded() {
		Optional<World> theWorld = Sponge.getServer().getWorld("Lightonia");
		if (theWorld.isPresent()) {
			return true;
		}
		return false;
	}

	public static void createLightonia(Player player) {
		if (FileFunctions.isLightoniaEmpty()) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Lightonia has no region files. Make sure you use /Lightonia import first."));
			return;
		}
		long unixTimeNow = System.currentTimeMillis() / 1000L;
		if (!doesLightoniaExist()) {
			World currentWorld = player.getWorld();
			String worldName = "Lightonia";
			WorldArchetype worldArchetype = WorldArchetype.builder()
					.difficulty(currentWorld.getDifficulty())
					.from(WorldArchetypes.THE_VOID)
					.generateSpawnOnLoad(false)
					.keepsSpawnLoaded(false)
					.loadsOnStartup(false)
					.build(worldName + unixTimeNow , worldName);
			try {
				Sponge.getServer().createWorldProperties(worldName, worldArchetype);
			} catch (IOException e1) {
				player.sendMessage(
						Text.of(TextColors.DARK_RED, "An error occurred while creating Lightonia..."));
				player.sendMessage(Text.of(TextColors.DARK_RED, e1));
			}
			Sponge.getServer().loadWorld("Lightonia");
			player.sendMessage(Text.of(TextColors.DARK_GREEN, "Successfully created and loaded Lightonia."));
			return;
		}
		if (!isLightoniaLoaded()) {
			Sponge.getServer().loadWorld("Lightonia");
			player.sendMessage(Text.of(TextColors.DARK_GREEN, "Lightonia already existed, but it has now been loaded."));
			return;
		}
		player.sendMessage(Text.of(TextColors.DARK_RED, "Lightonia already exists and it is already loaded."));
		return;
	}
	
	public static void unloadThenDeleteLightonia(Player player) {
		if (doesLightoniaExist()) {
			if (!isLightoniaLoaded()) {
				player.sendMessage(Text.of(TextColors.DARK_GREEN, "Lightonia was unloaded. Loading for deletion..."));
				Sponge.getServer().loadWorld("Lightonia");
			}
			World theWorld = Sponge.getServer().getWorld("Lightonia").get();
			if (Sponge.getServer().unloadWorld(theWorld)) {
				Sponge.getServer().deleteWorld(theWorld.getProperties());
				player.sendMessage(Text.of(TextColors.DARK_GREEN, "Successfully deleted Lightonia."));			
			} else {
				player.sendMessage(Text.of(TextColors.DARK_RED, "Lightonia cannot be deleted because someone appears to be inside it."));
			}
		} else {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Lightonia does not exist."));
		}
	}
}
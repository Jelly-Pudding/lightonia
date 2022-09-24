package com.minecraftonline.lightonia.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.minecraftonline.lightonia.ConfigFile;
import com.minecraftonline.lightonia.util.FileFunctions;
import com.minecraftonline.lightonia.util.WorldFunctions;

public class Import {
	static void importRegions(Player player) {
		if (FileFunctions.selectedWorldBackup.isEmpty()) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Please select a world backup with /lightonia select world <backup>."));
			return;
		}
		
		Vector3d playerCoordinates = player.getPosition();
		World playerWorld = player.getWorld();
		
		if (playerWorld.getName().equals("Lightonia")) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "You cannot import a region when you are in Lightonia!"));
			return;
		}
		
		if (WorldFunctions.doesLightoniaExist() && WorldFunctions.isLightoniaLoaded()) {
			World lightoniaWorld = Sponge.getServer().getWorld("Lightonia").get();
			if (!Sponge.getServer().unloadWorld(lightoniaWorld)) {
				player.sendMessage(Text.of(TextColors.DARK_RED, "Cannot import region file as someone else is inside Lightonia!"));
				return;
			}
		}
		
		Location<World> playerLocation = new Location<World>(playerWorld, playerCoordinates);
		Vector3i chunkCoords = playerLocation.getChunkPosition();
		Chunk theChunk = player.getWorld().getChunk(chunkCoords).orElse(null);
		
		if (theChunk == null) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Failed to get chunk coordinates."));
			return;
		}
		
		int regionX = theChunk.getPosition().getX() >> 5;
		int regionZ = theChunk.getPosition().getZ() >> 5;
		
		String regionName = "r." + regionX + "." + regionZ + ".mca";
		
		player.sendMessage(Text.of(TextColors.GRAY, "Importing region file " )
				.concat(Text.of(TextColors.DARK_GRAY, regionName)
				.concat(Text.of(TextColors.GRAY, " into Lightonia..."))));
		
		String dimension = playerWorld.getDimension().getType().toString();
		String folder = "";
		
		if (dimension.equals("OVERWORLD")) {
			folder = "";
		} else if (dimension.equals("NETHER")) {
			folder = "DIM-1/";
		} else if (dimension.equals("THE_END")) {
			folder = "DIM1/";
		}
		else {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Failed to identify the dimension: " + dimension));
			return;
		}
		if (FileFunctions.tarFileWorld || FileFunctions.tarCompressedFileWorld) {
			FileFunctions.transferFromTar(player, true, FileFunctions.selectedWorldBackup, FileFunctions.tarDirectoryWorld + folder + "region", regionName, 
					                      ConfigFile.worldBackupPath, "world", "/Lightonia/region/" + regionName);
		} else {
			FileFunctions.transferFiles(player, true, FileFunctions.selectedWorldBackup, "/" + folder + "region/" + regionName, "/Lightonia/region/" + regionName, "region");
		}
	}
}
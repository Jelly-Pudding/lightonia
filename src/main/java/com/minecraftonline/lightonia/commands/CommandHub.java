package com.minecraftonline.lightonia.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.minecraftonline.lightonia.util.WorldFunctions;

public class CommandHub implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource source, CommandContext args) {
		// Only continues if the command originates from a player.
		if (!(source instanceof Player)) {
			source.sendMessage(Text.of(TextColors.DARK_RED, "You must be a player to execute Lightonia commands."));
			return CommandResult.empty();
		}
		
		Player player = (Player) source;
		String argument = args.getOne("argument").get().toString().toLowerCase();
		Optional<String> option1 = args.getOne("option1");
		Optional<String> option2 = args.getOne("option2");
		
		if (argument.equals("select")) {
			Select.selectBackup(player, option1, option2);
			return CommandResult.success();
		}
		
		if (option2.isPresent()) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Only /Lightonia select allows this many arguments. See /Lightonia help."));
			return CommandResult.success();
		}
		
		if (argument.equals("invsee")) {
			Invsee.seeEverything(player, option1);
			return CommandResult.success();
		}
		
		if (argument.equals("list")) {
			List.listBackups(player, option1);
			return CommandResult.success();
		}
		
		if (option1.isPresent()) {
			player.sendMessage(Text.of(TextColors.DARK_RED, "Only /Lightonia invsee and /Lightonia list allow this many arguments. See /Lightonia help."));
			return CommandResult.success();
		}
		
		if (argument.equals("import")) {
			Import.importRegions(player);
			return CommandResult.success();
		}
		
		if (argument.equals("create")) {
			WorldFunctions.createLightonia(player);
			return CommandResult.success();
		}
		
		if (argument.equals("tp")) {
			Teleport.lightoniaTeleport(player);
			return CommandResult.success();
		}
		
		if (argument.equals("tpb")) {
			Teleport.defaultWorldTeleport(player);
			return CommandResult.success();
		}
		
		if (argument.equals("destroy")) {
			WorldFunctions.unloadThenDeleteLightonia(player);
			return CommandResult.success();
		}
		
		if (argument.equals("help")) {
			Help.listCommands(player);
			return CommandResult.success();
		}
		
		player.sendMessage(Text.of(TextColors.DARK_RED, "Unrecognised command. Check /Lightonia help."));
		return CommandResult.empty();
	}
}
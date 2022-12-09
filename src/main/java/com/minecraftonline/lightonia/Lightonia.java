package com.minecraftonline.lightonia;

import com.google.inject.Inject;
import com.minecraftonline.lightonia.commands.CommandHub;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(id = "lightonia",
		name = "Lightonia",
		description = "Allow players to access backups in game",
		authors = {"AlphaAlex115"}
		)
public class Lightonia {
	
	private static Lightonia plugin;
	private static Logger logger;
	
	@Inject
	public Lightonia(Logger logger) {
		plugin = this;
		Lightonia.logger = logger;
	}
	
	public static Lightonia getPlugin() {
		return plugin;
	}
	
	public static Logger getLogger() {
	    return logger;
	}
	
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		ConfigFile.getPaths();
		logger.info("Lightonia has been loaded.");
	}

	@Listener
	public void init(GameInitializationEvent event) {
		registerCommands();
	}

	private void registerCommands() {
		CommandSpec createSpec = CommandSpec.builder()
				.description(Text.of("Lightonia commands"))
				.permission("lightonia.command")
				.arguments(GenericArguments.seq(GenericArguments.string(Text.of("argument")), 
							GenericArguments.optionalWeak(GenericArguments.string(Text.of("option1"))),
							GenericArguments.optionalWeak(GenericArguments.string(Text.of("option2")))))
				.executor(new CommandHub())             					
				.build();
		Sponge.getCommandManager().register(this, createSpec, "lightonia");    
	}
}
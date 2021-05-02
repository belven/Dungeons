package com.belven.dungeons.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.belven.dungeons.Dungeons;

public abstract class BCommand {

	private String commandText = "";
	private Dungeons plugin;

	public BCommand(String inCommandText) {
		commandText = inCommandText;
	}

	public abstract void PerformCommand(CommandSender sender, Command command, String label, String[] args);

	public String getCommandText() {
		return commandText;
	}

	public void setCommandText(String commandText) {
		this.commandText = commandText;
	}

	public Dungeons getPlugin() {
		return plugin;
	}

	public void setPlugin(Dungeons testPlugin) {
		this.plugin = testPlugin;
	}
}

package com.belven.dungeons.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BTestCommand extends BCommand {

	static public String B_COMMAND_TEXT = "test";

	public BTestCommand() {
		super(B_COMMAND_TEXT);
	}

	@Override
	public void PerformCommand(CommandSender sender, Command command, String label, String[] args) {
		getPlugin().getLogger().info("You just tested the dungeons mod, congrats!");
	}
}
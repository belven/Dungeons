package com.belven.dungeons.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartArenaCommand extends BCommand {
	static public String B_COMMAND_TEXT = "startarena";

	public StartArenaCommand() {
		super(B_COMMAND_TEXT);
	}

	@Override
	public void PerformCommand(CommandSender sender, Command command, String label, String[] args) {
		// startarena {arenaname}
		Player p = (Player) sender;

		if (args.length > 0) {
			String arenaName = args[0];
			if (getPlugin().arenaExists(arenaName)) {
				getPlugin().getArena(arenaName).startArena();
			} else {
				p.sendMessage("Arena by the name of " + arenaName + " doesn't exist");
			}
		} else {
			p.sendMessage("Not enough args '//startarena {arenaName}'");
		}
	}
}
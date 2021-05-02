package com.belven.dungeons.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.belven.dungeons.Arena;

public class ArenaSetStartLocationCommand extends BCommand {
	static public String B_COMMAND_TEXT = "setstartlocation";

	public ArenaSetStartLocationCommand() {
		super(B_COMMAND_TEXT);
	}

	@Override
	public void PerformCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;

		if (args.length > 0) {
			if (getPlugin().dungeonExists(args[0])) {
				Arena a = getPlugin().getArena(args[0]);

				Location startLoc = p.getLocation();
				a.getData().setStartLoc(startLoc);
				a.update();
				p.sendMessage("Start location set to " + getPlugin().LocationToString(startLoc));
			} else {
				p.sendMessage("Dungeon by the name of " + args[0] + " doesn't exist");
			}
		} else {
			p.sendMessage("Dungeon name not provided!");
		}
	}
}
package com.belven.dungeons.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.belven.dungeons.ActiveArena;

public class ArenaClearCommand extends BCommand {
	static public String B_COMMAND_TEXT = "cleararena";

	public ArenaClearCommand() {
		super(B_COMMAND_TEXT);
	}

	@Override
	public void PerformCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;

		if (args.length > 0) {
			String arenaName = args[0];

			if (getPlugin().arenaExists(arenaName)) {
				ActiveArena activeArena = getPlugin().getActiveArena(arenaName);

				if (activeArena != null) {
					activeArena.clearArena();
					activeArena.deactivate();

					p.sendMessage("Arena " + args[0] + " has been cleared");
				} else {
					p.sendMessage("Arena " + args[0] + " isn't active");
				}
			} else {
				p.sendMessage("Arena by the name of " + arenaName + " doesn't exist");
			}
		} else {
			p.sendMessage("Arena name not provided!");
		}
	}
}
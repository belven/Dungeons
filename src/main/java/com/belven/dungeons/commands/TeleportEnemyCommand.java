package com.belven.dungeons.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.belven.dungeons.ActiveArena;
import com.belven.dungeons.ActiveDungeon;

public class TeleportEnemyCommand extends BCommand {
	static public String B_COMMAND_TEXT = "teleportenemy";

	public TeleportEnemyCommand() {
		super(B_COMMAND_TEXT);
	}

	@Override
	public void PerformCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;

		if (args.length > 0) {
			String arenaName = args[0];

			if (getPlugin().arenaExists(arenaName)) {
				ActiveArena activeArena = getPlugin().getActiveArena(arenaName);

				if (activeArena != null && activeArena instanceof ActiveDungeon) {
					ActiveDungeon activeDungeon = (ActiveDungeon) activeArena;
					activeDungeon.teleportEnemies();

					p.sendMessage("Arena " + args[0] + " enemies have been teleported back inside the arena");
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
package com.belven.dungeons.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.belven.dungeons.Dungeon;
import com.belven.dungeons.DungeonData;

public class EnemyAddCommand extends BCommand {
	static public String B_COMMAND_TEXT = "addenemy";

	public EnemyAddCommand() {
		super(B_COMMAND_TEXT);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void PerformCommand(CommandSender sender, Command command, String label, String[] args) {
		// addenemy {dungeonname} {type}
		Player p = (Player) sender;

		if (args.length > 1) {
			String dungeonName = args[0];
			if (getPlugin().dungeonExists(dungeonName)) {
				EntityType et = EntityType.fromName(args[1]);

				Dungeon d = (Dungeon) getPlugin().getArena(dungeonName);
				((DungeonData) d.getData()).addEnemy(et);
				d.getData().update();
				p.sendMessage("Enemy " + et.name() + " added to " + dungeonName);

			} else {
				p.sendMessage("Dungeon by the name of " + dungeonName + " doesn't exist");
			}
		} else {
			p.sendMessage("Not enough args '//addenemy {dungeonname} {type}'");
		}
	}
}
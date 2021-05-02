package com.belven.dungeons.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.belven.dungeons.Dungeon;

public class DungeonCreateCommand extends BCommand {
	static public String B_COMMAND_TEXT = "createdungeon";

	public DungeonCreateCommand() {
		super(B_COMMAND_TEXT);
	}

	@Override
	public void PerformCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;

		if (args.length > 0) {
			String dungeonName = args[0];

			if (!getPlugin().dungeonExists(dungeonName)) {
				Dungeon d = new Dungeon(dungeonName, getPlugin());
				getPlugin().addDungeon(d);
				d.getData().setWorld(p.getWorld());

				getPlugin().getLogger().info("Dungeon Name: " + d.getDungeonName());

				d.update();
				p.sendMessage("Dungeon " + args[0] + " has been created");
			} else {
				p.sendMessage("Dungeon already exists by the name of " + args[0]);
			}
		} else {
			p.sendMessage("Dungeon name not provided!");
		}
	}
}
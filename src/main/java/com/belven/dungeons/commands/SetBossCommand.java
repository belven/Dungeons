package com.belven.dungeons.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.belven.dungeons.Dungeon;
import com.belven.dungeons.DungeonData;

public class SetBossCommand extends BCommand {
	static public String B_COMMAND_TEXT = "setboss";

	public SetBossCommand() {
		super(B_COMMAND_TEXT);
	}

	@Override
	public void PerformCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;

		if (args.length > 1) {
			String dungeonName = args[0];
			if (getPlugin().dungeonExists(dungeonName)) {
				EntityType et = EntityType.fromName(args[1]);

				Block targetBlockExact = p.getTargetBlockExact(20);
				if (targetBlockExact.getType() == Material.CHEST) {

					Dungeon d = (Dungeon) getPlugin().getArena(dungeonName);
					((DungeonData) d.getData()).setBoss(et, targetBlockExact);
					d.getData().update();
					p.sendMessage("Boss of type " + et.name() + " added to " + dungeonName + " with equipment in chest");
				} else {
					p.sendMessage("Target block isn't a CHEST it's a " + targetBlockExact.getType().name());
				}

			} else {
				p.sendMessage("Dungeon by the name of " + dungeonName + " doesn't exist");
			}
		} else {
			p.sendMessage("Not enough args '//addenemy {dungeonname} {type}'");
		}

	}

}

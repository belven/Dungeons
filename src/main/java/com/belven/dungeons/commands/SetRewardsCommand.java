package com.belven.dungeons.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.belven.dungeons.Dungeon;

public class SetRewardsCommand extends BCommand {

	static public String B_COMMAND_TEXT = "setrewards";

	public SetRewardsCommand() {
		super(B_COMMAND_TEXT);
	}

	@Override
	public void PerformCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;

		if (args.length > 0) {
			if (getPlugin().dungeonExists(args[0])) {
				Dungeon d = (Dungeon) getPlugin().getArena(args[0]);

				Block targetBlockExact = p.getTargetBlockExact(20);
				if (targetBlockExact.getType() == Material.CHEST) {

					Location rewards = targetBlockExact.getLocation();
					d.getData().setRewardChest(rewards);
					d.update();

					p.sendMessage("Rewards set to " + getPlugin().LocationToString(rewards));
				} else {
					p.sendMessage("Target block isn't a CHEST it's a " + targetBlockExact.getType().name());
				}
			} else {
				p.sendMessage("Dungeon by the name of " + args[0] + " doesn't exist");
			}
		} else {
			p.sendMessage("Dungeon name not provided!");
		}
	}
}
package com.belven.dungeons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.belven.dungeons.commands.ArenaClearCommand;
import com.belven.dungeons.commands.ArenaSetEndLocationCommand;
import com.belven.dungeons.commands.ArenaSetStartLocationCommand;
import com.belven.dungeons.commands.BCommand;
import com.belven.dungeons.commands.DungeonCreateCommand;
import com.belven.dungeons.commands.EnemyAddCommand;
import com.belven.dungeons.commands.EnemyRemoveCommand;
import com.belven.dungeons.commands.SetBossCommand;
import com.belven.dungeons.commands.SetRewardsCommand;
import com.belven.dungeons.commands.StartArenaCommand;
import com.belven.dungeons.commands.TeleportEnemyCommand;
import com.belven.dungeons.listeners.EntityListener;

public class Dungeons extends JavaPlugin {
	private final EntityListener mobListener = new EntityListener(this);

	static HashMap<String, BCommand> commands = new HashMap<>();
	private ArrayList<Dungeon> dungeons = new ArrayList<Dungeon>();
	private ArrayList<ActiveArena> activeArenas = new ArrayList<ActiveArena>();

	static {
		commands.put(DungeonCreateCommand.B_COMMAND_TEXT, new DungeonCreateCommand());
		commands.put(ArenaSetStartLocationCommand.B_COMMAND_TEXT, new ArenaSetStartLocationCommand());
		commands.put(ArenaSetEndLocationCommand.B_COMMAND_TEXT, new ArenaSetEndLocationCommand());
		commands.put(EnemyAddCommand.B_COMMAND_TEXT, new EnemyAddCommand());
		commands.put(EnemyRemoveCommand.B_COMMAND_TEXT, new EnemyRemoveCommand());
		commands.put(StartArenaCommand.B_COMMAND_TEXT, new StartArenaCommand());
		commands.put(SetRewardsCommand.B_COMMAND_TEXT, new SetRewardsCommand());
		commands.put(ArenaClearCommand.B_COMMAND_TEXT, new ArenaClearCommand());
		commands.put(TeleportEnemyCommand.B_COMMAND_TEXT, new TeleportEnemyCommand());
		commands.put(SetBossCommand.B_COMMAND_TEXT, new SetBossCommand());
	}

	public Dungeons() {

	}

	public static int getRandomIndex(Object[] array) {
		int ran = new Random().nextInt(array.length);

		if (ran == array.length) {
			ran--;
		}

		return ran;
	}

	public static int getRandomIndex(List<?> array) {
		return getRandomIndex(array.toArray());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String commandString = "";
		commandString += command.getName();
		Player p = (Player) sender;

		for (String s : args) {
			commandString += " ";
			commandString += s;
		}

		if (commands.containsKey(command.getName())) {
			commands.get(command.getName()).PerformCommand(sender, command, label, args);
			return true;
		} else {
			for (BCommand bCommand : commands.values()) {
				if (commandString.contains(bCommand.getCommandText())) {
					getLogger().info(p.getName() + " ran the command " + bCommand.getCommandText());
					bCommand.PerformCommand(sender, command, label, args);
					return true;
				}
			}
		}

		getLogger().info("No command found");
		return false;
	}

	public Location StringToLocation(String s, World world) {
		Location tempLoc = null;

		if (s != null) {
			String[] strings = s.split(",");

			int x = 0;
			int y = 0;
			int z = 0;

			try {
				x = Integer.valueOf(strings[0].trim());
				y = Integer.valueOf(strings[1].trim());
				z = Integer.valueOf(strings[2].trim());

			} catch (NumberFormatException e) {

			}

			tempLoc = new Location(world, x, y, z);
		}

		return tempLoc;
	}

	public String LocationToString(Location l) {
		String locationString = "";

		if (l != null) {
			locationString = String.valueOf(l.getBlockX()) + "," + String.valueOf(l.getBlockY()) + "," + String.valueOf(l.getBlockZ());
		}
		return locationString;
	}

	@Override
	public void onDisable() {
		for (ActiveArena aa : getActiveArenas()) {
			aa.clearArena();
		}
		super.onDisable();
	}

	@Override
	public void onEnable() {
		super.onEnable();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(mobListener, this);

		for (BCommand command : commands.values()) {
			command.setPlugin(this);
		}

		loadDungeons();
	}

	private void loadDungeons() {
		if (getConfig().contains("Dungeons")) {

			for (String g : getConfig().getConfigurationSection("Dungeons").getKeys(false)) {
				Dungeon d = new Dungeon();
				DungeonData dd = new DungeonData(this);
				dd.load(this, g, getConfig());
				d.setData(dd);
				dungeons.add(d);
			}
		}
	}

	public void addDungeon(Dungeon d) {
		dungeons.add(d);
	}

	public boolean dungeonExists(String dungeonName) {
		for (Dungeon d : dungeons) {
			if (d.getDungeonName().equals(dungeonName)) {
				return true;
			}
		}
		return false;
	}

	public Arena getArena(String name) {
		for (Dungeon d : dungeons) {
			if (d.getDungeonName().equals(name)) {
				return d;
			}
		}
		return null;
	}

	public boolean arenaExists(String name) {
		for (Arena a : dungeons) {
			if (a.getData().getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public void addActiveArena(ActiveArena aa) {
		if (!activeArenas.contains(aa))
			activeArenas.add(aa);
	}

	public ArrayList<Dungeon> getDungeons() {
		return dungeons;
	}

	public void setDungeons(ArrayList<Dungeon> dungeons) {
		this.dungeons = dungeons;
	}

	public ArrayList<ActiveArena> getActiveArenas() {
		return activeArenas;
	}

	public void setActiveArenas(ArrayList<ActiveArena> activeArenas) {
		this.activeArenas = activeArenas;
	}

	public void removeActiveArena(ActiveArena aa) {
		if (activeArenas.contains(aa))
			activeArenas.remove(aa);
	}

	public ActiveArena getActiveArena(String name) {
		for (ActiveArena aa : getActiveArenas()) {
			if (aa.getArena().getData().getName().equals(name)) {
				return aa;
			}
		}
		return null;
	}

	public ActiveArena getPlayerArena(Player p) {
		for (ActiveArena aa : getActiveArenas()) {
			if (aa.getPlayers().contains(p)) {
				return aa;
			}
		}
		return null;
	}

	public boolean isPlayerInArena(Player p) {
		for (ActiveArena aa : getActiveArenas()) {
			if (aa.getPlayers().contains(p)) {
				return true;
			}
		}
		return false;
	}
}

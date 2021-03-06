package com.belven.dungeons;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ArenaData {
	private Dungeons plugin;
	private String name = "";
	private World world = null;
	private Location endLoc = null;
	private Location startLoc = null;
	private Location rewardChest = null;
	private boolean isActive = false;

	public ArenaData() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Dungeons getPlugin() {
		return plugin;
	}

	public void setPlugin(Dungeons plugin) {
		this.plugin = plugin;
	}

	public abstract void update();

	public abstract void load(Dungeons d, String name, FileConfiguration fc);

	public Location getStartLoc() {
		return startLoc;
	}

	public void setStartLoc(Location startLoc) {
		this.startLoc = startLoc;
	}

	public Location getEndLoc() {
		return endLoc;
	}

	public void setEndLoc(Location endLoc) {
		this.endLoc = endLoc;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Location getRewardChest() {
		return rewardChest;
	}

	public void setRewardChest(Location rewardChest) {
		this.rewardChest = rewardChest;
	}

}

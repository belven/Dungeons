package com.belven.dungeons;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public abstract class Arena {

	public Arena() {
		// TODO Auto-generated constructor stub
	}

	public void update() {
		getData().update();
	}

	public abstract ArenaData getData();

	public abstract void startArena();

	public ArrayList<Location> getSpawnableLocations() {
		World w = getData().getWorld();

		ArrayList<Location> spawnableLocs = new ArrayList<>();

		int startX = getData().getStartLoc().getBlockX();
		int endX = getData().getEndLoc().getBlockX();

		int startY = getData().getStartLoc().getBlockY();
		int endY = getData().getEndLoc().getBlockY();

		int startZ = getData().getStartLoc().getBlockZ();
		int endZ = getData().getEndLoc().getBlockZ();

		int lowX = Math.min(startX, endX);
		int lowY = Math.min(startY, endY);
		int lowZ = Math.min(startZ, endZ);

		int highX = Math.max(startX, endX);
		int highY = Math.max(startY, endY);
		int highZ = Math.max(startZ, endZ);

		for (int x = lowX; x < highX; x++) {
			for (int y = lowY; y < highY; y++) {
				for (int z = lowZ; z < highZ; z++) {
					Location floorL = new Location(w, x, y, z);
					Location feetL = new Location(w, x, y + 1, z);
					Location headL = new Location(w, x, y + 2, z);

					if (floorL.getBlock().getType() != Material.AIR && feetL.getBlock().getType() == Material.AIR && headL.getBlock().getType() == Material.AIR) {
						spawnableLocs.add(feetL);
					}
				}
			}
		}

		return spawnableLocs;
	}
}

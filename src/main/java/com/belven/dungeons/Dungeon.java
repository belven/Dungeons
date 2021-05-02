package com.belven.dungeons;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class Dungeon extends Arena {
	private DungeonData data = new DungeonData();

	public Dungeon() {

	}

	public Dungeon(String dungeonName, Dungeons plugin) {
		data.setName(dungeonName);
		data.setPlugin(plugin);
	}

	public String getDungeonName() {
		return data.getName();
	}

	@Override
	public ArenaData getData() {
		return data;
	}

	public void setData(DungeonData data) {
		this.data = data;
	}

	@Override
	public void startArena() {
		spawnEnemies(getSpawnableLocations());
	}

	public void spawnEnemies(ArrayList<Location> spawnableLocs) {

		for (int i = 0; i < 10; i++) {
			// random = 25, random % 10 = 5
			int rand = Dungeons.getRandomIndex(spawnableLocs);
			Location l = spawnableLocs.get(rand);

			DungeonData dd = (DungeonData) getData();
			int rand2 = Dungeons.getRandomIndex(dd.getEnemies());
			Entity e = getData().getWorld().spawnEntity(l, dd.getEnemies().get(rand2));
			// e.setMetadata(metadataKey, newMetadataValue);
		}
	}
}
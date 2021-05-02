package com.belven.dungeons;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public class DungeonData extends ArenaData {
	private ArrayList<EntityType> enemies = new ArrayList<>();

	public DungeonData() {

	}

	@Override
	public void load(Dungeons d, String name, FileConfiguration fc) {
		setName(name);
		setWorld(d.getServer().getWorld(fc.getString("Dungeons." + name + ".World")));
		setStartLoc(d.StringToLocation(fc.getString("Dungeons." + name + ".StartLocation"), getWorld()));
		setEndLoc(d.StringToLocation(fc.getString("Dungeons." + name + ".EndLocation"), getWorld()));

		for (String enemyType : fc.getString("Dungeons." + name + ".Enemies").split("@E")) {
			getEnemies().add(EntityType.fromName(enemyType));
		}
	}

	@Override
	public void update() {
		Dungeons plug = getPlugin();

		if (plug != null) {
			plug.reloadConfig();

			FileConfiguration con = plug.getConfig();

			con.set("Dungeons", getName());

			if (getWorld() != null)
				con.set("Dungeons." + getName() + ".World", getWorld().getName());

			if (getStartLoc() != null)
				con.set("Dungeons." + getName() + ".StartLocation", plug.LocationToString(getStartLoc()));

			if (getEndLoc() != null)
				con.set("Dungeons." + getName() + ".EndLocation", plug.LocationToString(getEndLoc()));

			StringBuilder sb = new StringBuilder();

			for (EntityType et : enemies) {
				sb.append("@E" + et.name());
			}

			// Sheep@ESkeleton@ECreeper
			if (sb.toString().length() > 2)
				con.set("Dungeons." + getName() + ".Enemies", sb.toString().substring(2));

			plug.saveConfig();
		}
	}

	public void addEnemy(EntityType et) {
		enemies.add(et);
	}

	public ArrayList<EntityType> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<EntityType> enemies) {
		this.enemies = enemies;
	}

}
package com.belven.dungeons;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public class DungeonData extends ArenaData {
	private ArrayList<EntityType> enemies = new ArrayList<>();
	private EntityType bossType = null;
	private Location bossChest = null;

	public DungeonData(Dungeons dungeons) {
		setPlugin(dungeons);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void load(Dungeons d, String name, FileConfiguration fc) {
		setName(name);
		setWorld(d.getServer().getWorld(fc.getString("Dungeons." + name + ".World")));
		setStartLoc(d.StringToLocation(fc.getString("Dungeons." + name + ".StartLocation"), getWorld()));
		setEndLoc(d.StringToLocation(fc.getString("Dungeons." + name + ".EndLocation"), getWorld()));
		setRewardChest(d.StringToLocation(fc.getString("Dungeons." + name + ".RewardChest"), getWorld()));
		setBossChest(d.StringToLocation(fc.getString("Dungeons." + name + ".BossChest"), getWorld()));
		setBossType(EntityType.fromName(fc.getString("Dungeons." + name + ".BossType")));

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

			if (getWorld() != null)
				con.set("Dungeons." + getName() + ".World", getWorld().getName());

			if (getStartLoc() != null)
				con.set("Dungeons." + getName() + ".StartLocation", plug.LocationToString(getStartLoc()));

			if (getEndLoc() != null)
				con.set("Dungeons." + getName() + ".EndLocation", plug.LocationToString(getEndLoc()));

			if (getRewardChest() != null)
				con.set("Dungeons." + getName() + ".RewardChest", plug.LocationToString(getRewardChest()));

			if (getBossChest() != null)
				con.set("Dungeons." + getName() + ".BossChest", plug.LocationToString(getBossChest()));

			if (getBossType() != null)
				con.set("Dungeons." + getName() + ".BossType", getBossType().name());

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

	public void removeEnemy(EntityType et) {
		enemies.remove(et);
	}

	public ArrayList<EntityType> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<EntityType> enemies) {
		this.enemies = enemies;
	}

	public void setBoss(EntityType et, Block targetBlockExact) {
		setBossType(et);
		setBossChest(targetBlockExact.getLocation());
	}

	public EntityType getBossType() {
		return bossType;
	}

	public void setBossType(EntityType bossType) {
		this.bossType = bossType;
	}

	public Location getBossChest() {
		return bossChest;
	}

	public void setBossChest(Location bossChest) {
		this.bossChest = bossChest;
	}

}
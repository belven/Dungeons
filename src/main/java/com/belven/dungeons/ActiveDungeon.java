package com.belven.dungeons;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class ActiveDungeon extends ActiveArena {
	private ArrayList<LivingEntity> entities = new ArrayList<>();
	int wavesLeft = 0;

	public ActiveDungeon(Dungeon inDungeon) {
		super(inDungeon);
	}

	public Dungeon getDungeon() {
		return (Dungeon) getArena();

	}

	public void entityKilled(LivingEntity entity) {
		entities.remove(entity);

		if (entities.size() == 0) {
			if (wavesLeft == 0) {
				giveRewards();
				clearArena();
				deactivate();
			} else {
				spawnWave();

				if (wavesLeft == 0) {
					spawnBoss();
				}
			}
		}

		for (Player p : getPlayers()) {
			p.sendMessage("Arena mob killed " + entities.size() + " left to kill and " + wavesLeft + " waves left to go!");
		}
	}

	private void spawnBoss() {

		DungeonData dd = getDungeonData();

		LivingEntity e = (LivingEntity) getDungeonData().getWorld().spawnEntity(getRandomLocation(), dd.getBossType());
		e.setMetadata("DungeonMob", new FixedMetadataValue(getDungeonData().getPlugin(), this));
		entities.add(e);

		Block block = getDungeonData().getBossChest().getBlock();
		Chest bossChest = (Chest) block.getState();

		if (bossChest != null) {
			e.getEquipment().setArmorContents(bossChest.getInventory().getContents());

//			for (ItemStack is : bossChest.getInventory().getContents()) {
//				if (is == null) {
//					continue;
//				}			
//			}
		}

	}

	public boolean contains(LivingEntity le) {
		return entities.contains(le);
	}

	@Override
	public synchronized boolean canTransitionToState(ArenaState state) {
		switch (state) {
		case ACTIVE:
			return getCurrentState() == ArenaState.NOT_ACTIVE;
		case CLEARING_ARENA:
			return getCurrentState() == ArenaState.ACTIVE;
		case GIVING_REWARDS:
			return getCurrentState() == ArenaState.ACTIVE;
		case NEXT_WAVE:
			return getCurrentState() == ArenaState.ACTIVE;
		case NOT_ACTIVE:
			return getCurrentState() == ArenaState.CLEARING_ARENA;
		default:
			return false;
		}
	}

	public synchronized void giveRewards() {
		Block block = getDungeonData().getRewardChest().getBlock();
		Chest rewardChest = (Chest) block.getState();
		if (rewardChest != null) {
			for (ItemStack is : rewardChest.getInventory().getContents()) {
				if (is == null) {
					continue;
				}

				for (Player p : getPlayers()) {
					p.getInventory().addItem(is);
				}
			}
		}
	}

	private DungeonData getDungeonData() {
		return getDungeon().getDungeonData();
	}

	public synchronized void spawnWave() {
		int enemiesToSpawn = 10;
		wavesLeft--;

		for (int i = 0; i < enemiesToSpawn; i++) {
			setSpawnableLocs(getDungeon().getSpawnableLocations());

			DungeonData dd = getDungeonData();
			int rand2 = Dungeons.getRandomIndex(dd.getEnemies());
			LivingEntity e = (LivingEntity) getDungeonData().getWorld().spawnEntity(getRandomLocation(), dd.getEnemies().get(rand2));
			e.setMetadata("DungeonMob", new FixedMetadataValue(getDungeonData().getPlugin(), this));
			entities.add(e);
		}
	}

	@Override
	public synchronized void activate() {
		getDungeonData().getPlugin().addActiveArena(this);
		spawnWave();
		wavesLeft = 1;
	}

	@Override
	public synchronized void clearArena() {
		for (LivingEntity le : getDungeonData().getWorld().getLivingEntities()) {
			if (le.hasMetadata("DungeonMob")) {

				if (le.getMetadata("DungeonMob").get(0).value() == this)
					le.remove();
			}
		}

		getSpawnableLocs().clear();
		entities.clear();
	}

	@Override
	public synchronized void deactivate() {
		getDungeonData().getPlugin().removeActiveArena(this);
	}

	public ArrayList<LivingEntity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<LivingEntity> entities) {
		this.entities = entities;
	}

	public void teleportEnemies() {
		for (LivingEntity le : getEntities()) {
			le.teleport(getRandomLocation());
		}
	}

	private Location getRandomLocation() {
		int rand = Dungeons.getRandomIndex(getSpawnableLocs());
		Location l = getSpawnableLocs().get(rand);
		return l;
	}

	@Override
	public Location getRespawnPoint(Player p) {
		return getRandomLocation();
	}
}
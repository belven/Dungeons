package com.belven.dungeons;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class ActiveDungeon extends ActiveArena {
	int enemiesLeft = 0;

	public ActiveDungeon(Dungeon inDungeon) {
		super(inDungeon);
	}

	public Dungeon getDungeon() {
		return (Dungeon) getArena();

	}

	public void entityKilled(LivingEntity entity) {
		enemiesLeft--;

		if (enemiesLeft == 0) {
			giveRewards();
			clearArena();
			deactivate();
		}

		for (Player p : getPlayers()) {
			p.sendMessage("Arena mob killed " + enemiesLeft + " left to kill!");
		}
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

		for (int i = 0; i < enemiesToSpawn; i++) {
			setSpawnableLocs(getDungeon().getSpawnableLocations());
			int rand = Dungeons.getRandomIndex(getSpawnableLocs());
			Location l = getSpawnableLocs().get(rand);

			DungeonData dd = getDungeonData();
			int rand2 = Dungeons.getRandomIndex(dd.getEnemies());
			Entity e = getDungeonData().getWorld().spawnEntity(l, dd.getEnemies().get(rand2));
			e.setMetadata("DungeonMob", new FixedMetadataValue(getDungeonData().getPlugin(), this));
			enemiesLeft++;
		}
	}

	@Override
	public synchronized void activate() {
		spawnWave();
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
	}

	@Override
	public synchronized void deactivate() {
		getDungeonData().getPlugin().removeActiveArena(this);
	}
}
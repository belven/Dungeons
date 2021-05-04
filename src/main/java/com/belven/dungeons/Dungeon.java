package com.belven.dungeons;

public class Dungeon extends Arena {
	private DungeonData data = null;

	public Dungeon() {

	}

	public Dungeon(String dungeonName, Dungeons plugin, DungeonData dd) {
		setData(dd);
		data.setName(dungeonName);
		data.setPlugin(plugin);
	}

	public String getDungeonName() {
		return data.getName();
	}

	public DungeonData getDungeonData() {
		return (DungeonData) getData();
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
		ActiveDungeon ad = new ActiveDungeon(this);
		ad.activate();
		getData().getPlugin().addActiveArena(ad);
	}
}
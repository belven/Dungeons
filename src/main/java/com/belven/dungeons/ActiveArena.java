package com.belven.dungeons;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class ActiveArena {
	private Arena arena = null;
	private ArenaState currentState = ArenaState.NOT_ACTIVE;
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Location> spawnableLocs = new ArrayList<>();

	public ActiveArena(Arena inArena) {
		arena = inArena;
	}

	public void setState(ArenaState state) throws IllegalStateException {
		if (canTransitionToState(state)) {
			currentState = state;
		} else {
			throw new IllegalStateException("Can't transition from state " + currentState.toString() + " to " + state.toString());
		}
	}

	public synchronized void addPlayer(Player p) {
		if (!players.contains(p))
			players.add(p);
	}

	public synchronized void removePlayer(Player p) {
		if (players.contains(p))
			players.remove(p);
	}

	public abstract void activate();

	public abstract void clearArena();

	public abstract void deactivate();

	public abstract boolean canTransitionToState(ArenaState state);

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public ArenaState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(ArenaState currentState) {
		this.currentState = currentState;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<Location> getSpawnableLocs() {
		return spawnableLocs;
	}

	public void setSpawnableLocs(ArrayList<Location> spawnableLocs) {
		this.spawnableLocs = spawnableLocs;
	}

	public boolean contains(Player p) {
		return players.contains(p);
	}

	public boolean contains(Location location) {
		for (Location l : getSpawnableLocs()) {
			if (l.equals(location))
				return true;
		}
		return false;
	}

	public abstract Location getRespawnPoint(Player p);
}
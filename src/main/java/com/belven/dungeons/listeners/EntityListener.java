package com.belven.dungeons.listeners;

import java.util.HashMap;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import com.belven.dungeons.ActiveArena;
import com.belven.dungeons.ActiveDungeon;
import com.belven.dungeons.Dungeons;

public class EntityListener implements Listener {
	public HashMap<String, ItemStack[]> playerInventories = new HashMap<>();
	public HashMap<String, ItemStack[]> playerArmour = new HashMap<>();
	public HashMap<String, ActiveArena> playerArena = new HashMap<>();

	private final Dungeons plugin;

	public EntityListener(Dungeons instance) {
		plugin = instance;
	}

	public static LivingEntity GetDamager(EntityDamageByEntityEvent event) {
		Entity damagerEntity = event.getDamager();

		if (damagerEntity instanceof LivingEntity) {
			return (LivingEntity) damagerEntity;
		} else if (damagerEntity.getType() == EntityType.ARROW) {
			Arrow currentArrow = (Arrow) damagerEntity;
			return (LivingEntity) currentArrow.getShooter();
		} else if (damagerEntity.getType() == EntityType.FIREBALL) {
			Projectile currentFireball = (Projectile) damagerEntity;
			return (LivingEntity) currentFireball.getShooter();
		} else {
			return null;
		}
	}

	@EventHandler
	public void onEntityTransformEvent(EntityTransformEvent event) {
		if (event.getEntity().hasMetadata("DungeonMob")) {

			for (Entity e : event.getTransformedEntities()) {
				MetadataValue metadataValue = event.getEntity().getMetadata("DungeonMob").get(0);
				ActiveDungeon ad = (ActiveDungeon) metadataValue.value();
				e.setMetadata("DungeonMob", metadataValue);
				ad.getEntities().add((LivingEntity) event.getEntity()); // TODO Possible cast check needed
			}
		}
	}

	public static LivingEntity GetDamager(LivingEntity le) {
		if (le.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			return GetDamager((EntityDamageByEntityEvent) le.getLastDamageCause());
		} else {
			return null;
		}
	}

	@EventHandler
	public void onEntityKilledEvent(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();

		if (entity.hasMetadata("DungeonMob")) {
			ActiveDungeon ad = (ActiveDungeon) entity.getMetadata("DungeonMob").get(0).value();
			ad.entityKilled(entity);
		}
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player p = event.getEntity();
		ActiveArena aa = getPlugin().getPlayerArena(p);

		if (aa != null) {
			playerInventories.put(p.getName(), p.getInventory().getContents());
			playerArmour.put(p.getName(), p.getInventory().getArmorContents());
			playerArena.put(p.getName(), aa);
			event.getDrops().clear();
		}
	}

	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player p = event.getPlayer();

		if (playerArena.containsKey(p.getName())) {
			event.setRespawnLocation(playerArena.get(p.getName()).getRespawnPoint(p));
			playerArena.remove(p.getName());
		}

		if (playerInventories.containsKey(p.getName())) {
			p.getInventory().setContents(playerInventories.get(p.getName()));
			playerInventories.remove(p.getName());
		}

		if (playerArmour.containsKey(p.getName())) {
			p.getInventory().setArmorContents(playerArmour.get(p.getName()));
			playerArmour.remove(p.getName());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntitySpawnEvent(EntitySpawnEvent event) {
		for (ActiveArena aa : plugin.getActiveArenas()) {
			if (aa.contains(event.getLocation())) {
				event.setCancelled(false);
			}
		}
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageByEntityEvent event) {
		LivingEntity le = null;
		Player p = null;

		Entity entity = event.getEntity();
		LivingEntity damager = GetDamager(event);

		if (entity != null && event.getDamager() != null && (entity instanceof Player || damager instanceof Player)) {

			// if the player attacks a mob inside the arena
			if (entity.hasMetadata("DungeonMob")) {
				le = (LivingEntity) entity;
				p = (Player) damager;
			} else if (damager.hasMetadata("DungeonMob")) {
				le = damager;
				p = (Player) entity;
			}

			if (le != null) {
				ActiveDungeon ad = (ActiveDungeon) le.getMetadata("DungeonMob").get(0).value();
				ad.addPlayer(p);
			}
		}
	}

	public Dungeons getPlugin() {
		return plugin;
	}

}

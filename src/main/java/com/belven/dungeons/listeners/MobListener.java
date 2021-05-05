package com.belven.dungeons.listeners;

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

import com.belven.dungeons.ActiveArena;
import com.belven.dungeons.ActiveDungeon;
import com.belven.dungeons.Dungeons;

public class MobListener implements Listener {
	private final Dungeons plugin;

	public MobListener(Dungeons instance) {
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
				e.setMetadata("DungeonMob", event.getEntity().getMetadata("DungeonMob").get(0));
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

package com.yurijware.bukkit.TntTorches;

import net.minecraft.server.EntityTNTPrimed;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;

public class TTBlockListener implements Listener {
	private final TntTorches plugin;
	
	public TTBlockListener(TntTorches plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockDamage(BlockDamageEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (event.isCancelled()) { return; }
		
		if (block.getType() == Material.TNT
				&& (item.getType() == Material.TORCH || item.getType() == Material.REDSTONE_TORCH_ON)) {
			if (!this.plugin.checkPermissions(player, "TntTorches.use")) {
				if (this.plugin.spoutEnabled && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
					SpoutManager.getPlayer(player).sendNotification("No permissions", "Permissions required",
							Material.TNT, (short) 0, 1000);
				} else {
					player.sendMessage("You need permissions to do that.");
				}
				return;
			}
			
			CraftWorld world = (CraftWorld) block.getWorld();
			EntityTNTPrimed tnt = new EntityTNTPrimed(world.getHandle(), block.getX() + 0.5D,
					block.getY() + 0.5D, block.getZ() + 0.5D);
			world.getHandle().addEntity(tnt);
			block.setType(Material.AIR);
			event.setCancelled(true);
		}
	}
	
}

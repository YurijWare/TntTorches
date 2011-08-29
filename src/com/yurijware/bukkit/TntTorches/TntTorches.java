package com.yurijware.bukkit.TntTorches;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class TntTorches extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
	protected static PluginDescriptionFile pdfFile = null;
    public static String logPrefix = null;
	
	protected static PermissionManager permissionsExHandler;
	protected static PermissionHandler permissionsHandler;
	
	protected boolean spoutEnabled = false;
	
	private final TTBlockListener blockListener = new TTBlockListener(this);
	
	@Override
	public void onDisable() {
		log.info(logPrefix + "Plugin disabled!");
	}
	
	@Override
	public void onEnable() {
		pdfFile = this.getDescription();
		logPrefix = "[" + pdfFile.getName() + "] ";
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, this);
		
		Plugin spout = pm.getPlugin("Spout");
		if (spout != null && spout.isEnabled()) {
			this.spoutEnabled = true;
			String v = spout.getDescription().getVersion();
			log.info(logPrefix + "Spout detected! Using version " + v);
	    }
		
		Plugin permissionsEx = pm.getPlugin("PermissionsEx");
		Plugin permissions = pm.getPlugin("Permissions");
		if (permissionsEx != null && permissionsEx.isEnabled()) {
			permissionsExHandler = PermissionsEx.getPermissionManager();
			String v = permissionsEx.getDescription().getVersion();
			log.info(logPrefix + "PermissionsEx detected! Using version " + v);
		} else if (permissions != null && permissions.isEnabled()) {
			permissionsHandler = ((Permissions) permissions).getHandler();
			String v = permissions.getDescription().getVersion();
			log.info(logPrefix + "Permissions detected! Using version " + v);
		}
		
		log.info(logPrefix + "Version " + pdfFile.getVersion()
				+ " is enabled!");
	}
	
	protected boolean checkPermissions(Player player, String node) {
		if (permissionsExHandler != null) {
			return permissionsExHandler.has(player, node);
		} else if (permissionsHandler != null) {
			return permissionsHandler.has(player, node);
		} else {
			return player.hasPermission(node);
		}
	}
	
}

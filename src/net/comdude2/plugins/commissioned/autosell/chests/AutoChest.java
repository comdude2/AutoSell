package net.comdude2.plugins.commissioned.autosell.chests;

import java.io.Serializable;
import java.util.UUID;

import net.comdude2.plugins.commissioned.autosell.main.AutoSell;

import org.bukkit.Location;
import org.bukkit.World;

public class AutoChest implements Serializable{
	
	private static final long serialVersionUID = 5589563463800646391L;
	private UUID playerId = null;
	private UUID worldId = null;
	private double x = -1;
	private double y = -1;
	private double z = -1;
	
	public AutoChest(UUID playerId, UUID worldId, double x, double y, double z){
		this.playerId = playerId;
		this.worldId = worldId;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Location getChestLocation(AutoSell as){
		World w = as.getServer().getWorld(worldId);
		if (w == null){return null;}
		Location l = new Location(w, x, y, z);
		return l;
	}
	
	public UUID getPlayerId(){
		return this.playerId;
	}
	
	@Override
	public String toString(){
		return "UUID: " + this.playerId + " Chest Location: WORLD: " + worldId + " X: " + x + " Y: " + y + " Z: " + z;
	}
	
}

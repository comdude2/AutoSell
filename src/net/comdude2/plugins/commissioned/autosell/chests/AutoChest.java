package net.comdude2.plugins.commissioned.autosell.chests;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Location;

public class AutoChest implements Serializable{
	
	private static final long serialVersionUID = 5589563463800646391L;
	private Location chestLocation = null;
	private UUID playerId = null;
	
	public AutoChest(Location chestLocation, UUID playerId){
		this.chestLocation = chestLocation;
		this.playerId = playerId;
	}
	
	public Location getChestLocation(){
		return this.chestLocation;
	}
	
	public UUID getPlayerId(){
		return this.playerId;
	}
	
	@Override
	public String toString(){
		return "UUID: " + this.playerId + " Chest Location: WORLD: " + this.chestLocation.getWorld().getName() + " X: " + this.chestLocation.getBlockX() + " Y: " + this.chestLocation.getBlockY() + " Z: " + this.chestLocation.getBlockZ();
	}
	
}

package net.comdude2.plugins.commissioned.autosell.chests;

import org.bukkit.OfflinePlayer;

public class Sale {
	
	private OfflinePlayer op = null;
	private Double value = null;
	
	public Sale(OfflinePlayer op, Double value){
		this.op = op;
		this.value = value;
	}
	
	public OfflinePlayer getOfflinePlayer(){
		return this.op;
	}
	
	public Double getValue(){
		return this.value;
	}
	
	public void addValue(Double value){
		this.value += value;
	}
	
}

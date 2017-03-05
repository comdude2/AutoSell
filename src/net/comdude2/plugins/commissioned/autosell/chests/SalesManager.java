package net.comdude2.plugins.commissioned.autosell.chests;

import java.util.LinkedList;
import java.util.UUID;

public class SalesManager {
	
	private LinkedList <Sale> sales = new LinkedList <Sale> ();
	
	public SalesManager(){
		
	}
	
	public Sale getSale(UUID playerId){
		for (Sale s : sales){
			if (s.getOfflinePlayer().getUniqueId().equals(playerId)){
				return s;
			}
		}
		return null;
	}
	
	public LinkedList <Sale> getSales(){return this.sales;}
	
	public void addSale(Sale s){
		this.sales.add(s);
	}
	
}

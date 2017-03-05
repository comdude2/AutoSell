package net.comdude2.plugins.commissioned.autosell.chests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.comdude2.plugins.commissioned.autosell.main.AutoSell;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSeller extends BukkitRunnable{
	
	private final boolean debug = true;
	
	private AutoSell as = null;
	private ChestManager cm = null;
	
	public AutoSeller(AutoSell as, ChestManager cm){
		this.as = as;
		this.cm = cm;
	}
	
	public void run() {
		long start = 0L;
		long finish = 0L;
		Date d = new Date();
		start = d.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String time = df.format(d);
		as.getLogger().info("Starting automatic selling of items '" + time + "'");
		HashMap <UUID, Integer> sales = new HashMap <UUID, Integer> ();
		ConcurrentLinkedQueue <AutoChest> chests = cm.getChests();
		for (AutoChest c : chests){
			try{
				if (c.getChestLocation() != null){
					World w = as.getServer().getWorld(c.getChestLocation().getWorld().getUID());
					if (w != null){
						Block b = w.getBlockAt(c.getChestLocation());
						if (b != null){
							if (b.getType() == Material.CHEST){
								Chest chest = (Chest)b;
								Inventory ci = chest.getInventory();
								//Invi manip
								
							}else{
								//Warn (Wrong block)
							}
						}else{
							//Warn
						}
					}else{
						//Warn
					}
				}else{
					//Warn
				}
			}catch(Exception e){/*Warn*/}
		}
		d = new Date();
		finish = d.getTime();
		long ttf = finish - start;
		as.getLogger().info("Finished automatic selling of items, took: " + ttf + " milliseconds!");
	}
	
}

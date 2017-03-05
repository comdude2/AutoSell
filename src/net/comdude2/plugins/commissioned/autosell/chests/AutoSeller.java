package net.comdude2.plugins.commissioned.autosell.chests;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.comdude2.plugins.commissioned.autosell.main.AutoSell;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AutoSeller implements Runnable{
	
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
		as.getLogger().info(ChatColor.stripColor(AutoSell.me) + "Starting automatic selling of items '" + time + "'");
		HashMap <OfflinePlayer, BigDecimal> sales = new HashMap <OfflinePlayer, BigDecimal> ();
		ConcurrentLinkedQueue <AutoChest> chests = cm.getChests();
		for (AutoChest c : chests){
			try{
				if (c.getChestLocation(as) != null){
					World w = as.getServer().getWorld(c.getChestLocation(as).getWorld().getUID());
					if (w != null){
						Block b = w.getBlockAt(c.getChestLocation(as));
						if (b != null){
							if (b.getType() == Material.CHEST){
								Chest chest = (Chest)b.getState();
								Inventory ci = chest.getInventory();
								//Invi manip
								ItemStack[] items = ci.getContents();
								if (items == null){System.out.println("No contents");}
								for (ItemStack item : items){
									if (item != null){
										BigDecimal value = as.getEssentials().getWorth().getPrice(item);
										OfflinePlayer op = as.getServer().getOfflinePlayer(c.getPlayerId());
										if (op != null){
											as.getLogger().info("Item found: " + item.getType().name() + " amount: " + item.getAmount() + " value: " + value);
											//ci.remove(item);
											if (!sales.containsKey(c.getPlayerId())){
												sales.put(op, value);
											}else{
												sales.put(op, sales.get(op).add(value));
											}
										}else{
											//Player doesn't exist, remove sign?
										}
									}
								}
								for (Map.Entry<OfflinePlayer, BigDecimal> sale : sales.entrySet()){
									as.econ.depositPlayer(sale.getKey(), Double.valueOf(sale.getValue().toString()));
									if (sale.getKey().isOnline()){
										Player p = as.getServer().getPlayer(sale.getKey().getUniqueId());
										if (p != null){p.sendMessage(AutoSell.me + "You gained " + sale.getValue() + " from AutoSell chests.");}
									}
								}
								System.out.println("Hmm");
							}else{
								//Warn (Wrong block)
								System.out.println("1");
							}
						}else{
							//Warn
							System.out.println("2");
						}
					}else{
						//Warn
						System.out.println("3");
					}
				}else{
					//Warn
					System.out.println("4");
				}
			}catch(Exception e){/*Warn*/
				e.printStackTrace();
			}
		}
		d = new Date();
		finish = d.getTime();
		long ttf = finish - start;
		as.getLogger().info(ChatColor.stripColor(AutoSell.me) + "Finished automatic selling of items, took: " + ttf + " milliseconds!");
	}
	
}

package net.comdude2.plugins.commissioned.autosell.chests;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	private AutoSell as = null;
	private ChestManager cm = null;
	private long lastFinish = 0L;
	
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
		SalesManager sales = new SalesManager();
		ConcurrentLinkedQueue <AutoChest> chests = cm.getChests();
		for (AutoChest c : chests){
			try{
				World w = as.getServer().getWorld(c.getWorldId());
				if (w != null){
					if (w.isChunkLoaded((int)c.getX(), (int)c.getZ())){
						Block b = w.getBlockAt((int)c.getX(), (int)c.getY(), (int)c.getZ());
						if (b != null){
							if (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST){
								Chest chest = (Chest)b.getState();
								Inventory ci = chest.getInventory();
								//Invi manip
								ItemStack[] items = ci.getContents();
								if (items == null){/*Contents were null*/}else{
									Double v = new Double(0D);
									OfflinePlayer op = as.getServer().getOfflinePlayer(c.getPlayerId());
									if (op != null){
										for (ItemStack item : items){
											try{
												if (item != null){
													BigDecimal value = as.getEssentials().getWorth().getPrice(item);
													if (value == null){}else if (value.intValue() <= 0){}else{
														v += (Double.valueOf(value.toString())) * item.getAmount();
														//as.getLogger().info("Item found: " + item.getType().name() + " amount: " + item.getAmount() + " value: " + value);
														if (v > 0){
															ci.remove(item);
														}
													}
													
												}
											}catch(Exception e){
												as.getLogger().warning("Error while processing chest: '" + c.toString() + "'");
												as.getLogger().warning("Error: " + e.getMessage() + " Cause: " + e.getCause());
												e.printStackTrace();
											}
										}
										if (v > 0){
											if (sales.getSale(op.getUniqueId()) == null){
												sales.addSale(new Sale(op, v));
											}else{
												sales.getSale(op.getUniqueId()).addValue(v);;
											}
										}//Else no value
									}else{
										//Player doesn't exist, remove sign?
										cm.remove(c);
										as.getLogger().warning("Couldn't find player owning chest at: " + chest.getLocation().toString() + ", chest removed.");
									}
								}
							}else{
								//Wrong block type, delete chest from system
								as.getChestManager().remove(c);
							}
						}else{
							//Block was null, shouldn't fire
							as.getChestManager().remove(c);
						}
					}else{
						//Chunk is not loaded, do not sell.
					}
				}else{
					//Warn
					as.getLogger().warning("Failed to find world '" + c.getWorldId() + "' keeping chest in the system in case world is loaded again.");
				}
			}catch(Exception e){/*Warn*/
				as.getLogger().warning("Error: " + e.getMessage() + " Cause: " + e.getCause());
				e.printStackTrace();
			}
		}
		for (Sale sale : sales.getSales()){
			as.econ.depositPlayer(sale.getOfflinePlayer(), Double.valueOf(sale.getValue().toString()));
			if (sale.getOfflinePlayer().isOnline()){
				Player p = as.getServer().getPlayer(sale.getOfflinePlayer().getUniqueId());
				if (p != null){
					if (!as.getChestManager().getDoNotNotify().contains(p.getUniqueId())){
						p.sendMessage(AutoSell.me + "You gained " + sale.getValue() + " from AutoSell chests.");
					}
				}
			}
		}
		d = new Date();
		finish = d.getTime();
		long ttf = finish - start;
		this.lastFinish = finish;
		time = df.format(d);
		as.getLogger().info(ChatColor.stripColor(AutoSell.me) + "Finished automatic selling of items '" + time + "'");
		as.getLogger().info(ChatColor.stripColor(AutoSell.me) + "Finished automatic selling of items, took: " + ttf + " milliseconds!");
	}
	
	@Deprecated
	public void oldrun() {
		long start = 0L;
		long finish = 0L;
		Date d = new Date();
		start = d.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String time = df.format(d);
		as.getLogger().info(ChatColor.stripColor(AutoSell.me) + "Starting automatic selling of items '" + time + "'");
		SalesManager sales = new SalesManager();
		ConcurrentLinkedQueue <AutoChest> chests = cm.getChests();
		for (AutoChest c : chests){
			try{
				if (c.getChestLocation(as) != null){
					World w = as.getServer().getWorld(c.getChestLocation(as).getWorld().getUID());
					if (w != null){
						Block b = w.getBlockAt(c.getChestLocation(as));
						if (b != null){
							if (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST){
								Chest chest = (Chest)b.getState();
								Inventory ci = chest.getInventory();
								//Invi manip
								ItemStack[] items = ci.getContents();
								if (items == null){}else{
									Double v = new Double(0);
									OfflinePlayer op = as.getServer().getOfflinePlayer(c.getPlayerId());
									for (ItemStack item : items){
										try{
											if (item != null){
												BigDecimal value = as.getEssentials().getWorth().getPrice(item);
												if (value == null){}else if (value.intValue() <= 0){}
												if (op != null){
													v += (Double.valueOf(value.toString())) * item.getAmount();
													//as.getLogger().info("Item found: " + item.getType().name() + " amount: " + item.getAmount() + " value: " + value);
													ci.remove(item);
												}else{
													//Player doesn't exist, remove sign?
													cm.remove(c);
													as.getLogger().warning("Couldn't find player owning chest at: " + chest.getLocation().toString());
												}
											}
										}catch(Exception e){
											
										}
									}
									if (sales.getSale(op.getUniqueId()) == null){
										sales.addSale(new Sale(op, v));
									}else{
										sales.getSale(op.getUniqueId()).addValue(v);;
									}
								}
							}else{
								//Wrong block type, delete chest from system
								as.getChestManager().remove(c);
							}
						}else{
							//Block was null, shouldn't fire
							as.getChestManager().remove(c);
						}
					}else{
						//Warn
						as.getLogger().warning("Failed to find world '" + c.getChestLocation(as).getWorld().getUID() + "' keeping chest in the system in case world is loaded again.");
					}
				}else{
					//Warn
					as.getLogger().warning("Failed to get chest location for a chest owned by: " + c.getPlayerId() + " deleting chest...");
					as.getChestManager().remove(c);
				}
			}catch(Exception e){/*Warn*/
				e.printStackTrace();
			}
		}
		for (Sale sale : sales.getSales()){
			as.econ.depositPlayer(sale.getOfflinePlayer(), Double.valueOf(sale.getValue().toString()));
			if (sale.getOfflinePlayer().isOnline()){
				Player p = as.getServer().getPlayer(sale.getOfflinePlayer().getUniqueId());
				if (p != null){
					if (!as.getChestManager().getDoNotNotify().contains(p.getUniqueId())){
						p.sendMessage(AutoSell.me + "You gained " + sale.getValue() + " from AutoSell chests.");
					}
				}
			}
		}
		d = new Date();
		finish = d.getTime();
		long ttf = finish - start;
		this.lastFinish = finish;
		as.getLogger().info(ChatColor.stripColor(AutoSell.me) + "Finished automatic selling of items, took: " + ttf + " milliseconds!");
	}
	
	public long getLastFinish(){
		return this.lastFinish;
	}
	
}

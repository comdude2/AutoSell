package net.comdude2.plugins.commissioned.autosell.main;

import net.comdude2.plugins.commissioned.autosell.chests.AutoChest;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Sign;

public class Listeners implements Listener{
	
	private AutoSell as = null;
	
	public Listeners(AutoSell as){
		this.as = as;
	}
	
	public void register(){
		as.getServer().getPluginManager().registerEvents(this, as);
	}
	
	public void unregister(){
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event){
		//Check if text is correct
		if (event.getLine(0) != null){
			if (event.getLine(0).equalsIgnoreCase("[autosell]")){
				//My Sign
				if (event.getBlock().getType() == Material.WALL_SIGN){
					//Check the attatched block is a chest
					Sign s = (Sign)event.getBlock().getState().getData();
					Block attatchedBlock = event.getBlock().getRelative(s.getAttachedFace());
					if (attatchedBlock.getType() == Material.CHEST){
						//Is fine
						if (!as.getChestManager().chestExists(attatchedBlock.getLocation())){
							AutoChest ac = new AutoChest(event.getPlayer().getUniqueId(), attatchedBlock.getWorld().getUID(), attatchedBlock.getLocation().getX(), attatchedBlock.getLocation().getY(), attatchedBlock.getLocation().getZ());
							as.getChestManager().addChest(ac);
							as.getChestManager().save();
							event.setLine(0, ChatColor.GOLD + "[AutoSell]");
							event.setLine(1, event.getPlayer().getDisplayName().toString());
							event.getPlayer().sendMessage(AutoSell.me + ChatColor.GREEN + "AutoSell chest created.");
						}else{
							event.setCancelled(true);
							event.getPlayer().sendMessage(AutoSell.me + ChatColor.RED + "There is already an AutoSell on this chest.");
						}
					}else{
						event.setCancelled(true);
						event.getPlayer().sendMessage(AutoSell.me + ChatColor.RED + "You must attatch the sign to a chest.");
					}
				}else{
					event.setCancelled(true);
					event.getPlayer().sendMessage(AutoSell.me + ChatColor.RED + "You must attatch the sign to a chest.");
				}
			}//Not my sign
		}//No top line, ignore.
	}
	
}

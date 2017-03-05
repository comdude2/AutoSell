package net.comdude2.plugins.commissioned.autosell.main;

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
					Sign s = (Sign)event.getBlock().getState().getData();
					Block attatchedBlock = event.getBlock().getRelative(s.getAttachedFace());
					if (attatchedBlock.getType() == Material.CHEST){
						
					}else{
						event.setCancelled(true);
						event.getPlayer().sendMessage(AutoSell.me + "");
					}
				}else{
					event.setCancelled(true);
					event.getPlayer().sendMessage(AutoSell.me + "");
				}
			}//Not my sign
		}//No top line, ignore.
	}
	
}

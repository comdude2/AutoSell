package net.comdude2.plugins.commissioned.autosell.main;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutoSellCommand implements CommandExecutor{
	
	public AutoSell as = null;
	
	public AutoSellCommand(AutoSell as){
		this.as = as;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0){
			Date d = new Date();
			long now = d.getTime();
			long difference = now - as.getAutoSeller().getLastFinish();
			System.out.println("Difference is: " + difference);
			if (as.getAutoSeller().getLastFinish() == 0){
				//Server just started
				sender.sendMessage(AutoSell.me + "The next AutoSell should start within a minute.");
			}else{
				long time = (((AutoSell.repeating_delay /20) * 1000) - difference) / 1000;
				sender.sendMessage(AutoSell.me + "The next AutoSell should start in roughly " + time + " seconds.");
			}
		}else if (args.length == 1){
			if (args[0].equalsIgnoreCase("notify")){
				if (sender instanceof Player){
					Player p = (Player) sender;
					if (p.hasPermission("autosell.notify")){
						if (as.getChestManager().getDoNotNotify().contains(p.getUniqueId())){
							//Notify
							as.getChestManager().getDoNotNotify().remove(p.getUniqueId());
							sender.sendMessage(AutoSell.me + ChatColor.GREEN + "AutoSell notifications turned on.");
						}else{
							//Don't notify
							as.getChestManager().getDoNotNotify().add(p.getUniqueId());
							sender.sendMessage(AutoSell.me + ChatColor.RED + "AutoSell notifications turned off.");
						}
					}else{
						sender.sendMessage(AutoSell.me + ChatColor.RED + "You don't have permission to change your notification status.");
					}
				}else{
					sender.sendMessage("You must be a player to perform this command.");
				}
			}else{
				sender.sendMessage(AutoSell.me + ChatColor.RED + "Unknow arguments.");
			}
		}else{
			sender.sendMessage(AutoSell.me + ChatColor.RED + "Unknow arguments.");
		}
		return true;
	}

}

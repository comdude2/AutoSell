package net.comdude2.plugins.commissioned.autosell.main;

import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
			if (difference == 0){
				//Server just started
				sender.sendMessage(AutoSell.me + "The next AutoSell should start within a minute.");
			}else{
				sender.sendMessage(AutoSell.me + "The next AutoSell should start in ");
			}
		}else if (args.length == 1){
			if (args[0].equalsIgnoreCase("notify")){
				
			}else{
				
			}
		}else{
			
		}
		return false;
	}

}

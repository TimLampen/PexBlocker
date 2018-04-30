package me.timlampen.prisoncore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class FakePlayerCommand
        implements CommandExecutor{
    private FakePlayers plugin;

    public FakePlayerCommand(FakePlayers plugin){
        plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender.hasPermission("fakeplayers.admin")){
            if(args.length >= 1){
                if(args[0].equalsIgnoreCase("setonline")){
                    try{
                        int online = Integer.parseInt(args[1]);
                        plugin.getAPI().setOnline(online);
                        sender.sendMessage(ChatColor.AQUA + "You changed the online players to " + online);
                        plugin.log("New online players: " + online);
                    }catch(Exception e) {
                        plugin.help(sender);
                    }
                }else if(args[0].equalsIgnoreCase("setmax")){
                    try{
                        int max = Integer.parseInt(args[1]);
                        plugin.getAPI().setMax(max);
                        sender.sendMessage(ChatColor.AQUA + "You changed the online players to " + max);
                        plugin.log("New max players: " + max);
                    }catch(Exception e) {
                        plugin.help(sender);
                    }
                }else{
                    plugin.help(sender);
                }
            }else{
                plugin.help(sender);
            }
        }else{
            sender.sendMessage(ChatColor.RED + "FakePlayers" + ChatColor.GOLD + " >>> " + ChatColor.DARK_RED + "You don't have permission to use that!");
        }
        return false;
    }
}

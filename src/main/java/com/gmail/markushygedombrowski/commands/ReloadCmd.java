package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.HLVagtPoster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCmd implements CommandExecutor {
    private HLVagtPoster plugin;

    public ReloadCmd(HLVagtPoster plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender.hasPermission("HLreload"))){
            sender.sendMessage("§aYou do not have permission to do that");
            return true;
        }
        plugin.reload();
        sender.sendMessage("§a§lPlugin reloadet!");
        return true;
    }
}

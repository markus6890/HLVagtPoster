package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.vagtpostutils.LoadRewards;
import com.gmail.markushygedombrowski.vagtpostutils.Rewards;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHeadCommand implements CommandExecutor {
    private LoadRewards loadRewards;

    public SetHeadCommand(LoadRewards loadRewards) {
        this.loadRewards = loadRewards;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender.hasPermission("setHead"))){
            commandSender.sendMessage("§aYou do not have permission to do that");
            return true;
        }
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cDu skal være en spiller for at kunne bruge denne kommando");
            return true;
        }
        Player p = (Player) commandSender;
        if(strings.length <= 1) {
            p.sendMessage("§c/setHead <headID> <block>");
            return true;
        }
        String headID = strings[0];
        String block = strings[1];
        if(loadRewards.getRewards(block) == null) {
            p.sendMessage("§cDenne block findes ikke");
            return true;
        }
        Rewards rewards = loadRewards.getRewards(block);
        HeadDatabaseAPI api = new HeadDatabaseAPI();
        if(api.getItemHead(headID) == null) {
            p.sendMessage("§cDenne headID findes ikke");
            return true;
        }
        rewards.setHead(headID);
        loadRewards.save(rewards);
        p.sendMessage("§aHead sat til " + headID);
        p.sendMessage("§aHead name: " + api.getItemHead(headID).getItemMeta().getDisplayName());


        return true;
    }
}

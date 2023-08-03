package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.utils.Utils;
import com.gmail.markushygedombrowski.vagtpost.VagtPostInfo;
import com.gmail.markushygedombrowski.vagtpost.VagtPostLoader;
import com.gmail.markushygedombrowski.vagtpostutils.LoadRewards;
import com.gmail.markushygedombrowski.vagtpostutils.Rewards;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateVagtPost implements CommandExecutor {

    private VagtPostLoader vagtPostLoader;
    private LoadRewards loadRewards;

    public CreateVagtPost(VagtPostLoader vagtPostLoader, LoadRewards loadRewards) {
        this.vagtPostLoader = vagtPostLoader;
        this.loadRewards = loadRewards;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDu skal være en spiller for at kunne bruge denne kommando");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("vagtpost.create")) {
            p.sendMessage("§cDu har ikke tilladelse til at bruge denne kommando");
            return true;
        }
        if (args.length <= 1) {
            p.sendMessage("§c/vagtpost <number/name>, <cooldown in minutes>");
            return true;
        }
        String name = args[0];
        int cooldown = Integer.parseInt(args[1]);
        Location location = p.getLocation();
        Location blockLocation = Utils.getTargetBlock(p, 5).getLocation();
        String region = getRegion(location);
        Rewards rewards = loadRewards.getRewards(region);

        if (alias.equalsIgnoreCase("replacevagtpost")) {
            String id = name + region;
            if (vagtPostLoader.getVagtPostInfo(id) == null) {
                p.sendMessage("§cDenne vagtpost findes ikke");
                return true;
            }

            VagtPostInfo vagtPostInfo = vagtPostLoader.getVagtPostInfo(id);
            vagtPostInfo.setLocation(blockLocation);
            vagtPostInfo.setRegion(region);
            vagtPostInfo.setCooldown(cooldown);
            return true;

        }
        String id = name + region;
        if (vagtPostLoader.getVagtPostInfo(id) != null) {
            p.sendMessage("§cDenne vagtpost findes allerede");
            return true;
        }
        VagtPostInfo vagtPostInfo = new VagtPostInfo(name, blockLocation, region, rewards, cooldown, id);
        vagtPostLoader.save(vagtPostInfo);


        return true;
    }

    public String getRegion(Location location) {

        if (Utils.isLocInRegion(location, "a")) {
            return "a";
        } else if (Utils.isLocInRegion(location, "b")) {
            return "b";
        } else {
            return "c";
        }
    }

}

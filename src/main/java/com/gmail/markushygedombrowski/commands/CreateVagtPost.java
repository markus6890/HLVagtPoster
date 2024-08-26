package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.utils.Utils;
import com.gmail.markushygedombrowski.vagtpost.VagtPostInfo;
import com.gmail.markushygedombrowski.vagtpost.VagtPostLoader;
import com.gmail.markushygedombrowski.vagtpostutils.LoadRewards;
import com.gmail.markushygedombrowski.vagtpostutils.Rewards;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateVagtPost implements CommandExecutor {

    private VagtPostLoader vagtPostLoader;
    private LoadRewards loadRewards;
    private HolographicDisplaysAPI hologram;

    public CreateVagtPost(VagtPostLoader vagtPostLoader, LoadRewards loadRewards, HolographicDisplaysAPI hologram) {
        this.vagtPostLoader = vagtPostLoader;
        this.loadRewards = loadRewards;
        this.hologram = hologram;
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
        if (alias.equalsIgnoreCase("vagtposthead")) {
            return vagtPostHead(p);

        }
        if (args.length <= 1) {
            p.sendMessage("§c/createvagtpost <number/name>, <cooldown in minutes>");
            return true;
        }
        String name = args[0];
        int cooldown = Integer.parseInt(args[1]);
        Location location = p.getLocation();
        Location blockLocation = Utils.getTargetBlock(p, 5).getLocation();
        String region = getRegion(location);
        Rewards rewards = loadRewards.getRewards(region);

        if (alias.equalsIgnoreCase("replacevagtpost")) {
            return replaceVagtPost(p, name, cooldown, blockLocation, region);

        }
        if(alias.equalsIgnoreCase("deletevagtpost")) {
            return deleteVagtPost(p, name, region);
        }


        String id = name + region;
        if (vagtPostLoader.getVagtPostInfo(id) != null) {
            p.sendMessage("§cDenne vagtpost findes allerede");
            return true;
        }
        VagtPostInfo vagtPostInfo = new VagtPostInfo(name, blockLocation, region, rewards, cooldown, id);
        Location loc = blockLocation.clone().add(0.5, 1.5, 0.5);
        Hologram holo = hologram.createHologram(loc);
        holo.getLines().appendText("§c§l⚠ Vagt Post " + name + " ⚠");


        vagtPostLoader.save(vagtPostInfo);
        PlayerInfoMessages(p, name, cooldown, blockLocation, region);


        return true;
    }

    private boolean vagtPostHead(Player p) {
        if (vagtPostLoader.getHeadTest(p.getUniqueId())) {
            vagtPostLoader.removeHeadTest(p.getUniqueId());
            p.sendMessage("§cDu har slået vagtpost head test fra");
            return true;
        }
        vagtPostLoader.setHeadTest(p.getUniqueId(), true);
        p.sendMessage("§aDu har slået vagtpost head test til");
        return true;
    }

    private boolean replaceVagtPost(Player p, String name, int cooldown, Location blockLocation, String region) {
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

    private boolean deleteVagtPost(Player p, String name, String region) {
        String id = name + region;
        if (vagtPostLoader.getVagtPostInfo(id) == null) {
            p.sendMessage("§cDenne vagtpost findes ikke");
            return true;
        }
        VagtPostInfo vagtPostInfo = vagtPostLoader.getVagtPostInfo(id);
        vagtPostLoader.remove(vagtPostInfo);
        p.sendMessage("§aVagtPosten er blevet slettet");
        return true;
    }

    private void PlayerInfoMessages(Player p, String name, int cooldown, Location blockLocation, String region) {
        p.sendMessage("§aVagtPosten er blevet oprettet");
        p.sendMessage("§aNavn: " + name);
        p.sendMessage("§aRegion: " + region);
        p.sendMessage("§aCooldown: " + cooldown);
        p.sendMessage("block: " + blockLocation);
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

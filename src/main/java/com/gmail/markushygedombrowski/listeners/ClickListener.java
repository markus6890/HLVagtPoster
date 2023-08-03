package com.gmail.markushygedombrowski.listeners;

import com.gmail.markushygedombrowski.gui.PostGUI;
import com.gmail.markushygedombrowski.vagtpost.VagtPostInfo;
import com.gmail.markushygedombrowski.vagtpost.VagtPostLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickListener implements Listener {
    private VagtPostLoader vagtPostLoader;
    private PostGUI postGUI;

    public ClickListener(VagtPostLoader vagtPostLoader, PostGUI postGUI) {
        this.vagtPostLoader = vagtPostLoader;
        this.postGUI = postGUI;
    }

    @EventHandler
    public void vagtPostClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getClickedBlock() == null) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.BEACON) {
            return;
        }
        VagtPostInfo vagtPostInfo = vagtPostLoader.getVagtPostInfo(event.getClickedBlock().getLocation());
        if (vagtPostInfo == null) {
            player.sendMessage("§4Denne vagtpost er ikke registreret");
            return;
        }


        if (!player.hasPermission("vagtpost")) {
            player.sendMessage("§4Du har ikke tilladelse til at bruge vagtpost");
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        postGUI.createGUI(vagtPostInfo, player);

    }

}

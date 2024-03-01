package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.commands.CreateVagtPost;
import com.gmail.markushygedombrowski.commands.ReloadCmd;
import com.gmail.markushygedombrowski.commands.SetHeadCommand;
import com.gmail.markushygedombrowski.cooldown.Cooldown;
import com.gmail.markushygedombrowski.gui.PostGUI;
import com.gmail.markushygedombrowski.listeners.ClickListener;
import com.gmail.markushygedombrowski.settings.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.vagtpost.VagtPostLoader;
import com.gmail.markushygedombrowski.vagtpostutils.HotBarMessage;
import com.gmail.markushygedombrowski.vagtpostutils.LoadRewards;
import com.gmail.markushygedombrowski.vagtpostutils.VagtPostManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HLVagtPoster extends JavaPlugin {

    private VagtPostManager vagtPostManager;
    private VagtPostLoader vagtPostLoader;
    private LoadRewards loadRewards;
    public Economy econ = null;

    public void onEnable() {
        loadManagers();
        PlayerProfiles playerProfiles = HLvagt.getInstance().getPlayerProfiles();
        HotBarMessage hotBarMessage = new HotBarMessage();

        PostGUI postGUI = new PostGUI(this, loadRewards, hotBarMessage, playerProfiles);
        Bukkit.getServer().getPluginManager().registerEvents(postGUI, this);
        ClickListener clickListener = new ClickListener(vagtPostLoader, postGUI);
        Bukkit.getServer().getPluginManager().registerEvents(clickListener, this);
        CreateVagtPost createVagtPost = new CreateVagtPost(vagtPostLoader,loadRewards);
        getCommand("createvagtpost").setExecutor(createVagtPost);
        SetHeadCommand setHeadCommand = new SetHeadCommand(loadRewards);
        getCommand("sethead").setExecutor(setHeadCommand);

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        System.out.println("==================================");
        System.out.println("HLVagtPoster enabled");
        System.out.println("==================================");

        CompletableFuture<Void> countDown = coodown();
        try {
            countDown.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        ReloadCmd reloadCmd = new ReloadCmd(this);
        getCommand("reloadvagtpost").setExecutor(reloadCmd);

    }
    public CompletableFuture<Void> coodown() {
        return CompletableFuture.runAsync(() -> {
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

                @Override
                public void run() {
                    Cooldown.handleCooldowns();
                }
            }, 1L, 1L);
        });
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    public void loadManagers() {
        vagtPostManager = new VagtPostManager();
        vagtPostManager.setup();
        vagtPostManager.saveVagtPost();
        vagtPostManager.reloadVagtPost();
        vagtPostManager.saveReward();
        vagtPostManager.reloadReward();
        loadRewards = new LoadRewards(vagtPostManager);
        loadRewards.load();
        vagtPostLoader = new VagtPostLoader(vagtPostManager,loadRewards);
        vagtPostLoader.load();


    }
    public void reload() {
        reloadConfig();
        loadManagers();

    }
    public void onDisable() {
        System.out.println("==================================");
        System.out.println("HLVagtPoster disabled");
        System.out.println("==================================");
    }
}

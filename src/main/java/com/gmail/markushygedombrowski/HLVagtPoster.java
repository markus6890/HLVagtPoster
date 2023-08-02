package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.commands.CreateVagtPost;
import com.gmail.markushygedombrowski.gui.PostGUI;
import com.gmail.markushygedombrowski.listeners.ClickListener;
import com.gmail.markushygedombrowski.vagtpost.VagtPostLoader;
import com.gmail.markushygedombrowski.vagtpostutils.LoadRewards;
import com.gmail.markushygedombrowski.vagtpostutils.VagtPostManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class HLVagtPoster extends JavaPlugin {

    private VagtPostManager vagtPostManager;
    private VagtPostLoader vagtPostLoader;
    private LoadRewards loadRewards;

    public void onEnable() {

        loadManagers();
        PostGUI postGUI = new PostGUI(this);
        Bukkit.getServer().getPluginManager().registerEvents(postGUI, this);
        ClickListener clickListener = new ClickListener(vagtPostLoader);
        Bukkit.getServer().getPluginManager().registerEvents(clickListener, this);
        CreateVagtPost createVagtPost = new CreateVagtPost(vagtPostLoader,loadRewards);
        getCommand("createvagtpost").setExecutor(createVagtPost);
        System.out.println("==================================");
        System.out.println("HLVagtPoster enabled");
        System.out.println("==================================");

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

package com.gmail.markushygedombrowski.vagtpostutils;

import com.gmail.markushygedombrowski.HLVagtPoster;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class VagtPostManager {
    private HLVagtPoster plugin = HLVagtPoster.getPlugin(HLVagtPoster.class);
    public FileConfiguration vagtpostcfg;
    public File vagtpostfile;
    public FileConfiguration rewardcfg;
    public File rewardfile;

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        vagtpostfile = new File(plugin.getDataFolder(), "vagtpost.yml");
        rewardfile = new File(plugin.getDataFolder(), "reward.yml");
        if (!vagtpostfile.exists()) {
            try {
                vagtpostfile.createNewFile();
                rewardfile.createNewFile();
            } catch (Exception e) {
                System.out.println("could not create vagtpost.yml File");
            }
        }
        vagtpostcfg = YamlConfiguration.loadConfiguration(vagtpostfile);
        rewardcfg = YamlConfiguration.loadConfiguration(rewardfile);
    }

    public FileConfiguration getReward() {
        return rewardcfg;
    }
    public FileConfiguration getVagtPost() {
        return vagtpostcfg;
    }
    public void saveReward(){
        try{
            rewardcfg.save(rewardfile);
        } catch (Exception e) {
            System.out.println("could not save reward.yml File");
        }
    }
    public void saveVagtPost() {
        try{
            vagtpostcfg.save(vagtpostfile);
        } catch (Exception e) {
            System.out.println("could not save vagtpost.yml File");
        }
    }
    public void reloadReward() {
        rewardcfg = YamlConfiguration.loadConfiguration(rewardfile);
    }
    public void reloadVagtPost() {
        vagtpostcfg = YamlConfiguration.loadConfiguration(vagtpostfile);
    }
}

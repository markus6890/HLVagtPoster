package com.gmail.markushygedombrowski.vagtpost;

import com.gmail.markushygedombrowski.vagtpostutils.LoadRewards;
import com.gmail.markushygedombrowski.vagtpostutils.Rewards;
import com.gmail.markushygedombrowski.vagtpostutils.VagtPostManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class VagtPostLoader {

    private Map<Location, VagtPostInfo> vagtPostMap = new HashMap<>();
    private Map<String, VagtPostInfo> vagtPostNameMap = new HashMap<>();
    private VagtPostManager configM;
    private LoadRewards loadRewards;

    public VagtPostLoader(VagtPostManager configM, LoadRewards loadRewards) {
        this.configM = configM;
        this.loadRewards = loadRewards;
    }

    public void load() {
        FileConfiguration config = configM.getVagtPost();
        vagtPostMap.clear();
        config.getConfigurationSection("vagtpost").getKeys(false).forEach(key -> {
            String path = "vagtpost." + key + ".";
            String name = config.getString(path + "name");
            Location location = (Location) config.get(path + "location");
            String region = config.getString(path + "region");
            int cooldown = config.getInt(path + "cooldown");
            Rewards rewards = loadRewards.getRewards(region);
            cooldown = cooldown * 60;
            VagtPostInfo vagtPostInfo = new VagtPostInfo(name, location, region, rewards, cooldown);
            vagtPostMap.put(location, vagtPostInfo);
            vagtPostNameMap.put(vagtPostInfo.getName() + vagtPostInfo.getRegion(), vagtPostInfo);
        });
    }

    public void save(VagtPostInfo vagtPostInfo) {
        FileConfiguration config = configM.getVagtPost();
        String name = vagtPostInfo.getName();
        Location location = vagtPostInfo.getLocation();
        String region = vagtPostInfo.getRegion();
        config.set("vagtpost.", name);
        config.set("vagtpost." + name + ".name", name);
        config.set("vagtpost." + name + ".location", location);
        config.set("vagtpost." + name + ".region", region);
        configM.saveVagtPost();
        vagtPostMap.put(vagtPostInfo.getLocation(), vagtPostInfo);
        vagtPostNameMap.put(vagtPostInfo.getName() + vagtPostInfo.getRegion(), vagtPostInfo);
    }

    public VagtPostInfo getVagtPostInfo(Location vagtPost) {
        return vagtPostMap.get(vagtPost);
    }

    public VagtPostInfo getVagtPostInfo(String name, String region) {
        return vagtPostNameMap.get(name + region);
    }

}

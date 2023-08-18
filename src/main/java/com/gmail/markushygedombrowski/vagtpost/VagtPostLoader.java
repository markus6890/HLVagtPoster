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
            cooldown = (cooldown * 60);
            VagtPostInfo vagtPostInfo = new VagtPostInfo(name, location, region, rewards, cooldown, key);
            vagtPostMap.put(location, vagtPostInfo);
            vagtPostNameMap.put(vagtPostInfo.getId(), vagtPostInfo);
        });
    }

    public void save(VagtPostInfo vagtPostInfo) {
        FileConfiguration config = configM.getVagtPost();
        String id = vagtPostInfo.getId();
        config.set("vagtpost." + id + ".name", vagtPostInfo.getName());
        config.set("vagtpost." + id + ".location", vagtPostInfo.getLocation());
        config.set("vagtpost." + id + ".region", vagtPostInfo.getRegion());
        config.set("vagtpost." + id + ".cooldown", vagtPostInfo.getCooldown());
        configM.saveVagtPost();
        vagtPostMap.put(vagtPostInfo.getLocation(), vagtPostInfo);
        vagtPostNameMap.put(vagtPostInfo.getName() + vagtPostInfo.getRegion(), vagtPostInfo);
    }
    public void remove(VagtPostInfo vagtPostInfo) {
        FileConfiguration config = configM.getVagtPost();
        String id = vagtPostInfo.getId();
        config.set("vagtpost." + id, null);
        configM.saveVagtPost();
        vagtPostMap.remove(vagtPostInfo.getLocation());
        vagtPostNameMap.remove(vagtPostInfo.getName() + vagtPostInfo.getRegion());
    }

    public VagtPostInfo getVagtPostInfo(Location vagtPost) {
        return vagtPostMap.get(vagtPost);
    }

    public VagtPostInfo getVagtPostInfo(String name) {
        return vagtPostNameMap.get(name);
    }

}

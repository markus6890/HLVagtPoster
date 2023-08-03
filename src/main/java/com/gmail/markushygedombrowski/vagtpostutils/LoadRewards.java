package com.gmail.markushygedombrowski.vagtpostutils;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class LoadRewards {
    private Map<String, Rewards> rewardsMap = new HashMap<>();
    private VagtPostManager vagtPostManager;

    public LoadRewards(VagtPostManager vagtPostManager) {
        this.vagtPostManager = vagtPostManager;
    }

    public void load() {
        FileConfiguration config = vagtPostManager.getReward();
        rewardsMap.clear();
        config.getConfigurationSection("rewards").getKeys(false).forEach(key -> {
            String path = "rewards." + key + ".";
            String region = config.getString(path + "region");
            int minmoney = config.getInt(path + "minmoney");
            int maxmoney = config.getInt(path + "maxmoney");
            String head = config.getString(path + "head");
            double chance = config.getDouble(path + "head.chance");
            int droped = config.getInt(path + "head.droped");
            int goldngmin = config.getInt(path + "goldngmin");
            int goldngmax = config.getInt(path + "goldngmax");
            double goldchance = config.getDouble(path + "goldchance");

            Rewards rewards = new Rewards(region, minmoney, maxmoney, head, chance, goldngmin, goldngmax, goldchance, droped);
            rewardsMap.put(key, rewards);
        });
    }
    public void save(Rewards rewards) {
        FileConfiguration config = vagtPostManager.getReward();
        config.set("rewards." + rewards.getRegion() + ".region", rewards.getRegion());
        config.set("rewards." + rewards.getRegion() + ".minmoney", rewards.getMinmoney());
        config.set("rewards." + rewards.getRegion() + ".maxmoney", rewards.getMaxmoney());
        config.set("rewards." + rewards.getRegion() + ".head", rewards.getHead());
        config.set("rewards." + rewards.getRegion() + ".chance", rewards.getChance());

        vagtPostManager.saveReward();
        rewardsMap.put(rewards.getRegion(), rewards);
    }
    public Rewards getRewards(String region) {
        return rewardsMap.get(region);
    }
}

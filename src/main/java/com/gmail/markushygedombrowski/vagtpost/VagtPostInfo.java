package com.gmail.markushygedombrowski.vagtpost;


import com.gmail.markushygedombrowski.vagtpostutils.Rewards;
import org.bukkit.Location;

public class VagtPostInfo {
    private String name;



    private Location location;
    private String region;
    private Rewards reward;
    private int cooldown;
    private String id;



    public VagtPostInfo(String name, Location location, String region, Rewards reward, int cooldown, String id) {
        this.name = name;
        this.location = location;
        this.region = region;
        this.reward = reward;
        this.cooldown = cooldown;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public Location getLocation() {
        return location;
    }
    public String getRegion() {
        return region;
    }
    public Rewards getReward() {

        return reward;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public int getCooldown() {
        return cooldown;
    }
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown * 60;
    }
    public void setReward(Rewards reward) {
        this.reward = reward;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}

package com.gmail.markushygedombrowski.vagtpostutils;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.inventory.ItemStack;

public class Rewards {
    private String region;
    private int minmoney;
    private int maxmoney;
    private String head;
    private ItemStack headItem;
    private double chance;
    private int goldngmin;
    private int goldngmax;
    private double goldchance;

    private int droped;


    public Rewards(String region, int minmoney, int maxmoney, String head, double chance, int goldngmin, int goldngmax, double goldchance, int droped) {
        this.region = region;
        this.minmoney = minmoney;
        this.maxmoney = maxmoney;
        this.head = head;
        this.chance = chance;
        this.goldngmin = goldngmin;
        this.goldngmax = goldngmax;
        this.goldchance = goldchance;
        this.droped = droped;
    }

    public int getMaxmoney() {
        return maxmoney;
    }

    public int getMinmoney() {
        return minmoney;
    }

    public String getRegion() {
        return region;
    }

    public String getHead() {
        return head;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setMinmoney(int minmoney) {
        this.minmoney = minmoney;
    }

    public void setMaxmoney(int maxmoney) {
        this.maxmoney = maxmoney;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public ItemStack getHeadItem() {
        HeadDatabaseAPI api = new HeadDatabaseAPI();
        headItem = api.getItemHead(head);
        if(headItem == null) {
            System.out.println("§cHead not found: " + head);
            return null;
        }
        return headItem;
    }

    public int getGoldngmin() {
        return goldngmin;
    }

    public void setGoldngmin(int goldngmin) {
        this.goldngmin = goldngmin;
    }

    public int getGoldngmax() {
        return goldngmax;
    }

    public void setGoldngmax(int goldngmax) {
        this.goldngmax = goldngmax;
    }

    public double getGoldchance() {
        return goldchance;
    }

    public void setGoldchance(double goldchance) {
        this.goldchance = goldchance;
    }

    public int getDroped() {
        return droped;
    }

    public void setDroped(int droped) {
        this.droped = droped;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}

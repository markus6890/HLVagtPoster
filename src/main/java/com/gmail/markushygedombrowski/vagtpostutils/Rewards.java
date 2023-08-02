package com.gmail.markushygedombrowski.vagtpostutils;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.arcaniax.hdb.object.head.Head;
import org.bukkit.inventory.ItemStack;

public class Rewards {
    private String region;
    private int minmoney;
    private int maxmoney;
    private String head;
    private ItemStack headItem;
    private double chance;


    public Rewards(String region, int minmoney, int maxmoney, String head, double chance) {
        this.region = region;
        this.minmoney = minmoney;
        this.maxmoney = maxmoney;
        this.head = head;
        this.chance = chance;
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
        return headItem;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}

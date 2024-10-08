package com.gmail.markushygedombrowski.gui;


import com.gmail.markushygedombrowski.HLVagtPoster;
import com.gmail.markushygedombrowski.cooldown.Cooldown;
import com.gmail.markushygedombrowski.cooldown.UtilTime;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;

import com.gmail.markushygedombrowski.vagtpost.VagtPostInfo;
import com.gmail.markushygedombrowski.vagtpost.VagtPostLoader;
import com.gmail.markushygedombrowski.vagtpostutils.HotBarMessage;
import com.gmail.markushygedombrowski.vagtpostutils.LoadRewards;
import com.gmail.markushygedombrowski.vagtpostutils.Rewards;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PostGUI implements Listener {
    private final int BUTTON_SLOT = 13;

    private Map<Player, VagtPostInfo> playerVagtPostInfoMap = new HashMap<>();
    private List<Player> playerList = new ArrayList<>();
    private HLVagtPoster plugin;
    private LoadRewards loadRewards;
    private HotBarMessage hotBarMessage;
    private PlayerProfiles playerProfiles;

    private VagtPostLoader vagtPostLoader;

    public PostGUI(HLVagtPoster plugin, LoadRewards loadRewards, HotBarMessage hotBarMessage, PlayerProfiles playerProfiles, VagtPostLoader vagtPostLoader) {
        this.plugin = plugin;
        this.loadRewards = loadRewards;
        this.hotBarMessage = hotBarMessage;
        this.playerProfiles = playerProfiles;
        this.vagtPostLoader = vagtPostLoader;
    }

    public void createGUI(VagtPostInfo vagtPostInfo, Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Vagt Post " + vagtPostInfo.getName());
        ItemStack clickItem;
        ItemStack glassItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);

        ItemMeta itemMeta;
        if (Cooldown.isCooling(player.getName(), vagtPostInfo.getName() + vagtPostInfo.getRegion())) {
            clickItem = coolDownItem(vagtPostInfo, player);
            glassItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        } else {
            clickItem = aktivItem(vagtPostInfo);
        }
        inventory.setItem(BUTTON_SLOT, clickItem);
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) {
                inventory.setItem(inventory.firstEmpty(), glassItem);
            }
        }
        player.openInventory(inventory);
        playerVagtPostInfoMap.put(player, vagtPostInfo);

    }

    private ItemStack coolDownItem(VagtPostInfo vagtPostInfo, Player player) {
        ItemMeta itemMeta;
        ItemStack clickItem;
        clickItem = new ItemStack(Material.BARRIER);
        itemMeta = clickItem.getItemMeta();
        itemMeta.setDisplayName("§4§lVagt Post");
        List<String> lore = new ArrayList<>();
        lore.add("§cDu kan tag vagtposten om §b" + Cooldown.getRemaining(player.getName(), vagtPostInfo.getName() + vagtPostInfo.getRegion()) + " §c" + UtilTime.getTimestamp());
        itemMeta.setLore(lore);
        clickItem.setItemMeta(itemMeta);
        return clickItem;
    }

    private ItemStack aktivItem(VagtPostInfo vagtPostInfo) {
        ItemMeta itemMeta;
        ItemStack clickItem;
        clickItem = new ItemStack(Material.EMERALD);
        itemMeta = clickItem.getItemMeta();
        itemMeta.setDisplayName("§6§lVagt Post");
        List<String> lore = new ArrayList<>();
        lore.add("§aKlik for at tage vagtposten");
        Rewards rewards = vagtPostInfo.getReward();
        lore.add(" ");
        lore.add("§9§lBelønning:");
        System.out.println("chance: " + rewards.getChance());
        lore.add("§7Du har §a" + rewards.getChance() + "% §7chance for at få:§b Random Head");
        lore.add("§7Du har §a" + rewards.getGoldchance() + "% §7chance for at få mellem: §6" + rewards.getGoldngmin() + " §7og §6" + rewards.getGoldngmax() + " §7gold");
        lore.add("§7Du kan få mellem §a" + rewards.getMinmoney() + "$ §7og §a" + rewards.getMaxmoney() + "$");
        lore.add("§7du for §b" + rewards.getExp() + " §3exp");
        lore.add(" ");
        lore.add("§c§lVIGTIGT!");
        lore.add("§7Fangerne kan se for du tager vagtposten");
        itemMeta.setLore(lore);
        clickItem.setItemMeta(itemMeta);
        return clickItem;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        Inventory inventory = event.getInventory();
        if (inventory.getName().contains("Vagt Post")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            if (clickedSlot == BUTTON_SLOT) {
                if (itemStack.getType() == Material.BARRIER) {
                    player.sendMessage("§cDu kan ikke tage vagtposten nu!");
                    player.closeInventory();
                    return;
                }
                if (itemStack.getType() == Material.EMERALD) {
                    if (!player.getUniqueId().equals(UUID.fromString("0ea61ef8-45e7-42b4-b775-5ac2b01ebb3d"))) {
                        if (player.getGameMode() != GameMode.SURVIVAL) {
                            player.sendMessage("§4Hmmmm nej smut du snyder");
                            return;
                        }
                    }

                    takePost(player, playerVagtPostInfoMap.get(player).getName() + playerVagtPostInfoMap.get(player).getRegion());
                }
            }
        }

    }

    public void takePost(Player player, String name) {
        VagtPostInfo vagtPostInfo = playerVagtPostInfoMap.get(player);
        if (Cooldown.isCooling(player.getName(), name)) {
            player.sendMessage("§cDu kan ikke tage vagtposten nu!");
            return;
        }
        Cooldown.add(player.getName(), name, vagtPostInfo.getCooldown(), System.currentTimeMillis());
        player.closeInventory();
        Bukkit.broadcastMessage("§c§l------§4§lVagt Post§c§l------");
        Bukkit.broadcastMessage("§e" + player.getName() + " §a er í gang med at tage en vagtpost§a! §7(" + vagtPostInfo.getRegion().toUpperCase() + ")");
        Bukkit.broadcastMessage("§c§l------§4§lVagt Post§c§l------");
        player.sendMessage("§a§l----------------------------------------");
        player.sendMessage("§aStå stille i §c20 §asekunder for at få belønningen");
        player.sendMessage("§a§l----------------------------------------");
        playerList.add(player);
        delay(player);

    }

    private void delay(Player player) {
        getMessageboxes(player);


        rewardCooldown(player);


    }

    private void getMessageboxes(Player player) {
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                if (player.isOnline()) {
                    if (playerList.contains(player)) {
                        hotBarMessage.sendActionbar(player, "§aStå stille i §c" + (20 - counter) + " §asekunder for at få belønningen");
                        counter++;

                    } else {
                        counter = 0;
                        cancel();
                    }
                } else {
                    counter = 0;
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    private void rewardCooldown(Player player) {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    if (playerList.contains(player)) {
                        getReward(player);
                        playerList.remove(player);

                    }
                }
            }
        }.runTaskLater(plugin, 20 * 20);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            if (playerList.contains(player)) {
                playerList.remove(player);
                player.sendMessage("§cDu bevægede dig, så du fik ikke din belønning");
            }
        }


    }

    private void getReward(Player player) {
        VagtPostInfo vagtPostInfo = playerVagtPostInfoMap.get(player);
        Rewards rewards = vagtPostInfo.getReward();
        int money = ThreadLocalRandom.current().nextInt(rewards.getMinmoney(), rewards.getMaxmoney() + 1);
        Random r = new Random();
        int exp = r.nextInt(rewards.getExp());
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
        player.sendMessage("§a§l----------------------------------------");
        player.sendMessage("§aDu har nu fået din belønning");
        player.sendMessage("§aDu fik §b" + money + "$");
        head(player, rewards);
        goldNugget(player, rewards);
        plugin.econ.depositPlayer(player, money);
        player.sendMessage("§aDu fik §b" + exp + " §3exp");
        player.sendMessage("§a§l----------------------------------------");
        Bukkit.broadcastMessage("§c§l------§4§lVagt Post§c§l------");
        Bukkit.broadcastMessage("§e" + player.getName() + " §ahar taget en vagtpost§a! §7(" + vagtPostInfo.getRegion().toUpperCase() + ")");
        Bukkit.broadcastMessage("§c§l------§4§lVagt Post§c§l------");
        PlayerProfile playerProfile = playerProfiles.getPlayerProfile(player.getUniqueId());
        double exp2 = playerProfile.castPropertyToInt(playerProfile.getProperty("exp")) + exp;
        double vagtposter1 = playerProfile.castPropertyToInt(playerProfile.getProperty("vagtposter"));
        System.out.println("vagtposter: " + vagtposter1);
        System.out.println("exp: " + exp2);
        playerProfile.setProperty("vagtposter", vagtposter1 + 1);
        playerProfile.setXp(exp2);
    }

    private void head(Player player, Rewards rewards) {
        if (procent(rewards.getChance()) || vagtPostLoader.getHeadTest(player.getUniqueId())) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            ItemStack itemStack = api.getRandomHead();
            player.sendMessage("§aDu fik også §b" + itemStack.getItemMeta().getDisplayName());

            player.getInventory().addItem(itemStack);
            rewards.setDroped(rewards.getDroped() + 1);
            loadRewards.save(rewards);

        }
    }

    private void goldNugget(Player player, Rewards rewards) {
        if (procent(rewards.getGoldchance())) {
            int gold = ThreadLocalRandom.current().nextInt(rewards.getGoldngmin(), rewards.getGoldngmax() + 1);
            player.sendMessage("§aDu fik også §6" + gold + " §agold");
            player.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, gold));
        }
    }

    public boolean procent(double pro) {
        Random r = new Random();
        double num = r.nextInt(100);
        return num <= pro;

    }

}

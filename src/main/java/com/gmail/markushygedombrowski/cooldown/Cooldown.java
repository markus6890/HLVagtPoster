package com.gmail.markushygedombrowski.cooldown;

import java.util.HashMap;
import java.util.Iterator;

public class Cooldown {


    private final UtilTime utilTime;
    public static HashMap<String, VagtPostAbilityCooldown> cooldownPlayers = new HashMap<>();

    public Cooldown(UtilTime utilTime) {
        this.utilTime = utilTime;
    }


    public static void add(String player, String ability, long seconds, long systime) {
        if (!cooldownPlayers.containsKey(player)) cooldownPlayers.put(player, new VagtPostAbilityCooldown(player));
        if (isCooling(player, ability)) return;
        cooldownPlayers.get(player).cooldownMap.put(ability, new VagtPostAbilityCooldown(player, seconds * 1000, System.currentTimeMillis()));
    }

    public static boolean isCooling(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) return false;
        return cooldownPlayers.get(player).cooldownMap.containsKey(ability);
    }

    public static double getRemaining(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) return 0.0;
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) return 0.0;
        return UtilTime.convert((cooldownPlayers.get(player).cooldownMap.get(ability).seconds + cooldownPlayers.get(player).cooldownMap.get(ability).systime) - System.currentTimeMillis(), TimeUnit.BEST, 1);
    }


    public static void removeCooldown(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) {
            return;
        }
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) {
            return;
        }
        cooldownPlayers.get(player).cooldownMap.remove(ability);

    }


    public static void handleCooldowns() {
        if (cooldownPlayers.isEmpty()) {
            return;
        }
        new HashMap<>(cooldownPlayers).forEach((player, abilityCooldown) -> {
            if (abilityCooldown.cooldownMap.isEmpty()) {
                return;
            }
            abilityCooldownLoop(player, abilityCooldown);
        });
    }

    private static void abilityCooldownLoop(String player, VagtPostAbilityCooldown abilityCooldown) {
        abilityCooldown.cooldownMap.forEach((ability, cooldown) -> {
            if (ability == null) {
                System.out.println("Ability is null");
                return;
            }
            if (getRemaining(player, ability) <= 0.0) {
                removeCooldown(player, ability);
            }

        });
    }
}

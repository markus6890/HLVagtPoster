package com.gmail.markushygedombrowski.cooldown;

import java.util.HashMap;

public class VagtPostAbilityCooldown {
    public String ability = "";
    public String player;
    public long seconds;
    public long systime;

    public VagtPostAbilityCooldown(String player, long seconds, long systime) {
        this.player = player;
        this.seconds = seconds;
        this.systime = systime;
    }
    public VagtPostAbilityCooldown(String player) {
        this.player = player;
    }
    public HashMap<String, VagtPostAbilityCooldown> cooldownMap = new HashMap<String, VagtPostAbilityCooldown>();
    public String getAbility() {
        return ability;
    }


}

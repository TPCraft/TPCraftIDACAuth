package net.tpcraft.minecraft.event;

import net.tpcraft.minecraft.TPCraftIDACAuth;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (TPCraftIDACAuth.notLoginPlayers.containsValue(player)) {
            TPCraftIDACAuth.notLoginPlayers.entrySet().removeIf(entry -> player == entry.getValue());
        }

        if (TPCraftIDACAuth.isLoginPlayers.get(player) != null) {
            TPCraftIDACAuth.isLoginPlayers.remove(player);
        }

        if (TPCraftIDACAuth.config.getCoverInfo()) {
            event.setQuitMessage(ChatColor.translateAlternateColorCodes('&',
                    "[&4-&r] &e" + player.getDisplayName()
            ));
        }
    }
}

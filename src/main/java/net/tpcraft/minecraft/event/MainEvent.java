package net.tpcraft.minecraft.event;

import net.tpcraft.minecraft.TPCraftIDACAuth;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class MainEvent implements Listener {
    private boolean checkIsLogin(Player player) {
        return TPCraftIDACAuth.isLoginPlayers.get(player) == null;
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            event.setCancelled(checkIsLogin(((Player) event.getWhoClicked()).getPlayer()));
        }
    }

    @EventHandler
    public void PlayerPortalEvent(PlayerPortalEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void PlayerPickupItemEvent(PlayerPickupItemEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void PlayerItemHeldEvent(PlayerItemHeldEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(checkIsLogin(((Player) event.getEntity()).getPlayer()));
        }
    }

    @EventHandler
    public void PlayerTeleportEvent(PlayerTeleportEvent event) {
        PlayerTeleportEvent.TeleportCause teleportCause = event.getCause();

        if (teleportCause == PlayerTeleportEvent.TeleportCause.PLUGIN || teleportCause == PlayerTeleportEvent.TeleportCause.COMMAND) {
            return;
        }

        event.setCancelled(checkIsLogin(event.getPlayer()));
    }
}

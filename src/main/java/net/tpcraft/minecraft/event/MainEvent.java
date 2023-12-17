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
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            event.setCancelled(checkIsLogin(((Player) event.getWhoClicked()).getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(checkIsLogin(((Player) event.getEntity()).getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        event.setCancelled(checkIsLogin(event.getPlayer()));
    }
}

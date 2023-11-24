package net.tpcraft.minecraft.event;

import net.tpcraft.minecraft.TPCraftIDACAuth;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class MainEvent implements Listener {
    public boolean checkIsLogin(Player player) {
        return TPCraftIDACAuth.isLoginPlayers.get(player) == null;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String state = UUID.randomUUID().toString().replaceAll("-", "");

        TPCraftIDACAuth.notLoginPlayers.put(state, player);

        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                " " + TPCraftIDACAuth.prefix + "授权"
        ));
        player.sendMessage("");
        player.sendMessage(" 点击下面的链接进行授权(打开聊天框单击链接):");
        player.sendMessage(" " + ChatColor.UNDERLINE +
                "https://auth.tpcraft.net/oauth2/authorize?client_id=" + TPCraftIDACAuth.config.getClientId() +
                "&redirect_uri=" + TPCraftIDACAuth.config.getRedirectUri() +
                "&response_type=code&state=" + state);
        player.sendMessage("");

        final int[] second = {120};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (TPCraftIDACAuth.notLoginPlayers.get(state) == null) {
                    this.cancel();
                    return;
                }
                second[0]--;
                if (second[0] == 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            " " + TPCraftIDACAuth.prefix + "授权已超时"
                    ));
                }
            }
        }.runTaskTimer(TPCraftIDACAuth.plugin, 0, 20);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (TPCraftIDACAuth.notLoginPlayers.containsValue(player)) {
            TPCraftIDACAuth.notLoginPlayers.entrySet().removeIf(entry -> player == entry.getValue());
        }

        if (TPCraftIDACAuth.isLoginPlayers.get(player) != null) {
            TPCraftIDACAuth.isLoginPlayers.remove(player);
        }
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

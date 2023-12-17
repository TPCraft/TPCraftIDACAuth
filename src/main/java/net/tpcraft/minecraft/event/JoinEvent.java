package net.tpcraft.minecraft.event;

import com.google.gson.Gson;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.tpcraft.minecraft.TPCraftIDACAuth;
import net.tpcraft.minecraft.util.HTTPRequest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerLoginEvent(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (!TPCraftIDACAuth.loaded) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "插件未加载完成，请稍后重试"
            ));
            return;
        }

        if (TPCraftIDACAuth.config.getLimitMode() && !TPCraftIDACAuth.config.getAllowPlayers().contains(player.getDisplayName())) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "已启用限制模式，您不存在白名单内"
            ));
            return;
        }

        String responseGetUser = HTTPRequest.sendGetRequest(
                "https://api.tpcraft.net/user/username/" + player.getDisplayName(),
                new HashMap<>(),
                new HashMap<>()
        );

        if (responseGetUser.equals("ERROR")) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "插件内部错误，请联系管理员"
            ));
        } else {
            Map<String, Object> response = new Gson().fromJson(responseGetUser, Map.class);
            if (response.get("status").toString().equals("false")) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                        TPCraftIDACAuth.prefix + "此账户未注册，请到 auth.tpcraft.net 注册"
                ));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String state = UUID.randomUUID().toString().replaceAll("-", "");

        TPCraftIDACAuth.notLoginPlayers.put(state, player);

        ComponentBuilder componentBuilder = new ComponentBuilder("打开聊天框点击此处授权(120秒超时)")
                .bold(true)
                .underlined(true)
                .event(new ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://auth.tpcraft.net/oauth2/authorize?" +
                                "client_id=" + TPCraftIDACAuth.config.getClientId() +
                                "&redirect_uri=" + TPCraftIDACAuth.config.getRedirectUri() +
                                "&response_type=code" +
                                "&state=" + state
                ));

        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                TPCraftIDACAuth.prefix + "授权登入"
        ));
        player.sendMessage("");
        player.spigot().sendMessage(componentBuilder.create());
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
                    player.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                            TPCraftIDACAuth.prefix + "授权已超时"
                    ));
                }
            }
        }.runTaskTimer(TPCraftIDACAuth.plugin, 0, 20);

        if (TPCraftIDACAuth.config.getCoverInfo()) {
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&',
                    "[&2+&r] &e" + player.getDisplayName()
            ));
        }
    }
}

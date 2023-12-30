package net.tpcraft.minecraft.event;

import com.google.gson.Gson;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.tpcraft.minecraft.Config;
import net.tpcraft.minecraft.TPCraftIDACAuth;
import net.tpcraft.minecraft.util.HTTPRequest;
import net.tpcraft.minecraft.util.MapList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.tpcraft.minecraft.util.Moment.timestampToDate;

public class JoinEvent implements Listener {
    private Boolean checkAutoLogin(Player player, String state) {
        Config config = TPCraftIDACAuth.config;
        List<Map<?, ?>> autoLoginData = TPCraftIDACAuth.autoLoginData;

        Map<?, ?> playerData = MapList.search(autoLoginData, "name", player.getDisplayName());
        if (playerData == null) {
            return false;
        }

        String ip = String.valueOf(playerData.get("ip"));
        Long lastLoginAt = Long.parseLong(String.valueOf(playerData.get("lastLoginAt")));

        if (!ip.equals(player.getAddress().getAddress().getHostAddress())) {
            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "IP地址已变更，请重新授权登入"
            ));
            player.sendMessage("");
            return false;
        }

        if (lastLoginAt + (config.getAutoLoginExpires() * 1000) < System.currentTimeMillis()) {
            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "自动登入已超时，请重新授权登入"
            ));
            player.sendMessage("");
            return false;
        }

        TPCraftIDACAuth.isLoginPlayers.put(player, state);

        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                TPCraftIDACAuth.prefix + "已自动登入，上次登入的时间为：" + timestampToDate(lastLoginAt)
        ));
        player.sendMessage("");

        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerLoginEvent(PlayerLoginEvent event) {
        Config config = TPCraftIDACAuth.config;

        Player player = event.getPlayer();

        if (!TPCraftIDACAuth.loaded) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "插件未加载完成，请稍后重试"
            ));
            return;
        }

        if (config.getWhiteListEnable() && !config.getWhiteListAllowPlayers().contains(player.getDisplayName())) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "已启用白名单，您不存在白名单内"
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
                    TPCraftIDACAuth.prefix + "授权错误\n\n错误类型：内部错误\n错误原因：获取用户信息时发生错误\n若出现此错误，您可以将此错误反馈给服务器管理员"
            ));
        } else {
            Map<String, Object> response = new Gson().fromJson(responseGetUser, Map.class);
            if (response.get("status").toString().equals("false")) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                        TPCraftIDACAuth.prefix + "此用户名未注册，请到 https://auth.tpcraft.net 注册"
                ));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Config config = TPCraftIDACAuth.config;

        Player player = event.getPlayer();
        String state = UUID.randomUUID().toString().replaceAll("-", "");

        if (config.getLoginMessageEnable()) {
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getLoginMessageJoin().replace("%player%", player.getDisplayName())
            ));
        }

        if (config.getLoginPositionEnable()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(new Location(
                            player.getWorld(),
                            config.getLoginPositionX(),
                            config.getLoginPositionY(),
                            config.getLoginPositionZ())
                    );
                }
            }.runTask(TPCraftIDACAuth.plugin);
        }

        if (config.getAutoLoginEnable() && checkAutoLogin(player, state)) {
            return;
        }

        TPCraftIDACAuth.notLoginPlayers.put(state, player);

        ComponentBuilder componentBuilder = new ComponentBuilder("打开聊天框点击此处授权(120秒超时)")
                .bold(true)
                .underlined(true)
                .event(new ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://auth.tpcraft.net/oauth2/authorize?" +
                                "client_id=" + config.getOauth2ClientId() +
                                "&redirect_uri=" + config.getOauth2RedirectUri() +
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
    }
}

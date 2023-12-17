package net.tpcraft.minecraft.http;

import com.google.gson.Gson;
import net.tpcraft.minecraft.TPCraftIDACAuth;
import net.tpcraft.minecraft.util.HTTPRequest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class HTTPServer {
    public static Boolean start() {
        Spark.port(TPCraftIDACAuth.config.getWebPort());

        Spark.get("/oauth2/callback", (req, res) -> {
            String error = req.queryParams("error");
            String code = req.queryParams("code");
            String state = req.queryParams("state");

            if (error != null) {
                authorizationFailed(state);
                return TPCraftIDACAuth.template
                        .replace("{{status}}", "error")
                        .replace("{{title}}", "授权失败");
            }

            if (TPCraftIDACAuth.notLoginPlayers.get(state) == null) {
                authorizationFailed(state);
                return TPCraftIDACAuth.template
                        .replace("{{status}}", "error")
                        .replace("{{title}}", "授权失败");
            }

            String responseAccessToken = HTTPRequest.sendPostRequest(
                    "https://api.tpcraft.net/oauth2/accessToken",
                    new HashMap<>(),
                    "client_id=" + TPCraftIDACAuth.config.getClientId() +
                            "&client_secret=" + TPCraftIDACAuth.config.getClientSecret() +
                            "&redirect_uri=" + TPCraftIDACAuth.config.getRedirectUri() +
                            "&grant_type=authorization_code" +
                            "&code=" + code,
                    new HashMap<>(),
                    false
            );

            String accessToken;

            if (responseAccessToken.equals("ERROR")) {
                authorizationFailed(state);
                return TPCraftIDACAuth.template
                        .replace("{{status}}", "error")
                        .replace("{{title}}", "插件内部错误，请联系管理员");
            } else {
                Map<String, Object> response = new Gson().fromJson(responseAccessToken, Map.class);
                if (response.get("access_token") != null) {
                    accessToken = response.get("access_token").toString();
                } else {
                    authorizationFailed(state);
                    return TPCraftIDACAuth.template
                            .replace("{{status}}", "error")
                            .replace("{{title}}", "授权失败");
                }
            }

            String finalAccessToken = accessToken;
            String responseUser = HTTPRequest.sendGetRequest(
                    "https://api.tpcraft.net/oauth2/user",
                    new HashMap<String, String>() {
                        {
                            put("Authorization", "Bearer " + finalAccessToken);
                        }
                    },
                    new HashMap<>()
            );

            Map<String, Object> user;

            if (responseUser.equals("ERROR")) {
                authorizationFailed(state);
                return TPCraftIDACAuth.template
                        .replace("{{status}}", "error")
                        .replace("{{title}}", "插件内部错误，请联系管理员");
            } else {
                Map<String, Object> response = new Gson().fromJson(responseUser, Map.class);
                if (Boolean.parseBoolean(response.get("status").toString())) {
                    user = (Map<String, Object>) response.get("data");
                } else {
                    authorizationFailed(state);
                    return TPCraftIDACAuth.template
                            .replace("{{status}}", "error")
                            .replace("{{title}}", "授权失败");
                }
            }

            Player player = TPCraftIDACAuth.notLoginPlayers.get(state);

            if (!player.getDisplayName().equals(user.get("username").toString())) {
                authorizationFailed(state);
                return TPCraftIDACAuth.template
                        .replace("{{status}}", "error")
                        .replace("{{title}}", "授权失败");
            }

            TPCraftIDACAuth.notLoginPlayers.remove(state);
            TPCraftIDACAuth.isLoginPlayers.put(player, state);

            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "授权成功"
            ));
            player.sendMessage("");

            return TPCraftIDACAuth.template
                    .replace("{{status}}", "success")
                    .replace("{{title}}", "授权成功");
        });

        return true;
    }

    public static Boolean stop() {
        Spark.stop();
        return true;
    }

    private static void authorizationFailed(String state) {
        if (TPCraftIDACAuth.notLoginPlayers.get(state) != null) {
            Player player = TPCraftIDACAuth.notLoginPlayers.get(state);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                            TPCraftIDACAuth.prefix + "授权失败"
                    ));
                }
            }.runTask(TPCraftIDACAuth.plugin);
        }

        TPCraftIDACAuth.notLoginPlayers.remove(state);
    }
}

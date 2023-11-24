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

import static org.bukkit.Bukkit.getLogger;

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
                return TPCraftIDACAuth.template
                        .replace("{{status}}", "error")
                        .replace("{{title}}", "授权失败");
            }

            Map<String, String> requestAccessTokenHeaders = new HashMap<>();
            requestAccessTokenHeaders.put("Content-Type", "application/x-www-form-urlencoded");

            Map<String, String> requestAccessTokenData = new HashMap<>();
            requestAccessTokenData.put("client_id", TPCraftIDACAuth.config.getClientId());
            requestAccessTokenData.put("client_secret", TPCraftIDACAuth.config.getClientSecret());
            requestAccessTokenData.put("redirect_uri", TPCraftIDACAuth.config.getRedirectUri());
            requestAccessTokenData.put("grant_type", "authorization_code");
            requestAccessTokenData.put("code", code);

            String responseAccessToken = HTTPRequest.sendPOST(
                    "https://api.tpcraft.net/oauth2/accessToken",
                    requestAccessTokenHeaders,
                    requestAccessTokenData
            );

            String accessToken = "";

            if (responseAccessToken.equals("ERROR")) {
                authorizationFailed(state);
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

            Map<String, String> requestGeUserHeaders = new HashMap<>();
            requestGeUserHeaders.put("Authorization", "Bearer " + accessToken);

            String responseGetUser = HTTPRequest.sendGET(
                    "https://api.tpcraft.net/oauth2/user",
                    requestGeUserHeaders
            );

            Map<String, Object> user;

            if (responseGetUser.equals("ERROR")) {
                authorizationFailed(state);
                return TPCraftIDACAuth.template;
            } else {
                Gson gson = new Gson();
                Map<String, Object> response = gson.fromJson(responseGetUser, Map.class);
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
                    " " + TPCraftIDACAuth.prefix + "授权"
            ));
            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    " &2授权成功"
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
                            TPCraftIDACAuth.prefix + "&4授权失败"
                    ));
                }
            }.runTask(TPCraftIDACAuth.plugin);
        }
        TPCraftIDACAuth.notLoginPlayers.remove(state);
    }
}

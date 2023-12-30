package net.tpcraft.minecraft.server.http.controller;

import com.google.gson.Gson;
import net.tpcraft.minecraft.Config;
import net.tpcraft.minecraft.TPCraftIDACAuth;
import net.tpcraft.minecraft.util.HTTPRequest;
import net.tpcraft.minecraft.util.MapList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import spark.Spark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Oauth2Controller {
    private String authorizationResults(String state, Boolean status, String title, Boolean errorDisplay, String error, String errorDescription) {
        Config config = TPCraftIDACAuth.config;
        String templateContent = TPCraftIDACAuth.templateContent;

        Player player = TPCraftIDACAuth.notLoginPlayers.get(state);

        templateContent = templateContent
                .replace("{{status}}", status ? "success" : "fail")
                .replace("{{title}}", title)
                .replace("{{errorDisplay}}", errorDisplay ? "block" : "none")
                .replace("{{error}}", error)
                .replace("{{errorDescription}}", errorDescription);

        if (status) {
            if (config.getAutoLoginEnable()) {
                TPCraftIDACAuth.autoLoginData = MapList.save(
                        TPCraftIDACAuth.autoLoginData,
                        new HashMap<String, Object>() {
                            {
                                put("name", player.getDisplayName());
                                put("ip", player.getAddress().getAddress().getHostAddress());
                                put("lastLoginAt", System.currentTimeMillis());
                            }
                        },
                        "name",
                        player.getDisplayName()
                );
                TPCraftIDACAuth.saveData();
            }

            TPCraftIDACAuth.notLoginPlayers.remove(state);
            TPCraftIDACAuth.isLoginPlayers.put(player, state);

            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "授权成功"
            ));
            player.sendMessage("");
        } else {
            if (player != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "授权失败"
                        ));
                    }
                }.runTask(TPCraftIDACAuth.plugin);

                TPCraftIDACAuth.notLoginPlayers.remove(state);
            }
        }

        return templateContent;
    }

    public Oauth2Controller() {
        Spark.get("/oauth2/callback", (req, res) -> {
            Config config = TPCraftIDACAuth.config;

            String reqDataError = req.queryParams("error");
            String reqDataErrorDescription = req.queryParams("error_description");
            String reqDataCode = req.queryParams("code");
            String reqDataState = req.queryParams("state");

            String accessToken;
            Map<String, Object> user;
            Player player = TPCraftIDACAuth.notLoginPlayers.get(reqDataState);

            if (player == null) {
                return authorizationResults(
                        reqDataState, false, "授权失败", false, "", ""
                );
            }

            if (reqDataError != null) {
                return authorizationResults(
                        reqDataState, false, "授权错误", true, reqDataError, reqDataErrorDescription
                );
            }

            String responseAccessToken = HTTPRequest.sendPostRequest(
                    "https://api.tpcraft.net/oauth2/accessToken",
                    new HashMap<>(),
                    "client_id=" + config.getOauth2ClientId() +
                            "&client_secret=" + config.getOauth2ClientSecret() +
                            "&redirect_uri=" + config.getOauth2RedirectUri() +
                            "&grant_type=authorization_code" +
                            "&code=" + reqDataCode,
                    new HashMap<>(),
                    false
            );

            if (responseAccessToken.equals("ERROR")) {
                return authorizationResults(
                        reqDataState, false, "授权错误", true, "内部错误", "获取令牌时请求错误"
                );
            } else {
                Map<String, Object> response = new Gson().fromJson(responseAccessToken, Map.class);
                if (response.get("access_token") != null) {
                    accessToken = response.get("access_token").toString();
                } else {
                    return authorizationResults(
                            reqDataState, false, "授权失败", false, "", ""
                    );
                }
            }

            String responseUser = HTTPRequest.sendGetRequest(
                    "https://api.tpcraft.net/oauth2/user",
                    new HashMap<String, String>() {
                        {
                            put("Authorization", "Bearer " + accessToken);
                        }
                    },
                    new HashMap<>()
            );

            if (responseUser.equals("ERROR")) {
                return authorizationResults(
                        reqDataState, false, "授权错误", true, "内部错误", "获取用户信息时请求错误"
                );
            } else {
                Map<String, Object> response = new Gson().fromJson(responseUser, Map.class);
                if (Boolean.parseBoolean(response.get("status").toString())) {
                    user = (Map<String, Object>) response.get("data");
                } else {
                    return authorizationResults(
                            reqDataState, false, "授权失败", false, "", ""
                    );
                }
            }

            if (!player.getDisplayName().equals(user.get("username").toString())) {
                return authorizationResults(
                        reqDataState, false, "授权失败", false, "", ""
                );
            }

            return authorizationResults(reqDataState, true, "授权成功", false, "", "");
        });
    }
}

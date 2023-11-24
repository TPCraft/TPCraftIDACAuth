package net.tpcraft.minecraft;

import net.tpcraft.minecraft.command.MainCommand;
import net.tpcraft.minecraft.event.MainEvent;
import net.tpcraft.minecraft.http.HTTPServer;
import net.tpcraft.minecraft.util.Checker;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TPCraftIDACAuth extends JavaPlugin {

    public static Plugin plugin;
    public static Config config = new Config();
    public static String template;
    public static String prefix = "&9&l[TPCraftIDACAuth] &r&8>>&r ";

    public static Map<String, Player> notLoginPlayers = new HashMap<>();
    public static Map<Player, String> isLoginPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("template/index.html", false);

        plugin = getPlugin(getClass());

        if (loadConfig() && loadTemplate()) {
            getServer().getPluginManager().registerEvents(new MainEvent(), this);
            getCommand("auth").setExecutor(new MainCommand());
            HTTPServer.start();
            getLogger().info("TPCraftIDACAuth 身份认证中心授权 插件已启用。");
        } else {
            getLogger().info("配置文件未填写。");
            getServer().shutdown();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("TPCraftIDACAuth 身份认证中心授权 插件已卸载。");
    }

    public static Boolean loadConfig() {
        config.setWebPort(plugin.getConfig().getInt("webPort", 5900));
        config.setClientId(plugin.getConfig().getString("clientId", ""));
        config.setClientSecret(plugin.getConfig().getString("clientSecret", ""));
        config.setRedirectUri(plugin.getConfig().getString("redirectUri", ""));

        return !Checker.stringIsNullOrEmpty(config.getClientId(), config.getClientSecret(), config.getRedirectUri());
    }

    public static Boolean loadTemplate() {
        template = "";

        String templatePath = plugin.getDataFolder() + "/template/index.html";
        File emplateFile = new File(templatePath);

        if (emplateFile.exists()) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(templatePath), StandardCharsets.UTF_8);
                for (String line : lines) {
                    template += line;
                }
            } catch (IOException e) {
                throw new Error(e);
            }
        }

        return true;
    }
}

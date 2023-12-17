package net.tpcraft.minecraft;

import net.tpcraft.minecraft.command.MainCommand;
import net.tpcraft.minecraft.event.JoinEvent;
import net.tpcraft.minecraft.event.LeaveEvent;
import net.tpcraft.minecraft.event.MainEvent;
import net.tpcraft.minecraft.http.HTTPServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TPCraftIDACAuth extends JavaPlugin {
    public static Boolean loaded = false;
    public static Plugin plugin;
    public static Config config = new Config();
    public static String template;
    public static String prefix = "&c&l[TPCraftIDACAuth] &r&8>>&r ";

    public static Map<String, Player> notLoginPlayers = new HashMap<>();
    public static Map<Player, String> isLoginPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("template/index.html", false);

        plugin = getPlugin(getClass());

        if (loadConfig() && loadTemplate()) {
            getCommand("auth").setExecutor(new MainCommand());
            getServer().getPluginManager().registerEvents(new JoinEvent(), this);
            getServer().getPluginManager().registerEvents(new LeaveEvent(), this);
            getServer().getPluginManager().registerEvents(new MainEvent(), this);
            HTTPServer.start();
            loaded = true;
            getLogger().info("插件已启用");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("插件已卸载");
    }

    public static Boolean loadConfig() {
        config.setWebPort(plugin.getConfig().getInt("webPort", 5900));
        config.setClientId(plugin.getConfig().getString("clientId", ""));
        config.setClientSecret(plugin.getConfig().getString("clientSecret", ""));
        config.setRedirectUri(plugin.getConfig().getString("redirectUri", ""));
        config.setCoverInfo(plugin.getConfig().getBoolean("coverInfo", true));
        config.setLimitMode(plugin.getConfig().getBoolean("limitMode", false));
        config.setAllowPlayers(plugin.getConfig().getStringList("allowPlayers"));

        return true;
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

package net.tpcraft.minecraft;

import net.tpcraft.minecraft.command.MainCommand;
import net.tpcraft.minecraft.command.MainTabCompleter;
import net.tpcraft.minecraft.event.JoinEvent;
import net.tpcraft.minecraft.event.LeaveEvent;
import net.tpcraft.minecraft.event.MainEvent;
import net.tpcraft.minecraft.server.http.WebServer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    public static final String prefix = "&c&l[TPCraftIDAC Auth] &r&8>>&r ";

    public static Boolean loaded = false;

    public static Plugin plugin;

    public static Config config = new Config();

    public static String templateFilePath = "/template/index.html";
    public static String templateContent = "";

    public static String autoLoginDataFilePath = "/data/autoLoginData.yml";
    public static List<Map<?, ?>> autoLoginData = new ArrayList<>();

    public static Map<String, Player> notLoginPlayers = new HashMap<>();
    public static Map<Player, String> isLoginPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("  __________  ______           ______  ________  ___   ______   ___         __  __  ");
        getLogger().info(" /_  __/ __ \\/ ____/________ _/ __/ /_/  _/ __ \\/   | / ____/  /   | __  __/ /_/ /_ ");
        getLogger().info("  / / / /_/ / /   / ___/ __ `/ /_/ __// // / / / /| |/ /      / /| |/ / / / __/ __ \\");
        getLogger().info(" / / / ____/ /___/ /  / /_/ / __/ /__/ // /_/ / ___ / /___   / ___ / /_/ / /_/ / / /");
        getLogger().info("/_/ /_/    \\____/_/   \\__,_/_/  \\__/___/_____/_/  |_\\____/  /_/  |_\\__,_/\\__/_/ /_/ ");

        plugin = getPlugin(getClass());

        saveDefaultConfig();
        saveResource("data/autoLoginData.yml", false);
        saveResource("template/index.html", false);

        getLogger().info("加载配置文件、数据与模板...");
        loadConfig();
        loadData();
        loadTemplate();

        getLogger().info("注册命令...");
        getCommand("auth").setExecutor(new MainCommand());
        getCommand("auth").setTabCompleter(new MainTabCompleter());

        getLogger().info("监听事件...");
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new LeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new MainEvent(), this);

        getLogger().info("在 " + config.getWebPort() + " 端口启动Web服务器...");
        WebServer.start();

        getLogger().info("插件已加载！");
        loaded = true;
    }

    @Override
    public void onDisable() {
        getLogger().info("插件已卸载！");
    }

    public static void loadConfig() {
        Configuration configuration = plugin.getConfig();

        config.setWebPort(configuration.getInt("webPort", 5900));

        config.setOauth2ClientId(configuration.getString("oauth2.clientId", ""));
        config.setOauth2ClientSecret(configuration.getString("oauth2.clientSecret", ""));
        config.setOauth2RedirectUri(configuration.getString("oauth2.redirectUri", ""));

        config.setLoginMessageEnable(configuration.getBoolean("loginMessage.enable", true));
        config.setLoginMessageJoin(configuration.getString("loginMessage.join", "[&2+&r] &e%player%"));
        config.setLoginMessageLeave(configuration.getString("loginMessage.leave", "[&4-&r] &e%player%"));

        config.setAutoLoginEnable(configuration.getBoolean("autoLogin.enable", true));
        config.setAutoLoginExpires(configuration.getInt("autoLogin.expires", 3600));

        config.setLoginPositionEnable(configuration.getBoolean("loginPosition.enable", false));
        config.setLoginPositionX(configuration.getDouble("loginPosition.x", 0.0));
        config.setLoginPositionY(configuration.getDouble("loginPosition.y", 0.0));
        config.setLoginPositionZ(configuration.getDouble("loginPosition.z", 0.0));

        config.setWhiteListEnable(configuration.getBoolean("whiteList.enable", false));
        config.setWhiteListAllowPlayers(configuration.getStringList("whiteList.allowPlayers"));
    }

    public static void loadData() {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + autoLoginDataFilePath));

        autoLoginData = configuration.getMapList("players");
    }

    public static void saveData() {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + autoLoginDataFilePath));

        configuration.set("players", autoLoginData);

        try {
            configuration.save(new File(plugin.getDataFolder() + autoLoginDataFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTemplate() {
        templateContent = "";

        String templatePath = plugin.getDataFolder() + templateFilePath;

        if (new File(templatePath).exists()) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(templatePath), StandardCharsets.UTF_8);
                for (String line : lines) {
                    templateContent += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

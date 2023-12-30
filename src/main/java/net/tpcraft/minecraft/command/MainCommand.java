package net.tpcraft.minecraft.command;

import net.tpcraft.minecraft.Config;
import net.tpcraft.minecraft.TPCraftIDACAuth;
import net.tpcraft.minecraft.server.http.WebServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainCommand implements CommandExecutor {
    private Boolean checkArgs(CommandSender sender, String[] args, Integer argsLength) {
        if (args.length < argsLength || args[argsLength - 1].isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "参数错误"
            ));
            return false;
        }
        return true;
    }

    private Boolean checkSenderIsConsole(CommandSender sender) {
        return !(sender instanceof Player);
    }

    private Boolean checkSenderIsPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    private void sendHelp(CommandSender sender) {
        Config config = TPCraftIDACAuth.config;

        sender.sendMessage("");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                TPCraftIDACAuth.prefix + "帮助"
        ));
        sender.sendMessage("");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "登入消息：" + (config.getLoginMessageEnable() ? "&a已启用" : "&c已禁用")
        ));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "自动登入：" + (config.getAutoLoginEnable() ? "&a已启用" : "&c已禁用")
        ));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "固定登入坐标：" + (config.getLoginPositionEnable() ? "&a已启用" : "&c已禁用")
        ));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "白名单：" + (config.getWhiteListEnable() ? "&a已启用" : "&c已禁用")
        ));
        sender.sendMessage("");
        sender.sendMessage("/auth - 主命令");
        sender.sendMessage("* /auth loginMessage on - 启用登入消息");
        sender.sendMessage("* /auth loginMessage off - 禁用登入消息");
        sender.sendMessage("* /auth autoLogin on - 启用自动登入");
        sender.sendMessage("* /auth autoLogin off - 禁用自动登入");
        sender.sendMessage("* /auth autoLogin set <超时时间(秒)> - 设置自动登入超时时间");
        sender.sendMessage("* /auth loginPosition on - 启用固定登入坐标");
        sender.sendMessage("* /auth loginPosition off - 禁用固定登入坐标");
        sender.sendMessage("* /auth loginPosition set - 设置固定登入坐标");
        sender.sendMessage("* /auth whiteList on - 启用白名单");
        sender.sendMessage("* /auth whiteList off - 禁用白名单");
        sender.sendMessage("* /auth whiteList add <玩家名称> - 添加白名单");
        sender.sendMessage("* /auth whiteList remove <玩家名称> - 移除白名单");
        sender.sendMessage("* /auth whiteList list - 白名单列表");
        sender.sendMessage("* /auth reload - 重载插件");
        sender.sendMessage("");
        sender.sendMessage("注意：带 * 字符需要管理员权限");
        sender.sendMessage("");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return false;
        } else {
            if (sender instanceof Player && !sender.hasPermission("auth.admin")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        TPCraftIDACAuth.prefix + "你没有执行此命令的权限"
                ));
                return false;
            }
        }

        Plugin plugin = TPCraftIDACAuth.plugin;
        Config config = TPCraftIDACAuth.config;

        switch (args[0]) {
            case "loginMessage": {
                if (!checkArgs(sender, args, 2)) {
                    return false;
                }

                switch (args[1]) {
                    case "on": {
                        config.setLoginMessageEnable(true);
                        plugin.getConfig().set("loginMessage.enable", true);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "登入消息：&2已启用"
                        ));
                        return false;
                    }
                    case "off": {
                        config.setLoginMessageEnable(false);
                        plugin.getConfig().set("loginMessage.enable", false);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "登入消息：&4已禁用"
                        ));
                        return false;
                    }
                }
            }
            case "autoLogin": {
                if (!checkArgs(sender, args, 2)) {
                    return false;
                }

                switch (args[1]) {
                    case "on": {
                        config.setAutoLoginEnable(true);
                        plugin.getConfig().set("autoLogin.enable", true);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "自动登入：&2已启用"
                        ));
                        return false;
                    }
                    case "off": {
                        config.setAutoLoginEnable(false);
                        plugin.getConfig().set("autoLogin.enable", false);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "自动登入：&4已禁用"
                        ));
                        return false;
                    }
                    case "set": {
                        if (!checkArgs(sender, args, 3)) {
                            return false;
                        }

                        if (!Pattern.matches("^[0-9]+$", args[2])) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    TPCraftIDACAuth.prefix + "参数错误"
                            ));
                            return false;
                        }

                        config.setAutoLoginExpires(Integer.parseInt(args[2]));
                        plugin.getConfig().set("autoLogin.expires", Integer.parseInt(args[2]));
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "自动登入：超时时间设置为 " + args[2] + " 秒"
                        ));
                        return false;
                    }
                }
            }
            case "loginPosition": {
                if (!checkArgs(sender, args, 2)) {
                    return false;
                }

                switch (args[1]) {
                    case "on": {
                        config.setLoginPositionEnable(true);
                        plugin.getConfig().set("loginPosition.enable", true);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "固定登入坐标：&2已启用"
                        ));
                        return false;
                    }
                    case "off": {
                        config.setLoginPositionEnable(false);
                        plugin.getConfig().set("loginPosition.enable", false);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "固定登入坐标：&4已禁用"
                        ));
                        return false;
                    }
                    case "set": {
                        if (!checkSenderIsPlayer(sender)) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    TPCraftIDACAuth.prefix + "此命令控制台无法使用"
                            ));
                            return false;
                        }

                        Player player = (Player) sender;

                        double x = player.getLocation().getX();
                        double y = player.getLocation().getY();
                        double z = player.getLocation().getZ();

                        x = Math.round(x * 10.0) / 10.0;
                        y = Math.round(y * 10.0) / 10.0;
                        z = Math.round(z * 10.0) / 10.0;

                        config.setLoginPositionX(x);
                        config.setLoginPositionY(y);
                        config.setLoginPositionZ(z);
                        plugin.getConfig().set("loginPosition.x", x);
                        plugin.getConfig().set("loginPosition.y", y);
                        plugin.getConfig().set("loginPosition.z", z);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "固定登入坐标：已设置为 X:" + x + " Y:" + y + " Z:" + z
                        ));
                        return false;
                    }
                }
            }
            case "whiteList": {
                if (!checkArgs(sender, args, 2)) {
                    return false;
                }

                switch (args[1]) {
                    case "on": {
                        config.setWhiteListEnable(true);
                        plugin.getConfig().set("whiteList.enable", true);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "白名单：&2已启用"
                        ));
                        return false;
                    }
                    case "off": {
                        config.setWhiteListEnable(false);
                        plugin.getConfig().set("whiteList.enable", false);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "白名单：&4已禁用"
                        ));
                        return false;
                    }
                    case "add": {
                        if (!checkArgs(sender, args, 3)) {
                            return false;
                        }
                        config.getWhiteListAllowPlayers().add(args[2]);
                        plugin.getConfig().set("whiteList.allowPlayers", config.getWhiteListAllowPlayers());
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "白名单：已添加 &e" + args[2]
                        ));
                        return false;
                    }
                    case "remove": {
                        if (!checkArgs(sender, args, 3)) {
                            return false;
                        }
                        config.getWhiteListAllowPlayers().removeIf(player -> player.equals(args[2]));
                        plugin.getConfig().set("whiteList.allowPlayers", config.getWhiteListAllowPlayers());
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "白名单：已移除 &e" + args[2]
                        ));
                        return false;
                    }
                    case "list": {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "白名单列表：&e" + String.join(" ", config.getWhiteListAllowPlayers())
                        ));
                        return false;
                    }
                }
            }
            case "reload": {
                for (Map.Entry entry : TPCraftIDACAuth.notLoginPlayers.entrySet()) {
                    Player player = (Player) entry.getValue();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                                    TPCraftIDACAuth.prefix + "插件重载，请稍后再重试"
                            ));
                        }
                    }.runTask(plugin);
                }
                TPCraftIDACAuth.notLoginPlayers = new HashMap<>();

                TPCraftIDACAuth.loaded = false;

                plugin.reloadConfig();

                TPCraftIDACAuth.loadConfig();
                TPCraftIDACAuth.loadData();
                TPCraftIDACAuth.loadTemplate();

                WebServer.stop();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        WebServer.start();

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "插件重载完成"
                        ));

                        TPCraftIDACAuth.loaded = true;
                    }
                }.runTaskLater(plugin, 60);
                return false;
            }
            default: {
                sendHelp(sender);
                return false;
            }
        }
    }
}

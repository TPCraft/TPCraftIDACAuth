package net.tpcraft.minecraft.command;

import net.tpcraft.minecraft.TPCraftIDACAuth;
import net.tpcraft.minecraft.http.HTTPServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return false;
        }

        switch (args[0]) {
            case "coverInfo": {
                if (sender instanceof Player && !sender.hasPermission("auth.admin")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            TPCraftIDACAuth.prefix + "你没有执行此命令的权限"
                    ));
                    return false;
                }

                if (!checkArgs(sender, args, 2)) {
                    return false;
                }

                switch (args[1]) {
                    case "on": {
                        TPCraftIDACAuth.config.setCoverInfo(true);
                        TPCraftIDACAuth.plugin.getConfig().set("coverInfo", true);
                        TPCraftIDACAuth.plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "进入/离开服务器消息：&2已启用"
                        ));
                        return false;
                    }
                    case "off": {
                        TPCraftIDACAuth.config.setCoverInfo(false);
                        TPCraftIDACAuth.plugin.getConfig().set("coverInfo", false);
                        TPCraftIDACAuth.plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "进入/离开服务器消息：&4已禁用"
                        ));
                        return false;
                    }
                    default: {
                        sendHelp(sender);
                        return false;
                    }
                }
            }
            case "limit": {
                if (sender instanceof Player && !sender.hasPermission("auth.admin")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            TPCraftIDACAuth.prefix + "你没有执行此命令的权限"
                    ));
                    return false;
                }

                if (!checkArgs(sender, args, 2)) {
                    return false;
                }

                switch (args[1]) {
                    case "on": {
                        TPCraftIDACAuth.config.setLimitMode(true);
                        TPCraftIDACAuth.plugin.getConfig().set("limitMode", true);
                        TPCraftIDACAuth.plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "限制模式：&2已启用"
                        ));
                        return false;
                    }
                    case "off": {
                        TPCraftIDACAuth.config.setLimitMode(false);
                        TPCraftIDACAuth.plugin.getConfig().set("limitMode", false);
                        TPCraftIDACAuth.plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "限制模式：&4已禁用"
                        ));
                        return false;
                    }
                    case "add": {
                        if (!checkArgs(sender, args, 3)) {
                            return false;
                        }
                        TPCraftIDACAuth.config.getAllowPlayers().add(args[2]);
                        TPCraftIDACAuth.plugin.getConfig().set("allowPlayers", TPCraftIDACAuth.config.getAllowPlayers());
                        TPCraftIDACAuth.plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "限制模式白名单：已添加 &e" + args[2]
                        ));
                        return false;
                    }
                    case "remove": {
                        if (!checkArgs(sender, args, 3)) {
                            return false;
                        }
                        TPCraftIDACAuth.config.getAllowPlayers().removeIf(player -> player.equals(args[2]));
                        TPCraftIDACAuth.plugin.getConfig().set("allowPlayers", TPCraftIDACAuth.config.getAllowPlayers());
                        TPCraftIDACAuth.plugin.saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "限制模式白名单：已移除 &e" + args[2]
                        ));
                        return false;
                    }
                    case "list": {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "限制模式白名单：&e" + String.join(" ", TPCraftIDACAuth.config.getAllowPlayers())
                        ));
                        return false;
                    }
                    default: {
                        sendHelp(sender);
                        return false;
                    }
                }
            }
            case "reload": {
                if (sender instanceof Player && !sender.hasPermission("auth.admin")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            TPCraftIDACAuth.prefix + "你没有执行此命令的权限"
                    ));
                    return false;
                }

                for (Map.Entry entry : TPCraftIDACAuth.notLoginPlayers.entrySet()) {
                    Player player = (Player) entry.getValue();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                                    TPCraftIDACAuth.prefix + "插件重载，请稍后重试"
                            ));
                        }
                    }.runTask(TPCraftIDACAuth.plugin);
                }
                TPCraftIDACAuth.notLoginPlayers = new HashMap<>();

                TPCraftIDACAuth.loaded = false;
                TPCraftIDACAuth.plugin.reloadConfig();
                if (TPCraftIDACAuth.loadConfig() && TPCraftIDACAuth.loadTemplate()) {
                    if (HTTPServer.stop()) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (HTTPServer.start()) {
                                    TPCraftIDACAuth.loaded = true;
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                            TPCraftIDACAuth.prefix + "插件重载完成"
                                    ));
                                }
                            }
                        }.runTaskLater(TPCraftIDACAuth.plugin, 60);
                    }
                }

                return false;
            }
            default: {
                sendHelp(sender);
                return false;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("coverInfo");
            completions.add("limit");
            completions.add("reload");
        }

        switch (args[0]) {
            case "coverInfo": {
                completions.add("on");
                completions.add("off");
                break;
            }
            case "limit": {
                completions.add("on");
                completions.add("off");
                completions.add("add");
                completions.add("remove");
                completions.add("list");
                break;
            }
        }

        return completions;
    }

    private Boolean checkArgs(CommandSender sender, String[] args, Integer argsLength) {
        if (args.length < argsLength || args[argsLength - 1].isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    TPCraftIDACAuth.prefix + "参数错误"
            ));
            return false;
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                TPCraftIDACAuth.prefix
        ));
        sender.sendMessage("");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "进入/离开服务器消息：" + (TPCraftIDACAuth.config.getCoverInfo() ? "&a已启用" : "&c已禁用")
        ));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "限制模式：" + (TPCraftIDACAuth.config.getLimitMode() ? "&a已启用" : "&c已禁用")
        ));
        sender.sendMessage("");
        sender.sendMessage("/auth - 主命令");
        sender.sendMessage("* /auth coverInfo on - 启用进入/离开服务器消息");
        sender.sendMessage("* /auth coverInfo off - 禁用进入/离开服务器消息");
        sender.sendMessage("* /auth limit on - 启用限制模式");
        sender.sendMessage("* /auth limit off - 禁用限制模式");
        sender.sendMessage("* /auth limit add [名称] - 添加限制模式白名单");
        sender.sendMessage("* /auth limit remove [名称] - 移除限制模式白名单");
        sender.sendMessage("* /auth limit list - 限制模式白名单列表");
        sender.sendMessage("* /auth reload - 重载插件");
        sender.sendMessage("");
        sender.sendMessage("注意：带 * 字符需要管理员权限");
        sender.sendMessage("");
    }
}

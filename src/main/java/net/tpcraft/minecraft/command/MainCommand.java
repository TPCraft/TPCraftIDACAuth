package net.tpcraft.minecraft.command;

import net.tpcraft.minecraft.TPCraftIDACAuth;
import net.tpcraft.minecraft.http.HTTPServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
        } else if (args.length == 1) {
            switch (args[0]) {
                case "reload": {
                    if (sender instanceof Player && !sender.hasPermission("auth.admin")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                TPCraftIDACAuth.prefix + "你没有执行此命令的权限"
                        ));
                        return false;
                    }

                    TPCraftIDACAuth.plugin.reloadConfig();

                    if (TPCraftIDACAuth.loadConfig() && TPCraftIDACAuth.loadTemplate()) {
                        if (HTTPServer.stop()) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (HTTPServer.start()) {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                TPCraftIDACAuth.prefix + "插件重载完成"
                                        ));
                                    }
                                }
                            }.runTaskLater(TPCraftIDACAuth.plugin, 60);
                        }
                    }
                    break;
                }
                default: {
                    sendHelp(sender);
                    break;
                }
            }
        } else {
            sendHelp(sender);
        }
        return false;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                " " + TPCraftIDACAuth.prefix + "帮助"
        ));
        sender.sendMessage("");
        sender.sendMessage(" /ta - 主命令");
        sender.sendMessage(" * /ta reload - 重载插件");
        sender.sendMessage("");
        sender.sendMessage(" 注意：带 * 字符需要管理员权限");
        sender.sendMessage("");
    }
}

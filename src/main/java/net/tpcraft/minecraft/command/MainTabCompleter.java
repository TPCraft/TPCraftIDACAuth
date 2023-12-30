package net.tpcraft.minecraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MainTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("loginMessage");
            completions.add("autoLogin");
            completions.add("loginPosition");
            completions.add("whiteList");
            completions.add("reload");
        }

        switch (args[0]) {
            case "loginMessage": {
                completions.add("on");
                completions.add("off");
                break;
            }
            case "autoLogin":
            case "loginPosition": {
                completions.add("on");
                completions.add("off");
                completions.add("set");
                break;
            }
            case "whiteList": {
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
}

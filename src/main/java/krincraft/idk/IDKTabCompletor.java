package krincraft.idk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IDKTabCompletor implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1) {
            List<String> sub_commands = new ArrayList<>();
            sub_commands.add("gm");
            sub_commands.add("open");
            sub_commands.add("help");
            sub_commands.add("menu");
            sub_commands.add("ping");
            sub_commands.add("reload");
            return sub_commands;
        }
        if(strings.length == 2 && strings[0].equals("gm")) {
            List<String> sub_commands = new ArrayList<>();
            sub_commands.add("0");
            sub_commands.add("1");
            sub_commands.add("2");
            sub_commands.add("3");
            return sub_commands;
        }
        if(strings.length == 2 && strings[0].equals("open")) {
            List<String> sub_commands = new ArrayList<>();
            sub_commands.add("workbench");
            return sub_commands;
        }
        return null;
    }
}

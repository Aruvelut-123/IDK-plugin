package idk.team;

import idk.team.network.IDKnetHandler;
import idk.team.plugin.IDKPluginManagement;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IDKCommand implements CommandExecutor {
    String filename = "messages.yml";
    boolean checking = false;
    void check() {
        Configuration config = IDK.idk.getConfig();
        while(checking) {
            int read_config_ver = config.getInt("config-version");
            if (read_config_ver == IDK.idk.config_ver) {
                checking = false;
            } else {
                config.set("config-version", 2);
                config.set("plugin-management", true);
                config.set("debug", false);
                String path = Bukkit.getPluginsFolder().getAbsolutePath();
                String config_path = path + "\\IDK\\config.yml";
                File config_file = new File(config_path);
                try {
                    FileWriter fw = new FileWriter(config_file);
                    fw.write("#it can be type with like \"papermc\" or \"modrinth\" or \"both\"");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                config.set("download-source", "papermc");
                IDK.idk.saveConfig();
                checking = false;
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) { //检测指令执行
        Configuration config = IDK.idk.getConfig();
        IDKMessageConfig messages = new IDKMessageConfig(IDK.idk.data_folder, config.getString("lang")) {
            protected void finalize() throws Throwable {
                super.finalize();
            }
        };
        Boolean plugin_manage = config.getBoolean("plugin-management");
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender; //获取执行玩家
            if(strings.length == 0) {
                List<String> main_msg = messages.getStringList("main");
                Object[] main_msg_fix = main_msg.toArray();
                for(int i = 0; i < main_msg_fix.length; i++) {
                    player.sendMessage(main_msg_fix[i].toString());
                }
                return true;
            }
            if(strings.length >= 1 && strings[0].equals("plugin")) {
                if(plugin_manage) {
                    if(player.hasPermission("IDK.command.IDK.plugin")) {
                        if(strings.length == 2 && strings[1].equals("list")) {
                            IDK.idk.plugins = Arrays.toString(Bukkit.getPluginManager().getPlugins());
                            player.sendMessage(messages.getString("available-plugins") + IDK.idk.plugins);
                            return true;
                        }
                        if(strings.length == 3 && strings[1].equals("load")) {
                            String folder = Bukkit.getPluginsFolder().getAbsolutePath();
                            if(!strings[2].isEmpty()) {
                                String file_name = strings[2];
                                String file_path = folder + "\\" + file_name;
                                try {
                                    Plugin plugin = Bukkit.getPluginManager().loadPlugin(new File(file_path));
                                    if (plugin != null) {
                                        Bukkit.getPluginManager().enablePlugin(plugin);
                                        player.sendMessage("Plugin file " + file_name + " " + "plugin name " + plugin.getName() + " loaded!");
                                    }
                                    return true;
                                } catch (InvalidPluginException | InvalidDescriptionException e) {
                                    return true;
                                }
                            } else {
                                player.sendMessage("Error!");
                                return true;
                            }
                        }
                        if(strings.length == 3 && strings[1].equals("disable")) {
                            String plugin_name = strings[2];
                            if (!plugin_name.equals("idk") && !plugin_name.equals("IDK")) {
                                Plugin plugin = Bukkit.getPluginManager().getPlugin(plugin_name);
                                if (plugin != null) {
                                    try {
                                        Bukkit.getPluginManager().disablePlugin(plugin);
                                    } catch (Exception e) {
                                        player.sendMessage(messages.getString("plugin-disable-error"));
                                    }
                                    player.sendMessage(messages.getString("plugin-disabled").replace("[plugin_name]", plugin_name));
                                } else {
                                    player.sendMessage(messages.getString("plugin-not-found"));
                                }
                            } else {
                                player.sendMessage(messages.getString("plugin-disable-egg"));
                            }
                            return true;
                        }
                        if(strings.length == 3 && strings[1].equals("enable")) {
                            String plugin_name = strings[2];
                            if (!plugin_name.equals("idk") && !plugin_name.equals("IDK")) {
                                Plugin plugin = Bukkit.getPluginManager().getPlugin(plugin_name);
                                if (plugin != null) {
                                    try {
                                        Bukkit.getPluginManager().enablePlugin(plugin);
                                    } catch (Exception e) {
                                        player.sendMessage(messages.getString("plugin-enable-error"));
                                    }
                                    player.sendMessage(messages.getString("plugin-enabled").replace("[plugin_name]", plugin_name));
                                } else {
                                    player.sendMessage(messages.getString("plugin-not-found"));
                                }
                            } else {
                                player.sendMessage(messages.getString("plugin-enable-egg"));
                            }
                            return true;
                        }
                        if(strings.length == 2 && strings[1].equals("search")) {
                            try {
                                IDKnetHandler.get_10_top_projects_a(commandSender);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return true;
                        }
                        if(strings.length >= 3 && strings[1].equals("search")) {
                            try {
                                if(strings.length > 3) {
                                    String plugin_title = "";
                                    for(int i = 2; i < strings.length; i++) {
                                        plugin_title = plugin_title+strings[i]+" ";
                                    }
                                    String real_plugin_title = plugin_title.substring(0, plugin_title.length() - 1);
                                    IDKnetHandler.get_projects_by_title_a(commandSender, real_plugin_title);
                                } else {
                                    IDKnetHandler.get_projects_by_title_a(commandSender, strings[2]);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return true;
                        }
                        if(strings.length == 3 && strings[1].equals("install")) {
                            try {
                                IDKnetHandler.install_project(commandSender, strings[2], false);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return true;
                        }
                        if(strings.length == 3 && strings[1].equals("del")) {
                            IDKPluginManagement idkpm = new IDKPluginManagement();
                            idkpm.delete_plugin(strings[2], commandSender);
                            return true;
                        }
                        if(strings.length == 2 && strings[1].equals("update")) {
                            try {
                                IDKnetHandler.update_self(commandSender);
                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return true;
                        }
                        if(strings.length == 3 && strings[1].equals("update")) {
                            if (strings[2].equals("all")) {
                                try {
                                    IDKnetHandler.update_plugins(commandSender);
                                }
                                catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                return true;
                            }
                            else {
                                return false;
                            }
                        }
                    }
                    return false;
                }
                else {
                    player.sendMessage("Plugin management function is not enabled in config.");
                    return true;
                }
            }
            if(strings.length == 1 && strings[0].equals("reload")) {
                if(player.hasPermission("IDK.command.IDK.reload")) {
                    try{
                        messages.save();
                        this.checking = true;
                        this.check();
                        IDK.idk.reload();
                        config = IDK.idk.getConfig();
                        // 测试是否有空值
                        config.getBoolean("plugin-management");
                        config.getBoolean("debug");
                        config.getString("download-source");
                        messages.getString("IDK_Helper_name");
                        messages.getStringList("IDK_Helper_lore");
                        messages.getString("IDK_Workbench_name");
                        messages.getStringList("IDK_Workbench_lore");
                        messages.getString("menu_title");
                        messages.getString("workbench_title");
                        messages.getString("failed");
                        messages.getString("failed_p");
                        messages.getString("survival");
                        messages.getString("creative");
                        messages.getString("adventure");
                        messages.getString("spectator");
                        commandSender.sendMessage(messages.getString("reload"));
                    } catch (Exception e) {
                        IDK.idk.logger.warning(messages.getString("failed"));
                        player.sendMessage(messages.getString("failed_p"));
                        e.printStackTrace();
                    }
                }
                return true;
            }
            if(strings.length == 2 && strings[0].equals("gm")) { //检测参数长度是否为二并且第一个参数是否为gm
                if(player.hasPermission("IDK.command.IDK.gm")) {

                }
                switch (strings[1]) { //检测第二个参数
                    case "0": //如果是0
                        player.setGameMode(GameMode.SURVIVAL); //设置玩家游戏模式为生存
                        player.sendMessage(messages.getString("survival")); //对玩家发送消息
                        break; //跳出循环
                    case "1": //如果是1
                        player.setGameMode(GameMode.CREATIVE); //设置玩家游戏模式为创造
                        player.sendMessage(messages.getString("creative")); //对玩家发送消息
                        break; //跳出循环
                    case "2": //如果是2
                        player.setGameMode(GameMode.ADVENTURE); //设置玩家游戏模式为冒险
                        player.sendMessage(messages.getString("adventure")); //对玩家发送消息
                        break; //跳出循环
                    case "3": //如果是3
                        player.setGameMode(GameMode.SPECTATOR); //设置玩家游戏模式为旁观
                        player.sendMessage(messages.getString("spectator")); //对玩家发送消息
                        break; //跳出循环
                    default: //默认
                        break; //跳出循环
                }
                return true;
            }
            if(strings.length == 1 && strings[0].equals("menu")) {
                if(player.hasPermission("IDK.command.IDK.menu")) {
                    Inventory inv = Bukkit.createInventory(null, 9, messages.getString("menu_title")); //创建一个大小为9格(必须为9或9的倍数)的背包，标题设置为IDK Chest
                    int left = inv.getSize(); //获取背包大小并赋值到left上
                    int slot = 0; //设置slot的值为0
                    int i = 0;
                    List<String> slot_used = new ArrayList<>();

                    ItemStack idk_helper = new ItemStack(Material.DIAMOND, 1); //创建一个物品，材质为钻石，数量为1个
                    ItemMeta idk_helper_meta = idk_helper.getItemMeta(); //获取物品meta
                    idk_helper_meta.setDisplayName(messages.getString("IDK_Helper_name"));
                    idk_helper_meta.setLore(messages.getStringList("IDK_Helper_lore"));
                    idk_helper.setItemMeta(idk_helper_meta); //设置物品meta
                    inv.setItem(1, idk_helper); //添加物品到背包中

                    left -= 1; //left等于left减1
                    slot_used.add("1");

                    ItemStack idk_workbench = new ItemStack(Material.LEGACY_WORKBENCH, 1);
                    ItemMeta idk_workbench_meta = idk_workbench.getItemMeta();
                    idk_workbench_meta.setDisplayName(messages.getString("IDK_Workbench_name"));
                    idk_workbench_meta.setLore(messages.getStringList("IDK_Workbench_lore"));
                    idk_workbench.setItemMeta(idk_workbench_meta);
                    inv.setItem(7, idk_workbench);

                    left -= 1;
                    slot_used.add("7");

                    ItemStack other = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1); //创建一个物品，材质为蓝色玻璃板，数量为1个
                    ItemMeta other_meta = other.getItemMeta(); //获取物品meta
                    other_meta.setDisplayName(messages.getString("Information_name")); //设置物品显示名称为Infomation
                    other_meta.setLore(messages.getStringList("Information_lore"));
                    other.setItemMeta(other_meta); //设置物品meta

                    while(left > 0) { //如果left大于0则循环
                        inv.setItem(slot, other); //设置背包中第slot位的物品为other(默认第一格的槽位ID为0)
                        left -= 1; //left等于left减1
                        slot += 1; //slot等于slot加1
                        if(String.valueOf(slot).equals(slot_used.get(i))) {
                            slot += 1;
                            try {
                                if(!slot_used.get(i+1).isEmpty()) {
                                    i += 1;
                                }
                            } catch (IndexOutOfBoundsException e) {
                            }
                        }
                    }

                    player.openInventory(inv); //使指定玩家打开背包
                }
                return true;
            }
            if(strings.length == 2 && strings[0].equals("open")) { //检测参数长度是否为二并且第一个参数是否为open
                if(player.hasPermission("IDK.command.IDK.open")) {
                    if(player.hasPermission("IDK.command.IDK.open.workbench")) {
                        if(strings[1].equals("workbench")) { //检测第二个参数是否为workbench
                            Inventory inv = Bukkit.createInventory(null, InventoryType.WORKBENCH, messages.getString("workbench_title")); //创建一个背包，标题为IDK Workbench，类型为工作台
                            player.openInventory(inv); //使指定玩家打开背包
                            return true;
                        }
                    }
                }
            }
            if(strings.length == 1 && strings[0].equals("help")) { //检测参数长度是否为1并且第一个参数是否为help
                if(player.hasPermission("IDK.command.IDK.help")) {
                    List<String> help_msg = messages.getStringList("Help");
                    Object[] help_msg_fix = help_msg.toArray();
                    for(int i = 0; i < help_msg_fix.length; i++) {
                        player.sendMessage(help_msg_fix[i].toString());
                    }
                }
                return true;
            }
            if(strings.length == 2 && strings[0].equals("help")) {
                if(player.hasPermission("IDK.command.IDK.help.plugin")) {
                    if(strings[1].equals("plugin")) {
                        List<String> help_msg = messages.getStringList("Plugin_command_help");
                        Object[] help_msg_fix = help_msg.toArray();
                        for(int i = 0; i < help_msg_fix.length; i++) {
                            player.sendMessage(help_msg_fix[i].toString());
                        }
                        return true;
                    }
                }
                return false;
            }
            if(strings.length == 1 && strings[0].equals("ping")) {
                if(player.hasPermission("IDK.command.IDK.ping")) {
                    player.sendMessage(messages.getString("ping") + player.getPing() + "ms!"); //给玩家发送消息
                }
                return true;
            }
            if(IDK.idk.test_build) {
                if(strings.length == 1 && strings[0].equals("test")) {
                    try {
                        IDKnetHandler.update_plugins(commandSender);
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
            }
        }
        else{
            if(strings.length == 0) {
                List<String> main_msg = messages.getStringList("main");
                Object[] main_msg_fix = main_msg.toArray();
                for(int i = 0; i < main_msg_fix.length; i++) {
                    IDK.idk.logger.info(main_msg_fix[i].toString());
                }
                return true;
            }
            if(strings.length == 1 && strings[0].equals("reload")) {
                try{
                    messages.save();
                    this.checking = true;
                    this.check();
                    IDK.idk.reload();
                    config = IDK.idk.getConfig();
                    // 测试是否有空值
                    config.getBoolean("plugin-management");
                    config.getBoolean("debug");
                    config.getString("download-source");
                    messages.getString("IDK_Helper_name");
                    messages.getStringList("IDK_Helper_lore");
                    messages.getString("IDK_Workbench_name");
                    messages.getStringList("IDK_Workbench_lore");
                    messages.getString("menu_title");
                    messages.getString("workbench_title");
                    messages.getString("failed");
                    messages.getString("failed_p");
                    messages.getString("survival");
                    messages.getString("creative");
                    messages.getString("adventure");
                    messages.getString("spectator");
                    commandSender.sendMessage(messages.getString("reload"));
                } catch (Exception e) {
                    commandSender.sendMessage(messages.getString("failed"));
                    e.printStackTrace();
                }
                return true;
            }
            if(strings.length >= 1 && strings[0].equals("plugin")) {
                if(plugin_manage) {
                    if(strings.length == 2 && strings[1].equals("list")) {
                        IDK.idk.plugins = Arrays.toString(Bukkit.getPluginManager().getPlugins());
                        IDK.idk.logger.info(messages.getString("available-plugins") + IDK.idk.plugins);
                        return true;
                    }
                    if(strings.length == 3 && strings[1].equals("load")) {
                        String folder = Bukkit.getPluginsFolder().getAbsolutePath();
                        if(!strings[2].isEmpty()) {
                            String file_name = strings[2];
                            String file_path = folder + "\\" + file_name;
                            try {
                                Plugin plugin = Bukkit.getPluginManager().loadPlugin(new File(file_path));
                                if (plugin != null) {
                                    Bukkit.getPluginManager().enablePlugin(plugin);
                                    IDK.idk.logger.info("Plugin file " + file_name + " " + "plugin name " + plugin.getName() + " loaded!");
                                }
                                return true;
                            } catch (InvalidPluginException | InvalidDescriptionException e) {
                                return true;
                            }
                        } else {
                            IDK.idk.logger.warning("Error!");
                            return true;
                        }
                    }
                    if(strings.length == 3 && strings[1].equals("disable")) {
                        String plugin_name = strings[2];
                        Plugin plugin = Bukkit.getPluginManager().getPlugin(plugin_name);
                        if (plugin != null && plugin.isEnabled()) {
                            if (!plugin_name.equals("idk") && !plugin_name.equals("IDK")) {
                                try {
                                    Bukkit.getPluginManager().disablePlugin(plugin);
                                } catch (Exception e) {
                                    IDK.idk.logger.warning(messages.getString("plugin-disable-error"));
                                }
                                IDK.idk.logger.info(messages.getString("plugin-disabled").replace("[plugin_name]", plugin_name));
                            } else {
                                IDK.idk.logger.warning(messages.getString("plugin-disable-egg"));
                            }
                            return true;
                        }
                    }
                    if(strings.length == 3 && strings[1].equals("enable")) {
                        String plugin_name = strings[2];
                        Plugin plugin = Bukkit.getPluginManager().getPlugin(plugin_name);
                        if (plugin != null && !plugin.isEnabled()) {
                            if (!plugin_name.equals("idk") && !plugin_name.equals("IDK")) {
                                try {
                                    Bukkit.getPluginManager().enablePlugin(plugin);
                                } catch (Exception e) {
                                    IDK.idk.logger.warning(messages.getString("plugin-enable-error"));
                                }
                                IDK.idk.logger.info(messages.getString("plugin-enabled").replace("[plugin_name]", plugin_name));
                            } else {
                                IDK.idk.logger.warning(messages.getString("plugin-enable-egg"));
                            }
                            return true;
                        }
                    }
                    if(strings.length == 2 && strings[1].equals("search")) {
                        try {
                            IDKnetHandler.get_10_top_projects_a(commandSender);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    }
                    if(strings.length >= 3 && strings[1].equals("search")) {
                        try {
                            if(strings.length > 3) {
                                String plugin_title = "";
                                for(int i = 2; i < strings.length; i++) {
                                    plugin_title = plugin_title+strings[i]+" ";
                                }
                                String real_plugin_title = plugin_title.substring(0, plugin_title.length() - 1);
                                IDKnetHandler.get_projects_by_title_a(commandSender, real_plugin_title);
                            } else {
                                IDKnetHandler.get_projects_by_title_a(commandSender, strings[2]);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    }
                    if(strings.length == 3 && strings[1].equals("install")) {
                        try {
                            IDKnetHandler.install_project(commandSender, strings[2], false);
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    }
                    if(strings.length == 3 && strings[1].equals("del")) {
                        IDKPluginManagement idkpm = new IDKPluginManagement();
                        idkpm.delete_plugin(strings[2], commandSender);
                        return true;
                    }
                    if(strings.length == 2 && strings[1].equals("update")) {
                        try {
                            IDKnetHandler.update_self(commandSender);
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    }
                    if(strings.length == 3 && strings[1].equals("update")) {
                        if (strings[2].equals("all")) {
                            try {
                                IDKnetHandler.update_plugins(commandSender);
                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                    return false;
                }
                else {
                    IDK.idk.logger.warning("Plugin management function is not enabled in config.");
                    return true;
                }
            }
            if(strings.length == 2 && strings[0].equals("gm")) {
                IDK.idk.logger.warning(messages.getString("no-console"));
                return true;
            }
            if(strings.length == 1 && strings[0].equals("menu")) {
                IDK.idk.logger.warning(messages.getString("no-console"));
                return true;
            }
            if(strings.length == 2 && strings[0].equals("open")) {
                IDK.idk.logger.warning(messages.getString("no-console"));
                return true;
            }
            if(strings.length == 1 && strings[0].equals("help")) {
                List<String> help_msg = messages.getStringList("Help");
                Object[] help_msg_fix = help_msg.toArray();
                for(int i = 0; i < help_msg_fix.length; i++) {
                    IDK.idk.logger.info(help_msg_fix[i].toString());
                }
                return true;
            }
            if(strings.length == 2 && strings[0].equals("help")) {
                if(strings[1].equals("plugin")) {
                    List<String> help_msg = messages.getStringList("Plugin_command_help");
                    Object[] help_msg_fix = help_msg.toArray();
                    for(int i = 0; i < help_msg_fix.length; i++) {
                        IDK.idk.logger.info(help_msg_fix[i].toString());
                    }
                    return true;
                }
                return false;
            }
            if(strings.length == 1 && strings[0].equals("ping")) {
                IDK.idk.logger.warning(messages.getString("no-console"));
                return true;
            }
            if(IDK.idk.test_build) {
                if(strings.length == 1 && strings[0].equals("test")) {
                    try {
                        IDK.idk.logger.info(IDKnetHandler.update_plugins(commandSender));
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}

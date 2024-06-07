package krincraft.idk;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public final class IDK extends JavaPlugin {
    static IDK idk;
    public boolean test_build = false;
    public boolean beta_build = false;
    int config_ver = 1;
    boolean checking = false;
    Configuration messages = null;
    String plugins = null;

    @Override
    public void onLoad() {
        this.plugins = Arrays.toString(Bukkit.getPluginManager().getPlugins());
    }

    @Override
    public void onEnable() {
        //插件启用逻辑
        Bukkit.getPluginCommand("IDK").setExecutor(new IDKCommand()); //注册指令
        Bukkit.getPluginCommand("IDK").setTabCompleter(new IDKTabCompletor());
        Bukkit.getPluginManager().registerEvents(new IDKListener(), this); //注册事件处理
        saveDefaultConfig();
        idk = this;
    }

    public void check() {
        while(checking) {
            int read_config_ver = this.getConfig().getInt("config-version");
            if (read_config_ver == config_ver) {
                checking = false;
            } else {
                List<String> emptylist = new ArrayList<>();
                this.getConfig().set("config-version", 1);
                this.getConfig().set("IDK_Helper_name", "IDK Help");
                this.getConfig().set("IDK_Helper_lore", emptylist);
                this.getConfig().set("IDK_Workbench_name", "IDK Workbench");
                this.getConfig().set("IDK_Workbench_lore", emptylist);
                this.getConfig().set("menu_title", "IDK Menu");
                this.getConfig().set("workbench_title", "IDK Workbench");
                this.getConfig().set("Information_name", "Information");
                List<String> information_lore = new ArrayList<>();
                information_lore.add("Author: MinecraftBaymax");
                information_lore.add("Add by IDK plugin.");
                this.getConfig().set("Information_lore", information_lore);
                information_lore.clear();
                List<String> help_msg = new ArrayList<>();
                help_msg.add("IDK plugin help:");
                help_msg.add("\n/IDK help - This help message");
                help_msg.add("\n/IDK gm <number of gamemodes> - Change your gamemode");
                help_msg.add("\n/IDK open workbench - Open a chest or a workbench");
                help_msg.add("\n/IDK menu - open IDK menu");
                help_msg.add("\n/IDK ping - check your ping in game");
                help_msg.add("\n/IDK reload - reload config file");
                this.getConfig().set("Help", help_msg);
                help_msg.clear();
                checking = false;
            }
        }
    }

    @Override
    public void onDisable() {
        //插件关闭逻辑
        Logger logger = Bukkit.getLogger();
        logger.info("Stopping...");
    }
}

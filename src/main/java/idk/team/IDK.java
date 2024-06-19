package idk.team;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public final class IDK extends JavaPlugin {
    public Logger logger = Bukkit.getLogger();
    public static IDK idk;
    public boolean test_build = false;
    public boolean beta_build = false;
    public boolean debug = false;
    int config_ver = 2;
    Configuration messages = null;
    String plugins = null;

    @Override
    public void onLoad() {
        this.plugins = Arrays.toString(Bukkit.getPluginManager().getPlugins());
        Configuration defaults = new MemoryConfiguration();
        defaults.set("config-version", 2);
        defaults.set("plugin-management", true);
        defaults.set("debug", false);
        defaults.set("download-source", "papermc");
        this.getConfig().setDefaults(defaults);
    }

    @Override
    public void onEnable() {
        //插件启用逻辑
        Bukkit.getPluginCommand("IDK").setExecutor(new IDKCommand()); //注册指令
        Bukkit.getPluginCommand("IDK").setTabCompleter(new IDKTabCompletor());
        Bukkit.getPluginManager().registerEvents(new IDKListener(), this); //注册事件处理
        saveDefaultConfig();
        this.debug = this.getConfig().getBoolean("debug");
        if(this.debug) {
            logger.warning("Debug is enabled! It may cause some performance issue.");
        }
        idk = this;
    }

    public void reload() {
        this.reloadConfig();
        this.debug = this.getConfig().getBoolean("debug");
        if(this.debug) {
            logger.warning("Debug is enabled! It may cause some performance issue.");
        }
    }

    @Override
    public void onDisable() {
        //插件关闭逻辑
        logger.info("Stopping...");
    }
}

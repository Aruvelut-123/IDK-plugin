package idk.team;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public final class IDK extends JavaPlugin {
    public String data_folder = this.getDataFolder().getAbsolutePath();
    public IDKMessageConfig messages = new IDKMessageConfig(data_folder, this.getConfig().getString("lang")) {
        protected void finalize() throws Throwable {
            super.finalize();
        }
    };
    public Logger logger = Bukkit.getLogger();
    public static IDK idk;
    public boolean test_build = true;
    public boolean beta_build = true;
    public boolean debug = false;
    int config_ver = 3;
    String plugins = null;

    @Override
    public void onLoad() {
        this.plugins = Arrays.toString(Bukkit.getPluginManager().getPlugins());
        Configuration defaults = new MemoryConfiguration();
        defaults.set("config-version", 3);
        defaults.set("plugin-management", true);
        defaults.set("debug", true);
        defaults.set("download-source", "papermc");
        defaults.set("lang", "en");
        this.getConfig().setDefaults(defaults);
    }

    @Override
    public void onEnable() {
        //插件启用逻辑
        Bukkit.getPluginCommand("IDK").setExecutor(new IDKCommand()); //注册指令
        Bukkit.getPluginCommand("IDK").setTabCompleter(new IDKTabCompletor());
        Bukkit.getPluginManager().registerEvents(new IDKListener(), this); //注册事件处理
        saveDefaultConfig();
        String debug_warn = messages.getString("debug_warn");
        String papermc_warn = messages.getString("papermc_warn");
        this.debug = this.getConfig().getBoolean("debug");
        if(this.debug) {
            logger.warning(debug_warn);
        }
        if(Objects.equals(this.getConfig().getString("download-source"), "papermc")) {
            logger.warning(papermc_warn);
        }
        idk = this;
    }

    public void reload() {
        this.reloadConfig();
        messages.reload(this.getConfig().getString("lang"));
        this.debug = this.getConfig().getBoolean("debug");
        String debug_warn = messages.getString("debug_warn");
        String papermc_warn = messages.getString("papermc_warn");
        if(this.debug) {
            logger.warning(debug_warn);
        }
        if(Objects.equals(this.getConfig().getString("download-source"), "papermc")) {
            logger.warning(papermc_warn);
        }
    }

    @Override
    public void onDisable() {
        //插件关闭逻辑
        logger.info(messages.getString("stop"));
    }
}

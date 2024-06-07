package krincraft.idk;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public final class IDK extends JavaPlugin {
    public Logger logger = Bukkit.getLogger();
    public static IDK idk;
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
                this.getConfig().set("plugin-management", "true");
                checking = false;
            }
        }
    }

    @Override
    public void onDisable() {
        //插件关闭逻辑
        logger.info("Stopping...");
    }
}

package krincraft.idk;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class IDK extends JavaPlugin {
    @Override
    public void onEnable() {
        //插件启用逻辑
        Bukkit.getPluginCommand("IDK").setExecutor(new IDKCommand()); //注册指令
        Bukkit.getPluginCommand("IDK").setTabCompleter(new IDKTabCompletor());
        Bukkit.getPluginManager().registerEvents(new IDKListener(), this); //注册事件处理
    }

    @Override
    public void onDisable() {
        //插件关闭逻辑
        Logger logger = Bukkit.getLogger();
        logger.info("Stopping...");
    }
}

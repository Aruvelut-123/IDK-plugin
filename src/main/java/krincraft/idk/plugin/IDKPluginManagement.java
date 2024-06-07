package krincraft.idk.plugin;

import krincraft.idk.FileManager;
import krincraft.idk.IDKCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;

public class IDKPluginManagement {
    FileManager fm = new FileManager();
    public void delete_plugin(String plugin_name, CommandSender commandSender) {
        Plugin target = Bukkit.getPluginManager().getPlugin(plugin_name);
        if (target != null) {
            commandSender.sendMessage("Start disable plugin: " + plugin_name);
            Bukkit.getPluginManager().disablePlugin(target);
            commandSender.sendMessage( "Succeed!");
            getPluginFile(target).delete();
            if(target.getDataFolder().exists()) {
                fm.deleteDir(target.getDataFolder());
            }
            commandSender.sendMessage("Succeed to delete plugin "+plugin_name);
        } else {
            commandSender.sendMessage("Plugin not found!");
        }
    }

    public File getPluginFile(Plugin plugin) {
        File file = null;
        ClassLoader cl = plugin.getClass().getClassLoader();
        if (cl instanceof URLClassLoader) {
            URLClassLoader ucl = (URLClassLoader)cl;
            URL url = ucl.getURLs()[0];
            try {
                file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
            } catch (UnsupportedEncodingException unsupportedEncodingException) {}
        }
        return file;
    }
}
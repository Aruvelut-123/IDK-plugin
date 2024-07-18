package idk.team.plugin;

import idk.team.FileManager;
import idk.team.IDK;
import idk.team.IDKMessageConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;

public class IDKPluginManagement {
    FileManager fm = new FileManager();
    FileConfiguration config = IDK.idk.getConfig();
    String prefix = IDK.idk.prefix;
    IDKMessageConfig messages = new IDKMessageConfig(IDK.idk.data_folder, config.getString("lang")) {
        protected void finalize() throws Throwable {
            super.finalize();
        }
    };

    private String replace_plugin(String str, String plugin_name) {
        return str.replace("[plugin]", plugin_name);
    }

    public void delete_plugin(String plugin_name, CommandSender commandSender, boolean skipable) {
        this.prefix = IDK.idk.prefix;
        Plugin target = Bukkit.getPluginManager().getPlugin(plugin_name);
        String startdp = messages.getString("startdp");
        String succeeddp = messages.getString("succeeddp");
        String succeeddep = messages.getString("succeeddep");
        String pnf = messages.getString("pnf");
        if (target != null) {
            commandSender.sendMessage(prefix+replace_plugin(startdp, plugin_name));
            Bukkit.getPluginManager().disablePlugin(target);
            commandSender.sendMessage(prefix+replace_plugin(succeeddp, plugin_name));
            getPluginFile(target).delete();
            Bukkit.getPluginManager().enablePlugin(target);
            commandSender.sendMessage(prefix+replace_plugin(succeeddep, plugin_name));
        } else {
            commandSender.sendMessage(prefix+replace_plugin(pnf, plugin_name));
        }
    }

    public void delete_plugin(String plugin_name, CommandSender commandSender) {
        this.prefix = IDK.idk.prefix;
        Plugin target = Bukkit.getPluginManager().getPlugin(plugin_name);
        String startdp = messages.getString("startdp");
        String succeeddp = messages.getString("succeeddp");
        String succeeddep = messages.getString("succeeddep");
        String pnf = messages.getString("pnf");
        if (target != null) {
            commandSender.sendMessage(prefix+replace_plugin(startdp, plugin_name));
            Bukkit.getPluginManager().disablePlugin(target);
            commandSender.sendMessage(prefix+replace_plugin(succeeddp, plugin_name));
            getPluginFile(target).delete();
            if(target.getDataFolder().exists()) {
                fm.deleteDir(target.getDataFolder());
            }
            commandSender.sendMessage(prefix+replace_plugin(succeeddep, plugin_name));
        } else {
            commandSender.sendMessage(prefix+replace_plugin(pnf, plugin_name));
        }
    }

    public File getPluginFile(Plugin plugin) {
        this.prefix = IDK.idk.prefix;
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
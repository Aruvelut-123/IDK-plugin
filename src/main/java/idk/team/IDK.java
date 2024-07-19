package idk.team;

import idk.team.plugin.IDKPluginManagement;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public final class IDK extends JavaPlugin {
    public String data_folder = this.getDataFolder().getAbsolutePath();
    public IDKMessageConfig messages = new IDKMessageConfig(data_folder, this.getConfig().getString("lang")) {
        protected void finalize() throws Throwable {
            super.finalize();
        }
    };
    public Logger logger = Bukkit.getLogger();
    public static Logger log = Bukkit.getLogger();
    public static IDK idk;
    public boolean test_build = false;
    public boolean beta_build = false;
    public boolean alpha_build = true;
    public boolean debug = true;
    public String prefix = messages.getString("prefix");
    int config_ver = 4;
    String plugins = null;

    public static void unzipJar(String destinationDir, String jarPath) throws IOException {
        File file = new File(jarPath);
        JarFile jar = new JarFile(file);

        // fist get all directories,
        // then make those directory on the destination Path
        for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
            JarEntry entry = (JarEntry) enums.nextElement();

            String fileName = destinationDir + File.separator + entry.getName();
            File f = new File(fileName);

            if (fileName.endsWith("/")) {
                f.mkdirs();
            }

        }

        //now create all files
        for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
            JarEntry entry = (JarEntry) enums.nextElement();

            String fileName = destinationDir + File.separator + entry.getName();
            File f = new File(fileName);

            if (!fileName.endsWith("/")) {
                InputStream is = jar.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(f);

                // write contents of 'is' to 'fos'
                while (is.available() > 0) {
                    fos.write(is.read());
                }

                fos.close();
                is.close();
            }
        }
    }

    @Override
    public void onLoad() {
        this.plugins = Arrays.toString(Bukkit.getPluginManager().getPlugins());
        Configuration defaults = new MemoryConfiguration();
        defaults.set("config-version", 4);
        defaults.set("plugin-management", true);
        defaults.set("debug", true);
        defaults.set("download-source", "papermc");
        defaults.set("lang", "en");
        defaults.set("test-notify", "true");
        this.getConfig().setDefaults(defaults);
    }

    private boolean notfirsttime = false;

    @Override
    public void onEnable() {
        //插件启用逻辑
        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new ComponentFilter());
        Bukkit.getPluginCommand("IDK").setExecutor(new IDKCommand()); //注册指令
        Bukkit.getPluginCommand("IDK").setTabCompleter(new IDKTabCompletor());
        Bukkit.getPluginManager().registerEvents(new IDKListener(), this); //注册事件处理
        saveDefaultConfig();
        String debug_warn = messages.getString("debug_warn");
        String papermc_warn = messages.getString("papermc_warn");
        this.debug = this.getConfig().getBoolean("debug");
        if(!notfirsttime) {
            if(this.debug) {
                logger.warning(prefix+debug_warn);
            }
            if(Objects.equals(this.getConfig().getString("download-source"), "papermc")) {
                logger.warning(prefix+papermc_warn);
            }
            idk = this;
        } else {
            notfirsttime = true;
        }
    }

    public void reload() {
        this.reloadConfig();
        messages.reload(this.getConfig().getString("lang"));
        this.debug = this.getConfig().getBoolean("debug");
        String debug_warn = messages.getString("debug_warn");
        String papermc_warn = messages.getString("papermc_warn");
        this.prefix = messages.getString("prefix");
        if(this.debug) {
            logger.warning(prefix+debug_warn);
        }
        if(Objects.equals(this.getConfig().getString("download-source"), "papermc")) {
            logger.warning(prefix+papermc_warn);
        }
    }

    @Override
    public void onDisable() {
        //插件关闭逻辑
        logger.info(prefix+messages.getString("stop"));
    }
}

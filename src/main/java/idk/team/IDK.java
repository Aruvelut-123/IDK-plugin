package idk.team;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public final class IDK extends JavaPlugin {
    //获取插件数据文件夹的绝对路径
    public String data_folder = this.getDataFolder().getAbsolutePath();
    //获取插件消息配置
    public IDKMessageConfig messages = new IDKMessageConfig(data_folder, this.getConfig().getString("lang")) {
        protected void finalize() throws Throwable {
            super.finalize();
        }
    };
    //获取Bukkit的Logger
    public Logger logger = Bukkit.getLogger();
    //获取Bukkit的Logger
    public static Logger log = Bukkit.getLogger();
    //获取IDK的实例
    public static IDK idk;
    //测试版本
    public boolean test_build = false;
    //Beta版本
    public boolean beta_build = false;
    //Alpha版本
    public boolean alpha_build = true;
    //调试模式
    public boolean debug = true;
    //插件前缀
    public String prefix = messages.getString("prefix");
    //配置版本
    int config_ver = 4;
    //插件列表
    String plugins = null;

    //解压jar包
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

    //插件加载逻辑
    @Override
    public void onLoad() {
        this.plugins = Arrays.toString(Bukkit.getPluginManager().getPlugins());
        Configuration defaults = new MemoryConfiguration();
        defaults.set("config-version", 4);
        defaults.set("plugin-management", true);
        defaults.set("debug", false);
        defaults.set("download-source", "papermc");
        defaults.set("lang", "en");
        defaults.set("test-notify", "true");
        this.getConfig().setDefaults(defaults);
    }

    //插件启用逻辑
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

    //重新加载插件
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

    //插件关闭逻辑
    @Override
    public void onDisable() {
        //插件关闭逻辑
        logger.info(prefix+messages.getString("stop"));
    }
}
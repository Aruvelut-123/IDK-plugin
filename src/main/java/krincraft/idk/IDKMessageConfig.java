package krincraft.idk;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class IDKMessageConfig {
    static int ver = 3;
    protected IDK idk;
    private static File file;
    public static FileConfiguration config;

    public IDKMessageConfig(IDK idk, String fileName) {
        this.idk = idk;
        file = new File(idk.getDataFolder(), fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.write("message-ver: 3\n" +
                        "main: \"IDK插件 版本1.2.1\\n制作者 Baymaxawa\"\n" +
                        "reload: \"&配置文件重载成功！\"\n" +
                        "failed: \"配置文件重载失败！ 检查以下详细信息！\"\n" +
                        "failed_p: \"配置文件重载失败！ 查看控制台获取更多信息。\"\n" +
                        "survival: \"你现在处于生存模式！\"\n" +
                        "creative: \"你现在处于创造模式！\"\n" +
                        "adventure: \"你现在处于冒险模式！\"\n" +
                        "spectator: \"你现在处于旁观模式！\"\n" +
                        "ping: \"你的ping值为： \"\n" +
                        "no-console: \"当前无法使用控制台。\"\n" +
                        "available-plugins: \"可用插件：\"\n" +
                        "plugin-disable-error: \"无法禁用此插件，或已禁用！\"\n" +
                        "plugin-disabled: \"插件 [plugin_name] 已禁用\"\n" +
                        "plugin-enable-error: \"无法启用此插件，或已启用！\"\n" +
                        "plugin-enabled: \"插件 [plugin_name] 已启用\"\n" +
                        "plugin-not-found: \"未找到插件！\"\n" +
                        "plugin-disable-egg: \"你不能让IDK插件自己关闭自己！！！\"\n" +
                        "plugin-enable-egg: \"你不能让IDK插件自己启用自己！！！\"\n" +
                        "IDK_Helper_name: \"IDK 帮助\"\n" +
                        "IDK_Helper_lore:\n" +
                        "  - \"\"\n" +
                        "IDK_Workbench_name: \"IDK 工作台\"\n" +
                        "IDK_Workbench_lore:\n" +
                        "  - \"\"\n" +
                        "menu_title: \"IDK 菜单\"\n" +
                        "workbench_title: \"IDK 工作台\"\n" +
                        "Information_name: \"Information\"\n" +
                        "Information_lore:\n" +
                        "  - \"制作者: 老谭酸菜(bushi)\"\n" +
                        "  - \"这是一句废话\"\n" +
                        "Help:\n" +
                        "  -  \"IDK 插件帮助:\"\n" +
                        "  -  \"/IDK help - 显示帮助信息\"\n" +
                        "  -  \"/IDK gm <游戏模式编号> - 更改你的游戏模式\"\n" +
                        "  -  \"/IDK open workbench - 打开工作台\"\n" +
                        "  -  \"/IDK plugin - 插件指令\"\n" +
                        "  -  \"/IDK menu - 打开IDK菜单\"\n" +
                        "  -  \"/IDK ping - 显示你的ping值\"\n" +
                        "  -  \"/IDK reload - 重载配置文件\"\n" +
                        "Plugin_command_help:\n" +
                        "  -  \"IDK plugin help:\"\n" +
                        "  -  \"/IDK plugin list - 显示插件列表\"\n" +
                        "  -  \"/IDK plugin load <jar filename> - 载入插件\"\n" +
                        "  -  \"/IDK plugin disable <plugin name> - 禁用插件\"\n" +
                        "  -  \"/IDK plugin enable <plugin name> - 启用插件\"\n" +
                        "  -  \"/IDK plugin search - 搜索前十个热门插件\"\n" +
                        "  -  \"/IDK plugin search <plugin name> - 搜索插件\"\n" +
                        "  -  \"/IDK plugin install <plugin id> -输入插件ID并安装\"\n");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                config = YamlConfiguration.loadConfiguration(file);
                if(config.getInt("message-ver") != ver) {
                    file.delete();
                    file.createNewFile();
                    FileWriter fw = new FileWriter(file);
                    fw.write("message-ver: 3\n" +
                            "main: \"IDK插件 版本1.2.1\\n制作者 Baymaxawa\"\n" +
                            "reload: \"&配置文件重载成功！\"\n" +
                            "failed: \"配置文件重载失败！ 检查以下详细信息！\"\n" +
                            "failed_p: \"配置文件重载失败！ 查看控制台获取更多信息。\"\n" +
                            "survival: \"你现在处于生存模式！\"\n" +
                            "creative: \"你现在处于创造模式！\"\n" +
                            "adventure: \"你现在处于冒险模式！\"\n" +
                            "spectator: \"你现在处于旁观模式！\"\n" +
                            "ping: \"你的ping值为： \"\n" +
                            "no-console: \"当前无法使用控制台。\"\n" +
                            "available-plugins: \"可用插件：\"\n" +
                            "plugin-disable-error: \"无法禁用此插件，或已禁用！\"\n" +
                            "plugin-disabled: \"插件 [plugin_name] 已禁用\"\n" +
                            "plugin-enable-error: \"无法启用此插件，或已启用！\"\n" +
                            "plugin-enabled: \"插件 [plugin_name] 已启用\"\n" +
                            "plugin-not-found: \"未找到插件！\"\n" +
                            "plugin-disable-egg: \"你不能让IDK插件自己关闭自己！！！\"\n" +
                            "plugin-enable-egg: \"你不能让IDK插件自己启用自己！！！\"\n" +
                            "IDK_Helper_name: \"IDK 帮助\"\n" +
                            "IDK_Helper_lore:\n" +
                            "  - \"\"\n" +
                            "IDK_Workbench_name: \"IDK 工作台\"\n" +
                            "IDK_Workbench_lore:\n" +
                            "  - \"\"\n" +
                            "menu_title: \"IDK 菜单\"\n" +
                            "workbench_title: \"IDK 工作台\"\n" +
                            "Information_name: \"Information\"\n" +
                            "Information_lore:\n" +
                            "  - \"制作者: 老谭酸菜(bushi)\"\n" +
                            "  - \"这是一句废话\"\n" +
                            "Help:\n" +
                            "  -  \"IDK 插件帮助:\"\n" +
                            "  -  \"/IDK help - 显示帮助信息\"\n" +
                            "  -  \"/IDK gm <游戏模式编号> - 更改你的游戏模式\"\n" +
                            "  -  \"/IDK open workbench - 打开工作台\"\n" +
                            "  -  \"/IDK plugin - 插件指令\"\n" +
                            "  -  \"/IDK menu - 打开IDK菜单\"\n" +
                            "  -  \"/IDK ping - 显示你的ping值\"\n" +
                            "  -  \"/IDK reload - 重载配置文件\"\n" +
                            "Plugin_command_help:\n" +
                            "  -  \"IDK plugin help:\"\n" +
                            "  -  \"/IDK plugin list - 显示插件列表\"\n" +
                            "  -  \"/IDK plugin load <jar filename> - 载入插件\"\n" +
                            "  -  \"/IDK plugin disable <plugin name> - 禁用插件\"\n" +
                            "  -  \"/IDK plugin enable <plugin name> - 启用插件\"\n" +
                            "  -  \"/IDK plugin search - 搜索前十个热门插件\"\n" +
                            "  -  \"/IDK plugin search <plugin name> - 搜索插件\"\n" +
                            "  -  \"/IDK plugin install <plugin id> -输入插件ID并安装\"\n");
                    fw.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public String getString(String s) {
        String a = config.getString(s);
        if (a != null) {
            return a;
        } else {
            return "null";
        }
    }

    public static void reload(String fileName) {
        file = new File(Bukkit.getPluginsFolder().getAbsolutePath()+"\\IDK", fileName);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getStringList(String str) {
        return config.getStringList(str);
    }
}

package krincraft.idk;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class IDKMessageConfig {
    static int ver = 2;
    protected IDK idk;
    private File file;
    protected FileConfiguration config;

    public IDKMessageConfig(IDK idk, String fileName) {
        this.idk = idk;
        this.file = new File(idk.getDataFolder(), fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.write("message-ver: 2\n" +
                        "reload: \"Config reloaded!\"\n" +
                        "failed: \"Config reload failed! Check details below!\"\n" +
                        "failed_p: \"Config reload failed! Check console for more information.\"\n" +
                        "survival: \"You're now in SURVIVAL!\"\n" +
                        "creative: \"You're now in CREATIVE!\"\n" +
                        "adventure: \"You're now in ADVENTURE!\"\n" +
                        "spectator: \"You're now in SPECTATOR!\"\n" +
                        "ping: \"Your ping is: \"\n" +
                        "no-console: \"Console not avalible right now.\"\n" +
                        "available-plugins: \"Available plugins: \"\n" +
                        "plugin-disable-error: \"Cannot disable this plugin or already disabled!\"\n" +
                        "plugin-disabled: \"Plugin [plugin_name] disabled!\"\n" +
                        "plugin-enable-error: \"Cannot enable this plugin or already enabled!\"\n" +
                        "plugin-enabled: \"Plugin [plugin_name] enabled!\"\n" +
                        "plugin-not-found: \"Plugin not found!\"\n" +
                        "plugin-disable-egg: \"I cannot disable myself. Like you cannot make your heart stop.\"\n" +
                        "plugin-enable-egg: \"I cannot enable myself. Like you cannot make dead alive.\"\n" +
                        "IDK_Helper_name: \"IDK Help\"\n" +
                        "IDK_Helper_lore:\n" +
                        "  - \"\"\n" +
                        "IDK_Workbench_name: \"IDK Workbench\"\n" +
                        "IDK_Workbench_lore:\n" +
                        "  - \"\"\n" +
                        "menu_title: \"IDK Menu\"\n" +
                        "workbench_title: \"IDK Workbench\"\n" +
                        "Information_name: \"Information\"\n" +
                        "Information_lore:\n" +
                        "  - \"Author: MinecraftBaymax\"\n" +
                        "  - \"Add by IDK plugin.\"\n" +
                        "Help:\n" +
                        "  -  \"IDK plugin help:\"\n" +
                        "  -  \"/IDK help - This help message\"\n" +
                        "  -  \"/IDK gm <number of gamemodes> - Change your gamemode\"\n" +
                        "  -  \"/IDK open workbench - Open a chest or a workbench\"\n" +
                        "  -  \"/IDK plugin - plugin commands\"\n" +
                        "  -  \"/IDK menu - open IDK menu\"\n" +
                        "  -  \"/IDK ping - check your ping in game\"\n" +
                        "  -  \"/IDK reload - reload config file\"\n" +
                        "Plugin_command_help:\n" +
                        "  -  \"IDK plugin help:\"\n" +
                        "  -  \"/IDK plugin list - Check plugin list\"\n" +
                        "  -  \"/IDK plugin load <jar filename> - Load a unloaded jar plugin to server\"\n" +
                        "  -  \"/IDK plugin disable <plugin name> - Disable a plugin\"\n" +
                        "  -  \"/IDK plugin enable <plugin name> - Enable a plugin\"\n" +
                        "  -  \"/IDK plugin search - Get Top 10 Plugin\"\n" +
                        "  -  \"/IDK plugin search <plugin name> - Search a plugin by it's known name\"\n");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.config = YamlConfiguration.loadConfiguration(file);
                if(this.config.getInt("message-ver") != ver) {
                    file.delete();
                    file.createNewFile();
                    FileWriter fw = new FileWriter(file);
                    fw.write("message-ver: 1\n" +
                            "reload: \"Config reloaded!\"\n" +
                            "failed: \"Config reload failed! Check details below!\"\n" +
                            "failed_p: \"Config reload failed! Check console for more information.\"\n" +
                            "survival: \"You're now in SURVIVAL!\"\n" +
                            "creative: \"You're now in CREATIVE!\"\n" +
                            "adventure: \"You're now in ADVENTURE!\"\n" +
                            "spectator: \"You're now in SPECTATOR!\"\n" +
                            "ping: \"Your ping is: \"\n" +
                            "no-console: \"Console not avalible right now.\"\n" +
                            "available-plugins: \"Available plugins: \"\n" +
                            "plugin-disable-error: \"Cannot disable this plugin or already disabled!\"\n" +
                            "plugin-disabled: \"Plugin [plugin_name] disabled!\"\n" +
                            "plugin-enable-error: \"Cannot enable this plugin or already enabled!\"\n" +
                            "plugin-enabled: \"Plugin [plugin_name] enabled!\"\n" +
                            "plugin-not-found: \"Plugin not found!\"\n" +
                            "plugin-disable-egg: \"I cannot disable myself. Like you cannot make your heart stop.\"\n" +
                            "plugin-enable-egg: \"I cannot enable myself. Like you cannot make dead alive.\"\n" +
                            "IDK_Helper_name: \"IDK Help\"\n" +
                            "IDK_Helper_lore:\n" +
                            "  - \"\"\n" +
                            "IDK_Workbench_name: \"IDK Workbench\"\n" +
                            "IDK_Workbench_lore:\n" +
                            "  - \"\"\n" +
                            "menu_title: \"IDK Menu\"\n" +
                            "workbench_title: \"IDK Workbench\"\n" +
                            "Information_name: \"Information\"\n" +
                            "Information_lore:\n" +
                            "  - \"Author: MinecraftBaymax\"\n" +
                            "  - \"Add by IDK plugin.\"\n" +
                            "Help:\n" +
                            "  -  \"IDK plugin help:\"\n" +
                            "  -  \"/IDK help - This help message\"\n" +
                            "  -  \"/IDK gm <number of gamemodes> - Change your gamemode\"\n" +
                            "  -  \"/IDK open workbench - Open a chest or a workbench\"\n" +
                            "  -  \"/IDK plugin - plugin commands\"\n" +
                            "  -  \"/IDK menu - open IDK menu\"\n" +
                            "  -  \"/IDK ping - check your ping in game\"\n" +
                            "  -  \"/IDK reload - reload config file\"\n" +
                            "Plugin_command_help:\n" +
                            "  -  \"IDK plugin help:\"\n" +
                            "  -  \"/IDK plugin list - Check plugin list\"\n" +
                            "  -  \"/IDK plugin load <jar filename> - Load a unloaded jar plugin to server\"\n" +
                            "  -  \"/IDK plugin disable <plugin name> - Disable a plugin\"\n" +
                            "  -  \"/IDK plugin enable <plugin name> - Enable a plugin\"\n" +
                            "  -  \"/IDK plugin search - Get Top 10 Plugin\"\n" +
                            "  -  \"/IDK plugin search title <plugin name> - Search a plugin by it's known name\"\n" +
                            "  -  \"/IDK plugin search id <plugin id> - Search a plugin by it's modrinth id\"\n");
                    fw.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public String getString(String s) {
        String a = this.config.getString(s);
        if (a != null) {
            return a;
        } else {
            return "null";
        }
    }

    public void reload(String fileName) {
        this.file = new File(idk.getDataFolder(), fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getStringList(String str) {
        return this.config.getStringList(str);
    }
}

package idk.team;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class IDKMessageConfig {
    static int ver = 6;
    protected IDK idk;
    private File file;
    public FileConfiguration config;

    public IDKMessageConfig(String file_path, String fileName) {
        if (file_path != null && !file_path.isEmpty() && fileName != null && !fileName.isEmpty()) {
            file = new File(file_path, fileName);
            String str = "message-ver: 6\n" +
                    "main:\n" +
                    "  -  'IDK Plugin Version 1.2.4'\n" +
                    "  -  'Made by Baymaxawa'\n" +
                    "reload: 'Config reloaded!'\n" +
                    "failed: 'Config reload failed! Check details below!'\n" +
                    "failed_p: 'Config reload failed! Check console for more information.'\n" +
                    "survival: 'You\"re now in SURVIVAL!'\n" +
                    "creative: 'You\"re now in CREATIVE!'\n" +
                    "adventure: 'You\"re now in ADVENTURE!'\n" +
                    "spectator: 'You\"re now in SPECTATOR!'\n" +
                    "ping: 'Your ping is: '\n" +
                    "no-console: 'Console not avalible right now.'\n" +
                    "available-plugins: 'Available plugins: '\n" +
                    "plugin-disable-error: 'Cannot disable this plugin or already disabled!'\n" +
                    "plugin-disabled: 'Plugin [plugin_name] disabled!'\n" +
                    "plugin-enable-error: 'Cannot enable this plugin or already enabled!'\n" +
                    "plugin-enabled: 'Plugin [plugin_name] enabled!'\n" +
                    "plugin-not-found: 'Plugin not found!'\n" +
                    "plugin-disable-egg: 'I cannot disable myself. Like you cannot make your heart stop.'\n" +
                    "plugin-enable-egg: 'I cannot enable myself. Like you cannot make dead alive.'\n" +
                    "IDK_Helper_name: 'IDK Help'\n" +
                    "IDK_Helper_lore:\n" +
                    "  - ''\n" +
                    "IDK_Workbench_name: 'IDK Workbench'\n" +
                    "IDK_Workbench_lore:\n" +
                    "  - ''\n" +
                    "menu_title: 'IDK Menu'\n" +
                    "workbench_title: 'IDK Workbench'\n" +
                    "Information_name: 'Information'\n" +
                    "Information_lore:\n" +
                    "  - 'Author: Baymaxawa'\n" +
                    "  - 'Add by IDK plugin.'\n" +
                    "Help:\n" +
                    "  -  'IDK plugin help:'\n" +
                    "  -  '/IDK help - This help message'\n" +
                    "  -  '/IDK gm <number of gamemodes> - Change your gamemode'\n" +
                    "  -  '/IDK open workbench - Open a workbench'\n" +
                    "  -  '/IDK plugin - plugin management commands'\n" +
                    "  -  '/IDK menu - open IDK menu'\n" +
                    "  -  '/IDK ping - check your ping in game'\n" +
                    "  -  '/IDK reload - reload config file'\n" +
                    "Plugin_command_help:\n" +
                    "  -  'IDK plugin help:'\n" +
                    "  -  '/IDK plugin list - Check plugin list'\n" +
                    "  -  '/IDK plugin load <jar filename> - Load a unloaded jar plugin to server'\n" +
                    "  -  '/IDK plugin disable <plugin name> - Disable a plugin'\n" +
                    "  -  '/IDK plugin enable <plugin name> - Enable a plugin'\n" +
                    "  -  '/IDK plugin search - Get Top 10 Plugin'\n" +
                    "  -  '/IDK plugin search <plugin name> - Search a plugin by it\"s known name'\n" +
                    "  -  '/IDK plugin install <plugin name> - Install a plugin by it\"s known name'\n" +
                    "  -  '/IDK plugin del <plugin name> - Delete a plugin by it\"s name'\n" +
                    "current_progress: 'Current Progress: %progress%%'\n" +
                    "download_complete: 'Download Complete!'\n" +
                    "download_complete2: 'File downloaded successfully to: %path%'\n" +
                    "download_start: 'Start download file: %filename% from: %url%'\n" +
                    "install_plugin_notice1: 'Install this plugin by click'\n" +
                    "install_plugin_notice2: 'or type /idk plugin install [plugin_id]'\n" +
                    "install_button: '[install]'\n" +
                    "install_button_hover: 'Click here to install'\n" +
                    "pnh: 'Plugin name here'\n" +
                    "pinh: 'Plugin id/name here'\n" +
                    "join_message:\n" +
                    "  -  '[Player] joined this server!'\n" +
                    "  -  'Message changed by IDK plugin.'\n" +
                    "leave_message:\n" +
                    "  -  '[Player] leaved this server!'\n" +
                    "  -  'Message changed by IDK plugin.'\n" +
                    "warning_test: 'You are currently using a test build of IDK plugin!'\n" +
                    "warning_beta: 'You are currently using a beta build of IDK plugin!'\n" +
                    "warning_2: 'Use it at your own risks!'\n" +
                    "warning_title: 'Warning!'\n" +
                    "debug_warn: 'Debug is enabled! It may cause some performance issue.'\n" +
                    "del_fail: 'Failed to delete [file]!'\n" +
                    "del_complete: 'Succeed to delete [file]!'\n" +
                    "startdp: 'Start disable plugin: [plugin]'\n" +
                    "succeeddp: 'Succeed to disable plugin [plugin]!'\n" +
                    "succeeddep: 'Succeed to delete plugin [plugin]'\n" +
                    "pnf: 'Plugin [plugin] not found!'\n" +
                    "stop: 'Stopping...'\n" +
                    "debug_p: 'commandSender=Player'\n" +
                    "debug_c: 'commandSender=Console'\n" +
                    "get_10_top_plugin: 'Getting latest 10 plugins from [source]...'\n" +
                    "10_top_plugin: '10 Top Plugins:'\n" +
                    "search_plugin_list:\n" +
                    "  -  '[number]. Name: [name]'\n" +
                    "  -  'Description: [description]'\n" +
                    "search_plugin_list_2:\n" +
                    "  -  '[number]. Name: [name]'\n" +
                    "  -  'ID: [id]'\n" +
                    "  -  'Description: [description]'\n" +
                    "install_tip: 'Install a plugin by using: /idk plugin install <name>'\n" +
                    "error_papermc_p: 'Error in getting from papermc, see console for more details.'\n" +
                    "error_papermc_c: 'Error in getting from papermc, see above message for more details.'\n" +
                    "switch_to_source: 'Switching to [source].'\n" +
                    "error_download_source: 'Download source is invalid!'\n" +
                    "find_plugin: 'Start finding plugin name: [name] from [source].'\n" +
                    "no_duplicate: 'Plugin has already installed! No duplicate!'\n" +
                    "error_install_p: 'Plugin not found or error was made, see console for more details.'\n" +
                    "error_install_c: 'Plugin not found or error was made, see above message for more details.'\n" +
                    "load_plugin: 'Start loading plugin [name]'\n" +
                    "plugin_loaded: 'Plugin [name] loaded!'\n" +
                    "enable_plugin: 'Start enable plugin [name]'\n" +
                    "install_complete: 'Plugin [name]\"s installation was complete!'\n" +
                    "get_plugin_info: 'Trying to get [name] plugin\"s info from [source]'\n" +
                    "papermc_warn: 'Warning! Papermc source cannot use fuzzy search, that means you must use full name to search a plugin.'\n";
            if(!file.exists()) {
                try {
                    file.createNewFile();
                    FileWriter fw = new FileWriter(file);
                    fw.write(str);
                    fw.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    config = YamlConfiguration.loadConfiguration(file);
                    if (config.getInt("message-ver") != ver) {
                        file.delete();
                        file.createNewFile();
                        FileWriter fw = new FileWriter(file);
                        fw.write(str);
                        fw.close();
                    }
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            config = YamlConfiguration.loadConfiguration(file);
        }
        else {
            throw new RuntimeException("Error: messages file is null, please report to developer.");
        }
    }

    public String getString(String s) {
        return config.getString(s);
    }

    public void reload(String fileName) {
        file = new File(Bukkit.getPluginsFolder().getAbsolutePath()+"\\IDK", fileName);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
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

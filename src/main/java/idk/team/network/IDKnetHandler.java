package idk.team.network;

import idk.team.IDK;
import idk.team.IDKMessageConfig;
import idk.team.JSON.decoder;
import idk.team.plugin.IDKPluginManagement;
import io.papermc.paper.plugin.configuration.PluginMeta;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

public class IDKnetHandler implements Runnable {
    static String prefix = "";
    static FileConfiguration config = IDK.idk.getConfig();
    static IDKMessageConfig messages = new IDKMessageConfig(IDK.idk.data_folder, config.getString("lang")) {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    };
    private static String download_source = IDK.idk.getConfig().getString("download-source");

    private static void refresh_download_source() {
        download_source = IDK.idk.getConfig().getString("download-source");
        prefix = IDK.idk.prefix;
    }

    public static void get_10_top_projects(CommandSender commandSender) throws IOException {
        if (IDK.idk.debug) {
            if (commandSender instanceof Player) {
                commandSender.sendMessage(prefix+messages.getString("debug_p"));
            } else {
                commandSender.sendMessage(prefix+messages.getString("debug_c"));
            }
        }
        refresh_download_source();
        String get_10_top_plugin = messages.getString("get_10_top_plugin");
        if (Objects.equals(download_source, "papermc")) {
            commandSender.sendMessage(prefix+get_10_top_plugin.replace("[source]", download_source));
            int limit = 10;
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"limit="+limit);
            }
            String version = Bukkit.getServer().getMinecraftVersion();
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"version="+version);
            }
            String url = "https://hangar.papermc.io/api/v1/projects?limit="+limit+"&version="+version;
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"url="+url);
            }
            String result = sendGet(url);
            commandSender.sendMessage(prefix+messages.getString("10_top_plugin"));
            for(int i = 0; i < 10; i++) {
                String plugin_title = decoder.decode_json(result, i, "result", "name");
                String plugin_description = decoder.decode_json(result, i, "result", "description");
                int a = i+1;
                List<String> search_plugin_list = messages.getStringList("search_plugin_list");
                Object[] search_plugin_list_fix = search_plugin_list.toArray();
                StringBuilder fixed = new StringBuilder();
                for(int b = 0; b < search_plugin_list_fix.length; b++) {
                    fixed.append(search_plugin_list_fix[b].toString());
                    if (b != search_plugin_list_fix.length - 1) {
                        fixed.append("\n");
                    }
                }
                commandSender.sendMessage(prefix+fixed.toString().replace("[number]", String.valueOf(a)).replace("[name]", plugin_title).replace("[description]", plugin_description));
            }
            commandSender.sendMessage(prefix+messages.getString("install_tip"));
        }
        else if (Objects.equals(download_source, "modrinth")) {
            commandSender.sendMessage(prefix+get_10_top_plugin.replace("[source]", download_source));
            int limit = 10;
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"limit="+limit);
            }
            String url = "https://api.modrinth.com/v2/search?limit="+limit+"&facets=";
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"url="+url);
            }
            String categories = "paper";
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"categories="+categories);
            }
            String versions = Bukkit.getServer().getMinecraftVersion();
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"versions="+versions);
            }
            String arg = "[[\"categories:"+categories+"\"],[\"versions:"+versions+"\"],[\"project_type:plugin\"]]";
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"arg="+arg);
            }
            String real_uri = argument_handler(url, arg);
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"real_uri="+real_uri);
            }
            String result = sendGet(real_uri);
            commandSender.sendMessage(prefix+messages.getString("10_top_plugin"));
            for(int i = 0; i < 10; i++) {
                String plugin_title = decoder.decode_json(result, i, "hits", "title");
                String plugin_description = decoder.decode_json(result, i, "hits", "description");
                String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                int a = i+1;
                List<String> search_plugin_list = messages.getStringList("search_plugin_list_2");
                Object[] search_plugin_list_fix = search_plugin_list.toArray();
                StringBuilder fixed = new StringBuilder();
                for(int b = 0; b < search_plugin_list_fix.length; b++) {
                    fixed.append(search_plugin_list_fix[b].toString());
                    if (b != search_plugin_list_fix.length - 1) {
                        fixed.append("\n");
                    }
                }
                commandSender.sendMessage(prefix+fixed.toString().replace("[number]", String.valueOf(a)).replace("[name]", plugin_title).replace("[id]", plugin_id).replace("[description]", plugin_description));
            }
            commandSender.sendMessage(prefix+messages.getString("install_tip"));
        }
        else if (Objects.equals(download_source, "both")) {
            try {
                commandSender.sendMessage(prefix+get_10_top_plugin.replace("[source]", "papermc"));
                int limit = 10;
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"limit="+limit);
                }
                String version = Bukkit.getServer().getMinecraftVersion();
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"version="+version);
                }
                String url = "https://hangar.papermc.io/api/v1/projects?limit="+limit+"&version="+version;
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"url="+url);
                }
                String result = sendGet(url);
                commandSender.sendMessage(prefix+messages.getString("10_top_plugin"));
                for(int i = 0; i < 10; i++) {
                    String plugin_title = decoder.decode_json(result, i, "result", "name");
                    String plugin_description = decoder.decode_json(result, i, "result", "description");
                    int a = i+1;
                    List<String> search_plugin_list = messages.getStringList("search_plugin_list");
                    Object[] search_plugin_list_fix = search_plugin_list.toArray();
                    StringBuilder fixed = new StringBuilder();
                    for(int b = 0; b < search_plugin_list_fix.length; b++) {
                        fixed.append(search_plugin_list_fix[b].toString());
                        if (b != search_plugin_list_fix.length - 1) {
                            fixed.append("\n");
                        }
                    }
                    commandSender.sendMessage(prefix+fixed.toString().replace("[number]", String.valueOf(a)).replace("[name]", plugin_title).replace("[description]", plugin_description));
                }
                commandSender.sendMessage(prefix+messages.getString("install_tip"));
            }
            catch (Exception e) {
                e.printStackTrace();
                if (commandSender instanceof Player) {
                    commandSender.sendMessage(prefix+messages.getString("error_papermc_p"));
                }
                else {
                    commandSender.sendMessage(prefix+messages.getString("error_papermc_c"));
                }
                commandSender.sendMessage(prefix+messages.getString("switch_to_source").replace("[source]", "modrinth"));
                commandSender.sendMessage(prefix+get_10_top_plugin.replace("[source]", "modrinth"));
                int limit = 10;
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"limit="+limit);
                }
                String url = "https://api.modrinth.com/v2/search?limit="+limit+"&facets=";
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"url="+url);
                }
                String categories = "paper";
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"categories="+categories);
                }
                String versions = Bukkit.getServer().getMinecraftVersion();
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"versions="+versions);
                }
                String arg = "[[\"categories:"+categories+"\"],[\"versions:"+versions+"\"],[\"project_type:plugin\"]]";
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"arg="+arg);
                }
                String real_uri = argument_handler(url, arg);
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"real_uri="+real_uri);
                }
                String result = sendGet(real_uri);
                commandSender.sendMessage(prefix+messages.getString("10_top_plugin"));
                for(int i = 0; i < 10; i++) {
                    String plugin_title = decoder.decode_json(result, i, "hits", "title");
                    String plugin_description = decoder.decode_json(result, i, "hits", "description");
                    String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                    int a = i+1;
                    List<String> search_plugin_list = messages.getStringList("search_plugin_list_2");
                    Object[] search_plugin_list_fix = search_plugin_list.toArray();
                    StringBuilder fixed = new StringBuilder();
                    for(int b = 0; b < search_plugin_list_fix.length; b++) {
                        fixed.append(search_plugin_list_fix[b].toString());
                        if (b != search_plugin_list_fix.length - 1) {
                            fixed.append("\n");
                        }
                    }
                    commandSender.sendMessage(prefix+fixed.toString().replace("[number]", String.valueOf(a)).replace("[name]", plugin_title).replace("[id]", plugin_id).replace("[description]", plugin_description));
                }
                commandSender.sendMessage(prefix+messages.getString("install_tip"));
            }
        }
        else {
            commandSender.sendMessage(prefix+messages.getString("error_download_source"));
        }
    }

    public static String convert_list_string_to_json_string(String list_string) {
        String fix_list_string = list_string.substring(1);
        return fix_list_string.substring(0, fix_list_string.length() - 1);
    }

    public static void install_project(CommandSender commandSender, String project_id, boolean skipable) throws IOException {
        if (IDK.idk.debug) {
            if (commandSender instanceof Player) {
                commandSender.sendMessage(prefix+messages.getString("debug_p"));
            } else {
                commandSender.sendMessage(prefix+messages.getString("debug_c"));
            }
            commandSender.sendMessage(prefix+"project_id="+project_id);
        }
        refresh_download_source();
        String find_plugin = messages.getString("find_plugin");
        if (Objects.equals(download_source, "papermc")) {
            commandSender.sendMessage(prefix+find_plugin.replace("[name]", project_id).replace("[source]", download_source));
            int limit = 10;
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"limit="+limit);
            }
            String version = Bukkit.getServer().getMinecraftVersion();
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"version="+version);
            }
            String url = "https://hangar.papermc.io/api/v1/projects/{id|slug}?limit="+limit+"&version="+version;
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"url="+url);
            }
            String real_uri = url.replace("{id|slug}", project_id);
            String result = sendGet(real_uri);
            String plugin_title = decoder.decode_json(result, "name");
            if (!skipable) {
                if (Bukkit.getPluginManager().getPlugin(plugin_title) != null) {
                    commandSender.sendMessage(prefix+messages.getString("no_duplicate"));
                }
                else {
                    try {
                        String new_url = "https://hangar.papermc.io/api/v1/projects/{id|slug}/versions?limit="+limit;
                        String new_real_uri = new_url.replace("{id|slug}",project_id);
                        String new_result = sendGet(new_real_uri);
                        String new_fixed_result = convert_list_string_to_json_string(decoder.decode_json(new_result, "result"));
                        String files = decoder.decode_json(new_fixed_result, "downloads");
                        String platform = "PAPER";
                        String fixed_file = decoder.decode_json(files, platform);
                        String file_url = decoder.decode_json(fixed_file, "downloadUrl");
                        String file_info = decoder.decode_json(fixed_file, "fileInfo");
                        String file_name = decoder.decode_json(file_info, "name");
                        String file_path = Bukkit.getPluginsFolder().getAbsolutePath()+"\\"+file_name;
                        downloadFileByUrl(plugin_title, file_url, file_path, file_name, commandSender);
                        enable_plugin(file_path, plugin_title, commandSender);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        if (commandSender instanceof Player) {
                            messages.getString("error_install_p");
                        }
                        else {
                            messages.getString("error_install_c");
                        }
                    }
                }
            }
            else {
                try {
                    String new_url = "https://hangar.papermc.io/api/v1/projects/{id|slug}/versions?limit="+limit;
                    String new_real_uri = new_url.replace("{id|slug}",project_id);
                    String new_result = sendGet(new_real_uri);
                    String new_fixed_result = convert_list_string_to_json_string(decoder.decode_json(new_result, "result"));
                    String files = decoder.decode_json(new_fixed_result, "downloads");
                    String platform = "PAPER";
                    String fixed_file = decoder.decode_json(files, platform);
                    String file_url = decoder.decode_json(fixed_file, "downloadUrl");
                    String file_info = decoder.decode_json(fixed_file, "fileInfo");
                    String file_name = decoder.decode_json(file_info, "name");
                    String file_path = Bukkit.getPluginsFolder().getAbsolutePath()+"\\"+file_name;
                    downloadFileByUrl(plugin_title, file_url, file_path, file_name, commandSender);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    if (commandSender instanceof Player) {
                        messages.getString("error_install_p");
                    }
                    else {
                        messages.getString("error_install_c");
                    }
                }
            }
        }
        else if (Objects.equals(download_source, "modrinth")) {
            commandSender.sendMessage(prefix+find_plugin.replace("[name]", project_id).replace("[source]", download_source));
            boolean featured = true;
            String loader = "paper";
            String game_version = Bukkit.getMinecraftVersion();
            String url = "https://api.modrinth.com/v2/project/{id|slug}?featured="+featured;
            String real_uri = url.replace("{id|slug}",project_id);
            String args = "&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
            String real_url = argument_handler(real_uri, args);
            String result = sendGet(real_url);
            String plugin_title = decoder.decode_json(result, "title");
            if (!skipable) {
                if (Bukkit.getPluginManager().getPlugin(plugin_title) != null) {
                    commandSender.sendMessage(prefix+messages.getString("no_duplicate"));
                }
                else {
                    try {
                        String new_url = "https://api.modrinth.com/v2/project/{id|slug}/version?";
                        String new_real_uri = new_url.replace("{id|slug}",project_id);
                        String new_args = "\"featured\"=\""+featured+"\"&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                        String new_real_url = argument_handler(new_real_uri, new_args);
                        String new_result = sendGet(new_real_url);
                        String fixed_new_result = convert_list_string_to_json_string(new_result);
                        String files = decoder.decode_json(fixed_new_result, "files");
                        String fixed_files = convert_list_string_to_json_string(files);
                        String file_url = decoder.decode_json(fixed_files, "url");
                        String file_name = decoder.decode_json(fixed_files, "filename");
                        String file_path = Bukkit.getPluginsFolder().getAbsolutePath()+"\\"+file_name;
                        downloadFileByUrl(plugin_title, file_url, file_path, file_name, commandSender);
                        enable_plugin(file_path, plugin_title, commandSender);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        if (commandSender instanceof Player) {
                            messages.getString("error_install_p");
                        }
                        else {
                            messages.getString("error_install_c");
                        }
                    }
                }
            }
            else {
                try {
                    String new_url = "https://api.modrinth.com/v2/project/{id|slug}/version?";
                    String new_real_uri = new_url.replace("{id|slug}",project_id);
                    String new_args = "\"featured\"=\""+featured+"\"&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                    String new_real_url = argument_handler(new_real_uri, new_args);
                    String new_result = sendGet(new_real_url);
                    String fixed_new_result = convert_list_string_to_json_string(new_result);
                    String files = decoder.decode_json(fixed_new_result, "files");
                    String fixed_files = convert_list_string_to_json_string(files);
                    String file_url = decoder.decode_json(fixed_files, "url");
                    String file_name = decoder.decode_json(fixed_files, "filename");
                    String file_path = Bukkit.getPluginsFolder().getAbsolutePath()+"\\"+file_name;
                    downloadFileByUrl(plugin_title, file_url, file_path, file_name, commandSender);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    if (commandSender instanceof Player) {
                        messages.getString("error_install_p");
                    }
                    else {
                        messages.getString("error_install_c");
                    }
                }
            }
        }
        else if (Objects.equals(download_source, "both")) {
            try {
                commandSender.sendMessage(prefix+find_plugin.replace("[name]", project_id).replace("[source]", "papermc"));
                int limit = 10;
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"limit="+limit);
                }
                String version = Bukkit.getServer().getMinecraftVersion();
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"version="+version);
                }
                String url = "https://hangar.papermc.io/api/v1/projects/{id|slug}?limit="+limit+"&version="+version;
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"url="+url);
                }
                String real_uri = url.replace("{id|slug}", project_id);
                String result = sendGet(real_uri);
                String plugin_title = decoder.decode_json(result, "name");
                if (!skipable) {
                    if (Bukkit.getPluginManager().getPlugin(plugin_title) != null) {
                        commandSender.sendMessage(prefix+messages.getString("no_duplicate"));
                    }
                    else {
                        try {
                            String new_url = "https://hangar.papermc.io/api/v1/projects/{id|slug}/versions?limit="+limit;
                            String new_real_uri = new_url.replace("{id|slug}",project_id);
                            String new_result = sendGet(new_real_uri);
                            String new_fixed_result = convert_list_string_to_json_string(decoder.decode_json(new_result, "result"));
                            String files = decoder.decode_json(new_fixed_result, "downloads");
                            String platform = "PAPER";
                            String fixed_file = decoder.decode_json(files, platform);
                            String file_url = decoder.decode_json(fixed_file, "downloadUrl");
                            String file_info = decoder.decode_json(fixed_file, "fileInfo");
                            String file_name = decoder.decode_json(file_info, "name");
                            String file_path = Bukkit.getPluginsFolder().getAbsolutePath()+"\\"+file_name;
                            downloadFileByUrl(plugin_title, file_url, file_path, file_name, commandSender);
                            enable_plugin(file_path, plugin_title, commandSender);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            if (commandSender instanceof Player) {
                                messages.getString("error_install_p");
                            }
                            else {
                                messages.getString("error_install_c");
                            }
                        }
                    }
                }
                else {
                    try {
                        String new_url = "https://hangar.papermc.io/api/v1/projects/{id|slug}/versions?limit="+limit;
                        String new_real_uri = new_url.replace("{id|slug}",project_id);
                        String new_result = sendGet(new_real_uri);
                        String new_fixed_result = convert_list_string_to_json_string(decoder.decode_json(new_result, "result"));
                        String files = decoder.decode_json(new_fixed_result, "downloads");
                        String platform = "PAPER";
                        String fixed_file = decoder.decode_json(files, platform);
                        String file_url = decoder.decode_json(fixed_file, "downloadUrl");
                        String file_info = decoder.decode_json(fixed_file, "fileInfo");
                        String file_name = decoder.decode_json(file_info, "name");
                        String file_path = Bukkit.getPluginsFolder().getAbsolutePath()+"\\"+file_name;
                        downloadFileByUrl(plugin_title, file_url, file_path, file_name, commandSender);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        if (commandSender instanceof Player) {
                            messages.getString("error_install_p");
                        }
                        else {
                            messages.getString("error_install_c");
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                if (commandSender instanceof Player) {
                    commandSender.sendMessage(prefix+messages.getString("error_papermc_p"));
                }
                else {
                    commandSender.sendMessage(messages.getString("error_papermc_c"));
                }
                commandSender.sendMessage(prefix+messages.getString("switch_to_source").replace("[source]", "modrinth"));
                commandSender.sendMessage(prefix+find_plugin.replace("[name]", project_id).replace("[source]", "modrinth"));
                boolean featured = true;
                String loader = "paper";
                String game_version = Bukkit.getMinecraftVersion();
                String url = "https://api.modrinth.com/v2/project/{id|slug}?featured="+featured;
                String real_uri = url.replace("{id|slug}",project_id);
                String args = "&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                String real_url = argument_handler(real_uri, args);
                String result = sendGet(real_url);
                String plugin_title = decoder.decode_json(result, "title");
                if (!skipable) {
                    if (Bukkit.getPluginManager().getPlugin(plugin_title) != null) {
                        commandSender.sendMessage(prefix+messages.getString("no_duplicate"));
                    }
                    else {
                        try {
                            String new_url = "https://api.modrinth.com/v2/project/{id|slug}/version?";
                            String new_real_uri = new_url.replace("{id|slug}",project_id);
                            String new_args = "\"featured\"=\""+featured+"\"&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                            String new_real_url = argument_handler(new_real_uri, new_args);
                            String new_result = sendGet(new_real_url);
                            String fixed_new_result = convert_list_string_to_json_string(new_result);
                            String files = decoder.decode_json(fixed_new_result, "files");
                            String fixed_files = convert_list_string_to_json_string(files);
                            String file_url = decoder.decode_json(fixed_files, "url");
                            String file_name = decoder.decode_json(fixed_files, "filename");
                            String file_path = Bukkit.getPluginsFolder().getAbsolutePath()+"\\"+file_name;
                            downloadFileByUrl(plugin_title, file_url, file_path, file_name, commandSender);
                            enable_plugin(file_path, plugin_title, commandSender);
                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                            if (commandSender instanceof Player) {
                                messages.getString("error_install_p");
                            }
                            else {
                                messages.getString("error_install_c");
                            }
                        }
                    }
                }
                else {
                    try {
                        String new_url = "https://api.modrinth.com/v2/project/{id|slug}/version?";
                        String new_real_uri = new_url.replace("{id|slug}",project_id);
                        String new_args = "\"featured\"=\""+featured+"\"&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                        String new_real_url = argument_handler(new_real_uri, new_args);
                        String new_result = sendGet(new_real_url);
                        String fixed_new_result = convert_list_string_to_json_string(new_result);
                        String files = decoder.decode_json(fixed_new_result, "files");
                        String fixed_files = convert_list_string_to_json_string(files);
                        String file_url = decoder.decode_json(fixed_files, "url");
                        String file_name = decoder.decode_json(fixed_files, "filename");
                        String file_path = Bukkit.getPluginsFolder().getAbsolutePath()+"\\"+file_name;
                        downloadFileByUrl(plugin_title, file_url, file_path, file_name, commandSender);
                    }
                    catch (Exception ea) {
                        ea.printStackTrace();
                        if (commandSender instanceof Player) {
                            messages.getString("error_install_p");
                        }
                        else {
                            messages.getString("error_install_c");
                        }
                    }
                }
            }
        }
        else {
            commandSender.sendMessage(prefix+"Download source is invalid!");
        }
    }

    public static void enable_plugin(String file_path, String plugin_title, CommandSender commandSender) throws InvalidPluginException, InvalidDescriptionException {
        refresh_download_source();
        commandSender.sendMessage(prefix+messages.getString("load_plugin").replace("[name]", plugin_title));
        Bukkit.getPluginManager().loadPlugin(new File(file_path));
        commandSender.sendMessage(prefix+messages.getString("plugin_loaded").replace("[name]", plugin_title));
        commandSender.sendMessage(prefix+messages.getString("enable_plugin").replace("[name]", plugin_title));
        Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(plugin_title));
        commandSender.sendMessage(prefix+messages.getString("install_complete").replace("[name]", plugin_title));
    }

    public static void get_projects_by_title(CommandSender commandSender, String title) throws IOException {
        if (IDK.idk.debug) {
            if (commandSender instanceof Player) {
                commandSender.sendMessage(prefix+messages.getString("debug_p"));
            } else {
                commandSender.sendMessage(prefix+messages.getString("debug_c"));
            }
            commandSender.sendMessage(prefix+"title="+title);
        }
        refresh_download_source();
        String get_plugin_info = messages.getString("get_plugin_info");
        if (Objects.equals(download_source, "papermc")) {
            try {
                commandSender.sendMessage(prefix+get_plugin_info.replace("[name]", title).replace("[source]", download_source));
                int limit = 10;
                if (IDK.idk.debug) {
                    commandSender.sendMessage("limit=" + limit);
                }
                String url = "https://hangar.papermc.io/api/v1/projects/{id/slug}?limit=" + limit;
                if (IDK.idk.debug) {
                    commandSender.sendMessage("url=" + url);
                }
                String real_uri = url.replace("{id/slug}", title);
                if (IDK.idk.debug) {
                    commandSender.sendMessage("real_uri=" + real_uri);
                }
                String result = sendGet(real_uri);
                if (IDK.idk.debug) {
                    commandSender.sendMessage("result="+result);
                }
                String plugin_title = decoder.decode_json(result, "title");
                String plugin_description = decoder.decode_json(result, "description");
                List<String> search_plugin_list = messages.getStringList("search_plugin_list");
                Object[] search_plugin_list_fix = search_plugin_list.toArray();
                StringBuilder fixed = new StringBuilder();
                for(int b = 0; b < search_plugin_list_fix.length; b++) {
                    fixed.append(search_plugin_list_fix[b].toString());
                    if (b != search_plugin_list_fix.length - 1) {
                        fixed.append("\n");
                    }
                }
                commandSender.sendMessage(prefix+fixed.toString().replace("[number]", String.valueOf(1)).replace("[name]", plugin_title).replace("[description]", plugin_description));
                tell_raw(convert_to_minecraft_clickable_string(plugin_title), commandSender);
            }
            catch (Exception e) {
                e.printStackTrace();
                if (commandSender instanceof Player) {
                    messages.getString("error_install_p");
                }
                else {
                    messages.getString("error_install_c");
                }
            }
        }
        else if (Objects.equals(download_source, "modrinth")) {
            commandSender.sendMessage(prefix+get_plugin_info.replace("[name]", title).replace("[source]", download_source));
            int limit = 10;
            if (IDK.idk.debug) {
                 commandSender.sendMessage(prefix+"limit=" + limit);
             }
            String url = "https://api.modrinth.com/v2/search?query=" + title + "&limit=" + limit + "&facets=";
            if (IDK.idk.debug) {
                 commandSender.sendMessage(prefix+"url=" + url);
             }
            String categories = "paper";
            if (IDK.idk.debug) {
                 commandSender.sendMessage(prefix+"categories=" + categories);
             }
            String versions = Bukkit.getServer().getMinecraftVersion();
            if (IDK.idk.debug) {
                 commandSender.sendMessage(prefix+"versions=" + versions);
             }
            String arg = "[[\"categories:" + categories + "\"],[\"versions:" + versions + "\"],[\"project_type:plugin\"]]";
            if (IDK.idk.debug) {
                 commandSender.sendMessage(prefix+"arg=" + arg);
             }
            String real_uri = argument_handler(url, arg);
            if (IDK.idk.debug) {
                 commandSender.sendMessage(prefix+"real_uri=" + real_uri);
             }
            String result = sendGet(real_uri);
            int total_hits = decoder.get_total_hits(result);
            if (IDK.idk.debug) {
                 commandSender.sendMessage(prefix+"total_hits=" + total_hits);
             }
            if (total_hits > limit) {
                 for (int i = 0; i < limit; i++) {
                     String plugin_title = decoder.decode_json(result, i, "hits", "title");
                     String plugin_description = decoder.decode_json(result, i, "hits", "description");
                     String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                     int a = i + 1;
                     List<String> search_plugin_list = messages.getStringList("search_plugin_list_2");
                     Object[] search_plugin_list_fix = search_plugin_list.toArray();
                     StringBuilder fixed = new StringBuilder();
                     for(int b = 0; b < search_plugin_list_fix.length; b++) {
                         fixed.append(search_plugin_list_fix[b].toString());
                         if (b != search_plugin_list_fix.length - 1) {
                             fixed.append("\n");
                         }
                     }
                     commandSender.sendMessage(prefix+fixed.toString().replace("[number]", String.valueOf(a)).replace("[name]", plugin_title).replace("[id]", plugin_id).replace("[description]", plugin_description));
                     tell_raw(convert_to_minecraft_clickable_string(plugin_title), commandSender);
                 }
             }
            else if (total_hits > 0) {
                 for (int i = 0; i < total_hits; i++) {
                     String plugin_title = decoder.decode_json(result, i, "hits", "title");
                     String plugin_description = decoder.decode_json(result, i, "hits", "description");
                     String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                     int a = i + 1;
                     List<String> search_plugin_list = messages.getStringList("search_plugin_list_2");
                     Object[] search_plugin_list_fix = search_plugin_list.toArray();
                     StringBuilder fixed = new StringBuilder();
                     for(int b = 0; b < search_plugin_list_fix.length; b++) {
                         fixed.append(search_plugin_list_fix[b].toString());
                         if (b != search_plugin_list_fix.length - 1) {
                             fixed.append("\n");
                         }
                     }
                     commandSender.sendMessage(prefix+fixed.toString().replace("[number]", String.valueOf(a)).replace("[name]", plugin_title).replace("[id]", plugin_id).replace("[description]", plugin_description));
                     tell_raw(convert_to_minecraft_clickable_string(plugin_title), commandSender);
                 }
             }
            else {
                 if (commandSender instanceof Player) {
                     messages.getString("error_install_p");
                 }
                 else {
                     messages.getString("error_install_c");
                 }
             }
        }
        else if (Objects.equals(download_source, "both")) {
             try {
                 try {
                     commandSender.sendMessage(prefix+get_plugin_info.replace("[name]", title).replace("[source]", "papermc"));
                     int limit = 10;
                     if (IDK.idk.debug) {
                         commandSender.sendMessage(prefix+"limit=" + limit);
                     }
                     String url = "https://hangar.papermc.io/api/v1/projects/{id/slug}?limit=" + limit;
                     if (IDK.idk.debug) {
                         commandSender.sendMessage(prefix+"url=" + url);
                     }
                     String real_uri = url.replace("{id/slug}", title);
                     if (IDK.idk.debug) {
                         commandSender.sendMessage(prefix+"real_uri=" + real_uri);
                     }
                     String result = sendGet(real_uri);
                     if (IDK.idk.debug) {
                         commandSender.sendMessage(prefix+"result="+result);
                     }
                     String plugin_title = decoder.decode_json(result, "title");
                     String plugin_description = decoder.decode_json(result, "description");
                     List<String> search_plugin_list = messages.getStringList("search_plugin_list");
                     Object[] search_plugin_list_fix = search_plugin_list.toArray();
                     StringBuilder fixed = new StringBuilder();
                     for(int b = 0; b < search_plugin_list_fix.length; b++) {
                         fixed.append(search_plugin_list_fix[b].toString());
                         if (b != search_plugin_list_fix.length - 1) {
                             fixed.append("\n");
                         }
                     }
                     commandSender.sendMessage(prefix+fixed.toString().replace("[number]", String.valueOf(1)).replace("[name]", plugin_title).replace("[description]", plugin_description));
                     tell_raw(convert_to_minecraft_clickable_string(plugin_title), commandSender);
                 }
                 catch (Exception e) {
                     e.printStackTrace();
                     if (commandSender instanceof Player) {
                         messages.getString("error_install_p");
                     }
                     else {
                         messages.getString("error_install_c");
                     }
                 }
             }
             catch (Exception e) {
                 e.printStackTrace();
                 if (commandSender instanceof Player) {
                     commandSender.sendMessage(prefix+messages.getString("error_papermc_p"));
                 }
                 else {
                     commandSender.sendMessage(messages.getString("error_papermc_c"));
                 }
                 commandSender.sendMessage(prefix+messages.getString("switch_to_source").replace("[source]", "modrinth"));
                 commandSender.sendMessage(prefix+get_plugin_info.replace("[name]", title).replace("[source]", "modrinth"));
                 int limit = 10;
                 if (IDK.idk.debug) {
                     commandSender.sendMessage(prefix+"limit=" + limit);
                 }
                 String url = "https://api.modrinth.com/v2/search?query=" + title + "&limit=" + limit + "&facets=";
                 if (IDK.idk.debug) {
                     commandSender.sendMessage(prefix+"url=" + url);
                 }
                 String categories = "paper";
                 if (IDK.idk.debug) {
                     commandSender.sendMessage(prefix+"categories=" + categories);
                 }
                 String versions = Bukkit.getServer().getMinecraftVersion();
                 if (IDK.idk.debug) {
                     commandSender.sendMessage(prefix+"versions=" + versions);
                 }
                 String arg = "[[\"categories:" + categories + "\"],[\"versions:" + versions + "\"],[\"project_type:plugin\"]]";
                 if (IDK.idk.debug) {
                     commandSender.sendMessage(prefix+"arg=" + arg);
                 }
                 String real_uri = argument_handler(url, arg);
                 if (IDK.idk.debug) {
                     commandSender.sendMessage(prefix+"real_uri=" + real_uri);
                 }
                 String result = sendGet(real_uri);
                 int total_hits = decoder.get_total_hits(result);
                 if (IDK.idk.debug) {
                     commandSender.sendMessage(prefix+"total_hits=" + total_hits);
                 }
                 if (total_hits > limit) {
                     for (int i = 0; i < limit; i++) {
                         String plugin_title = decoder.decode_json(result, i, "hits", "title");
                         String plugin_description = decoder.decode_json(result, i, "hits", "description");
                         String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                         int a = i + 1;
                         List<String> search_plugin_list = messages.getStringList("search_plugin_list_2");
                         Object[] search_plugin_list_fix = search_plugin_list.toArray();
                         StringBuilder fixed = new StringBuilder();
                         for(int b = 0; b < search_plugin_list_fix.length; b++) {
                             fixed.append(search_plugin_list_fix[b].toString());
                             if (b != search_plugin_list_fix.length - 1) {
                                 fixed.append("\n");
                             }
                         }
                         commandSender.sendMessage(prefix+fixed.toString().replace("[number]", String.valueOf(a)).replace("[name]", plugin_title).replace("[id]", plugin_id).replace("[description]", plugin_description));
                         tell_raw(convert_to_minecraft_clickable_string(plugin_title), commandSender);
                     }
                 }
                 else if (total_hits > 0) {
                     for (int i = 0; i < total_hits; i++) {
                         String plugin_title = decoder.decode_json(result, i, "hits", "title");
                         String plugin_description = decoder.decode_json(result, i, "hits", "description");
                         String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                         int a = i + 1;
                         List<String> search_plugin_list = messages.getStringList("search_plugin_list_2");
                         Object[] search_plugin_list_fix = search_plugin_list.toArray();
                         StringBuilder fixed = new StringBuilder();
                         for(int b = 0; b < search_plugin_list_fix.length; b++) {
                             fixed.append(search_plugin_list_fix[b].toString());
                             if (b != search_plugin_list_fix.length - 1) {
                                 fixed.append("\n");
                             }
                         }
                         commandSender.sendMessage(prefix+fixed.toString().replace("[number]", String.valueOf(a)).replace("[name]", plugin_title).replace("[id]", plugin_id).replace("[description]", plugin_description));
                         tell_raw(convert_to_minecraft_clickable_string(plugin_title), commandSender);
                     }
                 }
                 else {
                     if (commandSender instanceof Player) {
                         messages.getString("error_install_p");
                     }
                     else {
                         messages.getString("error_install_c");
                     }
                 }
             }
        }
        else {
            commandSender.sendMessage(prefix+messages.getString("error_download_source"));
        }
    }

    public static String update_plugins(CommandSender commandSender) throws IOException {
        if (IDK.idk.debug) {
            if (commandSender instanceof Player) {
                commandSender.sendMessage(prefix+messages.getString("debug_p"));
            } else {
                commandSender.sendMessage(prefix+messages.getString("debug_c"));
            }
        }
        refresh_download_source();
        String find_plugin = messages.getString("find_plugin");
        String update_plugin = messages.getString("update_plugin");
        String no_need_to_update = messages.getString("no_need_to_update");
        String all_plugins_are_new = messages.getString("all_plugins_are_new");
        String update_complete = messages.getString("update_complete");
        int counter = 0;
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        int max = plugins.length;
        if (Objects.equals(download_source, "papermc")) {
            commandSender.sendMessage(prefix+update_plugin.replace("[source]", download_source));
            for (int i = 0; i < plugins.length; i++) {
                String project_id = plugins[i].getPluginMeta().getName();
                if (Objects.equals(project_id, "IDK")) {
                    continue;
                }
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"project_id="+project_id);
                }
                commandSender.sendMessage(prefix+find_plugin.replace("[name]", project_id).replace("[source]", download_source));
                int limit = 10;
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"limit="+limit);
                }
                String version = Bukkit.getServer().getMinecraftVersion();
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"version="+version);
                }
                String url = "https://hangar.papermc.io/api/v1/projects/{id|slug}?limit="+limit+"&version="+version;
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"url="+url);
                }
                String real_uri = url.replace("{id|slug}", project_id);
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"real_uri="+real_uri);
                }
                String result = sendGet(real_uri);
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"result="+result);
                }
                String plugin_title = decoder.decode_json(result, "name");
                if (Bukkit.getPluginManager().getPlugin(project_id) == null) {
                    if (commandSender instanceof Player) {
                        commandSender.sendMessage(prefix+messages.getString("error_install_p"));
                    }
                    else {
                        commandSender.sendMessage(prefix+messages.getString("error_install_c"));
                    }
                    return null;
                }
                else if (decoder.decode_json(result, "message") != null && Objects.equals(decoder.decode_json(result, "message"), "Not Found")) {}
                else {
                    try {
                        PluginMeta plugin_meta = Bukkit.getPluginManager().getPlugin(plugin_title).getPluginMeta();
                        String plugin_ver = plugin_meta.getVersion();
                        String new_url = "https://hangar.papermc.io/api/v1/projects/{id|slug}/versions?limit="+limit;
                        String new_real_uri = new_url.replace("{id|slug}",project_id);
                        String new_result = sendGet(new_real_uri);
                        String new_fixed_result = convert_list_string_to_json_string(decoder.decode_json(new_result, "result"));
                        String ver = decoder.decode_json(new_fixed_result, "name");
                        String fix_plugin_ver = plugin_ver.replaceAll("[^0-9.]+", "");
                        String fix_ver = ver.replaceAll("[^0-9.]+", "");
                        if(IDK.idk.debug) {
                            commandSender.sendMessage(prefix+"plugin_ver="+plugin_ver);
                            commandSender.sendMessage(prefix+"fix_plugin_ver="+fix_plugin_ver);
                            commandSender.sendMessage(prefix+"ver="+ver);
                            commandSender.sendMessage(prefix+"fix_ver="+fix_ver);
                            commandSender.sendMessage(prefix+"plugin_title==\"viaversion\" =" + (plugin_title.equals("viaversion")));
                            commandSender.sendMessage(prefix+"fix_plugin_ver==fix_ver =" + (fix_plugin_ver.equals(fix_ver)));
                        }
                        if (plugin_title.equals("viaversion")) {
                            fix_ver = fix_ver.substring(0, fix_ver.length() - 3);
                        }
                        if (fix_plugin_ver.equals(fix_ver)) {
                            commandSender.sendMessage(prefix+no_need_to_update.replace("[plugin]", plugin_title));
                            counter++;
                        }
                        else {
                            commandSender.sendMessage(prefix+"Start updating plugin" + plugin_title + ".");
                            IDKPluginManagement idkpm = new IDKPluginManagement();
                            idkpm.delete_plugin(plugin_title, commandSender, true);
                            install_project(commandSender, plugin_title, true);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        if (commandSender instanceof Player) {
                            messages.getString("error_install_p");
                        }
                        else {
                            messages.getString("error_install_c");
                        }
                        return null;
                    }
                }
            }
            if (counter == max) {
                return all_plugins_are_new;
            }
            else {
                return update_complete;
            }
        }
        else if (Objects.equals(download_source, "modrinth")) {
            commandSender.sendMessage(prefix+update_plugin.replace("[source]", download_source));
            for (int i = 0; i < plugins.length; i++) {
                String project_id = plugins[i].getPluginMeta().getName();
                if (Objects.equals(project_id, "IDK")) {
                    continue;
                }
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"project_id="+project_id);
                }
                commandSender.sendMessage(prefix+find_plugin.replace("[name]", project_id).replace("[source]", download_source));
                boolean featured = true;
                String loader = "paper";
                String game_version = Bukkit.getMinecraftVersion();
                String url = "https://api.modrinth.com/v2/project/{id|slug}?featured="+featured;
                String real_uri = url.replace("{id|slug}",project_id);
                String args = "&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                String real_url = argument_handler(real_uri, args);
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"featured="+featured);
                    commandSender.sendMessage(prefix+"loader="+loader);
                    commandSender.sendMessage(prefix+"game_version="+game_version);
                    commandSender.sendMessage(prefix+"url="+url);
                    commandSender.sendMessage(prefix+"real_uri="+real_uri);
                    commandSender.sendMessage(prefix+"args="+args);
                    commandSender.sendMessage(prefix+"real_url="+real_url);
                }
                String result = sendGet(real_url);
                String plugin_title = decoder.decode_json(result, "title");
                if (IDK.idk.debug) {
                    commandSender.sendMessage(prefix+"plugin_title="+plugin_title);
                }
                if (Bukkit.getPluginManager().getPlugin(project_id) == null) {
                    if (commandSender instanceof Player) {
                        commandSender.sendMessage(prefix+messages.getString("error_install_p"));
                    }
                    else {
                        commandSender.sendMessage(prefix+messages.getString("error_install_c"));
                    }
                    return null;
                }
                else if (decoder.decode_json(result, "message") != null && Objects.equals(decoder.decode_json(result, "message"), "Not Found")) {}
                else {
                    try {
                        PluginMeta plugin_meta = Bukkit.getPluginManager().getPlugin(plugin_title).getPluginMeta();
                        String plugin_ver = plugin_meta.getVersion();
                        String new_url = "https://api.modrinth.com/v2/project/{id|slug}/version?";
                        String new_real_uri = new_url.replace("{id|slug}",project_id);
                        String new_args = "\"featured\"=\""+featured+"\"&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                        String new_real_url = argument_handler(new_real_uri, new_args);
                        if (IDK.idk.debug) {
                            commandSender.sendMessage(prefix+"plugin_ver="+plugin_ver);
                            commandSender.sendMessage(prefix+"new_url="+new_url);
                            commandSender.sendMessage(prefix+"new_real_uri="+new_real_uri);
                            commandSender.sendMessage(prefix+"new_args="+new_args);
                            commandSender.sendMessage(prefix+"new_real_url="+new_real_url);
                        }
                        String new_result = sendGet(new_real_url);
                        String fixed_new_result = convert_list_string_to_json_string(new_result);
                        String ver = decoder.decode_json(fixed_new_result, "version_number");
                        String fix_plugin_ver = plugin_ver.replaceAll("[^0-9.]+", "");
                        String fix_ver = ver.replaceAll("[^0-9.]+", "");
                        if (plugin_title.equals("viaversion")) {
                            fix_ver = fix_ver.substring(0, fix_ver.length() - 3);
                        }
                        if (fix_plugin_ver.equals(fix_ver)) {
                            commandSender.sendMessage(prefix+no_need_to_update.replace("[plugin]", plugin_title));
                            counter++;
                        }
                        else {
                            commandSender.sendMessage(prefix+"Start updating plugin" + plugin_title + ".");
                            IDKPluginManagement idkpm = new IDKPluginManagement();
                            idkpm.delete_plugin(plugin_title, commandSender, true);
                            install_project(commandSender, plugin_title, true);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        if (commandSender instanceof Player) {
                            messages.getString("error_install_p");
                        }
                        else {
                            messages.getString("error_install_c");
                        }
                    }
                }
            }
            if (counter == max) {
                return all_plugins_are_new;
            }
            else {
                return update_complete;
            }
        }
        else if (Objects.equals(download_source, "both")) {
            try {
                commandSender.sendMessage(prefix+update_plugin.replace("[source]", "papermc"));
                for (int i = 0; i < plugins.length; i++) {
                    String project_id = plugins[i].getPluginMeta().getName();
                    if (Objects.equals(project_id, "IDK")) {
                        continue;
                    }
                    if (IDK.idk.debug) {
                        commandSender.sendMessage(prefix+"project_id="+project_id);
                    }
                    commandSender.sendMessage(prefix+find_plugin.replace("[name]", project_id).replace("[source]", download_source));
                    int limit = 10;
                    if (IDK.idk.debug) {
                        commandSender.sendMessage(prefix+"limit="+limit);
                    }
                    String version = Bukkit.getServer().getMinecraftVersion();
                    if (IDK.idk.debug) {
                        commandSender.sendMessage(prefix+"version="+version);
                    }
                    String url = "https://hangar.papermc.io/api/v1/projects/{id|slug}?limit="+limit+"&version="+version;
                    if (IDK.idk.debug) {
                        commandSender.sendMessage(prefix+"url="+url);
                    }
                    String real_uri = url.replace("{id|slug}", project_id);
                    if (IDK.idk.debug) {
                        commandSender.sendMessage(prefix+"real_uri="+real_uri);
                    }
                    String result = sendGet(real_uri);
                    if (IDK.idk.debug) {
                        commandSender.sendMessage(prefix+"result="+result);
                    }
                    String plugin_title = decoder.decode_json(result, "name");
                    if (Bukkit.getPluginManager().getPlugin(project_id) == null) {
                        if (commandSender instanceof Player) {
                            commandSender.sendMessage(prefix+messages.getString("error_install_p"));
                        }
                        else {
                            commandSender.sendMessage(prefix+messages.getString("error_install_c"));
                        }
                        return null;
                    }
                    else if (decoder.decode_json(result, "message") != null && Objects.equals(decoder.decode_json(result, "message"), "Not Found")) {}
                    else {
                        try {
                            PluginMeta plugin_meta = Bukkit.getPluginManager().getPlugin(plugin_title).getPluginMeta();
                            String plugin_ver = plugin_meta.getVersion();
                            String new_url = "https://hangar.papermc.io/api/v1/projects/{id|slug}/versions?limit="+limit;
                            String new_real_uri = new_url.replace("{id|slug}",project_id);
                            String new_result = sendGet(new_real_uri);
                            String new_fixed_result = convert_list_string_to_json_string(decoder.decode_json(new_result, "result"));
                            String ver = decoder.decode_json(new_fixed_result, "name");
                            String fix_plugin_ver = plugin_ver.replaceAll("[^0-9.]+", "");
                            String fix_ver = ver.replaceAll("[^0-9.]+", "");
                            if(IDK.idk.debug) {
                                commandSender.sendMessage(prefix+"plugin_ver="+plugin_ver);
                                commandSender.sendMessage(prefix+"fix_plugin_ver="+fix_plugin_ver);
                                commandSender.sendMessage(prefix+"ver="+ver);
                                commandSender.sendMessage(prefix+"fix_ver="+fix_ver);
                                commandSender.sendMessage(prefix+"plugin_title==\"viaversion\" =" + (plugin_title.equals("viaversion")));
                                commandSender.sendMessage(prefix+"fix_plugin_ver==fix_ver =" + (fix_plugin_ver.equals(fix_ver)));
                            }
                            if (plugin_title.equals("viaversion")) {
                                fix_ver = fix_ver.substring(0, fix_ver.length() - 3);
                            }
                            if (fix_plugin_ver.equals(fix_ver)) {
                                commandSender.sendMessage(prefix+no_need_to_update.replace("[plugin]", plugin_title));
                                counter++;
                            }
                            else {
                                commandSender.sendMessage(prefix+"Start updating plugin" + plugin_title + ".");
                                IDKPluginManagement idkpm = new IDKPluginManagement();
                                idkpm.delete_plugin(plugin_title, commandSender, true);
                                install_project(commandSender, plugin_title, true);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            if (commandSender instanceof Player) {
                                messages.getString("error_install_p");
                            }
                            else {
                                messages.getString("error_install_c");
                            }
                            return null;
                        }
                    }
                }
                if (counter == max) {
                    return all_plugins_are_new;
                }
                else {
                    return update_complete;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                commandSender.sendMessage(prefix+messages.getString("error_get_update").replace("[source]", "papermc"));
                commandSender.sendMessage(prefix+messages.getString("switch_to_source").replace("[source]", "modrinth"));
                commandSender.sendMessage(prefix+update_plugin.replace("[source]", "modrinth"));
                for (int i = 0; i < plugins.length; i++) {
                    String project_id = plugins[i].getPluginMeta().getName();
                    if (Objects.equals(project_id, "IDK")) {
                        continue;
                    }
                    if (IDK.idk.debug) {
                        commandSender.sendMessage(prefix+"project_id="+project_id);
                    }
                    commandSender.sendMessage(prefix+find_plugin.replace("[name]", project_id).replace("[source]", download_source));
                    boolean featured = true;
                    String loader = "paper";
                    String game_version = Bukkit.getMinecraftVersion();
                    String url = "https://api.modrinth.com/v2/project/{id|slug}?featured="+featured;
                    String real_uri = url.replace("{id|slug}",project_id);
                    String args = "&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                    String real_url = argument_handler(real_uri, args);
                    if (IDK.idk.debug) {
                        commandSender.sendMessage(prefix+"featured="+featured);
                        commandSender.sendMessage(prefix+"loader="+loader);
                        commandSender.sendMessage(prefix+"game_version="+game_version);
                        commandSender.sendMessage(prefix+"url="+url);
                        commandSender.sendMessage(prefix+"real_uri="+real_uri);
                        commandSender.sendMessage(prefix+"args="+args);
                        commandSender.sendMessage(prefix+"real_url="+real_url);
                    }
                    String result = sendGet(real_url);
                    String plugin_title = decoder.decode_json(result, "title");
                    if (IDK.idk.debug) {
                        commandSender.sendMessage(prefix+"plugin_title="+plugin_title);
                    }
                    if (Bukkit.getPluginManager().getPlugin(project_id) == null) {
                        if (commandSender instanceof Player) {
                            commandSender.sendMessage(prefix+messages.getString("error_install_p"));
                        }
                        else {
                            commandSender.sendMessage(prefix+messages.getString("error_install_c"));
                        }
                        return null;
                    }
                    else if (decoder.decode_json(result, "message") != null && Objects.equals(decoder.decode_json(result, "message"), "Not Found")) {}
                    else {
                        try {
                            PluginMeta plugin_meta = Bukkit.getPluginManager().getPlugin(plugin_title).getPluginMeta();
                            String plugin_ver = plugin_meta.getVersion();
                            String new_url = "https://api.modrinth.com/v2/project/{id|slug}/version?";
                            String new_real_uri = new_url.replace("{id|slug}",project_id);
                            String new_args = "\"featured\"=\""+featured+"\"&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                            String new_real_url = argument_handler(new_real_uri, new_args);
                            if (IDK.idk.debug) {
                                commandSender.sendMessage(prefix+"plugin_ver="+plugin_ver);
                                commandSender.sendMessage(prefix+"new_url="+new_url);
                                commandSender.sendMessage(prefix+"new_real_uri="+new_real_uri);
                                commandSender.sendMessage(prefix+"new_args="+new_args);
                                commandSender.sendMessage(prefix+"new_real_url="+new_real_url);
                            }
                            String new_result = sendGet(new_real_url);
                            String fixed_new_result = convert_list_string_to_json_string(new_result);
                            String ver = decoder.decode_json(fixed_new_result, "version_number");
                            String fix_plugin_ver = plugin_ver.replaceAll("[^0-9.]+", "");
                            String fix_ver = ver.replaceAll("[^0-9.]+", "");
                            if (plugin_title.equals("viaversion")) {
                                fix_ver = fix_ver.substring(0, fix_ver.length() - 3);
                            }
                            if (fix_plugin_ver.equals(fix_ver)) {
                                commandSender.sendMessage(prefix+no_need_to_update.replace("[plugin]", plugin_title));
                                counter++;
                            }
                            else {
                                commandSender.sendMessage(prefix+"Start updating plugin" + plugin_title + ".");
                                IDKPluginManagement idkpm = new IDKPluginManagement();
                                idkpm.delete_plugin(plugin_title, commandSender, true);
                                install_project(commandSender, plugin_title, true);
                            }
                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                            if (commandSender instanceof Player) {
                                messages.getString("error_install_p");
                            }
                            else {
                                messages.getString("error_install_c");
                            }
                        }
                    }
                }
                if (counter == max) {
                    return all_plugins_are_new;
                }
                else {
                    return update_complete;
                }
            }
        }
        else {
            commandSender.sendMessage(prefix+"Download source is invalid!");
        }
        return null;
    }

    public static void tell_raw(String string, CommandSender commandSender) {
        if(commandSender instanceof Player) {
            ((Player) commandSender).performCommand("tellraw @s "+string);
        }
    }

    public static String convert_to_minecraft_clickable_string(String plugin_id) {
        // /tellraw @s
        // {"text":"Install this plugin by click ",
        // "extra":[{"clickEvent":{"action":"run_command","value":"/kill"},
        // "hoverEvent":{"action":"show_text","contents":"Click here to install"},
        // "text":"[install]","color":"blue"},
        // {"text":" or type /kill"}]}
        String notice1 = messages.getString("install_plugin_notice1");
        String notice2 = messages.getString("install_plugin_notice2").replace("[plugin_id]", plugin_id);
        String install_button = messages.getString("install_button");
        String install_button_hover = messages.getString("install_button_hover");
        return "{\"text\":\""+notice1+" "+"\",\"extra\":[" +
                "{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/idk plugin install "+plugin_id+"\"}," +
                "\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\""+install_button_hover+"\"}," +
                "\"text\":\""+install_button+"\",\"color\":\"blue\"},{\"text\":\""+" "+notice2+"\"}]}";
    }

    public static String argument_handler(String url, String arg) throws UnsupportedEncodingException {
        String encode_arg = URLEncoder.encode(arg, "UTF-8");
        String real_uri = url+encode_arg;
        return real_uri;
    }

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    public static String sendGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        }
        catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                response.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void get_10_top_projects_a(CommandSender commandSender) throws IOException {
        new Thread(() -> {
            try {
                get_10_top_projects(commandSender);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).run();
    }

    public static boolean downloadFileByUrl(String plugin_title, String urlPath, String downloadPath, String fileName, CommandSender commandSender) throws IOException {
        try {
            commandSender.sendMessage(prefix+messages.getString("download_start").replace("%filename%", fileName).replace("%url%", urlPath));
            File file = new File(downloadPath);
            if(file.exists()) {
                if(Bukkit.getPluginManager().getPlugin(plugin_title) != null) {
                    if(Bukkit.getPluginManager().getPlugin(plugin_title).isEnabled()) {
                        return true;
                    }
                    else{
                        Plugin plugin = Bukkit.getPluginManager().getPlugin(plugin_title);
                        Bukkit.getPluginManager().enablePlugin(plugin);
                        return true;
                    }
                }
                else {
                    file.delete();
                }
            }
            URL url = new URL(urlPath);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            conn.setConnectTimeout(3 * 1000);
            FileOutputStream fos = new FileOutputStream(downloadPath);
            IDKnetHandler pbt = new IDKnetHandler((int) conn.getContentLength(), 1L, "000001");//创建进度条
            new Thread(pbt).start();//开启线程，刷新进度条
            byte[] buf = new byte[1024];
            int size = 0;
            pbt.set_env(null, true);
            while ((size = inStream.read(buf)) != -1) {//循环读取
                fos.write(buf, 0, size);
                pbt.updateProgress(size);//写完一次，更新进度条
            }
            pbt.finish();//文件读取完成，关闭进度条
            fos.flush();
            fos.close();
            commandSender.sendMessage(prefix+messages.getString("download_complete2").replace("%path%", downloadPath));
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void get_projects_by_title_a(CommandSender commandSender, String title) throws IOException {
        new Thread(() -> {
            try {
                get_projects_by_title(commandSender, title);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).run();
    }

    public static void update_self(CommandSender commandSender) throws IOException {
        commandSender.sendMessage(prefix+messages.getString("start_update_idk"));
        String json_url = "https://idk.minecraftisbest.top/idk.json";
        if (IDK.idk.debug) {
            commandSender.sendMessage(prefix+"json_url="+json_url);
        }
        String result = sendGet(json_url).replace("\n", "").replace(" ", "");
        if (IDK.idk.debug) {
            commandSender.sendMessage(prefix+"result="+result);
        }
        String ver = decoder.decode_json(result, "ver");
        PluginMeta idkMeta = Bukkit.getPluginManager().getPlugin("IDK").getPluginMeta();
        String plugins_folder = Bukkit.getPluginsFolder().getAbsolutePath();
        String now_ver = idkMeta.getVersion();
        if (IDK.idk.debug) {
            commandSender.sendMessage(prefix+"ver="+ver);
            commandSender.sendMessage(prefix+"now_ver="+now_ver);
            commandSender.sendMessage(prefix+"plugins_folder="+plugins_folder);
            commandSender.sendMessage(prefix+"ver==now_ver = "+Objects.equals(ver, now_ver));
        }
        if (Objects.equals(ver, now_ver)) {
            commandSender.sendMessage(prefix+"You are running the latest version! No need to update!");
        }
        else {
            String url = decoder.decode_json(result, "url");
            String filename = decoder.decode_json(result, "filename");
            String new_plugin_path = plugins_folder+"\\"+filename;
            String plugin_title = idkMeta.getName();
            if (IDK.idk.debug) {
                commandSender.sendMessage(prefix+"url="+url);
                commandSender.sendMessage(prefix+"filename="+filename);
                commandSender.sendMessage(prefix+"new_plugin_path="+new_plugin_path);
                commandSender.sendMessage(prefix+"plugin_titile="+plugin_title);
            }
            Boolean download = downloadFileByUrl(plugin_title, url, new_plugin_path, filename, commandSender);
            if (download) {
                Plugin IDK = Bukkit.getPluginManager().getPlugin("IDK");
                IDKPluginManagement IDKPM = new IDKPluginManagement();
                File old_plugin = new File(IDKPM.getPluginFile(IDK).getAbsolutePath());
                if (old_plugin.exists()) {old_plugin.delete();}
                commandSender.sendMessage(prefix+messages.getString("update_idk_complete"));
            }
            else {
                if (commandSender instanceof Player) {
                    commandSender.sendMessage(prefix+messages.getString("update_idk_failed_p"));
                }
                else {
                    commandSender.sendMessage(prefix+messages.getString("update_idk_failed_c"));
                }
            }
        }
    }

    private ArrayList<Integer> proList = new ArrayList<Integer>();
    private int progress;//当前进度
    private int totalSize;//下载文法总大小
    private boolean run = true;
    private int showProgress;
    public static HashMap<String, Integer> progressMap = new HashMap<>();//各用户的下载进度
    public static HashMap<String, Boolean> executeStatusMap = new HashMap<>();//各用户是否下载中
    private Long fileId;
    private String token;
    private boolean is_a_console;
    private Player player;

    public IDKnetHandler(int totalSize, Long fileId, String token) {
        this.totalSize = totalSize;
        this.fileId = fileId;
        this.token = token;
        //创建进度条时，将指定用户的执行状态改为true
        executeStatusMap.put(token, true);
    }

    public void updateProgress(int progress) {
        synchronized (this.proList) {
            if (this.run) {
                this.proList.add(progress);
                this.proList.notify();
            }
        }
    }

    public void finish() {
        this.run = false;
        //关闭进度条
    }

    public void set_env(Player player, boolean is_a_console) {
        this.player = player;
        this.is_a_console = is_a_console;
    }

    @Override
    public void run() {
        synchronized (this.proList) {
            try {
                while (this.run) {
                    if (this.proList.size() == 0) {
                        this.proList.wait();
                    }
                    synchronized (proList) {
                        this.progress += this.proList.remove(0);
                        //更新进度条
                        showProgress = (int) ((new BigDecimal((float) this.progress / this.totalSize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) * 100);
                        progressMap.put(token + fileId, showProgress);
                        if (showProgress == 100) {
                            //进度100%时将用户的执行状态改为false
                            executeStatusMap.put(token, false);
                        }
                        if (showProgress == 25 || showProgress == 50 || showProgress == 75) {
                            if (is_a_console){
                                IDK.idk.logger.warning(prefix+messages.getString("current_progress").replace("%progress%", String.valueOf(showProgress)));
                                //System.err.println("Current Progress："+showProgress+"%");
                            } else {
                                player.sendMessage(prefix+messages.getString("current_progress").replace("%progress%", String.valueOf(showProgress)));
                                //player.sendMessage("Current Progress："+showProgress+"%");
                            }
                        }
                    }
                }
                if (is_a_console){
                    IDK.idk.logger.info(prefix+messages.getString("download_complete"));
                    //System.err.println("Download Complete!");
                } else {
                    player.sendMessage(prefix+messages.getString("download_complete"));
                    //player.sendMessage("Download Complete!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

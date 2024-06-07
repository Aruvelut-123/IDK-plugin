package krincraft.idk.network;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import krincraft.idk.JSON.decoder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class IDKnetHandler implements Runnable {
    static IDKnetHandler IDKNH;
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    public static void get_10_top_projects(Player player, boolean is_console) throws IOException {
        if (is_console) {
            System.out.println("Getting latest 10 plugins...");
            int limit = 10;
            String url = "https://api.modrinth.com/v2/search?limit="+limit+"&facets=";
            String categories = "paper";
            String versions = Bukkit.getServer().getMinecraftVersion();
            String arg = "[[\"categories:"+categories+"\"],[\"versions:"+versions+"\"],[\"project_type:plugin\"]]";
            String real_uri = argument_handler(url, arg);
            String result = sendGet(real_uri);
            System.out.println("10 Top Plugins:");
            for(int i = 0; i < 10; i++) {
                String plugin_title = decoder.decode_json(result, i, "hits", "title");
                String plugin_description = decoder.decode_json(result, i, "hits", "description");
                String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                int a = i+1;
                System.out.println(a+". Name: " + plugin_title + "\nID: " + plugin_id + "\nDescription: " + plugin_description);
            }
            System.out.println("Install a plugin by using: /idk plugin install <id>");
        } else {
            player.sendMessage("Getting latest 10 plugins...");
            int limit = 10;
            String url = "https://api.modrinth.com/v2/search?limit="+limit+"&facets=";
            String categories = "paper";
            String versions = Bukkit.getServer().getMinecraftVersion();
            String arg = "[[\"categories:"+categories+"\"],[\"versions:"+versions+"\"],[\"project_type:plugin\"]]";
            String real_uri = argument_handler(url, arg);
            String result = sendGet(real_uri);
            player.sendMessage("10 Top Plugins:");
            for(int i = 0; i < limit; i++) {
                String plugin_title = decoder.decode_json(result, i, "hits", "title");
                String plugin_description = decoder.decode_json(result, i, "hits", "description");
                String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                int a = i+1;
                player.sendMessage(a+". Name: " + plugin_title + "\nID: " + plugin_id + "\nDescription: " + plugin_description);
            }
            player.sendMessage("Install a plugin by using: /idk plugin install <id>");
        }
    }

    public static String convert_list_string_to_json_string(String list_string) {
        String fix_list_string = list_string.substring(1);
        return fix_list_string.substring(0, fix_list_string.length() - 1);
    }

    public static void install_project(Player player, boolean is_console, String project_id) throws IOException {
        if (is_console) {
            System.out.println("Start finding plugin id: " + project_id);
            int limit = 2;
            boolean featured = true;
            String loader = "paper";
            String game_version = Bukkit.getMinecraftVersion();
            String url = "https://api.modrinth.com/v2/project/{id|slug}?featured="+featured;
            String real_uri = url.replace("{id|slug}",project_id);
            String args = "&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
            String real_url = argument_handler(real_uri, args);
            String result = sendGet(real_url);
            String plugin_title = decoder.decode_json(result, "title");
            try {
                String new_url = "https://api.modrinth.com/v2/project/{id|slug}/version?";
                String new_real_uri = new_url.replace("{id|slug}",project_id);
                String new_args = "\"featured\"=\""+featured+"\"&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                String new_real_url = argument_handler(new_real_uri, new_args);
                String new_result = sendGet(new_real_url);
                String fixed_new_result = convert_list_string_to_json_string(new_result);
                String files = decoder.decode_json(fixed_new_result, "files");
                String fixed_files = convert_list_string_to_json_string(files);
                System.out.println("fixed_files="+fixed_files);
                String file_url = decoder.decode_json(fixed_files, "url");
                String file_name = decoder.decode_json(fixed_files, "filename");
                String file_path = Bukkit.getPluginsFolder().getAbsolutePath()+"\\"+file_name;
                System.out.println("file_url="+file_url);
                System.out.println("file_name="+file_name);
                System.out.println("file_path="+file_path);
                downloadFileByUrl(plugin_title, file_url, file_path, file_name, true, null);
                enable_plugin(file_path, plugin_title, true, null);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Plugin not found Or error was made!");
            }
        } else {
            player.sendMessage("Start finding plugin id: " + project_id);
            int limit = 2;
            boolean featured = true;
            String loader = "paper";
            String game_version = Bukkit.getMinecraftVersion();
            String url = "https://api.modrinth.com/v2/project/{id|slug}?featured="+featured;
            String real_uri = url.replace("{id|slug}",project_id);
            String args = "&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
            String real_url = argument_handler(real_uri, args);
            String result = sendGet(real_url);
            String plugin_title = decoder.decode_json(result, 0, "title");
            ArrayList project_categories = decoder.get_categories(result);
            try {
                int category_position = project_categories.indexOf(loader);
                if(project_categories.get(category_position) == loader) {
                    String new_url = "https://api.modrinth.com/v2/project/{id|slug}/version?featured="+featured;
                    String new_real_uri = new_url.replace("{id|slug}",project_id);
                    String new_args = "&loaders=[\""+loader+"\"]&game_version=[\""+game_version+"\"]";
                    String new_real_url = argument_handler(new_real_uri, new_args);
                    String new_result = sendGet(new_real_url);
                    String[] date_publisheds = new String[limit];
                    for(int i = 0; i < limit; i++) {
                        ArrayList plugin_loaders = decoder.get_loaders(new_result, i);
                        int loader_position = project_categories.indexOf(loader);
                        if(plugin_loaders.get(loader_position) == loader) {
                            date_publisheds[i] = decoder.decode_json(new_result, i, "date_published");
                        }
                    }
                    Timestamp a = Timestamp.valueOf(date_publisheds[0]);
                    Timestamp b = Timestamp.valueOf(date_publisheds[1]);
                    int which_one = 0;
                    if (b.after(a)) {
                        which_one = 1;
                    }
                    String files = decoder.decode_json(new_result, which_one, "files");
                    String file_url = decoder.decode_json(files, 0, "url");
                    String file_name = decoder.decode_json(files, 0, "filename");
                    String file_path = Bukkit.getPluginsFolder().getAbsolutePath()+"\\"+file_name;
                    downloadFileByUrl(plugin_title, file_url, file_path, file_name, false, player);
                    enable_plugin(file_path, plugin_title, false, player);
                }
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("Plugin not found Or error was made!");
            }
        }
    }

    public static void enable_plugin(String file_path, String plugin_title, boolean is_console, Player player) throws InvalidPluginException, InvalidDescriptionException {
        if(is_console) {
            System.out.println("Start loading plugin " + plugin_title);
            Bukkit.getPluginManager().loadPlugin(new File(file_path));
            System.out.println("Plugin " + plugin_title + " loaded!");
            System.out.println("Start enable plugin " + plugin_title);
            Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(plugin_title));
            System.out.println("Plugin " + plugin_title + "'s installation was complete!");
        } else {
            player.sendMessage("Start loading plugin" + plugin_title);
            Bukkit.getPluginManager().loadPlugin(new File(file_path));
            player.sendMessage("Plugin " + plugin_title + " loaded!");
            player.sendMessage("Start enable plugin " + plugin_title);
            Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(plugin_title));
            player.sendMessage("Plugin " + plugin_title + "'s installation was complete!");
        }
    }

    public static void downloadFileByUrl(String plugin_title, String urlPath, String downloadPath, String fileName, boolean is_console, Player player) throws IOException {
        if(is_console) {
            try {
                System.out.println("Start download file: "+fileName+" from:"+urlPath);
                File file = new File(downloadPath);
                if(file.exists()) {
                    if(Bukkit.getPluginManager().getPlugin(plugin_title) != null) {
                        if(Bukkit.getPluginManager().getPlugin(plugin_title).isEnabled()) {
                            return;
                        } else{
                            Plugin plugin = Bukkit.getPluginManager().getPlugin(plugin_title);
                            Bukkit.getPluginManager().enablePlugin(plugin);
                            return;
                        }
                    } else {
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
                while ((size = inStream.read(buf)) != -1) {//循环读取
                    fos.write(buf, 0, size);
                    pbt.updateProgress(size);//写完一次，更新进度条
                }
                pbt.finish();//文件读取完成，关闭进度条
                fos.flush();
                fos.close();
                System.out.println("File downloaded successfully to: "+downloadPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                player.sendMessage("Start download file: "+fileName+" from:"+urlPath);
                File file = new File(downloadPath);
                if(!file.exists()) {
                    file.createNewFile();
                } else {
                    return;
                }
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(downloadPath);
                IDKnetHandler pbt = new IDKnetHandler((int) file.length(), 1L, "000001");//创建进度条
                new Thread(pbt).start();//开启线程，刷新进度条
                byte[] buf = new byte[1024];
                int size = 0;
                while ((size = fis.read(buf)) > -1) {//循环读取
                    fos.write(buf, 0, size);
                    pbt.updateProgress(size);//写完一次，更新进度条
                }
                pbt.finish();//文件读取完成，关闭进度条
                fos.flush();
                fos.close();
                fis.close();
                player.sendMessage("File downloaded successfully to: "+downloadPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void get_projects_by_title(Player player, boolean is_console, String title) throws IOException {
        if (is_console) {
            System.out.println("Trying to get "+title+" plugin's info...");
            int limit = 10;
            String url = "https://api.modrinth.com/v2/search?limit="+limit+"&facets=";
            String categories = "paper";
            String versions = Bukkit.getServer().getMinecraftVersion();
            String arg = "[[\"categories:"+categories+"\"],[\"versions:"+versions+"\"],[\"project_type:plugin\"],[\"title:"+title+"\"]]";
            String real_uri = argument_handler(url, arg);
            String result = sendGet(real_uri);
            int total_hits = decoder.get_total_hits(result);
            if(total_hits > limit) {
                for(int i = 0; i < limit; i++) {
                    String plugin_title = decoder.decode_json(result, i, "hits", "title");
                    String plugin_description = decoder.decode_json(result, i, "hits", "description");
                    String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                    int a = i+1;
                    System.out.println(a+". Name: " + plugin_title + "\nID: " + plugin_id + "\nDescription: " + plugin_description);
                }
            } else if(total_hits > 0) {
                for(int i = 0; i < total_hits; i++) {
                    String plugin_title = decoder.decode_json(result, i, "hits", "title");
                    String plugin_description = decoder.decode_json(result, i, "hits", "description");
                    String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                    int a = i+1;
                    System.out.println(a+". Name: " + plugin_title + "\nID: " + plugin_id + "\nDescription: " + plugin_description);
                }
            } else {
                System.out.println("Error: Plugin not found or error was made! Check console messages if there's a error!");
                System.out.println("Or maybe try to type full name of the plugin because modrinth can only search that!");
            }
        } else {
            player.sendMessage("Trying to get "+title+" plugin's info...");
            int limit = 10;
            String url = "https://api.modrinth.com/v2/search?limit="+limit+"&facets=";
            String categories = "paper";
            String versions = Bukkit.getServer().getMinecraftVersion();
            String arg = "[[\"categories:"+categories+"\"],[\"versions:"+versions+"\"],[\"project_type:plugin\"],[\"title:"+title+"\"]]";
            String real_uri = argument_handler(url, arg);
            String result = sendGet(real_uri);
            int total_hits = decoder.get_total_hits(result);
            if(total_hits > limit) {
                for(int i = 0; i < limit; i++) {
                    String plugin_title = decoder.decode_json(result, i, "hits", "title");
                    String plugin_description = decoder.decode_json(result, i, "hits", "description");
                    String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                    int a = i+1;
                    player.sendMessage(a+". Name: " + plugin_title + "\nID: " + plugin_id + "\nDescription: " + plugin_description);
                }
            } else if(total_hits > 0) {
                for(int i = 0; i < total_hits; i++) {
                    String plugin_title = decoder.decode_json(result, i, "hits", "title");
                    String plugin_description = decoder.decode_json(result, i, "hits", "description");
                    String plugin_id = decoder.decode_json(result, i, "hits", "project_id");
                    int a = i+1;
                    player.sendMessage(a+". Name: " + plugin_title + "\nID: " + plugin_id + "\nDescription: " + plugin_description);
                }
            } else {
                player.sendMessage("Error: Plugin not found or error was made! Check console messages if there's a error!");
                player.sendMessage("Or maybe try to type full name of the plugin because modrinth can only search that!");
            }
        }
    }

    public static String argument_handler(String url, String arg) throws UnsupportedEncodingException {
        String encode_arg = URLEncoder.encode(arg, "UTF-8");
        String real_uri = url+encode_arg;
        return real_uri;
    }

    public static String sendGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
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
                        System.err.println("当前进度："+showProgress+"%");
                    }
                }
                System.err.println("下载完成");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

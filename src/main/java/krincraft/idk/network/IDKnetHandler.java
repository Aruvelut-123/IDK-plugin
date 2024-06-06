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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class IDKnetHandler {
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
                String plugin_title = decoder.get_total_hits(result, i, "hits", "title");
                String plugin_description = decoder.get_total_hits(result, i, "hits", "description");
                String plugin_id = decoder.get_total_hits(result, i, "hits", "project_id");
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
                String plugin_title = decoder.get_total_hits(result, i, "hits", "title");
                String plugin_description = decoder.get_total_hits(result, i, "hits", "description");
                String plugin_id = decoder.get_total_hits(result, i, "hits", "project_id");
                int a = i+1;
                player.sendMessage(a+". Name: " + plugin_title + "\nID: " + plugin_id + "\nDescription: " + plugin_description);
            }
            player.sendMessage("Install a plugin by using: /idk plugin install <id>");
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
                    String plugin_title = decoder.get_total_hits(result, i, "hits", "title");
                    String plugin_description = decoder.get_total_hits(result, i, "hits", "description");
                    String plugin_id = decoder.get_total_hits(result, i, "hits", "project_id");
                    int a = i+1;
                    System.out.println(a+". Name: " + plugin_title + "\nID: " + plugin_id + "\nDescription: " + plugin_description);
                }
            } else if(total_hits > 0) {
                for(int i = 0; i < total_hits; i++) {
                    String plugin_title = decoder.get_total_hits(result, i, "hits", "title");
                    String plugin_description = decoder.get_total_hits(result, i, "hits", "description");
                    String plugin_id = decoder.get_total_hits(result, i, "hits", "project_id");
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
            try {
                String plugin_title = decoder.get_total_hits(result, 0, "hits", "title");
                String plugin_description = decoder.get_total_hits(result, 0, "hits", "description");
                String plugin_id = decoder.get_total_hits(result, 0, "hits", "project_id");
                player.sendMessage("Name: " + plugin_title + "\nID: " + plugin_id + "\nDescription: " + plugin_description);
                player.sendMessage("Install this plugin by using: /idk plugin install "+plugin_id);
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("Error: Plugin not found or error was made! Check console messages if there's a error!");
                player.sendMessage("Or maybe try to type full name of the plugin because modrinth can only search that!");
            }
        }
    }

    public static String fix_json_decode(String decoded_json_string) {
        return decoded_json_string.replace("[", "").replace("]","");
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
}

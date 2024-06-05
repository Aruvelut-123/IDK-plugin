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
import java.io.IOException;

public class IDKnetHandler {
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    public static void get_projects() {
        String url = "https://api.modrinth.com/v2/search?";
        String categories = "spigot";
        String versions = Bukkit.getServer().getMinecraftVersion();
        String arg = "facets=[[\"categories:"+categories+"\"],[\"versions:"+versions+"\"],[\"project_type:plugin\"]]";
        String uri = url+"limit=10&"+arg;
        String result = sendGet(uri);
        decoder.decode_json(result,"");
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

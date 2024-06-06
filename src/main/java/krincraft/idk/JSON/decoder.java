package krincraft.idk.JSON;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

public class decoder {
    public static Map<String, Object> convertJsonToMap(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Map.class);
    }

    public static Integer get_total_hits(String json) throws IOException {
        Map<String, Object> converted_json_map = convertJsonToMap(json);
        JSONObject jsonObject = new JSONObject(converted_json_map);
        return jsonObject.getInteger("total_hits");
    }

    public static String get_total_hits(String json, int number, String key1, String key2) throws IOException {
        Map<String, Object> converted_json_map = convertJsonToMap(json);
        JSONObject jsonObject = new JSONObject(converted_json_map);
        return jsonObject.getJSONArray(key1).getJSONObject(number).getString(key2);
    }
}

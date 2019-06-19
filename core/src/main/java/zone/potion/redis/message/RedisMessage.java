package zone.potion.redis.message;

import com.google.gson.reflect.TypeToken;
import zone.potion.CorePlugin;

import java.lang.reflect.Type;
import java.util.Map;

public class RedisMessage {

    public static Map<String, Object> deserialize(String string) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return CorePlugin.GSON.fromJson(string, type);
    }

    public static String serialize(Map<String, Object> map) {
        return CorePlugin.GSON.toJson(map);
    }

}

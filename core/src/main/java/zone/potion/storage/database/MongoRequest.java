package zone.potion.storage.database;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import zone.potion.CorePlugin;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoRequest {
    private final String collectionName;
    private final Object documentObject;
    private final Map<String, Object> updatePairs = new HashMap<>();

    public static MongoRequest newRequest(String collectionName, Object value) {
        return new MongoRequest(collectionName, value);
    }

    public MongoRequest put(String key, Object value) {
        updatePairs.put(key, value);
        return this;
    }

    public void run() {
        CorePlugin.getInstance().getMongoStorage().massUpdate(collectionName, documentObject, updatePairs);
    }
}

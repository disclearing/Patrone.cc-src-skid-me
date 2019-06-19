package zone.potion.storage.database;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import zone.potion.CorePlugin;
import zone.potion.callback.DocumentCallback;

import java.util.Map;

public class MongoStorage {
    private final MongoDatabase database;

    public MongoStorage() {
        MongoClient mongoClient = MongoClients.create(
                CorePlugin.getInstance().getConfig().getString("mongo.uri")
        );
        database = mongoClient.getDatabase("potionzone");
    }

    public void getOrCreateDocument(String collectionName, Object documentObject, DocumentCallback callback) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document document = new Document("_id", documentObject);

        try (MongoCursor<Document> cursor = collection.find(document).iterator()) {
            if (cursor.hasNext()) {
                callback.call(cursor.next(), true);
            } else {
                collection.insertOne(document);
                callback.call(document, false);
            }
        }
    }

    public MongoCursor<Document> getAllDocuments(String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.find().iterator();
    }

    public Document getDocumentByFilter(String collectionName, String filter, Object documentObject) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.find(Filters.eq(filter, documentObject)).first();
    }

    public Document getDocument(String collectionName, Object documentObject) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.find(Filters.eq("_id", documentObject)).first();
    }

    public void updateDocument(String collectionName, Object documentObject, String key, Object newValue) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.updateOne(Filters.eq(documentObject), Updates.set(key, newValue));
    }

    public void massUpdate(String collectionName, Object documentObject, Map<String, Object> updates) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            collection.updateOne(Filters.eq(documentObject), Updates.set(entry.getKey(), entry.getValue()));
        }
    }
}

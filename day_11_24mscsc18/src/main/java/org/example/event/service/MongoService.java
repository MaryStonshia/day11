package org.example.event.service;

import com.mongodb.client.*;
import org.bson.Document;
import org.example.event.model.User;




import java.util.*;

import static com.mongodb.client.model.Filters.*;

public class MongoService {
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> events;
    private final MongoCollection<Document> bookings;

    public MongoService() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("event_ticket");
        users = db.getCollection("users");
        events = db.getCollection("events");
        bookings = db.getCollection("bookings");
    }

    public void saveUser(User user) {
        Document doc = new Document("email", user.email)
                .append("name", user.name)
                .append("password", user.password);
        users.insertOne(doc);
    }

    public boolean validateLogin(String email, String password) {
        return users.find(and(eq("email", email), eq("password", password))).first() != null;
    }

    public List<Document> getAllEvents() {
        return events.find().into(new ArrayList<>());
    }

    public boolean bookEvent(String email, String eventId, String token) {
        Document event = events.find(eq("_id", eventId)).first();
        if (event == null || event.getInteger("availableTokens") <= 0) return false;

        events.updateOne(eq("_id", eventId),
                new Document("$inc", new Document("availableTokens", -1)));

        Document booking = new Document("userEmail", email)
                .append("eventId", eventId)
                .append("tokenCode", token);
        bookings.insertOne(booking);
        return true;
    }
}

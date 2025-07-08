package org.example.event;

import static spark.Spark.*;
import com.google.gson.*;
import org.example.event.model.User;
import org.example.event.service.*;
import org.example.event.util.TokenUtil;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        staticFiles.location("/public");
        MongoService db = new MongoService();
        EmailService emailService = new EmailService("shosha202002@gmail.com", "funl iwoq bdzr ujxt");
        AuthService authService = new AuthService(db);
        Gson gson = new Gson();
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });
        post("/register", (req, res) -> {
            User user = gson.fromJson(req.body(), User.class);
            String password = TokenUtil.generateToken(8);
            user.password = password;
            db.saveUser(user);
            emailService.sendEmail(user.email, "Your Password", "Your password is: " + password);
            return "Registered";
        });

        post("/login", (req, res) -> {
            Map<?, ?> body = gson.fromJson(req.body(), Map.class);
            String email = (String) body.get("email");
            String password = (String) body.get("password");
            return authService.login(email, password) ? "Login Success" : "Invalid Credentials";
        });

        get("/events", (req, res) -> gson.toJson(db.getAllEvents()));

        post("/book", (req, res) -> {
            Map<?, ?> body = gson.fromJson(req.body(), Map.class);
            String email = (String) body.get("email");
            String eventId = (String) body.get("eventId");
            String token = TokenUtil.generateToken(10);
            boolean success = db.bookEvent(email, eventId, token);
            if (success) {
                emailService.sendEmail(email, "Booking Confirmed", "Your token is: " + token);
                return "Booking Confirmed";
            } else {
                return "Booking Failed";
            }
        });

    }
}
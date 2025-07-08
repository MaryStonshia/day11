package org.example.event.service;

public class AuthService {
    private final MongoService mongoService;

    public AuthService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public boolean login(String email, String password) {
        return mongoService.validateLogin(email, password);
    }
}
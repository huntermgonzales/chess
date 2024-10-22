package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requests.LoginRequest;
import requests.RegisterRequest;
import service.RegisterService;

public class RegisterHandler {

    public void handleRegisterRequest(String json) {
        try {
            var serializer = new Gson();
            var registerRequest = serializer.fromJson(json, RegisterRequest.class);
            RegisterService registerService = new RegisterService();
            registerService.register(registerRequest);
        } catch (DataAccessException e) {
            //errorCodes
        }
    }
}

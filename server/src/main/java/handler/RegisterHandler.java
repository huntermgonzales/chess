package handler;

import com.google.gson.Gson;
import requests.LoginRequest;
import requests.RegisterRequest;
import service.RegisterService;

public class RegisterHandler {

    public void handleRegisterRequest(String json) {
        var serializer = new Gson();
        var registerRequest = serializer.fromJson(json, RegisterRequest.class);
//        RegisterService registerService = new RegisterService()

    }
}

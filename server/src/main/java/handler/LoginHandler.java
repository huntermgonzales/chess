package handler;

import Results.ErrorResult;
import Results.LoginResult;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import requests.LoginRequest;
import requests.LoginRequest;
import service.LoginService;

public class LoginHandler {

    public String handleLogin(String json) {
        String resultJson;
        Gson serializer = new Gson();
        try {
            var loginRequest = serializer.fromJson(json, LoginRequest.class);
            LoginService loginService = new LoginService();
            LoginResult result = loginService.login(loginRequest);
            resultJson = serializer.toJson(result);
        } catch (
                DataAccessException e) {
            ErrorResult result;
            if (e.getClass() == UnauthorizedException.class) {
                result = new ErrorResult("Error: unauthorized");
            } else {
                result = new ErrorResult("Error: data access error");
            }
            resultJson = serializer.toJson(result);
        }
        return resultJson;
    }
}

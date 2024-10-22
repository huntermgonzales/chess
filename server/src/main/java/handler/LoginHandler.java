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
import spark.Request;
import spark.Response;

public class LoginHandler {

    public Object handleLogin(Request req, Response res) {
        String resultJson;
        Gson serializer = new Gson();
        try {
            var loginRequest = serializer.fromJson(req.body(), LoginRequest.class);
            LoginService loginService = new LoginService();
            LoginResult result = loginService.login(loginRequest);
            resultJson = serializer.toJson(result);
            res.status(200);
        } catch (
                DataAccessException e) {
            ErrorResult result;
            if (e.getClass() == UnauthorizedException.class) {
                result = new ErrorResult("Error: unauthorized");
                res.status(401);
            } else {
                result = new ErrorResult("Error: data access error");
                res.status(500);
            }
            resultJson = serializer.toJson(result);
        }
        return resultJson;
    }
}

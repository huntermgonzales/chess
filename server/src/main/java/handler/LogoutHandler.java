package handler;

import Results.ErrorResult;
import Results.LoginResult;
import Results.LogoutResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import requests.LogoutRequest;
import service.LoginService;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {

    public Object handleLogout(Request req, Response res) {
        String resultJson;
        Gson serializer = new Gson();
        try {
            String authToken = req.headers("Authorization").toString();
            LogoutService logoutService = new LogoutService();
            LogoutResult result = logoutService.logout(authToken);
            resultJson = serializer.toJson(result);
            res.status(200);
        } catch (DataAccessException e) {
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

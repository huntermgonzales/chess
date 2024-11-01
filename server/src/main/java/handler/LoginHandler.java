package handler;

import results.LoginResult;
import com.google.gson.Gson;
import exceptions.DataAccessException;
import requests.LoginRequest;
import service.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler{

    public Object handleLogin(Request req, Response res) {
        String resultJson;
        Gson serializer = new Gson();
        try {
            var loginRequest = serializer.fromJson(req.body(), LoginRequest.class);
            LoginService loginService = new LoginService();
            LoginResult result = loginService.login(loginRequest);
            resultJson = serializer.toJson(result);
            res.status(200);
        } catch (DataAccessException e) {
            resultJson = catchErrors(e, res);
        }
        return resultJson;
    }
}

package handler;

import results.RegisterResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requests.RegisterRequest;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {

    public Object handleRegister(Request req, Response res) {
        String resultJson;
        var serializer = new Gson();
        try {
            var registerRequest = serializer.fromJson(req.body(), RegisterRequest.class);
            RegisterService registerService = new RegisterService();
            RegisterResult result = registerService.register(registerRequest);
            resultJson = serializer.toJson(result);
            res.status(200);
        } catch (DataAccessException e) {
            resultJson = catchErrors(e, res);
        }
        return resultJson;
    }
}

package handler;

import Results.ErrorResult;
import Results.RegisterResult;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import requests.RegisterRequest;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

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
            ErrorResult result;
            if (e.getClass() == BadRequestException.class) {
                result = new ErrorResult("Error: bad request");
                res.status(400);
            } else if (e.getClass() == AlreadyTakenException.class) {
                result = new ErrorResult("Error: already taken");
                res.status(403);
            } else {
                result = new ErrorResult("Error: data access error");
                res.status(500);
            }
            resultJson = serializer.toJson(result);
        }
        return resultJson;
    }
}

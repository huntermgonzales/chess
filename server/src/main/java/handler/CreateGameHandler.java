package handler;

import results.CreateGameResult;
import results.ErrorResult;
import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import requests.CreateGameRequest;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {

    public Object handleCreateGame(Request req, Response res) {
        String resultJson;
        Gson serializer = new Gson();
        try {
            var createGameRequest = serializer.fromJson(req.body(), CreateGameRequest.class);
            String authToken = req.headers("Authorization");
            CreateGameService createGameService = new CreateGameService();
            CreateGameResult result = createGameService.createGame(createGameRequest, authToken);
            resultJson = serializer.toJson(result);
            res.status(200);
        }catch (DataAccessException e) {
            ErrorResult result;
            if (e.getClass() == UnauthorizedException.class) {
                result = new ErrorResult("Error: unauthorized");
                res.status(401);
            } else if (e.getClass() == BadRequestException.class) {
                result = new ErrorResult("Error: bad request");
                res.status(400);
            } else {
                result = new ErrorResult("Error: data access error");
                res.status(500);
            }
            resultJson = serializer.toJson(result);
        }

        return resultJson;
    }
}

package handler;

import results.CreateGameResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requests.CreateGameRequest;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler{

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
            resultJson = catchErrors(e, res);
        }

        return resultJson;
    }
}

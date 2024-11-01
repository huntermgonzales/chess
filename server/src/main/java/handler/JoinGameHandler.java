package handler;

import results.JoinGameResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requests.JoinGameRequest;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {

    public Object handleJoinGame(Request req, Response res) {
        String resultJson;
        Gson serializer = new Gson();
        try {
            var joinGameRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
            String authToken = req.headers("Authorization");
            JoinGameService joinGameService = new JoinGameService();
            JoinGameResult result= joinGameService.joinGame(authToken, joinGameRequest);
            resultJson = serializer.toJson(result);
            res.status(200);
        } catch (DataAccessException e) {
            resultJson = catchErrors(e, res);
        }
        return resultJson;
    }
}

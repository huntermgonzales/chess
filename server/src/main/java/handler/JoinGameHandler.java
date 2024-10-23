package handler;

import results.ErrorResult;
import results.JoinGameResult;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import requests.JoinGameRequest;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {

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
            ErrorResult result;
            if (e.getClass() == UnauthorizedException.class) {
                result = new ErrorResult("Error: unauthorized");
                res.status(401);
            } else if (e.getClass() == BadRequestException.class) {
                result = new ErrorResult("Error: bad request");
                res.status(400);
            } else if (e.getClass() == AlreadyTakenException.class) {
                result = new ErrorResult("Error: already taken");
                res.status(403);
            }else {
                result = new ErrorResult("Error: data access error");
                res.status(500);
            }
            resultJson = serializer.toJson(result);

        }
        return resultJson;
    }
}

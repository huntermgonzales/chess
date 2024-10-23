package handler;

import results.ErrorResult;
import results.ListGamesResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import service.ListGameService;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler{

    public Object handleListGames(Request req, Response res) {
        String resultJson;
        Gson serializer = new Gson();
        try {
            String authToken = req.headers("Authorization");
            ListGameService listGameService = new ListGameService();
            ListGamesResult result = listGameService.listGames(authToken);
            resultJson = serializer.toJson(result);
            res.status(200);
        } catch (DataAccessException e) {
            resultJson = catchErrors(e, res);
        }
        return resultJson;
    }
}

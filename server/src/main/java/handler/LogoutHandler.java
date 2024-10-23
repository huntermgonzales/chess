package handler;

import results.ErrorResult;
import results.LogoutResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {

    public Object handleLogout(Request req, Response res) {
        String resultJson;
        Gson serializer = new Gson();
        try {
            String authToken = req.headers("Authorization");
            LogoutService logoutService = new LogoutService();
            LogoutResult result = logoutService.logout(authToken);
            resultJson = serializer.toJson(result);
            res.status(200);
        } catch (DataAccessException e) {
            resultJson = catchErrors(e, res);
        }
        return resultJson;
    }
}

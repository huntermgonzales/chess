package handler;

import Results.ClearResult;
import Results.ErrorResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public Object handleClear(Request req, Response res) {
        String resultJson;
        Gson serializer = new Gson();
        try {
            ClearService clearService = new ClearService();
            ClearResult result = clearService.clearAll();
            resultJson = serializer.toJson(result);
            res.status(200);
        } catch (DataAccessException e) {
            ErrorResult result = new ErrorResult("Error: data access error");
            res.status(500);
            resultJson = serializer.toJson(result);
        }
        return resultJson;
    }
}

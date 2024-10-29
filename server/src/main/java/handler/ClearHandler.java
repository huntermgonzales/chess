package handler;

import results.ClearResult;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler{

    public Object handleClear(Request req, Response res) {
        String resultJson;
        Gson serializer = new Gson();
        try {
            ClearService clearService = new ClearService();
            ClearResult result = clearService.clearAll();
            resultJson = serializer.toJson(result);
            res.status(200);
        } catch (DataAccessException e) {
            resultJson = catchErrors(e, res);
        }
        return resultJson;
    }
}

package handler;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import results.ErrorResult;
import spark.Response;

public class Handler {

    public String catchErrors(DataAccessException e, Response res) {
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
        return new Gson().toJson(result);
    }
}

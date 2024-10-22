package service;

import Results.ListGamesResult;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.LocalMemory;
import model.GameData;
import requests.ListGameRequest;

import java.util.List;

public class ListGameService extends Service{


    public ListGamesResult listGames(ListGameRequest request) throws DataAccessException {
        authorize(request.authToken());
        return new ListGamesResult(gameDAO.listAllGames());
    }
}

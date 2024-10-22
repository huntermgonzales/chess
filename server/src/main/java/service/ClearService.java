package service;

import dataaccess.LocalMemory;

public class ClearService extends Service{

    public void clearAll () {
        gameDAO.deleteAll();
        authDAO.deleteAll();
        userDAO.deleteAll();
    }

}

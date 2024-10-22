package service;

import dataaccess.LocalMemory;

public class ClearService extends Service{
    public ClearService(LocalMemory localMemory) {
        super(localMemory);
    }

    public void clearAll () {
        gameDAO.deleteAll();
        authDAO.deleteAll();
        userDAO.deleteAll();
    }

}

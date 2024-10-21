package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO{
    LocalMemory localMemory;
    public MemoryAuthDAO(LocalMemory localMemory) {
        this.localMemory = localMemory;
    }

    @Override
    public void deleteAuthData(String authToken) throws DataAccessException{
        if (localMemory.getAuthData(authToken) == null){
            throw new DataAccessException("Auth Data does not exist");
        }
        localMemory.deleteAuthData(authToken);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        if (localMemory.getAuthData(authToken) == null){
            throw new DataAccessException("Auth Data does not exist");
        }
        return localMemory.getAuthData(authToken);
    }

    @Override
    public void addAuthData(AuthData authData) throws DataAccessException {
        if (localMemory.getAuthData(authData.authToken()) != null){
            throw new DataAccessException("Auth Data already exists");
        }
        localMemory.createAuthData(authData);
    }


    @Override
    public void deleteAll() {
        localMemory.deleteAllAuthData();
    }
}

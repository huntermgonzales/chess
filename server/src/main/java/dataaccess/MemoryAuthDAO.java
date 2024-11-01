package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    LocalMemory localMemory = LocalMemory.getInstance();

    @Override
    public void deleteAuthData(String authToken) {

        localMemory.deleteAuthData(authToken);
    }

    @Override
    public AuthData getAuthData(String authToken) {
        if (localMemory.getAuthData(authToken) == null){
            return null;
        }
        return localMemory.getAuthData(authToken);
    }

    @Override
    public void addAuthData(AuthData authData) {
        localMemory.createAuthData(authData);
    }


    @Override
    public void deleteAll() {
        localMemory.deleteAllAuthData();
    }
}

package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{

    LocalMemory localMemory;
    public MemoryUserDAO(LocalMemory localMemory) {
        this.localMemory = localMemory;
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        if (localMemory.getUserData(userData.username()) != null){
            throw new DataAccessException("user already taken");
        }
        localMemory.createUserData(userData);
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        if (localMemory.getUserData((username)) == null){
            throw new DataAccessException("user does not exist");
        }
        return localMemory.getUserData(username);
    }

    @Override
    public void deleteAll() {

    }
}

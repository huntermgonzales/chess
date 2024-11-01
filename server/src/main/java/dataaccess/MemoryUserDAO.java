package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO {

    LocalMemory localMemory = LocalMemory.getInstance();

    @Override
    public void addUser(UserData userData) {
        localMemory.createUserData(userData);
    }

    @Override
    public UserData getUserData(String username) {
        if (localMemory.getUserData((username)) == null){
            return null;
        }
        return localMemory.getUserData(username);
    }

    @Override
    public void deleteAll() {
        localMemory.deleteAllUserData();
    }
}

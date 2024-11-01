package dataaccess;

import exceptions.DataAccessException;
import model.UserData;

public interface UserDAO extends DataAccess{
    public void addUser(UserData userData) throws DataAccessException;
    public UserData getUserData(String username) throws DataAccessException;
}

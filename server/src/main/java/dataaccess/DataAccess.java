package dataaccess;

import exceptions.DataAccessException;

public interface DataAccess {
    public void deleteAll() throws DataAccessException;
}

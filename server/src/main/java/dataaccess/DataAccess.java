package dataaccess;

import dataaccess.exceptions.DataAccessException;

public interface DataAccess {
    public void deleteAll() throws DataAccessException;
}

package com.lowagie.database;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This is a helper class to access an hsqldb database.
 * The database scripts are supposed to be present in directory:
 * resources/db
 */
public class HsqldbConnection extends DatabaseConnection {

    /**
     * Creates the connection.
     * @param db_file_name_prefix the database name,
     * which is the prefix of the database file
     * @throws SQLException 
     */
    public HsqldbConnection(String db_file_name_prefix)
        throws SQLException {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("HSQLDB database driver not found");
        }
        connection = DriverManager.getConnection(
            "jdbc:hsqldb:src/main/resources/db/" + db_file_name_prefix + ";shutdown=true", "SA", "");
    }

}

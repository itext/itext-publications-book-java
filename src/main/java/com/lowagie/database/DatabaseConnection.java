package com.lowagie.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This is a helper class to access the database
 * that comes with the book samples.
 */

public abstract class DatabaseConnection {

    /**
     * our connection to the db.
     */
    protected Connection connection;

    /**
     * Closes the connection to the database.
     *
     * @throws SQLException database related error
     */
    public void close() throws SQLException {
        connection.close();
    }

    /**
     * Creates a statement.
     *
     * @return a statement
     *
     * @throws SQLException database related error
     */
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    /**
     * Creates a prepated statement using a query.
     *
     * @param query the query that will be used to create
     *              a prepared statement.
     *
     * @return a statement
     *
     * @throws SQLException database related error
     */
    public PreparedStatement createPreparedStatement(String query)
            throws SQLException {
        return connection.prepareStatement(query);
    }

    /**
     * Performs an update in the database.
     *
     * @param expression an SQL expression
     *                   (CREATE, DROP, INSERT, UPDATE)
     *
     * @throws SQLException database related error
     */
    public void update(String expression) throws SQLException {
        Statement st = createStatement();
        int i = st.executeUpdate(expression);
        st.close();
        if (i == -1) {
            throw new SQLException("db error : " + expression);
        }
    }
}

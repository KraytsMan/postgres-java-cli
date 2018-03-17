package com.kraytsman.service;

import com.kraytsman.exception.PostgresConnectionException;

import java.sql.*;

import static java.lang.String.format;

public class Postgres implements JDBCService {

    private static final String GET_DBS_SQL = "SELECT * FROM pg_database WHERE datistemplate = FALSE;";
    private static final String GET_DBS_TABLES_SQL = "SELECT table_catalog, table_schema, table_name, table_type FROM information_schema.tables;";

    private static final String DEFAULT_DB = "postgres";

    private State state;

    public Postgres(String host, String port, String username, String password) {
        try (Connection connection = DriverManager.getConnection(getUrl(host, port, DEFAULT_DB), username, password)) {
            this.state = new State(host, port, username, password);
            this.state.setCurrentDatabase(DEFAULT_DB);
        } catch (Exception e) {
            throw new PostgresConnectionException(e.getMessage());
        }
    }

    @Override
    public ResultSet showDatabases() throws SQLException {
        try (Connection connection = getConnection(DEFAULT_DB)) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_DBS_SQL);
            return preparedStatement.executeQuery();
        }
    }

    @Override
    public ResultSet showTables() throws SQLException {
        return executeRequest(GET_DBS_TABLES_SQL);
    }

    @Override
    public void switchDB(String dbname) {
        try {
            Connection connection = DriverManager.getConnection(getUrl(state.getHost(), state.getPort(), dbname),
                    state.getUsername(), state.getPassword());
            connection.close();
            this.state.setCurrentDatabase(dbname);
        } catch (Exception e) {
            throw new PostgresConnectionException(e.getMessage());
        }
    }

    @Override
    public ResultSet query(String query) throws SQLException {
        return executeRequest(query);
    }

    private ResultSet executeRequest(String query) throws SQLException {
        try (Connection connection = getConnection(state.getCurrentDatabase())) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        }
    }

    private String getUrl(String host, String port, String db) {
        return format("jdbc:postgresql://%s:%s/%s", host, port, db);
    }

    private Connection getConnection(String db) throws SQLException {
        return DriverManager.getConnection(getUrl(state.getHost(), state.getPort(), db),
                state.getUsername(), state.getPassword());
    }

}

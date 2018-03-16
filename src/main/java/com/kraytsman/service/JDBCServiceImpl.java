package com.kraytsman.service;

import com.kraytsman.exception.PostgresConnectionException;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class JDBCServiceImpl implements JDBCService {

    private static final String GET_DBS_SQL = "SELECT datname AS DATABASE FROM pg_database WHERE datistemplate = FALSE;";
    private static final String GET_DBS_TABLES_SQL = "SELECT table_name AS TABLE FROM information_schema.tables;";

    private static final String DEFAULT_DB = "postgres";

    private Optional<State> state;

    @Override
    public void connect(String host, String port, String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(getUrl(host, port, DEFAULT_DB), username, password);
            connection.close();
            this.state = Optional.of(new State(host, port, username, password));
            this.state.get().setCurrentDatabase(DEFAULT_DB);
        } catch (Exception e) {
            throw new PostgresConnectionException(e.getMessage());
        }
    }

    @Override
    public String[][] showDatabases() throws SQLException {
        Connection connection = getConnection(DEFAULT_DB);
        PreparedStatement preparedStatement = connection.prepareStatement(GET_DBS_SQL);
        String[][] data = parse(preparedStatement.executeQuery());
        connection.close();
        return data;
    }

    @Override
    public String[][] showTables() throws SQLException {
        State state = getState();
        Connection connection = getConnection(state.getCurrentDatabase());
        PreparedStatement preparedStatement = connection.prepareStatement(GET_DBS_TABLES_SQL);
        String[][] data = parse(preparedStatement.executeQuery());
        connection.close();
        return data;
    }

    @Override
    public void switchDB(String dbname) {
        State state = getState();
        try {
            Connection connection = DriverManager.getConnection(getUrl(state.getHost(), state.getPort(), dbname),
                    state.getUsername(), state.getPassword());
            connection.close();
            this.state.get().setCurrentDatabase(dbname);
        } catch (Exception e) {
            throw new PostgresConnectionException(e.getMessage());
        }
    }

    private String getUrl(String host, String port, String db) {
        return format("jdbc:postgresql://%s:%s/%s", host, port, db);
    }

    private Connection getConnection(String db) throws SQLException {
        State state = getState();
        return DriverManager.getConnection(getUrl(state.getHost(), state.getPort(), db),
                state.getUsername(), state.getPassword());
    }

    private String[][] parse(ResultSet set) throws SQLException {
        ResultSetMetaData metaData = set.getMetaData();
        List<List<String>> temp = new ArrayList<>();
        List<String> header = new ArrayList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            header.add(metaData.getColumnName(i));
        }
        temp.add(header);
        while (set.next()) {
            List<String> row = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.add(set.getString(i));
            }
            temp.add(row);
        }
        String[][] result = new String[temp.size()][metaData.getColumnCount()];
        for (int i = 0; i < temp.size(); i++) {
            result[i] = temp.get(i).toArray(new String[0]);
        }
        return result;
    }

    private State getState() {
        return this.state.orElseThrow(() ->
                new PostgresConnectionException("User is not able to use this function!\n Please connect to db first."));
    }

}

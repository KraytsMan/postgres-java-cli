package com.kraytsman.service;

import java.sql.SQLException;

public interface JDBCService {

    void connect(String host, String port, String username, String password);

    String[][] showDatabases() throws SQLException;

    String[][] showTables() throws SQLException;

    void switchDB(String dbname);

    String[][] query(String query) throws SQLException;

}

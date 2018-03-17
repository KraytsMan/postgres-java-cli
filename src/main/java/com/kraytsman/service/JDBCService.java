package com.kraytsman.service;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface JDBCService {

    ResultSet showDatabases() throws SQLException;

    ResultSet showTables() throws SQLException;

    void switchDB(String dbname);

    ResultSet query(String query) throws SQLException;

}

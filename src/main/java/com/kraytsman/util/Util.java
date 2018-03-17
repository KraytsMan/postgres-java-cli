package com.kraytsman.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface Util {

    static String[][] parse(ResultSet set) throws SQLException {
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
                String string = set.getString(i);
                if ((string == null)) {
                    row.add("null");
                } else {
                    row.add(string);
                }
            }
            temp.add(row);
        }

        String[][] result = new String[temp.size()][metaData.getColumnCount()];
        for (int i = 0; i < temp.size(); i++) {
            result[i] = temp.get(i).toArray(new String[0]);
        }
        return result;
    }

}

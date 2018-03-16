package com.kraytsman.command;

import com.jakewharton.fliptables.FlipTable;
import com.kraytsman.exception.PostgresConnectionException;
import com.kraytsman.service.JDBCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Arrays;

@ShellComponent
public class Commands {

    private JDBCService jdbcService;

    @Autowired
    public Commands(JDBCService jdbcService) {
        this.jdbcService = jdbcService;
    }

    @ShellMethod("Add two integers together.")
    public int add(int a, int b) {
        return a + b;
    }

    @ShellMethod("Connect to postgres database")
    public String connect(@ShellOption(value = "-host") String host,
                          @ShellOption(value = "-port") String port,
                          @ShellOption(value = "-user") String username,
                          @ShellOption(value = "-password") String password) {
        try {
            this.jdbcService.connect(host, port, username, password);
            return "Welcome to postgres CLI!";
        } catch (PostgresConnectionException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Print list of databases", key = "show databases;")
    public void showDatabases() {
        try {
            display(this.jdbcService.showDatabases());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod(value = "Print list of tables in database", key = "show tables;")
    public void showTables() {
        try {
            display(this.jdbcService.showTables());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod(value = "Switch database")
    public void use(String database) {
        try {
            this.jdbcService.switchDB(database);
            System.out.println(database + " is using!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void display(String[][] data) {
        String[] header = data[0];
        String[][] body = Arrays.copyOfRange(data, 1, data.length);
        System.out.println(FlipTable.of(header, body));
    }

}


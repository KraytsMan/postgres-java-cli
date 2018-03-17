package com.kraytsman.command;

import com.jakewharton.fliptables.FlipTable;
import com.kraytsman.exception.PostgresConnectionException;
import com.kraytsman.service.JDBCService;
import com.kraytsman.service.Postgres;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.Arrays;

import static com.kraytsman.util.Util.parse;

@ShellComponent
public class Commands {

    private JDBCService jdbcService;

    @ShellMethod("Connect to postgres database")
    public String connect(@ShellOption(value = "-host") String host,
                          @ShellOption(value = "-port") String port,
                          @ShellOption(value = "-user") String username,
                          @ShellOption(value = "-password") String password) {
        try {
            this.jdbcService = new Postgres(host, port, username, password);
            return "Welcome to postgres CLI!";
        } catch (PostgresConnectionException e) {
            return e.getMessage();
        }
    }

    @ShellMethod("Connect from postgres database")
    @ShellMethodAvailability("availabilityCheck")
    public String disconnect() {
        jdbcService = null;
        return "Disconnect from database!";
    }

    @ShellMethod(value = "Print list of databases", key = "show databases")
    @ShellMethodAvailability("availabilityCheck")
    public void showDatabases() {
        try {
            display(parse(jdbcService.showDatabases()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod(value = "Print list of tables in database", key = "show tables")
    @ShellMethodAvailability("availabilityCheck")
    public void showTables() {
        try {
            display(parse(jdbcService.showTables()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod(value = "Switch database")
    @ShellMethodAvailability("availabilityCheck")
    public void use(String database) {
        try {
            this.jdbcService.switchDB(database);
            System.out.println(database + " is using!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod(value = "Execute sql query")
    @ShellMethodAvailability("availabilityCheck")
    public void query(String query) {
        try {
            display(parse((jdbcService.query(query))));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Availability availabilityCheck() {
        return jdbcService != null
                ? Availability.available()
                : Availability.unavailable("you are not connected");
    }

    private void display(String[][] data) {
        String[] header = data[0];
        String[][] body = Arrays.copyOfRange(data, 1, data.length);
        System.out.println(FlipTable.of(header, body));
    }

}


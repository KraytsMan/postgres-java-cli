package com.kraytsman.service;

class State {

    private final String host;

    private final String port;

    private final String username;

    private final String password;

    private String currentDatabase;

    State(String host, String port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getCurrentDatabase() {
        return currentDatabase;
    }

    public void setCurrentDatabase(String currentDatabase) {
        this.currentDatabase = currentDatabase;
    }
}

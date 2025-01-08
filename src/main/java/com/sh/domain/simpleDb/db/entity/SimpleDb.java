package com.sh.domain.simpleDb.db.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class SimpleDb {
    private final String host;
    private final String user;
    private final String password;
    private final String dbName;
    private boolean devMode = false;
    private Connection conn;

    public SimpleDb(String host, String user, String password, String dbName) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.dbName = dbName;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://"+ host + ":3306/" + dbName, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void run(String Sql) {
        if (!devMode) return;
        if (conn == null) return;
        if (Sql == null) return;

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try  {
            Objects.requireNonNull(stmt).execute(Sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void run(Object... Sqls) {
        if (!devMode) return;
        if (conn == null) return;
        if (Sqls == null) return;

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String Sql = (String) Sqls[0];
        for (int i = 1; i < Sqls.length; i++) {
            Sql.replaceFirst("\\?", Objects.requireNonNull(Sqls[i]).toString());
        }

        try {
            Objects.requireNonNull(stmt).execute(Sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

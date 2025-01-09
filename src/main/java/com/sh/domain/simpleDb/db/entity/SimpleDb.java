package com.sh.domain.simpleDb.db.entity;

import com.sh.domain.simpleDb.sql.entity.Sql;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.*;
import java.util.*;

@Setter
@Getter
@SuperBuilder
public class SimpleDb {
    private final String host;
    private final String user;
    private final String password;
    private final String dbName;
    private boolean devMode = false;
    private Connection conn;
    private Sql sql;

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

        conn = Connection();
    }

    private Connection Connection() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://"+ host + ":3306/" + dbName, user, password)) {
            System.out.println("DB Connection Success");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Map<String, Object>> executeQuery(String query) {
        if (!devMode) return null;
        if (conn == null) return null;
        if (query == null) return null;

        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery()) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                results.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public void run(String query) {
        executeQuery(query);
    }

    public void run(Object... Sqls) {
        sql.setSql((String) Sqls[0]);
        for (int i = 1; i < Sqls.length; i++) {
           sql.replaceQuestion(Sqls[i].toString());
        }

        executeQuery(sql.getSql());
    }

    public Sql genSql() {
        sql = Sql.builder().build();
        return sql;
    }
}

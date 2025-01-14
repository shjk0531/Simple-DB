package com.sh.domain.simpleDb;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Setter
@Getter
@SuperBuilder
public class SimpleDb {
    private final String url;
    private final String username;
    private final String password;
    @Setter
    private boolean devMode;

    private static final Logger logger = Logger.getLogger(SimpleDb.class.getName());

    public SimpleDb(String host, String username, String password, String database) {
        this.url = "jdbc:mysql://" + host + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        this.username = username;
        this.password = password;
        this.devMode = false;
    }

    public Sql genSql() {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            return new Sql(conn, devMode);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating SQL object", e);
        }
    }

    public void run(String sql, Object... params) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParameters(pstmt, params);

            if (devMode) {
                logger.log(Level.INFO, "Executing SQL: " + pstmt);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL: " + sql, e);
        }
    }

    private void setParameters(PreparedStatement pstmt, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof String) {
                pstmt.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) param);
            } else if (param instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) param);
            } else if (param instanceof Timestamp) {
                pstmt.setTimestamp(i + 1, (Timestamp) param);
            } else {
                pstmt.setObject(i + 1, param);
            }
        }
    }
}

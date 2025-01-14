package com.sh.domain.simpleDb;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Setter
@Getter
@SuperBuilder
public class Sql {
    private final StringBuilder queryBuilder = new StringBuilder();
    private final List<Object> parameters = new ArrayList<>();
    private final Connection connection;
    private final boolean devMode;

    private static final Logger logger = Logger.getLogger(Sql.class.getName());

    public Sql(Connection connection, boolean devMode) {
        this.connection = connection;
        this.devMode = devMode;
    }

    public Sql append(String sqlBase, Object... params) {
        queryBuilder.append(sqlBase).append(" ");
        parameters.addAll(Arrays.asList(params));
        return this;
    }

    public long insert() {
        String sql = queryBuilder.toString().trim();
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            if (devMode) {
                logger.log(Level.INFO, "Executing SQL: " + pstmt);
            }

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL 실행 중 오류 발생", e);
        }
        throw new RuntimeException("INSERT 실행 실패, 생성된 키 없음");
    }

    public int update() {
        String sql = queryBuilder.toString().trim();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            if (devMode) {
                logger.log(Level.INFO, "Executing SQL: " + pstmt);
            }

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("SQL 실행 중 오류 발생", e);
        }
    }

    public int delete() {
        String sql = queryBuilder.toString().trim();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            if (devMode) {
                logger.log(Level.INFO, "Executing SQL: " + pstmt);
            }

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("SQL 실행 중 오류 발생", e);
        }
    }
}

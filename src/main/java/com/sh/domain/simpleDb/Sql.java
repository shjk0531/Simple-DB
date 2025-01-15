package com.sh.domain.simpleDb;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
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

    private int executeUpdate() {
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

    public int update() {
        return executeUpdate();
    }

    public int delete() {
        return executeUpdate();
    }

    public List<Map<String, Object>> selectRows() {
        String sql = queryBuilder.toString().trim();
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (devMode) {
                logger.log(Level.INFO, "Executing SQL: " + pstmt);
            }

            List<Map<String, Object>> rows = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);

                    if (value instanceof Timestamp) {
                        value = ((Timestamp) value).toLocalDateTime();
                    }

                    row.put(columnName, value);
                }
                rows.add(row);
            }

            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("SQL 실행 중 오류 발생", e);
        }
    }

    public Map<String, Object> selectRow() {
        String sql = queryBuilder.toString().trim();
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (devMode) {
                logger.log(Level.INFO, "Executing SQL: " + pstmt);
            }

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            if (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);

                    if (value instanceof Timestamp) {
                        value = ((Timestamp) value).toLocalDateTime();
                    }

                    row.put(columnName, value);
                }
                return row;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("SQL 실행 중 오류 발생", e);
        }
    }

    public LocalDateTime selectDatetime() {
        String sql = queryBuilder.toString().trim();
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (devMode) {
                logger.log(Level.INFO, "Executing SQL: " + pstmt);
            }

            if (rs.next()) {
                Timestamp timestamp = rs.getTimestamp(1);
                return timestamp.toLocalDateTime();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("SQL 실행 중 오류 발생", e);
        }
    }
}

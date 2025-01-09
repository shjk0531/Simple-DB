package com.sh.domain.simpleDb.sql.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class Sql {
    private String sql;

    public void genSql() {
        sql = "";
    }

    public Sql append(String appendSql) {
        sql += appendSql;
        return this;
    }

    public Sql append(Object... args) {
        sql = args[0].toString();

        for (int i = 1; i < args.length; i++) {
            sql = sql.replace("?", args[i].toString());
        }

        return this;
    }

    public void replaceQuestion(String replaceSql) {
        sql = sql.replace("?", replaceSql);
    }
}

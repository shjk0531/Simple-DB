package com.sh.domain.simpleDb.sql.service;

import com.sh.domain.simpleDb.sql.entity.Sql;
import com.sh.domain.simpleDb.sql.repository.SqlRepository;

public class SqlService implements SqlRepository {

    @Override
    public Sql replaceQuestion(Sql sql, String str) {
        sql.setSql(sql.getSql().replaceFirst("\\?", str));
        return sql;
    }

}

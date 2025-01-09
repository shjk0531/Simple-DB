package com.sh.domain.simpleDb.sql.repository;

import com.sh.domain.simpleDb.sql.entity.Sql;

public interface SqlRepository {
    public Sql replaceQuestion(Sql sql, String str);
}

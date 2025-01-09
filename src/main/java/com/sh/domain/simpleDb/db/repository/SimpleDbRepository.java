package com.sh.domain.simpleDb.db.repository;

import com.sh.domain.simpleDb.sql.entity.Sql;

public interface SimpleDbRepository {
    public Sql replaceQuestion(String str);
}

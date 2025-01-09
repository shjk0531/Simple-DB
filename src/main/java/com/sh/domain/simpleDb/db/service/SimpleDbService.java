package com.sh.domain.simpleDb.db.service;

import com.sh.domain.simpleDb.db.repository.SimpleDbRepository;

public class SimpleDbService {
    private final SimpleDbRepository simpleDbRepository;


    public SimpleDbService(SimpleDbRepository simpleDbRepository) {
        this.simpleDbRepository = simpleDbRepository;
    }
}

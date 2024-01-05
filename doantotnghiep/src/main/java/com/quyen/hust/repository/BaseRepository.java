package com.quyen.hust.repository;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public abstract class BaseRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

}
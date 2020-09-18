/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Phani
 */
@Repository
public class JdbcTemplateRepositary {

    private static final Logger logger = LogManager.getLogger(JdbcTemplateRepositary.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long saveDataIntoDB(String header, String query) {
        try {
            logger.info("{}>>query:[{}]", header, query);
            KeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update((Connection connection) -> {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                return ps;
            }, holder);
            return holder.getKey().longValue();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:saveDataIntoDB:query:[{}],Error:[{}]", header, query, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

}

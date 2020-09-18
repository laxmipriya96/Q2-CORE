package com.vm.qsmart2core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Qsmart2CoreApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(Qsmart2CoreApplication.class, args);
    }
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("-->"+jdbcTemplate.getDataSource().getConnection().isClosed());
    }

}

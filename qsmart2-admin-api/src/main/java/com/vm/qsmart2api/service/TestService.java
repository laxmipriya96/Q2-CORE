/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2api.configuration.RuntimeBeanBuilder;
import com.vm.qsmart2api.contoller.AuthController;
import java.util.Date;
import java.util.Properties;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Service;
import static org.springframework.web.server.adapter.WebHttpHandlerBuilder.applicationContext;

/**
 *
 * @author Phani
 */
@Service
public class TestService {

    @Autowired
    private ConfigurableEnvironment environment;

    @Autowired
    ApplicationContext context;

    public void doSomething() {
        ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) context;
        SingletonBeanRegistry beanRegistry = configContext.getBeanFactory();
        beanRegistry.registerSingleton("UserService", new UserService());
        beanRegistry.registerSingleton("AuthController", new AuthController());
        String[] beans = context.getBeanDefinitionNames();
        for (String s : beans) {
            System.out.println("Bean Name : " + s);
        }

    }

}

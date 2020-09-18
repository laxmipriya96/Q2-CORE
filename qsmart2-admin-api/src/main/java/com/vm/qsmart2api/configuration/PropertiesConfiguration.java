/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author VMHDCLAP030
 */
@Configuration
public class PropertiesConfiguration {

    @Bean
    public PropertyPlaceholderConfigurer properties() {
         String path = System.getProperty("user.dir") + File.separator + "config" + File.separator +"application.properties";
        System.out.println("Path :" + path);
        
        final PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
//        ppc.setIgnoreUnresolvablePlaceholders(true);
        ppc.setIgnoreResourceNotFound(true);
        ppc.setFileEncoding("UTF-8");
        final List<Resource> resourceLst = new ArrayList<>();

        // resourceLst.add(new ClassPathResource("myapp_base.properties"));
        resourceLst.add(new FileSystemResource(path));
        // resourceLst.add(new ClassPathResource(System.getProperty("user.dir") + File.separator + "config/" + File.separator + "messages.properties"  ));
        // resourceLst.add(new ClassPathResource("myapp_developer_overrides.properties")); // for Developer debugging.

        ppc.setLocations(resourceLst.toArray(new Resource[]{}));

        return ppc;
    }
}

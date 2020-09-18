/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.configuration;

import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author ASHOK
 */
@Component
public class WebUtils {

    private static final Logger logger = LogManager.getLogger(WebUtils.class);

    private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        logger.info("================setRequset=================");
        this.request = request;
    }

    public String getClientIpAddress() {
        try {
            logger.info("Enter:getClientIpAddress");
            String ipAddress = request.getRemoteAddr();
            String ipAddress1 = request.getRemoteHost();
            String ipAddress2 = request.getRemoteUser();
            logger.info("getRemoteAddr:[{}]", ipAddress);
            logger.info("getRemoteHost:[{}]", ipAddress1);
            logger.info("getRemoteUser:[{}]", ipAddress2);
            return ipAddress;
        } catch (Exception e) {
            logger.error("Error:getClientIpAddress:[{}]", e.getMessage());
            return " ";
        }
    }
}

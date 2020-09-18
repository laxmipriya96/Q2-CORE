/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2api.dtos.AuditDetails;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ASHOK
 */
@Service
public class AuditService {

    private static final Logger logger = LogManager.getLogger(AuditService.class);

    @Autowired
    DateUtils dateUtils;

    public void saveAuditDetails(String header, AuditDetails auditDetails) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void saveAuditDetails(String header, long userId, String action, AuditStatus status) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>:header:[{}]:userId:[{}]:action:[{}]:status:[{}]", header, userId, action, status);
            }
            AuditDetails auditDetails = new AuditDetails();
            auditDetails.setUserId(userId);
            auditDetails.setAction(action);
            auditDetails.setCreatedOn(dateUtils.getdate());
            auditDetails.setStatus(status.getMsg());
            if (logger.isDebugEnabled()) {
                logger.info("<<<<:header:[{}]:AuditDetails:[{}]", header, auditDetails);
            }
            // auditRepository.save(auditDetails);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.info(">>:header:[{}]:userId:[{}]:action:[{}]:status:[{}]", header, userId, action, status);
            }
            e.printStackTrace();
        }
    }

    public void saveAuditDetails(String header, Long userId, String action, AuditStatus status, String failurReason) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>:header:[{}]:userId:[{}]:action:[{}]:status:[{}]:failurReason:[{}]", header, userId, action, status, failurReason);
            }
            AuditDetails auditDetails = new AuditDetails();
            auditDetails.setUserId(userId);
            auditDetails.setAction(action);
            auditDetails.setCreatedOn(dateUtils.getdate());
            auditDetails.setStatus(status.getMsg());
            auditDetails.setFailurReason(failurReason);
            logger.info("<<:header:[{}]:AuditDetails:[{}]", header, auditDetails);
            // auditRepository.save(auditDetails);
        } catch (Exception e) {
            logger.error(">>:header:[{}]:userId:[{}]:action:[{}]:status:[{}]:failurReason:[{}]", header, userId, action, status, failurReason);
            e.printStackTrace();
        }

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.activedirectory.DirectoryGDto;
import com.vm.qsmart2api.dtos.global.ActiveDirectorySettings;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.ActiveDirectoryService;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.UserService;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Phani
 */
@RestController
@RequestMapping("activedirectory/")
public class ActiveDirectoryController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    MessageSource messageSource;

    @Autowired
    ActiveDirectoryService adService;

    @Autowired
    UserService userService;
    
    @Autowired
    AuditService auditService;

    @PostMapping(path = "{userId}/validate",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> validateActiveDirectoryDetails(
            Locale locale,
            @PathVariable("userId") long userId,
            @RequestHeader(value = "tranid", defaultValue = "VALIDATE_CONNECTION") String tranId, 
            @RequestBody ActiveDirectorySettings settings) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tranId).append("_").append(userId).append("] ");
        String header = sb.toString().toUpperCase();
        Response sResponse = null;
        logger.info("{}>>:loginId:[{}]:settings:{}", header, userId, settings);
        try {
            sResponse = adService.validateADConnection(header, locale, settings);
            logger.info("{}<<:validateActiveDirectoryDetails:Response:{}", header, sResponse.isStatus());
            auditService.saveAuditDetails(header, userId, AuditMessage.ActiveDirectoryValid.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(sResponse);
        } catch (Exception e) {
            logger.error("{}Excep:validateActiveDirectoryDetails:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            sResponse = new Response(false, messageSource.getMessage("common.wrong.message", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.ActiveDirectoryValidFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            logger.info("{}<<:validateActiveDirectoryDetails:Response:[{}]", header, sResponse);
            return new ResponseEntity(sResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            sb = null;
            header = null;
            sResponse = null;
        }
    }

    @GetMapping(path = "{userId}/check/{enterpriesid}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> checkActiveDirectoryConnecti(
            Locale locale,
            @PathVariable("userId") long userId,
            @PathVariable("enterpriesid") long enterpriseId,
            @RequestHeader(value = "tranid", defaultValue = "CHECK_AD_CONNECTION") String tranId) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tranId).append("_").append(userId).append("] ");
        String header = sb.toString().toUpperCase();
        Response sResponse = null;
        logger.info("{}>>:checkActiveDirectoryConnection:loginId:[{}]:enterpriseId:{}", header, userId, enterpriseId);
        try {
            sResponse = adService.checkActiveDirectoryConnection(header, userId, locale, enterpriseId);
            logger.info("{}<<:checkActiveDirectoryConnection:Response:{}", header, sResponse.isStatus());
            auditService.saveAuditDetails(header, userId, AuditMessage.ActiveDirectoryCheck.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(sResponse);
        } catch (Exception e) {
            logger.error("{}Excep:checkActiveDirectoryConnection:userId:[{}]:enterpriseId:[{}]:Error:{}", header, userId, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            sResponse = new Response(false, messageSource.getMessage("common.wrong.message", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.ActiveDirectoryCheckFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            logger.info("{}<<:checkActiveDirectoryConnection:Response:[{}]", header, sResponse);
            return new ResponseEntity(sResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            sb = null;
            header = null;
            sResponse = null;
        }
    }

    @GetMapping(path = "{userId}/userinfo/{enterpriesid}/{username}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<DirectoryGDto> getUsersFromDirectoryByName(
            Locale locale,
            @PathVariable("userId") long userId,
            @PathVariable("enterpriesid") long enterpriseId,
            @PathVariable("username") String name,
            @RequestHeader(value = "tranid", defaultValue = "GET_ADUSER_INFO") String tranId) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tranId).append("_").append(userId).append("] ");
        String header = sb.toString().toUpperCase();
        DirectoryGDto directory = null;
        DirectoryGDto srResponse = null;
        logger.info("{}>>:getUsersFromDirectoryByName:loginId:[{}]:enterpriseId:[{}]:UserName:[{}]", header, userId, enterpriseId, name);
        try {
            if (userService.validationUserName(header, name)) {
                //if (true) {
                directory = adService.getUserFromDirectoryByName(header, userId, name, locale, enterpriseId);
                logger.info("{}<<:getUsersFromDirectoryByName:Response:{}", header, (directory != null));
                logger.trace("{}<<:getUsersFromDirectoryByName:Response:{}", header, directory);
                auditService.saveAuditDetails(header, userId, AuditMessage.ActiveDirectoryGet.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(directory);
            } else {
                srResponse = new DirectoryGDto(false, messageSource.getMessage("user.ctrl.username.exists", null, locale));
                logger.info("{}<<:getUsersFromDirectoryByName:Response:{}", header, (srResponse != null));
                logger.trace("{}<<:getUsersFromDirectoryByName:Response:{}", header, srResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.ActiveDirectoryGetFail.getMsg(), AuditStatus.FAILURE, AuditMessage.UserNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(srResponse);
            }
        } catch (Exception e) {
            logger.error("{}Excep:getUsersFromDirectoryByName:userId:{}:enterpriseId:{}:Error:{}", header, userId, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
           auditService.saveAuditDetails(header, userId, AuditMessage.ActiveDirectoryGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DirectoryGDto(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;
            directory = null;
            srResponse = null;
        }
    }

}

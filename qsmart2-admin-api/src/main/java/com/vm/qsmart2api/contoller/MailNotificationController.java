/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.mailnotification.MailCrtDto;
import com.vm.qsmart2api.dtos.mailnotification.MailGetDto;
import com.vm.qsmart2api.dtos.mailnotification.MailUpDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.MailNotificationService;
import java.util.Locale;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tejasri
 */
@RestController
@RequestMapping("mail-notification/")
public class MailNotificationController {
    
    public static final Logger logger = LogManager.getLogger(MailNotificationController.class);
    
    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;
    
    @Autowired
    MailNotificationService mailNotificationSer;
    
    @PostMapping(path = "{userId}/create/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createMailNotification(Locale locale,
            @Valid @RequestBody MailCrtDto mailDto,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_MAILNOTIFICATION") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{}:enterpriseId:{}:mailDto:{}", header, userId, enterpriseId, (mailDto != null));
            if (mailNotificationSer.validateTriggerId(header, enterpriseId, mailDto.getTiggerId())) {
                Long mailNotfcnId = mailNotificationSer.saveMailNotificationInDb(header, userId, mailDto, enterpriseId);
                if (mailNotfcnId > 0) {
                    response = new CustomResponse(true, messageSource.getMessage("mailnotifcation.ctrl.create", null, locale), mailNotfcnId);
                    logger.info("{} <<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.MailNotificationCreate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("mailnotifcation.ctrl.createfail", null, locale));
                    logger.info("{}<<:createMailNotification:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.MailNotificationCreateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("mailnotifcation.ctrl.triggerid.exists", null, locale));
                logger.info("{} <<:createMailNotification:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MailNotificationCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.MailNotificationTrigggerFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createMailNotification:userId:{}:enterpriseId:{}:mailDto:{}:Error:{}", header, userId, enterpriseId, mailDto, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.MailNotificationCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }
    @PutMapping(path = "{userId}/update/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateMailNotification(Locale locale,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody MailUpDto mail,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_MAILNOTIFICATION") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{}:enterpriseId:{}:mail:{}", header, userId, (mail != null));
            if (mailNotificationSer.validateTriggerIdByMailNotfId(header, enterpriseId, mail.getNotificationMailId(), mail.getTiggerId())) {
                Long mailNotfcnId = mailNotificationSer.updateMailNotificationInDb(header, userId, enterpriseId, mail);
                if (mailNotfcnId > 0) {
                    response = new CustomResponse(true, messageSource.getMessage("mailnotifcation.ctrl.update", null, locale), mailNotfcnId);
                    logger.info("{}<<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.MailNotificationUpdate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("mailnotifcation.ctrl.updatefail", null, locale));
                    logger.info("{}<<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.MailNotificationUpdateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("mailnotifcation.ctrl.triggerid.exists", null, locale));
                logger.info("{}<<Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MailNotificationUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.MailNotificationTrigggerFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

        } catch (Exception e) {
            logger.error("{}Excep:updateMailNotification:userId:{}:enterpriseId:{}:mail:{}:Error:{}", header, userId, enterpriseId, mail, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.MailNotificationUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }
@GetMapping(path = "{userId}/all/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<MailGetDto> getAllMailNotificationsByEntId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_MAILNOTIFICATIONS") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:enterpriseId:[{}]", header, enterpriseId);
            MailGetDto mail = mailNotificationSer.getAllMailNotificationsByEntId(header, userId, enterpriseId);
            logger.info("{}<<:Response:Size:[{}]", header, mail.getMailNotifications().size());
            auditService.saveAuditDetails(header, userId, AuditMessage.MailNotificationGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(mail);
        } catch (Exception e) {
            logger.error("{}Excep:getAllMailNotificationsByEntId:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.MailNotificationGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            logger.info("{}<<:Response:Size:[{}]", header, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MailGetDto());
        } finally {
            sb = null;
            header = null;
        }
    }
}

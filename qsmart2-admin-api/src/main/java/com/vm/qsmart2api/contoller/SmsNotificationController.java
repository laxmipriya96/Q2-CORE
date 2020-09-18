/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.sms.NotificationSmsDTO;
import com.vm.qsmart2api.dtos.sms.NotificationSmsGDTO;
import com.vm.qsmart2api.dtos.sms.NotificationSmsUDTO;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.enums.Status;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.NotificationSmsService;
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
 * @author Ashok
 */
@RestController
@RequestMapping("sms-notification/")
public class SmsNotificationController {

    public static final Logger logger = LogManager.getLogger(BranchController.class);

    @Autowired
    NotificationSmsService notificationSmsService;

    @Autowired
    AuditService auditService;

    @Autowired
    MessageSource messageSource;

    @GetMapping(path = "{userId}/all/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<NotificationSmsGDTO> getAllNotificationsByEnterpriseId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_NOTIFICATION_SMS") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:enterpriseId:[{}]", header, enterpriseId);
            NotificationSmsGDTO notificationSmsGDTO = notificationSmsService.getAllNotificationSmsByEnterpriseId(header, userId, enterpriseId);
            logger.info("{}<<:Response:NotificationSms:[{}]", header, notificationSmsGDTO);
            auditService.saveAuditDetails(header, userId, AuditMessage.NotificationSmsGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(notificationSmsGDTO);
        } catch (Exception e) {
            logger.error("{}Excep:getAllNotificationByEnterpriseId:locationId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.NotificationSmsGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            logger.info("{}<<:Response:Size:[{}]", header, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NotificationSmsGDTO());
        } finally {
            sb = null;
            header = null;
        }
    }

    @PostMapping(path = "{userId}/create/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createNotificationSms(Locale locale,
            @Valid @RequestBody NotificationSmsDTO notificationSmsDTO,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_NOTIFICATION_SMS") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();

            logger.info("{}>>header:{},enterpriseId:{},NotificationSms:{}", header, enterpriseId, (notificationSmsDTO != null));
            if (notificationSmsService.validateNotificationSmsWithTriggerId(header, enterpriseId, notificationSmsDTO.getTriggerId())) {
                long branchId = notificationSmsService.save(header, userId, enterpriseId, notificationSmsDTO);
                if (branchId > 0) {
                    response = new CustomResponse(true, messageSource.getMessage("notificationsms.ctrl.create", null, locale), branchId);
                    logger.info("{}<<:createNotificationSms:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.NotificationSmsCreate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("notificationsms.ctrl.createfail", null, locale));
                    logger.info("{}<<:createNotificationSms:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.NotificationSmsCreateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("notificationsms.ctrl.trigger.exists", null, locale));
                logger.info("{}<<:createNotificationSms:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.NotificationSmsCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.NotificationSmsFailByEnterpriseAndTrigger.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createBranch:header:{}:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("notificationsms.ctrl.fail", null, locale));
            logger.info("{}<<:createNotificationSms:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.NotificationSmsCreateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateNotificationSms(Locale locale,
            @Valid @RequestBody NotificationSmsUDTO notificationSmsUDTO,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_NOTIFICATION_SMS") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();

            logger.info("{}>>header:{},enterpriseId:{},NotificationSms:{}", header, enterpriseId, (notificationSmsUDTO != null));
            if (notificationSmsService.validateNotificationSmsWithTriggerIdByNotificationId(header, enterpriseId, notificationSmsUDTO.getNotificationSmsId(), notificationSmsUDTO.getTriggerId())) {
                long branchId = notificationSmsService.update(header, userId, enterpriseId, notificationSmsUDTO);
                if (branchId > 0) {
                    response = new CustomResponse(true, messageSource.getMessage("notificationsms.ctrl.update", null, locale), branchId);
                    logger.info("{}<<:updateNotificationSms:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.NotificationSmsUpdate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("notificationsms.ctrl.updatefail", null, locale));
                    logger.info("{}<<:updateNotificationSms:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.NotificationSmsUpdateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("notificationsms.ctrl.trigger.exists", null, locale));
                logger.info("{}<<:updateNotificationSms:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.NotificationSmsUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.NotificationSmsFailByEnterpriseAndTrigger.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:updateNotificationSms:header:{}:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("notificationsms.ctrl.fail", null, locale));
            logger.info("{}<<:updateNotificationSms:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.NotificationSmsUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

}

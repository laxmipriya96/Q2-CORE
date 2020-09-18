/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.mailnotification.MailSmsDto;
import com.vm.qsmart2api.dtos.params.ParamGetDto;
import com.vm.qsmart2api.dtos.trigger.TriggerGetDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.TriggerService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tejasri
 */
@RestController
@RequestMapping("trigger/")
public class TriggerTypeController {
    public static final Logger logger = LogManager.getLogger(TriggerTypeController.class);
    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;
    
    @Autowired
    TriggerService triggerService;
    
    @GetMapping(path = "{userId}/allTriggers/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<TriggerGetDto> getAllTriggerTypes(@RequestParam(value = "notificationType", required = false) String notificationType,
            Locale locale,
            @PathVariable("userId") Long userId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @RequestHeader(value = "tranId", defaultValue = "GET_TRIGGERS") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>getAllTriggers:Request:[{}]", header, userId);
            TriggerGetDto trigger = triggerService.getAllTriggers(header,userId);
            logger.info("{}<<getAllTriggers:Response:{}", header, trigger);
            auditService.saveAuditDetails(header, userId, AuditMessage.TriggerGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(trigger);
        } catch (Exception e) {
            logger.error("{}Excep:getAllTriggers:Error:{}", header, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.TriggerGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<getAllTriggers:Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TriggerGetDto());
        } finally {
            sb = null;
            header = null;
        }
    }
    
    @GetMapping(path = "{userId}/allParams/{triggerId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ParamGetDto> getAllParams(
            Locale locale,
            @PathVariable("userId") Long userId,
            @PathVariable("triggerId") Long triggerId,
            @RequestHeader(value = "tranId", defaultValue = "GET_PARAMS") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>getAllParams:Request:[{}]:triggerId:[{}]", header, userId,triggerId);
            ParamGetDto param = triggerService.getAllParams(header,userId,triggerId);
            logger.info("{}<<getAllParams:Response:{}", header, param);
            auditService.saveAuditDetails(header, userId, AuditMessage.ParamGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(param);
        } catch (Exception e) {
            logger.error("{}Excep:getAllParams:triggerId:{}:Error:{}", header,triggerId,e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ParamGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<getAllParams:Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ParamGetDto());
        } finally {
            sb = null;
            header = null;
        }
    }
    
    
    @PostMapping(path = "{userId}/test-msg/{enterpriseid}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> sendSampleMessage(@RequestParam("test-type") String testType,Locale locale,
            @Valid @RequestBody MailSmsDto mailDto,
            @PathVariable("userId") Long userId,
            @PathVariable("enterpriseid") Long enterpriseId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_MAILSMS") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{}:enterpriseId:{}:mailDto:{}", header, userId, (mailDto != null));
                 mailDto = triggerService.saveMailOrSms(header, userId, mailDto);
                if (mailDto != null) {
                    response = new Response(true, messageSource.getMessage("notification.test.msg", null, locale));
                    logger.info("{}<<:Response:{}:mailDto:{}", header, response, mailDto);
                    auditService.saveAuditDetails(header, userId, AuditMessage.TestMsg.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    response = new Response(false, messageSource.getMessage("notification.test.msg.fail", null, locale));
                    logger.info("{}<<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.TestMsgFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            
        } catch (Exception e) {
            logger.error("{}Excep:sendSampleMessage:userId:{}:mailDto:{}:Error:{}", header, userId, mailDto, e.getMessage());
            response = new Response(false, messageSource.getMessage("notification.test.msg.fail", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.TestMsgFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }
}

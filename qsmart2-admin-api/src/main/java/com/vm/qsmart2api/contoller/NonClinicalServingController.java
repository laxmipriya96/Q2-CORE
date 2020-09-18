/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.token.ServingTokenRequest;
import com.vm.qsmart2api.dtos.token.TokensInfoResponse;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.NonclinicalTokenServingService;
import com.vm.qsmart2api.service.PharmacyTokenServingService;
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
 * @author AC Sekhar
 */
@RestController
@RequestMapping("non-clinical-serving/")
public class NonClinicalServingController {

    private final Logger logger = LogManager.getLogger(NonClinicalServingController.class);

    @Autowired
    AuditService auditService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    NonclinicalTokenServingService nonclinicalTokenServingService;

    @GetMapping(path = "{userId}/tokens-info/{serviceId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<TokensInfoResponse> getTokensInfo(
            Locale locale,
            @PathVariable("userId") Long userId,
            @PathVariable("serviceId") Long serviceId,
            @RequestHeader(value = "tranId", defaultValue = "Tokens_Info") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:userId:[{}],serviceId:[{}]", header, userId, serviceId);
            TokensInfoResponse tokens = nonclinicalTokenServingService.getTokensInfoByServiceId(header, userId, serviceId);
            logger.trace("{}<<Response:{}", header, tokens);
            auditService.saveAuditDetails(header, userId, AuditMessage.GettingTokens.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        } catch (Exception e) {
            logger.error("{}Excep:getTokensInfo:userId:{}:Error:{}", header, userId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.GettingTokensFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<getTokensInfo:Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TokensInfoResponse());
        } finally {
            sb = null;
            header = null;
        }
    }

    @PostMapping(path = "{userId}/token-action",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> tokenAction(Locale locale,
            @PathVariable("userId") Long userId,
            @RequestParam(required = true, name = "actionTye") String actionType,
            @Valid @RequestBody ServingTokenRequest servintToken,
            @RequestHeader(value = "tranId", defaultValue = "CALL_TOKEN_SERVING") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{},actionType:{},ServingTokenData:{}:", header, userId, actionType, servintToken);
            return nonclinicalTokenServingService.performActionOnTOken(header, servintToken, userId, locale, actionType);
        } catch (Exception e) {
            logger.error("{}Excep:tokenAction:userId:{},ServingTokenData:{},Error:{}", header, userId, servintToken, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ActionFailed.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

}

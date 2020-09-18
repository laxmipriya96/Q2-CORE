/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.service.NurseServingStatsRespponse;
import com.vm.qsmart2api.dtos.token.ServingTokenRequest;
import com.vm.qsmart2api.dtos.token.TokensInfoResponse;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.NurseTokenServingService;
import java.util.ArrayList;
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
 * @author Phani
 */
@RestController
@RequestMapping("nurse-serving/")
public class NurseServingController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    AuditService auditService;

    @Autowired
    NurseTokenServingService nurseServingService;

    @Autowired
    MessageSource messageSource;

//    @GetMapping(path = "{userId}/nurse-serving-stats/{serviceId}",
//            produces = {"application/json", "application/xml"})
//    public ResponseEntity<NurseServingStatsRespponse> getServingStatsByServicewise(
//            Locale locale,
//            @PathVariable("userId") long userId,
//            @PathVariable("serviceId") long serviceId,
//            @RequestHeader(value = "tranid", defaultValue = "GET_SERVICE_DETAILS") String tranId) {
//        StringBuilder sb = new StringBuilder();
//        String header = null;
//        List<NurseServingStats> dto = null;
//        try {
//            sb.append("[").append(tranId).append("_").append(userId).append("] ");
//            header = sb.toString().toUpperCase();
//            logger.info("{}>>:getAllServiceDetails():{}", header);
//            dto = new ArrayList<NurseServingStats>() {
//                {
//                    add(new NurseServingStats("Total Appointments", 50));
//                    add(new NurseServingStats("Patients checked-In", 20));
//                    add(new NurseServingStats("Patients Waiting", 30));
//                    add(new NurseServingStats("Average Waiting time", 15));
//                    add(new NurseServingStats("Average Care time", 15));
//                }
//            };
//            logger.info("{}<<Response:{}:dto:[{}]", header, dto);
//            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceDetailsGet.getMsg(), AuditStatus.SUCCESS);
//            return new ResponseEntity(new NurseServingStatsRespponse(dto), HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("{}Excep:getServingStatsByServicewise:userId:{},serviceId:{},Error:{}", header, userId, serviceId, e.getMessage());
//            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceDetailsGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
//            return new ResponseEntity(new NurseServingStatsRespponse(), HttpStatus.INTERNAL_SERVER_ERROR);
//        } finally {
//            sb = null;
//            header = null;
//            dto = null;
//        }
//    }
    
    @GetMapping(path = "{userId}/nurse-serving-stats/{serviceId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<NurseServingStatsRespponse> getServingStatsByServicewise(
            Locale locale,
            @PathVariable("userId") long userId,
            @PathVariable("serviceId") long serviceId,
            @RequestHeader(value = "tranid", defaultValue = "GET_SERVICE_DETAILS") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:serviceId:[{}]", header, serviceId);
            NurseServingStatsRespponse nurseServingDetails = nurseServingService.getNurseServingDetails(header, userId, serviceId);
            logger.info("{}<<Response:NurseServingDetails:[{}]", header, nurseServingDetails);
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceDetailsGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(nurseServingDetails);
        } catch (Exception e) {
            logger.error("{}Excep:getServingStatsByServicewise:userId:{},serviceId:{},Error:{}", header, userId, serviceId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceDetailsGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NurseServingStatsRespponse(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;
        }
    }
    
    

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
            TokensInfoResponse tokens = nurseServingService.getTokensInfoByServiceId(header, userId, serviceId);
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

//    @PostMapping(path = "{userId}/serving-call",
//            produces = {"application/json", "application/xml"})
//    public ResponseEntity<CustomResponse> servingToken(Locale locale,
//            @PathVariable("userId") Long userId,
//            @Valid @RequestBody ServingTokenRequest servintToken,
//            @RequestHeader(value = "tranId", defaultValue = "CALL_TOKEN_SERVING") String tranId) {
//        String header = null;
//        StringBuilder sb = new StringBuilder();
//        CustomResponse response = null;
//        try {
//            sb.append("[").append(tranId).append("_").append(userId).append("] ");
//            header = sb.toString().toUpperCase();
//            logger.info("{}>>userId:{},ServingTokenData:{}", header, userId, servintToken);
//            return nurseServingService.callTokenForServing(header, servintToken, userId, locale);
//        } catch (Exception e) {
//            logger.error("{}Excep:servingToken:userId:{},ServingTokenData:{},Error:{}", header, userId, servintToken, ExceptionUtils.getRootCauseMessage(e));
//            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
//            logger.info("{}<<:Response:[{}]", header, response);
//            auditService.saveAuditDetails(header, userId, AuditMessage.ActionFailed.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        } finally {
//            sb = null;
//            header = null;
//            response = null;
//        }
//    }
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
            return nurseServingService.performActionOnTOken(header, servintToken, userId, locale, actionType);
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

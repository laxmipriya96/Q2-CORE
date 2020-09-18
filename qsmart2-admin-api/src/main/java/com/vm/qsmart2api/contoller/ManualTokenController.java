/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.manualtoken.ManualTokenDTO;
import com.vm.qsmart2api.dtos.manualtoken.ManualTokenResponse;
import com.vm.qsmart2api.dtos.manualtoken.PatientServingGDTO;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.ManualTokenService;
import java.util.Locale;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * @author Ashok
 */
@RestController
@RequestMapping("manualtoken/")
public class ManualTokenController {

    public static final Logger logger = LogManager.getLogger(ManualTokenController.class);

    @Autowired
    ManualTokenService generateManualTokenService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    @PostMapping(path = "{userId}/generate/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ManualTokenResponse> generateManualToken(
            Locale locale,
            @Valid @RequestBody ManualTokenDTO manualTokenDTO,
            @PathVariable("userId") Long userId,
            @PathVariable("locationId") Long locationId,
            @RequestHeader(value = "tranId", defaultValue = "GENERATE_MANUALTOKEN") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        ManualTokenResponse manualTokenResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>locationId:[{}]:ManualTokenDTO:[{}]", header, locationId, manualTokenDTO);
            manualTokenResponse = generateManualTokenService.generateManualToken(header, userId, locationId, manualTokenDTO, locale);
            if (manualTokenResponse.isStatus()) {
                logger.info("{}<<:Response:{}", header, manualTokenResponse);
                if (isLogEnabled) {
                    logger.info("{}<<:Response:{}:Template:{}", header, manualTokenResponse, manualTokenResponse.getTokenTemplate());
                }
                auditService.saveAuditDetails(header, userId, AuditMessage.ManualTokenCreate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(manualTokenResponse);
            } else {
                logger.info("{}<<:Response:{}", header, manualTokenResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.ManualTokenCreateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(manualTokenResponse);
            }
        } catch (Exception e) {
            logger.error("{}Excep:generateManualToken:header:{}:locationId:{}:ManualTokenDTO:{}Error:{}", header, locationId, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
            manualTokenResponse = new ManualTokenResponse();
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.fail", null, locale));
            logger.info("{}<<:Response:[{}]", header, manualTokenResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.ManualTokenCreateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(manualTokenResponse);
        }

    }

    @GetMapping(path = "{userId}/search/{mrnNo}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<PatientServingGDTO> searchMrnNo(
            Locale locale,
            @PathVariable("userId") Long userId,
            @PathVariable("mrnNo") String mrnNo,
            @RequestHeader(value = "tranId", defaultValue = "SEARCH_MRNNO") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        PatientServingGDTO patientServingGDTO = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>mrnNo:[{}]", header, mrnNo);
            patientServingGDTO = generateManualTokenService.searchMrnNo(header, userId, mrnNo);
            patientServingGDTO.setStatus(true);
            if (!patientServingGDTO.getPatientServingDTOs().isEmpty()) {
                patientServingGDTO.setMessages(messageSource.getMessage("search.mrnNo.found", null, locale));
            } else {
                patientServingGDTO.setMessages(messageSource.getMessage("search.mrnNo.not.found", null, locale));
            }
            logger.info("{}<<:Response:{}", header, patientServingGDTO);
            auditService.saveAuditDetails(header, userId, AuditMessage.SearchMrnNoFound.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.CREATED).body(patientServingGDTO);
        } catch (Exception e) {
            logger.error("{}Excep:searchMrnNo:mrnNo:{}:Error:{}", header, mrnNo, ExceptionUtils.getRootCauseMessage(e));
            patientServingGDTO.setStatus(false);
            patientServingGDTO.setMessages(messageSource.getMessage("search.mrnNo.not.found", null, locale));
            logger.info("{}<<:Response:[{}]", header, patientServingGDTO);
            auditService.saveAuditDetails(header, userId, AuditMessage.SearchMrnNoNotFound.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(patientServingGDTO);
        }
    }

    @GetMapping(path = "{userId}/checkin/{serviceBookedId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ManualTokenResponse> checkIn(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestParam("accompanyVisitors") int accompanyVisitors,
            @PathVariable("serviceBookedId") Long serviceBookedId,
            @RequestHeader(value = "tranId", defaultValue = "CHECKIN_TOKEN") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        ManualTokenResponse manualTokenResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>serviceBookedId:[{}]:accompanyVisitors:[{}]", header, serviceBookedId, accompanyVisitors);
            manualTokenResponse = generateManualTokenService.checkInByServiceBookedId(header, userId, serviceBookedId, locale);
            if (manualTokenResponse != null) {
                manualTokenResponse.setStatus(true);
                manualTokenResponse.setMessages(messageSource.getMessage("token.ctrl.checkin.success", null, locale));
                logger.info("{}<<Response:{}", header, manualTokenResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.TokenCheckInSuccess.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(manualTokenResponse);
            } else {
                manualTokenResponse.setStatus(false);
                manualTokenResponse.setMessages(messageSource.getMessage("token.ctrl.checkin.fail", null, locale));
                logger.info("{}<<Response:{}", header, manualTokenResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.TokenCheckInFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(manualTokenResponse);
            }
        } catch (Exception e) {
            logger.error("{}Excep:checkIn:serviceBookedId:{}:Error:{}", header, serviceBookedId, ExceptionUtils.getRootCauseMessage(e));
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("token.ctrl.checkin.fail", null, locale));
            logger.info("{}<<Response:[{}]", header, manualTokenResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.TokenCheckInFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(manualTokenResponse);
        }

    }

    @GetMapping(path = "{userId}/reprint/{serviceBookedId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ManualTokenResponse> rePrintToken(
            Locale locale,
            @PathVariable("userId") Long userId,
            @PathVariable("serviceBookedId") Long serviceBookedId,
            @RequestHeader(value = "tranId", defaultValue = "REPRINT_TOKEN") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        ManualTokenResponse manualTokenResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>serviceBookedId:[{}]", header, serviceBookedId);
            manualTokenResponse = generateManualTokenService.rePrintTokenByServiceId(header, userId, serviceBookedId);
            if (manualTokenResponse != null) {
                manualTokenResponse.setStatus(true);
                manualTokenResponse.setMessages(messageSource.getMessage("token.ctrl.reprint.success", null, locale));
                logger.info("{}<<Response:{}", header, manualTokenResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.TokenRePrintSuccess.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(manualTokenResponse);
            } else {
                manualTokenResponse.setStatus(false);
                manualTokenResponse.setMessages(messageSource.getMessage("token.ctrl.reprint.fail", null, locale));
                logger.info("{}<<Response:{}", header, manualTokenResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.TokenRePrintFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(manualTokenResponse);
            }

        } catch (Exception e) {
            logger.error("{}Excep:rePrintToken:serviceBookedId:{}:Error:{}", header, serviceBookedId, ExceptionUtils.getRootCauseMessage(e));
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("token.ctrl.reprint.fail", null, locale));
            logger.info("{}<<:Response:[{}]", header, manualTokenResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.TokenRePrintFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(manualTokenResponse);
        }

    }

}

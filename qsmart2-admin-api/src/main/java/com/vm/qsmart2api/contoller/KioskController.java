/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.feedback.FeedBackDto;
import com.vm.qsmart2api.dtos.kiosk.KioskAppointmentGDTO;
import com.vm.qsmart2api.dtos.kiosk.KioskThemeDto;
import com.vm.qsmart2api.dtos.manualtoken.ManualTokenResponse;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.KioskService;
import com.vm.qsmart2api.service.ManualTokenService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ashok
 */
@RestController
@RequestMapping("kiosk-flow/")
public class KioskController {

    private static final Logger logger = LogManager.getLogger(KioskController.class);

    @Autowired
    KioskService kioskService;

    @Autowired
    ManualTokenService generateManualTokenService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @GetMapping(path = "{mrnNo}/{kioskIdentifier}",
            produces = {"application/json", "application/xml"})

    public ResponseEntity<KioskAppointmentGDTO> getAppointmentsByMrnNoandKioskId(Locale locale,
            @PathVariable("mrnNo") String mrnNo, @PathVariable("kioskIdentifier") String kioskIdentifier,
            @RequestHeader(value = "tranId", defaultValue = "GET_APPOINTMENTS_BY_KIOSK") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = sb.append("[").append(tranId).append("_").append(kioskIdentifier).append("] ").toString().toUpperCase();
        try {
            logger.info("{}>>userId:{},mrnNo:{},kioskIdentifier:{}", header, mrnNo, kioskIdentifier);
            KioskAppointmentGDTO dto = kioskService.getAppointmentsByMrnNoAndKioskId(header, mrnNo, kioskIdentifier, locale);
            if (dto.isStatus()) {
                auditService.saveAuditDetails(header, 0, AuditMessage.GetAppointmentsByMrnNoAndKiosk.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(dto);
            } else {
                auditService.saveAuditDetails(header, 0, AuditMessage.GetAppointmentsByMrnNoAndKioskFail.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(dto);
            }
        } catch (Exception e) {
            logger.error("{}Excep:getAppointmentsByMrnNoandKioskId:mrnNo:{},kioskIdentifier:{}Error:{}", header, mrnNo, kioskIdentifier, e.getMessage());
            logger.info("{}<<:Response:[{}]", header);
            auditService.saveAuditDetails(header, (long) 0, AuditMessage.GetAppointmentsByMrnNoAndKioskFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new KioskAppointmentGDTO());
        }

    }

    @GetMapping(path = "checkin/{serviceBookedId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ManualTokenResponse> checkIn(
            Locale locale,
            @PathVariable("serviceBookedId") Long serviceBookedId,
            @RequestHeader(value = "tranId", defaultValue = "CHECKIN_TOKEN") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        ManualTokenResponse manualTokenResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(serviceBookedId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>serviceBookedId:[{}]", header, serviceBookedId);
            manualTokenResponse = generateManualTokenService.checkInByServiceBookedIdByKiosk(header, serviceBookedId, locale);
            if (manualTokenResponse != null) {
                manualTokenResponse.setStatus(true);
                manualTokenResponse.setMessages(messageSource.getMessage("token.ctrl.checkin.success", null, locale));
                logger.info("{}<<Response:{}", header, manualTokenResponse);
                auditService.saveAuditDetails(header, 0, AuditMessage.TokenCheckInSuccess.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(manualTokenResponse);
            } else {
                manualTokenResponse.setStatus(false);
                manualTokenResponse.setMessages(messageSource.getMessage("token.ctrl.checkin.fail", null, locale));
                logger.info("{}<<Response:{}", header, manualTokenResponse);
                auditService.saveAuditDetails(header, 0, AuditMessage.TokenCheckInFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(manualTokenResponse);
            }
        } catch (Exception e) {
            logger.error("{}Excep:checkIn:serviceBookedId:{}:Error:{}", header, serviceBookedId, ExceptionUtils.getRootCauseMessage(e));
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("token.ctrl.checkin.fail", null, locale));
            logger.info("{}<<Response:[{}]", header, manualTokenResponse);
            auditService.saveAuditDetails(header, (long) 0, AuditMessage.TokenCheckInFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(manualTokenResponse);
        }

    }

    @GetMapping(path = "{mrnNo}/{kioskIdentifier}/generatetoken/{servingType}",
            produces = {"application/json", "application/xml"})

    public ResponseEntity<ManualTokenResponse> generateTokenByMrnNoAndKiosk(Locale locale,
            @PathVariable("mrnNo") String mrnNo, @PathVariable("kioskIdentifier") String kioskIdentifier,
            @PathVariable("servingType") String servingType,
            @RequestHeader(value = "tranId", defaultValue = "GENERATE_TOKEN_BY_KIOSK") String tranId) {
        StringBuilder sb = new StringBuilder();
        ManualTokenResponse manualTokenResponse;
        String header = sb.append("[").append(tranId).append("_").append(kioskIdentifier).append("] ").toString().toUpperCase();
        try {
            logger.info("{}>>mrnNo:{},kioskIdentifier:{}:servingType:{}", header, mrnNo, kioskIdentifier, servingType);
            manualTokenResponse = kioskService.generateTokenByMrnNoAndKiosk(header, mrnNo, kioskIdentifier, servingType, locale);
            if (manualTokenResponse.isStatus()) {
                logger.info("{}<<:Response:{}:Template:{}", header, manualTokenResponse, manualTokenResponse.getTokenTemplate());
                auditService.saveAuditDetails(header, 0, AuditMessage.KioskTokenCreate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(manualTokenResponse);
            } else {
                logger.info("{}<<:Response:{}", header, manualTokenResponse);
                auditService.saveAuditDetails(header, 0, AuditMessage.KioskTokenCreateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(manualTokenResponse);
            }
        } catch (Exception e) {
            logger.error("{}Excep:generateTokenByMrnNoAndKiosk:mrnNo:{},kioskIdentifier:{}Error:{}", header, mrnNo, kioskIdentifier, e.getMessage());
            manualTokenResponse = new ManualTokenResponse();
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("kiosktoken.crtl.fail", null, locale));
            logger.info("{}<<:Response:[{}]", header, manualTokenResponse);
            auditService.saveAuditDetails(header, (long) 0, AuditMessage.KioskTokenCreateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(manualTokenResponse);
        }

    }

    @GetMapping(path = "{kioskIdentifier}/theme",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<KioskThemeDto> getThemeByKioskIdentifier(Locale locale,
            @PathVariable("kioskIdentifier") String kioskIdentifier,
            @RequestHeader(value = "tranId", defaultValue = "GET_KIOSK_THEME") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = sb.append("[").append(tranId).append("_").append(kioskIdentifier).append("] ").toString().toUpperCase();
        KioskThemeDto kioskThemeDto = null;
        try {
            logger.info("{}>>kioskIdentifier:{}", header, kioskIdentifier);
            kioskThemeDto = kioskService.getThemeByKioskIdentifier(header, kioskIdentifier);
            if (kioskThemeDto != null) {
                kioskThemeDto.setStatus(true);
                kioskThemeDto.setMessage(messageSource.getMessage("kiosk.theme.found", null, locale));
                logger.info("{}<<:Response:{}:KioskThemeDto:{}", header, kioskThemeDto);
                auditService.saveAuditDetails(header, 0, AuditMessage.GetThemeByKioskSuccuss.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(kioskThemeDto);
            } else {
                kioskThemeDto.setStatus(false);
                kioskThemeDto.setMessage(messageSource.getMessage("kiosk.theme.not.foound", null, locale));
                logger.info("{}<<:Response:{}:KioskThemeDto:{}", header, kioskThemeDto);
                auditService.saveAuditDetails(header, 0, AuditMessage.GetThemeByKioskFail.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(kioskThemeDto);
            }

        } catch (Exception e) {
            kioskThemeDto.setStatus(false);
            kioskThemeDto.setMessage(messageSource.getMessage("kiosk.theme.not.foound", null, locale));
            logger.error("{}Excep:getThemeByKioskIdentifier:kioskIdentifier:{}Error:{}", header, kioskIdentifier, e.getMessage());
            auditService.saveAuditDetails(header, (long) 0, AuditMessage.GetThemeByKioskFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new KioskThemeDto());
        }

    }
    
    
    @PostMapping(path = "/feedback",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createFeedBack(Locale locale,
            @Valid @RequestBody FeedBackDto feedBackDto,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_FEEDBACK") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>FeedBackDto:{}", header, (feedBackDto != null));
            response = new CustomResponse(true, messageSource.getMessage("feedback.ctrl.create", null, locale));
            logger.info("{}<<:createFeedBack:Response:{}", header, response);
            auditService.saveAuditDetails(header, 0, AuditMessage.FeedBackCreate.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("{}Excep:createFeedBack:header:{}:FeedBackDto:{}:Error:{}", header, feedBackDto, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("feedback.ctrl.createfail", null, locale));
            logger.info("{}<<:createFeedBack:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, (long) 0, AuditMessage.FeedBackCreateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

}

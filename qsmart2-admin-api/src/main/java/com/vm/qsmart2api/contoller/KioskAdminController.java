/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2.model.TblKiosk;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.StatusDto;
import com.vm.qsmart2api.dtos.display.DisplayResponse;
import com.vm.qsmart2api.dtos.kiosk.KioskCrtDto;
import com.vm.qsmart2api.dtos.kiosk.KioskGetResponse;
import com.vm.qsmart2api.dtos.kiosk.KioskUpDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.repository.KioskRepository;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.KioskService;
import com.vm.qsmart2api.service.ManualTokenService;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * @author Phani
 */
@RestController
@RequestMapping("kiosk/")
public class KioskAdminController {

    private static final Logger logger = LogManager.getLogger(KioskAdminController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @Autowired
    KioskService kioskService;
    
    @Autowired
    ManualTokenService generateManualTokenService;

    @PostMapping(path = "{userId}/create/{enterpriseId}/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createDisplay(Locale locale,
            @Valid @RequestBody KioskCrtDto kisokDto,
            @PathVariable("userId") Long userId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_KIOSK") String tranId,
            @PathVariable("locationId") Long locationId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{},locationId:{},enterpriseId:{},kioskDto:{}", header, userId, locationId, enterpriseId, (kisokDto != null));
            if (kioskService.validateKioskName(header, locationId, kisokDto.getKioskName())) {
                Long kioskId = kioskService.saveKioskInDb(header, userId, kisokDto, locationId, enterpriseId);
                if (kioskId > 0) {
                    response = new CustomResponse(true, messageSource.getMessage("kiosk.ctrl.create", null, locale), kioskId);
                    logger.info("{} <<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.KioskCreate.getMsg(), AuditStatus.SUCCESS.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("kiosk.ctrl.createfail", null, locale));
                    logger.info("{}<<:createKiosk:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.KioskCreateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("kiosk.ctrl.name.exists", null, locale));
                logger.info("{} <<:createKiosk:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.KioskCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.DisplayNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createKiosk:userId:{},locationId:{},enterpriseId:{},KioskDto:{},Error:{}", header, userId, locationId, enterpriseId, kisokDto, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.KioskCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{enterpriseId}/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updatekiosk(Locale locale,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody KioskUpDto kiosk,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_KIOSK") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @PathVariable("locationId") Long locationId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{},locationId:{},kioskDto:{}", header, userId, (kiosk != null));
            if (kioskService.validatekioskNameByKisokId(header, locationId, kiosk.getKioskId(), kiosk.getKioskName())) {
                Long displayId = kioskService.updateKisokInDb(header, userId, locationId, enterpriseId, kiosk);
                if (displayId > 0) {
                    response = new CustomResponse(true, messageSource.getMessage("kiosk.ctrl.update", null, locale), displayId);
                    logger.info("{}<<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.KioskUpdate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("kiosk.ctrl.updatefail", null, locale));
                    logger.info("{}<<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.KioskUpdateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("kiosk.ctrl.name.exists", null, locale));
                logger.info("{}<<Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.KioskUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.KioskUpdateFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

        } catch (Exception e) {
            logger.error("{}Excep:updatekiosk:userId:{},locationId:{},enterpriseId:{},kiosk:{},Error:{}", header, userId, locationId, enterpriseId, kiosk, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.DisplayUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

    @Autowired
    KioskRepository kioskRepo;

    @DeleteMapping(path = "{userId}/delete/{kioskId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> deleteKioskById(
            Locale locale,
            @PathVariable("kioskId") Long kioskId, @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "DELETE_KIOSK") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>Request:[{}]", header, kioskId);
            kioskRepo.deleteById(kioskId);
            response = new Response(true, messageSource.getMessage("kiosk.ctrl.delete", null, locale));
            logger.trace("{}<<Response:{}", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.KioskDelete.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("{}Excep:deleteKioskById:userId:{},Error:{}", header, userId, e.getMessage());
            response = new Response(false, messageSource.getMessage("kiosk.ctrl.faildelete", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.KioskDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userId}/all/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<KioskGetResponse> getAllKiosksByLocationId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_DISPLAY_BOARDS") String tranId,
            @PathVariable("locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>locationId:[{}]", header, locationId);
            KioskGetResponse kResponse = kioskService.getAllKiosksByLocationId(header, userId, locationId);
            logger.trace("{}<<Response:{}", header, kResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.KioskGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(kResponse);
        } catch (Exception e) {
            logger.error("{}Excep:getAllKiosksByLocationId:userId:{}:locationId:{}:Error:{}", header, userId, locationId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.KioskGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new KioskGetResponse(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;
        }
    }

    @Value("${kiosk.context.path}")
    private String contextPath;

    @GetMapping(path = "{userId}/url/{kioskid}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> getUrlByKioskId(
            Locale locale,
            @PathVariable("userId") int userId,
            @PathVariable("kioskid") Long kioskid,
            @RequestHeader(value = "tranId", defaultValue = "GENERATE_KIOSK_URL") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        String url = null;
        Response response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>UserId:[{}],userid:[{}],kioskid{}", header, userId, kioskid);
            Optional<TblKiosk> optional = kioskRepo.findById(kioskid);
            if (optional.isPresent()) {
                url = contextPath + "" + optional.get().getKioskIdentifier();
                response = new Response(true, url);
                logger.info("{}<<Response:{},generated url{},kioskid{}", header, response, url, kioskid);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new Response(false, messageSource.getMessage("kiosk.ctrl.notfound", null, locale));
                logger.info("{}<<:Response:{},kioskid{}", header, response, kioskid);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:getUrlByKioskId:KioskId:{}Error:{}", header, kioskid, ExceptionUtils.getRootCauseMessage(e));
            response = new Response(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<Response:{},KioskId:[{}]", header, response, kioskid);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            url = null;
            response = null;
        }
    }
    
    
    @PutMapping(path = "{userId}/status-update/{kioskId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateKioskStatus(
            Locale locale,
            @PathVariable("kioskId") long kioskId,
            @PathVariable("userId") long userId,
            @RequestBody StatusDto dto,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_KIOSK_STATUS") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse cResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>kioskId:{}:status:{}", header, kioskId, dto);
            kioskId = kioskService.updateStatus(header, kioskId, dto.getIsActive(), userId);
            if (kioskId > 0) {
                cResponse = new CustomResponse(true, messageSource.getMessage("kiosk.ctrl.activate", null, locale), kioskId);
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.KioskStatusUpdate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(cResponse);
            } else {
                cResponse = new CustomResponse(false, messageSource.getMessage("kiosk.ctrl.activatefail", null, locale), kioskId);
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.KioskStatusUpdateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(cResponse);
            }
        } catch (Exception ex) {
            logger.info("{}Excep:updateKioskStatus:{}:kioskId:{}:{}:Eroro:{}", header, kioskId, ExceptionUtils.getRootCauseMessage(ex));
            cResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.KioskStatusUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(ex));
            logger.info("{} << Resposne:{}", header, cResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cResponse);
        }
    }
}

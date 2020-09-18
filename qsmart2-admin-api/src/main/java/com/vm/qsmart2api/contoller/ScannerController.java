/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.ScannerService;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.StatusDto;
import com.vm.qsmart2api.dtos.scanner.ScannerCrtDto;
import com.vm.qsmart2api.dtos.scanner.ScannerGetDto;
import com.vm.qsmart2api.dtos.scanner.ScannerUpDto;
import com.vm.qsmart2api.repository.ScannerRepository;
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
 * @author Tejasri
 */
@RestController
@RequestMapping("scanner/")
public class ScannerController {
    private static final Logger logger = LogManager.getLogger(ScannerController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;
    
    @Autowired
    ScannerService scannerService;
    
    @Autowired
    ScannerRepository scannerRepository;
    
    @PostMapping(path = "{userId}/create/{enterpriseId}/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createScanner(Locale locale,
            @Valid @RequestBody ScannerCrtDto scannerDto,
            @PathVariable("userId") Long userId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_SCANNER") String tranId,
            @PathVariable("locationId") Long locationId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{},locationId:{},enterpriseId:{},scannerDto:{}", header, userId, locationId, enterpriseId, (scannerDto != null));
            if (scannerService.validateScannerName(header, locationId, scannerDto.getScannerName())) {
                Long scannerId = scannerService.saveScannerInDb(header, userId, scannerDto, locationId, enterpriseId);
                if (scannerId > 0) {
                    response = new CustomResponse(true, messageSource.getMessage("scanner.ctrl.create", null, locale), scannerId);
                    logger.info("{} <<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.ScannerCreate.getMsg(), AuditStatus.SUCCESS.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("scanner.ctrl.createfail", null, locale));
                    logger.info("{}<<:createScanner:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.ScannerCreateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("scanner.ctrl.name.exists", null, locale));
                logger.info("{} <<:createScanner:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ScannerCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ScannerNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createScanner:userId:{},locationId:{},enterpriseId:{},scannerDto:{},Error:{}", header, userId, locationId, enterpriseId, scannerDto, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ScannerCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{enterpriseId}/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateScanner(Locale locale,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody ScannerUpDto scanner,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_SKANNER") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @PathVariable("locationId") Long locationId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{},locationId:{},scanner:{}", header, userId, (scanner != null));
            if (scannerService.validateScannerNameByScannerId(header, locationId, scanner.getScannerId(), scanner.getScannerName())) {
                Long scannerId = scannerService.updateScannerInDb(header, userId, locationId, enterpriseId, scanner);
                if (scannerId > 0) {
                    response = new CustomResponse(true, messageSource.getMessage("scanner.ctrl.update", null, locale), scannerId);
                    logger.info("{}<<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.ScannerUpdate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("scanner.ctrl.updatefail", null, locale));
                    logger.info("{}<<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.ScannerUpdateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("scanner.ctrl.name.exists", null, locale));
                logger.info("{}<<Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ScannerUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ScannerNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

        } catch (Exception e) {
            logger.error("{}Excep:updateScanner:userId:{},locationId:{},enterpriseId:{},scanner:{},Error:{}", header, userId, locationId, enterpriseId, scanner, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ScannerUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

    @DeleteMapping(path = "{userId}/delete/{scannerId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> deleteScannerById(
            Locale locale,
            @PathVariable("scannerId") Long scannerId, @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "DELETE_SCANNER") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>Request:[{}]", header, scannerId);
            scannerRepository.deleteById(scannerId);
            response = new Response(true, messageSource.getMessage("scanner.ctrl.delete", null, locale));
            logger.trace("{}<<Response:{}", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ScannerDelete.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("{}Excep:deleteScannerById:userId:{},Error:{}", header, userId, e.getMessage());
            response = new Response(false, messageSource.getMessage("scanner.ctrl.faildelete", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.ScannerDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userId}/all/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ScannerGetDto> getAllScannersByLocationId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_SCANNERS") String tranId,
            @PathVariable("locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>locationId:[{}]", header, locationId);
            ScannerGetDto scannerResp = scannerService.getAllScannersByLocationId(header, userId, locationId);
            logger.trace("{}<<Response:{}", header, scannerResp);
            auditService.saveAuditDetails(header, userId, AuditMessage.ScannerGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(scannerResp);
        } catch (Exception e) {
            logger.error("{}Excep:getAllScannersByLocationId:userId:{}:locationId:{}:Error:{}", header, userId, locationId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ScannerGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ScannerGetDto(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/status-update/{scannerId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateScannerStatus(
            Locale locale,
            @PathVariable("scannerId") long scannerId,
            @PathVariable("userId") long userId,
            @RequestBody StatusDto dto,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_SCANNER_STATUS") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse cResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>scannerId:{}:status:{}", header, scannerId, dto);
            scannerId = scannerService.updateStatus(header, scannerId, dto.getIsActive(), userId);
            if (scannerId > 0) {
                cResponse = new CustomResponse(true, messageSource.getMessage("scanner.ctrl.activate", null, locale), scannerId);
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.ScannerStatusUpdate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(cResponse);
            } else {
                cResponse = new CustomResponse(false, messageSource.getMessage("scanner.ctrl.activatefail", null, locale), scannerId);
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.ScannerStatusUpdateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(cResponse);
            }
        } catch (Exception ex) {
            logger.info("{}Excep:updateScannerStatus:{}:scannerId:{}:{}:Eroro:{}", header, scannerId, ExceptionUtils.getRootCauseMessage(ex));
            cResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.ScannerStatusUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(ex));
            logger.info("{} << Resposne:{}", header, cResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cResponse);
        }
    }
}

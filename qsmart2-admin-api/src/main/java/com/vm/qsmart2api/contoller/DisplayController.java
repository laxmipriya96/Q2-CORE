/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2.model.TblDisplay;
import com.vm.qsmart2api.service.AuditService;
import java.util.Locale;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.StatusDto;
import com.vm.qsmart2api.dtos.display.DisplayCrtDto;
import com.vm.qsmart2api.dtos.display.DisplayResponse;
import com.vm.qsmart2api.dtos.display.DisplayUpDto;
import com.vm.qsmart2api.dtos.display.ThemeGetDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.repository.DisplayRepository;
import com.vm.qsmart2api.service.DisplayService;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
 * @author Tejasri
 */
@RestController
@RequestMapping("display/")
public class DisplayController {

    private static final Logger logger = LogManager.getLogger(DisplayController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @Autowired
    DisplayService displayService;
    
    @Autowired
    DisplayRepository displayRepository;

    @PostMapping(path = "{userId}/create/{enterpriseId}/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createDisplay(Locale locale,
            @Valid @RequestBody DisplayCrtDto displayDto,
            @PathVariable("userId") Long userId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_DISPLAY") String tranId,
            @PathVariable("locationId") Long locationId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{},locationId:{},enterpriseId:{},displayDto:{}", header, userId, locationId, enterpriseId, (displayDto != null));
            if (displayService.validateDisplayName(header, locationId, displayDto.getDisplayName())) {
                Long displayId = displayService.saveDisplayboardInDb(header, userId, displayDto, locationId, enterpriseId);
                if (displayId > 0) {
                    response = new CustomResponse(true, messageSource.getMessage("display.ctrl.create", null, locale), displayId);
                    logger.info("{} <<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.DisplayCreate.getMsg(), AuditStatus.SUCCESS.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("display.ctrl.createfail", null, locale));
                    logger.info("{}<<:createDisplay:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.DisplayCreateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("display.ctrl.name.exists", null, locale));
                logger.info("{} <<:createDisplay:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.DisplayCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.DisplayNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createDisplay:userId:{},locationId:{},enterpriseId:{},displayDto:{},Error:{}", header, userId, locationId, enterpriseId, displayDto, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.DisplayCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{enterpriseId}/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateDisplayBoard(Locale locale,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody DisplayUpDto display,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_DISPLAY") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @PathVariable("locationId") Long locationId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{},locationId:{},display:{}", header, userId, (display != null));
            if (displayService.validateDisplayNameByDisplayId(header, locationId, display.getDisplayId(), display.getDisplayName())) {
                Long displayId = displayService.updateDisplayBoardInDb(header, userId, locationId, enterpriseId, display);
                if (displayId > 0) {
                    response = new CustomResponse(true, messageSource.getMessage("display.ctrl.update", null, locale), displayId);
                    logger.info("{}<<:update:header:{}:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.DisplayUpdate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("display.ctrl.updatefail", null, locale));
                    logger.info("{}<<:update:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.DisplayUpdateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("display.ctrl.name.exists", null, locale));
                logger.info("{}<<:updateRoom:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.DisplayUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.DisplayNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

        } catch (Exception e) {
            logger.error("{}Excep:updateDisplay:userId:{},locationId:{},enterpriseId:{},display:{},Error:{}", header, userId,locationId, enterpriseId, display, e.getMessage());
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
    
    @PutMapping(path = "{userId}/status-update/{displayId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateDisplayStatus(
            Locale locale,
            @PathVariable("displayId") long displayId,
            @PathVariable("userId") long userId,
            @RequestBody StatusDto dto,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_DISPLAY_STATUS") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse cResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>displayId:{}:status:{}", header, displayId, dto);
            displayId = displayService.updateStatus(header, displayId, dto.getIsActive(), userId);
            if (displayId > 0) {
                cResponse = new CustomResponse(true, messageSource.getMessage("display.ctrl.activate", null, locale), displayId);
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.DisplayStatusUpdate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(cResponse);
            } else {
                cResponse = new CustomResponse(false, messageSource.getMessage("display.ctrl.activatefail", null, locale), displayId);
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.DisplayStatusUpdateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(cResponse);
            }
        } catch (Exception ex) {
            logger.info("{}Excep:updateDisplayStatus:{}:displayId:{}:{}:Eroro:{}", header, displayId, ExceptionUtils.getRootCauseMessage(ex));
            cResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.DisplayStatusUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(ex));
            logger.info("{} << Resposne:{}", header, cResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cResponse);
        }
    }
    
    @DeleteMapping(path = "{userId}/delete/{displayId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> deleteDisplayBoardById(
            Locale locale,
            @PathVariable("displayId") Long displayId, @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "DELETE_DISPLAYBOARD") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>Request:[{}]", header, displayId);
            displayRepository.deleteById(displayId);
            response = new Response(true, messageSource.getMessage("display.ctrl.delete", null, locale));
            logger.trace("{}<<Response:{}", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.DisplayDelete.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("{}Excep:deleteDisplayBoardById:userId:{},Error:{}", header, userId, e.getMessage());
            response = new Response(false, messageSource.getMessage("display.ctrl.faildelete", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.DisplayDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;

        }
    }
    
    
    @GetMapping(path = "{userId}/all/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<DisplayResponse> getAllDisplaysByLocationId(
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
            DisplayResponse displays = displayService.getAllDisplaysByLocationId(header, userId, locationId);
            logger.trace("{}<<Response:{}", header, displays);
            auditService.saveAuditDetails(header, userId, AuditMessage.DisplayGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(displays);
        } catch (Exception e) {
            logger.error("{}Excep:getAllDisplaysByLocationId:userId:{}:locationId:{}:Error:{}", header, userId, locationId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.DisplayGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DisplayResponse());
        } finally {
            sb = null;
            header = null;
        }
    }
    
    
    @Value("${display.context.path}")
    private String contextPath;

    @GetMapping(path = "{userId}/url/{displayid}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> getUrlByDisplayId(
            Locale locale,
            @PathVariable("userId") int userId,
            @PathVariable("displayid") Long displayid,
            @RequestHeader(value = "tranId", defaultValue = "GENERATE_DISPLAY_URL") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        String url = null;
        Response response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>UserId:[{}],userid:[{}],displayid{}", header, userId, displayid);
            Optional<TblDisplay> optional = displayRepository.findById(displayid);
            if (optional.isPresent()) {
                url = contextPath + "" + optional.get().getDisplayIdentifier();
                response = new Response(true, url);
                logger.info("{}<<Response:{},generated url{},displayid:{}", header, response, url, displayid);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new Response(false, messageSource.getMessage("display.ctrl.notfound", null, locale));
                logger.info("{}<<Response:{},kioskid{}", header, response, displayid);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:getUrlByDisplayId:KioskId:{}Error:{}", header, displayid, ExceptionUtils.getRootCauseMessage(e));
            response = new Response(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<Response:{},KioskId:[{}]", header, response, displayid);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            url = null;
            response = null;
        }
    }
    
    @GetMapping(path = "{userId}/themes/all",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ThemeGetDto> getAllThemes(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_THEMES") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:Request:[{}]", header, userId);
            ThemeGetDto branch = displayService.getAllThemes(header,userId);
            logger.info("{}<<getAllThemes:Response:{}", header, branch);
            auditService.saveAuditDetails(header, userId, AuditMessage.ThemeGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(branch);
        } catch (Exception e) {
            logger.error("{}Excep:getAllThemes:Error:{}", header, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ThemeGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<getAllThemes:Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThemeGetDto());
        } finally {
            sb = null;
            header = null;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2.model.TblApptType;
import com.vm.qsmart2api.dtos.AuditDetails;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.appttype.ApptTypeCrtDto;
import com.vm.qsmart2api.dtos.appttype.ApptTypeDto;
import com.vm.qsmart2api.dtos.appttype.ApptTypeGetDto;
import com.vm.qsmart2api.dtos.appttype.UserApptType;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.repository.ApptTypeRepository;
import com.vm.qsmart2api.service.ApptTypeService;
import com.vm.qsmart2api.service.AuditService;
import java.util.Locale;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tejasri
 */
@RestController
@RequestMapping("appttype/")
public class ApptTypeController {

    public static final Logger logger = LogManager.getLogger(ApptTypeController.class);

    @Autowired
    ApptTypeService apptTypeSer;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @Autowired
    ApptTypeRepository apptTypeRepos;

    @Autowired
    ApplicationContext applicationContext;

    @GetMapping(path = "{userId}/getAll/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ApptTypeGetDto> getAllApptTypesByLocationId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_APPTTYPES") String tranId,
            @PathVariable("locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:userId:{},locationId:[{}]", header, userId, locationId);
            ApptTypeGetDto apptTypes = apptTypeSer.getAllApptTypesByLocationId(header, userId, locationId);
            logger.info("{}<<:Response:{}", header, apptTypes);
            auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(apptTypes);
        } catch (Exception e) {
            logger.error("{}Excep:getAllApptTypesByLocationId:userId:{},locationId:[{}],Error:{}", header, userId, locationId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApptTypeGetDto());
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userId}/all/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ApptTypeGetDto> getApptTypesByBranchId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestParam("branchId") Long branchId,
            @RequestHeader(value = "tranId", defaultValue = "GET_APPTTYPES") String tranId,
            @PathVariable("locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:userId:{},locationId:[{}]", header, userId, locationId);
            ApptTypeGetDto apptTypes = apptTypeSer.getAllApptTypesByBranchId(header, userId, locationId, branchId);
            logger.info("{}<<:Response:{}", header, apptTypes);
            auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(apptTypes);
        } catch (Exception e) {
            logger.error("{}Excep:getApptTypesByBranchId:userId:{},locationId:[{}],Error:{}", header, userId, locationId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApptTypeGetDto());
        } finally {
            sb = null;
            header = null;
        }
    }

    @PostMapping(path = "{userid}/create/{locationId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createApptType(@RequestBody ApptTypeCrtDto apptDto, Locale locale,
            @PathVariable("userid") Long userId,
            @PathVariable("locationId") Long locationId,
            @RequestHeader(value = "tranid", defaultValue = "CREATE_APPTTYPE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse sResponse = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> userId:{},locationId:{},apptDto:{}", header, userId, locationId, (apptDto != null));
            if (apptTypeSer.validateApptType(header, locationId, apptDto.getApptType())) {
                if (apptTypeSer.validateApptCode(header, locationId, apptDto.getApptCode())) {
                    Long apptTypeId = apptTypeSer.save(header, userId, locationId, apptDto);
                    if (apptTypeId > 0) {
                        sResponse = new CustomResponse(true, messageSource.getMessage("appttype.ctrl.create", null, locale), apptTypeId);
                        logger.info("{} <<:Response:{}", header, sResponse);
                        auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeCreate.getMsg(), AuditStatus.SUCCESS);
                        return ResponseEntity.status(HttpStatus.CREATED).body(sResponse);
                    } else {
                        sResponse = new CustomResponse(false, messageSource.getMessage("appttype.ctrl.createfail", null, locale));
                        logger.info("{} <<:Response:{}", header, sResponse);
                        auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeCreateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(sResponse);
                    }
                } else {
                    sResponse = new CustomResponse(false, messageSource.getMessage("appttype.ctrl.code.exists", null, locale));
                    logger.info("{}<<:createApptType:Response:{}", header, sResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ApptCodeFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(sResponse);
                }
            } else {
                sResponse = new CustomResponse(false, messageSource.getMessage("appttype.ctrl.appttype.exists", null, locale));
                logger.info("{}<<:createApptType:Response:{}", header, sResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ApptTypeFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(sResponse);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createApptType:userId:{},locationId:{},apptDto:{},Error:{}", header, userId, locationId, apptDto, e.getMessage());
            sResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, sResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sResponse);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateApptType(Locale locale,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody ApptTypeDto appt,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_APPTTYPE") String tranId,
            @PathVariable("locationId") Long locationId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> userId:{},locationId:{},appt:{}", header, userId, locationId, (appt != null));
            if (apptTypeSer.validateApptTypeByApptTypeId(header, locationId, appt.getApptTypeId(), appt.getApptType())) {
                if (apptTypeSer.validateApptCodeByApptTypeId(header, locationId, appt.getApptTypeId(), appt.getApptCode())) {
                    int apptID = apptTypeSer.updateApptType(header, userId, appt);
                    if (apptID > 0) {
                        response = new CustomResponse(true, messageSource.getMessage("appttype.ctrl.update", null, locale), appt.getApptTypeId());
                        logger.info("{} <<:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeUpdate.getMsg(), AuditStatus.SUCCESS);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("appttype.ctrl.failupdate", null, locale));
                        logger.info("{} <<:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeUpdateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("appttype.ctrl.code.exists", null, locale));
                    logger.info("{}<<:updateBranch:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ApptCodeFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("appttype.ctrl.appttype.exists", null, locale));
                logger.info("{}<<:updateApptType:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ApptTypeFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

        } catch (Exception e) {
            logger.error("{}Excep:updateApptType:locationId:{},appt:{},Error:{}", header, userId, locationId, appt, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

    @DeleteMapping(path = "{userId}/delete",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> deleteByApptTypeId(
            Locale locale,
            @RequestParam("apptTypeId") Long apptTypeId, @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "DELETE_APPTTYPE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:userId:{},apptTypeId:{}", header, userId, apptTypeId);
            TblApptType apptType = apptTypeSer.getAppttypeByApttypeId(header, apptTypeId);
            if ((apptType.getServices() != null && !apptType.getServices().isEmpty()) && (apptType.getUsers() != null && !apptType.getUsers().isEmpty())) {
                response = new Response(false, messageSource.getMessage("appttype.dococtors.services.map", null, locale));
                logger.info("{}<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeDelete.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else if (apptType.getServices() != null && !apptType.getServices().isEmpty()) {
                response = new Response(false, messageSource.getMessage("appttype.services.map", null, locale));
                logger.info("{}<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeDelete.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else if (apptType.getUsers() != null && !apptType.getUsers().isEmpty()) {
                response = new Response(false, messageSource.getMessage("appttype.users.map", null, locale));
                logger.info("{}<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeDelete.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                apptTypeRepos.deleteById(apptTypeId);
                response = new Response(true, messageSource.getMessage("appttype.ctrl.delete", null, locale));
                logger.info("{}<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeDelete.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:deleteByApptTypeId:userId:{},apptTypeId:{},Error:{}", header, userId, apptTypeId, e.getMessage());
            e.printStackTrace();
            response = new Response(false, messageSource.getMessage("appttype.ctrl.faildelete", null, locale));
            logger.info("{}<<:Response:{}", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ApptTypeDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;

        }
    }

    @PutMapping(path = "{userId}/map/doctors-apptType", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> mappingUsersWithApptType(
            Locale locale,
            @Valid @RequestBody UserApptType userApptType,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "MAP_SERVICE_APPTTYPE") String tranId) {

        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:Request:{}", header, userApptType);
            long serviceId = apptTypeSer.mappingUsersWithApptType(header, userId, userApptType);
            if (serviceId > 0) {
                response = new CustomResponse(true, messageSource.getMessage("appttype.ctrl.update", null, locale), serviceId);
                logger.info("{}<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MappingUsersWithApptType.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new CustomResponse(false, messageSource.getMessage("appttype.ctrl.failupdate", null, locale), serviceId);
                logger.info("{}<<:updateApptType:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MappingUsersWithApptTypeFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:mappingUsersWithApptType:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("appttype.ctrl.failupdate", null, locale));
            logger.info("{}<<:mappingUsersWithApptType:Response:[{}]", header, response.isStatus());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }
}

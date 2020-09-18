/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.medservice.MedServiceDTO;
import com.vm.qsmart2api.dtos.medservice.MedServiceUDTO;
import com.vm.qsmart2api.dtos.medservice.MedServiceWithUsers;
import com.vm.qsmart2api.dtos.medservice.UserMedService;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.repository.MedServiceRepository;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.MedServiceService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ashok
 */
@RestController
@RequestMapping("medservice/")
public class MedServiceController {

    private static final Logger logger = LogManager.getLogger(MedServiceController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @Autowired
    MedServiceService medService;

    @Autowired
    MedServiceRepository medServiceRepository;

    @PostMapping(path = "{userId}/create/{enterpriseId}/{locationId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createMedicalService(Locale locale,
            @Valid @RequestBody MedServiceDTO medServiceDTO,
            @PathVariable("userId") Long userId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @PathVariable("locationId") Long locationId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_MEDSERVICE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> enterpriseId:{},locationId:{},medServiceDTO:{}", header, enterpriseId, locationId, (medServiceDTO != null));
            if (medService.validateMedServiceName(header, locationId, medServiceDTO.getMedServiceName())) {
                if (medService.validateMedServiceCode(header, locationId, medServiceDTO.getMedServiceCode())) {
                    Long medServiceId = medService.save(header, userId, enterpriseId, locationId, medServiceDTO);
                    if (medServiceId > 0) {
                        response = new CustomResponse(true, messageSource.getMessage("medservice.ctrl.create", null, locale), medServiceId);
                        logger.info("{} <<:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceCreate.getMsg(), AuditStatus.SUCCESS);
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("medservice.ctrl.createfail", null, locale));
                        logger.info("{} <<:createMedService:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceCreateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("medservice.ctrl.medservicecode.exists", null, locale));
                    logger.info("{}<<:createMedService:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.MedServiceCodeFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("medservice.ctrl.medservicename.exists", null, locale));
                logger.info("{}<<:createMedService:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.MedServiceNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createMedService:enterpriseId:{}:locationId:{}:medServiceDTO:{}:medServiceDTO:{}:Error:{}", header, enterpriseId, locationId, medServiceDTO, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.LocationCreateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{enterpriseId}/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateMedicalService(Locale locale,
            @PathVariable("userId") Long userId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @PathVariable("locationId") Long locationId,
            @Valid @RequestBody MedServiceUDTO medServiceUDTO,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_MEDSERVICE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> enterpriseId:{},locationId:{},medServiceUDTO:{}", header, enterpriseId, locationId, (medServiceUDTO != null));
            if (medService.validateMedServiceNameByMedServiceId(header, medServiceUDTO.getMedServiceId(), locationId, medServiceUDTO.getMedServiceName())) {
                if (medService.validateMedServiceCodeByMedServiceId(header, medServiceUDTO.getMedServiceId(), locationId, medServiceUDTO.getMedServiceCode())) {
                    int id = medService.update(header, userId, enterpriseId, locationId, medServiceUDTO);
                    if (id > 0) {
                        response = new CustomResponse(true, messageSource.getMessage("medservice.ctrl.update", null, locale), medServiceUDTO.getMedServiceId());
                        logger.info("{}<<:updateMedService:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceUpdate.getMsg(), AuditStatus.SUCCESS);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("medservice.ctrl.failupdate", null, locale));
                        logger.info("{}<<:updateMedService:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceUpdateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("medservice.ctrl.medservicecode.exists", null, locale));
                    logger.info("{}<<:updateMedService:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.MedServiceCodeFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("medservice.ctrl.medservicename.exists", null, locale));
                logger.info("{}<<:updateMedService:Response:{}", header, response);

                auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.MedServiceNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

        } catch (Exception e) {
            logger.error("{}Excep:updateMedService:enterpriseId:{},locationId:{},medServiceDTO:{}:Error:{}", header, enterpriseId, locationId, medServiceUDTO, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

    @DeleteMapping(path = "{userId}/delete",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> deleteByMedicalServiceId(
            Locale locale,
            @RequestParam("medServiceId") Long medServiceId, @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "DELETE_MEDSERVICE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>Request:[{}]", header, medServiceId);
            int count = medServiceRepository.checkUsersByMedServiceId(medServiceId).getUsers().size();
            if (count > 0) {
                response = new Response(false, messageSource.getMessage("medservice.ctrl.doctor.mapped", null, locale));
                logger.info("<<:Response", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceDelete.getMsg(), AuditStatus.FAILURE,AuditMessage.MedServiceDeleteFailByMedService.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                medServiceRepository.deleteById(medServiceId);
                response = new Response(true, messageSource.getMessage("medservice.ctrl.delete", null, locale));
                logger.info("<<:Response", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceDelete.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:deleteByMedServiceId:medServiceId:{}:Error:{}", header, medServiceId, ExceptionUtils.getRootCauseMessage(e));
            response = new Response(false, messageSource.getMessage("medservice.ctrl.faildelete", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;

        }
    }

    @GetMapping(path = "{userId}/all/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<MedServiceWithUsers> getAllMedServiceByLocId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_MEDSERVICES") String tranId,
            @PathVariable("locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>header:[{}]:locationId:[{}]", header, locationId);
            MedServiceWithUsers medServiceWithUsers = medService.getAllMedServiceWithUsersByLocationId(header, locationId);
            logger.trace("{}<<Response:{}", header, medServiceWithUsers);
            auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(medServiceWithUsers);
        } catch (Exception e) {
            logger.error("{}Excep:getAllMedServiceWithUsersByLocationId:locationId:{}:Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.MedServiceGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MedServiceWithUsers());
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/map/doctors", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> mappingUsersWithMedService(
            Locale locale,
            @Valid @RequestBody UserMedService userMedService,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "MAP_MEDICAL_SERVICE") String tranId) {

        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            long serviceId = medService.mappingUsersWithMedService(header, userId, userMedService);
            if (serviceId > 0) {
                response = new CustomResponse(true, messageSource.getMessage("medservice.ctrl.update", null, locale), serviceId);
                logger.info("{} <<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MappingUsersWithMedService.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new CustomResponse(false, messageSource.getMessage("medservice.ctrl.failupdate", null, locale), serviceId);
                logger.info("{} <<:mappingUsersWithMedService:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MappingUsersWithMedServiceFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:mappingUsersWithMedService:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("medservice.ctrl.failupdate", null, locale));
            logger.info("{}<<:mappingUsersWithApptType:Response:[{}]", header, response.isStatus());
            auditService.saveAuditDetails(header, userId, AuditMessage.MappingUsersWithMedServiceFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }
}

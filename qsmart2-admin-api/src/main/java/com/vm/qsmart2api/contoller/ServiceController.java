/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

/**
 *
 * @author Ashok
 */
import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2api.dtos.AuditDetails;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.service.ApptServicDto;
import com.vm.qsmart2api.dtos.service.ServiceDTO;
import com.vm.qsmart2api.dtos.service.ServiceGUDTO;
import com.vm.qsmart2api.dtos.service.ServiceGetDto;
import com.vm.qsmart2api.dtos.service.ServiceResponse;
import com.vm.qsmart2api.dtos.service.ServiceUpDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.repository.ServiceRepository;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.ServiceService;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@RestController
@RequestMapping("service/")
public class ServiceController {

    private static final Logger logger = LogManager.getLogger(ServiceController.class);

    @Autowired
    ServiceService servicesSer;

    @Autowired
    ServiceRepository serviceRepo;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @GetMapping(path = "{userId}/getAll/{branchId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ServiceGUDTO> getAllServices(
            Locale locale,
            @PathVariable("userId") long userId,
            @PathVariable("branchId") long branchId,
            @RequestHeader(value = "tranid", defaultValue = "GET_SERVICE") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>header:[{}]:branchId:[{}]", header, branchId);
            ServiceGUDTO service = servicesSer.getAllServicesByBranchId(header, userId, branchId);
            logger.trace("{}<<Response:{}", header, service);
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(service);
        } catch (Exception e) {
            logger.error("{}Excep:getAllServicesByBranchId:userId:{},branchId:{},Error:{}", header, userId, branchId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceGUDTO());
        } finally {
            sb = null;
            header = null;
        }
    }

    @PostMapping(path = "{userId}/create/{branchId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createService(
            Locale locale,
            @Valid @RequestBody ServiceDTO serviceDto,
            @PathVariable("userId") Long userId,
            @PathVariable("branchId") Long branchId,
            @RequestHeader(value = "tranid", defaultValue = "CREATE_SERVICE") String tranId) {

        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();

            logger.info("{}>> userId:{},branchId:{},serviceDto:{}", header, branchId, (serviceDto != null));
            if (servicesSer.validateServiceEngName(header, branchId, serviceDto.getServiceNameEn())) {
                if (servicesSer.validateServiceNameArb(header, branchId, serviceDto.getServiceNameAr())) {
                    long serviceId = servicesSer.saveService(header, userId, branchId, serviceDto);
                    if (serviceId > 0) {
                        response = new CustomResponse(true, messageSource.getMessage("service.ctrl.create", null, locale), serviceId);
                        logger.info("{}<<:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.ServiceCreate.getMsg(), AuditStatus.SUCCESS);
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("service.ctrl.createfail", null, locale), serviceId);
                        logger.info("{}<<:createService:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.ServiceCreateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("service.ctrl.arbname.exists", null, locale));
                    logger.info("{}<<:createService:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.ServiceCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ServiceArbNameFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("service.ctrl.engname.exists", null, locale));
                logger.info("{}<<:createService:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServiceCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ServiceEngNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createService:userId:{},branchId:{},serviceDto:{}:Error:{}", header, branchId, serviceDto, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("service.ctrl.fail", null, locale));

            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{branchId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateService(
            Locale locale,
            @Valid @RequestBody ServiceUpDto serviceUpDTO,
            @PathVariable("userId") long userId,
            @PathVariable("branchId") long branchId,
            @RequestHeader(value = "tranid", defaultValue = "UPDATE_SERVICE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>> userId:{},branchId:{},serviceUDTO:{}", header, branchId, (serviceUpDTO != null));
            if (servicesSer.validateServiceEngNameByServiceId(header, branchId, serviceUpDTO.getServiceId(), serviceUpDTO.getServiceNameEn())) {
                if (servicesSer.validateServiceArbNameByServiceId(header, branchId, serviceUpDTO.getServiceId(), serviceUpDTO.getServiceNameAr())) {
                    long serviceId = servicesSer.updateService(header, userId, serviceUpDTO);
                    if (serviceId > 0) {
                        response = new CustomResponse(true, messageSource.getMessage("service.ctrl.update", null, locale), serviceId);
                        logger.info("{}<<:update:header:{}:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.ServiceUpdate.getMsg(), AuditStatus.SUCCESS);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("service.ctrl.failupdate", null, locale));
                        logger.info("{}<<:update:header:{}:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.ServiceUpdateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("service.ctrl.arbname.exists", null, locale));
                    logger.info("{}<<:updateService:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.ServiceUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ServiceArbNameFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("service.ctrl.engname.exists", null, locale));
                logger.info("{}<<:updateService:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServiceUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ServiceEngNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:updateService:branchId:{},serviceUpDTO:{},Error:{}", header, branchId, serviceUpDTO, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("service.ctrl.fail", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

    @DeleteMapping(path = "{userId}/delete/{serviceId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> deleteByServiceId(
            Locale locale,
            @PathVariable("serviceId") long serviceId, @PathVariable("userId") long userId,
            @RequestHeader(value = "tranid", defaultValue = "DELETE_SERVICE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>serviceId:{}", header, serviceId);
            TblService tblService = servicesSer.getServiceById(header, serviceId);
            if (tblService.getApptTypes() != null && !tblService.getApptTypes().isEmpty()) {
                response = new CustomResponse(false, messageSource.getMessage("service.ctrl.apptypes.map", null, locale));
                logger.info("{}<<Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServiceDelete.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } 
//            else if (tblService.getDisplayBoards() != null && !tblService.getDisplayBoards().isEmpty()) {
//                response = new CustomResponse(false, messageSource.getMessage("service.ctrl.displayboards.map", null, locale));
//                logger.info("{}<<Response:{}", header, response);
//                auditService.saveAuditDetails(header, userId, AuditMessage.ServiceDelete.getMsg(), AuditStatus.FAILURE);
//                return ResponseEntity.status(HttpStatus.OK).body(response);
//            } 
            else {
                serviceRepo.deleteById(serviceId);
                response = new CustomResponse(true, messageSource.getMessage("service.ctrl.delete", null, locale));
                logger.info("{}<<Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServiceDelete.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:deleteByServiceId:userId:{},serviceId:{},Error:{}", header, userId, serviceId, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("service.ctrl.faildelete", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/map/service-appttypes", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> mappingServiceWithApptTypes(
            Locale locale,
            @Valid @RequestBody ApptServicDto serviceDto,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "MAP_SERVICE_APPTTYPE") String tranId) {

        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:Request:{}", header, serviceDto);
            long serviceId = servicesSer.mappingServiceWithApptTypes(header, userId, serviceDto);
            if (serviceId > 0) {
                response = new CustomResponse(true, messageSource.getMessage("service.appttype.ctrl.map", null, locale), serviceId);
                logger.info("{}<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServiceApptTypeMapping.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new CustomResponse(false, messageSource.getMessage("service.appttype.ctrl.mapfail", null, locale), serviceId);
                logger.info("{}<<:createService:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServiceApptTypeMappingFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:mapServiceWithRooms:Error:{}", header, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("service.appttype.ctrl.mapfail", null, locale));
            logger.info("{}<<:mappingServiceWithApptTypes:Response:[{}]", header, response.isStatus());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

    @GetMapping(path = "{userId}/all",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ServiceGetDto> getAllServicesByBranches(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestParam List<Long> branches,
            @RequestHeader(value = "tranId", defaultValue = "GET_SERVICES") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>header:[{}]:branchId:[{}]", header, branches);
            ServiceGetDto services = servicesSer.getAllServicesByBranches(header, userId, branches);
            logger.trace("{}<<Response:{}", header, services);
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(services);
        } catch (Exception e) {
            logger.error("{}Excep:getAllServicesByBranches:branches:{}:Error:{}", header, branches, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<getAllServicesByBranches:Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceGetDto());
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userId}/getAllServices/{branchId}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ServiceResponse> getAllServicesByBranchId(
            Locale locale,
            @PathVariable("userId") long userId,
            @PathVariable("branchId") long branchId,
            @RequestHeader(value = "tranid", defaultValue = "GET_SERVICES") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>header:[{}]:branchId:[{}]", header, branchId);
            ServiceResponse service = servicesSer.getAllServicesByBranchIdInDb(header, userId, branchId);
            logger.trace("{}<<Response:{}", header, service);
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(service);
        } catch (Exception e) {
            logger.error("{}Excep:getAllServicesByBranchIdInDb:userId:{},branchId:{},Error:{}", header, userId, branchId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse());
        } finally {
            sb = null;
            header = null;
        }
    }

}

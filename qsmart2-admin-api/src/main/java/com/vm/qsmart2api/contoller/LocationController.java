/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.AuditDetails;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.StatusDto;
import com.vm.qsmart2api.dtos.location.LocationCrtDto;
import com.vm.qsmart2api.dtos.location.LocationDto;
import com.vm.qsmart2api.dtos.location.LocationGetDto;
import com.vm.qsmart2api.dtos.location.LocationResponse;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.repository.LocationRepository;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.LocationService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tejasri
 */
@RestController
@RequestMapping("location/")
public class LocationController {

    private static final Logger logger = LogManager.getLogger(LocationController.class);

    @Autowired
    LocationRepository locationRepo;

    @Autowired
    LocationService locationService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @PostMapping(path = "{userId}/create/{enterpriseId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createLocation(Locale locale,
            @Valid @RequestBody LocationCrtDto locationDto,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_LOCATION") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> userId:{},enterpriseId:{},locationDto:{}", header, userId, enterpriseId, (locationDto != null));
            if (locationService.validateLocationEngName(header, enterpriseId, locationDto.getLocationNameEn())) {
                if (locationService.validateLocationNameArbInDb(header, enterpriseId, locationDto.getLocationNameAr())) {
                    Long locationId = locationService.save(header, userId, enterpriseId, locationDto);
                    if (locationId > 0) {
                        response = new CustomResponse(true, messageSource.getMessage("location.ctrl.create", null, locale), locationId);
                        logger.info("{} <<:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.LocationCreate.getMsg(), AuditStatus.SUCCESS);
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("location.ctrl.createfail", null, locale));
                        logger.info("{} <<:createLoc            ation:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.LocationCreateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("location.ctrl.arbname.exists", null, locale));
                    logger.info("{}<<:createLocation:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.LocationCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.LocationArbNameFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("location.ctrl.engname.exists", null, locale));
                logger.info("{}<<:createLocation:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.LocationCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.LocationEngNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createLocation:userId:{}:enterpriseId:{}:Location:{}:Error:{}", header, userId, enterpriseId, locationDto, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.LocationCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateLocation(Locale locale,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody LocationDto location,
            @PathVariable("enterpriseId") Long enterpriseId,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_LOCATION") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{},enterpriseId:{},Location:{}", header, userId, enterpriseId, (location != null));
            if (locationService.validateLocationEngNameByLocationId(header, enterpriseId, location.getLocationId(), location.getLocationNameEn())) {
                if (locationService.validateLoactionArbNameByLocationId(header, enterpriseId, location.getLocationId(), location.getLocationNameAr())) {
                    Long locationId = locationService.update(header, userId, location);
                    if (locationId > 0) {
                        response = new CustomResponse(true, messageSource.getMessage("location.ctrl.update", null, locale), locationId);
                        logger.info("{} <<:update:header:{}:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.LocationUpdate.getMsg(), AuditStatus.SUCCESS);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("location.ctrl.updatefail", null, locale));
                        logger.info("{} <<:update:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.LocationUpdateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("location.ctrl.arbname.exists", null, locale));
                    logger.info("{}<<:updateLocation:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.LocationUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.LocationArbNameFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("location.ctrl.engname.exists", null, locale));
                logger.info("{}<<:updateLocation:Response:{}", header, response);

                auditService.saveAuditDetails(header, userId, AuditMessage.LocationUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.LocationEngNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

        } catch (Exception e) {
            logger.error("{}Excep:updateLocation:header:{}:userId:{}:Error:{}", header, userId, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.LocationUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

    @GetMapping(path = "{userId}/all/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<LocationGetDto> getAllLocationsByEntId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_LOCATIONS") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>enterpriseId:[{}]", header, enterpriseId);
            LocationGetDto location = locationService.getAllLocationsByEntId(header, enterpriseId);
            logger.info("{}<<Response:Size:[{}]", header, location.getLocations().size());
            auditService.saveAuditDetails(header, userId, AuditMessage.LocationGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(location);
        } catch (Exception e) {
            logger.error("{}Excep:getAllLocationsByEntId:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.LocationGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            logger.info("{}<<Response:Size:[{}]", header, "0");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LocationGetDto());
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "/{userid}/status-update/{activate-id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateLocationStatus(
            Locale locale,
            @PathVariable("activate-id") long activateId,
            @PathVariable("userid") long userId,
            @RequestBody StatusDto dto,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_LOCATION_STATUS") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse cResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> activateId:{}:status:{}", header, activateId, dto);
            activateId = locationService.updateStatus(header, activateId, dto.getIsActive(), userId);
            if (activateId > 0) {
                cResponse = new CustomResponse(true, messageSource.getMessage("location.ctrl.activate", null, locale), activateId);
                logger.info("{} << response:{}", header, cResponse);
                return ResponseEntity.status(HttpStatus.CREATED).body(cResponse);
            } else {
                cResponse = new CustomResponse(false, messageSource.getMessage("location.ctrl.activatefail", null, locale), activateId);
                logger.info("{} << response:{}", header, cResponse);
                return ResponseEntity.status(HttpStatus.OK).body(cResponse);
            }
        } catch (Exception ex) {
            logger.info("{}Excep:updateLocationStatus:{}:activateId:{}:{}:Eroro:{}", header, activateId, ExceptionUtils.getRootCauseMessage(ex));
            cResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{} << Resposne:{}", header, cResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cResponse);
        }
    }

    @DeleteMapping(path = "{userId}/delete/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> deleteByLocationId(
            Locale locale,
            @PathVariable("locationId") Long locationId, @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "DELETE_LOCATION") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>Request:[{}]", header, locationId);
            locationRepo.deleteById(locationId);
            response = new Response(true, messageSource.getMessage("location.ctrl.delete", null, locale));
            logger.info("<<:[{}]", "Exit", response);
            auditService.saveAuditDetails(header, userId, AuditMessage.LocationDelete.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("{}Excep:deleteByLocationId:Error:{}", header, e.getMessage());
            response = new Response(false, messageSource.getMessage("location.ctrl.faildelete", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.LocationDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;

        }
    }

    @GetMapping(path = "{userId}/validate/{enterpriseId}/{locationNameEn}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> validateLocationEngName(Locale locale,
            @PathVariable("userId") Long userId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @PathVariable("locationNameEn") String locationNameEn,
            @RequestHeader(value = "tranId", defaultValue = "VALIDATE_LOCATION_ENG") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        try {
            System.out.println("locale:::::" + locale);
            System.out.println("tranId:::::" + tranId);
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:validateLocationEngName:enterpriseId:{}:locationNameEn:{}", header, enterpriseId, locationNameEn);
            if (locationService.validateLocationEngName(header, enterpriseId, locationNameEn)) {
                response = new Response(true, messageSource.getMessage("location.ctrl.valid.engname", null, locale));
                logger.info("{}<<:validateLocationEngName:Response:{}", header, response);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new Response(false, messageSource.getMessage("location.ctrl.engname.exists", null, locale));
                logger.info("{}<<:validateLocationEngName:Response:{}", header, response);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:validateLocationEngName:enterpriseId:{}:locationNameEn:{}:Error:{}", header, enterpriseId, locationNameEn, ExceptionUtils.getRootCauseMessage(e));
            response = new Response(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

    @GetMapping(path = "{userId}/validateLocation/{locationNameAr}/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> validateLocationNameArbInDb(Locale locale,
            @PathVariable("userId") Long userId,
            @PathVariable("enterpriseId") Long enterpriseId,
            @PathVariable("locationNameAr") String locationNameAr,
            @RequestHeader(value = "tranId", defaultValue = "VALIDATE_LOCATION_ARB") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:validateLocationNameArbInDb:enterpriseId:{}:locationNameAr:{}", header, enterpriseId, locationNameAr);
            if (locationService.validateLocationNameArbInDb(header, enterpriseId, locationNameAr)) {
                response = new Response(true, messageSource.getMessage("location.ctrl.valid.arbname", null, locale));
                logger.info("{}<<:validateLocationNameArbInDb:Response:{}", header, response);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new Response(false, messageSource.getMessage("location.ctrl.arbname.exists", null, locale));
                logger.info("{}<<:validateLocationNameArbInDb:Response:{}", header, response);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:validateLocationNameArbInDb:enterpriseId:{}:locationNameAr:{}Error:{}", header, enterpriseId, locationNameAr, ExceptionUtils.getRootCauseMessage(e));
            response = new Response(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }
    
    
    @GetMapping(path = "{userId}/allLocations/{enterpriseId}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<LocationResponse> getAllLocationsBranchesAndServsByEntId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_LOCATIONS_BRANCHES_SERVICES") String tranId,
            @PathVariable("enterpriseId") Long enterpriseId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>header:[{}]:enterpriseId:{}:[{}]", header, enterpriseId);
            LocationResponse locations = locationService.getAllLocationsBranchesAndServsByEntId(header, userId, enterpriseId);
            logger.trace("{}<<Response:{}", header, locations);
            auditService.saveAuditDetails(header, userId, AuditMessage.LocationGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(locations);
        } catch (Exception e) {
            logger.error("{}Excep:getAllLocationsBranchesAndServsByEntId:userId:{}:enterpriseId:{}:Error:{}", header, userId,enterpriseId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.LocationGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LocationResponse());
        } finally {
            sb = null;
            header = null;
        }
    }

}

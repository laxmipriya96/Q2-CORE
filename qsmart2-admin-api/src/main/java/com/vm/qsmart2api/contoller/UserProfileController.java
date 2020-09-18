/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.roomNo.RoomGDto;
import com.vm.qsmart2api.dtos.roomNo.RoomDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.UserProfileService;
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
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.userprofile.AssignedServicesResponse;
import com.vm.qsmart2api.dtos.userprofile.UserRoomDto;
import com.vm.qsmart2api.dtos.userprofile.ServicesWithCount;
import com.vm.qsmart2api.dtos.userprofile.ServicesWithCountGDTO;
import com.vm.qsmart2api.dtos.userprofile.UserProfileCrtDto;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Tejasri
 */
@RestController
@RequestMapping("userprofile/")
public class UserProfileController {

    private static final Logger logger = LogManager.getLogger(UserProfileController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @Autowired
    UserProfileService userProfileService;

    @PostMapping(path = "{userId}/map/room-user", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> mappingRoomToUser(
            Locale locale,
            @Valid @RequestBody UserRoomDto userRoom,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "MAP_USER_ROOM") String tranId) {

        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            long roomId = userProfileService.mappingUsersWithRooms(header, userId, userRoom);
            if (roomId > 0) {
                response = new CustomResponse(true, messageSource.getMessage("user.room.ctrl.map", null, locale), roomId);
                logger.info("{} <<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MappingUsersWithRoom.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new CustomResponse(false, messageSource.getMessage("user.room.ctrl.mapfail", null, locale), roomId);
                logger.info("{} <<:mappingUsersWithRooms:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MappingUsersWithRoomFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:mappingUsersWithRooms:userRoom:{}:Error:{}", header, userRoom, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("user.room.ctrl.mapfail", null, locale));
            logger.info("{}<<:mappingUsersWithRooms:Response:[{}]", header, response.isStatus());
            auditService.saveAuditDetails(header, userId, AuditMessage.MappingUsersWithRoomFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

    @PostMapping(path = "{userId}/map/services-user", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> mappingServicesToUser(@RequestBody UserProfileCrtDto userDto, Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "CREATE_USERPROFILE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse sResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{},userDto:[{}]", header, userId, userDto);
            userProfileService.saveUserProfile(header, userId, userDto);
            sResponse = new CustomResponse(true, messageSource.getMessage("userprofile.ctrl.create", null, locale));
            logger.info("{}<<:Response:{}", header, sResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.MappingUsersWithService.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.CREATED).body(sResponse);

        } catch (Exception e) {
            logger.error("{}Excep:createUserProfile:userId:{}:userDto:{}:Error:{}", header, userId, userDto, e.getMessage());
            sResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, sResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sResponse);
        }
    }

    @GetMapping(path = "{userId}/rooms", produces = {"application/json", "application/xml"})
    public ResponseEntity<RoomGDto> getRoomsByUserId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "MAP_USER_ROOM") String tranId) {

        String header = null;
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:userId:{}", header, userId);
            RoomGDto roomGDto = userProfileService.getRoomsByUserId(header, userId);
            logger.info("{}<<:Response:{}", header, roomGDto);
            auditService.saveAuditDetails(header, userId, AuditMessage.GetRoomsByUserId.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(roomGDto);
        } catch (Exception e) {
            logger.error("{}Excep:getRoomsByUserId:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.GetRoomsByUserIdFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RoomGDto());
        }
    }

    @GetMapping(path = "{userId}/mapped-services",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<AssignedServicesResponse> getMappedServicesByUserId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_SERVICES") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:[{}]", header, userId);
            AssignedServicesResponse services = userProfileService.getAllServicesByUserId(header, userId);
            logger.trace("{}<<Response:{}", header, services);
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(services);
        } catch (Exception e) {
            logger.error("{}Excep:getUserAssignedServices:userId:{}:Error:{}", header, userId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<getAllServicesByUserId:Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AssignedServicesResponse());
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userId}/mapped/room-user", produces = {"application/json", "application/xml"})
    public ResponseEntity<RoomDto> getMappedRoomByUserId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "GET_MAPED_USER_ROOM") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>Request:{}", header);
            RoomDto roomDto = userProfileService.getRoomByUserId(userId);
            logger.info("{}<<:Response:{}", header, roomDto);
            auditService.saveAuditDetails(header, userId, AuditMessage.GetRoomsByUserId.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(roomDto);
        } catch (Exception e) {
            logger.error("{}Excep:getRoomsByUserId:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.GetRoomsByUserIdFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RoomDto());
        }

    }

    @GetMapping(path = "{userId}/mapped-services-count/{status}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ServicesWithCountGDTO> getMappedServicesAndCountByUserId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @PathVariable("status") String status,
            @RequestHeader(value = "tranId", defaultValue = "GET_SERVICES") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:userId:[{}]", header, userId);
            ServicesWithCountGDTO services = userProfileService.getAllServicesAndCountByUserId(header, userId, status);
            logger.info("{}<<Response:{}", header, services);
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(services);
        } catch (Exception e) {
            logger.error("{}Excep:getMappedServicesAndCountByUserId:userId:{}:Error:{}", header, userId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.ServiceGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<getMappedServicesAndCountByUserId:Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServicesWithCountGDTO(new ArrayList<ServicesWithCount>()));
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/priority-update/{tokenId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateTokenPriority(@RequestParam short priority,
            Locale locale,
            @PathVariable("tokenId") long tokenId,
            @PathVariable("userId") long userId,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_TOKEN_PRIORITY") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse cResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> tokenId:{}:priority:{}", header, tokenId, priority);
            tokenId = userProfileService.updateTokenPriority(header, tokenId, priority, userId);
            if (tokenId > 0) {
                cResponse = new CustomResponse(true, messageSource.getMessage("token.ctrl.priority", null, locale), tokenId);
                logger.info("{} << response:{}", header, cResponse);
                return ResponseEntity.status(HttpStatus.CREATED).body(cResponse);
            } else {
                cResponse = new CustomResponse(false, messageSource.getMessage("token.ctrl.priorityfail", null, locale), tokenId);
                logger.info("{} << response:{}", header, cResponse);
                return ResponseEntity.status(HttpStatus.OK).body(cResponse);
            }
        } catch (Exception ex) {
            logger.info("{}Excep:updateTokenPriority:{}:tokenId:{}:priority:{}:Eroro:{}", header, tokenId, priority, ExceptionUtils.getRootCauseMessage(ex));
            cResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{} << Resposne:{}", header, cResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cResponse);
        }
    }

}

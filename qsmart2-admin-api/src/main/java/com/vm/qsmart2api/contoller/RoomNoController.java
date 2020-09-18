/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2.model.TblRoom;
import com.vm.qsmart2api.dtos.AuditDetails;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.roomNo.RoomNoGDto;
import com.vm.qsmart2api.dtos.roomNo.RoomNoGetGDto;
import com.vm.qsmart2api.dtos.roomNo.RoomNoCrtDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.RoomNoRepository;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.RoomNoService;
import java.util.ArrayList;
import java.util.List;
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
 * @author Tejasri
 */
@RestController
@RequestMapping("roomNo/")
public class RoomNoController {

    private static final Logger logger = LogManager.getLogger(RoomNoController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @Autowired
    RoomNoRepository roomNoRepo;

    @Autowired
    RoomNoService roomNoService;

    @GetMapping(path = "{userId}/all/{roomMasterId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<RoomNoGetGDto> getAllRoomNos(
            Locale locale,
            @PathVariable("userId") long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_ROOMNoS") String tranId,
            @PathVariable("roomMasterId") Long roomMasterId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>Request:[{}]", header, userId);
            RoomNoGetGDto rooms = roomNoService.getAllRoomsByRoomMasterId(header, userId, roomMasterId);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomNosGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(rooms);
        } catch (Exception e) {
            logger.error("{}Excep:getAllRoomNos:Error:{}", e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomNosGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RoomNoGetGDto(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;

        }
    }

    @DeleteMapping(path = "{userId}/delete/{roomId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> deleteRoomByRoomId(
            Locale locale,
            @PathVariable("roomId") Long roomId, @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "DELETE_ROOMNO") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>Request:[{}]", header, roomId);
            roomNoRepo.deleteById(roomId);
            response = new Response(true, messageSource.getMessage("room.nbr.ctrl.delete", null, locale));
            logger.trace("{}<<Response:{}", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomNoDelete.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("{}Excep:deleteByRoomId:userId:{},Error:{}", header, userId, e.getMessage());
            response = new Response(false, messageSource.getMessage("room.nbr.ctrl.faildelete", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomNoDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;

        }
    }

    @PostMapping("{userId}/create/{roomMasterId}")
    public ResponseEntity<CustomResponse> createRoom(Locale locale,
            @Valid @RequestBody RoomNoCrtDto roomDto,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_ROOMNO") String tranId,
            @PathVariable("roomMasterId") Long roomMasterId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> userId:{},branchId:{},roomDto:{}", header, userId, roomMasterId, (roomDto != null));
            Long roomId = roomNoService.saveRoomInDb(header, userId, roomDto, roomMasterId);
            if (roomId > 0) {
                response = new CustomResponse(true, roomId + " "+ messageSource.getMessage("room.nbr.ctrl.create", null, locale), roomId);
                logger.info("{} <<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoomNoCreate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response = new CustomResponse(false, messageSource.getMessage("room.nbr.ctrl.createfail", null, locale));
                logger.info("{} <<:createRoom:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoomNoCreateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createRoom:userId:{},branchId:{},roomDto:{},Error:{}", header, userId, roomMasterId, roomDto, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{roomMasterId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateRoomNo(Locale locale,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody RoomNoGDto room,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_ROOMNO") String tranId,
            @PathVariable("roomMasterId") Long roomMasterId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> userId:{},branchId:{},room:{}", header, userId, (room != null));
            int id = roomNoService.updateRoomNo(header, userId, roomMasterId, room);
            if (id > 0) {
                response = new CustomResponse(true, messageSource.getMessage("room.nbr.ctrl.update", null, locale), room.getRoomId());
                logger.info("{} <<:update:header:{}:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoomNoUpdate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new CustomResponse(false, messageSource.getMessage("room.nbr.ctrl.updatefail", null, locale));
                logger.info("{} <<:update:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoomNoUpdateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

        } catch (Exception e) {
            logger.error("{} Excep:updateRoom:userId:{},branchId:{},room:{},Error:{}", header, userId, room, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomNoUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }
}

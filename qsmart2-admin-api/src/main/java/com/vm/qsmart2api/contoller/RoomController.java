/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2.model.TblRoomMaster;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.room.RoomCrtDto;
import com.vm.qsmart2api.repository.RoomRepository;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.dtos.AuditDetails;
import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.room.RoomGetDto;
import com.vm.qsmart2api.dtos.room.RoomUpDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.RoomService;
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
@RequestMapping("room/")
public class RoomController {

    private static final Logger logger = LogManager.getLogger(RoomController.class);

    @Autowired
    RoomRepository roomRepo;

    @Autowired
    RoomService roomService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @PostMapping("{userId}/create/{branchId}")
    public ResponseEntity<CustomResponse> createRoom(Locale locale,
            @Valid @RequestBody RoomCrtDto roomDto,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_ROOM") String tranId,
            @PathVariable("branchId") Long branchId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> userId:{},branchId:{},roomDto:{}", header, userId, branchId, (roomDto != null));
            if (roomService.validateRoomEngName(header, branchId, roomDto.getRoomNameEn())) {
                if (roomService.validateRoomNameArbInDb(header, branchId, roomDto.getRoomNameAr())) {
                    Long roomMasterId = roomService.saveRoomInDb(header, userId, roomDto, branchId);
                    if (roomMasterId > 0) {
                        response = new CustomResponse(true, messageSource.getMessage("room.ctrl.create", null, locale), roomMasterId);
                        logger.info("{} <<:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.RoomCreate.getMsg(), AuditStatus.SUCCESS);
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("room.ctrl.createfail", null, locale));
                        logger.info("{} <<:createRoom:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.RoomCreateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("room.ctrl.arbname.exists", null, locale));
                    logger.info("{} <<:createRoom:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.RoomCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.RoomArbNameFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("room.ctrl.engname.exists", null, locale));
                logger.info("{} <<:createRoom:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoomCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.RoomEngNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createRoom:userId:{},branchId:{},roomDto:{},Error:{}", header, userId, branchId, roomDto, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{branchId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateRoom(Locale locale,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody RoomUpDto room,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_ROOM") String tranId,
            @PathVariable("branchId") Long branchId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> userId:{},branchId:{},room:{}", header, userId, (room != null));
            if (roomService.validateRoomEngNameByRoomId(header, branchId, room.getRoomMasterId(), room.getRoomNameEn())) {
                if (roomService.validateRoomArbNameByRoomId(header, branchId, room.getRoomMasterId(), room.getRoomNameAr())) {
                    Long roomMasterId = roomService.updateRoom(header, userId, branchId, room);
                    if (roomMasterId > 0) {
                        response = new CustomResponse(true, messageSource.getMessage("room.ctrl.update", null, locale), roomMasterId);
                        logger.info("{} <<:update:header:{}:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.RoomUpdate.getMsg(), AuditStatus.SUCCESS);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("room.ctrl.updatefail", null, locale));
                        logger.info("{} <<:update:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.RoomUpdateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("room.ctrl.arbname.exists", null, locale));
                    logger.info("{} << :updateRoom:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.RoomUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.RoomArbNameFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("room.ctrl.engname.exists", null, locale));
                logger.info("{} << :updateRoom:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoomUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.RoomEngNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

        } catch (Exception e) {
            logger.error("{} Excep:updateRoom:userId:{},branchId:{},room:{},Error:{}", header, userId, room, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

    @GetMapping(path = "{userId}/all/{branchId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<RoomGetDto> getAllRoomsByBranchId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_ROOMS") String tranId,
            @PathVariable("branchId") Long branchId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>header:[{}]:branchId:[{}]", header, branchId);
            RoomGetDto rooms = roomService.getAllRoomsByBranchId(header, userId, branchId);
            logger.trace("{}<<Response:{}", header, rooms);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(rooms);
        } catch (Exception e) {
            logger.error("{}Excep:getAllRoomsByBranchId:userId:{},branchId:{},Error:{}", header, userId, branchId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RoomGetDto());
        } finally {
            sb = null;
            header = null;
        }
    }

    @DeleteMapping(path = "{userId}/deleteRoom/{roomMasterId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> deleteByRoomMasterId(
            Locale locale,
            @PathVariable("roomMasterId") Long roomMasterId, @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "DELETE_ROOM_MASTER") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> header:{},roomMasterId:{}", header, roomMasterId);
            TblRoomMaster roomMaster = roomService.getAllRoomsByMasterId(header, roomMasterId);
            if (roomMaster.getRooms() != null && !roomMaster.getRooms().isEmpty()) {
                response = new CustomResponse(false, messageSource.getMessage("room.ctrl.roomnos.assigned", null, locale));
                logger.info("<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoomDeleteFail.getMsg(), AuditStatus.FAILURE, AuditMessage.RoomAssignedToRoomNos.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                roomRepo.deleteById(roomMasterId);
                response = new CustomResponse(true, messageSource.getMessage("room.ctrl.delete", null, locale));
                logger.info("<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoomDelete.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:deleteByRoomMasterId:userId:{},roomMasterId:{},Error:{}", header, userId, roomMasterId, e.getMessage());
            response = new Response(false, messageSource.getMessage("room.ctrl.faildelete", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.RoomDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;

        }
    }

}

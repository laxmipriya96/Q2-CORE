/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblRoom;
import com.vm.qsmart2.model.TblRoomMaster;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.roomNo.RoomNoGDto;
import com.vm.qsmart2api.dtos.roomNo.RoomNoGetGDto;
import com.vm.qsmart2api.dtos.roomNo.RoomNoCrtDto;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.RoomNoRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tejasri
 */
@Service
public class RoomNoService {

    @Autowired
    RoomNoRepository roomNoRepository;

    @Autowired
    DateUtils dateUtils;

    private static final Logger logger = LogManager.getLogger(RoomNoService.class);

    public RoomNoGetGDto getAllRoomsByRoomMasterId(String header, Long userId, Long roomMasterId) {
        logger.info("{}>>:userId:{},branchId:[{}]", header, userId, roomMasterId);
        try {

            List<TblRoom> room = roomNoRepository.findByRoomMasterId(roomMasterId);
            List<RoomNoGDto> roomDto = Mapper.INSTANCE.roomListEntityToroomdto(room);
            RoomNoGetGDto roomGetDto = new RoomNoGetGDto();
            roomGetDto.setRoomNos(roomDto);
            return roomGetDto;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllRoomsByBranchId:header:{}:branchId:[{}],Error:{}", header, userId, roomMasterId, ExceptionUtils.getRootCauseMessage(e));
            return new RoomNoGetGDto(new ArrayList<>());
        }
    }

    public long saveRoomInDb(String header, Long userId, RoomNoCrtDto roomDto, Long roomMasterId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">> save:header:[{}]:branchId:{}:room:[{}]", header, roomMasterId, roomDto);
            }
            int count = 0;
            for (String room : roomDto.getRoomNoa()) {
                TblRoom roomt = roomNoRepository.findByRoomMasterIdAndRoomNo(roomMasterId, room);
                if (roomt == null) {
                    TblRoom roomTbl = new TblRoom();
                    roomTbl.setRoomNo(room);
                    roomTbl.setRoomMaster(new TblRoomMaster(roomMasterId));
                    roomTbl.setCreatedBy(userId);
                    roomTbl.setCreatedOn(dateUtils.getdate());
                    roomTbl.setUpdatedOn(dateUtils.getdate());
                    roomTbl.setUpdatedBy(userId);
                    roomTbl = roomNoRepository.saveAndFlush(roomTbl);
                    if (logger.isDebugEnabled()) {
                        logger.info("<<:header:[{}]:roomMaster:[{}]", header, roomTbl);
                    }
                    count++;
                }
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:saveRoomInDb:userId:{}:roomMasterId:{}:RoomDto:{},Error:{}", header, userId, roomMasterId, roomDto, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

     public int updateRoomNo(String header, Long userId, long roomMasterId, RoomNoGDto room) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>update:[{}]:header:[{}]:RoomUpDto:[{}]", header, room);
            }
            int roomId = roomNoRepository.updateRoom(room.getRoomId(), room.getRoomNo(), userId, dateUtils.getdate());
            return roomId;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:updateRoom:header:{},userId:{},room:{},Error:{}", header, userId, room, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally {
            room = null;
        }
    }
    
    


}

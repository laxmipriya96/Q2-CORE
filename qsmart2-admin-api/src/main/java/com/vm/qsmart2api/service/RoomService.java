/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblRoom;
import com.vm.qsmart2.model.TblRoomMaster;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.room.RoomCrtDto;
import com.vm.qsmart2api.dtos.room.RoomGetDto;
import com.vm.qsmart2api.dtos.room.RoomUpDto;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.RoomNoRepository;
import com.vm.qsmart2api.repository.RoomRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.NoResultException;
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
public class RoomService {

    private static final Logger logger = LogManager.getLogger(RoomService.class);

    @Autowired
    RoomRepository roomMasterRepo;

    @Autowired
    RoomNoRepository roomNoRepository;

    @Autowired
    DateUtils dateUtils;

    public long saveRoomInDb(String header, Long userId, RoomCrtDto room, Long branchId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">> save:header:[{}]:branchId:{}:room:[{}]", header, branchId, room);
            }
            TblRoomMaster roomMaster = Mapper.INSTANCE.roomCrtDtoToRoomEntity(room);
            roomMaster.setBranchId(branchId);
            roomMaster.setCreatedBy(userId);
            roomMaster.setCreatedOn(dateUtils.getdate());
            roomMaster.setUpdatedOn(dateUtils.getdate());
            roomMaster.setUpdatedBy(userId);
            roomMaster = roomMasterRepo.saveAndFlush(roomMaster);
            logger.info("<<:header:[{}]:roomMaster:[{}]", header, roomMaster);
            return roomMaster.getRoomMasterId();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:saveRoomInDb:header:{}:userId:{}:branchId:{}:locationDto:{},Error:{}", header, userId, branchId, room, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public boolean validateRoomEngName(String header, Long branchId, String roomNameEn) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>: header:{},roomNameEn:{}:branchId:{}", header, roomNameEn, branchId);
            }
            int count = roomMasterRepo.validateRoomEngName(roomNameEn.toUpperCase(), branchId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateRoomEngName:roomNameEn:{}:branchId:{},Error:{}", header, roomNameEn, branchId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateRoomEngName:roomNameEn:{}:branchId:{},Error:{}", header, roomNameEn, branchId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateRoomNameArbInDb(String header, Long branchId, String roomNameAr) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>: header:{},roomNameAr:{}:branchId:{}", header, roomNameAr, branchId);
            }
            int count = roomMasterRepo.validateRoomNameArbInDb(roomNameAr.toUpperCase(), branchId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateRoomNameArbInDb:roomNameAr:{}:branchId:{},Error:{}", header, roomNameAr, branchId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateRoomNameArbInDb:roomNameAr:{}:branchId:{},Error:{}", header, roomNameAr, branchId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public long updateRoom(String header, Long userId, long branchId, RoomUpDto room) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>update:[{}]:header:[{}]:RoomUpDto:[{}]", header, room);
            }
            TblRoomMaster roomMastr = Mapper.INSTANCE.roomDtoToRoomEntity(room);

            roomMastr.setUpdatedBy(userId);
            roomMastr.setUpdatedOn(dateUtils.getdate());
            roomMastr.setBranchId(branchId);
            logger.info("{}<<:header:[{}]:roomMastr:[{}]", header, roomMastr);
            roomMastr = roomMasterRepo.saveAndFlush(roomMastr);
            return roomMastr.getRoomMasterId();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:updateRoom:header:{},userId:{},room:{},Error:{}", header, userId, room, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally {
            room = null;
        }
    }

    public boolean validateRoomEngNameByRoomId(String header, Long branchId, Long roomMasterId, String roomNameEn) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:header:{},roomNameEn:{}:id:{}:branchId:{}", header, roomNameEn, roomMasterId, branchId);
            }
            int count = roomMasterRepo.validateRoomEngNameByRoomId(roomNameEn.toUpperCase(), roomMasterId, branchId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateRoomEngNameByRoomId:roomNameEn:{}:id:{}:branchId:{},Error:{}", header, roomNameEn, roomMasterId, branchId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateRoomEngNameByRoomId:roomNameEn:{}:id:{}:branchId:{},Error:{}", header, roomNameEn, roomMasterId, branchId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateRoomArbNameByRoomId(String header, Long branchId, Long roomMasterId, String roomNameAr) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:header:{},roomNameAr:{}:id:{}:branchId:{}", header, roomNameAr, roomMasterId, branchId);
            }
            int count = roomMasterRepo.validateRoomArbNameByRoomId(roomNameAr.toUpperCase(), roomMasterId, branchId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateRoomArbNameByRoomId:roomNameAr:{}:id:{}:branchId:{},Error:{}", header, roomNameAr, roomMasterId, branchId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateRoomArbNameByRoomId:roomNameAr:{}:id:{}:branchId:{},Error:{}", header, roomNameAr, roomMasterId, branchId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public RoomGetDto getAllRoomsByBranchId(String header, Long userId, Long branchId) {
        if (logger.isDebugEnabled()) {
            logger.info("{} >>:header:{},userId:{},branchId:[{}]", header, userId, branchId);
        }
        try {
            List<TblRoomMaster> roomList = roomMasterRepo.findByBranchId(branchId);
            return new RoomGetDto(Mapper.INSTANCE.roomEntityListToRoomUpDtoList(roomList));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllRoomsByBranchId:header:{},userId:{},branchId:{},Error:{}", header, userId, branchId, ExceptionUtils.getRootCauseMessage(e));
            return new RoomGetDto(new ArrayList<>());
        }
    }
    
    public TblRoomMaster getAllRoomsByMasterId(String header, Long roomMasterId) {
        if (logger.isDebugEnabled()) {
            logger.info("{}>>:branchId:[{}]", header, roomMasterId);
        }
        try {
            TblRoomMaster room = roomMasterRepo.findByRoomMasterId(roomMasterId);
            return room;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllRoomsByBranchId:roomMasterId:[{}],Error:{}", header, roomMasterId, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }
    
    public int checkRoomsBybranchId(Long branchId) {
        return roomMasterRepo.countRoomsByBranchId(branchId);
    }
}

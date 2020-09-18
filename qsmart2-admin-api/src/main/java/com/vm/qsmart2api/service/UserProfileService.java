/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblRoom;
import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.model.TblToken;
import com.vm.qsmart2.model.TblUser;
import com.vm.qsmart2.model.TblUserProfile;
import com.vm.qsmart2.utils.ApptStatus;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.branch.BranchesServices;
import com.vm.qsmart2api.dtos.branch.BranchesServicesResponse;
import com.vm.qsmart2api.dtos.roomNo.RoomGDto;
import com.vm.qsmart2api.dtos.roomNo.RoomDto;
import com.vm.qsmart2api.dtos.service.ServiceCrtDto;
import com.vm.qsmart2api.dtos.userprofile.AssignedServiceGDto;
import com.vm.qsmart2api.dtos.userprofile.AssignedServicesResponse;
import com.vm.qsmart2api.dtos.userprofile.ServicesWithCount;
import com.vm.qsmart2api.dtos.userprofile.ServicesWithCountGDTO;
import com.vm.qsmart2api.dtos.userprofile.UserProfileCrtDto;
import com.vm.qsmart2api.dtos.userprofile.UserRoomDto;
import com.vm.qsmart2api.dtos.userprofilenew.UserProfileCrtBranch;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.BranchRepository;
import com.vm.qsmart2api.repository.RoomNoRepository;
import com.vm.qsmart2api.repository.TokenRepository;
import com.vm.qsmart2api.repository.UserProfileRepository;
import com.vm.qsmart2api.repository.UserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tejasri
 */
@Service
public class UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    RoomNoRepository roomNoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DateUtils dateUtils;

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    private static final Logger logger = LogManager.getLogger(UserProfileService.class);

    public long mappingUsersWithRooms(String header, Long userId, UserRoomDto userRooms) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>mappingUsersWithRooms:userRooms:[{}]", header, userRooms);
            }
            TblUser user = userRepository.getOne(userId);
            Set<TblRoom> rooms = new HashSet<>();
            rooms.add(new TblRoom(userRooms.getRoomId()));
            user.setRooms(rooms);
            userRepository.saveAndFlush(user);
            if (isLogEnabled) {
                logger.info("<<room:[{}]", header, userRooms);
            }
            return userRooms.getRoomId();
        } catch (Exception e) {
            logger.error("{}Excep:mappingUsersWithRooms:userId:{}:userRooms:{},Error:{}", header, userId, userRooms, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public RoomGDto getRoomsByUserId(String header, Long userId) {
        List<RoomDto> rooms = new ArrayList<>();
        try {
            if (isLogEnabled) {
                logger.info("{}>>mappingUsersWithRooms:userId:[{}]", header, userId);
            }
            List<Object[]> roomsObj = userProfileRepository.getRoomsByUserId(userId);
            for (Object[] obj : roomsObj) {
                if (obj[0] != null && obj[1] != null) {
                    //  Long roomId, String roomNo, String roomNameEn, String roomNameAr, Integer maxAllowedToken
                    RoomDto roomObj = new RoomDto((long) obj[0], (String) obj[1], (String) obj[2], (String) obj[3], (int) obj[4]);
                    rooms.add(roomObj);
                }
            }
            return new RoomGDto(rooms);
        } catch (Exception e) {
            logger.error("{}Excep:getRoomsByUserId,userId:{},Error:{}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            return new RoomGDto(rooms);
        } finally {
            rooms = null;
        }
    }

    public RoomGDto getRoomsByUserIdAndBranch(String header, Long userId, long branchId) {
        List<RoomDto> rooms = new ArrayList<>();
        try {
            if (isLogEnabled) {
                logger.info("{}>>mappingUsersWithRooms:userId:[{}],branchId:[{}]", header, userId, branchId);
            }
            List<Object[]> roomsObj = userProfileRepository.getRoomsByUserIdNdBranchId(userId, branchId);
            for (Object[] obj : roomsObj) {
                if (obj[0] != null && obj[1] != null) {
                    //  Long roomId, String roomNo, String roomNameEn, String roomNameAr, Integer maxAllowedToken
                    RoomDto roomObj = new RoomDto((long) obj[0], (String) obj[1], (String) obj[2], (String) obj[3], (int) obj[4]);
                    rooms.add(roomObj);
                }
            }
            return new RoomGDto(rooms);
        } catch (Exception e) {
            logger.error("{}Excep:getRoomsByUserId,userId:{},Error:{}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            return new RoomGDto(rooms);
        } finally {
            rooms = null;
        }
    }

    public RoomDto getRoomByUserId(Long userId) {
        return roomNoRepository.getRoomByUserId(userId);
    }

    public void saveUserProfile(String header, long userId, UserProfileCrtDto userDto) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>userId:{}", userId);
            }
            userProfileRepository.deleteByUserIdInDb(userId);
            for (long serviceId : userDto.getServices()) {
                TblUserProfile userProfile = new TblUserProfile();
                userProfile.setBranchId(userDto.getBranchId());
                userProfile.setUserId(userId);
                userProfile.setServiceId(serviceId);
                userProfileRepository.saveAndFlush(userProfile);
                if (isLogEnabled) {
                    logger.info("{}<<userId:{}:userProfile:[{}]", header, userId, userProfile);
                }
            }
        } catch (Exception e) {
            logger.error("{}Excep:saveUserProfile:userDto:{}:Error:{}", header, userDto, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void saveMultiUserProfile(String header, long userId, com.vm.qsmart2api.dtos.userprofilenew.UserProfileCrtDto userDto) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>userId:{}", userId);
            }
            userProfileRepository.deleteByUserIdInDb(userId);
            for (UserProfileCrtBranch branchDto : userDto.getBranches()) {
                for (long serviceId : branchDto.getServices()) {
                    TblUserProfile userProfile = new TblUserProfile();
                    userProfile.setBranchId(branchDto.getBranchId());
                    userProfile.setUserId(userId);
                    userProfile.setServiceId(serviceId);
                    userProfileRepository.saveAndFlush(userProfile);
                    if (isLogEnabled) {
                        logger.info("{}<<userId:{}:userProfile:[{}]", header, userId, userProfile);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("{}Excep:saveUserProfile:userDto:{}:Error:{}", header, userDto, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public AssignedServicesResponse getAllServicesByUserId(String header, Long userId) {
        if (isLogEnabled) {
            logger.info("{}>>:userId:[{}]", header, userId);
        }
        try {
            List<TblService> serviceList = userProfileRepository.getAllServicesByUserId(userId);
            //List<TblService> serviceList = userProfileRepository.getAllServicesAndDocotrsByUserId(userId);
            List<AssignedServiceGDto> servceDto = Mapper.INSTANCE.serviceentityTOSerDTO(serviceList);
            for (AssignedServiceGDto serv : servceDto) {
                serv.setCount(2);
            }
            return new AssignedServicesResponse(serviceList != null & !serviceList.isEmpty() ? serviceList.get(0).getBranchId() : 0, servceDto);
        } catch (Exception e) {
            logger.error("{}Excep:getAllServicesByUserId:userId:{},Error:{}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            return new AssignedServicesResponse(0l, new ArrayList<>());
        }
    }

    @Autowired
    BranchRepository branchRepository;

    //public String ename = null;
    public BranchesServicesResponse getAllBranchesAndServsByLocationIdAndBranchType(String header, Long userId) {
        List<Object[]> branchesList = new ArrayList<>();
        if (logger.isDebugEnabled()) {
            logger.info("{}>>userId:{}", header, userId);
        }
        try {
            branchesList = branchRepository.getMappedBranchesAndServicesByUserId(userId);
            List<BranchesServices> brnachList = new ArrayList<>();
            if (branchesList != null && branchesList.size() > 0) {
                branchesList.forEach((obj) -> {
                    TblBranch loc = (TblBranch) obj[0];
                    BranchesServices branchDto = Mapper.INSTANCE.branchToBranchServices(loc);
                    ServiceCrtDto serviceDto = Mapper.INSTANCE.serviceTblEntityToServiceCrtDto((TblService) obj[1]);
                    //System.out.println("Service :"+serviceDto.toString());
                    Optional<BranchesServices> matchingObject = brnachList.stream().filter(branchObj -> Objects.equals(branchObj.getBranchId(), branchDto.getBranchId())).findFirst();
                    BranchesServices existDspObj = matchingObject.orElse(null);
                    if (existDspObj != null) {
                        brnachList.remove(existDspObj);
                        Optional<ServiceCrtDto> matchingDept = existDspObj.getServices().stream().filter(serObj -> Objects.equals(serObj.getServiceId(), serviceDto.getServiceId())).findFirst();
                        ServiceCrtDto existObjServce = matchingDept.orElse(null);
                        if (existObjServce != null) {
                            existDspObj.getServices().remove(existObjServce);
                            existDspObj.addService(existObjServce);
                        } else {
                            existDspObj.addService(serviceDto);
                        }
                        brnachList.add(existDspObj);
                    } else {
                        branchDto.addService(serviceDto);
                        brnachList.add(branchDto);
                    }
                });
            }
            return new BranchesServicesResponse(brnachList);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesAndServsByLocationIdAndBranchType:userId:{}:Error:{}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            return new BranchesServicesResponse(new ArrayList<>());
        } finally {
        }
    }

    public ServicesWithCountGDTO getAllServicesAndCountByUserId(String header, Long userId, String status) {
        List<ServicesWithCount> objList = new ArrayList<>();
        if (isLogEnabled) {
            logger.info("{}>>:userId:[{}]", header, userId);
        }
        List<String> sevicesStatusList = null;
        try {
            //objList = userProfileRepository.getAllServicesAndCountByUserId(userId, status);
            Date fromDate = dateUtils.dayStartDateTime();
            Date toDate = dateUtils.dayEndDateTime();
            sevicesStatusList = new ArrayList<String>(){
                {
                    add(ApptStatus.MANUALCHECKIN.getValue());
                }
                {
                    add(ApptStatus.HL7CHECKIN.getValue());
                }
                {
                    add(ApptStatus.KIOSKCHECKIN.getValue());
                }
                {
                    add(ApptStatus.WAITING.getValue());
                }
            };
            List<Object[]> obList = userProfileRepository.getAllServicesAndCountByUserId(userId, status, fromDate, toDate, sevicesStatusList);
            for (Object[] obj : obList) {
                if (obj[0] != null) {
                    ServicesWithCount serviceObj = new ServicesWithCount((long) obj[0], (String) obj[1], (String) obj[2], (long) obj[3]);
                    objList.add(serviceObj);
                }

            }
            return new ServicesWithCountGDTO(objList);
        } catch (Exception e) {
            logger.error("{}Excep:getAllServicesByUserId:userId:{},Error:{}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            return new ServicesWithCountGDTO(objList);
        } finally {
            objList = null;
            sevicesStatusList = null;
        }
    }

    public ServicesWithCountGDTO getAllServicesAndCountByUserIdAndBranchId(String header, Long userId, long branchId, String status) {
        List<ServicesWithCount> objList = new ArrayList<>();
        if (isLogEnabled) {
            logger.info("{}>>:userId:[{}],branchId:[{}]", header, userId, branchId);
        }
        try {
            //objList = userProfileRepository.getAllServicesAndCountByUserId(userId, status);
            List<Object[]> obList = userProfileRepository.getAllServicesAndCountByUserIdAndBranchId(userId, branchId, status);
            for (Object[] obj : obList) {
                if (obj[0] != null) {
                    ServicesWithCount serviceObj = new ServicesWithCount((long) obj[0], (String) obj[1], (String) obj[2], (long) obj[3]);
                    objList.add(serviceObj);
                }
            }
            return new ServicesWithCountGDTO(objList);
        } catch (Exception e) {
            logger.error("{}Excep:getAllServicesAndCountByUserIdAndBranchId:userId:{},Error:{}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            return new ServicesWithCountGDTO(objList);
        } finally {
            objList = null;
        }
    }

    @Autowired
    TokenRepository tokenRepository;

    public long updateTokenPriority(String header, long tokenId, short priority, long userId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>tokenId:{}:priority:{}", header, tokenId, priority);
            }
            Optional<TblToken> opt = tokenRepository.findById(tokenId);
            if (opt.isPresent()) {
                TblToken token = opt.get();
                token.setUpdatedOn(dateUtils.getdate());
                token.setPriority(priority);
                tokenRepository.save(token);
                logger.info("{}<<:tokenId:[{}]", header, token.getTokenId());
                return token.getTokenId();
            }
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        } catch (Exception e) {
            logger.error("{}Excep:updateTokenPriority:tokenId:{}:priority:{}:Error:{}", header, tokenId, priority, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        }
    }

}

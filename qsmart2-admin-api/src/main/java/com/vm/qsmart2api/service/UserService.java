/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblEnterprise;
import com.vm.qsmart2.model.TblLocation;
import com.vm.qsmart2.model.TblRole;
import com.vm.qsmart2.model.TblRoomMaster;
import com.vm.qsmart2.model.TblUser;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2.utils.Hider;
import com.vm.qsmart2api.dtos.UserDetails;
import com.vm.qsmart2api.dtos.room.RoomGetDto;
import com.vm.qsmart2api.dtos.room.RoomUpDto;
import com.vm.qsmart2api.dtos.user.DoctorDto;
import com.vm.qsmart2api.dtos.user.DoctorResponse;
import com.vm.qsmart2api.dtos.user.ProfileUpDto;
import com.vm.qsmart2api.dtos.user.UserDTO;
import com.vm.qsmart2api.dtos.user.UserUDTO;
import com.vm.qsmart2api.dtos.user.UserUpPasswrd;
import com.vm.qsmart2api.dtos.user.UsersGetDto;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.RoomRepository;
import com.vm.qsmart2api.repository.UserProfileRepository;
import com.vm.qsmart2api.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASHOK
 */
@Service
public class UserService {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    DateUtils dateUtils;

//    public long save(String header, Long userId, UserDTO userDto) {
//        try {
//            logger.info("{}>>usercreate:[{}]:userDtoCrtDto:[{}]:", header, userDto);
//            TblUser user = Mapper.INSTANCE.userCreateDtoToUserEntity(userDto);
//            user.setIsActive((short) 0);
//            user.setSystemAccess((short) 0);
//            user.setIsFirstLogin((short) 0);
//            user.setCreatedBy(userId);
//            user.setCreatedOn(new Date());
//            user.setUpdatedBy(userId);
//            user.setUpdatedOn(new Date());
//            user.setHashPassword("123456");
//            user.setResourceCode("123456");
//            user.setEnterprise(new TblEnterprise(userDto.getEnterpriseId()));
//            user.setRoles( new HashSet<TblRole>(){
//                {
//                    add(new TblRole(userDto.getRoleId()));
//                }
//            });
//            
//            return userRepository.saveAndFlush(user).getUserId();
//        } catch (Exception e) {
//            logger.error("{}Excep:save:Error:{}", header, e.getMessage());
//            return 0;
//        }
//    }
    public long save(String header, Long userId, UserDTO userDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>usercreate:[{}]:userDtoCrtDto:[{}]:", header, userDto);
            }
            TblUser user = Mapper.INSTANCE.userCreateDtoToUserEntity(userDto);
            user.setIsActive((short) 1);
            user.setSystemAccess((short) 0);
            user.setIsFirstLogin((short) 0);
            user.setCreatedBy(userId);
            user.setCreatedOn(dateUtils.getdate());
            user.setUpdatedBy(userId);
            user.setUpdatedOn(dateUtils.getdate());
            user.setHashPassword(Hider.getInstance().encrypt("123456"));
//            user.setResourceCode("123456");
            user.setEnterprise(new TblEnterprise(userDto.getEnterpriseId()));
            if (userDto.getLocationId() > 0) {
                Set<TblLocation> list = new HashSet();
                TblLocation location = new TblLocation();
                location.setLocationId(userDto.getLocationId());
                list.add(location);
                user.setLocations(list);
            }
            user.setRoles(new HashSet<TblRole>() {
                {
                    add(new TblRole(userDto.getRoleId()));
                }
            });

            return userRepository.saveAndFlush(user).getUserId();
        } catch (Exception e) {
            logger.error("{}Excep:save:userDto:{}:Error:{}", header, userDto, e.getMessage());
            return 0;
        }
    }

//    public long update(String header, Long updatedBY, UserUDTO userDto) {
//        try {
//            logger.info("{}>>userupdate:[{}]:userDtoCrtDto:[{}]:updatedBy:{}", header, userDto, updatedBY);
//            TblUser user = Mapper.INSTANCE.userUpdateDtoToUserEntity(userDto);
//            user.setRoles( new HashSet<TblRole>(){
//                {
//                    add(new TblRole(userDto.getRoleId()));
//                }
//            });
//            user.setIsActive((short) userDto.getIsActive());
//            user.setResourceCode("123456");
//            user.setEnterprise(new TblEnterprise(userDto.getEnterpriseId()));
//            user.setUpdatedBy(updatedBY);
//            user.setUpdatedOn(new Date());
//            return userRepository.saveAndFlush(user).getUserId();
//        } catch (Exception e) {
//            logger.error("{}Excep:update:Error:{}", header, e.getMessage());
//            return 0;
//        }
//    }
//    
    @Autowired
    UserProfileRepository userProfileRepository;
    
    public long update(String header, Long updatedBY, UserUDTO userDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>userupdate:[{}]:userDtoCrtDto:[{}]:updatedBy:{}", header, userDto, updatedBY);
            }
            TblUser userFromdb = userRepository.getOne(userDto.getUserId());
            if (!userFromdb.getRoles().isEmpty() && userFromdb.getRoles() != null) {
                List<TblRole> roles = new ArrayList(userFromdb.getRoles());
                if (roles.get(0).getRoleId() != userDto.getRoleId()) {
                    userProfileRepository.deleteByUserIdInDb(userDto.getUserId());
                }
            }
            TblUser user = Mapper.INSTANCE.userUpdateDtoToUserEntity(userDto);
            user.setRoles(new HashSet<TblRole>() {
                {
                    add(new TblRole(userDto.getRoleId()));
                }
            });
            if (userDto.getLocationId() > 0) {
                Set<TblLocation> list = new HashSet();
                TblLocation location = new TblLocation();
                location.setLocationId(userDto.getLocationId());
                list.add(location);
                user.setLocations(list);
            }
            user.setResourceCode("123456");
            user.setEnterprise(new TblEnterprise(userDto.getEnterpriseId()));
            user.setUpdatedBy(updatedBY);
            user.setUpdatedOn(dateUtils.getdate());
            return userRepository.saveAndFlush(user).getUserId();
        } catch (Exception e) {
            logger.error("{}Excep:update:userDto:{}:Error:{}", header, userDto, e.getMessage());
            return 0;
        }
    }

    public UsersGetDto getUsersByRoleIdAndErnterpriseId(String header, long enterpriseId, long roleId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>enterpriseId:[{}]:roleId:[{}]", header, enterpriseId, roleId);
            }
            List<TblUser> usersList = userRepository.getUsersByRoleCodeAndEnterpriseId(enterpriseId, roleId);
            return new UsersGetDto(Mapper.INSTANCE.userEntityListToUserUpdateDtoList(usersList));
        } catch (Exception e) {
            logger.error("{}Excep:getUsersByRoleIdAndErnterpriseId:enterpriseId:{}:roleId:{}Error:{}", header, enterpriseId, roleId, e.getMessage());
            return new UsersGetDto(new ArrayList<>());
        }
    }

    public UsersGetDto getUsersByRoleCode(String header, long roleId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>roleId:[{}]", header, roleId);
            }
            List<TblUser> usersList = userRepository.getUsersByRoleCode("EA");
            return new UsersGetDto(Mapper.INSTANCE.userEntityListToUserUpdateDtoList(usersList));
        } catch (Exception e) {
            logger.error("{}Excep:getUsersByRolecode:roleId:{}:{}Error:{}", header, roleId, e.getMessage());
            return new UsersGetDto(new ArrayList<>());
        }
    }

    public UsersGetDto getUsersByRoleIdAndErnterpriseIdAndLocationId(String header, Long enterpriseId, Long locationId, long roleId, long userId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>roleId:[{}]:enterpriseId:{}:", header, roleId, enterpriseId);
            }
            List<TblUser> usersList = userRepository.getUsersByRoleCodeAndLocationId("EA", "SA", enterpriseId, locationId, userId);
            return new UsersGetDto(Mapper.INSTANCE.userEntityListToUserUpdateDtoList(usersList));
        } catch (Exception e) {
            logger.error("{}Excep:getUsersByRolecode:{}:enterpriseId:{}:locationId:{}:roleId:{}:Error:{}", header, enterpriseId, locationId, roleId, e.getMessage());
            return new UsersGetDto(new ArrayList<>());
        }
    }

    public UserDetails getUserByUsername(String header, String userName) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>userName:[{}]", header, userName);
            }
            TblUser user = userRepository.getUsersByUsername(userName);
            return Mapper.INSTANCE.userEntityToUserDetailsDto(user);
        } catch (Exception e) {
            logger.error("{}Excep:getUserByUsername:userName:{}:Error:{}", header, userName, e.getMessage());
            return null;
        }
    }

    public long updateUserStatus(String header, long activateId, int status, long updatedBy) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>activateId:{}:status:{}", header, activateId, status);
            }
            Optional<TblUser> opt = userRepository.findById(activateId);
            if (opt.isPresent()) {
                TblUser tblUser = opt.get();
                tblUser.setUpdatedBy(updatedBy);
                tblUser.setUpdatedOn(dateUtils.getdate());
                tblUser.setIsActive((short) status);
                userRepository.save(tblUser);
                logger.info("{}<<:userId:[{}]", header, tblUser.getUserId());
                return tblUser.getUserId();
            }
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        } catch (Exception e) {
            logger.error("{}Excep:updateUserStatus:userId:{}:activateId:{}:Error:{}", header, activateId, activateId, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        }
    }

    public boolean validationUserName(String header, String userName) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:userName:{}", header, userName);
            }
            int count = userRepository.validateUserNameInDb(userName.toUpperCase());
            return count <= 0;
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateuserNameInDb:userDto:{}:Error:{}", header, userName, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateuserNameInDb:userDto:{}:Error:{}", header, userName, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }

    }

    public boolean validationUserNameByUserId(String header, UserUDTO userDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:userName:{}:userId:{}", header, userDto.getUserName(), userDto.getUserId());
            }
            int count = userRepository.validateUserNameInDbByUserId(userDto.getUserName().toUpperCase(), userDto.getUserId());
            return count <= 0;
        } catch (NoResultException ne) {
            logger.error("{}Excep:validationUserNameByUserId:userDto:{}:Error:{}", header, userDto, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validationUserNameByUserId:userDto:{}:Error:{}", header, userDto, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }

    }

    public boolean validationEmail(String header, UserDTO userDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:Email:{}", header, userDto.getEmailId());
            }
            int count = userRepository.validateEmailInDb(userDto.getEmailId());
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateUserEmailInDb:userDto:{}:Error:{}", header, userDto, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateUserEmailInDb:userDto:{}:Error:{}", header, userDto, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }

    }

    public UsersGetDto getDoctorsByRoleIdAndAndLocationId(String header, Long locationId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>locationId:[{}]", header, locationId);
            }
            List<TblUser> usersList = userRepository.getUsersByRoleCodeAndAndLocationId(locationId, "DR");
            return new UsersGetDto(Mapper.INSTANCE.userEntityListToUserUpdateDtoList(usersList));
        } catch (Exception e) {
            logger.error("{}Excep:getDoctorsByRoleIdAndAndLocationId:locationId:{}:{}Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            return new UsersGetDto(new ArrayList<>());
        }
    }

    public UsersGetDto getNonAssignedDoctorsByRoleIdAndAndLocationId(String header, Long locationId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>locationId:[{}]", header, locationId);
            }
            List<TblUser> usersList = userRepository.getNonAssignedDrsByRoleCodeAndAndLocationId(locationId, "DR");
            return new UsersGetDto(Mapper.INSTANCE.userEntityListToUserUpdateDtoList(usersList));
        } catch (Exception e) {
            logger.error("{}Excep:getNonAssignedDoctorsByRoleIdAndAndLocationId:locationId:{}:{}Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            return new UsersGetDto(new ArrayList<>());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updatePassword(String header, Long userId, UserUpPasswrd userDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>userupdate:[{}]:userDtoCrtDto:[{}]:updatedBy:{}", header, userDto, userId);
            }
            TblUser user = userRepository.getOne(userId);
            if (Hider.getInstance().decrypt(user.getHashPassword()).equals(userDto.getCurrentPassword())) {
                int id = userRepository.updatePassword(userId, userDto.getNewPassword());
                return id;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:update:userDto:{}:Error:{}", header, userDto, e.getMessage());
            return 0;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public long updateUserProfile(String header, Long userId, ProfileUpDto userDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>updateUserProfile:[{}]:userDtoCrtDto:[{}]:updatedBy:{}", header, userDto, userId);
            }
            return userRepository.updateProfile(userId, userDto.getFirstName(), userDto.getLastName(), userDto.getEmailId(), userDto.getContactNo());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:updateUserProfile:userDto:{}:Error:{}", header, userDto, e.getMessage());
            return 0;
        }
    }

    public DoctorResponse getAllDoctorsByServiceId(String header, Long userId, Long locationId, Long serviceId) {
        if (logger.isDebugEnabled()) {
            logger.info("{}>>:serviceId:[{}]:locatinId:[{}]", header, serviceId, locationId);
        }
        List<DoctorDto> doctors = new ArrayList<>();
        try {
            List<TblUser> doctorsObjs = userRepository.getAllDoctorsByServiceId("DR", serviceId);
            if (doctorsObjs != null && !doctorsObjs.isEmpty()) {
                for (TblUser obj : doctorsObjs) {
                    if (obj != null) {
                        DoctorDto dto = new DoctorDto();
                        dto.setDrId(obj.getUserId());
                        dto.setDoctorName(obj.getFirstName() + "" + obj.getLastName());
                        dto.setUserName(obj.getUserName());
                        dto.setIsActive(obj.getIsActive());
                        doctors.add(dto);
                    }
                }
            } else {
                List<TblUser> usersList = userRepository.getUsersByRoleCodeAndAndLocationId(locationId, "DR");
                if (usersList != null && !usersList.isEmpty()) {
                    for (TblUser obj : usersList) {
                        if (obj != null) {
                            DoctorDto dto = new DoctorDto();
                            dto.setDrId(obj.getUserId());
                            dto.setDoctorName(obj.getFirstName() + "" + obj.getLastName());
                            dto.setUserName(obj.getUserName());
                            dto.setIsActive(obj.getIsActive());
                            doctors.add(dto);
                        }
                    }
                }
            }
            return new DoctorResponse(doctors);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllDoctorsByServiceId:userId:{},ServiceId:{},Error:{}", header, userId, serviceId, ExceptionUtils.getRootCauseMessage(e));
            return new DoctorResponse(doctors);
        } finally {
            doctors = null;
        }

    }

    @Autowired
    RoomRepository roomRepository;

//    public RoomGetDto getMappingRoomsByUserId(String header, Long userId) {
//        if (logger.isDebugEnabled()) {
//            logger.info("{} >>:header:{},userId:{}", header, userId);
//        }
//        try {
//            List<Object[]> list = roomRepository.getMappingRoomsByUserId(userId);
//            List<RoomUpDto> roomDto = new ArrayList<>();
//            if (list != null && !list.isEmpty()) {
//                list.forEach(obj -> {
//                    RoomUpDto dto = Mapper.INSTANCE.roomMasterEntityToRoomDto((TblRoomMaster) obj[0]);
//                    roomDto.add(dto);
//                });
//            }
//            return new RoomGetDto(roomDto);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("{}Excep:getMappingRoomsByUserId:header:{}:userId:{}:Error:{}", header, userId, ExceptionUtils.getRootCauseMessage(e));
//            return new RoomGetDto(new ArrayList<>());
//        }
//    }
}

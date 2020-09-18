    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2.model.TblRole;
import com.vm.qsmart2api.dtos.AuditDetails;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.StatusDto;
import com.vm.qsmart2api.dtos.activedirectory.DirectoryGDto;
import com.vm.qsmart2api.dtos.user.DoctorResponse;
import com.vm.qsmart2api.dtos.user.ProfileUpDto;
import com.vm.qsmart2api.dtos.user.UserDTO;
import com.vm.qsmart2api.dtos.user.UserType;
import com.vm.qsmart2api.dtos.user.UserTypeGDto;
import com.vm.qsmart2api.dtos.user.UserUDTO;
import com.vm.qsmart2api.dtos.user.UserUpPasswrd;
import com.vm.qsmart2api.dtos.user.UsersGetDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.ActiveDirectoryService;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.MailQueueService;
import com.vm.qsmart2api.service.RoleService;
import com.vm.qsmart2api.service.UserService;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * @author ASHOK
 */
@RestController
@RequestMapping("user/")
public class UserController {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserService userService;

    @Autowired
    AuditService auditService;

    @Autowired
    RoleService roleService;

    @Value("${mail.send.required}")
    private boolean isSendMail;

    @Autowired
    MailQueueService mailQueueService;

    @Autowired
    ActiveDirectoryService adService;

    @PostMapping(path = "{userid}/create", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createUser(
            @RequestBody UserDTO userDto,
            Locale locale,
            @PathVariable("userid") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "CREATE_USER") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse sResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>header:userDto:{}", header, (userDto != null));
            if (userService.validationUserName(header, userDto.getUserName())) {
                if (userService.validationEmail(header, userDto)) {
                    Long id = 0l;
                    if (userDto.getUserType().equalsIgnoreCase("Act")) {
                        DirectoryGDto directory = adService.getUserFromDirectoryByName(header, userId, userDto.getUserName(), locale, userDto.getEnterpriseId());
                        if (directory.isStatus()) {
                            id = userService.save(header, userId, userDto);
                        } else {
                            sResponse = new CustomResponse(false, directory.getMessage());
                            logger.info("{}<<:Response:{}", header, sResponse);
                            auditService.saveAuditDetails(header, userId, AuditMessage.UserCreateFail.getMsg(), AuditStatus.FAILURE);
                            return ResponseEntity.status(HttpStatus.OK).body(sResponse);
                        }
                    } else {
                        id = userService.save(header, userId, userDto);
                    }
                    if (id > 0) {
                        sResponse = new CustomResponse(true, messageSource.getMessage("user.ctrl.create", null, locale), id);
                        logger.info("{}<<:Response:{}", header, sResponse);
                        if (isSendMail) {
                            mailQueueService.sendMail(header, id);
                        }
                        auditService.saveAuditDetails(header, userId, AuditMessage.UserCreate.getMsg(), AuditStatus.SUCCESS);;
                        return ResponseEntity.status(HttpStatus.CREATED).body(sResponse);
                    } else {
                        sResponse = new CustomResponse(false, messageSource.getMessage("user.ctrl.createfail", null, locale));
                        logger.info("{}<<:Response:{}", header, sResponse);
                        auditService.saveAuditDetails(header, userId, AuditMessage.UserCreateFail.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(sResponse);
                    }
                } else {
                    sResponse = new CustomResponse(false, messageSource.getMessage("user.ctrl.email.exists", null, locale));
                    logger.info("{}<<:Response:{}", header, sResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.UserCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.UserEmailFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(sResponse);
                }
            } else {
                sResponse = new CustomResponse(false, messageSource.getMessage("user.ctrl.username.exists", null, locale));
                logger.info("{}<<:createUser:Response:{}", header, sResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.UserCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.UserNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(sResponse);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createUser:userDto:{}:Error:{}", header, userDto, e.getMessage());
            sResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, sResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.UserCreateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sResponse);
        }
    }

    @PutMapping(path = "{userid}/update", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateUser(@RequestBody UserUDTO userDto, Locale locale,
            @PathVariable("userid") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "UPDATE_USER") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse sResponse = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:userDto:{}", header, userId, userDto);
            if (userService.validationUserNameByUserId(header, userDto)) {
                Long id = userService.update(header, userId, userDto);
                if (id > 0) {
                    sResponse = new CustomResponse(true, messageSource.getMessage("user.ctrl.update", null, locale), id);
                    logger.info("{}<<:Response:{}", header, sResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.UserUpdate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(sResponse);
                } else {
                    sResponse = new CustomResponse(false, messageSource.getMessage("user.ctrl.updatefail", null, locale));
                    logger.info("{}<<:updateUser:Response:{}", header, sResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.UserUpdateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(sResponse);
                }
            } else {
                sResponse = new CustomResponse(false, messageSource.getMessage("user.ctrl.username.exists", null, locale));
                logger.info("{}<<:updateUser:Response:{}", header, sResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.UserCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.UserNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(sResponse);
            }
        } catch (Exception e) {
            logger.error("{}Excep:updateUser:userId:{}:userDto:{}:Error:{}", header, userId, userDto, e.getMessage());
            sResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, sResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.UserUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sResponse);
        }
    }

    @GetMapping(path = "{userId}/all/{roleId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<UsersGetDto> getUsersByRoleId(Locale locale,
            @PathVariable("userId") long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_USERS") String tranId,
            @PathVariable("roleId") long roleId,
            @RequestParam(name = "enterpriseId", required = false) Long enterpriseId,
            @RequestParam(name = "locationId", required = false) Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        UsersGetDto usersGetDto = new UsersGetDto();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            TblRole role = roleService.getRoleCode(roleId);
            switch (role.getRoleCode().toUpperCase()) {
                case "SA":
                    logger.info("{}>>:roleId:[{}]:enterpriseId:{}:locationId:{}", header, roleId, enterpriseId, locationId, enterpriseId);
                    usersGetDto = userService.getUsersByRoleCode(header, roleId);
                    break;
                case "EA":
                    logger.info("{}>>:roleId:[{}]:enterpriseId:{}:locationId:{}", header, roleId, enterpriseId, locationId);
                    if (locationId == 0) {
                        usersGetDto = userService.getUsersByRoleIdAndErnterpriseId(header, enterpriseId, roleId);
                    } else {
                        usersGetDto = userService.getUsersByRoleIdAndErnterpriseIdAndLocationId(header, enterpriseId, locationId, roleId, userId);
                    }
                    break;
                default:
                    logger.info("{}>>:roleId:[{}]:enterpriseId:{}:locationid:{}", header, roleId, enterpriseId, locationId);
                    usersGetDto = userService.getUsersByRoleIdAndErnterpriseIdAndLocationId(header, enterpriseId, locationId, roleId, userId);
                    break;
            }
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}<<Response:Users-Size:[{}]", header, usersGetDto.getUsers().size());
            auditService.saveAuditDetails(header, userId, AuditMessage.UserGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(usersGetDto);
        } catch (Exception e) {
            logger.error("{}Excep:getUsersByRoleId:roleId:[{}]:enterpriseId:{}:locationid:{}:Error:{}", header, enterpriseId, locationId, e.getMessage());
            e.printStackTrace();
            auditService.saveAuditDetails(header, userId, AuditMessage.UserGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsersGetDto(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "/{userid}/status-update/{activate-id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateUserStatus(
            Locale locale,
            @PathVariable("activate-id") long activateId,
            @PathVariable("userid") long userId,
            @RequestBody StatusDto dto,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_USER_STATUS") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse cResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>activateId:{}:dto:{}", header, activateId, dto);
            activateId = userService.updateUserStatus(header, activateId, dto.getIsActive(), userId);
            if (activateId > 0) {
                cResponse = new CustomResponse(true, messageSource.getMessage("user.ctrl.activate", null, locale), activateId);
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.UserStatusUpdate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(cResponse);
            } else {
                cResponse = new CustomResponse(false, messageSource.getMessage("user.ctrl.activatefail", null, locale), activateId);
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.UserStatusUpdateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(cResponse);
            }
        } catch (Exception ex) {
            logger.info("{}Excep:updateUserStatus:[{}]:activateId:{}:dto:{}:Error:{}", header, activateId, dto, tranId, ExceptionUtils.getRootCauseMessage(ex));
            cResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<Resposne:{}", header, cResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.UserStatusUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(ex));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cResponse);
        }
    }

    @GetMapping(path = "{userid}/all/usertype",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<UserTypeGDto> getUserType(
            Locale locale,
            @PathVariable("userid") long userId,
            @RequestHeader(value = "tranid", defaultValue = "GET_USERTYPE") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = sb.toString();
        try {
            logger.info("{}>>userId:[{}]", header, userId);
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            UserTypeGDto userTypeGDto = new UserTypeGDto(new ArrayList() {
                {
                    add(new UserType("Act", "Active Directory User"));
                    add(new UserType("Nrml", "Normal User"));
                }
            });
            logger.info("{}<<:response:{}", header, userTypeGDto);
            auditService.saveAuditDetails(header, userId, AuditMessage.UserTypeGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(userTypeGDto);
        } catch (Exception e) {
            logger.error("{}Excep:getUserTypeDto:Error:{}", header, e.getMessage());
            UserTypeGDto userTypeGDto = new UserTypeGDto(new ArrayList<>());
            logger.info("{}<<:response:{}", header, userTypeGDto);
            auditService.saveAuditDetails(header, userId, AuditMessage.UserTypeGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserTypeGDto());
        }
    }

    @GetMapping(path = "{userId}/doctors/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<UsersGetDto> getDoctorsByRoleId(Locale locale,
            @PathVariable("userId") long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_DOCTORS") String tranId,
            @PathVariable(name = "locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        UsersGetDto usersGetDto = new UsersGetDto();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            usersGetDto = userService.getDoctorsByRoleIdAndAndLocationId(header, locationId);
            logger.info("{}>>locationid:{}", header, locationId);
            auditService.saveAuditDetails(header, userId, AuditMessage.UserGet.getMsg(), AuditStatus.SUCCESS);
            logger.info("{}<<locationid:{}", header, locationId);
            return ResponseEntity.status(HttpStatus.OK).body(usersGetDto);
        } catch (Exception e) {
            logger.error("{}Excep:getDoctorssByRoleId:locationId:{}:Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.UserGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsersGetDto(new ArrayList<>()));
        }
    }

    @GetMapping(path = "{userId}/retain-doctors/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<UsersGetDto> getMedicalServiceRetainDoctorsByRoleId(Locale locale,
            @PathVariable("userId") long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_DOCTORS") String tranId,
            @PathVariable(name = "locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        UsersGetDto usersGetDto = new UsersGetDto();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            usersGetDto = userService.getNonAssignedDoctorsByRoleIdAndAndLocationId(header, locationId);
            logger.info("{}>>locationid:{}", header, locationId);
            auditService.saveAuditDetails(header, userId, AuditMessage.UserGet.getMsg(), AuditStatus.SUCCESS);
            logger.info("{}<<locationid:{}", header, locationId);
            return ResponseEntity.status(HttpStatus.OK).body(usersGetDto);
        } catch (Exception e) {
            logger.error("{}Excep:getDoctorssByRoleId:locationId:{}:Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.UserGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsersGetDto(new ArrayList<>()));
        }
    }

    @PutMapping(path = "{userId}/update-password", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updatePassword(
            Locale locale,
            @PathVariable("userId") long userId,
            @RequestBody UserUpPasswrd dto,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_PASSWORD") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse sResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}Enter:updatePassword:userId:[{}]", header, userId);
            int id = userService.updatePassword(header, userId, dto);
            if (id > 0) {
                sResponse = new CustomResponse(true, messageSource.getMessage("user.ctrl.passwrd.update", null, locale));
                logger.info("{}<<:Response:{}", header, sResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.UserPasswordUpdate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(sResponse);
            } else if (id == -1) {
                sResponse = new CustomResponse(true, messageSource.getMessage("user.ctrl.passwrd.notmatch", null, locale));
                logger.info("{}<<:Response:{}", header, sResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.CurrentPasswordNotMatch.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(sResponse);
            } else {
                sResponse = new CustomResponse(false, messageSource.getMessage("user.ctrl.passwrd.updatefail", null, locale));
                logger.info("{}<<:updatePassword:Response:{}", header, sResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.UserPasswordUpdateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(sResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:updatePassword:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            sResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.UserPasswordUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sResponse);
        } finally {
            sb = null;
            header = null;
            sResponse = null;
        }
    }

    @PutMapping(path = "{userId}/update-profile", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateUserProfile(@RequestBody ProfileUpDto userDto, Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "UPDATE_PROFILE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse sResponse = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:userDto:{}", header, userId, userDto);
            Long id = userService.updateUserProfile(header, userId, userDto);
            if (id > 0) {
                sResponse = new CustomResponse(true, messageSource.getMessage("user.ctrl.profile.update", null, locale), id);
                logger.info("{}<<:Response:{}", header, sResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.UserProfileUpdate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(sResponse);
            } else {
                sResponse = new CustomResponse(false, messageSource.getMessage("user.ctrl.profile.updatefail", null, locale));
                logger.info("{}<<:updateUserProfile:Response:{}", header, sResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.UserProfileUpdateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(sResponse);
            }

        } catch (Exception e) {
            logger.error("{}Excep:updateUserProfile:userId:{}:userDto:{}:Error:{}", header, userId, userDto, e.getMessage());
            sResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, sResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.UserProfileUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sResponse);
        }
    }

    @GetMapping(path = "{userId}/resources/{locationId}/{serviceId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<DoctorResponse> getAllDoctorsByServiceId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @PathVariable("locationId") Long locationId,
            @PathVariable("serviceId") Long serviceId,
            @RequestHeader(value = "tranId", defaultValue = "GET_DOCTORS") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:userId:[{}],serviceId:[{}],LocationId:[{}]", header, userId, serviceId, locationId);
            DoctorResponse doctors = userService.getAllDoctorsByServiceId(header, userId, locationId, serviceId);
            logger.trace("{}<<Response:{}", header, doctors);
            auditService.saveAuditDetails(header, userId, AuditMessage.DoctorsGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(doctors);
        } catch (Exception e) {
            logger.error("{}Excep:getAllDoctorsByServiceId:userId:{}:Error:{}", header, userId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.DoctorsGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<getAllDoctorsByServiceId:Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DoctorResponse());
        } finally {
            sb = null;
            header = null;
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2.utils.Hider;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.LoginRequest;
import com.vm.qsmart2api.dtos.LoginResponseDto;
import com.vm.qsmart2api.dtos.UserDetails;
import com.vm.qsmart2api.dtos.enterprise.EnterpriseGDto;
import com.vm.qsmart2api.dtos.location.LocationDto;
import com.vm.qsmart2api.dtos.menus.CurrentDateTimeDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.enums.Status;
import com.vm.qsmart2api.repository.UserProfileRepository;
import com.vm.qsmart2api.security.JwtTokenUtil;
import com.vm.qsmart2api.service.ActiveDirectoryService;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.UserService;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ashok
 */
@RestController
@RequestMapping("auth/")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserService userService;

    @Autowired
    ActiveDirectoryService actService;

    @Autowired
    AuditService auditService;

    @Autowired
    UserProfileRepository userProfileRepository;

    @PostMapping(path = "/login", produces = {"application/json", "application/xml"})
    public ResponseEntity<LoginResponseDto> createAuthenticationToken(
            Locale locale,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_AUTH") String tranId,
            @RequestBody LoginRequest authenticationRequest,
            HttpServletResponse response) {
        LoginResponseDto loginResponse = null;
        String header = "[" + tranId + "] ";
        UserDetails userDetails = null;
        try {
            logger.info("{}>>Request:{}", header, authenticationRequest);
            if (authenticationRequest.getUsername() != null && !authenticationRequest.getUsername().isEmpty()) {
                userDetails = userService.getUserByUsername(header, authenticationRequest.getUsername());
                if (userDetails != null) {
                    String userType = userDetails.getUserType().toUpperCase();
                    boolean authenticate = false;
                    switch (userType) {
                        case "ACT":
                            int auth = actService.authentionWithActiveDirectory(header, userDetails.getUserId(), locale, userDetails.getEnterprise().getEnterpriseId(), userDetails.getUserName(), authenticationRequest.getPassword());
                            switch (auth) {
                                case 0:
                                    loginResponse = new LoginResponseDto(false, messageSource.getMessage("active.directory.info.not.fouund", null, locale));
                                    logger.info("{}>>Response:{}", header, loginResponse);
                                    auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, AuditMessage.ActiveDirectoryInfo.getMsg());
                                    return ResponseEntity.status(HttpStatus.OK).header("token", "").body(loginResponse);
                                case 1:
                                    authenticate = true;
                                    break;
                                case 2:
                                    loginResponse = new LoginResponseDto(false, messageSource.getMessage("invalid.creadentials", null, locale));
                                    logger.info("{}>>Response:{}", header, loginResponse);
                                    auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, AuditMessage.InvalidCreadential.getMsg());
                                    return ResponseEntity.status(HttpStatus.OK).header("token", "").body(loginResponse);
                                case 3:
                                    loginResponse = new LoginResponseDto(false, messageSource.getMessage("unable.connect.active.directory", null, locale));
                                    logger.info("{}>>Response:{}", header, loginResponse);
                                    auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, AuditMessage.UnableconnectActiveDirectory.getMsg());
                                    return ResponseEntity.status(HttpStatus.OK).header("token", "").body(loginResponse);
                                default:
                                    loginResponse = new LoginResponseDto(false, messageSource.getMessage("active.directory.info.not.found.4", null, locale));
                                    logger.info("{}>>Response:{}", header, loginResponse);
                                    auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, AuditMessage.ActiveDirectoryInfo.getMsg());
                                    return ResponseEntity.status(HttpStatus.OK).header("token", "").body(loginResponse);
                            }
                        default:
                            if (Hider.getInstance().decrypt(userDetails.getHashPassword()).equals(authenticationRequest.getPassword())) {
                                authenticate = true;
                            }
                            break;
                    }
                    if (authenticate) {
                        if (userDetails.getIsActive() == Status.ACTIVE.getValue()) {
                            EnterpriseGDto enterprise = userDetails.getEnterprise();
                            if (enterprise != null) {
                                if (enterprise.getIsActive() == Status.ACTIVE.getValue()) {
                                    LocationDto location = userDetails.getLocation();
                                    if (location != null) {
                                        if (location.getStatus() == Status.ACTIVE.getValue()) {
                                            if (userDetails.getRoles().getRoleType() == 1) {
                                                int count = userProfileRepository.getCountByUserId(userDetails.getUserId());
                                                if (count > 0) {
                                                    userDetails.setIsUserProfile(true);
                                                } else {
                                                    userDetails.setIsUserProfile(false);
                                                }
                                            }
                                            loginResponse = new LoginResponseDto(true, messageSource.getMessage("auth.ctrl.login", null, locale));
                                            loginResponse.setUserDetails(userDetails);
                                            response.addHeader("access-control-expose-headers", "Authorization");
                                            logger.info("{}>>Response:{}", header, loginResponse);
                                            auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.AuthLogin.getMsg(), AuditStatus.SUCCESS);
                                            HttpHeaders headers = new HttpHeaders();
                                            headers.add("Authorization", jwtTokenUtil.genrateJwtToken(userDetails, userDetails.getEnterprise() != null ? userDetails.getEnterprise().getEnterpriseId() : 0));
                                            return new ResponseEntity(loginResponse, headers, HttpStatus.OK);
//                                            return ResponseEntity.status(HttpStatus.OK).header("Authorization", jwtTokenUtil.genrateJwtToken(userDetails, userDetails.getEnterprise() != null ? userDetails.getEnterprise().getEnterpriseId() : 0)).body(loginResponse);
                                        } else {
                                            loginResponse = new LoginResponseDto(false, messageSource.getMessage("location.deactivated", null, locale));
                                            logger.info("{}>>Response:{}", header, loginResponse);
                                            auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, AuditMessage.LocationDeactivate.getMsg());
                                            return ResponseEntity.status(HttpStatus.OK).header("token", "").body(loginResponse);
                                        }
                                    } else {
                                        loginResponse = new LoginResponseDto(true, messageSource.getMessage("auth.ctrl.login", null, locale));
                                        loginResponse.setUserDetails(userDetails);
                                        response.addHeader("access-control-expose-headers", "Authorization");
                                        logger.info("{}>>Response:{}", header, loginResponse);
                                        auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.AuthLogin.getMsg(), AuditStatus.SUCCESS);
                                        HttpHeaders headers = new HttpHeaders();
                                        headers.add("Authorization", jwtTokenUtil.genrateJwtToken(userDetails, userDetails.getEnterprise() != null ? userDetails.getEnterprise().getEnterpriseId() : 0));
                                        return new ResponseEntity(loginResponse, headers, HttpStatus.OK);
//                                        return ResponseEntity.status(HttpStatus.OK).header("Authorization", jwtTokenUtil.genrateJwtToken(userDetails, userDetails.getEnterprise() != null ? userDetails.getEnterprise().getEnterpriseId() : 0)).body(loginResponse);
                                    }
                                } else {
                                    loginResponse = new LoginResponseDto(false, messageSource.getMessage("enterprise.deactivated", null, locale));
                                    logger.info("{}>>Response:{}", header, loginResponse);
                                    auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, AuditMessage.EnterpriseDeactivate.getMsg());
                                    return ResponseEntity.status(HttpStatus.OK).header("token", "").body(loginResponse);
                                }
                            } else {
                                loginResponse = new LoginResponseDto(true, messageSource.getMessage("auth.ctrl.login", null, locale));
                                loginResponse.setUserDetails(userDetails);
                                response.addHeader("access-control-expose-headers", "Authorization");
                                logger.info("{}>>Response:{}", header, loginResponse);
                                auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.AuthLogin.getMsg(), AuditStatus.SUCCESS);
                                HttpHeaders headers = new HttpHeaders();
                                headers.add("Authorization", jwtTokenUtil.genrateJwtToken(userDetails, userDetails.getEnterprise() != null ? userDetails.getEnterprise().getEnterpriseId() : 0));
                                return new ResponseEntity(loginResponse, headers, HttpStatus.OK);
//                                return ResponseEntity.status(HttpStatus.OK).header("Authorization", jwtTokenUtil.genrateJwtToken(userDetails, userDetails.getEnterprise() != null ? userDetails.getEnterprise().getEnterpriseId() : 0)).body(loginResponse);
                            }
                        } else {
                            loginResponse = new LoginResponseDto(false, messageSource.getMessage("auth.user.deactivated", null, locale));
                            logger.info("{}>>Response:{}", header, loginResponse);
                            auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, AuditMessage.EnterpriseDeactivate.getMsg());
                            return ResponseEntity.status(HttpStatus.OK).header("token", "").body(loginResponse);
                        }
                    } else {
                        loginResponse = new LoginResponseDto(false, messageSource.getMessage("invalid.password", null, locale));
                        logger.info("{}>>Response:{}", header, loginResponse);
                        auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, AuditMessage.InvalidPassword.getMsg());
                        return ResponseEntity.status(HttpStatus.OK).header("token", "").body(loginResponse);
                    }
                } else {
                    loginResponse = new LoginResponseDto(false, messageSource.getMessage("invalid.username", null, locale));
                    logger.info("{}>>Response:{}", header, loginResponse);
                    // auditService.saveAuditDetails(header, 0l, AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, AuditMessage.InvalidUsername.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).header("token", "").body(loginResponse);
                }
            } else {
                loginResponse = new LoginResponseDto(false, messageSource.getMessage("invalid.request", null, locale));
                logger.info("{}>>Response:{}", header, loginResponse);
                // auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, AuditMessage.InvalidRequest.getMsg());
                return ResponseEntity.status(HttpStatus.OK).header("token", "").body(loginResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:createAuthenticationToken:LoginRequest:[{}]:Error:{}", header, authenticationRequest, ExceptionUtils.getRootCauseMessage(e));
            loginResponse = new LoginResponseDto(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{} >> Response:{}", header, loginResponse);
            auditService.saveAuditDetails(header, userDetails.getUserId(), AuditMessage.UserLogin.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponse);
        } finally {
            userDetails = null;
            loginResponse = null;
        }
    }

    @Value("${license.dest.path}")
    private String path;

    @GetMapping(path = "/{logo}")
    public ResponseEntity<byte[]> getImage(@RequestHeader(value = "tranId", defaultValue = "UPOLOAD_LOGO_FILE") String tranId,
            Locale locale,
            @PathVariable("logo") String logo) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tranId).append("_").append("] ");
        String header = sb.toString().toUpperCase();
        logger.info("{} >> FileName:[{}]", header, logo);
        try {
            File img = new File(path + "/" + logo);
            return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
        } catch (Exception e) {
            logger.error("{}Excep:getImage:Exception:{}", header, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.ok().body(new byte[0]);
        }
    }

    @GetMapping(path = "/{userId}/logout", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> logout(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "TOKEN_LOGOUT") String tranId,
            @RequestHeader(value = "token", defaultValue = "ejlkjadljflasd") String token,
            @RequestParam(value = "reason") String reason) {
        String header = "[" + tranId + "] ";
        CustomResponse response = null;
        try {
            logger.info("{}>>Request:token:{}", header, token);
            jwtTokenUtil.tokenLogout(token);
            response = new CustomResponse(true, messageSource.getMessage("user.logout.succcessfully", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.UserLogout.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:logout:token:{}:Error:{}", header, token, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(true, messageSource.getMessage("user.logout.fail", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.UserLogout.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @Autowired
    DateUtils dateUtils;

    @GetMapping(path = "/{userId}/current-date-time",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CurrentDateTimeDto> getCurrentDate(Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "CURRENT_DATE_TIME") String tranId) {
        CurrentDateTimeDto dto = null;
        try {
            logger.info("{}>>:getCurrentDate");
            dto = new CurrentDateTimeDto();
            dto.setDateTime(dateUtils.getCurrenDateTime());
            dto.setDate(dateUtils.sdfYear.get().format(dateUtils.getdate()));
            dto.setTime(dateUtils.sdfTime.get().format(dateUtils.getdate()));
            dto.setHours(new SimpleDateFormat("HH").format(dateUtils.getdate()));
            dto.setMinutes(new SimpleDateFormat("mm").format(dateUtils.getdate()));
            dto.setSeconds(new SimpleDateFormat("ss").format(dateUtils.getdate()));
            logger.info("{}<<:getCurrentDate:Response:{}", (dto != null));

            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (Exception e) {
            logger.error("{}Excep:getCurrentDate:Error:{}", e.getMessage());
            return new ResponseEntity(new CurrentDateTimeDto(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            dto = null;
        }
    }

}

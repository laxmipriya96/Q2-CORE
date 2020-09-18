/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2.model.TblLanguage;
import com.vm.qsmart2.model.TblTimeZone;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.StatusDto;
import com.vm.qsmart2api.dtos.enterprise.EnterprisesDetails;
import com.vm.qsmart2api.dtos.enterprise.LanguageDto;
import com.vm.qsmart2api.dtos.enterprise.LanguageGetDto;
import com.vm.qsmart2api.dtos.enterprise.TimeZoneDto;
import com.vm.qsmart2api.dtos.enterprise.TimeZoneGetDto;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.LanguageRepository;
import com.vm.qsmart2api.repository.TimeZoneRepository;
import com.vm.qsmart2api.service.EnterpriseService;
import com.vm.qsmart2api.dtos.enterprise.EnterpriseCreateRequest;
import com.vm.qsmart2api.dtos.enterprise.EnterpriseGDto;
import com.vm.qsmart2api.dtos.enterprise.FileResponse;
import com.vm.qsmart2api.dtos.enterprise.LicenseDetails;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.repository.EnterpriseRepository;
import com.vm.qsmart2api.service.AuditService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Tejasri
 */
@RestController
@RequestMapping("enterprise/")
public class EnterpriseController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    EnterpriseService enterpriseService;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    TimeZoneRepository timeZoneRepository;

    @Autowired
    EnterpriseRepository enterpriseRepo;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @GetMapping(path = "{userId}/time-zones",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<TimeZoneGetDto> getAllTimeZones(
            Locale locale,
            @PathVariable("userId") long userId,
            @RequestHeader(value = "tranId", defaultValue = "TIME_ZONES") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        TimeZoneGetDto time = new TimeZoneGetDto();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>Request:[{}]", header, userId);
            List<TblTimeZone> times = timeZoneRepository.findAll();
            List<TimeZoneDto> timeDto = Mapper.INSTANCE.timeZoneEntityListToListDto(times);
            time.setTimeZones(timeDto);
            logger.info("{}<<Response:{}", header, time);
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseTimeGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(time);
        } catch (Exception e) {
            logger.error("{}Excep:getAllTimeZones:userId:{}:Error:{}", header, userId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseTimeGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TimeZoneGetDto(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;

        }
    }

    @GetMapping(path = "{userId}/languages",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<LanguageGetDto> getAllLanguages(
            Locale locale,
            @PathVariable("userId") long userId,
            @RequestHeader(value = "tranId", defaultValue = "LANGUAGES") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        LanguageGetDto languge = new LanguageGetDto();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>Request:[{}]", header, userId);
            List<TblLanguage> language = languageRepository.findAll();
            List<LanguageDto> langDto = Mapper.INSTANCE.languageEntityListToListDto(language);
            languge.setLanguages(langDto);
            logger.info("{}<<Response:{}", header, languge);
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseLanguageGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(languge);
        } catch (Exception e) {
            logger.error("{}Excep:getAllLanguages:userId:{}:Error:{}", header, userId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseLanguageGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LanguageGetDto(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;

        }
    }

    @PostMapping(path = "/{userid}/create", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createEnterprise(
            Locale locale,
            @RequestBody EnterpriseCreateRequest enterprise,
            @PathVariable("userid") long userId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_USER") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse cResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>enterprise:{}", header, enterprise);
            String filePath = enterprise.getLicenseFile();
            if (filePath != null && !filePath.isEmpty()) {
                // if (renameFile(header, enterprise.getEnterpriseNameEn(), filePath)) {
                long enterpirseId = enterpriseService.save(header, enterprise, userId);
                if (enterpirseId > 0) {
                    cResponse = new CustomResponse(true, messageSource.getMessage("enterprise.ctrl.create", null, locale), enterpirseId);
                    logger.info("{}<<response:{}", header, cResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseCreate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(cResponse);
                } else {
                    cResponse = new CustomResponse(false, messageSource.getMessage("enterprise.ctrl.createfail", null, locale), enterpirseId);
                    logger.info("{}<<response:{}", header, cResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseCreateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(cResponse);
                }
//                } else {
//                    cResponse = new CustomResponse(false, messageSource.getMessage("enterprise.ctrl.invalid.license", null, locale));
//                    logger.info("{} << response:{}", header, cResponse);
//                    return ResponseEntity.status(HttpStatus.OK).body(cResponse);
//                }
            } else {
                cResponse = new CustomResponse(false, messageSource.getMessage("enterprise.ctrl.license.empty", null, locale));
                logger.info("{} << response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.EnterpriseLicenseGetFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(cResponse);
            }
        } catch (Exception ex) {
            logger.info("{}Excep:createEnterprise:[{}]:userId:{}:Error:{}", header, userId, tranId, ExceptionUtils.getRootCauseMessage(ex));
            cResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseCreateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(ex));
            logger.info("{} << Resposne:{}", header, cResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cResponse);
        }
    }

    @PutMapping(path = "/{userid}/update", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateEnterprise(
            Locale locale,
            @RequestBody EnterpriseGDto enterprise,
            @PathVariable("userid") long userId,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_ENTERPRISE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse cResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>enterprise:{}", header, enterprise);
            String filePath = enterprise.getLicenseFile();
            if (filePath != null && !filePath.isEmpty()) {
                // if (renameFile(header, enterprise.getEnterpriseNameEn(), filePath)) {
                long enterpirseId = enterpriseService.updateEnterprise(header, enterprise, userId);
                if (enterpirseId > 0) {
                    cResponse = new CustomResponse(true, messageSource.getMessage("enterprise.ctrl.update", null, locale), enterpirseId);
                    logger.info("{}<<response:{}", header, cResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseUpdate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(cResponse);
                } else {
                    cResponse = new CustomResponse(false, messageSource.getMessage("enterprise.ctrl.updatefail", null, locale), enterpirseId);
                    logger.info("{}<<response:{}", header, cResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseUpdateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(cResponse);
                }

            } else {
                cResponse = new CustomResponse(false, messageSource.getMessage("enterprise.ctrl.license.empty", null, locale));
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.EnterpriseLicenseGetFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(cResponse);
            }
        } catch (Exception ex) {
            logger.info("{}Excep:updateEnterprise:[{}]:userId:{}:Error:{}", header, userId, tranId, ExceptionUtils.getRootCauseMessage(ex));
            cResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(ex));
            logger.info("{} << Resposne:{}", header, cResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cResponse);
        }
    }

    @GetMapping(path = "{userid}/all",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<EnterprisesDetails> getEnterpriseInfo(
            Locale locale,
            @RequestHeader(value = "tranId", defaultValue = "GET_ENTERPRISE") String tranId,
            @PathVariable("userid") long userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tranId).append("_").append(userId).append("] ");
        String header = sb.toString().toUpperCase();
        EnterprisesDetails sResponse = null;
        logger.info("{}>>userId:[{}]", header, userId);
        try {
            sResponse = enterpriseService.getAllEnterprisesDetails(header, locale);
            logger.info("{}<<Response:Enteprise-size:[{}}", header, sResponse.getEnterprises().size());
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseGet.getMsg(), AuditStatus.SUCCESS);
            return new ResponseEntity(sResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("{}Excep:getEnterpriseInfo:userId:{}:Error;[{}}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            sResponse = new EnterprisesDetails(new ArrayList<>());
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            logger.info("{}<<Response:[{}}", header, sResponse);
            return new ResponseEntity(sResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            sResponse = null;
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "/{userid}/status-update/{enterpriseid}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateStatus(
            Locale locale,
            @PathVariable("enterpriseid") long enterpriseId,
            @PathVariable("userid") long userId,
            @RequestBody StatusDto dto,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_ENTERPRISE_STATUS") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse cResponse = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>enterpriseId:{}:dto:{}", header, enterpriseId, dto);
            enterpriseId = enterpriseService.updateEnterpriseStatus(header, enterpriseId, dto.getIsActive(), userId);
            if (enterpriseId > 0) {
                cResponse = new CustomResponse(true, messageSource.getMessage("enterprise.ctrl.activate", null, locale), enterpriseId);
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseStatusUpdate.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(cResponse);
            } else {
                cResponse = new CustomResponse(false, messageSource.getMessage("enterprise.ctrl.activatefail", null, locale), enterpriseId);
                logger.info("{}<<response:{}", header, cResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseStatusUpdateFail.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(cResponse);
            }
        } catch (Exception ex) {
            logger.info("{}Excep:updateEnterprise:[{}]:enterpriseId:{}:dto:{}:Error:{}", header, tranId, enterpriseId, dto, ExceptionUtils.getRootCauseMessage(ex));
            cResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseStatusUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(ex));
            logger.info("{} << Resposne:{}", header, cResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cResponse);
        }
    }

    @Value("${license.dest.path}")
    private String path;

    @Value("${logo.context.path}")
    private String contextPath;

    @ApiImplicitParams(
            {
                @ApiImplicitParam(name = "file", required = true, dataType = "file", value = "Upload data *.l4j")
            }
    )
    @PostMapping(path = "{userid}/license/upload",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<LicenseDetails> uploadAndValidate(
            Locale locale,
            @RequestHeader(value = "tranId", defaultValue = "UPOLOAD_LICENSE_FILE") String tranId,
            @PathVariable("userid") long userId, @RequestPart("file") MultipartFile file) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tranId).append("_").append(userId).append("] ");
        String header = sb.toString().toUpperCase();
        String destFile = null;
        logger.info("{}>>file:[{}]", header, file.getOriginalFilename());
        LicenseDetails sResponse = null;
        try {
            destFile = path + System.currentTimeMillis() + ".l4j";
            Files.write(Paths.get(destFile), file.getBytes());
            sResponse = enterpriseService.readDetailsfromLicense(header, destFile, locale);
            logger.info("{}<<Response:[{}}", header, sResponse.toString());
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseFileUpload.getMsg(), AuditStatus.SUCCESS);
            return new ResponseEntity(sResponse, HttpStatus.OK);
        } catch (Exception e) {
            sResponse = new LicenseDetails(false, 0, messageSource.getMessage("common.wrong.message", null, locale));
            logger.error("{}Excep:uploadAndValidate:userId:{}:Error;[{}}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseFileUploadFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<Response:[{}}", header, sResponse.toString());
            return new ResponseEntity(sResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            sb = null;
            header = null;
            sResponse = null;
            destFile = null;
        }
    }

    @ApiImplicitParams(
            {
                @ApiImplicitParam(name = "file", required = true, dataType = "file", value = "Upload image file.jpeg|.jpg|.png")
            }
    )
    @PostMapping(path = "{userid}/logo/upload", produces = {"application/json", "application/xml"})
    public ResponseEntity<FileResponse> uplaodLogoFile(
            @RequestHeader(value = "tranId", defaultValue = "UPOLOAD_LOGO_FILE") String tranId,
            Locale locale,
            @PathVariable("userid") long userId,
            @RequestPart(value = "file", required = true) MultipartFile file) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tranId).append("_").append(userId).append("] ");
        String header = sb.toString().toUpperCase();
        String destFile = null;
        logger.info("{}>>FileName:[{}]", header, file.getOriginalFilename());
        try {
            String updatedFileName = System.currentTimeMillis() + ".jpg";
            destFile = path + updatedFileName;
            Files.write(Paths.get(destFile), file.getBytes());
            byte[] imgData = enterpriseService.convertFileIntoByteArray(header, new File(destFile));
            FileResponse fResponse = new FileResponse(true, messageSource.getMessage("enterprise.ctrl.logo.upload", null, locale), contextPath + updatedFileName, ArrayUtils.toObject(imgData));
            logger.info("{}<<Response:[{}]", fResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseLogoUpload.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.CREATED).body(fResponse);
        } catch (Exception e) {
            logger.error("{}Excep:uploadAndValidate:userId:{}:Error;[{}}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            FileResponse fResponse = new FileResponse(false, messageSource.getMessage("enterprise.ctrl.logo.uploadfail", null, locale), null);
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseLogoUploadFile.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<Response:[{}]", fResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fResponse);
        } finally {
            sb = null;
            header = null;
            destFile = null;
        }
    }
    
    
    @GetMapping(path = "{userid}/license-info",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<LicenseDetails> getLicenseInfo(
            Locale locale,
            @RequestHeader(value = "tranId", defaultValue = "LICENSE_INFO") String tranId,
            @PathVariable("userid") long userId, 
            @RequestParam("filepath") String destFile) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tranId).append("_").append(userId).append("] ");
        String header = sb.toString().toUpperCase();
        logger.info("{}>>filePath:[{}]", header, destFile);
        LicenseDetails sResponse = null;
        try {
            sResponse = enterpriseService.readDetailsfromLicense(header, destFile, locale);
            logger.info("{}<<Response:[{}}", header, sResponse.toString());
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseFileUpload.getMsg(), AuditStatus.SUCCESS);
            return new ResponseEntity(sResponse, HttpStatus.OK);
        } catch (Exception e) {
            sResponse = new LicenseDetails(false, 0, messageSource.getMessage("common.wrong.message", null, locale));
            logger.error("{}Excep:uploadAndValidate:userId:{}:Error;[{}}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.EnterpriseFileUploadFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<Response:[{}}", header, sResponse.toString());
            return new ResponseEntity(sResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            sb = null;
            header = null;
            sResponse = null;
            destFile = null;
        }
    }

}

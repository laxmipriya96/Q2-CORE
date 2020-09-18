/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2.model.TblBranchType;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.branch.BranchDTO;
import com.vm.qsmart2api.dtos.branch.BranchGUDTO;
import com.vm.qsmart2api.dtos.branch.BranchResponse;
import com.vm.qsmart2api.dtos.branch.BranchRoomDto;
import com.vm.qsmart2api.dtos.branch.BranchTypeGDTO;
import com.vm.qsmart2api.dtos.branch.BranchUDTO;
import com.vm.qsmart2api.dtos.branch.BranchesServicesResponse;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.BranchTypeService;
import com.vm.qsmart2api.repository.BranchRepository;
import com.vm.qsmart2api.repository.BranchTypeRepository;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.BranchService;
import com.vm.qsmart2api.service.RoomService;
import com.vm.qsmart2api.service.ServiceService;
import java.util.Locale;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * @author ASHOK
 */
@RestController
@RequestMapping("branch/")
public class BranchController {

    public static final Logger logger = LogManager.getLogger(BranchController.class);

    @Autowired
    BranchRepository branchRepo;

    @Autowired
    BranchService branchServc;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @Autowired
    BranchTypeService branchTypeService;

    @Autowired
    BranchTypeRepository branchTypeRepository;

    @Autowired
    ServiceService serviceService;

    @GetMapping(path = "{userId}/allBranchTypes",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<BranchTypeGDTO> getAllBrancheTypes(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_BRANCHETYPES") String tranId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>allBranchTypes:Request:[{}]", header, userId);
            BranchTypeGDTO branch = branchTypeService.getAllBrancheTypes(header, userId);
            logger.info("{}<<allBranchTypes:Response:{}", header, branch);
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchTypeGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(branch);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBrancheTypes:Error:{}", header, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchTypeGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<allBranchTypes:Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BranchTypeGDTO());
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userId}/all/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<BranchGUDTO> getAllBranchesByLocId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_BRANCHES") String tranId,
            @PathVariable("locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:locationId:[{}]", header, locationId);
            BranchGUDTO branch = branchServc.getAllBranchesByLocationId(header, userId, locationId);
            logger.info("{}<<:Response:Size:[{}]", header, branch.getBranches().size());
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(branch);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesByEntId:locationId:{}:Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            logger.info("{}<<:Response:Size:[{}]", header, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BranchGUDTO());
        } finally {
            sb = null;
            header = null;
        }
    }

    @PostMapping(path = "{userId}/create/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createBranch(Locale locale,
            @Valid @RequestBody BranchDTO branchDto,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "CREATE_BRANCH") String tranId,
            @PathVariable("locationId") Long locationId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();

            logger.info("{}>>header:{},locationId:{},branchDto:{}", header, (branchDto != null));
            if (branchServc.validateBranchEngName(header, locationId, branchDto.getBranchNameEn())) {
                if (branchServc.validateBranchNameArbInDb(header, locationId, branchDto.getBranchNameAr())) {
                    if (branchServc.validateServiceLocationInDb(header, locationId, branchDto.getServiceLocation())) {
                        TblBranchType branchType = branchTypeRepository.getOne(branchDto.getBranchTypeId());
                        long branchId = branchServc.save(header, userId, locationId, branchDto, branchType);
                        if (branchId > 0) {
                            response = new CustomResponse(true, messageSource.getMessage("branch.ctrl.create", null, locale), branchId);
                            logger.info("{}<<:createBranch:Response:{}", header, response);
                            auditService.saveAuditDetails(header, userId, AuditMessage.BranchCreate.getMsg(), AuditStatus.SUCCESS);
                            return ResponseEntity.status(HttpStatus.CREATED).body(response);
                        } else {
                            response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.createfail", null, locale));
                            logger.info("{}<<:createBranch:Response:{}", header, response);
                            auditService.saveAuditDetails(header, userId, AuditMessage.BranchCreateFail.getMsg(), AuditStatus.FAILURE);
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        }
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.servicelocation.exists", null, locale));
                        logger.info("{}<<:createBranch:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.BranchCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ServiceLocationFail.getMsg());
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.arbname.exists", null, locale));
                    logger.info("{}<<:createBranch:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.BranchCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.BranchArbNameFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.engname.exists", null, locale));
                logger.info("{}<<:createBranch:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.BranchCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.BranchEngNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createBranch:header:{}:locationId:{}:Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.fail", null, locale));
            logger.info("{}<<:createBranch:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchCreateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PutMapping(path = "{userId}/update/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateBranch(Locale locale,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody BranchUDTO branch,
            @RequestHeader(value = "tranId", defaultValue = "UPDATE_BRANCH") String tranId,
            @PathVariable("locationId") Long locationId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:{},branch:{}", header, userId, (branch != null));
            if (branchServc.validateBranchEngNameByBranchId(header, locationId, branch.getBranchId(), branch.getBranchNameEn())) {
                if (branchServc.validateBranchArbNameByBranchId(header, locationId, branch.getBranchId(), branch.getBranchNameAr())) {
                    if (branchServc.validateServiceLocationByBranchId(header, locationId, branch.getBranchId(), branch.getServiceLocation())) {
                        Long branchId = branchServc.update(header, userId, locationId, branch);
                        if (branchId > 0) {
                            response = new CustomResponse(true, messageSource.getMessage("branch.ctrl.update", null, locale), branchId);
                            logger.info("{}<<:updateBranch:Response:{}", header, response);
                            auditService.saveAuditDetails(header, userId, AuditMessage.BranchUpdate.getMsg(), AuditStatus.SUCCESS);
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        } else {
                            response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.failupdate", null, locale));
                            logger.info("{}<<:updateBranch:Response:{}", header, response);
                            auditService.saveAuditDetails(header, userId, AuditMessage.BranchUpdateFail.getMsg(), AuditStatus.FAILURE);
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        }
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.servicelocation.exists", null, locale));
                        logger.info("{}<<:updateBranch:Response:{}", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.BranchUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.ServiceLocationFail.getMsg());
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.arbname.exists", null, locale));
                    logger.info("{}<<:updateBranch:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.BranchUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.BranchArbNameFail.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.engname.exists", null, locale));
                logger.info("{}<<:updateBranch:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.BranchUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.BranchEngNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

        } catch (Exception e) {
            logger.error("{}Excep:updateBranch:header:{}:branch:{}:Error:{}", header, branch, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

//    @DeleteMapping(path = "{userId}/delete/{branchId}",
//            produces = {"application/json", "application/xml"})
//    public ResponseEntity<CustomResponse> deleteBranch(
//            Locale locale,
//            @PathVariable("branchId") Long branchId, @PathVariable("userId") Long userId,
//            @RequestHeader(value = "tranId", defaultValue = "DELETE_BRANCH") String tranId) {
//        String header = null;
//        StringBuilder sb = new StringBuilder();
//        CustomResponse response = null;
//        try {
//            sb.append("[").append(tranId).append("_").append(userId).append("] ");
//            header = sb.toString().toUpperCase();
//            logger.info("{}>>branchId:[{}]", header, branchId);
//            int count = serviceService.checkservicesBybranchId(branchId);
//            if (count > 0) {
//                logger.info("<<:Response:{}", header, response);
//                response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.service.map", null, locale));
//                auditService.saveAuditDetails(header, userId, AuditMessage.BranchDeleteFail.getMsg(), AuditStatus.FAILURE, AuditMessage.BranchDeleteFailByService.getMsg());
//                return ResponseEntity.status(HttpStatus.OK).body(response);
//            } else {
//                branchRepo.deleteById(branchId);
//                response = new CustomResponse(true, messageSource.getMessage("branch.ctrl.delete", null, locale));
//                logger.info("<<:Response:{}", header, response);
//                auditService.saveAuditDetails(header, userId, AuditMessage.BranchDelete.getMsg(), AuditStatus.SUCCESS);
//                return ResponseEntity.status(HttpStatus.OK).body(response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("{}Excep:deleteLocation:branchId:{}:Error:{}", header, branchId, ExceptionUtils.getRootCauseMessage(e));
//            response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.faildelete", null, locale));
//            auditService.saveAuditDetails(header, userId, AuditMessage.BranchDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        } finally {
//            sb = null;
//            header = null;
//        }
//    }
    
    @Autowired
    RoomService roomService;
    
    @DeleteMapping(path = "{userId}/delete/{branchId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> deleteBranch(
            Locale locale,
            @PathVariable("branchId") Long branchId, @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "DELETE_BRANCH") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>branchId:[{}]", header, branchId);
            int count = serviceService.checkservicesBybranchId(branchId);
            int countRoom = roomService.checkRoomsBybranchId(branchId);
            if (count > 0) {
                logger.info("<<:Response:{}", header, response);
                response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.service.map", null, locale));
                auditService.saveAuditDetails(header, userId, AuditMessage.BranchDeleteFail.getMsg(), AuditStatus.FAILURE, AuditMessage.BranchDeleteFailByService.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                if (countRoom > 0) {
                    logger.info("<<:Response:{}", header, response);
                    response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.room.map", null, locale));
                    auditService.saveAuditDetails(header, userId, AuditMessage.BranchDeleteFail.getMsg(), AuditStatus.FAILURE, AuditMessage.BranchDeleteFailByRoom.getMsg());
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    branchRepo.deleteById(branchId);
                    response = new CustomResponse(true, messageSource.getMessage("branch.ctrl.delete", null, locale));
                    logger.info("<<:Response:{}", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.BranchDelete.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:deleteLocation:branchId:{}:Error:{}", header, branchId, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("branch.ctrl.faildelete", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userId}/allBranches/{locationId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BranchResponse> getAllBranchesAndServsByLocationId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_BRANCHES_SERVICES") String tranId,
            @PathVariable("locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>header:[{}]:locationId:{}:[{}]", header, locationId);
            BranchResponse locations = branchServc.getAllBranchesAndServsByLocationId(header, userId, locationId);
            logger.trace("{}<<Response:{}", header, locations);
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(locations);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesAndServsByLocationId:userId:{}:locationId:{}:Error:{}", header, userId, locationId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BranchResponse());
        } finally {
            sb = null;
            header = null;
        }
    }
    

    @GetMapping(path = "{userId}/bracnhes/{locationId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<BranchGUDTO> getAllBranchesByLocIdAndBranchType(
            @RequestParam Long branchTypeId,
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_BRANCHES_BY_BRANCHTYPE") String tranId,
            @PathVariable("locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:locationId:[{}]:branchTypeId:{}", header, locationId);
            BranchGUDTO branch = branchServc.getAllBranchesByLocIdAndBranchType(header, userId, locationId, branchTypeId);
            logger.info("{}<<:Response:Size:[{}]", header, branch.getBranches().size());
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(branch);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesByLocIdAndBranchType:locationId:{}:branchTypeId:{}:Error:{}", header, locationId, branchTypeId, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            logger.info("{}<<:Response:Size:[{}]", header, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BranchGUDTO());
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userId}/bracnches-services/{locationId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BranchesServicesResponse> getAllBranchesWithServicesByLocationIdAndBranchType(
            @RequestParam String branchType,
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_BRANCHES_SERVICES_BY_BRANCHTYPE") String tranId,
            @PathVariable("locationId") Long locationId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>locationId:[{}]:branchType:[{}]", header, locationId, branchType);
            BranchesServicesResponse locations = branchServc.getAllBranchesAndServsByLocationIdAndBranchType(header, userId, locationId, branchType);
            logger.info("{}<<Response:{}", header, locations);
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(locations);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesAndServsByLocationIdAndBranchType:userId:{}:locationId:{}:branchTypeId:{}:Error:{}", header, userId, locationId, branchType, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            logger.info("{}<<Response:{}", header, "[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BranchesServicesResponse());
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userId}/allbranches-rooms",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BranchRoomDto> getAllBranchesWithRoomsByUserId(
            Locale locale,
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_BRANCHES_ROOMS") String tranId
    ) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>header:[{}]:userId:[{}]", header, userId);
            BranchRoomDto locations = branchServc.getAllBranchesWithRoomsByUserId(header, userId);
            logger.trace("{}<<Response:{}", header, locations);
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(locations);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesWithRoomsByUserId:userId:{}:Error:{}", header, userId, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.BranchGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BranchRoomDto());
        } finally {
            sb = null;
            header = null;
        }
    }
}

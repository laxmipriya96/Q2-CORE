/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2.model.TblRole;
import com.vm.qsmart2api.dtos.AuditDetails;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.Response;
import com.vm.qsmart2api.dtos.menus.ModulesResponse;
import com.vm.qsmart2api.dtos.role.CustomRoleDto;
import com.vm.qsmart2api.dtos.role.CustomRolesResponse;
import com.vm.qsmart2api.dtos.role.RoleCreateDto;
import com.vm.qsmart2api.dtos.role.RoleGDTO;
import com.vm.qsmart2api.dtos.role.RoleType;
import com.vm.qsmart2api.dtos.role.RoleTypeDto;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.repository.RoleRepository;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.RoleService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
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
 * @author Phani
 */
@RestController
@RequestMapping("role/")
public class RoleController {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(RoleController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    RoleService roleService;

    @Autowired
    AuditService auditService;

    @Autowired
    RoleRepository roleRepositary;

    @GetMapping(path = "{userid}/roles/{rolecode}/all",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<RoleGDTO> getRolesByRoleCode(
            Locale locale,
            @PathVariable("userid") long userId,
            @PathVariable("rolecode") String roleCode,
            @RequestParam("enterpriseid") int enterpriseId,
            @RequestHeader(value = "tranId", defaultValue = "GET_ROLES") String tranId
    ) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:[{}]:rolecode:[{}]:enterprieid:[{}]", header, userId, roleCode, enterpriseId);
            RoleGDTO roleGDTO = roleService.getRolesByRoleCode(header, userId, roleCode, enterpriseId);
            logger.info("{}<<Response:{}", header, roleGDTO);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(roleGDTO);
        } catch (Exception e) {
            logger.error("{}Excep:getRolesByRoleCode:roleCode:{}:Error:{}", header,roleCode, e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RoleGDTO(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userid}/custom-roles/{enterpriseId}/all",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomRolesResponse> getCustomRolesByEnterpriseId(
            Locale locale,
            @PathVariable("userid") long userId,
            @PathVariable("enterpriseId") int enterpriseId,
            @RequestHeader(value = "tranId", defaultValue = "GET_ROLES") String tranId
    ) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>userId:[{}]:enterprieid:{}", header, userId, enterpriseId);
            CustomRolesResponse CustomRolesResponse = roleService.getCustomRolesByEnterpriseId(header, userId, enterpriseId);
            logger.info("{}<<Response:{}", header, CustomRolesResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleCustomGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(CustomRolesResponse);
        } catch (Exception e) {
            logger.error("{}Excep:getCustomRolesByEnterpriseId:userId:[{}]:enterprieid:{}:Error:{}", header, userId, enterpriseId,e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleCustomGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomRolesResponse(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userid}/role-types", produces = {"application/json", "application/xml"})
    public ResponseEntity<RoleType> getRoleTypes(
            Locale locale,
            @PathVariable("userid") long userId,
            @RequestHeader(value = "tranId", defaultValue = "GET_ROLE_TYPES") String tranId
    ) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        AuditDetails auditDetails = new AuditDetails();
        List<RoleTypeDto> dto = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:getRoleTypes:{}", header);
            dto = new ArrayList<RoleTypeDto>() {
                {
                    add(new RoleTypeDto(0, "AD", "Admin"));
                    add(new RoleTypeDto(1, "CL", "Client"));
                }
            };
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleTypeGet.getMsg(), AuditStatus.SUCCESS);
            return new ResponseEntity(new RoleType(dto), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Excep:getRoleTypes:{}", e.getMessage());
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleTypeGetFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return new ResponseEntity(new RoleType(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            sb = null;
            header = null;
            dto = null;
        }

    }

    @GetMapping(path = "{userid}/pages/{role-type}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<ModulesResponse> getPageswithDefaultPermissions(
            Locale locale,
            @PathVariable("userid") long userId,
            @PathVariable("role-type") int roleType,
            @RequestParam("branch-type") int branchType,
            @RequestHeader(value = "tranId", defaultValue = "GET_SUB_MODULES") String tranId
    ) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        AuditDetails auditDetails = new AuditDetails();
        ModulesResponse dto = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>:getRoleTypes:{}", header);
            dto = roleService.readPagesWithPermissions(header, roleType, branchType);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleGetModules.getMsg(), AuditStatus.SUCCESS);
            logger.info("{}<<:Response:{}", header, dto);
            return new ResponseEntity(dto, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("{}Excep:getPageswithDefaultPermissions:roleType:{}:branchType:{}:Error:{}", header,roleType,branchType, ExceptionUtils.getRootCauseMessage(e));
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleGetModulesFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            logger.info("{}<<:Response:{}", header, dto);
            return new ResponseEntity(dto, HttpStatus.OK);
        } finally {
            sb = null;
            header = null;
            dto = null;
        }

    }

    @PostMapping(path = "{userid}/create/{enterpriseid}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> createRole(@RequestBody RoleCreateDto roleDto, Locale locale,
            @PathVariable("userid") Long userId,
            @PathVariable("enterpriseid") int enterpriseid,
            @RequestHeader(value = "tranid", defaultValue = "CREATE_ROLE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse sResponse = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>enterpriseId:{},Data:{}", header, roleDto);
            if (roleService.validationRoleName(header, roleDto, enterpriseid)) {
                Long id = roleService.save(header, userId, roleDto, enterpriseid);
                if (id > 0) {
                    sResponse = new CustomResponse(true, messageSource.getMessage("role.ctrl.create", null, locale), id);
                    logger.info("{}<<:Response:{}", header, sResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.RoleCreate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(sResponse);
                } else {
                    sResponse = new CustomResponse(false, messageSource.getMessage("role.ctrl.createfail", null, locale));
                    logger.info("{}<<:Response:{}", header, sResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.RoleCreateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(sResponse);
                }

            } else {
                sResponse = new CustomResponse(false, messageSource.getMessage("role.ctrl.username.exists", null, locale));
                logger.info("{}<<:createRole:Response:{}", header, sResponse);
                
                auditService.saveAuditDetails(header, userId, AuditMessage.RoleCreateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.RoleNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(sResponse);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createRole:userId:{}:roleDto:{}:Error:{}", header,userId,roleDto, e.getMessage());
            sResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, sResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleCreateFail.getMsg(), AuditStatus.FAILURE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sResponse);
        }
    }

    @PutMapping(path = "{userid}/update/{enterpriseid}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateRole(@RequestBody CustomRoleDto roleDto, Locale locale,
            @PathVariable("userid") Long userId,
            @PathVariable("enterpriseid") int enterpriseid,
            @RequestHeader(value = "tranid", defaultValue = "UPDATE_ROLE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        CustomResponse sResponse = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>enterpriseId:{},roleDto:{}", header, roleDto);
            if (roleService.validationRoleNameById(header, roleDto, enterpriseid)) {
                Long id = roleService.update(header, userId, roleDto, enterpriseid);
                if (id > 0) {
                    sResponse = new CustomResponse(true, messageSource.getMessage("role.ctrl.update", null, locale), id);
                    logger.info("{}<<:Response:{}", header, sResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.RoleUpdate.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.CREATED).body(sResponse);
                } else {
                    sResponse = new CustomResponse(false, messageSource.getMessage("role.ctrl.updatefail", null, locale));
                    logger.info("{}<<:Response:{}", header, sResponse);
                    auditService.saveAuditDetails(header, userId, AuditMessage.RoleUpdateFail.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(sResponse);
                }

            } else {
                sResponse = new CustomResponse(false, messageSource.getMessage("role.ctrl.username.exists", null, locale));
                logger.info("{}<<:createRole:Response:{}", header, sResponse);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoleUpdateFail.getMsg(), AuditStatus.FAILURE, AuditMessage.RoleNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(sResponse);
            }
        } catch (Exception e) {
            logger.error("{}Excep:createRole:usrId:{}:roleDto:{}:Error:{}", header,userId,roleDto, e.getMessage());
            sResponse = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, sResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleUpdateFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sResponse);
        }
    }

    @DeleteMapping(path = "{userId}/delete/{roleId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> deleteByRoleId(
            Locale locale,
            @PathVariable("roleId") Long roleId, @PathVariable("userId") Long userId,
            @RequestHeader(value = "tranId", defaultValue = "DELETE_ROLE") String tranId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        Response response = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>RoleId:[{}]", header, roleId);
            TblRole tblRole = roleService.getRoleByRoleId(roleId);

            if (tblRole.getUsers() != null && tblRole.getUsers().size() == 0) {
                roleRepositary.deleteById(roleId);
                response = new Response(true, messageSource.getMessage("role.ctrl.delete", null, locale));
                logger.info("<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoleDelete.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else if (tblRole.getUsers() == null) {
                roleRepositary.deleteById(roleId);
                response = new Response(true, messageSource.getMessage("role.ctrl.delete", null, locale));
                logger.info("<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoleDelete.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new Response(false, messageSource.getMessage("role.ctrl.delete.userexists", null, locale));
                logger.info("<<:Response:{}", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.RoleDeleteFail.getMsg(), AuditStatus.FAILURE, AuditMessage.RoleNameFail.getMsg());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:deleteByRoleId:userId:{}:Error:{}", header,userId, e.getMessage());
            response = new Response(false, messageSource.getMessage("role.ctrl.faildelete", null, locale));
            auditService.saveAuditDetails(header, userId, AuditMessage.RoleDeleteFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("<<:Response:{}", header, response);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
        }
    }
}

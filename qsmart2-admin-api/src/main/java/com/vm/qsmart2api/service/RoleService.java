/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblEnterprise;
import com.vm.qsmart2.model.TblLocation;
import com.vm.qsmart2.model.TblPermission;
import com.vm.qsmart2.model.TblRole;
import com.vm.qsmart2.model.TblSubModule;
import com.vm.qsmart2.model.TblUser;
import com.vm.qsmart2api.dtos.menus.ModulesDto;
import com.vm.qsmart2api.dtos.menus.ModulesResponse;
import com.vm.qsmart2api.dtos.menus.PermissionsDto;
import com.vm.qsmart2api.dtos.menus.SubModulesDto;
import com.vm.qsmart2api.dtos.role.CustomRoleDto;
import com.vm.qsmart2api.dtos.role.CustomRolesResponse;
import com.vm.qsmart2api.dtos.role.RoleCreateDto;
import com.vm.qsmart2api.dtos.role.RoleDTO;
import com.vm.qsmart2api.dtos.role.RoleGDTO;
import com.vm.qsmart2api.dtos.user.UserDTO;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.MenuRepositary;
import com.vm.qsmart2api.repository.RoleRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Phani
 */
@Service
//@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MenuRepositary menuRepository;

//    @Autowired
//    SubModuleRepositary submoduleRepositary;
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UserService.class);

    public RoleGDTO getRolesByRoleCode(String header, Long userId, String roleCode, int enterpriseId) {
        List<RoleDTO> roleList = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>Request:[{}]", header, userId);
            }
            switch (roleCode) {
                case "SA":
                    roleList = Mapper.INSTANCE.roleToROleDto(roleRepository.findRoleByRoleCode("EA"));
                    return new RoleGDTO(roleList);
                case "EA":
                    roleList = Mapper.INSTANCE.roleToROleDto(roleRepository.findCustomANdSystemRolesByRoleCode(new ArrayList<String>() {
                        {
                            add("SA");
                        }

                        {
                            add("EA");
                        }
                    }, ((short) 0), ((short) 1), enterpriseId));
                    return new RoleGDTO(roleList);
                default:
                    roleList = Mapper.INSTANCE.roleToROleDto(roleRepository.findCustomANdSystemRolesByRoleCode(new ArrayList<String>() {
                        {
                            add("SA");
                        }

                        {
                            add("EA");
                        }
                    }, ((short) 0), ((short) 1), enterpriseId));
                    return new RoleGDTO(roleList);
            }

        } catch (Exception e) {
            logger.error("{}Excep:getDefaultRolesFromService:enterpriseId:{}:roleCode:{}:Error:{}", header, enterpriseId, roleCode, e.getMessage());
            return new RoleGDTO(new ArrayList<>());
        } finally {
            roleList = null;
        }
    }

    public CustomRolesResponse getCustomRolesByEnterpriseId(String header, Long userId, int enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>Request:[{}]", header, userId);
            }
            if (enterpriseId == 0) {
                List<CustomRoleDto> roleList = Mapper.INSTANCE.roleListToCustomRoleDtoList(roleRepository.findRoleByRoleCode("EA"));
                return new CustomRolesResponse(roleList);
            } else {
                List<CustomRoleDto> roleList = Mapper.INSTANCE.roleListToCustomRoleDtoList(roleRepository.getCustomRolesByEnterpriseId(((short) 0), ((short) 1), enterpriseId,
                        new ArrayList<String>() {
                    {
                        add("SA");
                    }

                    {
                        add("EA");
                    }
                }
                ));
                return new CustomRolesResponse(roleList);
            }
        } catch (Exception e) {
            logger.error("{}Excep:getCustomRolesByEnterpriseId:enterpriseId:{}:Error:{}", header, enterpriseId, e.getMessage());
            return new CustomRolesResponse(new ArrayList<>());
        }
    }

    public TblRole getRoleCode(long roleId) {
        return roleRepository.getOne(roleId);
    }

    public ModulesResponse readPagesWithPermissions(String header, int roleType, int branchType) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>RoleType:[{}],brachType:[{}]", header, roleType, branchType);
            }
            return getModulesWithPermissions(header, roleType, branchType);
        } catch (Exception e) {
            logger.error("{}Excep:readPagesWithPermissions:roleType:{}:branchType:{}:Error:{}", header, roleType, branchType, e.getMessage());
            return new ModulesResponse(new ArrayList<>());
        }
    }

    public ModulesResponse getModulesWithPermissions(String header, int roleType, int branchType) {
        ModulesResponse mResponse = null;
        List<TblPermission> permissions = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>roleType:[{}]:BranchType:[{}]", header, roleType, branchType);
            }
            switch (roleType) {
                case 0:
                    permissions = menuRepository.getSubModulesByRoleTypeNdSubmoduleName("ADMIN", "Location".toUpperCase());
                    break;
                case 2:
                    permissions = menuRepository.getSubModulesByRoleType("SYSTEM");
                    break;
                default:
                    if (branchType > 0) {
                        permissions = menuRepository.getSubModulesByRoleTypeNdBranchType("CLIENT", branchType);
                        break;
                    } else {
                        permissions = menuRepository.getSubModulesByRoleType("CLIENT");
                        break;
                    }
            }
            mResponse = new ModulesResponse();
            List<ModulesDto> modulesList = new ArrayList<>();
            for (TblPermission permission : permissions) {
                ModulesDto moduleDto = Mapper.INSTANCE.modulesEntityToModulesDto(permission.getModule());
                Optional<ModulesDto> optionalModule = modulesList.stream().filter(module -> (module.getModuleId() == moduleDto.getModuleId())).findFirst();
                if (optionalModule.isPresent()) {
                    ModulesDto existModule = optionalModule.get();
                    SubModulesDto subModulesDto = Mapper.INSTANCE.subModulesEntityToSubModulesDto(permission.getSubModule());
                    PermissionsDto permissionDto = Mapper.INSTANCE.permissionEntityToPermissionDto(permission);
                    List<SubModulesDto> existSubModulesList = existModule.getSubModules();
                    modulesList.remove(existModule);
                    Optional<SubModulesDto> optionalSubModule = existSubModulesList.stream().filter(subModule -> subModule.getSubModuleId() == subModulesDto.getSubModuleId()).findFirst();
                    if (optionalSubModule.isPresent()) {
                        SubModulesDto existSubModule = optionalSubModule.get();
                        List<PermissionsDto> existPermissionList = existSubModule.getPrivileges();
                        existSubModulesList.remove(existSubModule);
                        Optional<PermissionsDto> optionalPermissionDto = existPermissionList.stream().filter(permissionD -> permissionD.getPermissionId() == permissionDto.getPermissionId()).findFirst();
                        if (optionalPermissionDto.isPresent()) {
                            existModule.addSubmodule(existSubModule);
                            existModule.setLink(existModule.getSubModules().get(0).getLink());
                            modulesList.add(existModule);
                        } else {
                            if (permissionDto.getPermission().equalsIgnoreCase("read")) {
                                existSubModule.setReadPermissionId(permissionDto.getPermissionId());
                            } else {
                                existSubModule.addprivileges(permissionDto);
                            }
                            existModule.addSubmodule(existSubModule);
                            existModule.setLink(existModule.getSubModules().get(0).getLink());
                            modulesList.add(existModule);
                        }
                    } else {
                        if (permissionDto.getPermission().equalsIgnoreCase("read")) {
                            subModulesDto.setReadPermissionId(permissionDto.getPermissionId());
                        } else {
                            subModulesDto.addprivileges(permissionDto);
                        }
                        existModule.addSubmodule(subModulesDto);
                        existModule.setLink(existModule.getSubModules().get(0).getLink());
                        modulesList.add(existModule);
                    }
                } else {
                    SubModulesDto subModulesDto = Mapper.INSTANCE.subModulesEntityToSubModulesDto(permission.getSubModule());
                    PermissionsDto permissionDto = Mapper.INSTANCE.permissionEntityToPermissionDto(permission);
                    if (permissionDto.getPermission().equalsIgnoreCase("read")) {
                        subModulesDto.setReadPermissionId(permissionDto.getPermissionId());
                    } else {
                        subModulesDto.addprivileges(permissionDto);
                    }
                    moduleDto.addSubmodule(subModulesDto);
                    moduleDto.setLink(moduleDto.getSubModules().get(0).getLink());
                    modulesList.add(moduleDto);
                }
            }
            mResponse.setModules(modulesList);
            return mResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return new ModulesResponse(new ArrayList<>());
        } finally {
            mResponse = null;
            permissions = null;
        }
    }

    public boolean validationRoleName(String header, RoleCreateDto roleDto, int enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:roleName:{}:enterpriseId:{}", header, roleDto.getRoleName(), enterpriseId);
            }
            int count = roleRepository.validateRoleNameInDb(roleDto.getRoleName().toUpperCase(), enterpriseId);
            return count <= 0;
        } catch (NoResultException ne) {
            logger.error("{}Excep:validationRoleName:roleDto:{}:enterpriseId:{}:Error:{}", header, roleDto, enterpriseId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validationRoleName:roleDto:{}:enterpriseId:{}:Error:{}", header, roleDto, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }

    }

    public boolean validationRoleNameById(String header, CustomRoleDto roleDto, int enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:roleName:{}:enterpriseId:{}", header, roleDto.getRoleName(), enterpriseId);
            }
            int count = roleRepository.validateRoleNameInDbByID(roleDto.getRoleName().toUpperCase(), enterpriseId, roleDto.getRoleId());
            return count <= 0;
        } catch (NoResultException ne) {
            logger.error("{}Excep:validationRoleName:roleDto:{}:enterpriseId:{}:Error:{}", header, roleDto, enterpriseId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validationRoleName:roleDto:{}:enterpriseId:{}:Error:{}", header, roleDto, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }

    }

    public long update(String header, Long userId, CustomRoleDto roleDto, int enterprise) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>rolecrtDto:[{}]:enterpriseid:[{}]", header, roleDto, enterprise);
            }
            TblRole existRole = roleRepository.findRoleByRoleId(roleDto.getRoleId());
            TblRole role = Mapper.INSTANCE.customRoleDtoToRoleEntity(roleDto);
            role.setUsers(existRole.getUsers());
            role.setUpdatedBy(userId);
            role.setUpdatedOn(new Date());
            return roleRepository.saveAndFlush(role).getRoleId();
        } catch (Exception e) {
            logger.error("{}Excep:update:roleDto:{}:enterprise:{}:Error:{}", header, e.getMessage());
            return 0;
        }
    }

    public long save(String header, Long userId, RoleCreateDto roleDto, int enterprise) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>rolecrtDto:[{}]:enterpriseid:[{}]", header, roleDto, enterprise);
            }
            TblRole role = Mapper.INSTANCE.roleCrtDtoToRoleEntity(roleDto);
//            role.setRoleCode("NA");
            role.setIsCustomRole((short) 1);
            role.setEnterpriseId(enterprise);
            role.setIsCustomRole((short) 1);
            role.setCreatedOn(new Date());
            role.setCreatedBy(userId);
            role.setUpdatedBy(userId);
            role.setUpdatedOn(new Date());
            return roleRepository.saveAndFlush(role).getRoleId();
        } catch (Exception e) {
            logger.error("{}Excep:save:roleDto:{}:enterprise:{}:Error:{}", header, roleDto, enterprise, e.getMessage());
            return 0;
        }
    }

    public TblRole getRoleByRoleId(long roleId) {
        return roleRepository.findRoleByRoleId(roleId);
    }
}

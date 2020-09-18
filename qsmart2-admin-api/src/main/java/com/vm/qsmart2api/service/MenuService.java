/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblPermission;
import com.vm.qsmart2api.dtos.menus.ModulesDto;
import com.vm.qsmart2api.dtos.menus.ModulesResponse;
import com.vm.qsmart2api.dtos.menus.PermissionsDto;
import com.vm.qsmart2api.dtos.menus.SubModulesDto;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.MenuRepositary;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Phani
 */
@Service
public class MenuService {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(MenuService.class);

    @Autowired
    MenuRepositary mRepositary;

    public ModulesResponse getModulesWithPermissions(String header, long roleId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:roleId:[{}]", header, roleId);
            }
            List<TblPermission> permissions = mRepositary.getAllPermissionsByRoleId(roleId);
            ModulesResponse mResponseDto = new ModulesResponse();
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
                            if (!permissionDto.getPermission().equalsIgnoreCase("read")) {
                                existSubModule.addprivileges(permissionDto);
                            }
                            existModule.addSubmodule(existSubModule);
                            existModule.setLink(existModule.getSubModules().get(0).getLink());
                            modulesList.add(existModule);
                        }
                    } else {
                        if (!permissionDto.getPermission().equalsIgnoreCase("read")) {
                            subModulesDto.addprivileges(permissionDto);
                        }
                        existModule.addSubmodule(subModulesDto);
                        existModule.setLink(existModule.getSubModules().get(0).getLink());
                        modulesList.add(existModule);
                    }
                } else {
                    SubModulesDto subModulesDto = Mapper.INSTANCE.subModulesEntityToSubModulesDto(permission.getSubModule());
                    PermissionsDto permissionDto = Mapper.INSTANCE.permissionEntityToPermissionDto(permission);
                    if (!permissionDto.getPermission().equalsIgnoreCase("read")) {
                        subModulesDto.addprivileges(permissionDto);
                    }
                    moduleDto.addSubmodule(subModulesDto);
                    moduleDto.setLink(moduleDto.getSubModules().get(0).getLink());
                    modulesList.add(moduleDto);
                }
            }
            mResponseDto.setModules(modulesList);
            return mResponseDto;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getModulesWithPermissions:roleId:{}:Error:{}", header, roleId, ExceptionUtils.getRootCauseMessage(e));
            return new ModulesResponse(new ArrayList<>());
        }
    }
}

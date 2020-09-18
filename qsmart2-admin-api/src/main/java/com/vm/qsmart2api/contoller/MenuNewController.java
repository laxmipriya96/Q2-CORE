/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.AuditDetails;
import com.vm.qsmart2api.dtos.menus.ModulesResponse;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.MenuService;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Phani
 */
@RestController
@RequestMapping("/menu-new")
public class MenuNewController {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(MenuNewController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    MenuService menuService;
    
    @Autowired
    AuditService auditService;

    @GetMapping(path = "/{userid}/menus/{roleid}", produces = {"application/json", "application/xml"})
    public ResponseEntity<ModulesResponse> getMenus(Locale locale,
            @PathVariable("userid") Long userId,
            @RequestHeader(value = "tranid", defaultValue = "GET_MENUS") String tranId,
            @PathVariable("roleid") Long roleId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        ModulesResponse sResponse = null;
        AuditDetails auditDetails = new AuditDetails();
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> roleid:{}", header, roleId);
            //TODO logic
            sResponse = menuService.getModulesWithPermissions(header, roleId);
            logger.info("{} <<:Response:{}", header, sResponse);
            auditService.saveAuditDetails(header, userId, AuditMessage.MenuGet.getMsg(), AuditStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(sResponse);
        } catch (Exception e) {
            logger.error("{}Excep:getMenus:Error:{}", header, e.getMessage());
            sResponse = new ModulesResponse(new ArrayList<>());
            auditService.saveAuditDetails(header, userId, AuditMessage.MenuGetFail.getMsg(), AuditStatus.FAILURE, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{} <<:Response:{}", header, sResponse);
            return ResponseEntity.status(HttpStatus.OK).body(sResponse);
        }
    }

}

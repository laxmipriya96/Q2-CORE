/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.displayboard.DisplayBoardTokensDto;
import com.vm.qsmart2api.service.AuditService;
import com.vm.qsmart2api.service.DisplayBoardService;
import java.util.Locale;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Phani
 */
@RestController
@RequestMapping("displayboard/")
public class DisplayBoardController {

    private static final Logger logger = LogManager.getLogger(DisplayBoardController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;
    
    @Autowired
    DisplayBoardService displayBoardService;

    @GetMapping(path = "/tokens-info", produces = {"application/json", "application/xml"})
    public ResponseEntity<DisplayBoardTokensDto> getDisplayboardInfoWithTokens(Locale locale,
            @RequestHeader(value = "tranId", defaultValue = "DISPLAY_BOARD") String tranId,
            @RequestParam(value = "displayBoardId") String displayId) {
        String header = null;
        StringBuilder sb = new StringBuilder();
        DisplayBoardTokensDto response;
        try {
            sb.append("[").append(tranId).append("_").append(displayId).append("] ");
            header = sb.toString().toUpperCase();
            if (isLogEnabled) {
                logger.info("{} >> displayId:{}", header, displayId);
            }   
            response = displayBoardService.getDisplayboardInfoAlongWithToken(header, displayId);
            if(isLogEnabled){
                logger.info("{} << Response:{}", header, response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("{}Excep:getDisplayboardInfoWithTokens:displayId:[{}]:Error:{}", header, displayId, ExceptionUtils.getRootCauseMessage(e));
            response = new DisplayBoardTokensDto();
            if(isLogEnabled){
                logger.info("{} << Response:{}", header, response);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            sb = null;
            header = null;
            response = null;
        }
    }

}

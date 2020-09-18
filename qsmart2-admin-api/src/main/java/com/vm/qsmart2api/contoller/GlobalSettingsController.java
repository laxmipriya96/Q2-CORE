/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.global.GlobalSettingResponseDto;
import com.vm.qsmart2api.dtos.global.ParamsDto;
import com.vm.qsmart2api.dtos.global.QyeryParamsResponse;
import com.vm.qsmart2api.service.GlobalService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Phani
 */
@RestController
@RequestMapping("global-settings")
public class GlobalSettingsController {

    public static final Logger logger = LogManager.getLogger(GlobalSettingsController.class);

    @Autowired
    GlobalService globalService;

    @GetMapping(path = "{userId}/{settings-type}/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<GlobalSettingResponseDto> getGlobalSettingsBySettingType(
            Locale locale,
            @PathVariable("userId") int userId,
            @RequestHeader(value = "tranId", defaultValue = "GLOBAL_SETTINGS") String tranId,
            @PathVariable("settings-type") String settingsType,
            @PathVariable("enterpriseId") long enterpriseId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>enterpriseId:[{}],SettingsType:[{}]", header, enterpriseId, settingsType);
            GlobalSettingResponseDto responseDto = globalService.getGlobalData(header, enterpriseId, userId, settingsType);
            logger.info("{}<<Response:{}", header, responseDto);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesByEntId:Error:{}", header, e.getMessage());
            GlobalSettingResponseDto responseDto = new GlobalSettingResponseDto(null, false, 0, "Something went wrong");
            logger.info("{}<<Response:{}", header, responseDto);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        } finally {
            sb = null;
            header = null;
        }
    }

    @PostMapping(path = "{userId}/{settings-type}/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> updateGlobalSetingsBySettingType(
            Locale locale,
            @PathVariable("userId") long userId,
            @RequestHeader(value = "tranId", defaultValue = "GLOBAL_SETTINGS") String tranId,
            @PathVariable("settings-type") String settingsType,
            @PathVariable("enterpriseId") long enterpriseId,
            @RequestBody Object settings) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        CustomResponse cResponse;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>enterpriseId:[{}],SettingsType:[{}]:RequestDto:{}", header, enterpriseId, settingsType, settings);
            long globalId = globalService.updateGlobalSettings(header, enterpriseId, userId, settingsType, settings);
            if (globalId > 0) {
                cResponse = new CustomResponse(true, "Data Updated Successfully", globalId);
            } else {
                cResponse = new CustomResponse(false, "Unable to update Data", globalId);
            }
            logger.info("{}<<Response:{}", header, cResponse);
            return ResponseEntity.status(HttpStatus.OK).body(cResponse);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesByEntId:Error:{}", header, e.getMessage());
            cResponse = new CustomResponse(false, "Something went wrong");
            logger.info("{}<<Response:{}", header, cResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cResponse);
        } finally {
            sb = null;
            header = null;
        }
    }

    @GetMapping(path = "{userId}/queryparams/{enterpriseId}",
            produces = {"application/json", "application/xml"})
    public ResponseEntity<QyeryParamsResponse> getQueryParams(
            Locale locale,
            @PathVariable("userId") int userId,
            @RequestHeader(value = "tranId", defaultValue = "GLOBAL_SETTINGS") String tranId,
            @PathVariable("enterpriseId") long enterpriseId) {
        StringBuilder sb = new StringBuilder();
        String header = null;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{}>>enterpriseId:[{}]", header, enterpriseId);
            List<ParamsDto> queryParams = new ArrayList() {
                {
                    add(new ParamsDto(1, "Mobile Number"));
                }
                {
                    add(new ParamsDto(3, "Message"));
                }
                {
                    add(new ParamsDto(4, "Data Coding"));
                }
            };
            logger.info("{}<<Response:{}", header, queryParams);
            return ResponseEntity.status(HttpStatus.OK).body(new QyeryParamsResponse(queryParams));
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesByEntId:Error:{}", header, e.getMessage());
            GlobalSettingResponseDto responseDto = new GlobalSettingResponseDto(null, false, 0, "Something went wrong");
            logger.info("{}<<Response:{}", header, responseDto);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new QyeryParamsResponse(new ArrayList<>()));
        } finally {
            sb = null;
            header = null;
        }
    }
}

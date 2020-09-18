/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2.model.TblGlobalSetting;
import com.vm.qsmart2api.dtos.global.ActiveDirectorySettings;
import com.vm.qsmart2api.dtos.global.GlobalSettingResponseDto;
import com.vm.qsmart2api.dtos.global.LoginSettings;
import com.vm.qsmart2api.dtos.global.MailServerSettings;
import com.vm.qsmart2api.dtos.global.PwdPolicySettings;
import com.vm.qsmart2api.dtos.global.SmsSettings;
import com.vm.qsmart2api.repository.GlobalSettingsRepositary;
import com.vm.qsmart2.utils.DateUtils;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Phani
 */
@Service
public class GlobalService {

    public static final Logger logger = LogManager.getLogger(GlobalService.class);

    @Autowired
    GlobalSettingsRepositary gRepositary;

    @Autowired
    ObjectMapper objMappaer;

    @Autowired
    DateUtils dateUtils;

    public GlobalSettingResponseDto getGlobalData(String header, long enterpriseId, long userId, String settings_type) {
        Object data = null;
        GlobalSettingResponseDto responseDto = null;
        try {
            logger.info("{}>>enterpriseId:[{}],SettingsType:[{}]", header, enterpriseId, settings_type);
            TblGlobalSetting globalSettings = gRepositary.getGlobalSettingsByType(settings_type, enterpriseId);
            if (globalSettings != null) {
                String jsonString = globalSettings.getSettingJson();
                System.out.println("JSON STRING: " + jsonString);
                switch (settings_type.toUpperCase()) {
                    case "SMTP":
                        data = objMappaer.readValue(jsonString, MailServerSettings.class);
                        break;
                    case "LDAP":
                        data = objMappaer.readValue(jsonString, ActiveDirectorySettings.class);
                        break;
                    case "PASSWORD POLICIES":
                        data = objMappaer.readValue(jsonString, PwdPolicySettings.class);
                        break;
                    case "LOGIN":
                        data = objMappaer.readValue(jsonString, LoginSettings.class);
                        break;
                    case "SMS":
                        data = objMappaer.readValue(jsonString, SmsSettings.class);
                    default:
                        break;
                }
                if (data != null) {
                    responseDto = new GlobalSettingResponseDto(data, true, 0, "Getting Data Successully");
                    return responseDto;
                } else {
                    responseDto = new GlobalSettingResponseDto(data, false, 0, "Not yet Implemented");
                    return responseDto;
                }
            } else {
                responseDto = new GlobalSettingResponseDto(null, false, 0, "Not Configured");
                return responseDto;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{} || Exception:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
            responseDto = new GlobalSettingResponseDto(null, false, 0, "Not Configured");
            return responseDto;
        } finally {
            data = null;
            responseDto = null;
        }
    }

    public long updateGlobalSettings(String header, long enterpriseId, long userId, String settings_type, Object settingsDto) {
        try {
            logger.info("{}>>enterpriseId:[{}],SettingsType:[{}],data:[{}]", header, enterpriseId, settings_type, settingsDto);
            switch (settings_type.toUpperCase(Locale.getDefault())) {
                case "LDAP":
                    break;
                case "SMTP":
                    break;
                case "PASSWORD POLICIES":
                    break;
                case "LOGIN":
                    break;
                case "SMS":
                    break;
                default:
                    break;
            }
            TblGlobalSetting globalSettings = gRepositary.getGlobalSettingsByType(settings_type, enterpriseId);
            if (globalSettings != null) {
                globalSettings.setSettingJson(objMappaer.writeValueAsString(settingsDto));
                globalSettings.setUpdatedBy(userId);
                globalSettings.setUpdatedOn(dateUtils.getdate());
                globalSettings = gRepositary.saveAndFlush(globalSettings);
            } else {
                globalSettings = new TblGlobalSetting();
                globalSettings.setEnterpriseId(enterpriseId);
                globalSettings.setSettingType(settings_type.toUpperCase());
                globalSettings.setSettingJson(objMappaer.writeValueAsString(settingsDto));
                globalSettings.setCreatedBy(userId);
                globalSettings.setCreatedOn(dateUtils.getdate());
                globalSettings.setUpdatedBy(userId);
                globalSettings.setUpdatedOn(dateUtils.getdate());
                globalSettings = gRepositary.saveAndFlush(globalSettings);
            }

            return globalSettings.getId();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:updateGlobalSettings:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally {
        }
    }
}

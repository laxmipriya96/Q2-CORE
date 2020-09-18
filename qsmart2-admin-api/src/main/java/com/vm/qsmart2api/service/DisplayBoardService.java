/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.utils.ApptStatus;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.displayboard.DisplayBoardTokensDto;
import com.vm.qsmart2api.dtos.displayboard.TokensWithRooms;
import com.vm.qsmart2api.repository.DisplayRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Phani
 */
@Service
public class DisplayBoardService {

    private static final Logger logger = LogManager.getLogger(DisplayBoardService.class);

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    @Autowired
    DisplayRepository displayRepositary;

    @Autowired
    DateUtils dateUtils;

    public DisplayBoardTokensDto getDisplayboardInfoAlongWithToken(String header, String displayId) {
        DisplayBoardTokensDto dto = new DisplayBoardTokensDto();
        List<Object[]> objList = null;
        List<TokensWithRooms> tokens = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>displayId:{}", header, displayId);
            }
            objList = displayRepositary.getDisplayInfoWithTokensAndRoomInfo(displayId, ApptStatus.SERVING.getValue(), dateUtils.dayStartDateTime(), dateUtils.dayEndDateTime());
            if (objList != null && !objList.isEmpty()) {
                dto.setDisplayId((long) objList.get(0)[0]);
                dto.setDisplayBoardId((String) objList.get(0)[1]);
                dto.setDisplayName((String) objList.get(0)[2]);
                dto.setThemeId((int) objList.get(0)[3]);
                dto.setThemeName((String) objList.get(0)[4]);
                dto.setThemeType((String) objList.get(0)[5]);
                dto.setIsDefault((short) objList.get(0)[6]);
                tokens = new ArrayList<>();
                for (Object[] object : objList) {
                    if (object[7] != null) {
                        TokensWithRooms tokenObj = new TokensWithRooms();
                        if (object[7] != null) {
                            tokenObj.setTokenId((long) object[7]);
                        }
                        if (object[8] != null) {
                            tokenObj.setTokenNo((String) object[8]);
                        }
                        if (object[9] != null) {
                            tokenObj.setRoomId((long) object[9]);
                        }
                        if (object[10] != null) {
                            tokenObj.setRoomNo((String) object[10]);
                        }
                        if (object[11] != null) {
                            tokenObj.setRoomMasterEngName((String) object[11]);
                        }
                        if (object[12] != null) {
                            tokenObj.setRoomMasterArbName((String) object[12]);
                        }
                        if (object[13] != null) {
                            Date updatedTime = (Date) object[13];
                            tokenObj.setTimeStamp(updatedTime.getTime());
                        }
                        tokens.add(tokenObj);
                    }
                }
                dto.setTokens(tokens);
                return dto;
            } else {
                return dto;
            }
        } catch (Exception e) {
            logger.error("{}Excep:getDisplayboardInfoWithTokens:displayId:[{}]:Error:{}", header, displayId, ExceptionUtils.getRootCauseMessage(e));
            return dto;
        } finally {
            dto = null;
            objList = null;
            tokens = null;
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblDisplay;
import com.vm.qsmart2.model.TblTheme;
import com.vm.qsmart2api.dtos.display.DisplayCrtDto;
import com.vm.qsmart2api.dtos.display.DisplayResponse;
import com.vm.qsmart2api.dtos.display.DisplayUpDto;
import com.vm.qsmart2api.dtos.display.ThemeDto;
import com.vm.qsmart2api.dtos.display.ThemeGetDto;
import com.vm.qsmart2api.repository.DisplayRepository;
import com.vm.qsmart2api.repository.ThemeRepository;
import com.vm.qsmart2.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vm.qsmart2api.mapper.DevicesMapper;

/**
 *
 * @author Tejasri
 */
@Service
public class DisplayService {

    @Autowired
    DateUtils dateUtils;

    @Autowired
    DisplayRepository displayRepository;

    private static final Logger logger = LogManager.getLogger(DisplayService.class);

    public long saveDisplayboardInDb(String header, Long userId, DisplayCrtDto display, Long locationId, Long enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>save:locationId:{}:display:[{}]", header, locationId, display);
            }
            TblDisplay displayBrd = DevicesMapper.INSTANCE.displayCrtDtoToDisplayEntity(display);
            displayBrd.setLocationId(locationId);
            displayBrd.setDisplayIdentifier(RandomStringUtils.randomAlphanumeric(8));
            displayBrd.setEnterpriseId(enterpriseId);
            displayBrd.setCreatedBy(userId);
            displayBrd.setStatus(1);
            displayBrd.setCreatedOn(dateUtils.getdate());
            displayBrd.setUpdatedOn(dateUtils.getdate());
            displayBrd.setUpdatedBy(userId);
            displayBrd = displayRepository.saveAndFlush(displayBrd);
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:Room:[{}]", header, displayBrd.getDisplayId());
            }
            return displayBrd.getDisplayId();
        } catch (Exception e) {
            logger.error("{}Excep:saveDisplayboardInDb:locationId:{}:display:{}:Error:{}", header, locationId, display, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public long updateDisplayBoardInDb(String header, Long userId, long locationId, long enterpriseId, DisplayUpDto displayUd) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>userId:{},locationId:{},enterpriseId:{},DisplayUpDto:[{}]", header, userId, locationId, enterpriseId, displayUd);
            }
            TblDisplay display = DevicesMapper.INSTANCE.displayUDtoToDisplayEntity(displayUd);
            display.setUpdatedBy(userId);
            display.setUpdatedOn(dateUtils.getdate());
            display.setLocationId(locationId);
            display.setEnterpriseId(enterpriseId);
            logger.info("{}<<header:[{}]:display:[{}]", header, display);
            displayRepository.saveAndFlush(display);
            return display.getDisplayId();
        } catch (Exception e) {
            logger.error("{}Excep:updateDisplayBoardInDb:userId:{},locationId:{},enterpriseId:{},DisplayUpDto:[{}],Error:{}", header, userId, locationId, enterpriseId, displayUd, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally {
            displayUd = null;
        }
    }

    public boolean validateDisplayName(String header, Long locationId, String displayName) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>header:{},locationId:{}:displayName:{}", header, locationId, displayName);
            }
            int count = displayRepository.validateDisplayName(displayName.toUpperCase(), locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateDisplayName:locationId:{}:displayName:{},Error:{}", header, locationId, displayName, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateDisplayName:locationId:{}:displayName:{},Error:{}", header, locationId, displayName, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateDisplayNameByDisplayId(String header, Long locationId, Long displayId, String displayName) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>header:{},locationId:{}:displayId:{}:displayName:{}", header, locationId, displayId, displayName);
            }
            int count = displayRepository.validateDisplayNameByDisplayId(displayName.toUpperCase(), displayId, locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateDisplayNameByDisplayId:locationId:{}:displayId:{}:displayName:{}:Error:{}", header, locationId, displayId, displayName, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateDisplayNameByDisplayId:locationId:{}:displayId:{}:displayName:{}:Error:{}", header, locationId, displayId, displayName, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public long updateStatus(String header, long displayId, int status, long userId) {
        try {
            logger.info("{}>>displayId:{}:status:{}", header, displayId, status);
            Optional<TblDisplay> opt = displayRepository.findById(displayId);
            if (opt.isPresent()) {
                TblDisplay diaplay = opt.get();
                diaplay.setUpdatedBy(userId);
                diaplay.setUpdatedOn(dateUtils.getdate());
                diaplay.setStatus(status);
                displayRepository.save(diaplay);
                logger.info("{}<<:userId:[{}]", header, diaplay.getDisplayId());
                return diaplay.getDisplayId();
            }
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        } catch (Exception e) {
            logger.error("{}Excep:updateDisplayStatus:displayId:{}:Error:{}", header, displayId, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        }
    }

    public DisplayResponse getAllDisplaysByLocationId(String header, Long userId, Long locationId) {
        if (logger.isDebugEnabled()) {
            logger.info("{}>>:userId:{},locationId:[{}]", header, userId, locationId);;
        }
        try {
            List<TblDisplay> displayseList = displayRepository.findByLocationId(locationId);
            List<DisplayUpDto> displayList = displayseList.stream().map(obj -> getDisplayDto(obj)).collect(Collectors.toList());
            //List<DisplayUpDto> displayList = DevicesMapper.INSTANCE.displayEntityListToDisplayUpDtoList(displayRepository.findByLocationId(locationId));
            return new DisplayResponse(displayList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllDisplaysByLocationId:userId:{},locationId:{},Error:{}", header, userId, locationId, ExceptionUtils.getRootCauseMessage(e));
            return new DisplayResponse(new ArrayList<>());
        }
    }

    public DisplayUpDto getDisplayDto(TblDisplay display) {
        Set<Long> branches = display.getServices().stream().map(obj -> obj.getBranchId()).collect(Collectors.toSet());
        DisplayUpDto dto = DevicesMapper.INSTANCE.displayEntityToDisplayUDto(display);
        dto.setBranches(branches);
        return dto;
    }

    @Autowired
    ThemeRepository themeRepository;
    
    public ThemeGetDto getAllThemes(String header, Long userId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>:getAllThemes:header:[{}]:locationId:[{}]", header);
            }
            List<TblTheme> theme = themeRepository.findAll();
            List<ThemeDto> themeDto = DevicesMapper.INSTANCE.themeEntityToThemeDto(theme);
            logger.info("{}<<:themes:[{}]", themeDto);
            return new ThemeGetDto(themeDto);
        } catch (Exception e) {
            logger.error("{}Excep:getAllThemes:header:{}:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            return new ThemeGetDto();
        }
    }
}

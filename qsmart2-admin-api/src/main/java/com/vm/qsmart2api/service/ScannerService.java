/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblScanner;
import com.vm.qsmart2api.dtos.scanner.ScannerCrtDto;
import com.vm.qsmart2api.dtos.scanner.ScannerGetDto;
import com.vm.qsmart2api.dtos.scanner.ScannerUpDto;
import com.vm.qsmart2api.repository.ScannerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import com.vm.qsmart2.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vm.qsmart2api.mapper.DevicesMapper;
import java.util.HashSet;

/**
 *
 * @author Tejasri
 */
@Service
public class ScannerService {

    private static final Logger logger = LogManager.getLogger(ScannerService.class);

    @Autowired
    ScannerRepository scannerRepository;

    @Autowired
    DateUtils dateUtils;

    public boolean validateScannerName(String header, Long locationId, String scannerName) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>header:{},locationId:{}:scannerName:{}", header, locationId, scannerName);
            }
            int count = scannerRepository.validateScannerName(scannerName.toUpperCase(), locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateScannerName:locationId:{}:scannerName:{},Error:{}", header, locationId, scannerName, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateScannerName:locationId:{}:scannerName:{},Error:{}", header, locationId, scannerName, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateScannerNameByScannerId(String header, Long locationId, Long scannerId, String scannerName) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>header:{},locationId:{}:scannerId:{}:scannerName:{}", header, locationId, scannerId, scannerName);
            }
            int count = scannerRepository.validateScannerNameByScannerId(scannerName.toUpperCase(), scannerId, locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateScannerNameByScannerId:locationId:{}:scannerId:{}:scannerName:{}:Error:{}", header, locationId, scannerId, scannerName, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateScannerNameByScannerId:locationId:{}:scannerId:{}:scannerName:{}:Error:{}", header, locationId, scannerId, scannerName, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public long saveScannerInDb(String header, Long userId, ScannerCrtDto scanner, Long locationId, Long enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>save:locationId:{}:scanner:[{}]", header, locationId, scanner);
            }
            TblScanner scannerObj = DevicesMapper.INSTANCE.scannerCrtDtoToScannerEntity(scanner);
            scannerObj.setLocationId(locationId);
            scannerObj.setScannerIdentifier(RandomStringUtils.randomAlphanumeric(8));
            scannerObj.setEnterpriseId(enterpriseId);
            scannerObj.setCreatedBy(userId);
            scannerObj.setStatus(1);
            scannerObj.setCreatedOn(dateUtils.getdate());
            scannerObj.setUpdatedOn(dateUtils.getdate());
            scannerObj.setUpdatedBy(userId);
            scannerObj.setBranches(new HashSet<TblBranch>() {
                {
                    add(new TblBranch(scanner.getBranch()));
                }
            });

            scannerObj = scannerRepository.saveAndFlush(scannerObj);
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:Room:[{}]", header, scannerObj.getScannerId());
            }
            return scannerObj.getScannerId();
        } catch (Exception e) {
            logger.error("{}Excep:saveScannerInDb:locationId:{}:display:{}:Error:{}", header, locationId, scanner, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public long updateScannerInDb(String header, Long userId, long locationId, long enterpriseId, final ScannerUpDto scannerUpD) {
        TblScanner scanner = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>userId:{},locationId:{},enterpriseId:{},ScannerUpDto:[{}]", header, userId, locationId, enterpriseId, scannerUpD);
            }
            scanner = DevicesMapper.INSTANCE.scannerUDtoToScannerEntity(scannerUpD);
            scanner.setUpdatedBy(userId);
            scanner.setUpdatedOn(dateUtils.getdate());
            scanner.setLocationId(locationId);
            scanner.setEnterpriseId(enterpriseId);
            scanner.setBranches(new HashSet<TblBranch>() {
                {
                    add(new TblBranch(scannerUpD.getBranch()));
                }
            });
            logger.info("{}<<header:[{}]:scannerUpD:[{}]", header, scannerUpD);
            scannerRepository.saveAndFlush(scanner);
            return scanner.getScannerId();
        } catch (Exception e) {
            logger.error("{}Excep:updateScannerInDb:userId:{},locationId:{},enterpriseId:{},scannerUpD:[{}],Error:{}", header, userId, locationId, enterpriseId, scannerUpD, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally{
            scanner = null;
        }
    }

    public ScannerGetDto getAllScannersByLocationId(String header, Long userId, Long locationId) {
        if (logger.isDebugEnabled()) {
            logger.info("{}>>:userId:{},locationId:[{}]", header, userId, locationId);;
        }
        try {
            List<ScannerUpDto> scannerList = DevicesMapper.INSTANCE.scannerEntityListToScannerUpDtoList(scannerRepository.findByLocationId(locationId));
            return new ScannerGetDto(scannerList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllScannersByLocationId:userId:{},locationId:{},Error:{}", header, userId, locationId, ExceptionUtils.getRootCauseMessage(e));
            return new ScannerGetDto(new ArrayList<>());
        }
    }

    public long updateStatus(String header, long scannerId, int status, long userId) {
        try {
            logger.info("{}>>scannerId:{}:status:{}", header, scannerId, status);
            Optional<TblScanner> opt = scannerRepository.findById(scannerId);
            if (opt.isPresent()) {
                TblScanner scanner = opt.get();
                scanner.setUpdatedBy(userId);
                scanner.setUpdatedOn(dateUtils.getdate());
                scanner.setStatus(status);
                scannerRepository.save(scanner);
                logger.info("{}<<:userId:[{}]", header, scanner.getScannerId());
                return scanner.getScannerId();
            }
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        } catch (Exception e) {
            logger.error("{}Excep:updateScannerStatus:scannerId:{}:Error:{}", header, scannerId, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        }
    }

}

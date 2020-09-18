/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblMedService;
import com.vm.qsmart2.model.TblUser;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.medservice.MedServiceDTO;
import com.vm.qsmart2api.dtos.medservice.MedServiceGDTO;
import com.vm.qsmart2api.dtos.medservice.MedServiceUDTO;
import com.vm.qsmart2api.dtos.medservice.MedServiceWithUsers;
import com.vm.qsmart2api.dtos.medservice.UserMedService;
import com.vm.qsmart2api.dtos.user.UserCtrDTO;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.MedServiceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ashok
 */
@Service
public class MedServiceService {

    private static final Logger logger = LogManager.getLogger(MedServiceService.class);

    @Autowired
    DateUtils dateUtils;

    @Autowired
    MedServiceRepository medServiceRepository;

    public long save(String header, Long userId, Long enterpriseId, Long locationId, MedServiceDTO medServiceDTO) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>save:header:[{}]:enterpriseId:[{}]:locationId:[{}]:medServiceDTO:[{}]", header, enterpriseId, locationId, medServiceDTO);
            }
            TblMedService medService = (TblMedService) Mapper.INSTANCE.medservicedtoTOMedservice(medServiceDTO);
            medService.setLocationId(locationId);
            medService.setEnterpriseId(enterpriseId);
            medService.setCreatedBy(userId);
            medService.setCreatedOn(dateUtils.getdate());
            medService.setUpdatedOn(dateUtils.getdate());
            medService.setUpdatedBy(userId);
            medService = medServiceRepository.saveAndFlush(medService);
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:medService:[{}]", header, medService);
            }
            return medService.getMedServiceId();
        } catch (Exception e) {
            logger.error("{}Excep:save:enterpriseId:{}:locationId:{}:medService:{}:Error:{}", header, enterpriseId, locationId, medServiceDTO, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public int update(String header, Long userId, Long enterpriseId, Long locationId, MedServiceUDTO medServiceUDTO) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>update:header:[{}]:enterpriseId:[{}]:locationId:[{}]:medServiceUDTO:[{}]", header, enterpriseId, locationId, medServiceUDTO);
            }
//            TblMedService medService = (TblMedService) Mapper.INSTANCE.medserviceudtoTOMedservice(medServiceUDTO);
//            medService.setLocationId(locationId);
//            medService.setEnterpriseId(enterpriseId);
//            medService.setUpdatedOn(dateUtils.getdate());
//            medService.setUpdatedBy(userId);
           // medServiceRepository.saveAndFlush(medService);
            int id = medServiceRepository.updateMedicalService(medServiceUDTO.getMedServiceName(), medServiceUDTO.getMedServiceCode(), dateUtils.getdate(), userId, medServiceUDTO.getMedServiceId());
           // logger.info("<<:header:[{}]:medService:[{}]", header, medService);
            return id;
        } catch (Exception e) {
            logger.error("{}Excep:update:header:{}:enterpriseId:{}:locationId:{}:medService:{}:Error:{}", header, enterpriseId, locationId, medServiceUDTO, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public boolean validateMedServiceName(String header, Long locationId, String medServiceName) {
        try {
            logger.info("{}>>: locationId:{}:medServiceName:{}", header, locationId, medServiceName);
            int count = medServiceRepository.validateMedServiceName(locationId, medServiceName);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("{}Excep:validateMedServiceName:locationId:{}:medServiceName:{}:Error:{}", header, locationId, medServiceName, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateMedServiceCode(String header, Long locationId, String medServiceCode) {
        try {
            logger.info("{}>>: locationId:{}:medServiceCode:{}", header, locationId, medServiceCode);
            int count = medServiceRepository.validateMedServiceCode(locationId, medServiceCode);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("{}Excep:validateMedServiceCode:locationId:{}:medServiceCode:{}:Error:{}", header, locationId, medServiceCode, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateMedServiceNameByMedServiceId(String header, Long medServiceId, Long locationId, String medServiceName) {
        try {
            logger.info("{}>>:validateMedServiceNameByMedServiceId:medServiceId:{}: locationId:{}:medServiceName:{}", header, medServiceId, locationId, medServiceName);
            int count = medServiceRepository.validateMedServiceNameByMedServiceId(medServiceId, locationId, medServiceName);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("{}Excep:validateMedServiceNameByMedServiceId:medServiceId:{}: locationId:{}:medServiceName:{}:Error:{}", header, medServiceId, locationId, medServiceName, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateMedServiceCodeByMedServiceId(String header, Long medServiceId, Long locationId, String medServiceCode) {
        try {
            logger.info("{}>>:validateMedServiceCode:medServiceId:{}: locationId:{}:medServiceCode:{}", header, medServiceId, locationId, medServiceCode);
            int count = medServiceRepository.validateMedServiceCodeByMedServiceId(medServiceId, locationId, medServiceCode);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("{}Excep:validateMedServiceCodeByMedServiceId:medServiceId:{}: locationId:{}:medServiceCode:{}:Error:{}", header, medServiceId, locationId, medServiceCode, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

//    public MedServiceWithUsers getAllMedServiceWithUsersByLocationId(String header, Long locationId) {
//        try {
//            if (logger.isDebugEnabled()) {
//                logger.info(">>getAllMedServiceWithUsersByLocationId:header:[{}]:locationId:[{}]", header, locationId);
//            }
//            MedServiceWithUsers medServiceWithUsers = new MedServiceWithUsers();
//            List<MedServiceGDTO> medServiceList = new ArrayList();
//            List<TblMedService> medServices = medServiceRepository.getAllMedServicesWithUsers(locationId);
//            for (TblMedService medService : medServices) {
//                MedServiceUDTO medServiceUDTO = Mapper.INSTANCE.medserviceentityTOMedserviceDTO(medService);
//                List<UserCtrDTO> userCtrDTOList = new ArrayList();
//                for (TblUser user : medService.getUsers()) {
//                    UserCtrDTO userCtrDTO = Mapper.INSTANCE.userEntityToUserCtrDto(user);
//                    userCtrDTOList.add(userCtrDTO);
//                }
//                MedServiceGDTO medServiceGDTO = new MedServiceGDTO();
//                medServiceGDTO.setMedServiceId(medServiceUDTO.getMedServiceId());
//                medServiceGDTO.setMedServiceName(medServiceUDTO.getMedServiceName());
//                medServiceGDTO.setMedServiceCode(medServiceUDTO.getMedServiceCode());
//                medServiceGDTO.setUsers(userCtrDTOList);
//                medServiceList.add(medServiceGDTO);
//            }
//            medServiceWithUsers.setMedServiceGDTO(medServiceList);
//            logger.info("<<:header:[{}]:medService:[{}]", header, medServiceWithUsers);
//            return medServiceWithUsers;
//        } catch (Exception e) {
//            logger.error("{}Excep:getAllMedServiceWithUsersByLocationId:header:{}:locationId:{}:Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
//            return null;
//        }
//
//    }
    public MedServiceWithUsers getAllMedServiceWithUsersByLocationId(String header, Long locationId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>getAllMedServiceWithUsersByLocationId:header:[{}]:locationId:[{}]", header, locationId);
            }
            MedServiceWithUsers medServiceWithUsers = new MedServiceWithUsers();
            List<MedServiceGDTO> medServiceList = new ArrayList();
            List<TblMedService> medServices = medServiceRepository.getAllMedServicesWithUsers(locationId);
            for (TblMedService medService : medServices) {
                MedServiceGDTO medServiceGDTO = new MedServiceGDTO();
                medServiceGDTO.setMedServiceId(medService.getMedServiceId());
                medServiceGDTO.setMedServiceName(medService.getMedServiceName());
                medServiceGDTO.setMedServiceCode(medService.getMedServiceCode());
                Set<UserCtrDTO> userCtrDTOList = Mapper.INSTANCE.userEntityListToUserCtrDtoList(medService.getUsers());
                medServiceGDTO.setUsers(userCtrDTOList);
                medServiceList.add(medServiceGDTO);
            }
            medServiceWithUsers.setMedServiceGDTO(medServiceList);
            logger.info("<<:header:[{}]:medService:[{}]", header, medServiceWithUsers);
            return medServiceWithUsers;
        } catch (Exception e) {
            logger.error("{}Excep:getAllMedServiceWithUsersByLocationId:header:{}:locationId:{}:Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }

    public long mappingUsersWithMedService(String header, Long userId, UserMedService userMedService) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">> mappingUsersWithMedService:header:[{}]:userMedService:[{}]", header, userMedService);
            }
            TblMedService medService = medServiceRepository.getOne(userMedService.getMedServiceId());
            Set<TblUser> users = Mapper.INSTANCE.userdtoToUserList(userMedService.getUsers());
            medService.setUsers(users);
            medService.setUpdatedBy(userId);
            medService.setUpdatedOn(dateUtils.getdate());
            medServiceRepository.saveAndFlush(medService);
            logger.info("<<:header:[{}]:medService:[{}]", header, medService);
            return medService.getMedServiceId();
        } catch (Exception e) {
            logger.error("{}Excep:mappingUsersWithMedService:header:{}:userId:{}:userMedService:{},Error:{}", header, userId, userMedService, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

}

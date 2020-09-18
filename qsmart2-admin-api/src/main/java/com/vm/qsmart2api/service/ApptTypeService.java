/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblApptType;
import com.vm.qsmart2.model.TblUser;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.appttype.ApptTypeCrtDto;
import com.vm.qsmart2api.dtos.appttype.ApptTypeDto;
import com.vm.qsmart2api.dtos.appttype.ApptTypeGetDto;
import com.vm.qsmart2api.dtos.appttype.UserApptType;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.ApptTypeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tejasri
 */
@Service
public class ApptTypeService {

    public static final Logger logger = LogManager.getLogger(ApptTypeService.class);

    @Autowired
    ApptTypeRepository apptTypeRepos;

    @Autowired
    DateUtils dateUtils;

    public ApptTypeGetDto getAllApptTypesByLocationId(String header, Long userId, Long locationId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:userId:{},locationId:[{}]", header, userId, locationId);
            }
            List<TblApptType> appt = apptTypeRepos.findApptTypesWithResourcesByLocationId(locationId);
            List<ApptTypeDto> apptDto = Mapper.INSTANCE.apptTypeEntityListToDto(appt);
            ApptTypeGetDto apptGDto = new ApptTypeGetDto();
            apptGDto.setApptTypes(apptDto);
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:Response:appttypes:[{}]", header, apptGDto.getApptTypes().size());
            }
            return apptGDto;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllApptTypesByServiceId:userId:{},locationId:{},Error:{}", header, userId, locationId, ExceptionUtils.getRootCauseMessage(e));
            return new ApptTypeGetDto(new ArrayList<>());
        }
    }

    public ApptTypeGetDto getAllApptTypesByBranchId(String header, Long userId, Long locationId, long branchId) {

        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:userId:{},locationId:[{}],brachId:[{}]", header, userId, locationId, branchId);
            }
            List<TblApptType> totalApptTypes = apptTypeRepos.findApptTypesWithResourcesByLocationId(locationId);
            System.out.println("Total Appts: " + totalApptTypes);
            List<TblApptType> mappedApptTypes = apptTypeRepos.findApptTypesByBranchId(locationId, branchId);
            System.out.println("Mapped Appts: " + mappedApptTypes);
            List<TblApptType> filterList = totalApptTypes.stream()
                    .filter(tAppt -> mappedApptTypes.stream()
                    .noneMatch(mappt
                            -> (Objects.equals(tAppt.getApptTypeId(), mappt.getApptTypeId()))))
                    .collect(Collectors.toList());
            System.out.println("fileter List: " + filterList);
            List<ApptTypeDto> apptDto = Mapper.INSTANCE.apptTypeEntityListToDto(filterList);
            ApptTypeGetDto apptGDto = new ApptTypeGetDto();
            apptGDto.setApptTypes(apptDto);
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:Response:appttypes:[{}]", header, apptGDto.getApptTypes().size());
            }
            return apptGDto;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllApptTypesByServiceId:userId:{},locationId:{},Error:{}", header, userId, locationId, ExceptionUtils.getRootCauseMessage(e));
            return new ApptTypeGetDto(new ArrayList<>());
        }
    }

    public Long save(String header, Long userId, Long locationId, ApptTypeCrtDto apptDTO) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:userId:{},locationId:[{}]:ApptTypeCrtDto:[{}]", header, userId, locationId, apptDTO);
            }
            TblApptType appt = Mapper.INSTANCE.apptTypeCrtDtoToEntity(apptDTO);
            appt.setLocationId(locationId);
            appt.setCreatedBy(userId);
            appt.setCreatedOn(dateUtils.getdate());
            appt.setUpdatedBy(userId);
            appt.setUpdatedOn(dateUtils.getdate());
            apptTypeRepos.saveAndFlush(appt);
            return appt.getApptTypeId();
        } catch (Exception e) {
            logger.error("{}Excep:save:header:[{}],userId:{}, locationId:[{}]:apptDTO:[{}],Error:{}", header, userId, locationId, apptDTO, e.getMessage());
            return null;
        }
    }

    public int updateApptType(String header, Long userId, ApptTypeDto apptDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:userId:{}, apptDto:[{}]", header, userId, apptDto);
            }
//            TblApptType appt = Mapper.INSTANCE.apptTypeDtoToEntity(apptDto);
//            appt.setUpdatedBy(userId);
//            appt.setUpdatedOn(dateUtils.getdate());
            int id = apptTypeRepos.updateApptType(apptDto.getApptType(), apptDto.getApptCode(), dateUtils.getdate(), userId, apptDto.getApptTypeId());
            return id;
        } catch (Exception e) {
            e.printStackTrace();;
            logger.error("{}Excep:updateApptType:header:{},userId:{}, apptDto:[{}],Error:{}", header, userId, apptDto, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally {
            apptDto = null;
        }
    }

    public TblApptType getAppttypeByApttypeId(String header, long apttypeId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>userId:{}, apptDto:[{}]", header, apttypeId);
            }
            TblApptType tblAppttype = apptTypeRepos.findApptTypeByApptTypeId(apttypeId);
            return tblAppttype;
        } catch (Exception e) {
            logger.error("{}Excep:getAppttypeByApttypeId:Error:{}:ApptTypeID:{}", header, ExceptionUtils.getRootCauseMessage(e), apttypeId);
            return null;
        }
    }

    public long mappingUsersWithApptType(String header, Long userId, UserApptType userApptType) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>update:header:[{}]:userApptType:[{}]", header, userApptType);
            }
            TblApptType apptType = apptTypeRepos.getOne(userApptType.getApptTypeId());
            Set<TblUser> users = Mapper.INSTANCE.userdtoToUserList(userApptType.getUsers());
            apptType.setUsers(users);
            apptType.setUpdatedBy(userId);
            apptType.setUpdatedOn(dateUtils.getdate());
            apptTypeRepos.saveAndFlush(apptType);
            return apptType.getApptTypeId();
        } catch (Exception e) {
            e.printStackTrace();;
            logger.error("{}Excep:updateApptType:header:{}:userId:{}:apptType:{},Error:{}", header, userId, userApptType, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public boolean validateApptCode(String header, Long locationId, String apptCode) {
        try {
            logger.info("{}>>: apptCode:{}:locationId:{}", header, apptCode, locationId);
            int count = apptTypeRepos.validateApptCode(apptCode, locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateApptCode:apptCode:{}:locationId:{},Error:{}", header, apptCode, locationId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateApptCode:apptCode:{}:locationId:{},Error:{}", header, apptCode, locationId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateApptType(String header, Long locationId, String apptType) {
        try {
            logger.info("{}>>: apptType:{}:locationId:{}", header, apptType, locationId);
            int count = apptTypeRepos.validateApptType(apptType, locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateApptType:apptType:{}:locationId:{},Error:{}", header, apptType, locationId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateApptType:apptType:{}:locationId:{},Error:{}", header, apptType, locationId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateApptTypeByApptTypeId(String header, Long locationId, Long apptTypeId, String apptType) {
        try {
            logger.info("{}>>: apptType:{}:apptTypeId:{}:locationId:{}", header, apptType, apptTypeId, locationId);
            int count = apptTypeRepos.validateApptTypeByApptId(apptType, apptTypeId, locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateApptTypeByApptTypeId:apptType:{}:apptTypeId:{}:locationId:{},Error:{}", header, apptType, apptTypeId, locationId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateApptTypeByApptTypeId:apptType:{}:apptTypeId:{}:locationId:{},Error:{}", header, apptType, apptTypeId, locationId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateApptCodeByApptTypeId(String header, Long locationId, Long apptTypeId, String apptCode) {
        try {
            logger.info("{}>>: apptCode:{}:apptTypeId:{}:locationId:{}", header, apptCode, apptTypeId, locationId);
            int count = apptTypeRepos.validateApptCodeByApptId(apptCode, apptTypeId, locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateApptCodeByApptId:apptCode:{}:apptTypeId:{}:locationId:{},Error:{}", header, apptCode, apptTypeId, locationId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateApptCodeByApptId:apptCode:{}:apptTypeId:{}:locationId:{},Error:{}", header, apptCode, apptTypeId, locationId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }
}

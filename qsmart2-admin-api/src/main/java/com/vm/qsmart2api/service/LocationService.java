/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblEnterprise;
import com.vm.qsmart2.model.TblLocation;
import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.location.BranchGetDto;
import com.vm.qsmart2api.dtos.location.LocationCrtDto;
import com.vm.qsmart2api.dtos.location.LocationDto;
import com.vm.qsmart2api.dtos.location.LocationGDto;
import com.vm.qsmart2api.dtos.location.LocationGetDto;
import com.vm.qsmart2api.dtos.location.LocationResponse;
import com.vm.qsmart2api.dtos.service.ServiceCrtDto;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.LocationRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
public class LocationService {

    private static final Logger logger = LogManager.getLogger(LocationService.class);

    @Autowired
    LocationRepository locationRepo;

    @Autowired
    DateUtils dateUtils;

    public long save(String header, Long userId, Long enterpriseId, LocationCrtDto locationDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>save:header:[{}]:enterpriseId:[{}]:locationDto:[{}]", header, enterpriseId, locationDto);
            }
            TblLocation location = Mapper.INSTANCE.locationCrtDtoToLocationEntity(locationDto);
            location.setStatus(1);
            location.setCreatedBy(userId);
            location.setCreatedOn(dateUtils.getdate());
            location.setUpdatedOn(dateUtils.getdate());
            location.setUpdatedBy(userId);
            location.setTblenterprise(new TblEnterprise(enterpriseId));
            locationRepo.saveAndFlush(location);
            logger.info("<<:header:[{}]:Location:[{}]", header, location);
            return location.getLocationId();
        } catch (Exception e) {
            logger.error("{}Excep:save:header:{}:userId:{}:enterpriseId:{}:locationDto:{}:Error:{}", header, userId, enterpriseId, locationDto, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public long update(String header, Long userId, LocationDto locationDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>update:[{}]:header:[{}]:Location:[{}]", header, locationDto);
            }
            TblLocation location = Mapper.INSTANCE.locationDtoToLocationEntity(locationDto);
            location.setUpdatedBy(userId);
            location.setUpdatedOn(dateUtils.getdate());
            logger.info("{}<<:header:[{}]:location:[{}]", header, location);
            locationRepo.saveAndFlush(location);
            return location.getLocationId();
        } catch (Exception e) {
            e.printStackTrace();;
            logger.error("{}Excep:update:header:{}:location:{}:Error:{}", header, locationDto, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public boolean validateLocationEngName(String header, Long enterpriseId, String locationNameEn) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:header:{}: locationNameEn:{}:enterpriseId:{}", header, locationNameEn, enterpriseId);
            }
            int count = locationRepo.validateLocationEngName(locationNameEn.toUpperCase(), enterpriseId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateLocationEngName::header:{}: locationNameEn:{}:enterpriseId:{}Error:{}", header, locationNameEn, enterpriseId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateLocationEngName::header:{}: locationNameEn:{}:enterpriseId:{}Error:{}", header, locationNameEn, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateLocationNameArbInDb(String header, Long enterpriseId, String locationNameAr) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:header:{}:locationNameAr:{}:enterpriseId:{}", header, locationNameAr, enterpriseId);
            }
            int count = locationRepo.validateLocationNameArbInDb(locationNameAr.toUpperCase(), enterpriseId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateLocationNameArbInDb::header:{}:locationNameAr:{}:enterpriseId:{}:Error:{}", header, locationNameAr, enterpriseId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateLocationNameArbInDb::header:{}:locationNameAr:{}:enterpriseId:{}:Error:{}", header, locationNameAr, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateLocationEngNameByLocationId(String header, Long enterpriseId, Long locationId, String locationNameEn) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:header:{}:locationId:{}: locationNameEn:{}:enterpriseId:{}", header, locationId, locationNameEn, enterpriseId);
            }
            int count = locationRepo.validateLocationEngNameByLocationId(locationNameEn.toUpperCase(), locationId, enterpriseId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateLocationEngNameByLocationId::header:{}:locationId:{}: locationNameEn:{}:enterpriseId:{}Error:{}", header, locationId, locationNameEn, enterpriseId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateLocationEngNameByLocationId::header:{}:locationId:{}: locationNameEn:{}:enterpriseId:{}Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateLoactionArbNameByLocationId(String header, Long enterpriseId, Long locationId, String locationNameAr) {
        try {
            logger.info("{}>>:header:{}:locationId{}: locationNameAr:{}:{}:enterpriseId:{}", header, locationId, locationNameAr, enterpriseId);
            int count = locationRepo.validateLoactionArbNameByLocationId(locationNameAr.toUpperCase(), locationId, enterpriseId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("{}Excep:validateLoactionArbNameByLocationId::header:{}:locationId{}: locationNameAr:{}:enterpriseId:{}Error:{}", header, locationId, locationNameAr, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public LocationGetDto getAllLocationsByEntId(String header, Long enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>:header:[{}]:enterpriseId:[{}]", header, enterpriseId);
            }
            return new LocationGetDto(Mapper.INSTANCE.locationEntityListToDtoList(locationRepo.findByEnterpriseId(enterpriseId)));
        } catch (Exception e) {
            logger.error("{}Excep:getAllLocationsByEntId:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return new LocationGetDto(new ArrayList<>());
        }
    }

    public long updateStatus(String header, long activateId, int status, long userId) {
        try {
            logger.info("{}>>activateId:{}:status:{}", header, activateId, status);
            Optional<TblLocation> opt = locationRepo.findById(activateId);
            if (opt.isPresent()) {
                TblLocation location = opt.get();
                location.setUpdatedBy(userId);
                location.setUpdatedOn(dateUtils.getdate());
                location.setStatus(status);
                locationRepo.save(location);
                logger.info("{}<<:userId:[{}]", header, location.getLocationId());
                return location.getLocationId();
            }
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        } catch (Exception e) {
            logger.error("{}Excep:updateLocationStatus:activateId:{}:Error:{}", header, activateId, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        }
    }

    public String ename = null;
    public LocationResponse getAllLocationsBranchesAndServsByEntId(String header, Long userId, Long enterpriseId) {
        List<Object[]> locationList = new ArrayList<>();
        if (logger.isDebugEnabled()) {
            logger.info("{}>>header:{}:userId:{}:enterpriseId:[{}]", header, userId, enterpriseId);
        }
        try {
            locationList = locationRepo.getAllLocationsBranchesAndServsByEntId(enterpriseId);
            List<LocationGDto> locList = new ArrayList<>();
            List<BranchGetDto> brnachList = new ArrayList<>();
            if (locationList != null && locationList.size() > 0) {
                locationList.forEach((obj) -> {
                    TblLocation loc = (TblLocation) obj[0];
                    LocationGDto locationDto = Mapper.INSTANCE.locationEntityToLocationGDto(loc);
                    BranchGetDto branchDto = Mapper.INSTANCE.branchEntityToBranchGetDto((TblBranch) obj[1]);
                    ServiceCrtDto serviceDto = Mapper.INSTANCE.serviceTblEntityToServiceCrtDto((TblService) obj[2]);
                    ename = loc.getTblenterprise().getEnterpriseNameEn();
                    Optional<LocationGDto> matchingObject = locList.stream().filter(buildObj -> Objects.equals(buildObj.getLocationId(), locationDto.getLocationId())).findFirst();
                    LocationGDto existDspObj = matchingObject.orElse(null);
                    if (existDspObj != null) {
                        locList.remove(existDspObj);
                        Optional<BranchGetDto> matchingFlr = existDspObj.getChildren().stream().filter(flrObj -> Objects.equals(flrObj.getBranchId(), branchDto.getBranchId())).findFirst();
                        BranchGetDto existObjBrnch = matchingFlr.orElse(null);
                        if (existObjBrnch != null) {
                            existDspObj.getChildren().remove(existObjBrnch);
                            existDspObj.addBranch(existObjBrnch);
                            Optional<ServiceCrtDto> matchingDept = existObjBrnch.getChildren().stream().filter(deptObj -> Objects.equals(deptObj.getServiceId(), serviceDto.getServiceId())).findFirst();
                            ServiceCrtDto existObjServce = matchingDept.orElse(null);
                            if (existObjServce != null) {
                                existObjBrnch.getChildren().remove(existObjServce);
                                existObjBrnch.addService(existObjServce);
                            } else {
                                existObjBrnch.addService(serviceDto);
                            }
                            brnachList.add(existObjBrnch);
                        } else {
                            existDspObj.addBranch(branchDto);
                        }
                        locList.add(existDspObj);
                    } else {
                        locationDto.addBranch(branchDto);
                        locList.add(locationDto);
                    }
                });
            }
            return new LocationResponse(ename, locList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllLocationsBranchesAndServsByEntId:header:{}:userId:{}:enterpriseId:{}:Error:{}", header, userId, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return new LocationResponse(null, new ArrayList<>());
        }finally{
            ename = null;
        }
    }
}

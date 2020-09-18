/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblBranchType;
import com.vm.qsmart2.model.TblLocation;
import com.vm.qsmart2.model.TblRoom;
import com.vm.qsmart2.model.TblRoomMaster;
import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.model.TblServiceLocation;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.branch.BranchDTO;
import com.vm.qsmart2api.dtos.branch.BranchGUDTO;
import com.vm.qsmart2api.dtos.branch.BranchResponse;
import com.vm.qsmart2api.dtos.branch.BranchRoom;
import com.vm.qsmart2api.dtos.branch.BranchRoomDto;
import com.vm.qsmart2api.dtos.branch.BranchUDTO;
import com.vm.qsmart2api.dtos.branch.BranchesServices;
import com.vm.qsmart2api.dtos.branch.BranchesServicesResponse;
import com.vm.qsmart2api.dtos.location.BranchGetDto;
import com.vm.qsmart2api.dtos.roomNo.RoomDto;
import com.vm.qsmart2api.dtos.service.ServiceCrtDto;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.BranchRepository;
import com.vm.qsmart2api.repository.ServiceLocationRepositary;
import com.vm.qsmart2api.repository.ServiceRepository;
import com.vm.qsmart2api.repository.UserProfileRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
 * @author ASHOK
 */
@Service
public class BranchService {

    public static final Logger logger = LogManager.getLogger(BranchService.class);

    @Autowired
    BranchRepository branchRepository;

//    public long save(String header, Long userId, Long locationId, BranchDTO branchDto) {
//        try {
//            if (logger.isDebugEnabled()) {
//                logger.info(">>save:header:[{}]:locationId:[{}]:branchDto:[{}]", header, locationId, branchDto);
//            }
//            TblBranch branch = Mapper.INSTANCE.branchDtoToBranch(branchDto);
//            branch.setLocation(new TblLocation(locationId));
//            branch.setCreatedBy(userId);
//            branch.setCreatedOn(new Date());
//            branch.setUpdatedBy(userId);
//            branch.setUpdatedOn(new Date());
//            logger.info("<<:header:[{}]:branch:[{}]", header, branch);
//            return branchRepository.saveAndFlush(branch).getBranchId();
//        } catch (Exception e) {
//            logger.error("{}Excep:save:header:{}:locationId:{}:Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
//            return 0;
//        }
//
//    }
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ServiceLocationRepositary serviceLocationRepositary;

    @Autowired
    DateUtils dateUtils;

    public boolean validateServiceLocationInDb(String header, Long locationId, String serviceLocation) {
        try {
            logger.info("{}>>:{}:serviceLocation:{}:locationId:{}", header, serviceLocation, locationId);
            int count = serviceLocationRepositary.validateServiceLocationInDb(serviceLocation, locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateServiceLocationInDb:serviceLocation:{}:locationId:{}:Error:{}", header, serviceLocation, locationId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateServiceLocationInDb:serviceLocation:{}:locationId:{}Error:{}", header, serviceLocation, locationId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateServiceLocationByBranchId(String header, Long locationId, Long branchId, String serviceLocation) {
        try {
            logger.info("{}>>:{}:serviceLocation:{}:locationId:{},branchId:{}", header, serviceLocation, locationId, branchId);
            int count = serviceLocationRepositary.validateServiceLocationByBranchId(serviceLocation, branchId, locationId).size();
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateServiceLocationByBranchId:serviceLocation:{}:locationId:{},branchId:{}:Error:{}", header, serviceLocation, locationId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateServiceLocationByBranchId:serviceLocation:{}:locationId:{},branchId:{},Error:{}", header, serviceLocation, locationId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    private long saveTblServiceLocation(long locationId, String serviceLocation) {
        TblServiceLocation tblServiceLoc = new TblServiceLocation();
        tblServiceLoc.setLocationId(locationId);
        tblServiceLoc.setServiceLocation(serviceLocation);
        tblServiceLoc.setCreatedOn(dateUtils.getdate());
        return serviceLocationRepositary.saveAndFlush(tblServiceLoc).getServiceLocationId();
    }

    private long serviceLocationId = 0;

    public long save(String header, Long userId, Long locationId, BranchDTO branchDto, TblBranchType branchType) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>save:header:[{}]:locationId:[{}]:branchDto:[{}]", header, locationId, branchDto);
            }
            serviceLocationId = saveTblServiceLocation(locationId, branchDto.getServiceLocation());

            TblBranch branch = Mapper.INSTANCE.branchDtoToBranch(branchDto);
            branch.setLocation(new TblLocation(locationId));
            branch.setServiceLocations(new HashSet<TblServiceLocation>() {
                {
                    add(new TblServiceLocation(serviceLocationId));
                }
            });

            branch.setCreatedBy(userId);
            branch.setCreatedOn(dateUtils.getdate());
            branch.setUpdatedBy(userId);
            branch.setUpdatedOn(dateUtils.getdate());
            logger.info("<<:header:[{}]:branch:[{}]", header, branch);
            TblBranch savedBranch = branchRepository.saveAndFlush(branch);
            //if (branchType.getBranchTypeCode().equalsIgnoreCase("CL") || branchType.getBranchTypeCode().equalsIgnoreCase("NCL") || branchType.getBranchTypeCode().equalsIgnoreCase("PH")) {
            saveDefaultService(header, userId, savedBranch);
            //}
            return savedBranch.getBranchId();
        } catch (Exception e) {
            logger.error("{}Excep:save:header:{}:locationId:{}:Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally {
            serviceLocationId = 0l;
        }

    }

    private void saveDefaultService(String header, Long userId, TblBranch savedBranch) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>save:header:[{}]:branch:[{}]", header, savedBranch);
            }
            TblService service = new TblService();
            service.setBranchId(savedBranch.getBranchId());
            service.setTokenPrefix(savedBranch.getBranchNameEn().substring(0, 2).toUpperCase());
            service.setServiceNameEn(savedBranch.getBranchNameEn() + "_sr");
            service.setServiceNameAr(savedBranch.getBranchNameAr() + "_sr");
            service.setIsDefault((short) 1);
            service.setStartSeq(1);
            service.setEndSeq(100);
            service.setMinCheckinTime(30);
            service.setMaxCheckinTime(60);
            service.setWaitTimeAvg(15);
            service.setCreatedBy(userId);
            service.setCreatedOn(dateUtils.getdate());
            service.setUpdatedOn(dateUtils.getdate());
            service.setUpdatedBy(userId);
            logger.info("<<:header:[{}]:service:[{}]", header, service);
            serviceRepository.save(service);
        } catch (Exception e) {
            logger.error("{}Excep:save:service:{}:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));

        }
    }

    public long update(String header, Long userId, Long locationId, BranchUDTO branchUDTO) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>update:header:[{}]:brachupdatedto:[{}]", header, branchUDTO);
            }
            //get tblserviceLocation 
            TblServiceLocation servicLoc = serviceLocationRepositary.getServiceLocationByServiceLocationNdLocationId(branchUDTO.getServiceLocation(), locationId);
            if (servicLoc != null) {
                serviceLocationId = servicLoc.getServiceLocationId();
            } else {
                serviceLocationId = saveTblServiceLocation(locationId, branchUDTO.getServiceLocation());
            }
            TblBranch branch = Mapper.INSTANCE.branchUpDtoToBranch(branchUDTO);
            branch.setLocation(new TblLocation(locationId));
            branch.setServiceLocations(new HashSet<TblServiceLocation>() {
                {
                    add(new TblServiceLocation(serviceLocationId));
                }
            });
            branch.setUpdatedBy(userId);
            branch.setUpdatedOn(new Date());
            logger.info("{}<<:branch:[{}]", header, branch);
            return branchRepository.saveAndFlush(branch).getBranchId();
        } catch (Exception e) {
            logger.error("{}Excep:update:branch:{}:Error:{}", header, branchUDTO, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public BranchGUDTO getAllBranchesByLocationId(String header, Long userId, Long locationId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:getAllBranchesByLocationId:locationId:[{}]", header, locationId);
            }
            List<TblBranch> listBranches = branchRepository.getAllBranchesByLocationId(locationId);
            List<BranchUDTO> branches = Mapper.INSTANCE.branchentityListToBranchUDTOList(listBranches);
//            for (TblBranch branch : listBranches) {
//                BranchUDTO branchUDTO = Mapper.INSTANCE.branchToBranchUDTO(branch);
//                branches.add(branchUDTO);
//            }
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:branches:[{}]", header, branches);
            }
            return new BranchGUDTO(branches);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesByEnterpriseId:locationId:{}:Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            return new BranchGUDTO(new ArrayList<>());
        }
    }

    public boolean validateBranchEngName(String header, Long locationId, String branchNameEn) {
        try {
            logger.info("{}>>:header:{}:branchEngName:{}:locationId:{}", header, branchNameEn, locationId);
            int count = branchRepository.validateBranchEngName(branchNameEn.toUpperCase(), locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateBranchEngName:header:{}:branchEngName:{}:locationId:{}:Error:{}", header, branchNameEn, locationId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateBranchEngName::header:{}:branchEngName:{}:locationId:{}Error:{}", header, branchNameEn, locationId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateBranchNameArbInDb(String header, Long locationId, String branchNameAr) {

        try {
            logger.info("{}>>:branchArbName:{}:locationId:{}", header, branchNameAr, locationId);
            int count = branchRepository.validateBranchNameArb(branchNameAr.toUpperCase(), locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateBranchArbName:branchArbName:{}:locationId:{}Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateBranchArbName:branchArbName:{}:locationId:{}Error:{}", header, locationId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateBranchEngNameByBranchId(String header, Long locationId, Long branchId, String branchNameEn) {
        try {
            logger.info("{}>>: branchEngName:{}:branchId:{}:locationId:{}", header, branchNameEn, branchId, locationId);
            int count = branchRepository.validateBranchEngNameByBranchId(branchNameEn.toUpperCase(), branchId, locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateBranchEngNameByBranchId:branchEngName:{}:branchId:{}:locationId:{}Error:{}", header, branchNameEn, branchId, locationId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateBranchEngNameByBranchId:branchEngName:{}:branchId:{}:locationId:{}Error:{}", header, branchNameEn, branchId, locationId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateBranchArbNameByBranchId(String header, Long locationId, Long branchId, String branchNameAr) {
        try {
            logger.info("{}>>: branchArbName:{}:branchId:{}:locationId:{}", header, branchNameAr, branchId, locationId);
            int count = branchRepository.validateBranchArbNameByBranchId(branchNameAr.toUpperCase(), branchId, locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("{}Excep:validateBranchArbNameByBranchId:branchArbName:{}:branchId:{}:locationId:{}:Error:{}", header, branchNameAr, branchId, locationId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public String ename = null;

    public BranchResponse getAllBranchesAndServsByLocationId(String header, Long userId, Long locationId) {
        List<Object[]> branchesList = new ArrayList<>();
        if (logger.isDebugEnabled()) {
            logger.info("{}>>header:{}:userId:{}:locationId:[{}]", header, userId, locationId);
        }
        try {
            branchesList = branchRepository.getAllBranchesAndServsByLocationId(locationId);
            List<BranchGetDto> brnachList = new ArrayList<>();
            if (branchesList != null && branchesList.size() > 0) {
                branchesList.forEach((obj) -> {
                    TblBranch loc = (TblBranch) obj[0];
                    BranchGetDto branchDto = Mapper.INSTANCE.branchEntityToBranchGetDto(loc);
                    ServiceCrtDto serviceDto = Mapper.INSTANCE.serviceTblEntityToServiceCrtDto((TblService) obj[1]);
                    ename = loc.getLocation().getLocationNameEn();
                    Optional<BranchGetDto> matchingObject = brnachList.stream().filter(branchObj -> Objects.equals(branchObj.getBranchId(), branchDto.getBranchId())).findFirst();
                    BranchGetDto existDspObj = matchingObject.orElse(null);
                    if (existDspObj != null) {
                        brnachList.remove(existDspObj);
                        Optional<ServiceCrtDto> matchingDept = existDspObj.getChildren().stream().filter(serObj -> Objects.equals(serObj.getServiceId(), serviceDto.getServiceId())).findFirst();
                        ServiceCrtDto existObjServce = matchingDept.orElse(null);
                        if (existObjServce != null) {
                            existDspObj.getChildren().remove(existObjServce);
                            existDspObj.addService(existObjServce);
                        } else {
                            existDspObj.addService(serviceDto);
                        }
                        brnachList.add(existDspObj);
                    } else {
                        brnachList.add(branchDto);
                    }
                });
            }
            return new BranchResponse(ename, brnachList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllBranchesAndServsByLocationId:header:{}:userId:{}:locationId:{}:Error:{}", header, userId, locationId, ExceptionUtils.getRootCauseMessage(e));
            return new BranchResponse(null, new ArrayList<>());
        } finally {
            ename = null;
        }
    }

    public BranchGUDTO getAllBranchesByLocIdAndBranchType(String header, Long userId, Long locationId, Long branchTypeId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:getAllBranchesByLocIdAndBranchType:locationId:[{}]:branchTypeId:{}", header, locationId);
            }
            List<TblBranch> listBranches = branchRepository.getAllBranchesByLocIdAndBranchType(locationId, branchTypeId);
            List<BranchUDTO> branches = Mapper.INSTANCE.branchentityListToBranchUDTOList(listBranches);
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:locationId:[{}]:branchTypeId:{}", header, branches);
            }
            return new BranchGUDTO(branches);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBranchesByLocIdAndBranchType:locationId:{}:branchTypeId:{}:Error:{}", header, locationId, branchTypeId, ExceptionUtils.getRootCauseMessage(e));
            return new BranchGUDTO(new ArrayList<>());
        }
    }

    //public String ename = null;
    public BranchesServicesResponse getAllBranchesAndServsByLocationIdAndBranchType(String header, Long userId, Long locationId, String branchType) {
        List<Object[]> branchesList = new ArrayList<>();
        if (logger.isDebugEnabled()) {
            logger.info("{}>>userId:{}:locationId:[{}]:branchTypeId:{}", header, userId, locationId, branchType);
        }
        try {
            branchesList = branchRepository.getAllBranchesAndServsByLocationIdAndBranchType(locationId, branchType);
            List<BranchesServices> brnachList = new ArrayList<>();
            if (branchesList != null && branchesList.size() > 0) {
                branchesList.forEach((obj) -> {
                    TblBranch loc = (TblBranch) obj[0];
                    BranchesServices branchDto = Mapper.INSTANCE.branchToBranchServices(loc);
                    ServiceCrtDto serviceDto = Mapper.INSTANCE.serviceTblEntityToServiceCrtDto((TblService) obj[1]);
                    //System.out.println("Service :"+serviceDto.toString());
                    Optional<BranchesServices> matchingObject = brnachList.stream().filter(branchObj -> Objects.equals(branchObj.getBranchId(), branchDto.getBranchId())).findFirst();
                    BranchesServices existDspObj = matchingObject.orElse(null);
                    if (existDspObj != null) {
                        brnachList.remove(existDspObj);
                        Optional<ServiceCrtDto> matchingDept = existDspObj.getServices().stream().filter(serObj -> Objects.equals(serObj.getServiceId(), serviceDto.getServiceId())).findFirst();
                        ServiceCrtDto existObjServce = matchingDept.orElse(null);
                        if (existObjServce != null) {
                            existDspObj.getServices().remove(existObjServce);
                            existDspObj.addService(existObjServce);
                        } else {
                            existDspObj.addService(serviceDto);
                        }
                        brnachList.add(existDspObj);
                    } else {
                        branchDto.addService(serviceDto);
                        brnachList.add(branchDto);
                    }
                });
            }
            return new BranchesServicesResponse(brnachList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllBranchesAndServsByLocationIdAndBranchType:userId:{}:locationId:{}:branchTypeId:{}:Error:{}", header, userId, locationId, branchType, ExceptionUtils.getRootCauseMessage(e));
            return new BranchesServicesResponse(new ArrayList<>());
        } finally {
            ename = null;
        }
    }

    @Autowired
    UserProfileRepository userProfileRepository;

    public BranchRoomDto getAllBranchesWithRoomsByUserId(String header, Long userId) {
        List<Object[]> branchesList = new ArrayList<>();
        if (logger.isDebugEnabled()) {
            logger.info("{}>>header:{}:userId:[{}]", header, userId);
        }
        try {
            branchesList = userProfileRepository.getAllBranchesWithRoomsByUserId(userId);
            System.out.println("---->" + branchesList.size());
            List<BranchRoom> brnachList = new ArrayList<>();
            if (branchesList != null && !branchesList.isEmpty()) {
                branchesList.forEach((obj) -> {
                    BranchRoom branchDto = Mapper.INSTANCE.branchEntityToBranchRoom((TblBranch) obj[0]);
                    //RoomDto roomDto = Mapper.INSTANCE.roomMasterEntityTblToRoomDto((TblRoomMaster) obj[1]);
                    TblRoomMaster room = (TblRoomMaster) obj[1];
                    if (branchDto != null && room != null) {
                        //System.out.println("--->"+room.getRooms());
                        if (room.getRooms()
                                != null && !room.getRooms().isEmpty()) {
                            for (TblRoom roomNO : room.getRooms()) {
                                RoomDto roomDto = new RoomDto();
                                roomDto.setMaxAllowedToken(room.getMaxAllowedToken());
                                roomDto.setRoomNameEn(room.getRoomNameEn());
                                roomDto.setRoomNameAr(room.getRoomNameAr());
                                roomDto.setRoomId(roomNO.getRoomId());
                                roomDto.setRoomNo(roomNO.getRoomNo());
                                //  System.out.println("room ->"+roomDto.toString());
                                Optional<BranchRoom> matchingObject = brnachList.stream().filter(branchObj -> Objects.equals(branchObj.getBranchId(), branchDto.getBranchId())).findFirst();
                                BranchRoom existDspObj = matchingObject.orElse(null);
                                if (existDspObj != null) {
                                    brnachList.remove(existDspObj);
//                                Optional<RoomDto> matchingDept = existDspObj.getRooms().stream().filter(serObj -> Objects.equals(serObj.getRoomId(), roomDto.getRoomId())).findFirst();
//                                RoomDto existObjServce = matchingDept.orElse(null);
//                                if (existObjServce != null) {
//                                    existDspObj.getRooms().remove(existObjServce);
//                                    existDspObj.addRoom(existObjServce);
//                                } else {
//                                    existDspObj.addRoom(roomDto);
//                                }
                                    existDspObj.addRoom(roomDto);
                                    brnachList.add(existDspObj);
                                } else {
                                    branchDto.addRoom(roomDto);
                                    brnachList.add(branchDto);
                                }
                            }

                        }
                    }
                });
            }
            return new BranchRoomDto(brnachList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllBranchesWithRoomsByUserId:header:{}:userId:{}:Error:{}", header, userId, ExceptionUtils.getRootCauseMessage(e));
            return new BranchRoomDto(new ArrayList<>());
        }
    }
}

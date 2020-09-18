/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblApptType;
import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.service.ApptServicDto;
import com.vm.qsmart2api.dtos.service.ServiceCrtDto;
import com.vm.qsmart2api.dtos.service.ServiceDTO;
import com.vm.qsmart2api.dtos.service.ServiceGUDTO;
import com.vm.qsmart2api.dtos.service.ServiceGetDto;
import com.vm.qsmart2api.dtos.service.ServiceResponse;
import com.vm.qsmart2api.dtos.service.ServiceUDTO;
import com.vm.qsmart2api.dtos.service.ServiceUpDto;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.BranchRepository;
import com.vm.qsmart2api.repository.ServiceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.NoResultException;
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
public class ServiceService {

    public static final Logger logger = LogManager.getLogger(ServiceService.class);

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    DateUtils dateUtils;

    public long saveService(String header, Long userId, Long branchId, ServiceDTO serviceDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">> save:header:[{}]:branchId:{}:serviceDto:[{}]", header, branchId, serviceDto);
            }
            TblService service = Mapper.INSTANCE.serviceDtoToService(serviceDto);
            service.setBranchId(branchId);
            service.setIsDefault((short) 0);
            service.setCreatedBy(userId);
            service.setCreatedOn(dateUtils.getdate());
            service.setUpdatedOn(dateUtils.getdate());
            service.setUpdatedBy(userId);
            serviceRepository.saveAndFlush(service);
            logger.info("<<:header:[{}]:service:[{}]", header, service);
            return service.getServiceId();
        } catch (Exception e) {
            e.printStackTrace();;
            logger.error("{}Excep:saveServiceInDb:header:{}:userId:{}:branchId:{}:serviceDto:{},Error:{}", header, userId, branchId, serviceDto, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }

    }

    public int updateService(String header, long userId, ServiceUpDto serviceUpDTO) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>update:[{}]:header:[{}]:serviceUDTO:[{}]", header, serviceUpDTO);
            }
//            TblService service = Mapper.INSTANCE.serviceUpDtoToService(serviceUpDTO);
//            service.setUpdatedBy(userId);
//            service.setUpdatedOn(dateUtils.getdate());
            int serviceId = serviceRepository.updateService(serviceUpDTO.getServiceId(), serviceUpDTO.getServiceNameEn(), serviceUpDTO.getServiceNameAr(), serviceUpDTO.getTokenPrefix(),
            serviceUpDTO.getStartSeq(), serviceUpDTO.getEndSeq(), serviceUpDTO.getWaitTimeAvg(), serviceUpDTO.getMinCheckinTime(), serviceUpDTO.getMaxCheckinTime(), userId, dateUtils.getdate());
            return serviceId;
        } catch (Exception e) {
            e.printStackTrace();;
            logger.error("{}Excep:updateService:header:{},userId:{},serviceUpDTO:{},Error:{}", header, userId, serviceUpDTO, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally {
            serviceUpDTO = null;
        }
    }

    public ServiceGUDTO getAllServicesByBranchId(String header, long userId, long branchId) {
        if (logger.isDebugEnabled()) {
            logger.info("{}>>:userId:{},branchId:[{}]", header, userId, branchId);
        }
        try {
            List<TblService> listService = serviceRepository.getAllServicesByBranchId(branchId);
            List<ServiceUDTO> services = new ArrayList<>();
            if (listService != null && listService.size() > 0) {
                services = Mapper.INSTANCE.listserviceToServiceUDtO(listService);
            }
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:Response:services:[{}]", header, services);
            }
            return new ServiceGUDTO(services);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllServicesByBranchId:userId:{},branchId:[{}],Error:{}", header, userId, branchId, ExceptionUtils.getRootCauseMessage(e));
            return new ServiceGUDTO(new ArrayList<>());
        }
    }

    public boolean validateServiceEngName(String header, Long branchId, String serviceNameEn) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:header:{},serviceNameEn:{},branchId:{}", header, serviceNameEn, branchId);
            }
            int count = serviceRepository.validateServiceEngName(serviceNameEn.toUpperCase(), branchId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateBranchEngName:header:{},serviceNameEn:{},branchId:{},Error:{}", header, serviceNameEn, branchId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateBranchEngName:header:{},serviceNameEn:{},branchId:{},Error:{}", header, serviceNameEn, branchId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }

    }

    public boolean validateServiceNameArb(String header, Long branchId, String serviceNameAr) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:header:{},ServiceArbName:{},branchId:{}", header, serviceNameAr, branchId);
            }
            int count = serviceRepository.validateServiceNameArb(serviceNameAr.toUpperCase(), branchId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateServiceArbName:ServiceArbName:{},branchId:{},Error:{}", header, serviceNameAr, branchId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateServiceArbName:ServiceArbName:{},branchId:{},Error:{}", header, serviceNameAr, branchId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateServiceEngNameByServiceId(String header, long branchId, Long serviceId, String serviceNameEn) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>: header:{},serviceEngName:{}:id:{}:branchId:{}", header, serviceNameEn, serviceId, branchId);
            }
            int count = serviceRepository.validateServiceEngNameByServiceId(serviceNameEn.toUpperCase(), serviceId, branchId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateServiceEngNameByServiceId:header:{},serviceEngName:{}:id:{}:branchId:{},Error:{}", header, serviceNameEn, serviceId, branchId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateServiceEngNameByServiceId:header:{},serviceEngName:{}:id:{}:branchId:{},Error:{}", header, serviceNameEn, serviceId, branchId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateServiceArbNameByServiceId(String header, long branchId, long serviceId, String serviceNameAr) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:ServiceArbName:{}:id:{}:branchId:{}", header, serviceNameAr, serviceId, branchId);
            }
            int count = serviceRepository.validateServiceArbNameByServiceId(serviceNameAr.toUpperCase(), serviceId, branchId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateServiceArbNameByServiceId:header:{},ServiceArbName:{}:id:{}:branchId:{},Error:{}", header, serviceNameAr, serviceId, branchId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateServiceArbNameByServiceId:header:{},ServiceArbName:{}:id:{}:branchId:{},Error:{}", header, serviceNameAr, serviceId, branchId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public int checkservicesBybranchId(Long branchId) {
        return serviceRepository.countServicesByBranchId(branchId);
    }

    public long mappingServiceWithApptTypes(String header, Long userId, ApptServicDto serviceDto) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:serviceDto:[{}]", header, serviceDto);
            }
            TblService service = serviceRepository.getOne(serviceDto.getServiceId());
            Set<TblApptType> appt = Mapper.INSTANCE.apptTypeServDtoToServiceEntity(serviceDto.getApptTypes());
            service.setApptTypes(appt);
            //TblService services = serviceRepository.mappingServiceWithApptTypes(branchId);
            service.setUpdatedBy(userId);
            serviceRepository.saveAndFlush(service);
            logger.info("{}<<:service:[{}]", header, service);
            return service.getServiceId();
        } catch (Exception e) {
            e.printStackTrace();;
            logger.error("{}Excep:mappingServiceWithApptTypes:header:{}:userId:{}:serviceDto:{},Error:{}", header, userId, serviceDto, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }

    }

    public TblService getServiceById(String header, long serviceId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>> ServiceId:{}", header, serviceId);
            }
            return serviceRepository.findServiceByServiceId(serviceId);
        } catch (Exception e) {
            e.printStackTrace();;
            logger.error("{}Excep:getServiceById:serviceId:{},Error:{}", header, serviceId, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }

    public ServiceGetDto getAllServicesByBranches(String header, long userId, List<Long> branches) {
        if (logger.isDebugEnabled()) {
            logger.info("{}>>:userId:{},branchId:[{}]", header, userId, branches);
        }
        try {
            List<TblService> serviceList = serviceRepository.getAllServicesByBranches(branches);
            List<ServiceCrtDto> servceDto = Mapper.INSTANCE.serviceTblEntityToServiceGetDto(serviceList);
            ServiceGetDto serList = new ServiceGetDto();
            serList.setServices(servceDto);
            return serList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllServicesByBranches:userId:{},branches:[{}],Error:{}", header, userId, branches, ExceptionUtils.getRootCauseMessage(e));
            return new ServiceGetDto(new ArrayList<>());
        }
    }

    @Autowired
    BranchRepository branchRepository;

    public String ename = null;

    public ServiceResponse getAllServicesByBranchIdInDb(String header, long userId, Long branchId) {
        if (logger.isDebugEnabled()) {
            logger.info("{}>>:userId:{},branchId:[{}]", header, userId, branchId);
        }
        try {
            List<TblService> listService = serviceRepository.getAllServicesByBranchId(branchId);
            List<ServiceCrtDto> services = new ArrayList<>();
            TblBranch branch = branchRepository.findBrnachNameBybranchId(branchId);
            ename = branch.getBranchNameEn();
            if (listService != null && listService.size() > 0) {
                services = Mapper.INSTANCE.serviceTblEntityToServiceGetDto(listService);
            }
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:Response:services:[{}]", header, services);
            }
            return new ServiceResponse(ename, services);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllServicesByBranchIdInDb:userId:{},branchId:[{}],Error:{}", header, userId, branchId, ExceptionUtils.getRootCauseMessage(e));
            return new ServiceResponse(null, new ArrayList<>());
        }
    }
}

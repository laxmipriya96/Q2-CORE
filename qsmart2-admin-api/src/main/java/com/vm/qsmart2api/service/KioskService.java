/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2.model.TblAppointment;
import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblKiosk;
import com.vm.qsmart2.model.TblPatient;
import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.model.TblServiceBooked;
import com.vm.qsmart2.model.TblToken;
import com.vm.qsmart2.model.TblTokenSequence;
import com.vm.qsmart2.utils.ApptStatus;
import com.vm.qsmart2.utils.CdrLog;
import com.vm.qsmart2api.dtos.kiosk.KioskCrtDto;
import com.vm.qsmart2api.dtos.kiosk.KioskGetResponse;
import com.vm.qsmart2api.dtos.kiosk.KioskUpDto;
import com.vm.qsmart2api.repository.KioskRepository;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2.utils.TokenPrintData;
import com.vm.qsmart2.utils.TokenStatus;
import com.vm.qsmart2.utils.TokenTemplate;
import com.vm.qsmart2.utils.TriggerTypeDTO;
import com.vm.qsmart2api.dtos.kiosk.KioskAppointmentDTO;
import com.vm.qsmart2api.dtos.kiosk.KioskAppointmentGDTO;
import com.vm.qsmart2api.dtos.kiosk.KioskThemeDto;
import com.vm.qsmart2api.dtos.manualtoken.ManualTokenDTO;
import com.vm.qsmart2api.dtos.manualtoken.ManualTokenResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vm.qsmart2api.mapper.DevicesMapper;
import com.vm.qsmart2api.repository.AppointmentRepository;
import com.vm.qsmart2api.repository.BranchRepository;
import com.vm.qsmart2api.repository.PatientRepository;
import com.vm.qsmart2api.repository.ServiceBookedRepository;
import com.vm.qsmart2api.repository.ServiceRepository;
import com.vm.qsmart2api.repository.TokenRepository;
import com.vm.qsmart2api.repository.TokenSequenceRepositary;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

/**
 *
 * @author Phani
 */
@Service
public class KioskService {

    private static final Logger logger = LogManager.getLogger(KioskService.class);

    @Autowired
    KioskRepository kioskRepository;

    @Autowired
    DateUtils dateUtils;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    MessageSource messageSource;

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    @Autowired
    ServiceBookedRepository serviceBookedRepository;

    @Autowired
    AppointmentRepository apptRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ManualTokenService manualTokenService;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    TokenSequenceRepositary tokenSequenceRepositary;

    @Value("${hl7.cdr.queue.name:Q2_HL7_CDR}")
    private String cdrQueueName;
    
    
    @Value("${qsmart.trigger.queue.name:Q2_TRIGGER}")
    private String q2TriggerQueueName;

    @Value("${trigger.notification.enabled:true}")
    private boolean isTriggerNotification;

    public boolean validateKioskName(String header, Long locationId, String kioskName) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>header:{},locationId:{}:displayName:{}", header, locationId, kioskName);
            }
            int count = kioskRepository.validateKioskName(kioskName.toUpperCase(), locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateKioskName:locationId:{}:displayName:{},Error:{}", header, locationId, kioskName, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateKioskName:locationId:{}:displayName:{},Error:{}", header, locationId, kioskName, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validatekioskNameByKisokId(String header, Long locationId, Long kioskId, String kioskName) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>header:{},locationId:{}:kioskId:{}:kioskName:{}", header, locationId, kioskId, kioskName);
            }
            int count = kioskRepository.validateKioskNameByKioskId(kioskName.toUpperCase(), kioskId, locationId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validatekioskNameByKisokId:locationId:{}:KisokId:{}:KisokName:{}:Error:{}", header, locationId, kioskId, kioskName, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validatekioskNameByKisokId:locationId:{}:KisokId:{}:KioskNmae:{}:Error:{}", header, locationId, kioskId, kioskName, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public long saveKioskInDb(String header, Long userId, KioskCrtDto kiosk, Long locationId, Long enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>save:locationId:{}:kiosk:[{}]", header, locationId, kiosk);
            }
            TblKiosk kioskObj = DevicesMapper.INSTANCE.kioskCrtDtoToKioskEntity(kiosk);
            kioskObj.setLocationId(locationId);
            kioskObj.setKioskIdentifier(RandomStringUtils.randomAlphanumeric(8));
            kioskObj.setEnterpriseId(enterpriseId);
            kioskObj.setCreatedBy(userId);
            kioskObj.setStatus(1);
            kioskObj.setCreatedOn(dateUtils.getdate());
            kioskObj.setUpdatedOn(dateUtils.getdate());
            kioskObj.setUpdatedBy(userId);
            kioskObj = kioskRepository.saveAndFlush(kioskObj);
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:Room:[{}]", header, kioskObj.getKioskId());
            }
            return kioskObj.getKioskId();
        } catch (Exception e) {
            logger.error("{}Excep:saveKioskInDb:locationId:{}:display:{}:Error:{}", header, locationId, kiosk, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public long updateKisokInDb(String header, Long userId, long locationId, long enterpriseId, KioskUpDto kioskUd) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>userId:{},locationId:{},enterpriseId:{},DisplayUpDto:[{}]", header, userId, locationId, enterpriseId, kioskUd);
            }
            TblKiosk kiosk = DevicesMapper.INSTANCE.kioskUDtoToKioskEntity(kioskUd);
            kiosk.setUpdatedBy(userId);
            kiosk.setUpdatedOn(dateUtils.getdate());
            kiosk.setLocationId(locationId);
            kiosk.setEnterpriseId(enterpriseId);
            logger.info("{}<<header:[{}]:display:[{}]", header, kioskUd);
            kioskRepository.saveAndFlush(kiosk);
            return kiosk.getKioskId();
        } catch (Exception e) {
            logger.error("{}Excep:updateKisokInDb:userId:{},locationId:{},enterpriseId:{},KioskUpdateDto:[{}],Error:{}", header, userId, locationId, enterpriseId, kioskUd, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally {
            kioskUd = null;
        }
    }

    public KioskGetResponse getAllKiosksByLocationId(String header, Long userId, Long locationId) {
        if (logger.isDebugEnabled()) {
            logger.info("{}>>:userId:{},locationId:[{}]", header, userId, locationId);;
        }
        try {
            List<KioskUpDto> displayseList = DevicesMapper.INSTANCE.kioskEntityListToKioskUpDtoList(kioskRepository.findByLocationId(locationId));
            //List<DisplayUpDto> displayList = DevicesMapper.INSTANCE.displayEntityListToDisplayUpDtoList(displayRepository.findByLocationId(locationId));
            return new KioskGetResponse(displayseList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllKiosksByLocationId:userId:{},locationId:{},Error:{}", header, userId, locationId, ExceptionUtils.getRootCauseMessage(e));
            return new KioskGetResponse(new ArrayList<>());
        }
    }

    public long updateStatus(String header, long kioskId, int status, long userId) {
        try {
            logger.info("{}>>kioskId:{}:status:{}", header, kioskId, status);
            Optional<TblKiosk> opt = kioskRepository.findById(kioskId);
            if (opt.isPresent()) {
                TblKiosk kiosk = opt.get();
                kiosk.setUpdatedBy(userId);
                kiosk.setUpdatedOn(dateUtils.getdate());
                kiosk.setStatus(status);
                kioskRepository.save(kiosk);
                logger.info("{}<<:userId:[{}]", header, kiosk.getKioskId());
                return kiosk.getKioskId();
            }
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        } catch (Exception e) {
            logger.error("{}Excep:updateKioskStatus:kioskId:{}:Error:{}", header, kioskId, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<:userId:[{}]", header, 0);
            return 0;
        }
    }

    public KioskAppointmentGDTO getAppointmentsByMrnNoAndKioskId(String header, String mrnNo, String kioskIdentifier, Locale locale) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:getAppointmentsByMrnNoAndKioskId:mrnNo:[{}]:kioskIdentifier:[{}]", header, mrnNo, kioskIdentifier);
            }
            TblPatient patient = null;
            patient = patientRepository.getPatientByMrnNo(mrnNo);
            if (patient != null) {
                Long locationId = kioskRepository.getLocationIdByKioskIdentifier(kioskIdentifier);
                Date fromdate = dateUtils.dayStartDateTime();
                Date toDate = dateUtils.dayEndDateTime();
                List<KioskAppointmentDTO> listDto = patientRepository.getAppointmentsByMrnNoAndkioskId(mrnNo, locationId, ApptStatus.SCHEDULED.getValue(), fromdate, toDate);
                if (listDto.isEmpty()) {
                    return new KioskAppointmentGDTO(false, messageSource.getMessage("search.mrnNo.not.found", null, locale), listDto);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.info("{}<<:KioskAppointmentGDTO:[{}]", header, listDto);
                    }
                    return new KioskAppointmentGDTO(true, messageSource.getMessage("search.mrnNo.found", null, locale), listDto);
                }
            } else {
                return new KioskAppointmentGDTO(false, messageSource.getMessage("mrnNo.not.found", null, locale), new ArrayList<>());
            }
        } catch (Exception e) {
            logger.error("{}Excep:getAppointmentsByMrnNoAndKioskId:mrnNo:{}:kioskIdentifier:{}:Error:{}", header, mrnNo, kioskIdentifier, ExceptionUtils.getRootCauseMessage(e));
            return new KioskAppointmentGDTO(new ArrayList<>());
        }
    }

    @Transactional
    public ManualTokenResponse generateTokenByMrnNoAndKiosk(String header, String mrnNo, String kioskIdentifier, String servingType, Locale locale) {
        ManualTokenResponse manualTokenResponse = new ManualTokenResponse();
        ManualTokenDTO manualTokenDTO = new ManualTokenDTO();
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:generateTokenByMrnNoAndKiosk:mrnNo:[{}]:kioskIdentifier:[{}]:servingType:[{}]", header, mrnNo, kioskIdentifier, servingType);
            }
            Long locationId = kioskRepository.getLocationIdByKioskIdentifier(kioskIdentifier);
            System.out.println("locationId>>>>" + locationId);
            Long branchId;
            List<TblBranch> branches = new ArrayList<>();
            TblPatient patient;
            TblService service;
            patient = patientRepository.getPatientByMrnNo(mrnNo);
            short isDefault = 1;
            if (patient != null) {
                manualTokenDTO.setMrnNo(patient.getMrnNo());
                manualTokenDTO.setAppointmentTime(dateUtils.getCurrentDaString());
                manualTokenDTO.setDrId((long) 0);
                manualTokenDTO.setAccompanyVisitors(0);
                switch (servingType.toUpperCase()) {
                    case "REGISTRATION":
                        branches = branchRepository.getAllBranchesByLocationIdAndBranchTypeCode(locationId, "NCL");
                        if (!branches.isEmpty()) {
                            branchId = branches.get(0).getBranchId();
                            service = serviceRepository.getDefaultServiceByBranchId(branchId, isDefault);
                            manualTokenDTO.setBranchId(branchId);
                            manualTokenDTO.setServiceId(service.getServiceId());
                            manualTokenResponse = saveAppointmentByKiosk(header, patient, locationId, manualTokenDTO, locale);
                        } else {
                            manualTokenResponse.setStatus(false);
                            manualTokenResponse.setMessages(messageSource.getMessage("dept.not.found", null, locale));
                        }
                        break;
                    case "PHARMACY":
                        branches = branchRepository.getAllBranchesByLocationIdAndBranchTypeCode(locationId, "PH");
                        if (!branches.isEmpty()) {
                            branchId = branches.get(0).getBranchId();
                            service = serviceRepository.getDefaultServiceByBranchId(branchId, isDefault);
                            manualTokenDTO.setBranchId(branchId);
                            manualTokenDTO.setServiceId(service.getServiceId());
                            manualTokenResponse = saveAppointmentByKiosk(header, patient, locationId, manualTokenDTO, locale);
                        } else {
                            manualTokenResponse.setStatus(false);
                            manualTokenResponse.setMessages(messageSource.getMessage("dept.not.found", null, locale));
                        }
                        break;
                    case "LABORATORY":
                        branches = branchRepository.getAllBranchesByLocationIdAndBranchTypeCode(locationId, "LAB");
                        if (!branches.isEmpty()) {
                            branchId = branches.get(0).getBranchId();
                            System.out.println("branchId>>>>" + branchId);
                            System.out.println("ashok>>>>" + "ashok");
                            service = serviceRepository.getDefaultServiceByBranchId(branchId, isDefault);
                            System.out.println("service>>>>" + service.getServiceNameEn());
                            manualTokenDTO.setBranchId(branchId);
                            manualTokenDTO.setServiceId(service.getServiceId());
                            manualTokenResponse = saveAppointmentByKiosk(header, patient, locationId, manualTokenDTO, locale);
                        } else {
                            manualTokenResponse.setStatus(false);
                            manualTokenResponse.setMessages(messageSource.getMessage("dept.not.found", null, locale));
                        }
                        break;

                    case "RADIOLOGY":
                        branches = branchRepository.getAllBranchesByLocationIdAndBranchTypeCode(locationId, "RG");
                        if (!branches.isEmpty()) {
                            branchId = branches.get(0).getBranchId();
                            System.out.println("branchId>>>>" + branchId);
                            System.out.println("ashok>>>>" + "ashok");
                            service = serviceRepository.getDefaultServiceByBranchId(branchId, isDefault);
                            System.out.println("service>>>>" + service.getServiceNameEn());
                            manualTokenDTO.setBranchId(branchId);
                            manualTokenDTO.setServiceId(service.getServiceId());
                            manualTokenResponse = saveAppointmentByKiosk(header, patient, locationId, manualTokenDTO, locale);
                        } else {
                            manualTokenResponse.setStatus(false);
                            manualTokenResponse.setMessages(messageSource.getMessage("dept.not.found", null, locale));
                        }
                        break;

                    default:
                        manualTokenResponse.setStatus(false);
                        manualTokenResponse.setMessages(messageSource.getMessage("dept.not.found", null, locale));
                        break;

                }
                return manualTokenResponse;
            } else {
                manualTokenResponse.setStatus(false);
                manualTokenResponse.setMessages(messageSource.getMessage("mrnNo.not.found.crtl.fail", null, locale));
                return manualTokenResponse;
            }
        } catch (Exception e) {
            logger.error("{}Excep:generateTokenByMrnNoAndKiosk:mrnNo:{}:kioskIdentifier:{}:servingType:{}:Error:{}", header, mrnNo, kioskIdentifier, servingType, ExceptionUtils.getRootCauseMessage(e));
            e.printStackTrace();
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("kiosktoken.crtl.fail", null, locale));
            return manualTokenResponse;
        }
    }

    @Transactional
    ManualTokenResponse saveAppointmentByKiosk(String header, TblPatient patient, Long locationId, ManualTokenDTO manualTokenDTO, Locale locale) {
        if (isLogEnabled) {
            logger.info("{}>>TblPatient:[{}]:locationId:[{}]:ManualTokenDTO:[{}]", header, patient, locationId, manualTokenDTO);
        }
        TblServiceBooked serviceBooked = null;
        try {
            serviceBooked = createNewTblAppointment(header, manualTokenDTO, patient, locationId);
            if (isLogEnabled) {
                logger.info("{}<<saveAppointment:TblPatient:[{}]:locationId:[{}]:ManualTokenDTO:[{}]", header, patient, locationId, manualTokenDTO);
            }
            return generateToken(header, patient, serviceBooked, manualTokenDTO, locale);
        } catch (Exception e) {
            logger.error("{}Excep:saveAppointment:TblPatient:[{}]:locationId:[{}]:ManualTokenDTO:[{}]:Error:{}", header, patient, locationId, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
            ManualTokenResponse manualTokenResponse = new ManualTokenResponse();
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("kiosktoken.crtl.fail", null, locale));
            return manualTokenResponse;
        } finally {
            serviceBooked = null;
        }
    }

    @Transactional
    public TblServiceBooked createNewTblAppointment(String header, ManualTokenDTO manualTokenDTO, TblPatient patient, long locationId) {
        TblServiceBooked serviceBooked1 = null;
        TblAppointment appointment = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>ManualTokenDTO:[{}],TblPatient:[{}],locationId:[{}]", header, manualTokenDTO, patient, locationId);
            }
            appointment = new TblAppointment();
            appointment.setPatientId(patient.getPatientId());
            appointment.setTranId(dateUtils.generateUniqueTranId());
            appointment.setClerkId((long) 0);
            appointment.setCreatedOn(dateUtils.getdate());
            appointment.setUpdatedOn(dateUtils.getdate());
            appointment.setStartTime(dateUtils.getdate());
            appointment.setApptStatus(ApptStatus.KIOSKCHECKIN.getValue());
            appointment.setLocationId(locationId);
            apptRepository.saveAndFlush(appointment);

            serviceBooked1 = new TblServiceBooked();
            serviceBooked1.setAppointment(appointment);
            if (manualTokenDTO.getDrId() > 0) {
                serviceBooked1.setDrId(manualTokenDTO.getDrId());
            }
            serviceBooked1.setServiceStatus(ApptStatus.KIOSKCHECKIN.getValue());
            serviceBooked1.setEncounterId(RandomStringUtils.randomAlphanumeric(8).toUpperCase(Locale.getDefault()));
            serviceBooked1.setStartTime(dateUtils.getdate());
            serviceBooked1.setEndTime(dateUtils.getdate());
            serviceBooked1.setService(new TblService(manualTokenDTO.getServiceId()));
            serviceBookedRepository.saveAndFlush(serviceBooked1);
            return serviceBooked1;
        } catch (Exception e) {
            logger.error("{}Excep:createNewTblAppointment:ManualTokenDTO:[{}],TblPatient:[{}],locationId:[{}],Error:{}", header, manualTokenDTO, patient, locationId, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
            return null;
        } finally {
            serviceBooked1 = null;
            appointment = null;
        }
    }

    @Transactional
    private ManualTokenResponse generateToken(String header, TblPatient patient, TblServiceBooked serviceBooked, ManualTokenDTO manualTokenDTO, Locale locale) {
        if (isLogEnabled) {
            logger.info("{}>>generateToken:TblServiceBooked:[{}]:ManualTokenDTO:[{}]", header, serviceBooked, manualTokenDTO);
        }
        ManualTokenResponse manualTokenResponse = null;
        try {
            TblBranch branch = branchRepository.findBrnachNameBybranchId(manualTokenDTO.getBranchId());
            TblService service = serviceRepository.getOne(manualTokenDTO.getServiceId());
            TblToken token = tokenRepository.getTokenByTranId(serviceBooked.getAppointment().getTranId());
            if (token == null) {
                token = new TblToken();
                token.setAppaitment(serviceBooked.getAppointment());
                token.setCreatedOn(dateUtils.getdate());
                token.setUpdatedOn(dateUtils.getdate());
                // if (branch.getBranchType().getBranchTypeCode().equalsIgnoreCase("CL")) {
                String branchTypeCode = branch.getBranchType().getBranchTypeCode();
                switch (branchTypeCode) {
                    case "CL":
                        if (branch.getIsRegistration() == 0 && branch.getIsVital() == 0) {
                            token.setStatus(TokenStatus.WAIT.getValue());
                        } else {
                            if (branch.getIsRegistration() == 1) {
                                token.setStatus(TokenStatus.RWAIT.getValue());
                            } else if (branch.getIsVital() == 1) {
                                token.setStatus(TokenStatus.VWAIT.getValue());
                            } else {
                                token.setStatus(TokenStatus.WAIT.getValue());
                            }
                        }
                        break;
                    case "PH":
                        if (branch.getIsRegistration() == 0) {
                            token.setStatus(TokenStatus.PHWAIT.getValue());
                        } else {
                            token.setStatus(TokenStatus.PRWAIT.getValue());
                        }
                        break;
                    case "LAB":
                        if (branch.getIsRegistration() == 0) {
                            token.setStatus(TokenStatus.LWAIT.getValue());
                        } else {
                            token.setStatus(TokenStatus.LRWAIT.getValue());
                        }
                        break;
                    case "RG":
                        if (branch.getIsRegistration() == 0) {
                            token.setStatus(TokenStatus.RGWAIT.getValue());
                        } else {
                            token.setStatus(TokenStatus.RRWAIT.getValue());
                        }
                        break;
                    case "NCL":
                        if (branch.getIsRegistration() == 0) {
                            token.setStatus(TokenStatus.NCWAIT.getValue());
                        } else {
                            token.setStatus(TokenStatus.NRWAIT.getValue());
                        }
                        break;

                }
                String tokenNo = "";
                if (patient.getPriority() == 0) {
                    tokenNo = service.getTokenPrefix() + getServiceTokenSequence(header, service);
                    token.setPriority((short) 0);
                } else {
                    tokenNo = "P" + service.getTokenPrefix() + getServiceTokenSequence(header, service);
                    token.setPriority((short) 1);
                }
                token.setNoShow(0);
                token.setTokenNo(tokenNo);
                tokenRepository.saveAndFlush(token);
                if (isLogEnabled) {
                    logger.info("{}>>generateToken:token:[{}]", header, token);
                }
                serviceBookedRepository.updateServiceBookedByTranId(serviceBooked.getServiceStatus(), serviceBooked.getAppointment().getTranId(), new ArrayList<String>() {
                    {
                        add(ApptStatus.SCHEDULED.getValue());
                    }
                });
                TblAppointment appointment = serviceBooked.getAppointment();
                appointment.setApptStatus(serviceBooked.getServiceStatus());
                apptRepository.saveAndFlush(appointment);
                CdrLog log = generateCdrLog(service.getBranchId(), serviceBooked.getStartTime(), serviceBooked);
                sendCdrReportToCdrQueue(header, log);
                if (isTriggerNotification) {
                    sendDataToQ2TriggerQueue(header, serviceBooked.getAppointment().getTranId(), "CheckIn", service.getServiceId());
                }
            } else {
                if (token.getStatus().equalsIgnoreCase(TokenStatus.FLOTING.getValue())) {

                    String branchTypeCode = branch.getBranchType().getBranchTypeCode();
                    switch (branchTypeCode) {
                        case "CL":
                            if (branch.getIsRegistration() == 0 && branch.getIsVital() == 0) {
                                token.setStatus(TokenStatus.WAIT.getValue());
                                token.setUpdatedOn(dateUtils.getdate());
                            } else {
                                if (branch.getIsRegistration() == 1) {
                                    token.setStatus(TokenStatus.RWAIT.getValue());
                                    token.setUpdatedOn(dateUtils.getdate());
                                } else if (branch.getIsVital() == 1) {
                                    token.setStatus(TokenStatus.VWAIT.getValue());
                                    token.setUpdatedOn(dateUtils.getdate());
                                } else {
                                    token.setStatus(TokenStatus.WAIT.getValue());
                                    token.setUpdatedOn(dateUtils.getdate());
                                }

//                        if (branch.getIsRegistration() == 1 && tblVisitDetailsRepo.findByTranIdAndStatus(serviceBooked.getAppointment().getTranId(), TokenStatus.RSRVD.getValue()) != null) {
//                            if (branch.getIsVital() == 1 && tblVisitDetailsRepo.findByTranIdAndStatus(serviceBooked.getAppointment().getTranId(), TokenStatus.VSRVD.getValue()) != null) {
//                                token.setStatus(TokenStatus.WAIT.getValue());
//                            } else if (branch.getIsVital() == 1) {
//                                token.setStatus(TokenStatus.VWAIT.getValue());
//                            } else {
//                                token.setStatus(TokenStatus.WAIT.getValue());
//                            }
//                        } else if (branch.getIsRegistration() == 1) {
//                            token.setStatus(TokenStatus.RWAIT.getValue());
//                        } else {
//                            if (branch.getIsVital() == 1 && tblVisitDetailsRepo.findByTranIdAndStatus(serviceBooked.getAppointment().getTranId(), TokenStatus.VSRVD.getValue()) != null) {
//                                token.setStatus(TokenStatus.WAIT.getValue());
//                            } else if (branch.getIsVital() == 1) {
//                                token.setStatus(TokenStatus.VWAIT.getValue());
//                            } else {
//                                token.setStatus(TokenStatus.WAIT.getValue());
//                            }
//                        }
                            }
                            break;
                        case "PH":
                            if (branch.getIsRegistration() == 0) {
                                token.setStatus(TokenStatus.PHWAIT.getValue());
                                token.setUpdatedOn(dateUtils.getdate());
                            } else {
                                token.setStatus(TokenStatus.PRWAIT.getValue());
                                token.setUpdatedOn(dateUtils.getdate());

                            }
                            break;
                        case "LAB":
                            if (branch.getIsRegistration() == 0) {
                                token.setStatus(TokenStatus.LWAIT.getValue());
                                token.setUpdatedOn(dateUtils.getdate());
                            } else {
                                token.setStatus(TokenStatus.LRWAIT.getValue());
                                token.setUpdatedOn(dateUtils.getdate());
                            }
                        case "RG":
                            if (branch.getIsRegistration() == 0) {
                                token.setStatus(TokenStatus.RGWAIT.getValue());
                                token.setUpdatedOn(dateUtils.getdate());
                            } else {
                                token.setStatus(TokenStatus.RRWAIT.getValue());
                                token.setUpdatedOn(dateUtils.getdate());
                            }
                    }

                } else {
                    //do nothing.
                }
            }
            String tokenTemplate = convertTokenTemplate(header, branch, token, serviceBooked.getStartTime());
            manualTokenResponse = new ManualTokenResponse();
            manualTokenResponse.setTokenId(token.getTokenId());
            manualTokenResponse.setTokenNo(token.getTokenNo());
            manualTokenResponse.setTokenTemplate(tokenTemplate);
            manualTokenResponse.setMrnNo(patient.getMrnNo());
            manualTokenResponse.setStatus(true);
            manualTokenResponse.setMessages(messageSource.getMessage("kiosktoken.crtl.succuss", null, locale));
            return manualTokenResponse;
        } catch (Exception e) {
            logger.error("{}Excep:generateToken:TblServiceBooked:[{}]:ManualTokenDTO:[{}]:Error:{}", header, serviceBooked, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
            manualTokenResponse = new ManualTokenResponse();
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("kiosktoken.crtl.fail", null, locale));
            return manualTokenResponse;
        } finally {
            manualTokenResponse = null;
        }
    }

    private CdrLog generateCdrLog(long branchId, Date checkInTime, TblServiceBooked servicBooked) {
        try {
            CdrLog log = new CdrLog();
            log.setBranchId(branchId);
            log.setCheckInTime(DateUtils.sdf.get().format(checkInTime));
            log.setStartTime(DateUtils.sdf.get().format(servicBooked.getStartTime()));
            log.setEndTime(DateUtils.sdf.get().format(servicBooked.getEndTime()));
            log.setDrId(servicBooked.getDrId() != null ? servicBooked.getDrId() : 0);
            log.setLocationId(servicBooked.getAppointment().getLocationId());
            log.setPatienId(servicBooked.getAppointment().getPatientId());
            log.setServiceId(servicBooked.getService().getServiceId());
            log.setTransId(servicBooked.getAppointment().getTranId());
            log.setStatus(TokenStatus.CHECKIN);
            return log;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendDataToQ2TriggerQueue(String header, String tranId, String triggerType, Long serviceId) {
        try {
            String cdr = mapper.writeValueAsString(new TriggerTypeDTO(tranId, triggerType, serviceId));
            jmsTemplate.convertAndSend(q2TriggerQueueName, cdr);
            if (isLogEnabled) {
                logger.info("{}Cdr:[{}]:Successfully:sendToQueue:[{}]", header, cdr, q2TriggerQueueName);
            }
        } catch (Exception e) {
            logger.error("{}Excep:sendDataToQ2TriggerQueue:tranId:{}:triggerType:{}:serviceId:{},Error:{}", header, tranId, triggerType, serviceId, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private void sendCdrReportToCdrQueue(String header, CdrLog log) {
        try {
            String cdr = mapper.writeValueAsString(log);
            jmsTemplate.convertAndSend(cdrQueueName, cdr);
            if (isLogEnabled) {
                logger.info("{}Cdr:[{}]:Successfully:sendToQueue:[{}]", header, cdr, cdrQueueName);
            }
        } catch (Exception e) {
            logger.error("{}Excep:updateAppointmentCheckInDone:cdrLog:{},Error:{}", header, log, ExceptionUtils.getRootCauseMessage(e));
        }
    }

//    @Transactional
    private int getServiceTokenSequence(String header, TblService service) {
        TblTokenSequence tokenSequence = null;
        int count = 1;
        try {
            if (isLogEnabled) {
                logger.info("{}>>service:[{}]", header, service);
            }
            tokenSequence = tokenSequenceRepositary.getTokenSequenceByServiceId(service.getServiceId());
            if (tokenSequence != null) {
                count = tokenSequence.getTokenSeq() + 1;
                tokenSequence.setTokenSeq(count);
            } else {
                tokenSequence = new TblTokenSequence();
                tokenSequence.setServiceId(service.getServiceId());
                tokenSequence.setTokenSeq(count);
            }
            tokenSequenceRepositary.saveAndFlush(tokenSequence);
            return count;
        } catch (Exception e) {
            logger.error("{}Excep:getServiceTokenSequence:Error:[{}]", ExceptionUtils.getRootCauseMessage(e));
            return 1;
        }
    }

    private String convertTokenTemplate(String header, TblBranch branch, TblToken token, Date startTimeDate) throws FileNotFoundException, IOException {
        try {
            if (isLogEnabled) {
                logger.info("{}>>convertTokenTemplate:service:[{}]:branch:[{}]:token:[{}]:startTimeDate:[{}]", header, branch, token, startTimeDate);
            }
            File file = ResourceUtils.getFile("config/templates/" + branch.getBranchType().getBranchTypeCode() + ".html");
            logger.info("{} || File Found :[{}] file :[{}]", header, file.exists(), file);
            String result = new String(Files.readAllBytes(file.toPath()));
            TokenPrintData tokenPrintData = new TokenPrintData();
            tokenPrintData.setAppointmentTime(dateUtils.getConvertTimeString(startTimeDate));
            tokenPrintData.setBranchNameEn(branch.getBranchNameEn());
            tokenPrintData.setBranchNameAr(branch.getBranchNameAr());
            tokenPrintData.setTokenNO(token.getTokenNo());
            tokenPrintData.setTimeNow(dateUtils.getCurrentTimeString());
            tokenPrintData.setDate(dateUtils.getCurrentDaString());
            String template = TokenTemplate.getTokenPrintingTemplate(tokenPrintData, result);
            if (isLogEnabled) {
                logger.info("{}>>convertTokenTemplate:template:[{}]", header, template);
            }
            return template;
        } catch (Exception e) {
            logger.error("{}Excep:convertTokenTemplate:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }

    public KioskThemeDto getThemeByKioskIdentifier(String header, String kioskIdentifier) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>getThemeByKioskIdentifier:kioskIdentifier:[{}]", header, kioskIdentifier);
            }
            return kioskRepository.getThemeByKioskIdentifi(kioskIdentifier);
        } catch (Exception e) {
            logger.error("{}Excep:getThemeByKioskIdentifier:kioskIdentifier:{}Error:[{}]", header, kioskIdentifier, ExceptionUtils.getRootCauseMessage(e));
            return new KioskThemeDto();
        }
    }

}

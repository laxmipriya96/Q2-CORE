/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2.model.TblAppointment;
import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblPatient;
import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.model.TblServiceBooked;
import com.vm.qsmart2.model.TblToken;
import com.vm.qsmart2.model.TblTokenSequence;
import com.vm.qsmart2.utils.ApptStatus;
import com.vm.qsmart2.utils.CdrLog;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2.utils.TokenPrintData;
import com.vm.qsmart2.utils.TokenStatus;
import com.vm.qsmart2.utils.TokenTemplate;
import com.vm.qsmart2.utils.TriggerTypeDTO;
import com.vm.qsmart2api.dtos.manualtoken.ManualTokenDTO;
import com.vm.qsmart2api.dtos.manualtoken.ManualTokenResponse;
import com.vm.qsmart2api.dtos.manualtoken.PatientServingDTO;
import com.vm.qsmart2api.dtos.manualtoken.PatientServingGDTO;
import com.vm.qsmart2api.repository.AppointmentRepository;
import com.vm.qsmart2api.repository.BranchRepository;
import com.vm.qsmart2api.repository.PatientRepository;
import com.vm.qsmart2api.repository.ServiceBookedRepository;
import com.vm.qsmart2api.repository.ServiceRepository;
import com.vm.qsmart2api.repository.TblVisitDetailsRepositary;
import com.vm.qsmart2api.repository.TokenRepository;
import com.vm.qsmart2api.repository.TokenSequenceRepositary;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

/**
 *
 * @author Ashok
 */
@Service
public class ManualTokenService {

    public static final Logger logger = LogManager.getLogger(ManualTokenService.class);

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DateUtils dateUtils;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ServiceBookedRepository serviceBookedRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    TokenSequenceRepositary tokenSequenceRepositary;

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    ObjectMapper mapper;

    @Value("${hl7.cdr.queue.name:Q2_HL7_CDR}")
    private String cdrQueueName;

    @Value("${trigger.notification.enabled:false}")
    private boolean isTriggerNotification;

    @Autowired
    MessageSource messageSource;

//    @Transactional
//    public ManualTokenResponse generateManualToken(String header, Long userId, Long locationId, ManualTokenDTO manualTokenDTO, Locale locale) {
//        if (isLogEnabled) {
//            logger.info("{}>>locationId:[{}]:ManualTokenDTO:[{}]", header, locationId, manualTokenDTO);
//        }
//        TblPatient patient = null;
//        try {
//            patient = patientRepository.getPatientByMrnNo(manualTokenDTO.getMrnNo());
//            if (patient != null) {
//                //do nothing
//            } else {
//                patient = new TblPatient();
//                patient.setFirstName(manualTokenDTO.getFirstName());
//                patient.setLastName(manualTokenDTO.getLastName());
//                patient.setMrnNo(manualTokenDTO.getMrnNo());
//                patient.setPriority((short) 0);
//                patient.setCreatedOn(dateUtils.getdate());
//                patient.setUpdatedOn(dateUtils.getdate());
//                patient = patientRepository.saveAndFlush(patient);
//                if (isLogEnabled) {
//                    logger.info("{}<<locationId:[{}]:TblPatient:[{}]", header, locationId, patient);
//                }
//            }
//            return saveAppointment(header, userId, patient, locationId, manualTokenDTO, locale);
//        } catch (Exception e) {
//            logger.error("{}Excep:generateManualToken:locationId:[{}]:ManualTokenDTO:[{}]:Error:{}", header, locationId, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
//            ManualTokenResponse manualTokenResponse = new ManualTokenResponse();
//            manualTokenResponse.setStatus(false);
//            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.fail", null, locale));
//            return manualTokenResponse;
//        } finally {
//            patient = null;
//        }
//    }
//    @Transactional
//    public ManualTokenResponse generateManualToken(String header, Long userId, Long locationId, ManualTokenDTO manualTokenDTO, Locale locale) {
//        if (isLogEnabled) {
//            logger.info("{}>>locationId:[{}]:ManualTokenDTO:[{}]", header, locationId, manualTokenDTO);
//        }
//        TblPatient patient = null;
//        try {
//            TblBranch branch = branchRepository.getBranchTypeCodeBybranchId(manualTokenDTO.getBranchId());
//            String branchTypeCode = branch.getBranchType().getBranchTypeCode();
//            switch (branchTypeCode) {
//                case "CL":
//                    patient = patientRepository.getPatientByMrnNo(manualTokenDTO.getMrnNo());
//                    if (patient != null) {
//                        //do nothing     
//                    } else {
//                        patient = new TblPatient();
//                        patient.setFirstName(manualTokenDTO.getFirstName());
//                        patient.setLastName(manualTokenDTO.getLastName());
//                        patient.setMrnNo(manualTokenDTO.getMrnNo());
//                        patient.setPriority((short) 0);
//                        patient.setCreatedOn(dateUtils.getdate());
//                        patient.setUpdatedOn(dateUtils.getdate());
//                        patient = patientRepository.saveAndFlush(patient);
//                        if (isLogEnabled) {
//                            logger.info("{}<<locationId:[{}]:TblPatient:[{}]", header, locationId, patient);
//                        }
//                    }
//                    break;
//                case "PH":
//                    patient = new TblPatient();
//                    patient.setFirstName(manualTokenDTO.getFirstName());
//                    patient.setLastName(manualTokenDTO.getLastName());
//                    patient.setMrnNo(manualTokenDTO.getMrnNo());
//                    patient.setPriority((short) 0);
//                    patient.setCreatedOn(dateUtils.getdate());
//                    patient.setUpdatedOn(dateUtils.getdate());
//                    patient = patientRepository.saveAndFlush(patient);
//                    if (isLogEnabled) {
//                        logger.info("{}<<locationId:[{}]:TblPatient:[{}]", header, locationId, patient);
//                    }
//                    break;
//                case "LAB":
//                    patient = new TblPatient();
//                    patient.setFirstName(manualTokenDTO.getFirstName());
//                    patient.setLastName(manualTokenDTO.getLastName());
//                    patient.setMrnNo(manualTokenDTO.getMrnNo());
//                    patient.setPriority((short) 0);
//                    patient.setCreatedOn(dateUtils.getdate());
//                    patient.setUpdatedOn(dateUtils.getdate());
//                    patient = patientRepository.saveAndFlush(patient);
//                    if (isLogEnabled) {
//                        logger.info("{}<<locationId:[{}]:TblPatient:[{}]", header, locationId, patient);
//                    }
//                    break;
//            }
//            return saveAppointment(header, userId, patient, locationId, manualTokenDTO, locale);
//        } catch (Exception e) {
//            logger.error("{}Excep:generateManualToken:locationId:[{}]:ManualTokenDTO:[{}]:Error:{}", header, locationId, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
//            ManualTokenResponse manualTokenResponse = new ManualTokenResponse();
//            manualTokenResponse.setStatus(false);
//            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.fail", null, locale));
//            return manualTokenResponse;
//        } finally {
//            patient = null;
//        }
//    }
    @Transactional
    public ManualTokenResponse generateManualToken(String header, Long userId, Long locationId, ManualTokenDTO manualTokenDTO, Locale locale) {
        if (isLogEnabled) {
            logger.info("{}>>locationId:[{}]:ManualTokenDTO:[{}]", header, locationId, manualTokenDTO);
        }
        TblPatient patient = null;
        try {
            patient = patientRepository.getPatientByMrnNo(manualTokenDTO.getMrnNo());
            if (patient != null) {
                //do nothing     
            } else {
                patient = new TblPatient();
                patient.setFirstName(manualTokenDTO.getFirstName());
                patient.setLastName(manualTokenDTO.getLastName());
                patient.setMrnNo(manualTokenDTO.getMrnNo());
                patient.setPriority((short) 0);
                patient.setCreatedOn(dateUtils.getdate());
                patient.setUpdatedOn(dateUtils.getdate());
                patient = patientRepository.saveAndFlush(patient);
                if (isLogEnabled) {
                    logger.info("{}<<locationId:[{}]:TblPatient:[{}]", header, locationId, patient);
                }
            }
            return saveAppointment(header, userId, patient, locationId, manualTokenDTO, locale);
        } catch (Exception e) {
            logger.error("{}Excep:generateManualToken:locationId:[{}]:ManualTokenDTO:[{}]:Error:{}", header, locationId, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
            ManualTokenResponse manualTokenResponse = new ManualTokenResponse();
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.fail", null, locale));
            return manualTokenResponse;
        } finally {
            patient = null;
        }
    }

    @Autowired
    ServiceBookedRepository serviceBookedRepositary;

    @Autowired
    AppointmentRepository apptRepository;

    @Transactional
    ManualTokenResponse saveAppointment(String header, Long userId, TblPatient patient, Long locationId, ManualTokenDTO manualTokenDTO, Locale locale) {
        if (isLogEnabled) {
            logger.info("{}>>TblPatient:[{}]:locationId:[{}]:ManualTokenDTO:[{}]", header, patient, locationId, manualTokenDTO);
        }
        Date fromDate = null;
        Date toDate = null;
        TblServiceBooked serviceBooked1 = null;
        try {
            fromDate = dateUtils.dayStartDateTime();
            toDate = dateUtils.dayEndDateTime();
            List<TblServiceBooked> servicBookedList = serviceBookedRepository.findServiceBookedByServiceBranchId(manualTokenDTO.getBranchId(), patient.getPatientId(), fromDate, toDate);
            boolean aptExist = servicBookedList.stream().anyMatch(obj -> obj.getService().getServiceId().equals(manualTokenDTO.getServiceId()));
            if (aptExist) {
                ManualTokenResponse manualTokenResponse = new ManualTokenResponse();
                manualTokenResponse.setStatus(false);
                manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.appt.exist", null, locale));
                return manualTokenResponse;
            } else {
                TblServiceBooked serviceBooked = (servicBookedList != null && !servicBookedList.isEmpty()) ? servicBookedList.get(0) : null;
                if (serviceBooked != null) {
                    TblAppointment appt = serviceBooked.getAppointment();
                    ApptStatus aptStatus = ApptStatus.getInstce(appt.getApptStatus());
                    switch (aptStatus) {
                        case FLOTING:
                            appt.setApptStatus(ApptStatus.MANUALCHECKIN.getValue());
                            appt.setUpdatedOn(dateUtils.getdate());
                            apptRepository.saveAndFlush(appt);
                            serviceBooked1 = groupingTheAppointment(header, appt, manualTokenDTO);
                            break;
                        case NURSECHECKOUT:
                        case HL7CHECKOUT:
                        case FORCECHECKOUT:
                        case CANCELLED:
                            serviceBooked1 = createNewTblAppointment(header, manualTokenDTO, patient, userId, locationId);
                            break;
                        default:
                            serviceBooked1 = groupingTheAppointment(header, appt, manualTokenDTO);
                            break;
                    }
                } else {
                    serviceBooked1 = createNewTblAppointment(header, manualTokenDTO, patient, userId, locationId);
                }
                if (isLogEnabled) {
                    logger.info("{}<<saveAppointment:TblPatient:[{}]:locationId:[{}]:ManualTokenDTO:[{}]", header, patient, locationId, manualTokenDTO);
                }
                return generateToken(header, patient, serviceBooked1, manualTokenDTO, locale);
            }
        } catch (Exception e) {
            logger.error("{}Excep:saveAppointment:TblPatient:[{}]:locationId:[{}]:ManualTokenDTO:[{}]:Error:{}", header, patient, locationId, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
            ManualTokenResponse manualTokenResponse = new ManualTokenResponse();
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.fail", null, locale));
            return manualTokenResponse;
        } finally {
            fromDate = null;
            toDate = null;
            serviceBooked1 = null;
        }
    }

    public TblServiceBooked createNewTblAppointment(String header, ManualTokenDTO manualTokenDTO, TblPatient patient, long userId, long locationId) {
        TblServiceBooked serviceBooked1 = null;
        TblAppointment appointment = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>ManualTokenDTO:[{}],TblPatient:[{}],UserId:[{}],locationId:[{}]", header, manualTokenDTO, patient, userId, locationId);
            }
            appointment = new TblAppointment();
            appointment.setPatientId(patient.getPatientId());
            appointment.setTranId(dateUtils.generateUniqueTranId());
            appointment.setClerkId(userId);
            appointment.setCreatedOn(dateUtils.getdate());
            appointment.setUpdatedOn(dateUtils.getdate());
            appointment.setStartTime(dateUtils.getdate());
            appointment.setApptStatus(ApptStatus.MANUALCHECKIN.getValue());
            appointment.setLocationId(locationId);
            appointmentRepository.saveAndFlush(appointment);

            serviceBooked1 = new TblServiceBooked();
            serviceBooked1.setAppointment(appointment);
            if (manualTokenDTO.getDrId() > 0) {
                serviceBooked1.setDrId(manualTokenDTO.getDrId());
            }
            serviceBooked1.setServiceStatus(ApptStatus.MANUALCHECKIN.getValue());
            serviceBooked1.setEncounterId(RandomStringUtils.randomAlphanumeric(8).toUpperCase(Locale.getDefault()));
            serviceBooked1.setStartTime(dateUtils.getdate());
            serviceBooked1.setEndTime(dateUtils.getdate());
            serviceBooked1.setService(new TblService(manualTokenDTO.getServiceId()));
            serviceBookedRepository.saveAndFlush(serviceBooked1);
            return serviceBooked1;
        } catch (Exception e) {
            logger.error("{}Excep:createNewTblAppointment:ManualTokenDTO:[{}],TblPatient:[{}],userId:[{}],locationId:[{}],Error:{}", header, manualTokenDTO, patient, userId, locationId, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
            return null;
        } finally {
            serviceBooked1 = null;
            appointment = null;
        }

    }

    public TblServiceBooked groupingTheAppointment(String header, TblAppointment appt, ManualTokenDTO manualTokenDTO) {
        TblServiceBooked serviceBooked1 = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>TblAppointment:[{}]:ManualTokenDTO:[{}]", header, appt, manualTokenDTO);
            }
            serviceBooked1 = new TblServiceBooked();
            serviceBooked1.setAppointment(appt);
            if (manualTokenDTO.getDrId() > 0) {
                serviceBooked1.setDrId(manualTokenDTO.getDrId());
            }
            serviceBooked1.setEncounterId(RandomStringUtils.randomAlphanumeric(8).toUpperCase(Locale.getDefault()));
            serviceBooked1.setStartTime(dateUtils.getdate());
            serviceBooked1.setEndTime(dateUtils.getdate());
            serviceBooked1.setService(new TblService(manualTokenDTO.getServiceId()));
            serviceBooked1.setServiceStatus(ApptStatus.MANUALCHECKIN.getValue());
            serviceBookedRepository.saveAndFlush(serviceBooked1);
            return serviceBooked1;
        } catch (Exception e) {
            logger.error("{}Excep:groupingTheAppointment:TblAppointment:[{}]:ManualTokenDTO:[{}]:Error:{}", header, appt, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
            return null;
        } finally {
            serviceBooked1 = null;
        }
    }
    @Autowired
    TblVisitDetailsRepositary tblVisitDetailsRepo;

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
                serviceBookedRepositary.updateServiceBookedByTranId(serviceBooked.getServiceStatus(), serviceBooked.getAppointment().getTranId(), new ArrayList<String>() {
                    {
                        add(ApptStatus.SCHEDULED.getValue());
                    }
                });
                TblAppointment appointment = serviceBooked.getAppointment();
                appointment.setApptStatus(serviceBooked.getServiceStatus());
                appointmentRepository.saveAndFlush(appointment);
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
                        case "NCL":
                            if (branch.getIsRegistration() == 0) {
                                token.setStatus(TokenStatus.NCWAIT.getValue());
                                token.setUpdatedOn(dateUtils.getdate());
                            } else {
                                token.setStatus(TokenStatus.NRWAIT.getValue());
                                token.setUpdatedOn(dateUtils.getdate());
                            }
                    }
                    if (isTriggerNotification) {
                        sendDataToQ2TriggerQueue(header, serviceBooked.getAppointment().getTranId(), "CheckIn", service.getServiceId());
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
            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.succuss", null, locale));
            return manualTokenResponse;
        } catch (Exception e) {
            logger.error("{}Excep:generateToken:TblServiceBooked:[{}]:ManualTokenDTO:[{}]:Error:{}", header, serviceBooked, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
            manualTokenResponse = new ManualTokenResponse();
            manualTokenResponse.setStatus(false);
            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.fail", null, locale));
            return manualTokenResponse;
        } finally {
            manualTokenResponse = null;
        }
    }

    @Value("${qsmart.trigger.queue.name:Q2_TRIGGER}")
    private String q2TriggerQueueName;

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

//    @Transactional
//    private ManualTokenResponse generateToken(String header, TblPatient patient, TblServiceBooked serviceBooked, ManualTokenDTO manualTokenDTO, Locale locale) {
//        if (isLogEnabled) {
//            logger.info("{}>>generateToken:TblServiceBooked:[{}]:ManualTokenDTO:[{}]", header, serviceBooked, manualTokenDTO);
//        }
//        ManualTokenResponse manualTokenResponse = null;
//        try {
//            TblBranch branch = branchRepository.findBrnachNameBybranchId(manualTokenDTO.getBranchId());
//            TblService service = serviceRepository.getOne(manualTokenDTO.getServiceId());
//            TblToken token = tokenRepository.getTokenByTranId(serviceBooked.getAppointment().getTranId());
//            if (token == null) {
//                token = new TblToken();
//                token.setAppaitment(serviceBooked.getAppointment());
//                token.setCreatedOn(dateUtils.getdate());
//                token.setUpdatedOn(dateUtils.getdate());
//
//                // if (branch.getBranchType().getBranchTypeCode().equalsIgnoreCase("CL")) {
//                String branchTypeCode = branch.getBranchType().getBranchTypeCode();
//                switch (branchTypeCode) {
//                    case "CL":
//                        if (branch.getIsRegistration() == 0 && branch.getIsVital() == 0) {
//                            token.setStatus(TokenStatus.WAIT.getValue());
//                        } else {
//                            if (branch.getIsRegistration() == 1) {
//                                token.setStatus(TokenStatus.RWAIT.getValue());
//                            } else if (branch.getIsVital() == 1) {
//                                token.setStatus(TokenStatus.VWAIT.getValue());
//                            } else {
//                                token.setStatus(TokenStatus.WAIT.getValue());
//                            }
//                        }
//                        break;
//                    case "PH":
//                        if (branch.getIsRegistration() == 0) {
//                            token.setStatus(TokenStatus.PHWAIT.getValue());
//                        } else {
//                            token.setStatus(TokenStatus.PRWAIT.getValue());
//                        }
//                        break;
//                    case "LAB":
//                        if (branch.getIsRegistration() == 0) {
//                            token.setStatus(TokenStatus.LWAIT.getValue());
//                        } else {
//                            token.setStatus(TokenStatus.LRWAIT.getValue());
//                        }
//                        break;
//                }
//                String tokenNo = "";
//                if (patient.getPriority() == 0) {
//                    tokenNo = service.getTokenPrefix() + getServiceTokenSequence(header, service);
//                    token.setPriority((short) 0);
//                } else {
//                    tokenNo = "P" + service.getTokenPrefix() + getServiceTokenSequence(header, service);
//                    token.setPriority((short) 1);
//                }
//                token.setNoShow(0);
//                token.setTokenNo(tokenNo);
//                tokenRepository.saveAndFlush(token);
//                if (isLogEnabled) {
//                    logger.info("{}>>generateToken:token:[{}]", header, token);
//                }
//                serviceBookedRepositary.updateServiceBookedByTranId(serviceBooked.getServiceStatus(), serviceBooked.getAppointment().getTranId(), new ArrayList<String>() {
//                    {
//                        add(ApptStatus.SCHEDULED.getValue());
//                    }
//                });
//                TblAppointment appointment = serviceBooked.getAppointment();
//                appointment.setApptStatus(serviceBooked.getServiceStatus());
//                appointmentRepository.saveAndFlush(appointment);
//                CdrLog log = generateCdrLog(service.getBranchId(), serviceBooked.getStartTime(), serviceBooked);
//                sendCdrReportToCdrQueue(header, log);
//            } else {
//                if (token.getStatus().equalsIgnoreCase(TokenStatus.FLOTING.getValue())) {
//
//                    String branchTypeCode = branch.getBranchType().getBranchTypeCode();
//                    switch (branchTypeCode) {
//                        case "CL":
//                            if (branch.getIsRegistration() == 0 && branch.getIsVital() == 0) {
//                                token.setStatus(TokenStatus.WAIT.getValue());
//                                token.setUpdatedOn(dateUtils.getdate());
//                            } else {
//                                if (branch.getIsRegistration() == 1) {
//                                    token.setStatus(TokenStatus.RWAIT.getValue());
//                                    token.setUpdatedOn(dateUtils.getdate());
//                                } else if (branch.getIsVital() == 1) {
//                                    token.setStatus(TokenStatus.VWAIT.getValue());
//                                    token.setUpdatedOn(dateUtils.getdate());
//                                } else {
//                                    token.setStatus(TokenStatus.WAIT.getValue());
//                                    token.setUpdatedOn(dateUtils.getdate());
//                                }
//
////                        if (branch.getIsRegistration() == 1 && tblVisitDetailsRepo.findByTranIdAndStatus(serviceBooked.getAppointment().getTranId(), TokenStatus.RSRVD.getValue()) != null) {
////                            if (branch.getIsVital() == 1 && tblVisitDetailsRepo.findByTranIdAndStatus(serviceBooked.getAppointment().getTranId(), TokenStatus.VSRVD.getValue()) != null) {
////                                token.setStatus(TokenStatus.WAIT.getValue());
////                            } else if (branch.getIsVital() == 1) {
////                                token.setStatus(TokenStatus.VWAIT.getValue());
////                            } else {
////                                token.setStatus(TokenStatus.WAIT.getValue());
////                            }
////                        } else if (branch.getIsRegistration() == 1) {
////                            token.setStatus(TokenStatus.RWAIT.getValue());
////                        } else {
////                            if (branch.getIsVital() == 1 && tblVisitDetailsRepo.findByTranIdAndStatus(serviceBooked.getAppointment().getTranId(), TokenStatus.VSRVD.getValue()) != null) {
////                                token.setStatus(TokenStatus.WAIT.getValue());
////                            } else if (branch.getIsVital() == 1) {
////                                token.setStatus(TokenStatus.VWAIT.getValue());
////                            } else {
////                                token.setStatus(TokenStatus.WAIT.getValue());
////                            }
////                        }
//                            }
//                            break;
//                        case "PH":
//                            if (branch.getIsRegistration() == 0) {
//                                token.setStatus(TokenStatus.PHWAIT.getValue());
//                                token.setUpdatedOn(dateUtils.getdate());
//                            } else {
//                                token.setStatus(TokenStatus.PRWAIT.getValue());
//                                token.setUpdatedOn(dateUtils.getdate());
//
//                            }
//                            break;
//                        case "LAB":
//                            if (branch.getIsRegistration() == 0) {
//                                token.setStatus(TokenStatus.LWAIT.getValue());
//                                token.setUpdatedOn(dateUtils.getdate());
//                            } else {
//                                token.setStatus(TokenStatus.LRWAIT.getValue());
//                                token.setUpdatedOn(dateUtils.getdate());
//                            }
//                    }
//
//                } else {
//                    //do nothing.
//                }
//            }
//            String tokenTemplate = convertTokenTemplate(header, branch, token, serviceBooked.getStartTime());
//            manualTokenResponse = new ManualTokenResponse();
//            manualTokenResponse.setTokenId(token.getTokenId());
//            manualTokenResponse.setTokenTemplate(tokenTemplate);
//            manualTokenResponse.setMrnNo(patient.getMrnNo());
//            manualTokenResponse.setStatus(true);
//            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.succuss", null, locale));
//            return manualTokenResponse;
//        } catch (Exception e) {
//            logger.error("{}Excep:generateToken:TblServiceBooked:[{}]:ManualTokenDTO:[{}]:Error:{}", header, serviceBooked, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
//            manualTokenResponse = new ManualTokenResponse();
//            manualTokenResponse.setStatus(false);
//            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.fail", null, locale));
//            return manualTokenResponse;
//        } finally {
//            manualTokenResponse = null;
//        }
//
//    }
//    @Transactional
//    private ManualTokenResponse generateToken(String header, TblPatient patient, TblServiceBooked serviceBooked, ManualTokenDTO manualTokenDTO, Locale locale) {
//        if (isLogEnabled) {
//            logger.info("{}>>generateToken:TblServiceBooked:[{}]:ManualTokenDTO:[{}]", header, serviceBooked, manualTokenDTO);
//        }
//        ManualTokenResponse manualTokenResponse = null;
//        try {
//            TblBranch branch = branchRepository.findBrnachNameBybranchId(manualTokenDTO.getBranchId());
//            TblService service = serviceRepository.getOne(manualTokenDTO.getServiceId());
//            TblToken token = tokenRepository.getTokenByTranId(serviceBooked.getAppointment().getTranId());
//            if (token == null) {
//                token = new TblToken();
//                token.setAppaitment(serviceBooked.getAppointment());
//                token.setCreatedOn(dateUtils.getdate());
//                token.setUpdatedOn(dateUtils.getdate());
//                if (branch.getIsRegistration() == 0 && branch.getIsVital() == 0) {
//                    token.setStatus(TokenStatus.WAIT.getValue());
//                } else {
//                    if (branch.getIsRegistration() == 1) {
//                        token.setStatus(TokenStatus.RWAIT.getValue());
//                    } else if (branch.getIsVital() == 1) {
//                        token.setStatus(TokenStatus.VWAIT.getValue());
//                    } else {
//                        token.setStatus(TokenStatus.WAIT.getValue());
//                    }
//                }
//                String tokenNo = "";
//                if (patient.getPriority() == 0) {
//                    tokenNo = service.getTokenPrefix() + getServiceTokenSequence(header, service);
//                    token.setPriority((short) 0);
//                } else {
//                    tokenNo = "P" + service.getTokenPrefix() + getServiceTokenSequence(header, service);
//                    token.setPriority((short) 1);
//                }
//                token.setNoShow(0);
//                token.setTokenNo(tokenNo);
//                tokenRepository.saveAndFlush(token);
//                if (isLogEnabled) {
//                    logger.info("{}>>generateToken:token:[{}]", header, token);
//                }
//                serviceBookedRepositary.updateServiceBookedByTranId(serviceBooked.getServiceStatus(), serviceBooked.getAppointment().getTranId(), new ArrayList<String>() {
//                    {
//                        add(ApptStatus.SCHEDULED.getValue());
//                    }
//                });
//                TblAppointment appointment = serviceBooked.getAppointment();
//                appointment.setApptStatus(serviceBooked.getServiceStatus());
//                appointmentRepository.saveAndFlush(appointment);
//                CdrLog log = generateCdrLog(service.getBranchId(), serviceBooked.getStartTime(), serviceBooked);
//                sendCdrReportToCdrQueue(header, log);
//            } else {
//                if (token.getStatus().equalsIgnoreCase(TokenStatus.FLOTING.getValue())) {
//                    if (branch.getIsRegistration() == 0 && branch.getIsVital() == 0) {
//                        token.setStatus(TokenStatus.WAIT.getValue());
//                        token.setUpdatedOn(dateUtils.getdate());
//                    } else {
//                        if (branch.getIsRegistration() == 1) {
//                            token.setStatus(TokenStatus.RWAIT.getValue());
//                            token.setUpdatedOn(dateUtils.getdate());
//                        } else if (branch.getIsVital() == 1) {
//                            token.setStatus(TokenStatus.VWAIT.getValue());
//                            token.setUpdatedOn(dateUtils.getdate());
//                        } else {
//                            token.setStatus(TokenStatus.WAIT.getValue());
//                            token.setUpdatedOn(dateUtils.getdate());
//                        }
////                        if (branch.getIsRegistration() == 1 && tblVisitDetailsRepo.findByTranIdAndStatus(serviceBooked.getAppointment().getTranId(), TokenStatus.RSRVD.getValue()) != null) {
////                            if (branch.getIsVital() == 1 && tblVisitDetailsRepo.findByTranIdAndStatus(serviceBooked.getAppointment().getTranId(), TokenStatus.VSRVD.getValue()) != null) {
////                                token.setStatus(TokenStatus.WAIT.getValue());
////                            } else if (branch.getIsVital() == 1) {
////                                token.setStatus(TokenStatus.VWAIT.getValue());
////                            } else {
////                                token.setStatus(TokenStatus.WAIT.getValue());
////                            }
////                        } else if (branch.getIsRegistration() == 1) {
////                            token.setStatus(TokenStatus.RWAIT.getValue());
////                        } else {
////                            if (branch.getIsVital() == 1 && tblVisitDetailsRepo.findByTranIdAndStatus(serviceBooked.getAppointment().getTranId(), TokenStatus.VSRVD.getValue()) != null) {
////                                token.setStatus(TokenStatus.WAIT.getValue());
////                            } else if (branch.getIsVital() == 1) {
////                                token.setStatus(TokenStatus.VWAIT.getValue());
////                            } else {
////                                token.setStatus(TokenStatus.WAIT.getValue());
////                            }
////                        }
//                    }
//                } else {
//                    //do nothing.
//                }
//            }
//            String tokenTemplate = convertTokenTemplate(header, branch, token, serviceBooked.getStartTime());
//            manualTokenResponse = new ManualTokenResponse();
//            manualTokenResponse.setTokenId(token.getTokenId());
//            manualTokenResponse.setTokenTemplate(tokenTemplate);
//            manualTokenResponse.setMrnNo(patient.getMrnNo());
//            manualTokenResponse.setStatus(true);
//            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.succuss", null, locale));
//            return manualTokenResponse;
//        } catch (Exception e) {
//            logger.error("{}Excep:generateToken:TblServiceBooked:[{}]:ManualTokenDTO:[{}]:Error:{}", header, serviceBooked, manualTokenDTO, ExceptionUtils.getRootCauseMessage(e));
//            manualTokenResponse = new ManualTokenResponse();
//            manualTokenResponse.setStatus(false);
//            manualTokenResponse.setMessages(messageSource.getMessage("manualtoken.crtl.fail", null, locale));
//            return manualTokenResponse;
//        } finally {
//            manualTokenResponse = null;
//        }
//
//    }
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

    @Transactional
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

    public PatientServingGDTO searchMrnNo(String header, Long userId, String mrnNo) {
        if (isLogEnabled) {
            logger.info("{}>>searchMrnNo:mrnNo:[{}]", header, mrnNo);
        }
        try {
            List<String> status = new ArrayList<>();
            status.add(ApptStatus.SCHEDULED.getValue());
            status.add(ApptStatus.HL7CHECKIN.getValue());
            status.add(ApptStatus.KIOSKCHECKIN.getValue());
            status.add(ApptStatus.MANUALCHECKIN.getValue());
            status.add(ApptStatus.WAITING.getValue());
            Date fromDate = dateUtils.dayStartDateTime();
            Date toDate = dateUtils.dayEndDateTime();
            List<PatientServingDTO> patientServingDTOs = patientRepository.getServiceBookedsByMrnNo(mrnNo, status, fromDate, toDate);
//            List<PatientServingDTO> patientServingDTOs = patientRepository.getServiceBookedsByMrnNo(mrnNo, status);
            return new PatientServingGDTO(patientServingDTOs);
        } catch (Exception e) {
            logger.info("{}>>searchMrnNo:mrnNo:[{}]", header, mrnNo, ExceptionUtils.getRootCauseMessage(e));
            return new PatientServingGDTO();
        }
    }

    public ManualTokenResponse checkInByServiceBookedId(String header, Long userId, Long serviceBookedId, Locale locale) {
        if (isLogEnabled) {
            logger.info("{}>>checkInByServiceBookedId:serviceBookedId:[{}]", header, serviceBookedId);
        }
        ManualTokenDTO manualTokenDTO = null;
        ManualTokenResponse manualTokenResponse = null;
        TblServiceBooked serviceBooked = null;
        TblToken token = null;
        TblPatient patient = null;
        TblAppointment appt = null;
        try {
            serviceBooked = serviceBookedRepository.findTblServiceBookedByServiceBookedId(serviceBookedId);
            //token = tokenRepository.getTokenByTranId(serviceBooked.getAppointment().getTranId());
            patient = patientRepository.getOne(serviceBooked.getAppointment().getPatientId());
//            if (token == null) {
            manualTokenDTO = new ManualTokenDTO();
            manualTokenDTO.setAppointmentTime(dateUtils.convertDateToDtString(serviceBooked.getStartTime()));
            manualTokenDTO.setBranchId(serviceBooked.getService().getBranchId());
            manualTokenDTO.setServiceId(serviceBooked.getService().getServiceId());
            serviceBooked.setServiceStatus(ApptStatus.MANUALCHECKIN.getValue());
            appt = serviceBooked.getAppointment();
            appt.setApptStatus(ApptStatus.MANUALCHECKIN.getValue());
            appointmentRepository.saveAndFlush(appt);
            serviceBookedRepository.saveAndFlush(serviceBooked);
            return generateToken(header, patient, serviceBooked, manualTokenDTO, locale);
//            } else {
//                TblBranch branch = branchRepository.getOne(serviceBooked.getService().getBranchId());
//                String tokenTemplate = convertTokenTemplate(header, branch, token, serviceBooked.getStartTime());
//                manualTokenResponse = new ManualTokenResponse();
//                manualTokenResponse.setTokenId(token.getTokenId());
//                manualTokenResponse.setTokenTemplate(tokenTemplate);
//                manualTokenResponse.setMrnNo(patient.getMrnNo());
//                return manualTokenResponse;
//            }
        } catch (Exception e) {
            logger.error("{}Excep:checkInByServiceBookedId:serviceBookedId:[{}]:Error:[{}]", header, serviceBookedId, ExceptionUtils.getRootCauseMessage(e));
            return null;
        } finally {
            manualTokenDTO = null;
            manualTokenResponse = null;
            serviceBooked = null;
            token = null;
            patient = null;
        }
    }

    public ManualTokenResponse rePrintTokenByServiceId(String header, Long userId, Long serviceBookedId) {
        if (isLogEnabled) {
            logger.info("{}>>rePrintTokenByServiceId:serviceBookedId:[{}]", header, serviceBookedId);
        }
        try {
            TblServiceBooked serviceBooked = serviceBookedRepository.getOne(serviceBookedId);
            TblToken token = tokenRepository.getTokenByTranId(serviceBooked.getAppointment().getTranId());
            TblBranch branch = branchRepository.getOne(serviceBooked.getService().getBranchId());
            String tokenTemplate = convertTokenTemplate(header, branch, token, serviceBooked.getStartTime());
            ManualTokenResponse manualTokenResponse = new ManualTokenResponse();
            manualTokenResponse.setTokenId(token.getTokenId());
            manualTokenResponse.setTokenTemplate(tokenTemplate);
            manualTokenResponse.setMrnNo(patientRepository.getOne(serviceBooked.getAppointment().getPatientId()).getMrnNo());
            return manualTokenResponse;
        } catch (Exception e) {
            logger.error("{}Excep:rePrintTokenByServiceId:serviceBookedId:[{}]:Error:[{}]", header, serviceBookedId, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }

    }

    @Transactional
    public ManualTokenResponse checkInByServiceBookedIdByKiosk(String header, Long serviceBookedId, Locale locale) {
        if (isLogEnabled) {
            logger.info("{}>>checkInByServiceBookedId:serviceBookedId:[{}]", header, serviceBookedId);
        }
        ManualTokenDTO manualTokenDTO = null;
        TblServiceBooked serviceBooked = null;
        TblPatient patient = null;
        TblAppointment appt = null;
        try {
            serviceBooked = serviceBookedRepository.findTblServiceBookedByServiceBookedId(serviceBookedId);
            //token = tokenRepository.getTokenByTranId(serviceBooked.getAppointment().getTranId());
            List<String> serviceStatus = new ArrayList();
            serviceStatus.add(ApptStatus.SCHEDULED.getValue());
            List<TblServiceBooked> serviceBookedList = serviceBookedRepository.findServiceBookedByTransId(serviceBooked.getAppointment().getTranId(), serviceBooked.getService().getServiceId(), serviceStatus);
            patient = patientRepository.getOne(serviceBooked.getAppointment().getPatientId());
//            if (token == null) {
            manualTokenDTO = new ManualTokenDTO();
            manualTokenDTO.setAppointmentTime(dateUtils.convertDateToDtString(serviceBooked.getStartTime()));
            manualTokenDTO.setBranchId(serviceBooked.getService().getBranchId());
            manualTokenDTO.setServiceId(serviceBooked.getService().getServiceId());
            serviceBooked.setServiceStatus(ApptStatus.KIOSKCHECKIN.getValue());
            appt = serviceBooked.getAppointment();
            appt.setApptStatus(ApptStatus.KIOSKCHECKIN.getValue());
            appointmentRepository.saveAndFlush(appt);
            serviceBookedRepository.saveAndFlush(serviceBooked);
            for (TblServiceBooked serviceBooked1 : serviceBookedList) {
                serviceBooked1.setServiceStatus(ApptStatus.KIOSKCHECKIN.getValue());
                serviceBookedRepository.saveAndFlush(serviceBooked1);
            }
            return generateToken(header, patient, serviceBooked, manualTokenDTO, locale);
        } catch (Exception e) {
            logger.error("{}Excep:checkInByServiceBookedIdByKiosk:serviceBookedId:[{}]:Error:[{}]", header, serviceBookedId, ExceptionUtils.getRootCauseMessage(e));
            return null;
        } finally {
            manualTokenDTO = null;
            serviceBooked = null;
            patient = null;
        }
    }
}

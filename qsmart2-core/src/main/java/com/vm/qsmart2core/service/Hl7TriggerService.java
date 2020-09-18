/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2.model.TblAppointment;
import com.vm.qsmart2.model.TblApptType;
import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblEnterprise;
import com.vm.qsmart2.model.TblHl7Cdr;
import com.vm.qsmart2.model.TblLocation;
import com.vm.qsmart2.model.TblPatient;
import com.vm.qsmart2.model.TblRole;
import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.model.TblServiceBooked;
import com.vm.qsmart2.model.TblToken;
import com.vm.qsmart2.model.TblTokenSequence;
import com.vm.qsmart2.model.TblUser;
import com.vm.qsmart2.utils.ApptStatus;
import com.vm.qsmart2.utils.CdrLog;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2.utils.Hider;
import com.vm.qsmart2.utils.Hl7MsgTrigger;
import com.vm.qsmart2.utils.TokenStatus;
import com.vm.qsmart2.utils.TriggerType;
import com.vm.qsmart2.utils.TriggerTypeDTO;
import com.vm.qsmart2core.repositary.AppointmentRepositary;
import com.vm.qsmart2core.repositary.ApptTypeRepository;
import com.vm.qsmart2core.repositary.BranchRepository;
import com.vm.qsmart2core.repositary.Hl7CdrRepositary;
import com.vm.qsmart2core.repositary.PatientRepositary;
import com.vm.qsmart2core.repositary.RoleRepository;
import com.vm.qsmart2core.repositary.ServiceBookedRepositary;
import com.vm.qsmart2core.repositary.ServiceRepository;
import com.vm.qsmart2core.repositary.TokenRepositary;
import com.vm.qsmart2core.repositary.UserRepository;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.vm.qsmart2core.repositary.TokenSequenceRepositary;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Phani
 */
@Service
public class Hl7TriggerService {

    private static final Logger logger = LogManager.getLogger(Hl7TriggerService.class);

    @Autowired
    Hl7CdrRepositary hl7CdrRepo;

    @Autowired
    BranchRepository branchRepo;

    @Autowired
    ApptTypeRepository aptTypeRepo;

    @Autowired
    AppointmentRepositary aptRepo;

    @Autowired
    DateUtils dateUtils;

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    @Autowired
    ServiceBookedRepositary serviceBookedRepo;

    @Autowired
    ServiceRepository serviceRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${hl7.cdr.queue.name:Q2_HL7_CDR}")
    private String cdrQueueName;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    TokenRepositary tokenRepositary;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    PatientRepositary patientRepo;

    public Hl7TriggerService() {
    }

    public Hl7TriggerService(String cdrQueueName, JmsTemplate jmsTemplate, ObjectMapper mapper) {
        this.cdrQueueName = cdrQueueName;
        this.jmsTemplate = jmsTemplate;
        this.mapper = mapper;
    }

    public boolean hl7TriggerProcess(String header, Hl7MsgTrigger hl7Trigger) {
        try {
            switch (hl7Trigger.getHl7MsgType()) {
                case S12:
                    logger.info("S12");
                    processS12Message("[SIU-S12] ", hl7Trigger);
                    break;
                case S13:
                    logger.info("S13");
                    processS13Message("[SIU-S13] ", hl7Trigger);
                    break;
                case S15:
                    logger.info("S15");
                    processS15Message("[SIU-S15] ", hl7Trigger);
                    break;
                case Z01:
                    isItCheckinOrCheckoutMsg(header, hl7Trigger);
                    break;
                case A05:
                    processA05Message("[ADT-A05] ", hl7Trigger);
                    break;
                case A03:
                    logger.info("<--------NotImplemented-------->");
                    break;
                default:
                    logger.info("<--------NotImplemented-------->");
                    break;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void processA05Message(String header, Hl7MsgTrigger hl7Trigger) {
        TblHl7Cdr tblHl7Cdr = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>TrgigerData:[{}]", header, hl7Trigger.toString());
            }
            Optional<TblHl7Cdr> optionalCdr = hl7CdrRepo.findById(hl7Trigger.getHl7CdrId());
            if (optionalCdr.isPresent()) {
                tblHl7Cdr = optionalCdr.get();
                TblPatient patient = patientRepo.findPatientByMrn(tblHl7Cdr.getMrnNo());
                if (patient != null) {
                    String priorityFlag = tblHl7Cdr.getPriorityFlag();
                    if (priorityFlag != null && !priorityFlag.isEmpty()) {
                        if (priorityFlag.equalsIgnoreCase("")) {
                            patient.setPriority((short) 1);
                            patient.setUpdatedOn(dateUtils.getdate());
                            patientRepo.saveAndFlush(patient);
                            tblHl7Cdr.setRemarks("Patient priority updated successfully");
                            tblHl7Cdr.setStatus(1);
                            updateHl7CdrStatus(tblHl7Cdr);
                        } else {
                            tblHl7Cdr.setRemarks("Priority value Not valid");
                            tblHl7Cdr.setStatus(0);
                            updateHl7CdrStatus(tblHl7Cdr);
                        }
                    } else {
                        tblHl7Cdr.setRemarks("Priority value was emtpy or null");
                        tblHl7Cdr.setStatus(0);
                        updateHl7CdrStatus(tblHl7Cdr);
                    }
                } else {
                    tblHl7Cdr.setRemarks("Patient Info Not Found");
                    tblHl7Cdr.setStatus(0);
                    updateHl7CdrStatus(tblHl7Cdr);
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}<<:hl7cdr:not:found:hl7cdrId:[{}]", header, hl7Trigger.getHl7CdrId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:processA05Message:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            tblHl7Cdr = null;
        }
    }

    private void processS15Message(String header, Hl7MsgTrigger hl7Trigger) {
        TblHl7Cdr tblHl7Cdr = null;
        TblServiceBooked serviceBooked = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>TrgigerData:[{}]", header, hl7Trigger.toString());
            }
            Optional<TblHl7Cdr> optionalCdr = hl7CdrRepo.findById(hl7Trigger.getHl7CdrId());
            if (optionalCdr.isPresent()) {
                tblHl7Cdr = optionalCdr.get();
                serviceBooked = serviceBookedRepo.findServiceBookedByServiceEncounterIdAndLocationCode(tblHl7Cdr.getEncounterId(), tblHl7Cdr.getLocationCode());
                if (serviceBooked != null) {
                    if (ApptStatus.getInstce(serviceBooked.getServiceStatus()) == ApptStatus.SCHEDULED) {
                        serviceBooked.setServiceStatus(ApptStatus.CANCELLED.getValue());
                        serviceBookedRepo.saveAndFlush(serviceBooked);
                        tblHl7Cdr.setRemarks("Appointment Canceled Successfully");
                        tblHl7Cdr.setStatus(1);
                        updateHl7CdrStatus(tblHl7Cdr);
                        List<TblServiceBooked> servicesBooked = serviceBookedRepo.findServiceBookedByTransId(serviceBooked.getAppointment().getTranId(), serviceBooked.getService().getServiceId(), new ArrayList<String>() {
                            {
                                add(ApptStatus.SCHEDULED.getValue());
                            }

                            {
                                add(ApptStatus.HL7CHECKIN.getValue());
                            }

                            {
                                add(ApptStatus.KIOSKCHECKIN.getValue());
                            }

                            {
                                add(ApptStatus.MANUALCHECKIN.getValue());
                            }

                            {
                                add(ApptStatus.SERVING.getValue());
                            }

                            {
                                add(ApptStatus.WAITING.getValue());
                            }
                        });
                        if (servicesBooked != null && servicesBooked.isEmpty()) {
                            if (isLogEnabled) {
                                logger.info("{}AnotherAppointmentsAlsoTheir,EncounterId:[{}]", header, tblHl7Cdr.getEncounterId());
                            }
                        } else {
                            updateAppointmentWithStatus(header, serviceBooked.getAppointment().getTranId(), ApptStatus.CANCELLED.getValue());
                        }
                    } else {
                        if (isLogEnabled) {
                            logger.info("{}Appointment:AlreadyInServingProcess,EncounterId:[{}]", header, tblHl7Cdr.getEncounterId());
                        }
                        tblHl7Cdr.setRemarks("Appointment already In Serving Process");
                        tblHl7Cdr.setStatus(0);
                        updateHl7CdrStatus(tblHl7Cdr);
                    }
                } else {
                    if (isLogEnabled) {
                        logger.info("{}Appointment:NotFound,EncounterId:[{}]", header, tblHl7Cdr.getEncounterId());
                    }
                    tblHl7Cdr.setRemarks("Appointment Not Found");
                    tblHl7Cdr.setStatus(0);
                    updateHl7CdrStatus(tblHl7Cdr);
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}<<:hl7cdr:not:found:hl7cdrId:[{}]", header, hl7Trigger.getHl7CdrId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:processS13Message:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            tblHl7Cdr = null;
            serviceBooked = null;
        }
    }

    private void processS13Message(String header, Hl7MsgTrigger hl7Trigger) {
        TblHl7Cdr tblHl7Cdr = null;
        TblServiceBooked serviceBooked = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>TrgigerData:[{}]", header, hl7Trigger.toString());
            }
            Optional<TblHl7Cdr> optionalCdr = hl7CdrRepo.findById(hl7Trigger.getHl7CdrId());
            if (optionalCdr.isPresent()) {
                tblHl7Cdr = optionalCdr.get();
                serviceBooked = serviceBookedRepo.findServiceBookedByServiceEncounterIdAndLocationCode(tblHl7Cdr.getEncounterId(), tblHl7Cdr.getLocationCode());
                if (serviceBooked != null) {
                    if (ApptStatus.getInstce(serviceBooked.getServiceStatus()) == ApptStatus.SCHEDULED) {
                        serviceBooked.setEndTime(dateUtils.convertDtStringToDate(tblHl7Cdr.getApptEndTime()));
                        serviceBooked.setStartTime(dateUtils.convertDtStringToDate(tblHl7Cdr.getApptStartTime()));
                        serviceBookedRepo.saveAndFlush(serviceBooked);
                        tblHl7Cdr.setRemarks("Appointment Updated Successfully");
                        tblHl7Cdr.setStatus(1);
                        updateHl7CdrStatus(tblHl7Cdr);
                        TblAppointment tblAppointment = serviceBooked.getAppointment();
                        List<TblServiceBooked> servicesBooked = serviceBookedRepo.findServiceBookedByTransId(tblAppointment.getTranId(), serviceBooked.getService().getServiceId(), new ArrayList<String>() {
                            {
                                add(ApptStatus.SCHEDULED.getValue());
                            }

                            {
                                add(ApptStatus.HL7CHECKIN.getValue());
                            }

                            {
                                add(ApptStatus.KIOSKCHECKIN.getValue());
                            }

                            {
                                add(ApptStatus.MANUALCHECKIN.getValue());
                            }

                            {
                                add(ApptStatus.WAITING.getValue());
                            }

                            {
                                add(ApptStatus.SERVING.getValue());
                            }
                        });
                        if (servicesBooked != null && servicesBooked.isEmpty()) {
                            if (isLogEnabled) {
                                logger.info("{}AnotherAppointmentsAlsoTheir,EncounterId:[{}]", header, tblHl7Cdr.getEncounterId());
                            }
                        } else {
                            tblAppointment.setEndTime(dateUtils.convertDtStringToDate(tblHl7Cdr.getApptEndTime()));
                            tblAppointment.setStartTime(dateUtils.convertDtStringToDate(tblHl7Cdr.getApptStartTime()));
                            aptRepo.saveAndFlush(tblAppointment);
                        }
                    } else {
                        if (isLogEnabled) {
                            logger.info("{}Appointment:AlreadyInServingProcess,EncounterId:[{}]", header, tblHl7Cdr.getEncounterId());
                        }
                        tblHl7Cdr.setRemarks("Appointment already In Serving Process");
                        tblHl7Cdr.setStatus(0);
                        updateHl7CdrStatus(tblHl7Cdr);
                    }
                } else {
                    if (isLogEnabled) {
                        logger.info("{}Appointment:NotFound,EncounterId:[{}]", header, tblHl7Cdr.getEncounterId());
                    }
                    tblHl7Cdr.setRemarks("Appointment Not Found");
                    tblHl7Cdr.setStatus(0);
                    updateHl7CdrStatus(tblHl7Cdr);
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}<<:hl7cdr:not:found:hl7cdrId:[{}]", header, hl7Trigger.getHl7CdrId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:processS13Message:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            tblHl7Cdr = null;
            serviceBooked = null;
        }
    }

    public void isItCheckinOrCheckoutMsg(String header, Hl7MsgTrigger hl7Trigger) {
        TblHl7Cdr tblHl7Cdr = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>TrgigerData:[{}]", header, hl7Trigger.toString());
            }
            Optional<TblHl7Cdr> optionalCdr = hl7CdrRepo.findById(hl7Trigger.getHl7CdrId());
            if (optionalCdr.isPresent()) {
                tblHl7Cdr = optionalCdr.get();
                String msg = tblHl7Cdr.getApptStatus();
                if (msg != null ? msg.equalsIgnoreCase("CHECKED OUT") : false) {
                    // need to implement Checkout
                    processCheckOutMessage("[Z01-COUT] ", tblHl7Cdr);
                } else {
                    processZ01CheckInMessage("[Z01-CIN] ", hl7Trigger, tblHl7Cdr);
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}<<:hl7cdr:not:found:hl7cdrId:[{}]", header, hl7Trigger.getHl7CdrId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:isItCheckinOrCheckoutMsg:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            tblHl7Cdr = null;
        }

    }

    @Autowired
    TokenSequenceRepositary tokenSequenceRepositary;

    @Transactional
    private int getServiceTokenSequence(String header, long serviceId, int strtRange, int endRange) {
        TblTokenSequence tokenSequence = null;
        int count = 1;
        try {
            if (isLogEnabled) {
                logger.info("{}>>ServiceId:[{}]", header, serviceId);
            }
            tokenSequence = tokenSequenceRepositary.getTokenSequenceByServiceId(serviceId);
            if (tokenSequence != null) {
                count = tokenSequence.getTokenSeq() + 1;
                tokenSequence.setTokenSeq(count);
            } else {
                tokenSequence = new TblTokenSequence();
                tokenSequence.setServiceId(serviceId);
                tokenSequence.setTokenSeq(count);
            }
            tokenSequenceRepositary.saveAndFlush(tokenSequence);
            return count;
        } catch (Exception e) {
            logger.error("{}Excep:getServiceTokenSequence:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
            return 1;
        }
    }

    @Transactional
    public long createToken(String header, String tokenNo, TblAppointment appt, Date checkInTime, short vitalRequired) {
        TblToken token = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>TokenNo:[{}],ApptId:[{}]", header, tokenNo, appt.getAppointmentId());
            }
            token = tokenRepositary.findTokenByTranId(appt.getTranId());
            if (token != null) {
                if (token.getStatus().equalsIgnoreCase(TokenStatus.FLOTING.getValue())) {
                    token.setStatus(vitalRequired == (short) 1 ? TokenStatus.VWAIT.getValue() : TokenStatus.WAIT.getValue());
                    return tokenRepositary.saveAndFlush(token).getTokenId();
                } else {
                    return token.getTokenId();
                }
            } else {
                token = new TblToken();
                token.setAppaitment(appt);
                // token.setTranId(appt.getTranId());
                token.setCreatedOn(checkInTime);
                token.setUpdatedOn(checkInTime);
                token.setNoShow(0);
                token.setStatus(vitalRequired == (short) 1 ? TokenStatus.VWAIT.getValue() : TokenStatus.WAIT.getValue());
                token.setTokenNo(tokenNo);
                return tokenRepositary.saveAndFlush(token).getTokenId();
            }

        } catch (Exception e) {
            logger.error("{}Excep:createToken:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally {
            token = null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public long updateSerivBookedWithStatus(String header, String tranId, List<String> previousStatus, String status) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>tranId:{},Status:{}", header, tranId, status);
            }
            return serviceBookedRepo.updateTblServiceBooked(tranId, previousStatus, status);
        } catch (Exception e) {
            logger.error("{}Excep:updateSerivBookedWithCheckInStatus:tranId:{},Error:{}", header, tranId, ExceptionUtils.getRootCauseMessage(e));
            return 0;
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

    private CdrLog generateCdrLog(long branchId, Date checkInTime, TblServiceBooked servicBooked) {
        CdrLog log = new CdrLog();
        log.setBranchId(branchId);
        log.setCheckInTime(DateUtils.sdf.get().format(checkInTime));
        log.setStartTime(DateUtils.sdf.get().format(servicBooked.getStartTime()));
        log.setEndTime(DateUtils.sdf.get().format(servicBooked.getEndTime()));
        log.setDrId(servicBooked.getDrId());
        log.setLocationId(servicBooked.getAppointment().getLocationId());
        log.setPatienId(servicBooked.getAppointment().getPatientId());
        log.setServiceId(servicBooked.getService().getServiceId());
        log.setTransId(servicBooked.getAppointment().getTranId());
        log.setStatus(TokenStatus.CHECKIN);
        return log;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public long updateAppointmentWithStatus(String header, String tranId, String status) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>tranId:{},Status:{}", header, tranId, status);
            }
            return aptRepo.updateTblAppointmentStatus(tranId, status);
        } catch (Exception e) {
            logger.error("{}Excep:updateAppointmentCheckInDone:tranId:{},Error:{}", header, tranId, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public void updateAvailablStatusServicBookedWithCheckoutStatus(String header, TblServiceBooked servicBooked) {
        TblService service = null;
        TblBranch tblBranch = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>TblServiceBooked:{}", header, servicBooked);
            }
            service = servicBooked.getService();
            Optional<TblBranch> optBranch = branchRepo.findById(service.getBranchId());
            if (optBranch.isPresent()) {
                tblBranch = optBranch.get();
                if (tblBranch.getIsFloating() == 0) {
                    servicBooked.setServiceStatus(ApptStatus.HL7CHECKOUT.getValue());
                } else {
                    servicBooked.setServiceStatus(ApptStatus.FLOTING.getValue());
                }
                serviceBookedRepo.saveAndFlush(servicBooked);
                List<TblServiceBooked> serviceBookList = serviceBookedRepo.findServiceBookedByTransId(servicBooked.getAppointment().getTranId(), servicBooked.getServiceBookedId(), new ArrayList<String>() {
                    {
                        add(ApptStatus.HL7CHECKIN.getValue());
                    }

                    {
                        add(ApptStatus.KIOSKCHECKIN.getValue());
                    }

                    {
                        add(ApptStatus.MANUALCHECKIN.getValue());
                    }

                    {
                        add(ApptStatus.SCHEDULED.getValue());
                    }

                    {
                        add(ApptStatus.WAITING.getValue());
                    }

                    {
                        add(ApptStatus.SERVING.getValue());
                    }
                });
                if (serviceBookList != null && !serviceBookList.isEmpty()) {
                    //do nothing
                } else {
                    if (tblBranch.getIsFloating() == 0) {
                        updateAppointmentWithStatus(header, servicBooked.getAppointment().getTranId(), ApptStatus.HL7CHECKOUT.getValue());
                    } else {
                        updateAppointmentWithStatus(header, servicBooked.getAppointment().getTranId(), ApptStatus.FLOTING.getValue());

                    }
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}<<:NotFoundtheBranch:TblServiceBooked:[{}]", header, servicBooked);
                }
            }
        } catch (Exception e) {
            logger.error("{}Excep:updateAvailablStatusServicBookedWithCheckoutStatus:TblServiceBooked:{},Error:{}", header, servicBooked, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            service = null;
            tblBranch = null;
        }
    }

    public void updateCheckinStatusServicBookedWithCheckoutStatus(String header, TblServiceBooked servicBooked) {
        TblBranch tblBranch = null;
        TblService service = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>TblServiceBooked:{}", header, servicBooked);
            }
            service = servicBooked.getService();
            Optional<TblBranch> optBranch = branchRepo.findById(service.getBranchId());
            if (optBranch.isPresent()) {
                tblBranch = optBranch.get();
                if (tblBranch.getIsFloating() == 0) {
                    servicBooked.setServiceStatus(ApptStatus.HL7CHECKOUT.getValue());
                } else {
                    servicBooked.setServiceStatus(ApptStatus.FLOTING.getValue());
                }
                serviceBookedRepo.saveAndFlush(servicBooked);
                List<TblServiceBooked> serviceBookList = serviceBookedRepo.findServiceBookedByTransId(servicBooked.getAppointment().getTranId(), servicBooked.getServiceBookedId(), new ArrayList<String>() {
                    {
                        add(ApptStatus.HL7CHECKIN.getValue());
                    }

                    {
                        add(ApptStatus.KIOSKCHECKIN.getValue());
                    }

                    {
                        add(ApptStatus.MANUALCHECKIN.getValue());
                    }

                    {
                        add(ApptStatus.SCHEDULED.getValue());
                    }

                    {
                        add(ApptStatus.WAITING.getValue());
                    }

                    {
                        add(ApptStatus.SERVING.getValue());
                    }
                });
                if (serviceBookList != null && !serviceBookList.isEmpty()) {
                    //do nothing
                    //update token with waiting status
                    // update token tokensummarydetail with partialend
                    //update token visit summary.
                    perfromReturnToWaitingQueueActionAndPartialEndStatus(header, servicBooked, false, tblBranch);
                } else {
                    //update token with served status
                    // update token tokensummarydetail with served end
                    //update token visit summary with checkout
                    if (tblBranch.getIsFloating() == 0) {
                        updateAppointmentWithStatus(header, servicBooked.getAppointment().getTranId(), ApptStatus.HL7CHECKOUT.getValue());
                    } else {
                        updateAppointmentWithStatus(header, servicBooked.getAppointment().getTranId(), ApptStatus.FLOTING.getValue());
                    }
                    perfromReturnToWaitingQueueActionAndPartialEndStatus(header, servicBooked, true, tblBranch);
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}<<:NotFoundtheBranch:TblServiceBooked:[{}]", header, servicBooked);
                }
            }

        } catch (Exception e) {
            logger.error("{}Excep:updateCheckinStatusServicBookedWithCheckoutStatus:TblServiceBooked:{},Error:{}",
                    header, servicBooked, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            tblBranch = null;
            service = null;
        }
    }

    private CdrLog generateCdrLog(String header, TokenStatus status,
            TblToken token, long servedBy, TblServiceBooked serviceBooked,
            Date firstCallTime, Date strtTime, Date checkOutTime) {
        try {
            CdrLog log = new CdrLog();
            log.setStatus(status);
            log.setFirstCallTime(firstCallTime != null ? DateUtils.sdf.get().format(firstCallTime) : null);
            log.setStartTime(DateUtils.sdf.get().format(strtTime));
            log.setEndTime(DateUtils.sdf.get().format(token.getUpdatedOn()));
            log.setCheckInTime(DateUtils.sdf.get().format(token.getCreatedOn()));
            log.setRoomId(token.getRoomNo() != null ? token.getRoomNo() : 0);
            log.setDrId(serviceBooked.getDrId());
            log.setServedBy(servedBy);
            log.setServiceId(serviceBooked.getService().getServiceId());
            log.setTransId(token.getAppaitment().getTranId());
            log.setPatienId(token.getAppaitment().getPatientId());
            log.setBranchId(serviceBooked.getService().getBranchId());
            log.setCheckOutTime(checkOutTime != null ? DateUtils.sdf.get().format(checkOutTime) : null);
            log.setServingState(token.getStatus().equalsIgnoreCase(TokenStatus.SRNG.getValue()));
            return log;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:generateCdrLog:TblToken:{},servedBy:{},ServiceBooked:{},firstCallTime:{},starttime:{},checkouttime:{},Error:{}",
                    header, token, servedBy, serviceBooked, firstCallTime, strtTime, checkOutTime, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }

    private void perfromReturnToWaitingQueueActionAndPartialEndStatus(String header, TblServiceBooked serviceBooked, boolean isServedDone, TblBranch branch) {
        TblToken token = null;
        CdrLog log = null;
        Date strtTime = null;
        TokenStatus staus = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>TranId:{},serviceId:{},isServedDone:{}", header, serviceBooked.getAppointment().getTranId(), serviceBooked.getService().getServiceId(), isServedDone);
            }
            token = tokenRepositary.findTokenByTranId(serviceBooked.getAppointment().getTranId());
            if (token != null) {
                strtTime = token.getUpdatedOn();
                token.setUpdatedOn(dateUtils.getdate());
                if (isServedDone) {
                    if (branch.getIsFloating() == 0) {
                        token.setStatus(TokenStatus.SRVD.getValue());
                    } else {
                        token.setStatus(TokenStatus.FLOTING.getValue());
                    }
                    staus = TokenStatus.SRVD;
                } else {
                    token.setStatus(TokenStatus.WAIT.getValue());
                    staus = TokenStatus.PSRVD;
                }
                token = tokenRepositary.saveAndFlush(token);
                //serviceBooked = serviceBookedRepo.getServiceBookedByTranIdNdServiceId(token.getAppaitment().getTranId(), serviceId);
                if (isServedDone) {
                    log = generateCdrLog(header, staus, token, 99999, serviceBooked, null, strtTime, token.getUpdatedOn());
                } else {
                    log = generateCdrLog(header, staus, token, 99999, serviceBooked, null, strtTime, null);
                }
                if (log != null) {
                    sendCdrReportToCdrQueue(header, log);
                }
            } else {
                logger.info("{}token:Not:Exist:tranId:[{}]", header, serviceBooked.getAppointment().getTranId());
            }
        } catch (Exception e) {
            logger.error("{}Excep:perfromReturnToWaitingQueueActionAndPartialEndStatus:tranId:{},serviceId:{},Error:{}", header, serviceBooked.getAppointment().getTranId(), serviceBooked.getService().getServiceId(), ExceptionUtils.getRootCauseMessage(e));
        } finally {
            serviceBooked = null;
            strtTime = null;
            staus = null;
            token = null;
            log = null;
        }
    }

    public void processCheckOutMessage(String header, TblHl7Cdr hl7Cdr) {
        TblServiceBooked servicBooked = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>hl7CdrId:[{}],EncounterId:[{}]", header, hl7Cdr.getHl7CdrId(), hl7Cdr.getEncounterId());
            }
            servicBooked = serviceBookedRepo.findServiceBookedByServiceEncounterIdAndLocationCode(hl7Cdr.getEncounterId(), hl7Cdr.getLocationCode());
            if (servicBooked != null) {
                switch (ApptStatus.getInstce(servicBooked.getServiceStatus())) {
                    case SCHEDULED:
                        updateAvailablStatusServicBookedWithCheckoutStatus(header, servicBooked);
                        hl7Cdr.setRemarks("Check out Successfully");
                        hl7Cdr.setStatus(1);
                        updateHl7CdrStatus(hl7Cdr);
                        break;
                    case KIOSKCHECKIN:
                    case HL7CHECKIN:
                    case MANUALCHECKIN:
                    case WAITING:
                    case SERVING:
                        updateCheckinStatusServicBookedWithCheckoutStatus(header, servicBooked);
                        hl7Cdr.setRemarks("Check out Successfully");
                        hl7Cdr.setStatus(1);
                        updateHl7CdrStatus(hl7Cdr);
                        break;
                    case HL7CHECKOUT:
                        logger.info("{}Already:checkoutfromHL7,EncounterId:[{}]", header, hl7Cdr.getEncounterId());
                        //updateCheckinStatusServicBookedWithCheckoutStatus(header, servicBooked);
                        hl7Cdr.setRemarks("Already checkout done from HL7");
                        hl7Cdr.setStatus(0);
                        updateHl7CdrStatus(hl7Cdr);
                        break;
                    case NURSECHECKOUT:
                        logger.info("{}Already:checkout,EncounterId:[{}]", header, hl7Cdr.getEncounterId());
                        //  updateCheckinStatusServicBookedWithCheckoutStatus(header, servicBooked);
                        hl7Cdr.setRemarks("Already checkout done");
                        hl7Cdr.setStatus(0);
                        updateHl7CdrStatus(hl7Cdr);
                        break;
                    default:
                        logger.info("{}>>INvalidStatus:[{}],hl7CdrId:[{}],EncounterId:[{}]", header, servicBooked.getServiceStatus(), hl7Cdr.getHl7CdrId(), hl7Cdr.getEncounterId());
                        break;
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}Appointment:NotFound,EncounterId:[{}]", header, hl7Cdr.getEncounterId());
                }
                hl7Cdr.setRemarks("Appointment Not Found");
                hl7Cdr.setStatus(0);
                updateHl7CdrStatus(hl7Cdr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:processCheckOutMessage:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            servicBooked = null;
        }
    }

    @Transactional
    private void processZ01CheckInMessage(String header, Hl7MsgTrigger hl7Trigger, TblHl7Cdr tblHl7Cdr) {
        TblServiceBooked servicBooked = null;
        TblService service = null;
        TblPatient patient = null;
        long tokenId = 0;
        try {
            if (isLogEnabled) {
                logger.info("{}>>hl7CdrId:[{}],EncounterId:[{}],LocationCode:[{}],MrnNo:[{}]", header, tblHl7Cdr.getHl7CdrId(), tblHl7Cdr.getEncounterId(), tblHl7Cdr.getLocationCode(), tblHl7Cdr.getMrnNo());
            }
            servicBooked = serviceBookedRepo.findServiceBookedByServiceEncounterIdAndLocationCode(tblHl7Cdr.getEncounterId(), tblHl7Cdr.getLocationCode());
            if (servicBooked != null) {
                if (isToday(dateUtils.datezFormat().parse(dateUtils.convertDateToDtString(servicBooked.getStartTime())))) {
                    if (ApptStatus.SCHEDULED.getValue().equalsIgnoreCase(servicBooked.getServiceStatus())) {
                        service = servicBooked.getService();
                        Optional<TblBranch> optBranch = branchRepo.findById(service.getBranchId());
                        if (optBranch.isPresent()) {
                            TblBranch branch = optBranch.get();
                            patient = patientRepo.findPatientByMrn(tblHl7Cdr.getMrnNo());
                            if (patient != null) {
                                Date checkinTime = dateUtils.getdate();
                                String sP = patient.getPriority() == (short) 1 ? "P" + service.getTokenPrefix() : service.getTokenPrefix();
                                tokenId = createToken(
                                        header,
                                        sP + ""
                                        + getServiceTokenSequence(
                                                header,
                                                service.getServiceId(),
                                                service.getStartSeq(),
                                                service.getEndSeq()),
                                        servicBooked.getAppointment(), checkinTime, branch.getIsVital()
                                );
                                if (tokenId > 0) {
                                    updateSerivBookedWithStatus(header, servicBooked.getAppointment().getTranId(), new ArrayList<String>() {
                                        {
                                            add(ApptStatus.SCHEDULED.getValue());
                                        }
                                    }, ApptStatus.HL7CHECKIN.getValue());
                                    updateAppointmentWithStatus(header, servicBooked.getAppointment().getTranId(), ApptStatus.HL7CHECKIN.getValue());
                                    tblHl7Cdr.setRemarks("Checked in Successfully");
                                    tblHl7Cdr.setStatus(1);
                                    updateHl7CdrStatus(tblHl7Cdr);
                                    CdrLog log = generateCdrLog(service.getBranchId(), checkinTime, servicBooked);
                                    if (log != null) {
                                        sendCdrReportToCdrQueue(header, log);
                                    }
                                    if (isTriggerNotification) {
                                        TriggerTypeDTO fileTriger = new TriggerTypeDTO(servicBooked.getAppointment().getTranId(), TriggerType.CMSG.getValue(), servicBooked.getService().getServiceId());
                                        jmsTemplate.convertAndSend(triggerQueueName, mapper.writeValueAsString(fileTriger));
                                    }
                                } else {
                                    if (isLogEnabled) {
                                        logger.info("{}<<:Unable:To:Chekcin:Appt:[{}],hl7cdrId:[{}],encounterId:[{}]", header, servicBooked.getAppointment().getAppointmentId(), servicBooked.getStartTime(), hl7Trigger.getHl7CdrId(), tblHl7Cdr.getEncounterId());
                                    }
                                    tblHl7Cdr.setRemarks("Unable to checkin the Appointment");
                                    tblHl7Cdr.setStatus(0);
                                    updateHl7CdrStatus(tblHl7Cdr);
                                }

                            } else {
                                if (isLogEnabled) {
                                    logger.info("{}<<::PatientInfo:Not:Found:Appt:[{}],hl7cdrId:[{}],encounterId:[{}]", header, servicBooked.getAppointment().getAppointmentId(), servicBooked.getStartTime(), hl7Trigger.getHl7CdrId(), tblHl7Cdr.getEncounterId());
                                }
                                tblHl7Cdr.setRemarks("Unable to Find Patient Info");
                                tblHl7Cdr.setStatus(0);
                                updateHl7CdrStatus(tblHl7Cdr);
                            }

                        } else {
                            if (isLogEnabled) {
                                logger.info("{}<<:NotFoundtheBraanch:hl7cdrId:[{}],encounterId:[{}]", header, hl7Trigger.getHl7CdrId(), tblHl7Cdr.getEncounterId());
                            }
                            tblHl7Cdr.setRemarks("Unable To find the branch");
                            tblHl7Cdr.setStatus(0);
                            updateHl7CdrStatus(tblHl7Cdr);
                        }
                    } else {
                        Optional<TblBranch> optBranch = branchRepo.findById(service.getBranchId());
                        if (optBranch.isPresent()) {
                            TblBranch branch = optBranch.get();
                            short isRegistration = branch.getIsRegistration();
                            if (isRegistration == 1) {
                                TblToken token = tokenRepositary.findTokenByTranIdAndStatuss(servicBooked.getAppointment().getTranId(), new ArrayList<String>() {
                                    {
                                        add(TokenStatus.RWAIT.getValue());
                                    }

                                    {
                                        add(TokenStatus.RSRNG.getValue());
                                    }
                                });
                                if (token != null) {
                                    Date previousDate = token.getUpdatedOn();
                                    if (branch.getIsVital() == 0) {
                                        token.setStatus(TokenStatus.WAIT.getValue());
                                        token.setUpdatedOn(dateUtils.getdate());
                                        tokenRepositary.saveAndFlush(token);
                                    } else {
                                        token.setStatus(TokenStatus.VWAIT.getValue());
                                        token.setUpdatedOn(dateUtils.getdate());
                                        tokenRepositary.saveAndFlush(token);
                                    }
                                    updateSerivBookedWithStatus(header, servicBooked.getAppointment().getTranId(), new ArrayList<String>() {
                                        {
                                            add(ApptStatus.SCHEDULED.getValue());
                                        }

                                        {
                                            add(ApptStatus.HL7CHECKIN.getValue());
                                        }

                                        {
                                            add(ApptStatus.KIOSKCHECKIN.getValue());
                                        }

                                        {
                                            add(ApptStatus.MANUALCHECKIN.getValue());
                                        }

                                        {
                                            add(ApptStatus.SERVING.getValue());
                                        }

                                        {
                                            add(ApptStatus.WAITING.getValue());
                                        }
                                    }, ApptStatus.WAITING.getValue());
                                    CdrLog log = generateCdrLog(header, TokenStatus.RSRVD, token,
                                            99999, servicBooked, null, previousDate, null);
                                    if (log != null) {
                                        sendCdrReportToCdrQueue(header, log);
                                    }
                                } else {
                                    if (isLogEnabled) {
                                        logger.info("{}<<:Already:CheckinDone:hl7cdrId:[{}],encounterId:[{}]", header, hl7Trigger.getHl7CdrId(), tblHl7Cdr.getEncounterId());
                                    }
                                    tblHl7Cdr.setRemarks("Already Checkin Done");
                                    tblHl7Cdr.setStatus(0);
                                    updateHl7CdrStatus(tblHl7Cdr);
                                }
                            } else {
                                if (isLogEnabled) {
                                    logger.info("{}<<:Already:CheckinDone:hl7cdrId:[{}],encounterId:[{}]", header, hl7Trigger.getHl7CdrId(), tblHl7Cdr.getEncounterId());
                                }
                                tblHl7Cdr.setRemarks("Already Checkin Done");
                                tblHl7Cdr.setStatus(0);
                                updateHl7CdrStatus(tblHl7Cdr);
                            }
                        } else {
                            if (isLogEnabled) {
                                logger.info("{}<<:NotFoundtheBraanch:hl7cdrId:[{}],encounterId:[{}]", header, hl7Trigger.getHl7CdrId(), tblHl7Cdr.getEncounterId());
                            }
                            tblHl7Cdr.setRemarks("Unable To find the branch");
                            tblHl7Cdr.setStatus(0);
                            updateHl7CdrStatus(tblHl7Cdr);
                        }
                    }
                } else {
                    if (isLogEnabled) {
                        logger.info("{}<<:Appt:Taken:NotForToday:[{}],hl7cdrId:[{}],encounterId:[{}]", header, servicBooked.getStartTime(), hl7Trigger.getHl7CdrId(), tblHl7Cdr.getEncounterId());
                    }
                    tblHl7Cdr.setRemarks("Appointment was not taken for Today");
                    tblHl7Cdr.setStatus(0);
                    updateHl7CdrStatus(tblHl7Cdr);
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}<<:Appointement:Not:Found:encounterId:[{}]", header, tblHl7Cdr.getEncounterId());
                }
                tblHl7Cdr.setRemarks("Appointement Not Found");
                tblHl7Cdr.setStatus(0);
                updateHl7CdrStatus(tblHl7Cdr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:processZ01Message:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {

        }
    }

//    public void prepareCdrForRegistrationEnd(String header, Date previousUpdate, TblToken token,
//            TblServiceBooked serviceBooked) {
//        try {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("{}Excep:cdrProcessing:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
//        }
//    }
    public boolean isToday(TemporalAccessor date) {
        return java.time.LocalDate.now().equals(LocalDate.from(date));
    }

    public void groupingAppointment(String header,
            TblServiceBooked serviceBooked,
            long doctorId, TblHl7Cdr hl7Cdr, long serviceId) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>ServiceBooked:{},DoctorId:{},Hl7cdr:{},serviceId:{}", header,
                        serviceBooked, doctorId, hl7Cdr, serviceId);
            }
            TblServiceBooked serviceBooked1 = new TblServiceBooked();
            serviceBooked1.setAppointment(serviceBooked.getAppointment());
            serviceBooked1.setDrId(doctorId);
            serviceBooked1.setEncounterId(hl7Cdr.getEncounterId());
            serviceBooked1.setEndTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptEndTime()));
            serviceBooked1.setStartTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptStartTime()));
            serviceBooked1.setService(new TblService(serviceId));
            serviceBooked1.setReasonToVisit(hl7Cdr.getReasonToVist());
            serviceBooked1.setServiceStatus(ApptStatus.SCHEDULED.getValue());
            serviceBookedRepo.saveAndFlush(serviceBooked1);
            if (isTriggerNotification) {
                TriggerTypeDTO fileTriger = new TriggerTypeDTO(serviceBooked.getAppointment().getTranId(), TriggerType.CMSG.getValue(), serviceId);
                jmsTemplate.convertAndSend(triggerQueueName, mapper.writeValueAsString(fileTriger));
            }
        } catch (Exception e) {
            logger.error("{}Excep:groupingAppointment:ServiceBooked:{},DoctorId:{},Hl7cdr:{},serviceId:{}Error:[{}]", header, serviceBooked, doctorId, hl7Cdr, serviceId, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void creatNewTblAppointment(String header, long patientId, TblHl7Cdr hl7Cdr, long locationId,
            long doctorId, long serviceId) {
        TblServiceBooked serviceBooked = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>patientId:{},Hl7cdr:{},locatiionId:{},doctorId:{},serviceId:{}", header,
                        patientId, hl7Cdr, locationId, doctorId, serviceId);
            }
            TblAppointment appointment = new TblAppointment();
            appointment.setPatientId(patientId);
            appointment.setTranId(dateUtils.generateUniqueTranId());
            appointment.setClerkId(hl7Cdr.getClerkId());
            appointment.setCreatedOn(dateUtils.getdate());
            appointment.setUpdatedOn(dateUtils.getdate());
            appointment.setStartTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptStartTime()));
            appointment.setEndTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptEndTime()));
            appointment.setApptStatus(ApptStatus.SCHEDULED.getValue());
            appointment.setLocationId(locationId);
            aptRepo.saveAndFlush(appointment);

            serviceBooked = new TblServiceBooked();
            serviceBooked.setAppointment(appointment);
            serviceBooked.setDrId(doctorId);
            serviceBooked.setServiceStatus(ApptStatus.SCHEDULED.getValue());
            serviceBooked.setReasonToVisit(hl7Cdr.getReasonToVist());
            serviceBooked.setEncounterId(hl7Cdr.getEncounterId());
            serviceBooked.setEndTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptEndTime()));
            serviceBooked.setStartTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptStartTime()));
            serviceBooked.setService(new TblService(serviceId));
            serviceBookedRepo.saveAndFlush(serviceBooked);
            if (isTriggerNotification) {
                TriggerTypeDTO fileTriger = new TriggerTypeDTO(serviceBooked.getAppointment().getTranId(), TriggerType.CMSG.getValue(), serviceId);
                jmsTemplate.convertAndSend(triggerQueueName, mapper.writeValueAsString(fileTriger));
            }
        } catch (Exception e) {
            logger.error("{}Excep:creatNewTblAppointment:patientId:{},Hl7cdr:{},locatiionId:{},doctorId:{},serviceId:{},Error:[{}]", header, patientId, hl7Cdr, locationId, doctorId, serviceId, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            serviceBooked = null;
        }
    }

    @Value("${trigger.notification.enabled:false}")
    private boolean isTriggerNotification;

    @Value("${qsmart.trigger.queue.name:Q2_TRIGGER}")
    private String triggerQueueName;

    public void createAppoinment(String header, TblHl7Cdr hl7Cdr, long branchId,
            long serviceId, long doctorId, long patientId, long locationId) {
        Date fromDate = null;
        Date toDate = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>branchId:[{}],serviceId:[{}],doctorId:[{}],patientId:[{}],locationId:[{}],hl7CdrId:[{}]",
                        header, branchId, serviceId, doctorId, patientId, locationId, hl7Cdr.getHl7CdrId());
            }
            fromDate = dateUtils.dayStartDateTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptStartTime()));
            toDate = dateUtils.dayEndDateTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptEndTime()));
            List<TblServiceBooked> servicBookedList = serviceBookedRepo.findServiceBookedByServiceBranchId(branchId, patientId, fromDate, toDate);
            TblServiceBooked serviceBooked = (servicBookedList != null && !servicBookedList.isEmpty()) ? servicBookedList.get(0) : null;
            if (serviceBooked != null) {
                ApptStatus aptStatus = ApptStatus.getInstce(serviceBooked.getAppointment().getApptStatus());
                switch (aptStatus) {
                    case FLOTING:
                        TblAppointment appt = serviceBooked.getAppointment();
                        appt.setApptStatus(ApptStatus.SCHEDULED.getValue());
                        appt.setUpdatedOn(dateUtils.getdate());
                        aptRepo.saveAndFlush(appt);
                        if (isTriggerNotification) {
                            TriggerTypeDTO fileTriger = new TriggerTypeDTO(appt.getTranId(), TriggerType.CMSG.getValue(), serviceId);
                            jmsTemplate.convertAndSend(triggerQueueName, mapper.writeValueAsString(fileTriger));
                        }
                    case HL7CHECKIN:
                    case MANUALCHECKIN:
                    case KIOSKCHECKIN:
                    case SCHEDULED:
                    case SERVING:
                    case WAITING:
                        groupingAppointment(header, serviceBooked, doctorId, hl7Cdr, serviceId);
                        break;
                    case CANCELLED:
                    case FORCECHECKOUT:
                    case HL7CHECKOUT:
                    case NURSECHECKOUT:
                    default:
                        creatNewTblAppointment(header, patientId, hl7Cdr, locationId, doctorId, serviceId);
                        break;
                }
            } else {
                creatNewTblAppointment(header, patientId, hl7Cdr, locationId, doctorId, serviceId);
            }

//            if (serviceBooked != null) {
//                TblServiceBooked serviceBooked1 = new TblServiceBooked();
//                serviceBooked1.setAppointment(serviceBooked.getAppointment());
//                serviceBooked1.setDrId(doctorId);
//                serviceBooked1.setEncounterId(hl7Cdr.getEncounterId());
//                serviceBooked1.setEndTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptEndTime()));
//                serviceBooked1.setStartTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptStartTime()));
//                serviceBooked1.setService(new TblService(serviceId));
//                serviceBooked1.setReasonToVisit(hl7Cdr.getReasonToVist());
//                serviceBooked1.setServiceStatus(ApptStatus.SCHEDULED.getValue());
//                serviceBookedRepo.saveAndFlush(serviceBooked1);
//            } else {
//                TblAppointment appointment = new TblAppointment();
//                appointment.setPatientId(patientId);
//                appointment.setTranId(dateUtils.generateUniqueTranId());
//                appointment.setClerkId(hl7Cdr.getClerkId());
//                appointment.setCreatedOn(dateUtils.getdate());
//                appointment.setUpdatedOn(dateUtils.getdate());
//                appointment.setStartTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptStartTime()));
//                appointment.setEndTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptEndTime()));
//                appointment.setApptStatus(ApptStatus.SCHEDULED.getValue());
//                appointment.setLocationId(locationId);
//                aptRepo.saveAndFlush(appointment);
//
//                serviceBooked = new TblServiceBooked();
//                serviceBooked.setAppointment(appointment);
//                serviceBooked.setDrId(doctorId);
//                serviceBooked.setServiceStatus(ApptStatus.SCHEDULED.getValue());
//                serviceBooked.setReasonToVisit(hl7Cdr.getReasonToVist());
//                serviceBooked.setEncounterId(hl7Cdr.getEncounterId());
//                serviceBooked.setEndTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptEndTime()));
//                serviceBooked.setStartTime(dateUtils.convertDtStringToDate(hl7Cdr.getApptStartTime()));
//                serviceBooked.setService(new TblService(serviceId));
//                serviceBookedRepo.saveAndFlush(serviceBooked);
//            }
        } catch (Exception e) {
            logger.error("{}Excep:createAppoinment:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            fromDate = null;
            toDate = null;
        }
    }

    private void processS12Message(String header, Hl7MsgTrigger hl7Trigger) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>TrgigerData:[{}]", header, hl7Trigger.toString());
            }
            Optional<TblHl7Cdr> optionalCdr = hl7CdrRepo.findById(hl7Trigger.getHl7CdrId());
            if (optionalCdr.isPresent()) {
                TblHl7Cdr hl7cdr = optionalCdr.get();
                TblBranch branch = branchRepo.findBrnachNameByLocation(hl7cdr.getServiceLocation(), hl7cdr.getLocationCode());
                if (branch != null) {
                    TblServiceBooked serviceBooked = serviceBookedRepo.findServiceBookedByServiceEncounterIdAndLocationId(hl7cdr.getEncounterId(), branch.getLocation().getLocationId());
                    if (serviceBooked != null) {
                        if (isLogEnabled) {
                            logger.info("{}<<:AlreadyOneApptExistWithSameEncounterId:EncounterId:{}:hl7cdrId:[{}]", header, hl7cdr.getEncounterId(), hl7Trigger.getHl7CdrId());
                        }
                        hl7cdr.setRemarks("Duplicate Encounter Id");
                        hl7cdr.setStatus(0);
                        updateHl7CdrStatus(hl7cdr);
                    } else {
                        TblApptType aptType = findAppotintmentType(header, hl7cdr.getApptCode(), hl7cdr.getApptType(), branch.getLocation().getLocationId(), branch.getBranchId());
                        if (aptType != null) {
                            TblService service = null;
                            if (aptType.getServices() != null && !aptType.getServices().isEmpty()) {
                                service = aptType.getServices().iterator().next();
                            } else {
                                service = serviceRepo.getDefaultServiceByBranchId(branch.getBranchId(), (short) 1);
                            }
                            TblUser doctor = findResourceByResourceCode(header, hl7cdr.getResourceCode(),
                                    hl7cdr.getDrFirstName(), hl7cdr.getDrLastName(), branch.getLocation().getTblenterprise().getEnterpriseId(), branch.getLocation().getLocationId());
                            if (doctor != null) {
                                createAppoinment(header, hl7cdr, branch.getBranchId(),
                                        service.getServiceId(), doctor.getUserId(), hl7Trigger.getPatientId(),
                                        branch.getLocation().getLocationId());
                                if (doctor != null) {
                                    aptType.setUsers(new HashSet<TblUser>() {
                                        {
                                            add(new TblUser(doctor.getUserId()));
                                        }
                                    });
                                    aptTypeRepo.saveAndFlush(aptType);
                                }
                                hl7cdr.setRemarks("Appointment added Successfully");
                                hl7cdr.setStatus(1);
                                updateHl7CdrStatus(hl7cdr);
                            } else {
                                if (isLogEnabled) {
                                    logger.info("{}<<:UnableToCreteResource:hl7cdrId:[{}]", header, hl7Trigger.getHl7CdrId());
                                }
                                hl7cdr.setRemarks("Unable to create Resource");
                                hl7cdr.setStatus(0);
                                updateHl7CdrStatus(hl7cdr);
                            }
                        } else {
                            if (isLogEnabled) {
                                logger.info("{}<<:UnableToCreteApptType:hl7cdrId:[{}]", header, hl7Trigger.getHl7CdrId());
                            }
                            hl7cdr.setRemarks("Unable to create ApptType");
                            hl7cdr.setStatus(0);
                            updateHl7CdrStatus(hl7cdr);
                        }
                    }
                } else {
                    if (isLogEnabled) {
                        logger.info("{}<<:Location Not Found:hl7cdrId:[{}]", header, hl7Trigger.getHl7CdrId());
                    }
                    hl7cdr.setRemarks("Location Not Found");
                    hl7cdr.setStatus(0);
                    updateHl7CdrStatus(hl7cdr);
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}<<:hl7cdr:not:found:hl7cdrId:[{}]", header, hl7Trigger.getHl7CdrId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:processS12Message:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void updateHl7CdrStatus(TblHl7Cdr hl7Cdr) {
        hl7CdrRepo.saveAndFlush(hl7Cdr);
    }

    public TblUser findResourceByResourceCode(String header, String resourceCode, String drfirstName, String drLastName, long enterpriseId, long locationId) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>resourceCode:{},drfirstName:{},drLastName:{}", header, resourceCode, drfirstName, drLastName);
            }
            TblUser doctor = userRepo.findUserByResourceCode(resourceCode);
            if (doctor != null) {
                return doctor;
            } else {
                doctor = new TblUser();
                doctor.setResourceCode(resourceCode);
                doctor.setUserName(resourceCode);
                doctor.setFirstName(drfirstName);
                doctor.setHashPassword(Hider.getInstance().encrypt("123456"));
                doctor.setLastName(drLastName);
                doctor.setUserType("Nrml");
                doctor.setCreatedOn(dateUtils.getdate());
                doctor.setUpdatedBy(999999l);
                doctor.setUpdatedOn(dateUtils.getdate());
                doctor.setCreatedBy(999999l);
                doctor.setEnterprise(new TblEnterprise(enterpriseId));
                doctor.setLocations(new HashSet<TblLocation>() {
                    {
                        add(new TblLocation(locationId));
                    }
                });
                doctor.setIsActive((short) 1);
                doctor.setIsFirstLogin((short) 1);
                doctor.setSystemAccess((short) 0);
                doctor.setRoles(new HashSet<TblRole>() {
                    {
                        add(new TblRole(roleRepo.findRoleIdByRoleCode("DR")));
                    }
                });
                doctor = userRepo.saveAndFlush(doctor);
                return doctor;
            }
        } catch (Exception e) {
            logger.error("{}Excep:findResourceByResourceCode:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }

    public TblApptType findAppotintmentType(String header, String apptCode, String apptType, long locatinId, long branchId) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>apptCode:{},apptType:{},locationId:{},branchId:{}", header, apptCode, apptType, locatinId, branchId);
            }
            TblApptType aptType = aptTypeRepo.findApptTypeByApptCode(apptCode, branchId);
            if (aptType != null) {
                return aptType;
            } else {
                aptType = new TblApptType();
                aptType.setApptCode(apptCode);
                aptType.setApptType(apptType);
                aptType.setCreatedBy(999999l);
                aptType.setCreatedOn(dateUtils.getdate());
                aptType.setUpdatedOn(dateUtils.getdate());
                aptType.setUpdatedBy(999999l);
                aptType.setLocationId(locatinId);
                aptType = aptTypeRepo.saveAndFlush(aptType);
                return aptType;
            }
        } catch (Exception e) {
            logger.error("{}Excep:findAppotintmentType:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }
}

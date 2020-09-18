/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2.model.TblAppointment;
import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblRoom;
import com.vm.qsmart2.model.TblServiceBooked;
import com.vm.qsmart2.model.TblToken;
import com.vm.qsmart2.utils.ApptStatus;
import com.vm.qsmart2.utils.CdrLog;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2.utils.TokenStatus;
import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.token.ServingTokenRequest;
import com.vm.qsmart2api.dtos.token.TokenDTO;
import com.vm.qsmart2api.dtos.token.TokensInfoResponse;
import com.vm.qsmart2api.enums.AuditMessage;
import com.vm.qsmart2api.enums.AuditStatus;
import com.vm.qsmart2api.repository.AppointmentRepository;
import com.vm.qsmart2api.repository.BranchRepository;
import com.vm.qsmart2api.repository.RoomNoRepository;
import com.vm.qsmart2api.repository.ServiceBookedRepository;
import com.vm.qsmart2api.repository.TokenRepository;
import com.vm.qsmart2api.repository.UserProfileRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ashok
 */
@Service
public class PharmacyRegistrationTokenServingService {

    private final Logger logger = LogManager.getLogger(PharmacyRegistrationTokenServingService.class);

    @Autowired
    UserProfileRepository userProfielRepository;

    @Autowired
    TokenRepository tokenRepositary;

    @Autowired
    DateUtils dateUtils;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    BranchRepository branchRepository;

    @Value("${hl7.cdr.queue.name:Q2_HL7_CDR}")
    private String cdrQueueName;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ServiceBookedRepository serviceBookedRepositary;

    @Autowired
    RoomNoRepository roomNoRepositary;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditService auditService;

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    public TokensInfoResponse getTokensInfoByBranchId(String header, Long userId, Long branchId) {
        if (isLogEnabled) {
            logger.info("{}>>:branchId:[{}]", header, branchId);
        }
        try {
            Date fromDate = dateUtils.dayStartDateTime();
            Date toDate = dateUtils.dayEndDateTime();
            if (isLogEnabled) {
                logger.info("{}>>Info:from:[{}],to:[{}]", header, fromDate, toDate);
            }
            List<TokenDTO> waitTokens = serviceBookedRepositary.getRegistratinWaitingTokensByBranchId(branchId, TokenStatus.PRWAIT.getValue(), fromDate, toDate);
            //List<TokenDTO> servingTokens = serviceBookedRepositary.getRegistrationServingTokensByBranchIdNdServedBy(branchId, TokenStatus.RSRNG.getValue(), userId, fromDate, toDate);
            List<TokenDTO> servingTokens = serviceBookedRepositary.getRegistrationServingTokensByBranchIdNdServedBy(branchId, TokenStatus.PRSRNG.getValue(), ApptStatus.SERVING.getValue(), userId, fromDate, toDate);
            return new TokensInfoResponse(waitTokens, servingTokens);
        } catch (Exception e) {
            logger.error("{}Excep:getTokensInfoByBranchId:userId:{},branchId:{},Error:{}", header, userId, branchId, ExceptionUtils.getRootCauseMessage(e));
            return new TokensInfoResponse(new ArrayList<>(), new ArrayList<>());
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
            if (token.getRoomNo() != null) {
                log.setRoomId(token.getRoomNo());
            }
            log.setDrId(serviceBooked.getDrId());
            log.setServedBy(servedBy);
            log.setLocationId(serviceBooked.getAppointment().getLocationId());
            log.setServiceId(serviceBooked.getService().getServiceId());
            log.setTransId(token.getAppaitment().getTranId());
            log.setPatienId(token.getAppaitment().getPatientId());
            log.setBranchId(serviceBooked.getService().getBranchId());
            log.setCheckOutTime(checkOutTime != null ? DateUtils.sdf.get().format(checkOutTime) : null);
            log.setServingState(token.getStatus().equalsIgnoreCase(TokenStatus.SRNG.getValue()));
            //  log.setFirstCallTime(DateUtils.sdf.format(firstCallTime));
            return log;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:generateCdrLog:TblToken:{},servedBy:{},ServiceBooked:{},firstCallTime:{},starttime:{},checkouttime:{},Error:{}",
                    header, token, servedBy, serviceBooked, firstCallTime, strtTime, checkOutTime, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }

    }

    private void sendCdrReportToCdrQueue(String header, CdrLog log) {
        try {
            String cdr = mapper.writeValueAsString(log);
            jmsTemplate.convertAndSend(cdrQueueName, cdr);
            if (isLogEnabled) {
                logger.info("{}>>sendToQueue:[{}]:Successfully,Cdr:[{}]:", header, cdr, cdrQueueName);
            }
        } catch (Exception e) {
            logger.error("{}Excep:updateAppointmentCheckInDone:cdrLog:{},Error:{}", header, log, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private ResponseEntity<CustomResponse> callTokenForServing(String header, ServingTokenRequest servintToken,
            long userId, Locale locale, boolean isRecall) {
        CustomResponse response = null;
        TblRoom room = null;
        Date fromDate = null;
        Date toDate = null;
        TblToken token = null;
        Date strtTime = null;
        Date firstCallTime = null;
        CdrLog log = null;
        TblServiceBooked serviceBooked = null;
        List<String> status = new ArrayList<String>() {
            {
                add(TokenStatus.SRNG.getValue());
            }

            {
                add(TokenStatus.VSRNG.getValue());
            }

            {
                add(TokenStatus.RSRNG.getValue());
            }

            {
                add(TokenStatus.LSRNG.getValue());
            }

            {
                add(TokenStatus.LRSRNG.getValue());
            }

            {
                add(TokenStatus.PHSRNG.getValue());
            }

            {
                add(TokenStatus.PRSRNG.getValue());
            }

            {
                add(TokenStatus.RGSRNG.getValue());
            }

            {
                add(TokenStatus.RRSRNG.getValue());
            }

            {
                add(TokenStatus.NCSRNG.getValue());
            }

            {
                add(TokenStatus.NRSRNG.getValue());
            }
        };
        status.add(TokenStatus.SRNG.getValue());
        status.add(TokenStatus.VSRNG.getValue());
        status.add(TokenStatus.RSRNG.getValue());
        try {
            if (isLogEnabled) {
                logger.info("{}>>ServedBy:{},ServingRequest:{}", header, userId, servintToken);
            }
            fromDate = dateUtils.dayStartDateTime();
            toDate = dateUtils.dayEndDateTime();
            room = roomNoRepositary.findByRoomNoId(servintToken.getRoomId());
            if (room != null) {
                int maxAllowedRoomSize = room.getRoomMaster().getMaxAllowedToken();
                int servingTokensCount = tokenRepositary.findNoOfTokensServingInRoom(servintToken.getRoomId(), status, fromDate, toDate);
                if (maxAllowedRoomSize > servingTokensCount) {
                    token = tokenRepositary.findTokenById(servintToken.getTokenId(), TokenStatus.PRWAIT.getValue());
                    if (token != null) {
                        strtTime = token.getUpdatedOn();
                        token.setUpdatedOn(dateUtils.getdate());
                        token.setStatus(TokenStatus.PRSRNG.getValue());
                        if (token.getFirstCall() == null) {
                            token.setFirstCall(1);
                            firstCallTime = dateUtils.getdate();
                            //token.setStartTime(firstCallTime);
                        }
                        token.setRoomNo(servintToken.getRoomId());
                        token.setServedBy(userId);
                        tokenRepositary.saveAndFlush(token);
                        //need to implement the token vist detail logic
                        serviceBooked = serviceBookedRepositary.findFirstBytranIdOrderBystartTimeAsc(token.getAppaitment().getTranId()).get(0);
                        log = generateCdrLog(header, TokenStatus.PRSRNG, token, userId, serviceBooked, firstCallTime, strtTime, null);
                        if (log != null) {
                            sendCdrReportToCdrQueue(header, log);
                        }
                        serviceBooked.setServiceStatus(ApptStatus.SERVING.getValue());
                        serviceBookedRepositary.saveAndFlush(serviceBooked);
                        if (isRecall) {
                            response = new CustomResponse(true, messageSource.getMessage("token.ctrl.recall.success", null, locale));
                            logger.info("{}<<:Response:[{}]", header, response);
                            auditService.saveAuditDetails(header, userId, AuditMessage.RecallSuccess.getMsg(), AuditStatus.SUCCESS);
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        } else {
                            response = new CustomResponse(true, messageSource.getMessage("token.ctrl.serving.success", null, locale));
                            logger.info("{}<<:Response:[{}]", header, response);
                            auditService.saveAuditDetails(header, userId, AuditMessage.ServingSuccess.getMsg(), AuditStatus.SUCCESS);
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        }
                    } else {
                        response = new CustomResponse(false, messageSource.getMessage("token.ctrl.waiting.notexist", null, locale));
                        logger.info("{}<<:Response:[{}]", header, response);
                        auditService.saveAuditDetails(header, userId, AuditMessage.WaitingTokenNotExist.getMsg(), AuditStatus.FAILURE);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    if (isLogEnabled) {
                        logger.info("{}<<MaxAllowedTOkenReached:MaxAllowSize:{},ServingTokensInRoom:{},ServedBy:{},ServingRequest:{}", header, maxAllowedRoomSize, servingTokensCount, userId, servintToken);
                    }
                    response = new CustomResponse(false, messageSource.getMessage("token.ctrl.serving.maxreadched", null, locale));
                    logger.info("{}<<:Response:[{}]", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.MaxAllowedReached.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}<<InvalidRoomId,ServedBy:{},ServingRequest:{}", header, userId, servintToken);
                }
                response = new CustomResponse(false, messageSource.getMessage("token.ctrl.serving.invalidroom", null, locale));
                logger.info("{}<<:Response:[{}]", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.InvalidRoomInfo.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:callTokenForServing:servintTokenRequest:{},userId:{},Error:{}", header, servintToken, userId, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ActionFailed.getMsg(), AuditStatus.FAILURE);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            response = null;
            room = null;
            fromDate = null;
            toDate = null;
            token = null;
            strtTime = null;
            firstCallTime = null;
            log = null;
            serviceBooked = null;
        }
    }

    private ResponseEntity<CustomResponse> perfromRecallAction(String header,
            ServingTokenRequest servintToken, long userId, Locale locale) {
        TblToken token = null;
        CustomResponse response = null;
        CdrLog log = null;
        TblServiceBooked serviceBooked = null;
        Date strtTime = null;
        TokenStatus staus = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>ServedBy:{},ServingRequest:{},ActionType:{}", header, userId, servintToken);
            }
            token = tokenRepositary.findTokenById(servintToken.getTokenId(), TokenStatus.PRSRNG.getValue());
            if (token != null) {
                strtTime = token.getUpdatedOn();
                token.setUpdatedOn(dateUtils.getdate());
                token.setStatus(TokenStatus.PRWAIT.getValue());
                token.setRoomNo(servintToken.getRoomId());
                staus = TokenStatus.RCAL;

                token = tokenRepositary.saveAndFlush(token);
                serviceBooked = serviceBookedRepositary.findFirstBytranIdOrderBystartTimeAsc(token.getAppaitment().getTranId()).get(0);
                log = generateCdrLog(header, staus, token, userId, serviceBooked, null, strtTime, null);
                if (log != null) {
                    sendCdrReportToCdrQueue(header, log);
                }
                serviceBooked.setServiceStatus(ApptStatus.WAITING.getValue());
                serviceBookedRepositary.saveAndFlush(serviceBooked);
                //need to implement the token vist detail logic
                return callTokenForServing(header, servintToken, userId, locale, true);
            } else {
                response = new CustomResponse(false, messageSource.getMessage("token.ctrl.serving.notexist", null, locale));
                logger.info("{}<<:Response:[{}]", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServingTokenNotExist.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:perfromRecallAction:servintTokenRequest:{},userId:{},Error:{}", header, servintToken, userId, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ActionFailed.getMsg(), AuditStatus.FAILURE);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            serviceBooked = null;
            strtTime = null;
            staus = null;
        }
    }

    private ResponseEntity<CustomResponse> perfromReturnToWaitingQueueAction(String header,
            ServingTokenRequest servintToken, long userId, Locale locale, boolean isRecycleToken) {
        TblToken token = null;
        CustomResponse response = null;
        CdrLog log = null;
        TblServiceBooked serviceBooked = null;
        Date strtTime = null;
        TokenStatus staus = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>ServedBy:{},ServingRequest:{},ActionType:{}", header, userId, servintToken);
            }
            token = tokenRepositary.findTokenById(servintToken.getTokenId(), TokenStatus.PRSRNG.getValue());
            if (token != null) {
                strtTime = token.getUpdatedOn();
                token.setUpdatedOn(dateUtils.getdate());
                token.setStatus(TokenStatus.PRWAIT.getValue());
                token.setRoomNo(servintToken.getRoomId());
                if (isRecycleToken) {
                    staus = TokenStatus.RCYL;
                } else {
                    staus = TokenStatus.NSHW;
                    token.setNoShow(token.getNoShow() + 1);
                }
                token = tokenRepositary.saveAndFlush(token);
                serviceBooked = serviceBookedRepositary.findFirstBytranIdOrderBystartTimeAsc(token.getAppaitment().getTranId()).get(0);
                log = generateCdrLog(header, staus, token, userId, serviceBooked, null, strtTime, null);
                if (log != null) {
                    sendCdrReportToCdrQueue(header, log);
                }
                serviceBooked.setServiceStatus(ApptStatus.WAITING.getValue());
                serviceBookedRepositary.saveAndFlush(serviceBooked);
                //need to implement the token vist detail logic
                response = new CustomResponse(true, messageSource.getMessage("token.ctrl.wait.success", null, locale));
                logger.info("{}<<:Response:[{}]", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.MoveToWaitQueue.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new CustomResponse(false, messageSource.getMessage("token.ctrl.serving.notexist", null, locale));
                logger.info("{}<<:Response:[{}]", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServingTokenNotExist.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:perfromReturnToWaitingQueueAction:servintTokenRequest:{},userId:{},Error:{}", header, servintToken, userId, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ActionFailed.getMsg(), AuditStatus.FAILURE);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            serviceBooked = null;
            strtTime = null;
            staus = null;
        }
    }

    private ResponseEntity<CustomResponse> perfromServedAction(String header, ServingTokenRequest servintToken, long userId, Locale locale) {
        TblToken token = null;
        CustomResponse response = null;
        TblServiceBooked serviceBooked = null;
        TokenStatus status = null;
        Date strtTime = null;
        CdrLog log = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>ServedBy:{},ServingRequest:{},ActionType:{}", header, userId, servintToken);
            }
            token = tokenRepositary.findTokenById(servintToken.getTokenId(), TokenStatus.PRSRNG.getValue());
            if (token != null) {
                strtTime = token.getUpdatedOn();
                //String tranId = token.getAppaitment().getTranId();
                List<TblServiceBooked> bookedServices = serviceBookedRepositary.findFirstBytranIdOrderBystartTimeAsc(token.getAppaitment().getTranId());
                if (bookedServices != null && !bookedServices.isEmpty()) {
                    token.setUpdatedOn(dateUtils.getdate());
                    TblBranch branch = branchRepository.getOne(bookedServices.get(0).getService().getBranchId());
                    token.setStatus(TokenStatus.PHWAIT.getValue());
                    status = TokenStatus.PSRVD;
                    token.setRoomNo(servintToken.getRoomId());
                    tokenRepositary.saveAndFlush(token);
                    // serviceBooked = serviceBookedRepositary.findFirstBytranIdOrderBystartTimeAsc(token.getAppaitment().getTranId()).get(0);
                    log = generateCdrLog(header, status, token, userId, bookedServices.get(0), null, strtTime, token.getUpdatedOn());
                    if (log != null) {
                        sendCdrReportToCdrQueue(header, log);
                    }
                    //TODO to update 
//                serviceBooked.setServiceStatus(ApptStatus.WAITING.getValue());
//                serviceBookedRepositary.saveAndFlush(serviceBooked);
                    serviceBookedRepositary.updateServiceBookedByTranId(ApptStatus.WAITING.getValue(), token.getAppaitment().getTranId(), new ArrayList<String>() {
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
                    response = new CustomResponse(true, messageSource.getMessage("token.ctrl.served.success", null, locale));
                    logger.info("{}<<:Response:[{}]", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.ServedSuccess.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    response = new CustomResponse(false, messageSource.getMessage("token.ctrl.serving.notexist", null, locale));
                    logger.info("{}<<:Response:[{}]", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.ServingTokenNotExist.getMsg(), AuditStatus.SUCCESS);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("token.ctrl.serving.notexist", null, locale));
                logger.info("{}<<:Response:[{}]", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServingTokenNotExist.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:perfromServedAction:servintTokenRequest:{},userId:{},Error:{}", header, servintToken, userId, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ActionFailed.getMsg(), AuditStatus.FAILURE);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    public ResponseEntity<CustomResponse> performActionOnTOken(String header, ServingTokenRequest servintToken, long userId, Locale locale, String actionType) {
        CustomResponse response = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>ServedBy:{},ServingRequest:{},ActionType:{}", header, userId, servintToken, actionType);
            }
            switch (actionType.toUpperCase()) {
                case "SERVING":
                    return callTokenForServing(header, servintToken, userId, locale, false);
                case "NOSHOW":
                    return perfromReturnToWaitingQueueAction(header, servintToken, userId, locale, false);
                case "RECYCLE":
                    return perfromReturnToWaitingQueueAction(header, servintToken, userId, locale, true);
                case "RECALL":
                    return perfromRecallAction(header, servintToken, userId, locale);
                case "SERVED":
                    return perfromServedAction(header, servintToken, userId, locale);
                case "END":
                    return perfromENDAction(header, servintToken, userId, locale);
                default:
                    if (isLogEnabled) {
                        logger.info("{}<<InvalidActionType:{},ServedBy:{},ServingRequest:{}", header, actionType, userId, servintToken);
                    }
                    response = new CustomResponse(false, messageSource.getMessage("toekn.ctrl.invalid.action", null, locale));
                    logger.info("{}<<:Response:[{}]", header, response);
                    auditService.saveAuditDetails(header, userId, AuditMessage.InvalidAction.getMsg(), AuditStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:performActionOnTOken:servintTokenRequest:{},userId:{},Error:{}", header, servintToken, userId, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ActionFailed.getMsg(), AuditStatus.FAILURE);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            response = null;
        }
    }

    @Autowired
    AppointmentRepository apptRepo;

    private ResponseEntity<CustomResponse> perfromENDAction(String header, ServingTokenRequest servintToken, long userId, Locale locale) {
        TblToken token = null;
        CustomResponse response = null;
        TblServiceBooked serviceBooked = null;
        TokenStatus status = null;
        Date strtTime = null;
        CdrLog log = null;
        TblAppointment tblAppt = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>ServedBy:{},ServingRequest:{}", header, userId, servintToken);
            }
            token = tokenRepositary.findTokenByIdWithStatusList(servintToken.getTokenId(), new ArrayList<String>() {
                {
                    add(TokenStatus.PRSRNG.getValue());
                }

                {
                    add(TokenStatus.PRWAIT.getValue());
                }
            }
            );
            if (token != null) {
                strtTime = token.getUpdatedOn();
                //List<TblServiceBooked> bookedServices = serviceBookedRepositary.getServiceBookedByApptId(tranId, servintToken.getServiceId(), 1);
                serviceBooked = serviceBookedRepositary.findFirstBytranIdOrderBystartTimeAsc(token.getAppaitment().getTranId()).get(0);
                token.setUpdatedOn(dateUtils.getdate());
//                if (bookedServices != null && !bookedServices.isEmpty()) {
//                    token.setStatus(TokenStatus.RWAIT.getValue());
//                    status = TokenStatus.END;
//                } else {
                token.setStatus(TokenStatus.END.getValue());
                status = TokenStatus.END;
//                }
                //token.setRoomNo(servintToken.getRoomId());
                tokenRepositary.saveAndFlush(token);
                tblAppt = serviceBooked.getAppointment();
                serviceBookedRepositary.updateServiceBookedByTranId(ApptStatus.FORCECHECKOUT.getValue(), token.getAppaitment().getTranId(), new ArrayList<String>() {
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
                tblAppt.setApptStatus(ApptStatus.FORCECHECKOUT.getValue());
                apptRepo.saveAndFlush(tblAppt);
                log = generateCdrLog(header, status, token, userId, serviceBooked, null, strtTime, token.getUpdatedOn());
                if (log != null) {
                    sendCdrReportToCdrQueue(header, log);
                }
                response = new CustomResponse(true, messageSource.getMessage("token.ctrl.end.success", null, locale));
                logger.info("{}<<:Response:[{}]", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServedSuccess.getMsg(), AuditStatus.SUCCESS);
                return ResponseEntity.status(HttpStatus.OK).body(response);

            } else {
                response = new CustomResponse(false, messageSource.getMessage("token.ctrl.serving-waiting.notexist", null, locale));
                logger.info("{}<<:Response:[{}]", header, response);
                auditService.saveAuditDetails(header, userId, AuditMessage.ServingWatingTokenNotExist.getMsg(), AuditStatus.FAILURE);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:perfromENDAction:servintTokenRequest:{},userId:{},Error:{}", header, servintToken, userId, ExceptionUtils.getRootCauseMessage(e));
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            auditService.saveAuditDetails(header, userId, AuditMessage.ActionFailed.getMsg(), AuditStatus.FAILURE);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}

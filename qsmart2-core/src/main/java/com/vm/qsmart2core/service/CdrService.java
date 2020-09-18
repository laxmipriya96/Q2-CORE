/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.service;

import com.vm.qsmart2.model.TblVisitDetail;
import com.vm.qsmart2.model.TblVisitSummary;
import com.vm.qsmart2.utils.CdrLog;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2core.repositary.VisitDetailsRepositary;
import com.vm.qsmart2core.repositary.VisitSummaryRepositary;
import java.util.Date;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Phani
 */
@Service
public class CdrService {

    private static final Logger logger = LogManager.getLogger(CdrService.class);

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    @Autowired
    VisitDetailsRepositary visitDetailRepositary;

    @Autowired
    VisitSummaryRepositary visitSummaryRepositary;

    @Autowired
    DateUtils dateUtils;

    public void processCdrLog(String header, CdrLog cdr) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>CdrLog:{}", header, cdr);
            }
            switch (cdr.getStatus()) {
                case CHECKIN:
                    saveCheckInCdrInVisitSummaryDetails(header, cdr);
                    break;
                case SRNG:
                case RSRNG:
                case VSRNG:
                    updateServingStateToVisitSummaryDetails(header, cdr);
                    break;
                case NSHW:
                case RCYL:
                case RCAL:
                case RSRVD:
                case VSRVD:
                    updateNoShowStateToVisitSummaryDetails(header, cdr);
                    break;
                case PSRVD:
                    updatePartialServedStateToVisitSummaryDetails(header, cdr);
                    break;
                case SRVD:
                case END:
                    updateServedStateToVisitSummaryDetails(header, cdr);
                    break;
                default:
                    updateServedStateToVisitSummaryDetails(header, cdr);
                    break;
            }
        } catch (Exception e) {
            logger.error("{}Excep:processCdrLog:Cdr:{}:Error:{}", header, cdr, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void saveCheckInCdrInVisitSummaryDetails(String header, CdrLog cdr) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>CdrLog:{}", header, cdr);
            }
            TblVisitSummary vistSummaryObj = new TblVisitSummary();
            vistSummaryObj.setBranchId(cdr.getBranchId());
            vistSummaryObj.setStartTime(dateUtils.convertDtStringToDate(cdr.getStartTime()));
            vistSummaryObj.setEndTime(dateUtils.convertDtStringToDate(cdr.getEndTime()));
            vistSummaryObj.setCheckInTime(dateUtils.convertDtStringToDate(cdr.getCheckInTime()));
            vistSummaryObj.setLocationId(cdr.getLocationId());
            vistSummaryObj.setPatientId(cdr.getPatienId());
            vistSummaryObj.setTransId(cdr.getTransId());
            vistSummaryObj.setTotalWaitTime(0);
            vistSummaryObj.setTotalCareTime(0);
            visitSummaryRepositary.saveAndFlush(vistSummaryObj);
        } catch (Exception e) {
            logger.error("{}Excep:saveCheckInCdrInVisitSummaryDetails:Cdr:{}:Error:{}", header, cdr, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void updateServingStateToVisitSummaryDetails(String header, CdrLog cdr) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>CdrLog:{}", header, cdr);
            }
            TblVisitSummary vistSummaryObj = visitSummaryRepositary.findVisitSummaryObjByTranID(cdr.getTransId());
            if (vistSummaryObj != null) {
                if (vistSummaryObj.getFirstCallTime() == null) {
                    vistSummaryObj.setFirstCallTime(cdr.getFirstCallTime() != null && !cdr.getFirstCallTime().isEmpty()
                            ? dateUtils.convertDtStringToDate(cdr.getFirstCallTime()) : null);
                }
                vistSummaryObj.setTotalWaitTime(vistSummaryObj.getTotalWaitTime()
                        + (caluclateWaitTime(
                                dateUtils.convertDtStringToDate(cdr.getStartTime()),
                                dateUtils.convertDtStringToDate(cdr.getEndTime()))));
                visitSummaryRepositary.saveAndFlush(vistSummaryObj);
            } else {
                if (isLogEnabled) {
                    logger.info("{}>>VisitSummaryObj:NotExist:CdrLog:{}", header, cdr);
                }
            }
        } catch (Exception e) {
            logger.error("{}Excep:updateVisitSummaryDetails:Cdr:{}:Error:{}", header, cdr, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public TblVisitDetail saveVisitDetailObj(String header, CdrLog cdr) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>CdrLog:{}", header, cdr);
            }
            TblVisitDetail visitDetailObj = new TblVisitDetail();
            visitDetailObj.setBranchId(cdr.getBranchId());
            visitDetailObj.setLocationId(cdr.getLocationId());
            visitDetailObj.setStartTime(dateUtils.convertDtStringToDate(cdr.getStartTime()));
            visitDetailObj.setEndTime(dateUtils.convertDtStringToDate(cdr.getEndTime()));
            visitDetailObj.setRoomNo(cdr.getRoomId());
            visitDetailObj.setServedBy(cdr.getServedBy());
            visitDetailObj.setServiceId(cdr.getServiceId());
            visitDetailObj.setStatus(cdr.getStatus().getValue());
            visitDetailObj.setTransId(cdr.getTransId());
            visitDetailObj.setDrId(cdr.getDrId());
            return visitDetailRepositary.saveAndFlush(visitDetailObj);
        } catch (Exception e) {
            logger.error("{}Excep:updateVisitSummaryDetails:Cdr:{}:Error:{}", header, cdr, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }

    public int caluclateWaitTime(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();
        int diffmin = (int) (diff / (60 * 1000));
        return diffmin;
    }

    public int caluclateServeTime(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();
        int diffmin = (int) (diff / (60 * 1000));
        return diffmin;
    }

    public void updateNoShowStateToVisitSummaryDetails(String header, CdrLog cdr) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>CdrLog:{}", header, cdr);
            }
            TblVisitDetail visitDetailObj = saveVisitDetailObj(header, cdr);
            if (visitDetailObj != null) {
                TblVisitSummary vistSummaryObj = visitSummaryRepositary.findVisitSummaryObjByTranID(visitDetailObj.getTransId());
                if (vistSummaryObj != null) {
                    vistSummaryObj.setTotalCareTime(vistSummaryObj.getTotalCareTime()
                            + (caluclateServeTime(
                                    dateUtils.convertDtStringToDate(cdr.getStartTime()),
                                    dateUtils.convertDtStringToDate(cdr.getEndTime()))));
                    visitSummaryRepositary.saveAndFlush(vistSummaryObj);
                } else {
                    if (isLogEnabled) {
                        logger.info("{}>>VisitSummaryObj:NotExist:CdrLog:[{}]", header, cdr);
                    }

                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}>>visitDetailObj:NotExist:CdrLog:[{}]", header, cdr);
                }
            }
        } catch (Exception e) {
            logger.error("{}Excep:updateNoShowStateToVisitSummaryDetails:Cdr:{}:Error:{}", header, cdr, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void updatePartialServedStateToVisitSummaryDetails(String header, CdrLog cdr) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>CdrLog:{}", header, cdr);
            }
            TblVisitDetail visitDetailObj = saveVisitDetailObj(header, cdr);
            if (visitDetailObj != null) {
                TblVisitSummary vistSummaryObj = visitSummaryRepositary.findVisitSummaryObjByTranID(visitDetailObj.getTransId());
                if (vistSummaryObj != null) {
                    if (cdr.isServingState()) {
                        vistSummaryObj.setTotalCareTime(vistSummaryObj.getTotalCareTime()
                                + (caluclateServeTime(
                                        dateUtils.convertDtStringToDate(cdr.getStartTime()),
                                        dateUtils.convertDtStringToDate(cdr.getEndTime()))));

                    }
                    visitSummaryRepositary.saveAndFlush(vistSummaryObj);
                } else {
                    if (isLogEnabled) {
                        logger.info("{}>>VisitSummaryObj:NotExist:CdrLog:[{}]", header, cdr);
                    }
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}>>visitDetailObj:NotExist:CdrLog:[{}]", header, cdr);
                }
            }
        } catch (Exception e) {
            logger.error("{}Excep:updatePartialServedStateToVisitSummaryDetails:Cdr:{}:Error:{}", header, cdr, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void updateServedStateToVisitSummaryDetails(String header, CdrLog cdr) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>CdrLog:{}", header, cdr);
            }
            TblVisitDetail visitDetailObj = saveVisitDetailObj(header, cdr);
            if (visitDetailObj != null) {
                TblVisitSummary vistSummaryObj = visitSummaryRepositary.findVisitSummaryObjByTranID(visitDetailObj.getTransId());
                if (vistSummaryObj != null) {
                    if (cdr.isServingState()) {
                        vistSummaryObj.setTotalCareTime(vistSummaryObj.getTotalCareTime()
                                + (caluclateServeTime(
                                        dateUtils.convertDtStringToDate(cdr.getStartTime()),
                                        dateUtils.convertDtStringToDate(cdr.getEndTime()))));
                    }
                    //vistSummaryObj.setEndTime(dateUtils.convertDtStringToDate(cdr.getEndTime()));
                    vistSummaryObj.setCheckOutTime(dateUtils.convertDtStringToDate(cdr.getCheckOutTime()));
                    visitSummaryRepositary.saveAndFlush(vistSummaryObj);
                } else {
                    if (isLogEnabled) {
                        logger.info("{}>>VisitSummaryObj:NotExist:CdrLog:[{}]", header, cdr);
                    }
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}>>visitDetailObj:NotExist:CdrLog:[{}]", header, cdr);
                }
            }
        } catch (Exception e) {
            logger.error("{}Excep:updateNoShowStateToVisitSummaryDetails:Cdr:{}:Error:{}", header, cdr, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void updateEndStateToVisitSummaryDetails(String header, CdrLog cdr) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>CdrLog:{}", header, cdr);
            }
            TblVisitDetail visitDetailObj = saveVisitDetailObj(header, cdr);
            if (visitDetailObj != null) {
                TblVisitSummary vistSummaryObj = visitSummaryRepositary.findVisitSummaryObjByTranID(visitDetailObj.getTransId());
                if (vistSummaryObj != null) {
                    //vistSummaryObj.setEndTime(dateUtils.convertDtStringToDate(cdr.getEndTime()));
                    vistSummaryObj.setCheckOutTime(dateUtils.convertDtStringToDate(cdr.getCheckOutTime()));
                    visitSummaryRepositary.saveAndFlush(vistSummaryObj);
                } else {
                    if (isLogEnabled) {
                        logger.info("{}>>VisitSummaryObj:NotExist:CdrLog:[{}]", header, cdr);
                    }

                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}>>visitDetailObj:NotExist:CdrLog:[{}]", header, cdr);
                }
            }
        } catch (Exception e) {
            logger.error("{}Excep:updateNoShowStateToVisitSummaryDetails:Cdr:{}:Error:{}", header, cdr, ExceptionUtils.getRootCauseMessage(e));
        }
    }
}

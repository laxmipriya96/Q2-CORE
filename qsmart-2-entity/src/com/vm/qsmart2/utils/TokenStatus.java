/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.utils;

/**
 *
 * @author Phani
 */
public enum TokenStatus {

    CHECKIN("CHECKIN"),
    WAIT("WAITING"), SRNG("SERVING"), NSHW("NOSHOW"), RCAL("RECALL"),
    RCYL("RECYCLE"), SRVD("SERVED"), TRSR("TRANSFER"),
    RTRS("R-TRANSFER"), PSRVD("P-SERVED"), FLOTING("FLOTING"),
    END("END"),
    RWAIT("R-WAITING"), RSRNG("R-SERVING"), RSRVD("R-SERVED"),
    VWAIT("V-WAITING"), VSRNG("V-SERVING"), VSRVD("V-SERVED"),
    PHWAIT("PH-WAITING"), PHSRNG("PH-SERVING"), PHSRVD("PH-SERVED"),
    PRWAIT("PR-WAITING"), PRSRNG("PR-SERVING"), PRSRVD("PR-SERVED"),
    LWAIT("L-WAITING"), LSRNG("L-SERVING"), LSRVD("L-SERVED"),
    LRWAIT("LR-WAITING"), LRSRNG("LR-SERVING"), LRSRVD("LR-SERVED"),
    RGWAIT("RG-WAITING"), RGSRNG("RG-SERVING"), RGSRVD("RG-SERVED"),
    RRWAIT("RR-WAITING"), RRSRNG("RR-SERVING"), RRSRVD("RR-SERVED"),
    NCWAIT("NC-WAITING"), NCSRNG("NC-SERVING"), NCSRVD("NC-SERVED"),
    NRWAIT("NR-WAITING"), NRSRNG("NR-SERVING"), NRSRVD("NR-SERVED");

    private String value;

    private TokenStatus(String status) {
        this.value = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static TokenStatus getInstce(String value) {
        switch (value) {
            case "CHECKIN":
                return CHECKIN;
            case "WAITING":
                return WAIT;
            case "SERVING":
                return SRNG;
            case "NOSHOW":
                return NSHW;
            case "RECYCLE":
                return RCYL;
            case "RECALL":
                return RCAL;
            case "SERVED":
                return SRVD;
            case "TRANSFER":
                return TRSR;
            case "R-TRANSFER":
                return RTRS;
            case "P-SERVED":
                return PSRVD;
            case "FLOTING":
                return FLOTING;
            case "END":
                return END;
            case "R-WAITING":
                return RWAIT;
            case "R-SERVING":
                return RSRNG;
            case "R-SERVED":
                return RSRVD;
            case "V-WAITING":
                return VWAIT;
            case "V-SERVING":
                return VSRNG;
            case "V-SERVED":
                return VSRVD;
            case "PH-WAITING":
                return PHWAIT;
            case "PH-SERVING":
                return PHSRNG;
            case "PH-SERVED":
                return PHSRVD;
            case "PR-WAITING":
                return PRWAIT;
            case "PR-SERVING":
                return PRSRNG;
            case "PR-SERVED":
                return PRSRVD;
            case "L-WAITING":
                return LWAIT;
            case "L-SERVING":
                return LSRNG;
            case "L-SERVED":
                return LSRVD;
            case "LR-WAITING":
                return LRWAIT;
            case "LR-SERVING":
                return LRSRNG;
            case "LR-SERVED":
                return LRSRVD;
                 case "RG-WAITING":
                return RGWAIT;
            case "RG-SERVING":
                return RGSRNG;
            case "RG-SERVED":
                return RGSRVD;
            case "RR-WAITING":
                return RRWAIT;
            case "RR-SERVING":
                return RRSRNG;
            case "RR-SERVED":
                return RRSRVD;
            case "NC-WAITING":
                return NCWAIT;
            case "NC-SERVING":
                return NCSRNG;
            case "NC-SERVED":
                return NCSRVD;
            case "NR-WAITING":
                return NRWAIT;
            case "NR-SERVING":
                return NRSRNG;
            case "NR-SERVED":
                return NRSRVD;
            default:
                return END;
        }
    }
}

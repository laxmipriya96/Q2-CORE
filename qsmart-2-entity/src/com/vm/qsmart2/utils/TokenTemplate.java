/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 *
 * @author Ashok
 */
public class TokenTemplate {

    public static String getTokenPrintingTemplate(TokenPrintData tokenPrintData, String template) {
        Map<String, String> map = new HashMap() {
            {
                put("BRANCH_EN", tokenPrintData.getBranchNameEn());
            }

            {
                put("BRANCH_AR", tokenPrintData.getBranchNameAr() == null ? " " : tokenPrintData.getBranchNameAr());
            }

            {
                put("APPOINTMENT_TIME", tokenPrintData.getAppointmentTime());
            }

            {
                put("TIME_NOW", tokenPrintData.getTimeNow());
            }

            {
                put("DATE", tokenPrintData.getDate());
            }

            {
                put("TOKEN_NO", tokenPrintData.getTokenNO());
            }

            {
                put("MRN_NO", tokenPrintData.getMrnNo());
            }
        };
        template = StrSubstitutor.replace(template, map, "{", "}");;
        return template;
    }

}

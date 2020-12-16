/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.CustomResponse;
import com.vm.qsmart2api.dtos.template.SurveryRequest;
import com.vm.qsmart2api.service.SurveyService;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Phani
 */
@RestController
@RequestMapping("survey-test/")
public class SurveyController {

    private static final Logger logger = LogManager.getLogger(SurveyController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    SurveyService surveyService;

    public String header = null;

    @PostMapping(path = "{userId}/send-link/{enterpriseId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CustomResponse> sendSurveryTestLink(Locale locale,
            @PathVariable("userId") int userId,
            @RequestHeader(value = "tranId", defaultValue = "Survery_Send_Link") String tranId,
            @RequestBody final SurveryRequest surveryRequest,
            @PathVariable("enterpriseId") final long enterpriseId) {

        StringBuilder sb = new StringBuilder();
        CustomResponse response = null;
        boolean isRequstValid = false;
        try {
            sb.append("[").append(tranId).append("_").append(userId).append("] ");
            header = sb.toString().toUpperCase();
            logger.info("{} >> userId:{},enterpriseId:{},SurveryRequest:{}", header, userId, enterpriseId, surveryRequest);
            //logger.info ("==== >> config map {}",  confi.getMmap());
            if (surveryRequest != null) {
                if (surveryRequest.getEmailId() != null && !surveryRequest.getEmailId().isEmpty()) {
                    logger.info("{}EmailId:[{}]:Exist:send:email", header, surveryRequest.getEmailId());
                    //to do implement the sending mail
                    new Thread() {
                        @Override
                        public void run() {
                            surveyService.sendSurveyLinkToEmail(header + "[" + surveryRequest.getEmailId() + "] ", enterpriseId, surveryRequest.getEmailId());
                        }

                    }.start();
                    isRequstValid = true;
                }
                if (surveryRequest.getMobileNumber() != null && !surveryRequest.getMobileNumber().isEmpty()) {
                    logger.info("{}MobileNo:[{}]:Exist:send:Sms", header, surveryRequest.getMobileNumber());
                    //to do implement the sending mail
                    new Thread() {
                        @Override
                        public void run() {
                            surveyService.sendPendingSms(header + "[" + surveryRequest.getMobileNumber() + "] ", enterpriseId, surveryRequest.getMobileNumber());
                        }
                    }.start();
                    isRequstValid = true;
                }
                if (surveryRequest.getWhatsappNumber() != null && !surveryRequest.getWhatsappNumber().isEmpty()) {
                    logger.info("{}WhatsappMobile:[{}]:Exist:whatsapp:Sms", header, surveryRequest.getWhatsappNumber());
                    //to do implement the sending mail
                    isRequstValid = true;
                    new Thread() {
                        @Override
                        public void run() {
                            surveyService.submitLinkToWhatsapp(header + "[" + surveryRequest.getWhatsappNumber() + "] ", surveryRequest.getWhatsappNumber());
                        }
                    }.start();
                }
                if (isRequstValid) {
                    response = new CustomResponse(true, messageSource.getMessage("survey.request.received", null, locale));
                    logger.info("{}<<:Response:[{}]", header, response);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    response = new CustomResponse(true, messageSource.getMessage("survery.request.inavalid", null, locale));
                    logger.info("{}<<:Response:[{}]", header, response);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response = new CustomResponse(false, messageSource.getMessage("survery.request.inavalid", null, locale));
                logger.info("{} <<:sendSurveryTestLink:Response:{}", header, response);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            logger.error("{}Excep:sendSurveryTestLink:userId:{}:enterpriseId:{}:SurveryRequest:{}:Error:{}", header, userId, enterpriseId, surveryRequest, e.getMessage());
            response = new CustomResponse(false, messageSource.getMessage("common.wrong.message", null, locale));
            logger.info("{}<<:Response:[{}]", header, response);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            response = null;
            sb = null;
            header = null;
        }

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2api.dtos.MsgResponse;
import com.vm.qsmart2api.dtos.template.AuthResponse;
import com.vm.qsmart2api.dtos.template.NotificationMsgRequest;
import com.vm.qsmart2api.dtos.template.NotificationPayload;
import com.vm.qsmart2api.dtos.template.TemplateComponent;
import com.vm.qsmart2api.dtos.template.TemplateMsgPayload;
import com.vm.qsmart2api.dtos.template.TemplateMsgRequest;
import com.vm.qsmart2api.dtos.template.TemplateNotification;
import com.vm.qsmart2api.dtos.template.TemplateParams;
import com.vm.qsmart2api.dtos.template.TemplatePayload;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Phani
 */
public class CallableWorker implements Callable<MsgResponse> {

    private static final Logger logger = LogManager.getLogger(CallableWorker.class);

    public RestTemplate restTemplate;

    //private MessageQueueDTO pdu;
    ObjectMapper mapper;

    private AuthResponse authentication;

    private boolean isLogEnabled;

    public String header;

    //private ConfigProp prop;
    private String msgType;
    private String vendorIdentifier;
    private String mobileNo;
    private String msg;
    private String templateNmae;
    private List<TemplateParams> msgParams;
    private String language;

    public CallableWorker() {
    }

    public CallableWorker(ObjectMapper maper, RestTemplate template, boolean isLogEnabled, String pHeader,
            AuthResponse auth, String msgType, String vIdentifier,
            String mobileNo, String msg, String templateName, List<TemplateParams> msgParams, String lang) {
        this.header = pHeader;
//        this.pdu = pdu;
        //this.fcmObj = new FcmPayload(pdu.getDeviceId(), new FcmData(pdu.getAppName(), pdu.getMessage()));
        this.mapper = maper;
        this.restTemplate = template;
        this.isLogEnabled = isLogEnabled;
        this.authentication = auth;
        this.msgType = msgType;
        this.mobileNo = mobileNo;
        this.vendorIdentifier = vIdentifier;
        this.msg = msg;
        this.templateNmae = templateName;
        this.msgParams = msgParams;
        this.language = lang;
    }

    @Override
    public MsgResponse call() {
        try {
            return postDataToFCMServer();
        } catch (Exception e) {
            logger.error("{}Excep:call:Error:MobileNo:[{}]:{}:Error:{}", header, mobileNo, ExceptionUtils.getRootCauseMessage(e));
            MsgResponse str = new MsgResponse(null, null, 0, false, null, ExceptionUtils.getRootCauseMessage(e), null, 0);
            return str;
        }
    }

    public MsgResponse postDataToFCMServer() throws JsonProcessingException {
        StringBuilder sb = new StringBuilder();
        try {
            if (isLogEnabled) {
                logger.info("{}>>MobileNo:[{}]", header, mobileNo);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-type", MediaType.APPLICATION_JSON.toString());
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            headers.add("Authorization", sb.append(authentication.getToken_type()).append(" ").append(authentication.getAccess_token()).toString());
            if (msgType.equalsIgnoreCase("Notification")) {
                NotificationMsgRequest notificationMsg = new NotificationMsgRequest(vendorIdentifier + ":" + mobileNo, new NotificationPayload(msg));
                String requestObj = mapper.writeValueAsString(notificationMsg);
                HttpEntity<String> request = new HttpEntity<>(requestObj, headers);
                if (isLogEnabled) {
                    logger.info("{}>>MobileNo:[{}]:Request Object:[{}]", header, mobileNo, request);
                }
                ResponseEntity<MsgResponse> result = restTemplate.postForEntity("https://api.messengerpeople.dev/messages", request, MsgResponse.class);
                if (isLogEnabled) {
                    logger.info("{}>>MobileNo:[{}]:Response Object:[{}]", header, mobileNo, result);
                }
                //String id, String messenger, int message_amount, boolean split, String status, String error, String hint, int submitStatus
                MsgResponse str = result.getBody();
                str.setSubmitStatus(1);
                str.setError("SUCESS");
                return str;
            } else {
                TemplateMsgRequest templaeMsg = new TemplateMsgRequest(vendorIdentifier + ":" + mobileNo, new TemplateMsgPayload(msgType, new TemplatePayload("notification", new TemplateNotification(templateNmae, language,
                        new ArrayList<TemplateComponent>() {
                    {
                        add(new TemplateComponent("body", msgParams));
                    }
                }
                ))));
                String requestObj = mapper.writeValueAsString(templaeMsg);
                logger.info("Request Obj:[{}]", requestObj);
                HttpEntity<String> request = new HttpEntity<String>(requestObj, headers);
                if (isLogEnabled) {
                    logger.info("{}>>MobileNo:[{}]:Request Object:[{}]", header, mobileNo, request);
                }
                ResponseEntity<MsgResponse> result = restTemplate.postForEntity("https://api.messengerpeople.dev/messages", request, MsgResponse.class);
                if (isLogEnabled) {
                    logger.info("{}>>MobileNo:[{}]:Response Object:[{}]", header, mobileNo, result);
                }
                MsgResponse str = result.getBody();
                str.setSubmitStatus(1);
                str.setError("SUCESS");
                return str;
            }
        } catch (HttpStatusCodeException he) {
            MsgResponse msg = mapper.readValue(he.getResponseBodyAsString(), MsgResponse.class);
            msg.setSubmitStatus(0);
            return msg;
        } catch (Exception e) {
            logger.error("{}Excep:postDataToFCMServer:Error:MobileNo:[{}]:Error:{}", header, mobileNo, ExceptionUtils.getRootCauseMessage(e));
            //String id, String messenger, int message_amount, boolean split, String status, String error, String hint, int submitStatus
            MsgResponse str = new MsgResponse(null, null, 0, false, null, ExceptionUtils.getRootCauseMessage(e), null, 0);
            return str;
        } finally {
            sb = null;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.vm.qsmart2.model.TblGlobalSetting;
import com.vm.qsmart2api.dtos.MsgResponse;
import com.vm.qsmart2api.dtos.global.MailServerSettings;
import com.vm.qsmart2api.dtos.global.QueryParam;
import com.vm.qsmart2api.dtos.global.SmsSettings;
import com.vm.qsmart2api.dtos.template.AuthResponse;
import com.vm.qsmart2api.dtos.template.TemplateParams;
import com.vm.qsmart2api.repository.GlobalSettingsRepositary;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Phani
 */
@Service
public class SurveyService {

    private static final Logger logger = LogManager.getLogger(SurveyService.class);

    @Autowired
    GlobalSettingsRepositary globalRepo;

    @Autowired
    ObjectMapper objMappaer;

    @Autowired
    RestTemplate template;

    public void sendSurveyLinkToEmail(String header, long enterpriseId, String toEmail) {
        try {
            logger.info("{} >> userId:{},enterpriseId:{},toEmail:{}", header, enterpriseId, toEmail);
            TblGlobalSetting globalSettings = globalRepo.getGlobalSettingsByType("SMTP  ", enterpriseId);
            if (globalSettings != null) {
                MailServerSettings data = objMappaer.readValue(globalSettings.getSettingJson(), MailServerSettings.class);
                if (data != null) {
                    sendMail(header, toEmail, data);
                } else {
                    logger.info("{}Enterprise:[{}]:UnabletoGetMailInfo", header, enterpriseId);
                }
            } else {
                logger.info("{}Enterprise:[{}]:MailServer:Not:Configured", header, enterpriseId);
            }
        } catch (Exception e) {
            logger.error("{}Excep:sendSurveyLinkToEmail:enterpriseId:{}:fromEmail:{}:Error:{}", header, enterpriseId, toEmail, e.getMessage());
        }
    }

    @Value("${mail.ssl.required:false}")
    public boolean isSSLRequired;

    @Value("${survey.email.subject:Survey Link}")
    public String subject = "Survery Link";

    @Value("${survey.sms.test:Thank you for visiting American Hospital. We would like to request your feedback to improve our service. Please click on <URL> to take the survey.}")
    public String smsMsg;

    public void sendMail(String header, String mail, MailServerSettings mailServerSettings) {
        logger.info("{}>>ToMail:[{}]:MailServerSettings:{}", header, mail, mailServerSettings);
        Properties prop = new Properties();
        prop.put("mail.smtp.host", mailServerSettings.getServerIp());
        prop.put("mail.smtp.port", mailServerSettings.getPort());
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailServerSettings.getEmailAccount(), mailServerSettings.getPassword());
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailServerSettings.getEmailAccount()));
            message.addRecipient(
                    MimeMessage.RecipientType.TO,
                    new InternetAddress(mail)
            );
            message.setSubject(subject);
            File file = ResourceUtils.getFile("config/index.html");
            logger.info("{} || File Found :[{}] file :[{}]", header, file.exists(), file);
            String result = new String(Files.readAllBytes(file.toPath()));
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(result, "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
            logger.info("{}<<ToMail:[{}]:Mail:Sent:Successfully", header, mail);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:sendMail:mail:{}:mailServerSettings:{}:Error:{}", mail, mailServerSettings, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void sendPendingSms(String header, long enterpriseId, String mobileNo) {
        logger.info("{}>>MobileNo:[{}]:EnterpriseId:{}", header, mobileNo, enterpriseId);
        TblGlobalSetting globalSettings = null;
        SmsSettings smsSettings = null;
        try {
            globalSettings = globalRepo.getGlobalSettingsByType("SMS", enterpriseId);
            if (globalSettings != null) {
                String jsonString = globalSettings.getSettingJson();
                smsSettings = objMappaer.readValue(jsonString, SmsSettings.class);
                if (smsSettings != null) {
                    sendSms(header, mobileNo, smsSettings);
                } else {
                    logger.info("{}>>MobileNo:[{}]:Enterprise:[{}]:UnabletoGetSmsApiInfo", header, mobileNo, enterpriseId);
                }
            } else {
                logger.info("{}MobileNo:[{}]:Enterprise:[{}]:SMS:Not:Configured", header, mobileNo, enterpriseId);
            }
        } catch (Exception e) {
            logger.error("{}Excep:sendPendingSms:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            globalSettings = null;
            smsSettings = null;
        }
    }

    private void sendSms(String header, String mobileNo, SmsSettings smsSettings) {
        logger.info("{}>>mobileNo:[{}]:SmsSettings:[{}]", header, mobileNo, smsSettings);
        try {
            StringBuilder url = new StringBuilder();
            StringBuilder param = new StringBuilder();
            url.append(smsSettings.getTypeOfApi()).append("://").append(smsSettings.getHost()).append("/").append(smsSettings.getPath());
            param.append("?");
            for (QueryParam queryParam : smsSettings.getQueryParameters()) {
                param.append(queryParam.getParamName()).append("=");
                String value = queryParam.getParamValue();
                switch (value) {
                    case "Message":
                        param.append(smsMsg).append("&");
                        break;
                    case "Mobile Number":
                        param.append(mobileNo).append("&");
                        break;
                    case "Data Coding":
                        param.append((smsMsg.getBytes(Charset.forName("UTF-8")).length > smsMsg.length() ? 8 : 1)).append("&");
                        break;
                    default:
                        param.append(value).append("&");
                }
            }
            url.append(param.deleteCharAt(param.length() - 1));
            logger.info("{}>>sendSms:url:[{}]", header, url);
            ResponseEntity<String> result = template.getForEntity(url.toString(), String.class);
            logger.info("{}SmsApiResponseCode:[{}]:Response:[{}]", header, result.getStatusCode(), result);
        } catch (Exception e) {
            logger.error("{}Excep:sendSms:MobileNo:{}:Message:{}smsSettings:{}:Error:{}", header, mobileNo, smsMsg, smsSettings, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    @Autowired
    ObjectMapper mapper;

    @Autowired
    RestTemplate restTemplate;


    @Autowired
    ListeningExecutorService listenerService;

    @Value("${whatsapp.clientApiKey:d4676dea73e063f6c94db40b}")
    private String clientApiKey;

    @Value("${whatsapp.clientSecretKey:b56cbae8c4680647caed99f60081c362}")
    private String clienSecretKey;

    @Value("${whatsapp.msgType:notification}")
    private String msgType;

    @Value("${whatsapp.clientId:ecb498dc-20c9-4649-84de-159ada3d96f3}")
    private String clientId;

    @Value("${whatsap.params}")
    private String msgParams;

    public void submitLinkToWhatsapp(String header1, String mobileNo) {
        StringBuilder sb = new StringBuilder();
        String header = null;

        try {
            header = sb.append(header1).append("[").append(mobileNo).append("] ").toString();
//            ObjectMapper maper, RestTemplate template, boolean isLogEnabled, String pHeader, String clientApiKey, String clienSecretKey
            ListenableFuture<AuthResponse> authTask = listenerService.submit(
                    new CallableAuthenticationWorker(mapper, restTemplate, true, header, clientApiKey, clienSecretKey));
            AuthResponse resp = (AuthResponse) authTask.get();
            logger.info("{}Getting Auth Response:{},MsgParams:{}", header1, resp, msgParams);
            if (resp.getStatus() == 1) {
//                ObjectMapper maper, RestTemplate template, boolean isLogEnabled, String pHeader, 
//            AuthResponse auth, String msgType, String vIdentifier, 
//            String mobileNo, String msg, String templateName, List<TemplateParams> msgParams, String lang
                List<TemplateParams> params = new ArrayList<>();
                for (String pair : msgParams.split(",")) {
                    String[] entry = pair.split("=");
                    TemplateParams tmp = new TemplateParams(entry[0].trim(), entry[1].trim());
                    params.add(tmp);
                }
                logger.info("TemplateParams :{}", params);
                ListenableFuture<MsgResponse> msgSubmitTask = listenerService.submit(
                        new CallableWorker(mapper, restTemplate, true, header, resp, msgType, clientId, mobileNo, smsMsg, "welcomemessage", params, "en"));
                MsgResponse msgResp = (MsgResponse) msgSubmitTask.get();
                logger.info("{}Getting Msg Response:{}", header1, msgResp);
            } else {
                logger.info("{}Getting Auth Response:{}", header1, resp);
            }
        } catch (Exception e) {
            logger.error("{}Excep:submitPduToExecutor:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            sb = null;
            header = null;
        }

    }
}

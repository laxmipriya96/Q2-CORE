/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2api.dtos.template.AuthResponse;
//import com.vm.whatsapp.config.ConfigProp;
import java.util.Arrays;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Phani
 */
public class CallableAuthenticationWorker implements Callable<AuthResponse> {

    private static final Logger logger = LogManager.getLogger(CallableAuthenticationWorker.class);

    public RestTemplate restTemplate;

    ObjectMapper mapper;

    private boolean isLogEnabled;

    public String header;

    public String clientApiKey;

    public String clientSecretKey;

    public CallableAuthenticationWorker() {
    }

    public CallableAuthenticationWorker(ObjectMapper maper, RestTemplate template, boolean isLogEnabled, String pHeader, String clientApiKey, String clienSecretKey) {
        this.header = pHeader;
        this.mapper = maper;
        this.restTemplate = template;
        this.isLogEnabled = isLogEnabled;
        this.clientApiKey = clientApiKey;
        this.clientSecretKey = clienSecretKey;
    }

    @Override
    public AuthResponse call() throws Exception {
        try {
            return postDataToFCMServer();
        } catch (Exception e) {
            logger.error("{}Excep:call:apikey:[{}]:secretKey:[{}]:Error:{}", header, clientApiKey, clientSecretKey, ExceptionUtils.getRootCauseMessage(e));
            AuthResponse response = new AuthResponse(null, null, null, ExceptionUtils.getRootCauseMessage(e), null, null, (short) 0);
            return response;
        }
    }

    public AuthResponse postDataToFCMServer() throws JsonProcessingException {
        try {
            if (isLogEnabled) {
                logger.info("{}>>apikey:[{}]:secretKey:[{}]", header, clientApiKey, clientSecretKey);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-type", MediaType.APPLICATION_FORM_URLENCODED.toString());
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "client_credentials");
            map.add("client_id", clientApiKey);
            map.add("client_secret", clientSecretKey);
            map.add("scope", "messages:send media:create");
            if (isLogEnabled) {
                logger.info("{}>>request:requestData:[{}]", header, map);
            }
            //HttpEntity<String> request = new HttpEntity<String>(requestObj, headers);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<AuthResponse> result = restTemplate.exchange("https://auth.messengerpeople.dev/token", HttpMethod.POST, request, AuthResponse.class);
            System.out.println("Response : " + result);
            AuthResponse str = result.getBody();
            str.setStatus((short) 1);
            System.out.println("Response : " + str);
            return str;
        } catch (HttpStatusCodeException he) {
            AuthResponse msg = mapper.readValue(he.getResponseBodyAsString(), AuthResponse.class);
            msg.setStatus((short) 0);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            //String token_type, String expires_in, String access_token, String error, String hint, String correlation_id, short status, short errorMessage
            AuthResponse response = new AuthResponse(null, null, null, ExceptionUtils.getRootCauseMessage(e), null, null, (short) 0);
            logger.error("{}Excep:postDataToFCMServer:apikey:[{}]:secretKey:[{}]:Error:{}", header, clientApiKey, clientSecretKey, ExceptionUtils.getRootCauseMessage(e));
            return response;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imperva.ddc.core.Connector;
import com.imperva.ddc.core.exceptions.AuthenticationException;
import com.imperva.ddc.core.exceptions.InvalidConnectionException;
import com.imperva.ddc.core.language.PhraseOperator;
import com.imperva.ddc.core.language.QueryAssembler;
import com.imperva.ddc.core.language.Sentence;
import com.imperva.ddc.core.query.ConnectionResponse;
import com.imperva.ddc.core.query.Endpoint;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import com.imperva.ddc.core.query.*;
import com.vm.qsmart2.model.TblGlobalSetting;
import com.vm.qsmart2api.dtos.activedirectory.DirectoryGDto;
import com.vm.qsmart2api.dtos.activedirectory.DirectoryUsersDto;
import com.vm.qsmart2api.dtos.global.ActiveDirectorySettings;
import com.vm.qsmart2api.repository.GlobalSettingsRepositary;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author VMHDCLAP21
 */
@Service
public class ActiveDirectoryService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    MessageSource messageSource;

    @Autowired
    GlobalSettingsRepositary gRepositary;

    @Autowired
    ObjectMapper objMappaer;

    public Endpoint endpoint = null;

    public ActiveDirectorySettings getActiveDirectoryDataFromDB(String header, long enterpriseId, long userId) {
        ActiveDirectorySettings data = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>enterpriseId:[{}],SettingsType:[{}]", header, enterpriseId, "LDAP");
            }
            TblGlobalSetting globalSettings = gRepositary.getGlobalSettingsByType("LDAP", enterpriseId);
            if (globalSettings != null) {
                String jsonString = globalSettings.getSettingJson();
                System.out.println("JSON STRING: " + jsonString);
                if (jsonString != null && !jsonString.isEmpty()) {
                    data = objMappaer.readValue(jsonString, ActiveDirectorySettings.class);
                }
                return data;
            }
            return null;
        } catch (Exception e) {
            logger.error("{}Excep:getActiveDirectoryData:enterpriseid:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return null;
        } finally {
            data = null;
        }
    }

    public com.vm.qsmart2api.dtos.Response checkActiveDirectoryConnection(String header, long userId, Locale locale, long enterpriseId) {
        ActiveDirectorySettings config = getActiveDirectoryDataFromDB(header, enterpriseId, userId);
        return validateADConnection(header, locale, config);
    }

    public com.vm.qsmart2api.dtos.Response validateADConnection(String header, Locale locale, ActiveDirectorySettings config) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:config:[{}]", header, config);
            }
            if (config != null) {
                endpoint = new Endpoint();
                endpoint.setSecuredConnection(false);
                endpoint.setSecuredConnectionSecondary(false);
                endpoint.setHost(config.getHost());
                endpoint.setUserAccountName(config.getAdPath() + "" + config.getUserName());
                endpoint.setPassword(config.getPassword());
                if (config.getPort() != 0) {
                    endpoint.setPort(config.getPort());
                }
                ConnectionResponse connectionResponse = authenticate(endpoint, config);
                if (!connectionResponse.isError()) {
                    return new com.vm.qsmart2api.dtos.Response(true, messageSource.getMessage("active.ctrl.sucess", null, locale));
                } else {
                    Exception e = connectionResponse.getStatuses().get(config.getHost()).getError();
                    logger.info("===>" + e.getLocalizedMessage());
                    if (e instanceof AuthenticationException) {
                        return new com.vm.qsmart2api.dtos.Response(false, messageSource.getMessage("active.ctrl.auth.fail", null, locale));
                    } else if (e instanceof InvalidConnectionException) {
                        return new com.vm.qsmart2api.dtos.Response(false, messageSource.getMessage("active.ctrl.fail", null, locale));
                    } else {
                        return new com.vm.qsmart2api.dtos.Response(false, messageSource.getMessage("active.ctrl.fail", null, locale));
                    }
                }
            } else {
                return new com.vm.qsmart2api.dtos.Response(false, messageSource.getMessage("active.ctrl.config.notfound", null, locale));
            }
        } catch (Exception e) {
            logger.error("{}Excep:checkDirectoryConnection:Error:{}", header, e.getMessage());
            throw new RuntimeException(messageSource.getMessage("common.wrong.message", null, locale));
        } finally {
            if (endpoint != null) {
                endpoint.close();
            }
        }
    }

    public int authentionWithActiveDirectory(String header, long userId, Locale locale, long enterpriseId, String username, String pwd) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:userId:[{}]:enterpriseId:[{}]:username:{}", header, userId, enterpriseId, username);
            }
            ActiveDirectorySettings config = getActiveDirectoryDataFromDB(header, enterpriseId, userId);
            if (config != null) {
                endpoint = new Endpoint();
                endpoint.setSecuredConnection(false);
                endpoint.setSecuredConnectionSecondary(false);
                endpoint.setHost(config.getHost());
                endpoint.setUserAccountName(config.getAdPath() + "" + username);
                endpoint.setPassword(pwd);
                if (config.getPort() != 0) {
                    endpoint.setPort(config.getPort());
                }
                ConnectionResponse connectionResponse = authenticate(endpoint, config);
                if (connectionResponse.isError()) {
                    Exception e = connectionResponse.getStatuses().get(config.getHost()).getError();
                    logger.info("===>" + e.getLocalizedMessage());
                    if (e instanceof AuthenticationException) {
                        return 2;
                    } else if (e instanceof InvalidConnectionException) {
                        return 3;
                    } else {
                        return 4;
                    }
                } else {
                    return 1;
                }

            } else {
                return 0;
            }
        } catch (Exception e) {
            logger.error("{}Excep:authentionWithActiveDirectory:userId:{}:enterpriseId:{}:userName:{}:Error:{}", header, userId, enterpriseId, username, e.getMessage());
            return -1;
        } finally {
            if (endpoint != null) {
                endpoint.close();
            }
        }
    }

    public DirectoryGDto getUserFromDirectoryByName(String header, long userId, String name, Locale locale, long enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:LoginId:[{}]:enterpriseId:[{}]:name:[{}]", header, userId, enterpriseId, name);
            }
            ActiveDirectorySettings config = getActiveDirectoryDataFromDB(header, enterpriseId, userId);
            endpoint = new Endpoint();
            endpoint.setSecuredConnection(false);
            endpoint.setSecuredConnectionSecondary(false);
            endpoint.setHost(config.getHost());
            endpoint.setUserAccountName(config.getAdPath() + "" + config.getUserName());
            endpoint.setPassword(config.getPassword());
            if (config.getPort() != 0) {
                endpoint.setPort(config.getPort());
            }
            ConnectionResponse connectionResponse = authenticate(endpoint, config);//DirectoryConnectorService.authenticate(endpoint);
            boolean succeeded = !connectionResponse.isError();
            if (succeeded) {
                QueryRequest queryRequest = new QueryRequest();

                queryRequest.setDirectoryType(DirectoryType.MS_ACTIVE_DIRECTORY);
                queryRequest.setEndpoints(new ArrayList<Endpoint>() {
                    {
                        add(endpoint);
                    }
                });
                if (logger.isDebugEnabled()) {
                    logger.info("AD MAX USERS :" + config.getMaxUsers());
                }
                queryRequest.setSizeLimit(config.getMaxUsers() > 0 ? config.getMaxUsers() : 10000);
                // queryRequest.setTimeLimit(5);
                if (logger.isDebugEnabled()) {
                    logger.debug("AD TIME OUT :" + config.getLdapTimeOut());
                }
                queryRequest.setTimeLimit(config.getLdapTimeOut() > 0 ? config.getLdapTimeOut() : 30);
                queryRequest.setObjectType(ObjectType.USER);
                queryRequest.addRequestedField(config.getLoginId());
                queryRequest.addRequestedField(config.getFirstName());
                queryRequest.addRequestedField(config.getLastName());
                queryRequest.addRequestedField(config.getMobileNo());
                queryRequest.addRequestedField(config.getMailId());
                queryRequest.addRequestedField(FieldType.USER_PRINCIPAL_NAME);

                Sentence firstNameSentence = new QueryAssembler().addPhrase(FieldType.LOGON_NAME, PhraseOperator.EQUAL, name).closeSentence();
                queryRequest.addSearchSentence(firstNameSentence);
                QueryResponse queryResponse = null;
                try (Connector connector = new Connector(queryRequest)) {
                    queryResponse = connector.execute();
                }
                List<DirectoryUsersDto> users = new ArrayList<>();
                for (EntityResponse entityResponse : queryResponse.getAll()) {
                    DirectoryUsersDto dto = new DirectoryUsersDto();
                    for (Field field : entityResponse.getValue()) {
                        String fieldType = field.getName();
                        if (fieldType.equalsIgnoreCase(config.getMobileNo())) {
                            dto.setPhoneNumber((String) field.getValue());
                        } else if (fieldType.equalsIgnoreCase(config.getFirstName())) {
                            dto.setFirstName((String) field.getValue());
                        } else if (fieldType.equalsIgnoreCase(config.getLastName())) {
                            dto.setLastName((String) field.getValue());
                        } else if (fieldType.equalsIgnoreCase(config.getMailId())) {
                            dto.setEmail((String) field.getValue());
                        } else if (fieldType.equalsIgnoreCase(config.getLoginId())) {
                            dto.setLoginId((String) field.getValue());
                        } else {
                            logger.info("{} || Value:[{}]", header, field.getValue().toString());
                        }
                    }
                    users.add(dto);
                }
                List<DirectoryUsersDto> list = users.stream()
                        .filter(e -> e.getLoginId().equals(name))
                        .collect(Collectors.toList());
                if (logger.isDebugEnabled()) {
                    logger.info("ActiveDirectores:Users:Size:{}", users.size());
                }
                DirectoryGDto directory = list.isEmpty() ? new DirectoryGDto(false, messageSource.getMessage("directory.ctrl.notexist", null, locale)) : new DirectoryGDto(true, messageSource.getMessage("directory.ctrl.exist", null, locale), list);
                return directory;
            } else {
                DirectoryGDto directory = new DirectoryGDto(false, messageSource.getMessage("active.ctrl.fail", null, locale));
                return directory;
            }
        } catch (Exception e) {
            logger.error("{}Excep:getUsersFromDirectoryByName:userid:{}:enterpriseid:{}:Error:{}", header, userId, enterpriseId, e.getMessage());
            return new DirectoryGDto(false, messageSource.getMessage("directory.ctrl.notexist", null, locale));
        } finally {
            if (endpoint != null) {
                endpoint.close();
            }
        }
    }

    public ConnectionResponse authenticate(Endpoint endpoint, ActiveDirectorySettings config) {
        try {
            ConnectionResponse connectionResponse = null;
            QueryRequest queryRequest = new QueryRequest();
            queryRequest.setSizeLimit(config.getMaxUsers() > 0 ? config.getMaxUsers() : 10000);
            queryRequest.setTimeLimit(config.getLdapTimeOut() > 0 ? config.getLdapTimeOut() : 30);
            queryRequest.setIgnoreSSLValidations(true);
            queryRequest.addEndpoint(endpoint);
            try (Connector connector = new Connector(queryRequest)) {
                connectionResponse = connector.testConnection();
            }
            return connectionResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}

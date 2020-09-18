/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.global;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tejasri
 */
@JsonIgnoreProperties( ignoreUnknown = true)
public class SmsSettings {

    private String connectionName;
    private String typeOfApi;
    private String method;
    private String host;
    private String path;
    private List<QueryParam> queryParameters;

    public SmsSettings() {
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getTypeOfApi() {
        return typeOfApi;
    }

    public void setTypeOfApi(String typeOfApi) {
        this.typeOfApi = typeOfApi;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<QueryParam> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(List<QueryParam> queryParameters) {
        this.queryParameters = queryParameters;
    }

    @Override
    public String toString() {
        return "SmsSettings{" + "connectionName=" + connectionName + ", typeOfApi=" + typeOfApi + ", method=" + method + ", host=" + host + ", path=" + path + ", queryParameters=" + queryParameters + '}';
    }

}

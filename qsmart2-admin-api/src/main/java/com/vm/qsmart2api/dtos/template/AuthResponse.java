/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
    
    private String token_type;
    private String expires_in;
    private String access_token;
    private String error;
    private String hint;
    private String correlation_id;
    private short status;

    public AuthResponse(String token_type, String expires_in, String access_token, String error, String hint, String correlation_id, short status) {
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.access_token = access_token;
        this.error = error;
        this.hint = hint;
        this.correlation_id = correlation_id;
        this.status = status;
    }


    public AuthResponse() {
    }
    
    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getCorrelation_id() {
        return correlation_id;
    }

    public void setCorrelation_id(String correlation_id) {
        this.correlation_id = correlation_id;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AuthResponse{" + "token_type=" + token_type + ", expires_in=" + expires_in + ", access_token=" + access_token + ", error=" + error + ", hint=" + hint + ", correlation_id=" + correlation_id + ", status=" + status + '}';
    }

        
}

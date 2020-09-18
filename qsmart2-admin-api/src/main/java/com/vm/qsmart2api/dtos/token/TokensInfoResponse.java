/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.token;

import java.util.List;

/**
 *
 * @author Ashok
 */
public class TokensInfoResponse {

    private List<TokenDTO> waitingTokenss;
    private List<TokenDTO> servingTokens;

    public List<TokenDTO> getWaitingTokenss() {
        return waitingTokenss;
    }

    public void setWaitingTokenss(List<TokenDTO> waitingTokenss) {
        this.waitingTokenss = waitingTokenss;
    }

    public List<TokenDTO> getServingTokens() {
        return servingTokens;
    }

    public void setServingTokens(List<TokenDTO> servingTokens) {
        this.servingTokens = servingTokens;
    }

    public TokensInfoResponse() {
    }

    public TokensInfoResponse(List<TokenDTO> waitingTokenss, List<TokenDTO> servingTokens) {
        this.waitingTokenss = waitingTokenss;
        this.servingTokens = servingTokens;
    }

}

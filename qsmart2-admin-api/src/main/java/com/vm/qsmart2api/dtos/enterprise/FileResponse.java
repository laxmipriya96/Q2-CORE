/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.enterprise;

import com.vm.qsmart2api.dtos.Response;

/**
 *
 * @author Phani
 */
public class FileResponse extends Response{

    private String filePath;
    private Byte[] logo;

    public FileResponse() {
    }

    public FileResponse(boolean status, String messages) {
        super(status, messages);
    }

    public FileResponse(String filePath) {
        this.filePath = filePath;
    }

    public FileResponse(boolean status,  String messages, String filePath) {
        super(status, messages);
        this.filePath = filePath;
    }
    
    public FileResponse(boolean status,  String messages, String filePath, Byte[] logo) {
        super(status, messages);
        this.filePath = filePath;
        this.logo = logo;
    }
    


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Byte[] getLogo() {
        return logo;
    }

    public void setLogo(Byte[] logo) {
        this.logo = logo;
    }
    
    @Override
    public String toString() {
        return "FileResponse{" + "filePath=" + filePath + '}';
    }

}


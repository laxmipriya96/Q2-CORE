/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.userprofile;

import com.vm.qsmart2api.dtos.userprofile.ServicesWithCount;
import java.util.List;

/**
 *
 * @author Ashok
 */
public class ServicesWithCountGDTO {
    
    private List<ServicesWithCount> ServicesWithCounts;

    public ServicesWithCountGDTO() {
    }

    public ServicesWithCountGDTO(List<ServicesWithCount> ServicesWithCounts) {
        this.ServicesWithCounts = ServicesWithCounts;
    }

    public List<ServicesWithCount> getServicesWithCounts() {
        return ServicesWithCounts;
    }

    public void setServicesWithCounts(List<ServicesWithCount> ServicesWithCounts) {
        this.ServicesWithCounts = ServicesWithCounts;
    }
    
}

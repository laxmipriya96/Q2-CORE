/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblGlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Phani
 */
@Repository
public interface GlobalSettingsRepositary  extends JpaRepository<TblGlobalSetting, Integer>{
    
    
     @Query("SELECT tbl FROM TblGlobalSetting tbl where tbl.settingType = :settingType and tbl.enterpriseId = :enterpriseId")
    public TblGlobalSetting getGlobalSettingsByType(@Param("settingType") String settingType, @Param("enterpriseId") long enterpriseId);
    
}

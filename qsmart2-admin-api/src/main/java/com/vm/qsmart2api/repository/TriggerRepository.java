/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblTrigger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Tejasri
 */
public interface TriggerRepository extends JpaRepository<TblTrigger, Long>{
    
    @Query("SELECT distinct tbl FROM TblTrigger tbl left join fetch tbl.params params ")
    public List<TblTrigger> findTriggersWithParams();
    
}

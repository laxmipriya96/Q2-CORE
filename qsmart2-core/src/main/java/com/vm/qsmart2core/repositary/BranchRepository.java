/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ASHOK
 */
public interface BranchRepository extends JpaRepository<TblBranch, Long> {

    @Query("SELECT tbl FROM TblBranch tbl left join fetch tbl.location loc left join fetch loc.tblenterprise ent left join fetch tbl.serviceLocations serLoccations WHERE serLoccations.serviceLocation = :serviceLocation and loc.locationIdentfier = :locationIdentfier")
    public TblBranch findBrnachNameByLocation(@Param("serviceLocation") String serviceLocation, @Param("locationIdentfier") String locationIdentfier);
}

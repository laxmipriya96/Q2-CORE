/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblApptType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Tejasri
 */
public interface ApptTypeRepository extends JpaRepository<TblApptType, Long> {

    @Query("SELECT a FROM TblApptType a left join fetch a.services s  left join fetch a.users u WHERE a.apptCode = :apptCode and (s.branchId = :branchId or s.branchId is null)")
    public TblApptType findApptTypeByApptCode(@Param("apptCode") String apptCode, @Param("branchId") long branchId);

}

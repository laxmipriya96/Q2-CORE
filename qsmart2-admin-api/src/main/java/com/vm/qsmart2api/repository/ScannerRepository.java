/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblScanner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Tejasri
 */
public interface ScannerRepository extends JpaRepository<TblScanner, Long>{
    @Query("SELECT COUNT(tbl) FROM TblScanner tbl WHERE UPPER(tbl.scannerName) = :scannerName and tbl.locationId = :locationId")
    public int validateScannerName(@Param("scannerName") String scannerName, @Param("locationId") Long locationId);

    @Query("SELECT COUNT(tbl) FROM TblScanner tbl WHERE UPPER(tbl.scannerName) = :scannerName and tbl.scannerId != :scannerId and tbl.locationId = :locationId")
    public int validateScannerNameByScannerId(@Param("scannerName") String scannerName, @Param("scannerId") Long scannerId, @Param("locationId") Long locationId);

    @Query("SELECT distinct tbl FROM TblScanner tbl left join fetch tbl.branches branches where tbl.locationId = :locationId order by tbl.scannerId desc")
    public List<TblScanner> findByLocationId(@Param("locationId") Long locationId);
    
}

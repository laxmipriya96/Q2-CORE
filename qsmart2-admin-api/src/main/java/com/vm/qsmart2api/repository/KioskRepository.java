/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblKiosk;
import com.vm.qsmart2api.dtos.kiosk.KioskThemeDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Tejasri
 */
public interface KioskRepository extends JpaRepository<TblKiosk, Long> {

    @Query("SELECT COUNT(tbl) FROM TblKiosk tbl WHERE UPPER(tbl.kioskName) = :kioskName and tbl.locationId = :locationId")
    public int validateKioskName(@Param("kioskName") String kioskName, @Param("locationId") Long locationId);

    @Query("SELECT COUNT(tbl) FROM TblKiosk tbl WHERE UPPER(tbl.kioskName) = :kioskName and tbl.kioskId != :kioskId and tbl.locationId = :locationId")
    public int validateKioskNameByKioskId(@Param("kioskName") String kioskName, @Param("kioskId") Long displayId, @Param("locationId") Long locationId);

    @Query("SELECT distinct tbl FROM TblKiosk tbl left join fetch tbl.branches branches where tbl.locationId = :locationId order by tbl.kioskId desc")
    public List<TblKiosk> findByLocationId(@Param("locationId") Long locationId);

    @Query("SELECT tbl.locationId FROM TblKiosk tbl where tbl.kioskIdentifier= :kioskIdentifier")
    public long getLocationIdByKioskIdentifier(@Param("kioskIdentifier") String kioskIdentifier);

    @Query("SELECT new com.vm.qsmart2api.dtos.kiosk.KioskThemeDto(theme.themeId, theme.themeName) FROM TblKiosk tbl "
            + " left join TblTheme theme ON theme.themeId = tbl.themeId  "
            + " where tbl.kioskIdentifier= :kioskIdentifier")
    public KioskThemeDto getThemeByKioskIdentifi(@Param("kioskIdentifier") String kioskIdentifier);

}

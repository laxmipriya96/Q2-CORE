/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblBranch;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ASHOK
 */
public interface BranchRepository extends JpaRepository<TblBranch, Long> {

    @Query("SELECT distinct tbl FROM TblBranch tbl join fetch tbl.location loc join fetch tbl.branchType brnachtype where loc.locationId = :locationId order by tbl.branchId desc")
    public List<TblBranch> getAllBranchesByLocationId(@Param("locationId") Long locationId);

    @Query("SELECT COUNT(tbl) FROM TblBranch tbl WHERE UPPER(tbl.branchNameEn)= :branchNameEn and tbl.location.locationId= :locationId")
    public int validateBranchEngName(@Param("branchNameEn") String branchNameEn, @Param("locationId") Long locationId);

    @Query("SELECT COUNT(tbl) FROM TblBranch tbl WHERE UPPER(tbl.branchNameAr)= :branchNameAr and tbl.location.locationId= :locationId")
    public int validateBranchNameArb(@Param("branchNameAr") String branchNameAr, @Param("locationId") Long locationId);

    @Query("SELECT COUNT(tbl) FROM TblBranch tbl WHERE UPPER(tbl.branchNameEn)= :branchNameEn and tbl.branchId != :branchId and tbl.location.locationId= :locationId")
    public int validateBranchEngNameByBranchId(@Param("branchNameEn") String branchNameEn, @Param("branchId") Long branchId, @Param("locationId") Long locationId);

    @Query("SELECT COUNT(tbl) FROM TblBranch tbl WHERE UPPER(tbl.branchNameAr) = :branchNameAr and tbl.branchId != :branchId and tbl.location.locationId = :locationId")
    public int validateBranchArbNameByBranchId(@Param("branchNameAr") String branchNameAr, @Param("branchId") Long branchId, @Param("locationId") Long locationId);

    @Query("SELECT distinct tbl,s FROM TblBranch tbl join fetch tbl.location loc "
            + "left join TblService s on tbl.branchId = s.branchId "
            + "where loc.locationId = :locationId")
    public List<Object[]> getAllBranchesAndServsByLocationId(@Param("locationId") Long locationId);

    @Query("SELECT tbl FROM TblBranch tbl left join fetch tbl.branchType bt WHERE tbl.branchId = :branchId")
    public TblBranch findBrnachNameBybranchId(@Param("branchId") Long branchId);

    @Query("SELECT distinct tbl FROM TblBranch tbl join fetch tbl.location loc join fetch tbl.branchType brnachtype where loc.locationId = :locationId and brnachtype.branchTypeId = :branchTypeId")
    public List<TblBranch> getAllBranchesByLocIdAndBranchType(@Param("locationId") Long locationId, @Param("branchTypeId") Long branchTypeId);

    @Query("SELECT distinct tbl,s FROM TblBranch tbl join fetch tbl.location loc join fetch tbl.branchType brnachtype left join fetch tbl.serviceLocations servicelocs"
            + "left join TblService s on tbl.branchId = s.branchId "
            + "where loc.locationId = :locationId and brnachtype.branchTypeCode = :branchTypeCode")
    public List<Object[]> getAllBranchesAndServsByLocationIdAndBranchType(@Param("locationId") Long locationId, @Param("branchTypeCode") String branchType);

    @Query("SELECT distinct tbl,s FROM TblBranch tbl join fetch tbl.location loc join fetch tbl.branchType brnachtype left join fetch tbl.serviceLocations servicelocs"
            + " left join TblService s on tbl.branchId = s.branchId "
            + " left join TblUserProfile tup on tbl.branchId = tup.branchId and s.serviceId = tup.serviceId "
            + " where tup.userId = :userId")
    public List<Object[]> getMappedBranchesAndServicesByUserId(@Param("userId") Long userId);

    @Query("SELECT distinct tbl FROM TblBranch tbl join fetch tbl.branchType branctype Where tbl.branchId = :branchId")
    public TblBranch getBranchTypeCodeBybranchId(@Param("branchId") Long branchId);

    @Query("SELECT distinct tbl FROM TblBranch tbl "
            + "join fetch tbl.location loc "
            + "join fetch tbl.branchType brnachtype "
            + "where loc.locationId = :locationId and brnachtype.branchTypeCode = :branchTypeCode ORDER BY tbl.branchId ASC")
    public List<TblBranch> getAllBranchesByLocationIdAndBranchTypeCode(@Param("locationId") Long locationId, @Param("branchTypeCode") String branchTypeCode);

}

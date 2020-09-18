/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblPatient;
import com.vm.qsmart2api.dtos.kiosk.KioskAppointmentDTO;
import com.vm.qsmart2api.dtos.manualtoken.PatientServingDTO;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Ashok
 */
public interface PatientRepository extends JpaRepository<TblPatient, Long> {

    @Query("select case when count(tbl)> 0 then true else false end  from TblPatient tbl where tbl.mrnNo= :mrnNo")
    public boolean existsPatient(@Param("mrnNo") String mrnNo);

    @Query("SELECT tbl from TblPatient tbl where tbl.mrnNo= :mrnNo")
    public TblPatient getPatientByMrnNo(@Param("mrnNo") String mrnNo);

    @Query("SELECT new com.vm.qsmart2api.dtos.manualtoken.PatientServingDTO(tbl.mrnNo, tbl.firstName, tbl.lastName, service.serviceNameEn, serviceBooked.serviceBookedId, serviceBooked.serviceStatus) FROM TblPatient tbl "
            + "left join TblAppointment appt on appt.patientId= tbl.patientId "
            + "left join TblServiceBooked serviceBooked on serviceBooked.appointment.tranId= appt.tranId "
            + "left join serviceBooked.service  service "
            + "where tbl.mrnNo= :mrnNo and serviceBooked.serviceStatus in(:serviceStatus) and (serviceBooked.startTime BETWEEN :fromDate AND :toDate)")
    public List<PatientServingDTO> getServiceBookedsByMrnNo(@Param("mrnNo") String mrnNo, @Param("serviceStatus") List<String> status, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT new com.vm.qsmart2api.dtos.kiosk.KioskAppointmentDTO(tbl.mrnNo as mrnNo, concat(tbl.lastName,' ',tbl.firstName) as patientName, concat(user.lastName,' ',user.firstName) as doctorName, branch.branchNameEn as departmentName, appt.startTime as appointmentTime, serviceBooked.serviceBookedId as serviceBookedId) FROM TblPatient tbl "
            + "left join TblAppointment appt on appt.patientId= tbl.patientId "
            + "left join TblServiceBooked serviceBooked on serviceBooked.appointment.tranId= appt.tranId "
            + "left join serviceBooked.service  service "
            + "left join TblBranch branch on branch.branchId= service.branchId "
            + "left join branch.location location "
            + "left join TblUser user ON user.userId= serviceBooked.drId "
            + "where tbl.mrnNo= :mrnNo and location.locationId= :locationId and serviceBooked.serviceStatus= :serviceStatus and (serviceBooked.startTime BETWEEN :fromDate AND :toDate)")
    public List<KioskAppointmentDTO> getAppointmentsByMrnNoAndkioskId(@Param("mrnNo") String mrnNo, @Param("locationId") Long locationId, @Param("serviceStatus") String serviceStatus, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}

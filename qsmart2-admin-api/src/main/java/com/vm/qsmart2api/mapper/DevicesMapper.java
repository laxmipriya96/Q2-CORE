/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.mapper;

import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblDisplay;
import com.vm.qsmart2.model.TblKiosk;
import com.vm.qsmart2.model.TblScanner;
import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.model.TblTheme;
import com.vm.qsmart2api.dtos.display.DisplayCrtDto;
import com.vm.qsmart2api.dtos.display.DisplayUpDto;
import com.vm.qsmart2api.dtos.display.ThemeDto;
import com.vm.qsmart2api.dtos.kiosk.KioskCrtDto;
import com.vm.qsmart2api.dtos.kiosk.KioskUpDto;
import com.vm.qsmart2api.dtos.scanner.ScannerCrtDto;
import com.vm.qsmart2api.dtos.scanner.ScannerUpDto;
import java.util.List;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Phani
 */
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "String")
public interface DevicesMapper {

    DevicesMapper INSTANCE = Mappers.getMapper(DevicesMapper.class);

    default TblService generateServiceEntity(Long serviceId) {
        return new TblService(serviceId);
    }
//

    default Long generateServiceEntityToInt(TblService service) {
        return service.getServiceId();
    }

    @Mappings({
        @Mapping(source = "services", target = "services")
    })
    TblDisplay displayCrtDtoToDisplayEntity(DisplayCrtDto dto);

    @Mappings({
        @Mapping(source = "services", target = "services")
    })
    TblDisplay displayUDtoToDisplayEntity(DisplayUpDto dto);

    @Mappings({
        @Mapping(source = "services", target = "services")
    })
    DisplayUpDto displayEntityToDisplayUDto(TblDisplay dto);

    List<DisplayUpDto> displayEntityListToDisplayUpDtoList(List<TblDisplay> dtoList);

    List<ThemeDto> themeEntityToThemeDto(List<TblTheme> dto);

    
//    @Mappings({
//        @Mapping(source = "branches", target = "branches")
//    })
    TblScanner scannerCrtDtoToScannerEntity(ScannerCrtDto dto);

//    @Mappings({
//        @Mapping(source = "branches", target = "branches")
//    })
    TblScanner scannerUDtoToScannerEntity(ScannerUpDto dto);

    @Mappings({
        @Mapping( target = "branch", expression = "java((dto.getBranches() != null && !dto.getBranches().isEmpty()) ? dto.getBranches().iterator().next().getBranchId() : 0)")
    })
    ScannerUpDto scannerEntityToScannerUDto(TblScanner dto);

    List<ScannerUpDto> scannerEntityListToScannerUpDtoList(List<TblScanner> dtoList);
    
    
    //Kiosk
    default TblBranch generateBranchEntity(Long branchId) {
        return new TblBranch(branchId);
    }
//

    default Long generateBranchEntityToInt(TblBranch branche) {
        return branche.getBranchId();
    }

    @Mappings({
        @Mapping(source = "branches", target = "branches")
    })
    TblKiosk kioskCrtDtoToKioskEntity(KioskCrtDto dto);

    @Mappings({
        @Mapping(source = "branches", target = "branches")
    })
    TblKiosk kioskUDtoToKioskEntity(KioskUpDto dto);

    @Mappings({
        @Mapping(source = "branches", target = "branches")
    })
    KioskUpDto kioskEntityToKioskUDto(TblKiosk dto);

    List<KioskUpDto> kioskEntityListToKioskUpDtoList(List<TblKiosk> dtoList);

}

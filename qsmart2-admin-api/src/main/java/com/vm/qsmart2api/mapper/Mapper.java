/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.mapper;

import com.vm.qsmart2.model.TblApptType;
import com.vm.qsmart2.model.TblBranch;
import com.vm.qsmart2.model.TblBranchType;
import com.vm.qsmart2.model.TblEnterprise;
import com.vm.qsmart2.model.TblLanguage;
import com.vm.qsmart2.model.TblLocation;
import com.vm.qsmart2.model.TblMedService;
import com.vm.qsmart2.model.TblModule;
import com.vm.qsmart2.model.TblNotificationMail;
import com.vm.qsmart2.model.TblNotificationSms;
import com.vm.qsmart2.model.TblParams;
import com.vm.qsmart2.model.TblPermission;
import com.vm.qsmart2.model.TblRole;
import com.vm.qsmart2.model.TblRoom;
import com.vm.qsmart2.model.TblRoomMaster;
import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.model.TblSubModule;
import com.vm.qsmart2.model.TblTimeZone;
import com.vm.qsmart2.model.TblTrigger;
import com.vm.qsmart2.model.TblUser;
import com.vm.qsmart2api.dtos.UserDetails;
import com.vm.qsmart2api.dtos.appttype.ApptTypeCrtDto;
import com.vm.qsmart2api.dtos.appttype.ApptTypeDto;
import com.vm.qsmart2api.dtos.branch.BranchDTO;
import com.vm.qsmart2api.dtos.branch.BranchRoom;
import com.vm.qsmart2api.dtos.branch.BranchTypeDTO;
import com.vm.qsmart2api.dtos.branch.BranchUDTO;
import com.vm.qsmart2api.dtos.branch.BranchesServices;
import com.vm.qsmart2api.dtos.enterprise.Enterprise;
import com.vm.qsmart2api.dtos.enterprise.EnterpriseCreateRequest;
import com.vm.qsmart2api.dtos.enterprise.EnterpriseGDto;
import com.vm.qsmart2api.dtos.enterprise.LanguageDto;
import com.vm.qsmart2api.dtos.enterprise.TimeZoneDto;
import com.vm.qsmart2api.dtos.jwt.JwtUserDto;
import com.vm.qsmart2api.dtos.location.BranchGetDto;
import com.vm.qsmart2api.dtos.location.LocationCrtDto;
import com.vm.qsmart2api.dtos.location.LocationDto;
import com.vm.qsmart2api.dtos.location.LocationGDto;
import com.vm.qsmart2api.dtos.mailnotification.MailCrtDto;
import com.vm.qsmart2api.dtos.mailnotification.MailUpDto;
import com.vm.qsmart2api.dtos.medservice.MedServiceDTO;
import com.vm.qsmart2api.dtos.medservice.MedServiceUDTO;
import com.vm.qsmart2api.dtos.menus.ModulesDto;
import com.vm.qsmart2api.dtos.menus.PermissionsDto;
import com.vm.qsmart2api.dtos.menus.SubModulesDto;
import com.vm.qsmart2api.dtos.params.ParamDto;
import com.vm.qsmart2api.dtos.role.CustomRoleDto;
import com.vm.qsmart2api.dtos.role.RoleCreateDto;
import com.vm.qsmart2api.dtos.role.RoleDTO;
import com.vm.qsmart2api.dtos.room.RoomCrtDto;
import com.vm.qsmart2api.dtos.room.RoomUpDto;
import com.vm.qsmart2api.dtos.roomNo.RoomDto;
import com.vm.qsmart2api.dtos.roomNo.RoomNoGDto;
import com.vm.qsmart2api.dtos.service.ApptCreateDto;
import com.vm.qsmart2api.dtos.service.ServiceCrtDto;
import com.vm.qsmart2api.dtos.service.ServiceDTO;
import com.vm.qsmart2api.dtos.service.ServiceUDTO;
import com.vm.qsmart2api.dtos.service.ServiceUpDto;
import com.vm.qsmart2api.dtos.sms.NotificationSmsDTO;
import com.vm.qsmart2api.dtos.sms.NotificationSmsUDTO;
import com.vm.qsmart2api.dtos.trigger.TriggerDto;
import com.vm.qsmart2api.dtos.user.ProfileUpDto;
import com.vm.qsmart2api.dtos.user.UserCtrDTO;
import com.vm.qsmart2api.dtos.user.UserDTO;
import com.vm.qsmart2api.dtos.user.UserUDTO;
import com.vm.qsmart2api.dtos.userprofile.AssignedServiceGDto;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author ASHOK
 */
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "String")
public interface Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    //Enterprise
    EnterpriseGDto enterpriseToDto(TblEnterprise tbl);

    TblEnterprise createEnterpriseDtotoEnterpriseEntity(EnterpriseCreateRequest enterprise);

    TblEnterprise updateEnterpriseDtotoEnterpriseEntity(EnterpriseGDto enterprise);

    List<EnterpriseGDto> enterpriseEntityListToUpdateEnterpriseDtoList(List<TblEnterprise> enterprises);

    //User
    TblUser userCreateDtoToUserEntity(UserDTO dto);

    TblUser userUpdateDtoToUserEntity(UserUDTO dto);

    Set<TblUser> userdtoToUserList(List<UserCtrDTO> users);

    Set<UserCtrDTO> userEntityListToUserCtrDtoList(Set<TblUser> users);

    UserCtrDTO userEntityToUserCtrDto(TblUser user);

    @Mappings({
        @Mapping(target = "roleId", expression = "java((user.getRoles() != null && !user.getRoles().isEmpty()) ? user.getRoles().iterator().next().getRoleId() : 0)")
        ,
        @Mapping(target = "enterpriseId", expression = "java((user.getEnterprise() != null ) ? user.getEnterprise().getEnterpriseId() : 0)")
        ,
         @Mapping(target = "locationId", expression = "java((user.getLocations() != null && !user.getLocations().isEmpty())? user.getLocations().iterator().next().getLocationId() : 0)")
    })
    UserUDTO userEntityToUserUpdateDto(TblUser user);

    @Mappings({
        @Mapping(target = "roles", expression = "java((user.getRoles() != null && !user.getRoles().isEmpty()) ? roleEntityToRoleDto(user.getRoles().iterator().next()) : null)")
        ,
        @Mapping(target = "location", expression = "java((user.getLocations() != null && !user.getLocations().isEmpty()) ? locationEntityToDto(user.getLocations().iterator().next()) : null)")})
    UserDetails userEntityToUserDetailsDto(TblUser user);

    List<UserUDTO> userEntityListToUserUpdateDtoList(List<TblUser> usersList);

    TblUser userProfileUpdateDtoToUserEntity(ProfileUpDto dto);
   
    JwtUserDto userDetailsToJwtUserDto(UserDetails dto);

    //Role
    default TblPermission generatePermissionEntity(int permissionId) {
        return new TblPermission(permissionId);
    }

    default Integer generateInt(TblPermission permission) {
        return permission.getPermissionId();
    }

    @Mapping(source = "permissions", target = "permissions")
    TblRole roleCrtDtoToRoleEntity(RoleCreateDto dto);

    @Mapping(source = "permissions", target = "permissions")
    TblRole customRoleDtoToRoleEntity(CustomRoleDto dto);

    List<CustomRoleDto> roleListToCustomRoleDtoList(List<TblRole> roles);

    RoleDTO roleEntityToRoleDto(TblRole roles);

    List<RoleDTO> roleToROleDto(List<TblRole> roles);

    Enterprise enterpriseEntityToDto(TblEnterprise tbl);

    List<TimeZoneDto> timeZoneEntityListToListDto(List<TblTimeZone> dto);

    List<LanguageDto> languageEntityListToListDto(List<TblLanguage> dto);

    //location
    TblLocation locationCrtDtoToLocationEntity(LocationCrtDto dto);

    TblLocation locationDtoToLocationEntity(LocationDto dto);

    LocationDto locationEntityToDto(TblLocation dto);

    List<LocationDto> locationEntityListToDtoList(List<TblLocation> dto);

    //Room
    TblRoomMaster roomCrtDtoToRoomEntity(RoomCrtDto dto);

    TblRoomMaster roomDtoToRoomEntity(RoomUpDto dto);

    RoomUpDto roomMasterEntityToRoomDto(TblRoomMaster dto);

    RoomNoGDto rooEntityToRoomGdto(TblRoom dto);

    List<RoomUpDto> roomEntityListToRoomUpDtoList(List<TblRoomMaster> rooms);

    //RoomNo
//    TblRoom roomNoCrtDtoToRoomEntity(RoomNoCrtDto dto);
    TblRoom roomGDtoToRoomEntity(RoomNoGDto dto);

    List<RoomNoGDto> roomListEntityToroomdto(List<TblRoom> dto);

    //Branch 
    @Mapping(target = "branchType.branchTypeId", source = "branchTypeId")
    TblBranch branchDtoToBranch(BranchDTO dto);

    @Mappings({
        @Mapping(target = "branchTypeId", source = "branchType.branchTypeId")
        ,
        @Mapping(target = "serviceLocation", expression = "java((tbl.getServiceLocations() != null && !tbl.getServiceLocations().isEmpty()) ? tbl.getServiceLocations().iterator().next().getServiceLocation() : null)")
    })
    BranchUDTO branchToBranchUDTO(TblBranch tbl);

    List<BranchUDTO> branchentityListToBranchUDTOList(List<TblBranch> tbl);

    @Mappings({
        @Mapping(target = "branchTypeId", source = "branchType.branchTypeId")
        ,
        @Mapping(target = "serviceLocation", expression = "java((tbl.getServiceLocations() != null && !tbl.getServiceLocations().isEmpty()) ? tbl.getServiceLocations().iterator().next().getServiceLocation() : null)")
    })
    BranchesServices branchToBranchServices(TblBranch tbl);

    @Mappings({
        @Mapping(target = "branchType.branchTypeId", source = "branchTypeId")
    })
    TblBranch branchUpDtoToBranch(BranchUDTO udto);

    BranchRoom branchEntityToBranchRoom(TblBranch tbl);

    RoomDto roomMasterEntityTblToRoomDto(TblRoomMaster tbl);

//    LocationWithBranches locationToBranchList(TblLocation location);
    //Serivce
    TblService serviceDtoToService(ServiceDTO tbl);

    TblService serviceUDtoToService(ServiceUDTO tbl);

    ServiceUDTO serviceEntityToServiceUdto(TblService tblservice);

    List<ServiceUDTO> listserviceToServiceUDtO(List<TblService> listservice);

    Set<TblApptType> apptTypeServDtoToServiceEntity(Set<ApptCreateDto> dto);

    TblService serviceUpDtoToService(ServiceUpDto dto);

    List<ServiceCrtDto> serviceTblEntityToServiceGetDto(List<TblService> tbl);

    //BranchType
    List<BranchTypeDTO> branchTypeToBranchTypeDto(List<TblBranchType> tbl);

    //permission
    PermissionsDto permissionEntityToPermissionDto(TblPermission tblPermission);

    //Modules
    ModulesDto modulesEntityToModulesDto(TblModule module);

    //Submodules
    SubModulesDto subModulesEntityToSubModulesDto(TblSubModule subModule);

    //AppyType
    //@Mapping(target = "users", expression = "java((user.getRoles() != null && !user.getRoles().isEmpty()) ? roleEntityToRoleDto(user.getRoles().iterator().next()) : null)")
    List<ApptTypeDto> apptTypeEntityListToDto(List<TblApptType> tbl);

    // @Mapping(target = "users", expression = "java((user.getUsers() != null && !user.getUsers().isEmpty()) ? roleEntityToRoleDto(user.getRoles().iterator().next()) : null)")
    ApptTypeDto aptTypeEntityAptTypeDto(TblApptType aptTyp);

    TblApptType apptTypeCrtDtoToEntity(ApptTypeCrtDto dto);

    TblApptType apptTypeDtoToEntity(ApptTypeDto dto);

    //MedService
    TblMedService medservicedtoTOMedservice(MedServiceDTO dto);

    TblMedService medserviceudtoTOMedservice(MedServiceUDTO dto);

    MedServiceUDTO medserviceentityTOMedserviceDTO(TblMedService medService);

    //Trigger
    List<TriggerDto> triggerToTriggerDto(List<TblTrigger> tbl);

    //Params
    List<ParamDto> paramsToParamDto(List<TblParams> tbl);

    //MailNotification
    TblNotificationMail mailNotificationCrtDtoToEntityMail(MailCrtDto dto);

    TblNotificationMail mailUpDtoToNotificationMailEntity(MailUpDto dto);

    List<MailUpDto> listOfEntityNotificationMailToMailsList(List<TblNotificationMail> tbl);

    //NotificationSms
    List<NotificationSmsUDTO> notificationSmsListTONotificationSmsUDTO(List<TblNotificationSms> notification);

    TblNotificationSms notifiDtoTONotifiEntity(NotificationSmsDTO notifi);

    TblNotificationSms notifiUDtoTONotifiEntity(NotificationSmsUDTO notifi);

    ServiceCrtDto serviceTblEntityToServiceCrtDto(TblService dto);

    BranchGetDto branchEntityToBranchGetDto(TblBranch dto);

    LocationGDto locationEntityToLocationGDto(TblLocation dto);

    @Mappings({
        @Mapping(target = "count", expression = "java((tbl.getServiceBookings() != null && !tbl.getServiceBookings().isEmpty()) ? tbl.getServiceBookings().size() : 0)")
    })
    AssignedServiceGDto serviceEntityToAssignedServiceDto(TblService tbl);

    List<AssignedServiceGDto> serviceentityTOSerDTO(List<TblService> tbl);
}

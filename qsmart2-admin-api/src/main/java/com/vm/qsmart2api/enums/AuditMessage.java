/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.enums;

/**
 *
 * @author Ashok
 */
public enum AuditMessage {
    //Role
    RoleGet("Got the list of Roles"),
    RoleGetFail("Failed To Get list of Roles"),
    RoleCustomGet("Got the list of Custom Roles"),
    RoleCustomGetFail("Failed To Get list of Custom Roles"),
    RoleTypeGet("Got the list of RoleTypes"),
    RoleTypeGetFail("Failed To Get list of RoleTypes"),
    RoleGetModules("Got the list of Modules"),
    RoleGetModulesFail("Failed To Get list of Modules"),
    RoleCreate("Role Created"),
    RoleCreateFail("Failed to Create Role"),
    RoleUpdate("Role Updated"),
    RoleUpdateFail("Failed to Updated Role"),
    RoleDelete("Role Deleted"),
    RoleDeleteFail("Failed to Deleted Role"),
    RoleNameFail("Role Name already exist"),
    //Enterprise
    EnterpriseGet("Got the list of Enterprises"),
    EnterpriseGetFail("Failed To Get list of Enterprises"),
    EnterpriseTimeGet("Got the list of TimeZones"),
    EnterpriseTimeGetFail("Failed To Get list of TimeZones"),
    EnterpriseLanguageGet("Got the list of Languages"),
    EnterpriseLanguageGetFail("Failed To Get list of Languages"),
    EnterpriseFileUpload("File upload and validate"),
    EnterpriseFileUploadFail("Failed to File upload and validate"),
    EnterpriseLogoUpload("Logo Upload successfully"),
    EnterpriseLogoUploadFile("Failed to upload Logo"),
    EnterpriseLicenseGetFail("Invalid license file"),
    EnterpriseCreate("Enterprise Created"),
    EnterpriseCreateFail("Failed to Create Enterprise"),
    EnterpriseUpdate("Enterprise Updated"),
    EnterpriseUpdateFail("Failed to Updated Enterprise"),
    EnterpriseStatusUpdate("Enterprise Status Updated"),
    EnterpriseStatusUpdateFail("Failed to Updated Enterprise Status"),
    EnterpriseDelete("Enterprise Deleted"),
    EnterpriseDeleteFail("Failed to Deleted Enterprise"),
    //Location
    LocationGet("Got the list of Locations"),
    LocationGetFail("Failed To Get list of Locations"),
    LocationCreate("Location Created"),
    LocationCreateFail("Failed to Create Location"),
    LocationUpdate("Location Updated"),
    LocationUpdateFail("Failed to Updated Location"),
    LocationDelete("Location Deleted"),
    LocationDeleteFail("Failed to Deleted Location"),
    LocationArbNameFail("Location arbname already exist"),
    LocationEngNameFail("Location enname already exist"),
    //Branch
    BranchGet("Got the list of Branchs"),
    BranchGetFail("Failed To Get list of Branchs"),
    BranchCreate("Branch Created"),
    BranchCreateFail("Failed to Create Branch"),
    BranchUpdate("Branch Updated"),
    BranchUpdateFail("Failed to Updated Branch"),
    BranchDelete("Branch Deleted"),
    BranchDeleteFail("Failed to Deleted Branch"),
    BranchArbNameFail("Branch arbname already exist"),
    BranchEngNameFail("Branch enname already exist"),
    BranchDeleteFailByService("Branch have service So it can't Delete"),
    BranchDeleteFailByRoom("Branch have Room So it can't Delete"),
    BranchTypeGet("Got the list of Branchs"),
    BranchTypeGetFail("Failed To Get list of Branchs"),
    ServiceLocationFail("Service Location Already exists"),
    //Room
    RoomGet("Got the list of Rooms"),
    RoomGetFail("Failed To Get list of Rooms"),
    RoomCreate("Room Created"),
    RoomCreateFail("Failed to Create Room"),
    RoomUpdate("Room Updated"),
    RoomUpdateFail("Failed to Updated Room"),
    RoomDelete("Room Deleted"),
    RoomDeleteFail("Failed to Deleted Room"),
    RoomAssignedToRoomNos("Room have RoomNumbers So it can't Delete"),
    RoomArbNameFail("Room arbname already exist"),
    RoomEngNameFail("Room enname already exist"),
    //RoomNo
    RoomNosGet("Got the list of RoomNos"),
    RoomNosGetFail("Failed To Get list of RoomNos"),
    RoomNoCreate("Room Created"),
    RoomNoCreateFail("Failed to Create RoomNo"),
    RoomNoUpdate("RoomNo Updated"),
    RoomNoUpdateFail("Failed to Updated RoomNo"),
    RoomNoDelete("RoomNo Deleted"),
    RoomNoDeleteFail("Failed to Deleted RoomNo"),
    //ApptType
    ApptTypeGet("Got the list of ApptType"),
    ApptTypeGetFail("Failed To Get list of ApptType"),
    ApptTypeCreate("ApptType Created"),
    ApptTypeCreateFail("Failed to Create ApptType"),
    ApptTypeUpdate("ApptType Updated"),
    ApptTypeUpdateFail("Failed to Updated ApptType"),
    ApptTypeDelete("ApptType Deleted"),
    ApptTypeDeleteFail("Failed to Deleted ApptType"),
    MappingUsersWithApptType("ApptType User mapping sucessfull"),
    MappingUsersWithApptTypeFail("ApptType User mapping unsucessfull"),
    ApptCodeFail("ApptCode Already exists"),
    ApptTypeFail("ApptType Already exists"),
    //Users
    UserGet("Got the list of Rooms"),
    UserGetFail("Failed To Get list of Rooms"),
    UserCreate("User Created"),
    UserCreateFail("Failed to Create User"),
    UserUpdate("User Updated"),
    UserUpdateFail("Failed to Updated User"),
    UserTypeGet("Got the list of UserTypes"),
    UserTypeGetFail("Failed To Get list of UserTypes"),
    UserEmailFail("User Email already exist"),
    UserNameFail("UserName already exist"),
    UserStatusUpdate("User Status Updated"),
    UserStatusUpdateFail("Failed to Updated User Status"),
    UserProfileUpdate("User Profile Updated"),
    UserProfileUpdateFail("Failed to Updated User Profile"),
    UserPasswordUpdate("User Password Updated"),
    CurrentPasswordNotMatch("Current Password Not Matched"),
    UserPasswordUpdateFail("Failed to Updated User Password"),
    DoctorsGet("Getting doctors successfully"),
    DoctorsGetFail("Unable to get the doctors"),
    //Services
    ServiceGet("Got the list of Services"),
    ServiceGetFail("Failed To Get list of Services"),
    ServiceCreate("Service Created"),
    ServiceCreateFail("Failed to Create Service"),
    ServiceUpdate("Service Updated"),
    ServiceUpdateFail("Failed to Updated Service"),
    ServiceDelete("Service Deleted"),
    ServiceDeleteFail("Failed to Deleted Service"),
    ServiceArbNameFail("Service arbname already exist"),
    ServiceEngNameFail("Service enname already exist"),
    ServiceApptTypeMapping("Service ApptType Mapped"),
    ServiceApptTypeMappingFail("Failed to Mapping Service ApptType"),
    ServiceDetailsGet("Got the list of ServiceDetails"),
    ServiceDetailsGetFail("Failed To Get list of ServiceDetails"),
    //MedicalService
    MedServiceGet("Got the list of  MedServices"),
    MedServiceGetFail("Failed To Get list of  MedServices"),
    MedServiceCreate(" MedService Created"),
    MedServiceCreateFail("Failed to Create  MedService"),
    MedServiceUpdate(" MedService Updated"),
    MedServiceUpdateFail("Failed to Updated  MedService"),
    MedServiceDelete(" MedService Deleted"),
    MedServiceDeleteFail("Failed to Deleted Location"),
    MedServiceNameFail("MedServiceName already exist"),
    MedServiceCodeFail("MedServiceCode already exist"),
    MappingUsersWithMedService("Users Mapped with ApptType"),
    MappingUsersWithMedServiceFail("Failed to Mapping User to ApptType"),
    MedServiceDeleteFailByMedService("MedService have users So it can't Delete"),
    //Display
    DisplayGet("Got the list of DisplayBoards"),
    DisplayGetFail("Failed To Get list of DisplayBoards"),
    DisplayCreate("DisplayBoard Created"),
    DisplayCreateFail("Failed to Create DisplayBoard"),
    DisplayUpdate("DisplayBoard Updated"),
    DisplayUpdateFail("Failed to Updated DisplayBoard"),
    DisplayNameFail("Display Name Already exists"),
    DisplayDelete("DisplayBoard Deleted"),
    DisplayDeleteFail("Failed to Deleted DisplayBoard"),
    DisplayStatusUpdate("DisplayBoard Status Updated"),
    DisplayStatusUpdateFail("Failed to Updated DisplayBoard Status"),
    ThemeGet("Got the list of Themes"),
    ThemeGetFail("Failed To Get list of Themes"),
    //Kiosk
    KioskGet("Got the list of DisplayBoards"),
    KioskGetFail("Failed To Get list of DisplayBoards"),
    KioskCreate("DisplayBoard Created"),
    KioskCreateFail("Failed to Create DisplayBoard"),
    KioskUpdate("DisplayBoard Updated"),
    KioskUpdateFail("Failed to Updated DisplayBoard"),
    KioskNameFail("Display Name Already exists"),
    KioskDelete("DisplayBoard Deleted"),
    KioskDeleteFail("Failed to Deleted DisplayBoard"),
    KioskStatusUpdate("DisplayBoard Status Updated"),
    KioskStatusUpdateFail("Failed to Updated DisplayBoard Status"),
    //Scanner
    ScannerGet("Got the list of Scanners"),
    ScannerGetFail("Failed To Get list of Scanners"),
    ScannerCreate("Scanner Created"),
    ScannerCreateFail("Failed to Create Scanner"),
    ScannerUpdate("Scanner Updated"),
    ScannerUpdateFail("Failed to Updated Scanner"),
    ScannerNameFail("Scanner Name Already exists"),
    ScannerDelete("Scanner Deleted"),
    ScannerDeleteFail("Failed to Deleted Scanner"),
    ScannerStatusUpdate("Scanner Status Updated"),
    ScannerStatusUpdateFail("Failed to Updated Scanner Status"),
    //Trigger
    TriggerGet("Got the list of Triggers"),
    TriggerGetFail("Failed To Get list of Triggers"),
    //Params
    ParamGet("Got the list of Params"),
    ParamGetFail("Failed To Get list of Params"),
    //MailNotification
    MailNotificationGet("Got the list of MailNotifications"),
    MailNotificationGetFail("Failed To Get list of MailNotifications"),
    MailNotificationCreate("MailNotification Created"),
    MailNotificationCreateFail("Failed to Create MailNotification"),
    MailNotificationUpdate("MailNotification Updated"),
    MailNotificationUpdateFail("Failed to Updated MailNotification"),
    MailNotificationTrigggerFail("TriggerId Already exists"),
    TestMsg("Test message Sucess"),
    TestMsgFail("Test message Failed"),
    //NotificationSms
    NotificationSmsGet("Got the list of NotificationSms"),
    NotificationSmsGetFail("Failed To Get list of NotificationSms"),
    NotificationSmsCreate("NotificationSms Created"),
    NotificationSmsCreateFail("Failed to Create NotificationSms"),
    NotificationSmsUpdate("NotificationSms Updated"),
    NotificationSmsUpdateFail("Failed to Updated NotificationSms"),
    NotificationSmsFailByEnterpriseAndTrigger("NotificationSms already created with this trigger to enterpriseId"),
    //ActiveDirectory
    ActiveDirectoryCheck("ActiveDirectory connection Checked Success"),
    ActiveDirectoryCheckFail("Failed To Check ActiveDirectory connection"),
    ActiveDirectoryGet("Got the list of ActiveDirectory users"),
    ActiveDirectoryGetFail("Failed To Get list of ActiveDirectory users"),
    ActiveDirectoryValid("ActiveDirectory Details valid"),
    ActiveDirectoryValidFail("Invalid ActiveDirectory Details"),
    //Menus
    MenuGet("Got the list of Menus"),
    MenuGetFail("Failed To Get list of Menus"),
    GettingTokens("Getting Tokens Successfully"),
    GettingTokensFail("Unable to get the tokens"),
    ActionFailed("Token action has been failed"),
    ServingSuccess("Call Token Serving Success"),
    RecallSuccess("Recall Success"),
    MoveToWaitQueue("Move to waiting Queue"),
    ServedSuccess("Served Success"),
    WaitingTokenNotExist("Token not exist in waiting queue"),
    ServingTokenNotExist("Token not exist in Serving queue"),
    ServingWatingTokenNotExist("Token not exist in Serving and Waiting queue"),
    MaxAllowedReached("allowd tokens in room reached."),
    InvalidRoomInfo("Invalid room selected"),
    InvalidAction("Invalid Action"),
    //
    ActiveDirectoryInfo("Active Directory Info not found."),
    UserLogin("Authantication & Authorization for User Login"),
    InvalidCreadential("Invalid Credentials."),
    UnableconnectActiveDirectory("Unable connect Active Directory"),
    AuthLogin("Login Successfully."),
    LocationDeactivate("Location deactivated."),
    EnterpriseDeactivate("Enterprise deactivated."),
    InvalidPassword("Invalid Password"),
    InvalidUsername("Invalid Username"),
    InvalidRequest("Invalid Request"),
    UserLogout("User Logout Process"),
    UserLogoutFail("User Logout Failed"),
    MappingUsersWithRoom("User with room mapped Successfully"),
    MappingUsersWithRoomFail("Failed to map User with Room"),
    MappingUsersWithService("Users Mapped with Service"),
    MappingUsersWithServiceFail("Failed to Map User with Service"),
    GetRoomsByUserId("Got the list of Rooms "),
    GetRoomsByUserIdFail("Failed To Get list of Rooms"),
    //Manualtoken
    ManualTokenCreate("ManualToken Created Succussfully"),
    ManualTokenCreateFail("Failed to  Create ManualToken"),
    SearchMrnNoFound("Found records with this mrnNo"),
    SearchMrnNoNotFound("Not Found records with this mrnNo"),
    TokenCheckInSuccess("Token CheckIn Successfully"),
    TokenCheckInFail("Token Failed to  CheckIn"),
    TokenRePrintSuccess("successfully Re-Print Token."),
    TokenRePrintFail("Failed to Re-Print Token."),
    GetAppointmentsByMrnNoAndKiosk("Got The List Of Appointments Succussfully"),
    GetAppointmentsByMrnNoAndKioskFail("Failed To Get List Of Appointments Appointments"),
    GetThemeByKioskSuccuss("Got the Theme of kiosk Succussfully"),
    GetThemeByKioskFail("Failed To Get the Theme of kiosk"),
    KioskTokenCreate("KioskToken Created Succussfully"),
    KioskTokenCreateFail("Failed to  Create KioskToken"),

    FeedBackCreate(
    "FeedBack Created"),
    FeedBackCreateFail("Failed to Create FeedBack");
    private final String msg;

    AuditMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

}
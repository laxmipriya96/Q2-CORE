/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.license4j.License;
import com.license4j.LicenseText;
import com.license4j.LicenseValidator;
import com.vm.qsmart2.model.TblEnterprise;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.enterprise.EnterpriseCreateRequest;
import com.vm.qsmart2api.dtos.enterprise.EnterpriseGDto;
import com.vm.qsmart2api.dtos.enterprise.EnterprisesDetails;
import com.vm.qsmart2api.dtos.enterprise.LicenseDetails;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.EnterpriseRepository;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 *
 * @author LENOVO
 */
@Service
public class EnterpriseService {

    @Autowired
    EnterpriseRepository enterpriseRepositary;

    @Autowired
    DateUtils dateUtils;

    private static final Logger logger = LogManager.getLogger(EnterpriseService.class);

    @Value("${license.dest.path}")
    private String path;
    
    public long save(String header, EnterpriseCreateRequest enterprise, long createBy) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>enterpriseName:[{}],createdBy:[{}]", header, enterprise.getEnterpriseNameEn(), createBy);
            }
            TblEnterprise tblEnterprise = Mapper.INSTANCE.createEnterpriseDtotoEnterpriseEntity(enterprise);
            tblEnterprise.setCreatedBy(createBy);
            tblEnterprise.setIsActive(1);
            tblEnterprise.setCreatedOn(dateUtils.getdate());
            tblEnterprise.setUpdatedBy(createBy);
            tblEnterprise.setUpdatedOn(dateUtils.getdate());
//            String[] splitArr = enterprise.getLogoPath().split("\\/");
//            File file = new File(path+splitArr[splitArr.length-1]);
//            System.out.println("File Path :"+file.getPath());
//            if (file.exists()) {
//                byte[] imgData = convertFileIntoByteArray(header, file);
//                tblEnterprise.setLogo(ArrayUtils.toObject(imgData));
//            }
            enterpriseRepositary.save(tblEnterprise);
            enterpriseRepositary.flush();
            System.out.println("id :" + tblEnterprise.getEnterpriseId());
            logger.info("{}>>EnterpriseId:{}", header, tblEnterprise.getEnterpriseId());
            return tblEnterprise.getEnterpriseId();
        } catch (Exception ex) {
            logger.error("{}Excep:createEnterprise:enterprise:{}:Error:{}", header, enterprise, ExceptionUtils.getRootCauseMessage(ex));
            return 0;
        }
    }

    public byte[] convertFileIntoByteArray(String header, File file) {
        DataInputStream dis = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>File:[{}]", header, file.getPath());
            }
            byte[] imgData = new byte[(int) file.length()];
            dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(imgData);  // read from file into byte[] array
            dis.close();
            return imgData;
        } catch (Exception e) {
            logger.error("{}Excep:convertFileIntoByteArray:File:{},Error:{}", header, file.getPath(), ExceptionUtils.getRootCauseMessage(e));
            return null;
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(EnterpriseService.class.getName()).log(Level.SEVERE, null, ex);
                }
                dis = null;
            }
        }
    }

    public long updateEnterprise(String header, EnterpriseGDto enterprise, long updatedBy) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>enterpriseName:[{}],createdBy:[{}]", header, updatedBy);
            }
            TblEnterprise tblEnterprise = Mapper.INSTANCE.updateEnterpriseDtotoEnterpriseEntity(enterprise);
            tblEnterprise.setUpdatedBy(updatedBy);
            tblEnterprise.setIsActive(enterprise.getIsActive());
            tblEnterprise.setUpdatedOn(dateUtils.getdate());
            enterpriseRepositary.save(tblEnterprise);
            enterpriseRepositary.flush();
            System.out.println("id :" + tblEnterprise.getEnterpriseId());
            logger.info("{}>>EnterpriseId:{}", header, tblEnterprise.getEnterpriseId());
            return tblEnterprise.getEnterpriseId();
        } catch (Exception ex) {
            logger.error("{}Excep:updateEnterprise:[{}]:enterprise:{}:Error:{}", header, enterprise, ExceptionUtils.getRootCauseMessage(ex));
            return 0;
        }
    }

    public EnterpriseGDto setLicneseDetailsToObj(String header, EnterpriseGDto dto, Locale locale) {
        LicenseDetails lDetails = readDetailsfromLicense(header, dto.getLicenseFile(), locale);
        dto.setLicenseInfo(lDetails);
        return dto;
    }

    public EnterprisesDetails getAllEnterprisesDetails(String header, Locale locale) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>", header);
            }
            // List<EnterpriseGDto> list = Mapper.INSTANCE.enterpriseEntityListToUpdateEnterpriseDtoList(enterpriseRepositary.findAllByOrderByEnterpriseIdDesc()).stream().map(obj -> setLicneseDetailsToObj(header, obj, locale)).collect(Collectors.toList());
            List<EnterpriseGDto> list = Mapper.INSTANCE.enterpriseEntityListToUpdateEnterpriseDtoList(enterpriseRepositary.findAllByOrderByEnterpriseIdDesc());//.stream().map(obj -> setLicneseDetailsToObj(header, obj, locale)).collect(Collectors.toList());
            logger.info("{}<<:enterprises:size:[{}]", header, list.size());
            return new EnterprisesDetails(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:getAllEnterprisesDetails:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            return new EnterprisesDetails(new ArrayList<>());
        }
    }

    public long updateEnterpriseStatus(String header, long enterpriseId, int status, long updatedBy) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>enterpriseId:{}:status:{}", header, enterpriseId, status);
            }
            Optional<TblEnterprise> opt = enterpriseRepositary.findById(enterpriseId);
            if (opt.isPresent()) {
                TblEnterprise tblEnterprise = opt.get();
                tblEnterprise.setUpdatedBy(updatedBy);
                tblEnterprise.setUpdatedOn(dateUtils.getdate());
                tblEnterprise.setIsActive(status);
                enterpriseRepositary.save(tblEnterprise);
                logger.info("{}<<:enterprisesId:[{}]", header, tblEnterprise.getEnterpriseId());
                return tblEnterprise.getEnterpriseId();
            }
            logger.info("{}<<:enterprisesId:[{}]", header, 0);
            return 0;
        } catch (Exception e) {
            logger.error("{}Excep:updateEnterpriseStatus:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            logger.info("{}<<:enterprisesId:[{}]", header, 0);
            return 0;
        }
    }

    @Autowired
    MessageSource messageSource;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static final String publickey = "30819f300d06092a864886f70d010101050003818d003081893032301006072a8648ce3d02002EC311215SHA512withECDSA106052b81040006031e000454c432c79e22480903c2fb12c6e91337819d60d9fea1ef41ad76b492G0281810092ad29643fb0a8d10048f2c59307bb89e867ca073539f46977bfd04724403ee12bf49c8c92a687ec1f89ca82a86e492d2e8d8db4b646e4eb4d1c70938d9737d4628b122f49f2518801a4ddd4844ddac566b9a4715da04b729031f15a9b69413603RSA4102413SHA512withRSAbdec450090859745a2fd7a764729ad8129dbe0283c4cc6c2636665b79a4806410203010001";

    public LicenseDetails readDetailsfromLicense(String header, String licenseFile, Locale locale) {
        LicenseDetails sResponse = null;
        License license = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>LicenseFilePath:[{}]", header, licenseFile);
            }
            license = LicenseValidator.validate(
                    FileUtils.readFileToString(new File(licenseFile)),
                    publickey,
                    null,
                    null,
                    null,
                    null,
                    null);
            switch (license.getValidationStatus()) {
                case LICENSE_VALID:
                    LicenseText lciTxt = license.getLicenseText();
                    sResponse = new LicenseDetails(true, 0, messageSource.getMessage("enterprise.ctrl.valid.license", null, locale));
                    sResponse.setLicneseNumber("" + lciTxt.getLicenseID());
                    sResponse.setFilePath(licenseFile);
                    sResponse.setLicenseExpireDate((sdf.format(lciTxt.getLicenseExpireDate())));
                    sResponse.setLicenseActivationDate((sdf.format(new Date(lciTxt.getLicenseGenerationDateTime()))));
                    break;
                case LICENSE_EXPIRED:
                    sResponse = new LicenseDetails(false, 0, messageSource.getMessage("enterprise.ctrl.license.expired", null, locale));
                    //sResponse = new LicenseResp(ValidationStatus.LICENSE_EXPIRED, "License Time Was Expired");
                    break;
                case LICENSE_MAINTENANCE_EXPIRED:
                    sResponse = new LicenseDetails(false, 0, messageSource.getMessage("enterprise.ctrl.license.time.expired", null, locale));
                    break;
                default:
                    sResponse = new LicenseDetails(false, 0, messageSource.getMessage("enterprise.ctrl.invalid.license", null, locale));
                    break;
            }
            logger.info("{}<<:valdiateAndReadData:Given:Response:[{}}", header, sResponse.toString());
            return sResponse;
        } catch (Exception ex) {
            sResponse = new LicenseDetails(false, 0, messageSource.getMessage("enterprise.ctrl.invalid.license", null, locale));
            logger.error("{}Excep:readDetailsfromLicense:licenseFile:{}:Error:[{}]", header, licenseFile, ExceptionUtils.getRootCauseMessage(ex));
            logger.info("{}<<:valdiateAndReadData:Given:Response:[{}}", header, sResponse.toString());
            return sResponse;
        }
    }
}

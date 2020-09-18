/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblBranchType;
import com.vm.qsmart2api.contoller.BranchController;
import com.vm.qsmart2api.dtos.branch.BranchDTO;
import com.vm.qsmart2api.dtos.branch.BranchGUDTO;
import com.vm.qsmart2api.dtos.branch.BranchTypeDTO;
import com.vm.qsmart2api.dtos.branch.BranchTypeGDTO;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.BranchTypeRepository;
import static com.vm.qsmart2api.service.BranchService.logger;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ashok
 */
@Service
public class BranchTypeService {

    public static final Logger logger = LogManager.getLogger(BranchTypeService.class);

    @Autowired
    BranchTypeRepository branchTypeRepository;

    public BranchTypeGDTO getAllBrancheTypes(String header, Long userId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>:getAllBrancheTypes:header:[{}]:locationId:[{}]", header);
            }
            List<TblBranchType> branchType = branchTypeRepository.findAll();
            List<BranchTypeDTO> branchDto = Mapper.INSTANCE.branchTypeToBranchTypeDto(branchType);
            logger.info("{}<<:branchetypes:[{}]", branchDto);
            return new BranchTypeGDTO(branchDto);
        } catch (Exception e) {
            logger.error("{}Excep:getAllBrancheTypes:header:{}:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            return new BranchTypeGDTO();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblParams;
import com.vm.qsmart2.model.TblTrigger;
import com.vm.qsmart2api.dtos.mailnotification.MailSmsDto;
import com.vm.qsmart2api.dtos.params.ParamDto;
import com.vm.qsmart2api.dtos.params.ParamGetDto;
import com.vm.qsmart2api.dtos.trigger.TriggerDto;
import com.vm.qsmart2api.dtos.trigger.TriggerGetDto;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.ParamRepository;
import com.vm.qsmart2api.repository.TriggerRepository;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tejasri
 */
@Service
public class TriggerService {

    private static final Logger logger = LogManager.getLogger(TriggerService.class);

    @Autowired
    ParamRepository paramRepository;

    @Autowired
    TriggerRepository triggerRepository;

    public TriggerGetDto getAllTriggers(String header, Long userId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>:getAllTriggers:header:[{}]", header);
            }
            List<TblTrigger> trigger = triggerRepository.findTriggersWithParams();
            List<TriggerDto> triggerDto = Mapper.INSTANCE.triggerToTriggerDto(trigger);
            logger.info("{}<<:getAllTriggers:[{}]", triggerDto);
            return new TriggerGetDto(triggerDto);
        } catch (Exception e) {
            logger.error("{}Excep:getAllTriggers:header:{}:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
            return new TriggerGetDto();
        }
    }

    public ParamGetDto getAllParams(String header, Long userId, Long triggerId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>:getAllParams:header:[{}]:triggerId:[{}]", header);
            }
            List<TblParams> param = paramRepository.findByTriggerId(triggerId);
            List<ParamDto> paramDto = Mapper.INSTANCE.paramsToParamDto(param);
            logger.info("{}<<:getAllParams:[{}]", paramDto);
            return new ParamGetDto(paramDto);
        } catch (Exception e) {
            logger.error("{}Excep:getAllParams:header:{}:triggerId:{}:Error:{}", header, triggerId, ExceptionUtils.getRootCauseMessage(e));
            return new ParamGetDto();
        }
    }

    public MailSmsDto saveMailOrSms(String header, Long userId, MailSmsDto mail) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>save:mail:[{}]", header, mail);
            }
            return mail;
        } catch (Exception e) {
            logger.error("{}Excep:saveMailOrSms:mail:{}:Error:{}", header, mail, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.model.v231.message;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v231.message.SIU_S12;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 *
 * @author Phani
 */
public class SIU_Z01 extends SIU_S12 {

    public SIU_Z01() throws HL7Exception {
        this(new DefaultModelClassFactory());
    }

    public SIU_Z01(ModelClassFactory factory) throws HL7Exception {
        super(factory);
        String[] segmentNamesInMessage = getNames();
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.enums;

/**
 *
 * @author Phani
 */
public enum Status {

    ACTIVE(1),
    INACTIVE(0);

    private int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    public Status getInstce(int value) {
        switch (value) {
            case 0:
                return ACTIVE;
            case 1:
                return INACTIVE;
            default:
                return ACTIVE;
        }
    }
}

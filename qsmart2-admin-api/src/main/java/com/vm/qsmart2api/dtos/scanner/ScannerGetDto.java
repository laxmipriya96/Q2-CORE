/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.scanner;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class ScannerGetDto {
    private List<ScannerUpDto> scanners;

    public ScannerGetDto() {
    }

    public ScannerGetDto(List<ScannerUpDto> scanners) {
        this.scanners = scanners;
    }

    public List<ScannerUpDto> getScanners() {
        return scanners;
    }

    public void setScanners(List<ScannerUpDto> scanners) {
        this.scanners = scanners;
    }

    @Override
    public String toString() {
        return "ScannerGetDto{" + "scanners=" + scanners + '}';
    }
}

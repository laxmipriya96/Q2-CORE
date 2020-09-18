/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.test;

import com.vm.qsmart2.utils.DateUtils;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

/**
 *
 * @author Phani
 */
public class DateTest {

    public static void main(String[] args) {
        DateUtils dateUtils = new DateUtils();
        if (isToday(dateUtils.datezFormat().parse("2020-06-07 12:30:00"))) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

    public static boolean isToday(TemporalAccessor date) {
        return java.time.LocalDate.now().equals(LocalDate.from(date));
    }
}

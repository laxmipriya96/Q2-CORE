/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    public Date getdate() {
        return new Date();
    }

    public static final ThreadLocal<DateFormat> sdf = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static final ThreadLocal<DateFormat> sdfYear = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static final ThreadLocal<DateFormat> sdfTime = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };

    public static final ThreadLocal<DateFormat> tranIdFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }
    };


    public DateTimeFormatter datezFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public String getCurrenDateTime() {
        return sdf.get().format(getdate());
    }

    public Date convertDtStringToDate(String dateString) throws ParseException {
        return sdf.get().parse(dateString);
    }

    public String convertDateToDtString(Date date) throws ParseException {
        return sdf.get().format(date);
    }

    public String getCurrentDaString() {
        return sdfYear.get().format(getdate());
    }

    public Date dayStartDateTime() throws ParseException {

        return sdf.get().parse(sdfYear.get().format(getdate()) + " " + "00:00:00");
    }

    public Date dayEndDateTime() throws ParseException {
        return sdf.get().parse(sdfYear.get().format(getdate()) + " " + "23:59:59");
    }

    public Date dayStartDateTime(Date date) throws ParseException {

        return sdf.get().parse(sdfYear.get().format(date) + " " + "00:00:00");
    }

    public Date dayEndDateTime(Date date) throws ParseException {
        return sdf.get().parse(sdfYear.get().format(date) + " " + "23:59:59");
    }

    public String generateUniqueTranId() {
        return tranIdFormat.get().format(getdate());
    }

    public String getCurrentTimeString() {
        return sdfTime.get().format(getdate());
    }

    public String getConvertTimeString(Date date) {
        return sdfTime.get().format(date);
    }
}

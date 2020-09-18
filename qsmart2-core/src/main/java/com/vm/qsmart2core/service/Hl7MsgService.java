/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.service;

import com.vm.qsmart2core.config.Hl7MsgConsumer;
import com.vm.qsmart2core.hl7.HL7MapStore;
import com.vm.qsmart2core.repositary.JdbcTemplateRepositary;
import com.vm.qsmart2core.utilities.RootInfo;
import com.vm.qsmart2core.utilities.SegmentField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Phani
 */
public class Hl7MsgService {

    private static final Logger logger = LogManager.getLogger(Hl7MsgConsumer.class);

    StringBuilder sb = null;

    JdbcTemplateRepositary hl7Repositary;

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    public Hl7MsgService() {
    }

    public Hl7MsgService(JdbcTemplateRepositary hl7Repositary) {
        this.hl7Repositary = hl7Repositary;
    }

    public long readDataFromHl7JSONString(String header, RootInfo rootInfo, HL7MapStore<String, HL7MapStore> store) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>:Insert:TableName:[{}]:StoreSize:[{}]", header, rootInfo.getTableName(), store.size());
            }
            sb = new StringBuilder();
            sb.append("insert into ").append(rootInfo.getTableName()).append(" (");
            //sb.append("location_id").append(",");
            for (SegmentField field : rootInfo.getFields()) {
                sb.append(field.getColumnName()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(") VALUES(");
            // sb.append(rootInfo.getLocationId()).append(",");
            for (SegmentField field : rootInfo.getFields()) {
                if (field.isIsHl7Segment()) {
                    String[] segmentIds = field.getSegmentId().split("\\|");
                    if (!field.isIsRepeatable()) {
                        if (!field.isIsConditional()) {
                            if (field.getDataType().equalsIgnoreCase("STRING")) {
                                String value = getValueByTagName(store, segmentIds[0]);
                                if (segmentIds.length > 1) {
                                    if (value != null && !value.isEmpty()) {
                                        sb.append("'").append(value).append("'").append(",");
                                    } else {
                                        value = getValueByTagName(store, segmentIds[1]);
                                        sb.append("'").append(value).append("'").append(",");
                                    }
                                } else {
                                    sb.append("'").append(value).append("'").append(",");
                                }
                            } else if (field.getDataType().equalsIgnoreCase("DATE")) {
                                String value = getValueByTagName(store, segmentIds[0]);
                                if (segmentIds.length > 1) {
                                    if (value != null && !value.isEmpty()) {
                                        sb.append("'").append(getDateValueFromString(value, field.getDefaultValue(), field.getExpectedFormat())).append("'").append(",");
                                    } else {
                                        value = getValueByTagName(store, segmentIds[1]);
                                        sb.append("'").append(getDateValueFromString(value, field.getDefaultValue(), field.getExpectedFormat())).append("'").append(",");
                                    }
                                } else {
                                    sb.append("'").append(getDateValueFromString(value, field.getDefaultValue(), field.getExpectedFormat())).append("'").append(",");
                                }
                            } else {
                                String value = getValueByTagName(store, segmentIds[0]);
                                if (segmentIds.length > 1) {
                                    if (value != null && !value.isEmpty()) {
                                        sb.append(value).append(",");
                                    } else {
                                        value = getValueByTagName(store, segmentIds[1]);
                                        sb.append(value).append(",");
                                    }
                                } else {
                                    sb.append("'").append(getDateValueFromString(value, field.getDefaultValue(), field.getExpectedFormat())).append("'").append(",");
                                }
                            }
                        } else {
                            String value = getValueByTagName(store, field.getConditionalSegmentId());
                            String segmentValue = "";
                            if (value.equalsIgnoreCase(field.getConditionalSegmentValue())) {
                                segmentValue = getValueByTagName(store, segmentIds[0]);
                                if (segmentIds.length > 1) {
                                    if (segmentValue != null && !segmentValue.isEmpty()) {
                                        sb.append(segmentValue).append(",");
                                    } else {
                                        value = getValueByTagName(store, segmentIds[1]);
                                        sb.append(value).append(",");
                                    }
                                } else {
                                    sb.append("'").append(segmentValue).append("'").append(",");
                                }
                            } else {
                                sb.append("'").append(segmentValue).append("'").append(",");
                            }
                        }
                    } else {
                        if (!field.isIsConditional()) {
                            String segementValue = getSegmentValueFromRepeatableSegement(store, field.getRepeatSegment(), field.getSegmentId());
                            sb.append("'").append(segementValue).append("'").append(",");
                        } else {
                            String segementValue = "";
                            segementValue = getValueFromRepeatSegmentWithConditionInSameSegment(store,
                                    field.getRepeatSegment(),
                                    field.getSegmentId(),
                                    field.getConditionalSegmentId(),
                                    field.getConditionalSegmentValue(),
                                    field.isSameSegment());
                            sb.append("'").append(segementValue).append("'").append(",");
                        }
                    }

                } else {
                    if (field.getDataType().equalsIgnoreCase("STRING")) {
                        sb.append("'");
                        sb.append(field.getDefaultValue());
                        sb.append("'").append(",");
                    } else {
                        sb.append(field.getDefaultValue()).append(",");
                    }
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            return hl7Repositary.saveDataIntoDB(header, sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:insertIntoDb:Error:{}", header, ExceptionUtils.getStackTrace(e));
            return 0;
        } finally {
            sb = null;
        }

    }

    private String getValueFromRepeatSegmentWithConditionInSameSegment(HL7MapStore<String, HL7MapStore> store,
            String repeatableSegement,
            String segementValue,
            String conditinSegment,
            String conditinValue, boolean isSameSegment) {
        try {
            //String segmentName = getSegmentName(repeatableSegement);
            logger.info(">>repeatableSegment:{},segmentvalue:{},conditionalsegment:{},conditionvalue:{},samesegment:{}",
                     repeatableSegement, segementValue, conditinSegment, conditinValue, isSameSegment);
            List<HL7MapStore> hl7MapStr = store.get(repeatableSegement);
            for (HL7MapStore hL7MapStore1 : hl7MapStr) {
//                System.out.println("--->< : " + hL7MapStore1);
                String fieldPath = getFieldName(conditinSegment);
                List<Map<String, String>> listMaps = hL7MapStore1.get(fieldPath);
//                System.out.println("Original--->" + listMaps);
                for (Map<String, String> listMap : listMaps) {
//                    System.out.println("sdafd--->" + listMap + "\t condition segment :" + conditinSegment + "\t conditional value :" + conditinValue);
                    String findValue = listMap.get(conditinSegment);
//                    System.out.println("Find Value :" + findValue);
                    if (findValue != null) {
                        if (findValue.equalsIgnoreCase(conditinValue)) {
                            if (isSameSegment) {
                                return listMap.get(segementValue);
                            } else {
                                String fieldPath2 = getFieldName(segementValue);
                                List<Map<String, String>> list = hL7MapStore1.get(fieldPath2);
                                if (list != null && !list.isEmpty()) {
                                    return list.get(0).get(segementValue);
                                }
                            }

                        }
                    }
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getSegmentValueFromRepeatableSegement(HL7MapStore<String, HL7MapStore> store,
            String repeatableSegement,
            String segementValue) {
        List<HL7MapStore> hl7MapStr = store.get(repeatableSegement);
        // System.out.println("---> " + hl7MapStr);
        for (HL7MapStore hL7MapStore1 : hl7MapStr) {
            String fieldPath = getFieldName(segementValue);
            List<Map<String, String>> listMaps = hL7MapStore1.get(fieldPath);
            for (Map<String, String> map : listMaps) {
                String valu = map.get(segementValue);
                // System.out.println("==========>=====>" + valu);
                if (valu != null) {
                    return valu;
                }
            }
        }
        return "";
    }

    private String getDateValueFromString(String result, String inputFromat, String expectedFormat) throws ParseException {
        try {

            if (result.length() > expectedFormat.length()) {
                result = result.substring(0, expectedFormat.length());
            }
            Date date1 = new SimpleDateFormat(inputFromat).parse(result);
            // LocalDateTime dateTime = Instant.ofEpochMilli(date1.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
            //Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            //return result;
            return new SimpleDateFormat(expectedFormat).format(date1);
        } catch (Exception e) {
            e.printStackTrace();
            Date date = new Date(0);
            SimpleDateFormat form = new SimpleDateFormat(expectedFormat);
            return form.format(date);
        }
    }

    private String getSegmentName(String path) {
        return path.substring(0, path.indexOf("."));
    }

    private int ordinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal, final boolean lastIndex) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        int index = lastIndex ? str.length() : -1;
        do {
            if (lastIndex) {
                index = str.toString().lastIndexOf(searchStr.toString(), index - searchStr.length());
            } else {
                index = str.toString().indexOf(searchStr.toString(), index + searchStr.length());
            }
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

    private String getFieldName(String path) {
        int index = ordinalIndexOf(path, ".", 2, false);
        index = index < 1 ? path.length() : index;
        return path.substring(0, index);
    }

    private String getValueByTagName(HL7MapStore<String, HL7MapStore> store, String path) {
        try {
            String segmentName = getSegmentName(path);
            // System.out.println("Path:" + path);
            //System.out.println("segmentName:" + segmentName);
            List<HL7MapStore> hL7MapStore = store.get(segmentName);
            for (HL7MapStore hL7MapStore1 : hL7MapStore) {
                String fieldPath = getFieldName(path);
                //System.out.println("==>" + fieldPath);
                if (hL7MapStore1.containsKey(fieldPath)) {
                    //System.out.println("==>" + hL7MapStore1.get(fieldPath));
                }
                List<Map> listMaps = hL7MapStore1.get(fieldPath);
                //System.out.println("List map size ==>" + listMaps.size());
                if (listMaps.size() == 1) {
                    //System.out.println("==>" + listMaps.get(0).get(path));
                    return listMaps.get(0).get(path).toString();
                } else {
                    //TODO multiful sub fileds
                    //System.out.println("==>" + listMaps.get(0).get(path));
                    return listMaps.get(0).get(path).toString();
                }
            }
            return "";

        } catch (Exception e) {
            logger.error("Excep:getValueByTagName:Error:{}", path);
            return "";
        }
    }

}

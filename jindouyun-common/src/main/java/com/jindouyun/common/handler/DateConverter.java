package com.jindouyun.common.handler;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class DateConverter implements Converter<String, LocalDateTime> {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy-MM-dd";
    private static final String timeStampFormat = "^\\d+$";

    private static final String hDateFormat = "yyyy年MM月dd日 HH:mm:ss";
    private static final String hshortDateFormat = "yyyy年MM月dd日";

    @Override
    public LocalDateTime convert(String value) {

        if (StringUtils.isEmpty(value)) {
            return null;
        }

        value = value.trim();

        try {
            DateTimeFormatter df;
            LocalDateTime time = null;
            if (value.contains("-")) {
//                SimpleDateFormat formatter;
                if (value.contains(":")) {
                    df = DateTimeFormatter.ofPattern(dateFormat);
//                    formatter = new SimpleDateFormat(dateFormat);
                } else {
                    df = DateTimeFormatter.ofPattern(shortDateFormat);
//                    formatter = new SimpleDateFormat(shortDateFormat);
                }
//                return formatter.parse(value);
                time = LocalDateTime.parse(value,df);
            } else if (value.matches(timeStampFormat)) {
                Long timestamp = Long.parseLong(value);
                time = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            } else if (value.contains("年")) {
//                SimpleDateFormat formatter;
                if (value.contains(":")) {
                    df = DateTimeFormatter.ofPattern(hDateFormat);
//                    formatter = new SimpleDateFormat(hDateFormat);
                } else {
                    df = DateTimeFormatter.ofPattern(hshortDateFormat);
//                    formatter = new SimpleDateFormat(hshortDateFormat);
                }
                time = LocalDateTime.parse(value,df);
            }

            if(time == null) throw new RuntimeException(String.format("parser %s to LocalDateTime fail", value));

            return time;
        } catch (Exception e) {
            throw new RuntimeException(String.format("parser %s to LocalDateTime fail", value));
        }
    }
}

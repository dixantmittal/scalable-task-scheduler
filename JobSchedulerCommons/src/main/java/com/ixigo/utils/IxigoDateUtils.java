package com.ixigo.utils;

import com.ixigo.constants.CommonConstants;
import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by dixant on 27/03/17.
 */
public class IxigoDateUtils extends DateUtils {

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(CommonConstants.DATE_PATTERN);

    public static String dateToString(LocalDateTime date) {
        return date.format(dateFormat);
    }

    public static LocalDateTime parse(String text) {
        return LocalDateTime.parse(text, dateFormat);
    }
}

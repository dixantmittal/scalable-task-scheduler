package org.dixantmittal.utils;

import org.dixantmittal.constants.CommonConstants;
import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by dixant on 27/03/17.
 */
public class IxigoDateUtils extends DateUtils {

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(CommonConstants.DATE_PATTERN);

    public static String dateToString(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(dateFormat);
    }

    public static LocalDateTime parse(String text) {
        return LocalDateTime.parse(text, dateFormat);
    }
}

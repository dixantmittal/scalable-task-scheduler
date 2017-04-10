package com.ixigo.utils;

import com.ixigo.constants.CommonConstants;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dixant on 27/03/17.
 */
public class IxigoDateUtils extends DateUtils {
    private static final String datePattern = CommonConstants.DATE_PATTERN;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    public static Calendar add(Calendar date, int calenderField, int value) {
        Calendar newDate = Calendar.getInstance();
        newDate.setTime(date.getTime());
        newDate.add(calenderField, value);
        return newDate;
    }

    public static String dateToString(Calendar date) {
        return dateFormat.format(date.getTime());
    }
}

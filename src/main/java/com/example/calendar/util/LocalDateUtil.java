package com.example.calendar.util;

import java.time.LocalDate;

public class LocalDateUtil {
    public static int getStartOffset(LocalDate currentDate) {
        return currentDate.withDayOfMonth(1).getDayOfWeek().getValue();
    }

    public static int getDayPreviousMonth(LocalDate currentDate) {
        int startOffset = LocalDateUtil.getStartOffset(currentDate);
        return currentDate.minusMonths(1).lengthOfMonth() - (startOffset - 2);
    }
}

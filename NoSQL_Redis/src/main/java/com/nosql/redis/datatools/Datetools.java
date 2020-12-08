package com.nosql.redis.datatools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Datetools {

    public enum TimeUnit {
        DAY,
        HOUR,
        MIN,
        SEC,
        ;
    }

    public static List<DateSplit> splitDate(Date startTime, Date endTime, TimeUnit timeUnit, int interval) {
        if (interval < 0) {
            return null;
        }
        if (endTime.getTime() <= startTime.getTime()) {
            return null;
        }

        if (timeUnit == TimeUnit.DAY) {
            return splitByDay(startTime, endTime, interval);
        }
        if (timeUnit == TimeUnit.HOUR) {
            return splitByHour(startTime, endTime, interval);
        }
        if (timeUnit == TimeUnit.MIN) {
            return splitByMinute(startTime, endTime, interval);
        }
        if (timeUnit == TimeUnit.SEC) {
            return splitBySecond(startTime, endTime, interval);
        }
        return null;
    }

    /**
     * 按照小时切割时间区间
     */
    public static List<DateSplit> splitByHour(Date start, Date end, int intervalHours) {
        if (end.getTime() <= start.getTime()) {
            return null;
        }

        List<DateSplit> dateSplits = new ArrayList<>(256);

        DateSplit param = new DateSplit();
        param.setStartDateTime(start);
        param.setEndDateTime(end);
        param.setEndDateTime(addHours(start, intervalHours));
        while (true) {
            param.setStartDateTime(start);
            Date tempEndTime = addHours(start, intervalHours);
            if (tempEndTime.getTime() >= end.getTime()) {
                tempEndTime = end;
            }
            param.setEndDateTime(tempEndTime);

            dateSplits.add(new DateSplit(param.getStartDateTime(), param.getEndDateTime()));

            start = addHours(start, intervalHours);
            if (start.getTime() >= end.getTime()) {
                break;
            }
            if (param.getEndDateTime().getTime() >= end.getTime()) {
                break;
            }
        }
        return dateSplits;
    }

    /**
     * 按照秒切割时间区间
     */
    public static List<DateSplit> splitBySecond(Date startTime, Date endTime, int intervalSeconds) {
        if (endTime.getTime() <= startTime.getTime()) {
            return null;
        }
        List<DateSplit> dateSplits = new ArrayList<>(256);

        DateSplit param = new DateSplit();
        param.setStartDateTime(startTime);
        param.setEndDateTime(endTime);
        param.setEndDateTime(addSeconds(startTime, intervalSeconds));
        while (true) {
            param.setStartDateTime(startTime);
            Date tempEndTime = addSeconds(startTime, intervalSeconds);
            if (tempEndTime.getTime() >= endTime.getTime()) {
                tempEndTime = endTime;
            }
            param.setEndDateTime(tempEndTime);

            dateSplits.add(new DateSplit(param.getStartDateTime(), param.getEndDateTime()));

            startTime = addSeconds(startTime, intervalSeconds);
            if (startTime.getTime() >= endTime.getTime()) {
                break;
            }
            if (param.getEndDateTime().getTime() >= endTime.getTime()) {
                break;
            }
        }
        return dateSplits;
    }

    /**
     * 按照天切割时间区间
     */
    public static List<DateSplit> splitByDay(Date startTime, Date endTime, int intervalDays) {
        if (endTime.getTime() <= startTime.getTime()) {
            return null;
        }
        List<DateSplit> dateSplits = new ArrayList<>(256);

        DateSplit param = new DateSplit();
        param.setStartDateTime(startTime);
        param.setEndDateTime(endTime);
        param.setEndDateTime(addDays(startTime, intervalDays));
        while (true) {
            param.setStartDateTime(startTime);
            Date tempEndTime = addDays(startTime, intervalDays);
            if (tempEndTime.getTime() >= endTime.getTime()) {
                tempEndTime = endTime;
            }
            param.setEndDateTime(tempEndTime);

            dateSplits.add(new DateSplit(param.getStartDateTime(), param.getEndDateTime()));

            startTime = addDays(startTime, intervalDays);
            if (startTime.getTime() >= endTime.getTime()) {
                break;
            }
            if (param.getEndDateTime().getTime() >= endTime.getTime()) {
                break;
            }
        }
        return dateSplits;
    }

    /**
     * 按照分钟切割时间区间
     *
     * @param startTime
     * @param endTime
     * @param intervalMinutes
     * @return
     */
    public static List<DateSplit> splitByMinute(Date startTime, Date endTime, int intervalMinutes) {
        if (endTime.getTime() <= startTime.getTime()) {
            return null;
        }
        List<DateSplit> dateSplits = new ArrayList<>(256);

        DateSplit param = new DateSplit();
        param.setStartDateTime(startTime);
        param.setEndDateTime(endTime);
        param.setEndDateTime(addMinute(startTime, intervalMinutes));
        while (true) {
            param.setStartDateTime(startTime);
            Date tempEndTime = addMinute(startTime, intervalMinutes);
            if (tempEndTime.getTime() >= endTime.getTime()) {
                tempEndTime = endTime;
            }
            param.setEndDateTime(tempEndTime);

            dateSplits.add(new DateSplit(param.getStartDateTime(), param.getEndDateTime()));

            startTime = addMinute(startTime, intervalMinutes);
            if (startTime.getTime() >= endTime.getTime()) {
                break;
            }
            if (param.getEndDateTime().getTime() >= endTime.getTime()) {
                break;
            }
        }
        return dateSplits;
    }



    private static Date addDays(Date date, int days) {
        return add(date, Calendar.DAY_OF_MONTH, days);
    }

    private static Date addHours(Date date, int hours) {
        return add(date, Calendar.HOUR_OF_DAY, hours);
    }

    private static Date addMinute(Date date, int minute) {
        return add(date, Calendar.MINUTE, minute);
    }
    private static Date addSeconds(Date date, int second) {
        return add(date, Calendar.SECOND, second);
    }

    private static Date add(final Date date, final int calendarField, final int amount) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    private static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return simpleDateFormat.format(date);
    }

    public static class DateSplit {
        private Date startDateTime;
        private Date endDateTime;

        public DateSplit() {
        }

        public DateSplit(Date startDateTime, Date endDateTime) {
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
        }

        public String getStartDateTimeStr() {
            return formatDateTime(startDateTime);
        }

        public String getEndDateTimeStr() {
            return formatDateTime(endDateTime);
        }

        public Date getStartDateTime() {
            return startDateTime;
        }

        public Date getEndDateTime() {
            return endDateTime;
        }

        public void setStartDateTime(Date startDateTime) {
            this.startDateTime = startDateTime;
        }

        public void setEndDateTime(Date endDateTime) {
            this.endDateTime = endDateTime;
        }
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat strToDate = new SimpleDateFormat("yyyyMMddHHmm");
        Date start = strToDate.parse("202011221200");
        Date end = strToDate.parse("202011222300");
        List<Datetools.DateSplit> dateSplits = Datetools.splitDate(start, end, TimeUnit.HOUR, 1);

        for(Datetools.DateSplit dateSplit : dateSplits)
        {
            System.out.println(dateSplit.getStartDateTimeStr() + "--->" + dateSplit.getEndDateTimeStr());
        }
    }
}

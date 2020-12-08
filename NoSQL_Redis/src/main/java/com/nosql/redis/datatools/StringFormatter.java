package com.nosql.redis.datatools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringFormatter {

    public static String[] StringFormat(String timeStamp) {
        String[] res = timeStamp.split("\\s+");
        SimpleDateFormat strToDate = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat dateToStr = new SimpleDateFormat("yyyyMMddHH00");
        for(int i = 0; i < res.length; i++)
        {
            try {
                Date date = strToDate.parse(res[i]);
                res[i] = dateToStr.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}

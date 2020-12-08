package com.nosql.redis.factory;

import com.nosql.redis.counter.Counter;
import com.nosql.redis.datatools.Datetools;
import com.nosql.redis.datatools.StringFormatter;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FreqCounter implements DataTypeCounter {

    private Counter counter;
    private Jedis jedis;

    @Override
    public void initiate(Counter counter, Jedis jedis) {
        this.counter = counter;
        this.jedis = jedis;
    }

    @Override
    public String executeCount() throws ParseException {
        //Assign key, changedValue and time period
        String key = counter.getKeyFields();
        String field = counter.getFields();
        String changedValue = counter.getValueFields();

        AtomicReference<String> info = new AtomicReference<>("Error: Operation failed. Type: Freq.");

        if(key != null) {
            if(jedis.exists(key)) {
                if(field != null) {
                    String[] t = StringFormatter.StringFormat(field);
                    if(t.length == 1) {
                        if(jedis.hexists(key, t[0])) {
                            if(changedValue != null) {
                                long change = Long.parseLong(changedValue);
                                jedis.hincrBy(key, t[0], change);
                                info.set("key: " + key + " - Period: " + t[0] +
                                        "- Changed: " + change + "- To: " + jedis.hget(key, t[0]) + ".");
                            } else {
                                info.set("key: " + key + " - Period: " + t[0] +
                                        "- Value: " + jedis.hget(key, t[0]) + ".");
                            }
                        } else {
                            if(changedValue != null) {
                                jedis.hset(key, t[0], changedValue);
                                info.set("SET key: " + key + " - Period: " + t[0] +
                                                "- Value: " + changedValue + ".");
                            } else {
                                info.set("Sorry, current Key cannot be found in storage.");
                            }
                        }
                    } else if (t.length == 2) {
                        String from_Time = t[0];
                        String to_Time = t[1];

                        SimpleDateFormat strToDate = new SimpleDateFormat("yyyyMMddHHmm");

                        Date start = strToDate.parse(from_Time);
                        Date end = strToDate.parse(to_Time);

                        if (end.getTime() <= start.getTime()) {
                            return null;
                        }

                        List<Datetools.DateSplit> dateSplits = new ArrayList<>(256);

                        Datetools.DateSplit param = new Datetools.DateSplit();
                        param.setStartDateTime(start);
                        param.setEndDateTime(end);

                        param.setEndDateTime(add_1_Hour(start,Calendar.HOUR_OF_DAY));
                        while (true) {
                            param.setStartDateTime(start);
                            Date tempEndTime = add_1_Hour(start, Calendar.HOUR_OF_DAY);
                            if (tempEndTime.getTime() >= end.getTime()) {
                                tempEndTime = end;
                            }
                            param.setEndDateTime(tempEndTime);

                            dateSplits.add(new Datetools.DateSplit(param.getStartDateTime(), param.getEndDateTime()));

                            start = add_1_Hour(start, Calendar.HOUR_OF_DAY);
                            if (start.getTime() >= end.getTime()) {
                                break;
                            }
                            if (param.getEndDateTime().getTime() >= end.getTime()) {
                                break;
                            }
                        }

                        List<String> timeKeys = new ArrayList<>();
                        for(int i = 0; i < dateSplits.toArray().length; i++)
                        {
                            timeKeys.add(dateSplits.get(i).getStartDateTimeStr());
                        }
                        long total = 0;
                        for(int i = 0; i < timeKeys.size(); i++)
                        {
                            if(jedis.hexists(key, timeKeys.get(i))) {
                                total += Long.parseLong(jedis.hget(key, timeKeys.get(i)));
                            }
                        }
                        info.set("键:" + key + "，时段" + from_Time + "-->" + to_Time + "，总和为：" + total);
                    }
                } else {
                    jedis.hgetAll(key).forEach((k, v) -> {
                        System.out.println("field:" + k + "，changedValue:" + v);
                    });
                    info.set("以上为该key中所有field和field的值");
                }
            } else {
                if(field != null) {
                    String[] t = StringFormatter.StringFormat(field);
                    if(t.length == 1) {
                        if(changedValue != null) {
                            jedis.hset(key, t[0], changedValue);
                            info.set("键:" + key + "，时段：" + t[0] + "，设置值为：" + changedValue);
                        }
                    }
                }
            }
        }

        return info.get();
    }

    private static Date add_1_Hour(final Date date, final int calendarField) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, 1);
        return c.getTime();
    }

}

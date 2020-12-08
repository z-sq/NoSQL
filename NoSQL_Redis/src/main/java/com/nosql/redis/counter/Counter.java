package com.nosql.redis.counter;

public class Counter {

    private String counterName;
    private String counterIndex;
    private String type;
    private String keyFields;
    private String fields;
    private String valueFields;
    private int maxSize;
    private int timePeriod;

    public String getCounterName() {
        return counterName;
    }

    public String getCounterIndex() {
        return counterIndex;
    }

    public String getType() {
        return type;
    }

    public String getKeyFields() {
        return keyFields;
    }

    public String getValueFields() {
        return valueFields;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getExpireTime() {
        return timePeriod;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public void setCounterIndex(String counterIndex) {
        this.counterIndex = counterIndex;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKeyFields(String keyFields) {
        this.keyFields = keyFields;
    }

    public void setValueFields(String valueFields) {
        this.valueFields = valueFields;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setExpireTime(int expireTime) {
        this.timePeriod = expireTime;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "counterName='" + counterName + '\'' +
                ", counterIndex='" + counterIndex + '\'' +
                ", type='" + type + '\'' +
                ", keyFields='" + keyFields + '\'' +
                ", fields='" + fields + '\'' +
                ", valueFields='" + valueFields + '\'' +
                ", maxSize=" + maxSize +
                ", timePeriod=" + timePeriod +
                '}';
    }
}

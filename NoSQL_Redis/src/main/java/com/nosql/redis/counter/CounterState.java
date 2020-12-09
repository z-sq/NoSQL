package com.nosql.redis.counter;

/**
 * @ClassName:    CounterState
 * @Package:      com.nosql.redis.domain
 * @File:         CounterState.java
 * @Description:  Write counter state to Json file.
 * @Author:       Shadow Zhu
 */
public class CounterState {

    private String counterName;

    private String state;


    public String getCounterName() {
        return counterName;
    }

    public String getState() {
        return state;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public void setCondition(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "CounterRetrieveState{" +
                "counterName='" + counterName + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}

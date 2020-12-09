package com.nosql.redis.Actions;

import com.nosql.redis.counter.CounterState;

import java.util.List;

public class Action {

    private List<CounterState> retrieve;

    private List<CounterState> save;

    public List<CounterState> getRetrieve() {
            return retrieve;
    }

    public List<CounterState> getSave() {
        return save;
    }

    public void setRetrieve(List<CounterState> retrieve) {
        this.retrieve = retrieve;
    }

    public void setSave(List<CounterState> save) {
        this.save = save;
    }


    public Action(List<CounterState> retrieve, List<CounterState> save) {
        this.retrieve = retrieve;
        this.save = save;
    }

    @Override
    public String toString() {
        return "Action{" +
                "retrieve=" + retrieve +
                ", save=" + save +
                '}';
    }
}

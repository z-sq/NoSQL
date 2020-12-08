package com.nosql.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nosql.redis.Actions.Action;
import com.nosql.redis.counter.Counter;
import com.nosql.redis.counter.CounterState;
import com.nosql.redis.factory.TypeFactory;
import com.nosql.redis.datatools.FileHelper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    // Mapper for counter
    private static final HashMap<String, Counter> counterMapper = new HashMap<>();
    // Mapper for actions
    private static final HashMap<String, Action> actionMapper = new HashMap<>();
    // Creat the Factory for different data types. (FACTORY MODE)
    private static final TypeFactory typeFactory = new TypeFactory();

    private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static final PrintWriter out = new PrintWriter(System.err, true);

    // JSON Monitor
    private static FileAlterationMonitor monitor;
    // Access of data modifying.
    public static AtomicBoolean access = new AtomicBoolean(false);


    private static void dynamicReadJson() throws Exception {
        String monitorDir = "src/main/resources";
        long pollingInterval = TimeUnit.SECONDS.toMillis(1);
        FileAlterationObserver observer = new FileAlterationObserver(monitorDir);
        observer.addListener(new FileHelper());
        monitor = new FileAlterationMonitor(pollingInterval, observer);
        monitor.start();
        out.println(">>> Start Observing <<<");
    }

    public static void loadDataFromJson() {
        try {
            out.println();
            out.println(">>> LOADING FROM JSON FILES <<<");

            //Initiate Mappers
            counterMapper.clear();
            actionMapper.clear();

            String actionsPath = "src/main/resources/actions.json";
            String countersPath = "src/main/resources/counters.json";

            //Load JSON file to strings.
            InputStream inputStream = new FileInputStream(actionsPath);
            String actionsText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream = new FileInputStream(countersPath);
            String countersText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            //Load actions into actionMap from JSON object.
            JSONObject obj1 = JSON.parseObject(actionsText);
            JSONArray actions = obj1.getJSONArray("actions");
            for(int i = 0; i < actions.size(); i++) {
                String actionName = (String) actions.getJSONObject(i).get("name");
                List<CounterState> retrieve = new ArrayList<>();
                List<CounterState> save = new ArrayList<>();
                JSONArray retrieveArray = actions.getJSONObject(i).getJSONArray("retrieve");
                JSONArray saveArray = actions.getJSONObject(i).getJSONArray("save");
                for(int j = 0; j < retrieveArray.size(); j++)
                    retrieve.add(retrieveArray.getJSONObject(j).toJavaObject(CounterState.class));
                for(int j = 0; j < saveArray.size(); j++)
                    save.add(saveArray.getJSONObject(j).toJavaObject(CounterState.class));
                Action action = new Action(retrieve, save);
                actionMapper.put(actionName, action);
            }

            // Load counter into counterMapper from JSON object.
            JSONObject obj2 = JSON.parseObject(countersText);
            JSONArray counters = obj2.getJSONArray("counters");
            for(int i = 0; i < counters.size(); i++) {
                Counter counter = counters.getJSONObject(i).toJavaObject(Counter.class);
                counterMapper.put(counter.getCounterName(), counter);
            }
            out.println(">>> LOADED <<<");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Counter> resolveAction(String actionName) {
        Action action = actionMapper.get(actionName);
        List<Counter> counterList = new ArrayList<>();
        action.getRetrieve().forEach(counterState -> {
            counterList.add(counterMapper.get(counterState.getCounterName()));
        });
        action.getSave().forEach(counterState -> {
            counterList.add(counterMapper.get(counterState.getCounterName()));
        });
        return counterList;
    }

    public static void resolveCounters(List<Counter> counterList) {
        counterList.forEach(counter -> {
            System.out.println(counter.getCounterName() + ">>> EXECUTING ... <<<");
            String res = null;
            try {
                res = typeFactory.getCounter(counter.getType(), counter).executeCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(res);
        });
        out.println();
    }

    private static int getChoice() throws IOException {
        int input;
        do {
            try {
                out.println();
                out.print(
                        "[0]  ----- Check exist option ----"
                      + "\n"
                      + "[1]  ------ Execute Actions ------"
                      + "\n"
                      + "[2]  ----------- EXIT ------------"
                      + "\n"
                      + "Your Choice>> ");
                out.flush();

                input = Integer.parseInt(in.readLine());

                out.println();

                if (0 <= input && 3 >= input) {
                    break;
                } else {
                    out.println("Illegal choice:  " + input);
                }
            } catch (NumberFormatException nfe) {
                out.println(nfe);
            }
        } while (true);

        return input;
    }

    private static void showAllActions() {
        out.println("Here are the options you got: ");
        actionMapper.forEach((name, action) -> out.println(name));
    }

    private static void toResolveAction() throws IOException {
        out.print("Now, choose your action: ");
        out.flush();
        String name = in.readLine();
        // check if the key is in the actionMapper
        if (actionMapper.containsKey(name)) {
            // Get the specific counter with the given name.
            List<Counter> counters = resolveAction(name);
            // Execute counting
            resolveCounters(counters);
        } else {
            out.println("--- Non-exist Action ---");
        }
    }


    public static void main(String[] args) throws Exception {
        out.println("---------------- Welcome, Samurai ---------------");
        out.println("--- This is the CYBERPUNNNNNNK Counter System ---");

        dynamicReadJson();
        loadDataFromJson();

        out.println("------- Make Your choice, wisely you must -------");

        int choice = getChoice();
        while (choice != 2) {
            access.compareAndSet(false, true);
            if (choice == 0) {
                showAllActions();
            } else if (choice == 1) {
                toResolveAction();
            }
            access.set(false);
            choice = getChoice();
        }
        monitor.stop();
        out.println("------ Quit is choice for the weaks -----");

        out.println("-------------- SYSTEM OFFLINE ------------");
        System.exit(0);
    }

    public static HashMap<String, Counter> getCounterMapper() {
        return counterMapper;
    }

    public static HashMap<String, Action> getActionMapper() {
        return actionMapper;
    }
}

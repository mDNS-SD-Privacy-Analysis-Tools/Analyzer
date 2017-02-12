package analyzer.rest;

import analyzer.utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by codewing on 01.02.2017.
 */
public class Database {
    private static Database db = null;
    private HashMap<String, ArrayList<LogEntry>> originLogs = new HashMap<>();

    public Database() {
        refresh();
    }

    public static Database get(){
        if(db == null){
            db = new Database();
        }
        return db;
    }

    public void refresh(){
        Set<String> origins = DataUtils.getOrigins();
        for (String origin : origins){
            originLogs.put(origin, new ArrayList<>());
        }
    }

    public Set<String> getOrigins(){
        return originLogs.keySet();
    }

    public void setLogsForOrigin(String origin, ArrayList<LogEntry> logEntries){
        originLogs.put(origin, logEntries);
    }

    public ArrayList<LogEntry> getLogs(String origin){
        ArrayList<LogEntry> logEntries = originLogs.get(origin);
        if(logEntries == null || logEntries.isEmpty()){
            logEntries = DataUtils.getForOrigin(origin);
            setLogsForOrigin(origin, logEntries);
        }
        return logEntries;
    }

    public Set<String> getServices(String origin){
        return DataUtils.getDistinctServices(getLogs(origin));
    }

    public Set<String> getServers(String origin){
        return DataUtils.getDistinctServers(getLogs(origin));
    }

    public List<LogEntry> getLogsForService(String origin, String service){
        return DataUtils.filterLogsByService(service, getLogs(origin));
    }

    public List<LogEntry> getLogsForServer(String origin, String server){
        return DataUtils.filterLogsByServer(server, getLogs(origin));
    }

}

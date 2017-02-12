package analyzer.utils;

import analyzer.rest.LogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by codewing on 01.02.2017.
 */
public class DataUtils {
    public static Set<String> getOrigins(){
        try {
            HttpResponse<JsonNode> response = Unirest.get(Cfg.getCfg().getServerURL()+"/log/getOrigins")
                    .header("accept", "application/json")
                    .asJson();
            JsonNode body = response.getBody();
            JSONArray originJson = body.getObject().getJSONArray("strings");
            Set<String> origins = new HashSet<>();
            for (int i = 0; i < originJson.length(); i++){
                origins.add(originJson.getString(i));
            }
            return origins;
        }catch (UnirestException e){
            System.err.println(e);
        }
        return new HashSet<>();
    }

    public static ArrayList<LogEntry> getForOrigin(String origin){
        try {
            HttpResponse<JsonNode> response = Unirest.get(Cfg.getCfg().getServerURL()+"/log/getByOrigin?origin="+origin)
                    .header("accept", "application/json")
                    .asJson();

            ArrayList<LogEntry> logEntries = new ArrayList<>();
            JsonNode body = response.getBody();
            JSONArray logEntriesJSON = body.getObject().getJSONArray("logEntries");
            for(int i = 0; i < logEntriesJSON.length(); i++){
                ObjectMapper mapper = new ObjectMapper();
                try{
                    LogEntry le = mapper.readValue(logEntriesJSON.get(i).toString(), LogEntry.class);
                    logEntries.add(le);
                }catch (IOException ioe){
                    System.err.println(ioe);
                }
            }
            return logEntries;
        }catch (UnirestException e){
            System.err.println(e);
        }
        return new ArrayList<>();
    }


    public static Set<String> getDistinctServices(ArrayList<LogEntry> logEntries){
        return logEntries.stream()
                .map(le -> le.getService()+"."+le.getService_protocol())
                .collect(Collectors.toSet());
    }

    public static Set<String> getDistinctServers(ArrayList<LogEntry> logEntries){
        return logEntries.stream()
                .map(le -> le.getServer())
                .collect(Collectors.toSet());
    }

    public static List<LogEntry> filterLogsByServer(String selectedServer, ArrayList<LogEntry> logEntries) {
        return logEntries.stream()
                .filter(logEntry -> (logEntry.getServer()).equals(selectedServer))
                .collect(Collectors.toList());
    }

    public static List<LogEntry> filterLogsByService(String selectedServiceType, ArrayList<LogEntry> logEntries) {
        return logEntries.stream()
                .filter(logEntry -> (logEntry.getService()+"."+logEntry.getService_protocol()).equals(selectedServiceType))
                .collect(Collectors.toList());
    }
}

package analyzer.rest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codewing on 25.01.2017.
 */
public class LogEntry {
    private int id;
    private int port;
    private String server;
    private String origin;
    private String domain;
    private String service;
    private String service_protocol;
    private String instance;
    private String time;
    private List<TxtData> txtDataList;

    public LogEntry(){}

    public String getTxtDataListAsKVString(){
        if(txtDataList == null || txtDataList.isEmpty()){
            return "";
        }
        return txtDataList.stream().map(td -> td.getName()+"="+td.getContent()).collect(Collectors.joining("\n"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService_protocol() {
        return service_protocol;
    }

    public void setService_protocol(String service_protocol) {
        this.service_protocol = service_protocol;
    }

    public String getFullService() {
        return service+"."+service_protocol;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<TxtData> getTxtDataList() {
        return txtDataList;
    }

    public void setTxtDataList(List<TxtData> txtDataList) {
        this.txtDataList = txtDataList;
    }
}

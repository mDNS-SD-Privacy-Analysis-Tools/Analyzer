package analyzer.controller;


import analyzer.rest.Database;
import analyzer.rest.LogEntry;
import analyzer.utils.StrLngTpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StatsController {

    @FXML
    ChoiceBox<String> cbLocation;

    @FXML
    ComboBox<String> cbService;

    @FXML
    ComboBox<String> cbServer;

    @FXML
    Label lblAmountServices;
    @FXML
    Label lblAmountDevices;
    @FXML
    Label lblAmountApple;
    @FXML
    Label lblServicesPerDevice;
    @FXML
    Label lblAmountInstances;

    @FXML
    TableView<StrLngTpl> tblServiceInstanceCount;
    @FXML
    TableColumn<StrLngTpl, Long> instanceCountColumn;
    @FXML
    TableColumn<StrLngTpl, String> serviceColumn;

    ObservableList<StrLngTpl> serviceInstanceOccur = FXCollections.observableArrayList(new ArrayList<>());

    private ObservableList<LogEntry> logEntriesObs =  FXCollections.observableArrayList(new ArrayList<LogEntry>());
    private static Pattern APPLE_PATTERN = Pattern.compile(".*(iPhone|iPad|MacBook).*", Pattern.CASE_INSENSITIVE);

    public void initialize(){
        updateLocations();
        initTableRenderer();
    }

    @FXML
    protected void onLocationChanged(){
        cbService.getItems().clear();
        cbServer.getItems().clear();
        cbServer.getSelectionModel().clearSelection();
        cbService.getSelectionModel().clearSelection();

        cbService.setItems(FXCollections.observableArrayList(Database.get().getServices(cbLocation.getValue())));
        cbServer.setItems(FXCollections.observableArrayList(Database.get().getServers(cbLocation.getValue())));

        logEntriesObs.clear();
        logEntriesObs.addAll(Database.get().getLogs(cbLocation.getValue()));

        updateStats();
    }

    @FXML
    protected void onServiceChanged(){
        if(!cbService.getSelectionModel().isEmpty()){
            cbServer.getSelectionModel().clearSelection();
            logEntriesObs.clear();
            logEntriesObs.addAll(Database.get().getLogsForService(cbLocation.getValue(), cbService.getValue()));

            updateStats();
        }
    }

    @FXML
    protected void onServerChanged(){
        if(!cbServer.getSelectionModel().isEmpty()){
            cbService.getSelectionModel().clearSelection();
            logEntriesObs.clear();
            logEntriesObs.addAll(Database.get().getLogsForServer(cbLocation.getValue(), cbServer.getValue()));

            updateStats();
        }
    }

    private void updateLocations(){
        cbLocation.setItems(FXCollections.observableArrayList(Database.get().getOrigins()));
    }

    private void updateStats(){
        Platform.runLater(() -> {
            updateDeviceStats();
            updateServiceStats();
        });
    }

    private void updateDeviceStats(){
        Set<String> servers = logEntriesObs.parallelStream().map(LogEntry::getServer).distinct().collect(Collectors.toSet());
        lblAmountDevices.setText(""+servers.size());

        Set<String> appleDevs = servers.parallelStream().filter(srv -> APPLE_PATTERN.matcher(srv).matches()).collect(Collectors.toSet());
        lblAmountApple.setText(""+appleDevs.size());

        long instanceCount = 0l;
        for (String server : servers){
            instanceCount += logEntriesObs.parallelStream().filter(le -> le.getServer().equals(server)).map(le -> le.getService()).distinct().count();
        }
        lblServicesPerDevice.setText("" + (instanceCount / (float) servers.size()));
    }

    private void updateServiceStats(){
        Set<String> services = logEntriesObs.parallelStream().map(LogEntry::getService).distinct().collect(Collectors.toSet());
        int numberOfServices = services.size();

        serviceInstanceOccur.clear();
        long sumInstances = 0;
        for (String service : services){
            long count = logEntriesObs.parallelStream().filter(le -> le.getService().equals(service)).map(le -> le.getInstance()).distinct().count();
            sumInstances += count;
            serviceInstanceOccur.add(new StrLngTpl(service, count));
        }
        lblAmountInstances.setText(""+sumInstances);
        lblAmountServices.setText(""+numberOfServices);
    }

    private void initTableRenderer(){
        instanceCountColumn.setCellValueFactory(new PropertyValueFactory<>("lng"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("str"));
        tblServiceInstanceCount.setItems(serviceInstanceOccur);
    }

}

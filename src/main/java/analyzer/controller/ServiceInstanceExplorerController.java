package analyzer.controller;


import analyzer.rest.Database;
import analyzer.rest.LogEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ServiceInstanceExplorerController {

    @FXML
    ChoiceBox<String> cbLocation;

    @FXML
    ComboBox<String> cbService;

    @FXML
    ComboBox<String> cbServer;

    @FXML
    TableView<LogEntry> tblLogEntries;
    @FXML
    TableColumn<LogEntry, String> nameColumn;
    @FXML
    TableColumn<LogEntry, String> serviceColumn;
    @FXML
    TableColumn<LogEntry, LocalDateTime> timeColumn;
    @FXML
    TableColumn<LogEntry, String> serverColumn;
    @FXML
    TableColumn<LogEntry, Integer> portColumn;
    @FXML
    TableColumn<LogEntry, String> txtColumn;


    private ObservableList<LogEntry> logEntriesObs =  FXCollections.observableArrayList(new ArrayList<LogEntry>());

    public void initialize(){
        updateLocations();
        initTableRenderer();
    }

    @FXML
    protected void onLocationChanged(){
        cbService.getItems().clear();
        cbServer.getItems().clear();

        cbService.setItems(FXCollections.observableArrayList(Database.get().getServices(cbLocation.getValue())));
        cbServer.setItems(FXCollections.observableArrayList(Database.get().getServers(cbLocation.getValue())));
        logEntriesObs.clear();
        logEntriesObs.addAll(Database.get().getLogs(cbLocation.getValue()));
    }

    @FXML
    protected void onServiceChanged(){
        if(!cbService.getSelectionModel().isEmpty()){
            cbServer.getSelectionModel().clearSelection();
            logEntriesObs.clear();
            logEntriesObs.addAll(Database.get().getLogsForService(cbLocation.getValue(), cbService.getValue()));
        }
    }

    @FXML
    protected void onServerChanged(){
        if(!cbServer.getSelectionModel().isEmpty()){
            cbService.getSelectionModel().clearSelection();
            logEntriesObs.clear();
            logEntriesObs.addAll(Database.get().getLogsForServer(cbLocation.getValue(), cbServer.getValue()));
        }
    }


    private void updateLocations(){
        cbLocation.setItems(FXCollections.observableArrayList(Database.get().getOrigins()));
    }

    private void initTableRenderer(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("instance"));
        serviceColumn.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getFullService()));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        serverColumn.setCellValueFactory(new PropertyValueFactory<>("server"));
        portColumn.setCellValueFactory(new PropertyValueFactory<>("port"));
        txtColumn.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getTxtDataListAsKVString()));
        tblLogEntries.setItems(logEntriesObs);
    }

}

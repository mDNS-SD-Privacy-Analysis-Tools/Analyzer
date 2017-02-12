package analyzer;

import analyzer.utils.Cfg;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main_window.fxml"));
        primaryStage.setTitle("mDNS-SD Analyzer");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Cfg.getCfg();
    }

    @Override
    public void stop() throws Exception {
        System.exit(1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

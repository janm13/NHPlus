package controller;

import datastorage.ConnectionBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Login;
import org.hsqldb.persist.Log;

import java.io.IOException;
import java.util.Timer;

public class Main extends Application {


    private Stage primaryStage;
    private Login user;
    @Override
    public void start(Stage primaryStage) {
        loginWindow();
        this.primaryStage = primaryStage;
        if (this.user != null) {
            mainWindow();
        }
    }

    public void loginWindow() {
        try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/LoginWindowView.fxml"));
                AnchorPane pane = loader.load();
                Scene scene = new Scene(pane);
                //da die primaryStage noch im Hintergrund bleiben soll
                Stage stage = new Stage();

                LoginWindowController controller = loader.getController();
                controller.initialize(this, stage);

                stage.setScene(scene);
                stage.setResizable(false);
                stage.showAndWait();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    public void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/MainWindowView.fxml"));
            BorderPane pane = loader.load();
            Scene scene = new Scene(pane);

            MainWindowController controller = loader.getController();
            controller.initialize(this.user);

            this.primaryStage.setTitle("NHPlus");
            this.primaryStage.setScene(scene);
            this.primaryStage.setResizable(false);
            this.primaryStage.show();

            this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    ConnectionBuilder.closeConnection();
                    Platform.exit();
                    System.exit(0);
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setUser(Login user) {
        this.user = user;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
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

import java.io.IOException;

/**
 * The main class that serves as the entry point for the application.
 */
public class Main extends Application {

    private Stage primaryStage;
    private Login user;

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        loginWindow();
        this.primaryStage = primaryStage;
        if (this.user != null) {
            mainWindow();
        }
    }

    /**
     * Displays the login window.
     */
    public void loginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/LoginWindowView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            // Since the primaryStage should remain in the background
            Stage stage = new Stage();

            LoginWindowController controller = loader.getController();
            controller.initialize(this, stage);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the main application window.
     */
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
            e.printStackTrace();
        }
    }

    /**
     * Sets the user for the application.
     *
     * @param user The user to set.
     */
    public void setUser(Login user) {
        this.user = user;
    }
}
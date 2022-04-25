package com.tictaccode.messageapptest.client;

import com.tictaccode.messageapptest.ComponentMessages;
import com.tictaccode.messageapptest.Connection;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static com.tictaccode.messageapptest.ComponentMessages.CREATE_LOBBY;

public class ClientController {
    private Connection connection;
    private Stage stage;
    private Scene scene;
    
    @FXML
    private TextArea display;
    
    @FXML
    private TextField input;
    
    @FXML
    private Button send;
    
    public void initialize() {
        display.setLayoutX(0);
        display.setLayoutY(0);
        
        input.setLayoutX(0);
    }
    
    public void updateWidth() {
        double width = scene.getWidth();
        
        display.setPrefWidth(width);
        input.setPrefWidth(width / 4 * 3);
        send.setPrefWidth(width / 4);
        send.setLayoutX(width / 4 * 3);
    }
    
    public void updateHeight() {
        double height = scene.getHeight();
        
        display.setPrefHeight(height / 4 * 3);
        input.setPrefHeight(height / 4);
        input.setLayoutY(height / 4 * 3);
        send.setPrefHeight(height / 4);
        send.setLayoutY(height / 4 * 3);
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public void setScene(Scene scene) {
        this.scene = scene;
    
        updateWidth();
        updateHeight();
    
        scene.widthProperty().addListener((ChangeListener) (observableValue, o, t1) -> updateWidth());
        scene.heightProperty().addListener((ChangeListener) (observableValue, o, t1) -> updateHeight());
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    
        stage.setOnCloseRequest(e -> {
            if (connection != null)
                connection.closeSocket();
        });
    }
    
    public void displayMessage(String message) {
        display.appendText(message + '\n');
    }
    
    public void sendMessage(ActionEvent actionEvent) {
        //String message = input.getText().trim();
        ComponentMessages message = CREATE_LOBBY;
        
        if (message.equals(CREATE_LOBBY)) {
            try {
                connection.sendMessage(message);
            }
            catch (IOException e) {
                closeConnection();
            }
            displayMessage(String.valueOf(message));
        }
        
        input.setText("");
    }
    
    public void closeConnection() {
        input.setDisable(true);
        send.setDisable(true);
        displayMessage("Disconnected from the server.\n");
    }
}

package com.tictaccode.messageapptest.client;

import com.tictaccode.messageapptest.Connection;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ClientController {
    private Connection connection;
    private Stage stage;
    
    public void initialize() {
        // TODO initialize UI elements here (delete method if not needed)
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void displayMessage(String message) {
        // TODO display message to the screen here
    }
    
    // TODO create function that sends a message when client pushes send button
    // connection.sendMessage(message)
    // displayMessage(message)
    
    // TODO close the socket in the Connection object if the user exits the UI (you will need the stage object, look at
    //  Server class for reference)
    // connection.closeSocket()
}

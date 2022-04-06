package com.tictaccode.messageapptest.client;

import com.tictaccode.messageapptest.Connection;
import com.tictaccode.messageapptest.SocketManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class ClientApplication extends Application implements SocketManager {
    public static final String HOST = "localhost";
    public static final int PORT = 8000;
    
    private Connection connection;
    
    private ClientController clientController;
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("client-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Messenger");
        stage.setScene(scene);
        stage.show();
        
        try {
            Socket socket = new Socket(HOST, PORT);
            connection = new Connection(socket, this);
        }
        catch (ConnectException e) {
            handleConnectionClosed(null, null);
        }
        
        clientController = fxmlLoader.getController();
        clientController.setConnection(connection);
        clientController.setStage(stage);
        clientController.setScene(scene);
    }
    
    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void handleReceivedMessage(Socket socket, String message) {
        // TODO handle when a message is received
    }
    
    @Override
    public void handleConnectionClosed(Socket socket, Connection connection) {
        // TODO handle when the connection to the server is closed
        // maybe create and call a method in the controller class that displays to the user that the connection has been
        // closed and disable input
    }
}

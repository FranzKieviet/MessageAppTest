package com.tictaccode.messageapptest.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.tictaccode.messageapptest.Connection;
import com.tictaccode.messageapptest.SocketManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Server extends Application implements SocketManager {
    
    public static final int PORT = 8000;
    
    private TextArea ta;
    
    private List<Connection> connections;
    
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        ta = new TextArea();
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.setPrefWidth(450);
        ta.setPrefHeight(200);
        
        // Create a scene and place it in the stage
        Scene scene = new Scene(new Pane(ta), 450, 200);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
        
        primaryStage.widthProperty().addListener(
                (ChangeListener) (observableValue, o, t1) -> ta.setPrefWidth(scene.getWidth()));
    
        primaryStage.heightProperty().addListener(
                (ChangeListener) (observableValue, o, t1) -> ta.setPrefHeight(scene.getHeight()));
        
        connections = Collections.synchronizedList(new ArrayList<>());
        
        new Thread(() -> {
            // Create a server socket
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(PORT);
            }
            catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            
            Platform.runLater(() ->
                    ta.appendText("Server started at " + new Date() + '\n'));
    
            ServerSocket finalServerSocket = serverSocket;
            primaryStage.setOnCloseRequest(e -> {
                for (Connection connection : connections)
                    connection.closeSocket();
                
                try {
                    finalServerSocket.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
    
            while (!finalServerSocket.isClosed()) {
                try {
                    // Listen for a connection request
                    Socket socket = finalServerSocket.accept();
                    Connection connection = new Connection(socket, this);
                    connections.add(connection);
                }
                catch (IOException e) {}
            }
        }).start();
    }
    
    @Override
    public synchronized void handleReceivedMessage(Socket socket, String message) {
        Platform.runLater(() -> ta.appendText(socket.getInetAddress().getHostAddress() + ": " + message));
        
        for (Connection connection : connections) {
            if (!connection.hasSocket(socket)) {
                try {
                    connection.sendMessage(message);
                }
                catch (IOException e) {}
            }
        }
    }
    
    @Override
    public synchronized void handleConnectionClosed(Socket socket, Connection connection) {
        Platform.runLater(() -> ta.appendText(socket.getInetAddress().getHostAddress() + " has disconnected."));
        connections.remove(connection);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

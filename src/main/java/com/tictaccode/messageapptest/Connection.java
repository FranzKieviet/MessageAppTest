package com.tictaccode.messageapptest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    
    private Socket socket;
    
    private SocketManager socketManager;
    
    private Thread listener;
    
    public Connection(Socket socket, SocketManager socketManager) {
        this.socket = socket;
        this.socketManager = socketManager;
    
        listener = new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    String message = getMessage();
                    socketManager.handleReceivedMessage(socket, message);
                }
                catch (IOException e) {
                    closeSocket();
                }
            }
        });
    
        listener.start();
    }
    
    public boolean hasSocket(Socket otherSocket) {
        return socket.equals(otherSocket);
    }
    
    private String getMessage() throws IOException {
        return new DataInputStream(socket.getInputStream()).readUTF();
    }
    
    public synchronized void sendMessage(String message) throws IOException {
        new DataOutputStream(socket.getOutputStream()).writeUTF(socket.getInetAddress().getHostAddress() + ": "
                + message);
    }
    
    public synchronized void closeSocket() {
        listener.interrupt();
        
        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            socketManager.handleConnectionClosed(socket, this);
        }
    }
}

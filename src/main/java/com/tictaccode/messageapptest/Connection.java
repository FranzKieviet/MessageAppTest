package com.tictaccode.messageapptest;

import java.io.*;
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
                    ComponentMessages message = getMessage();
                    socketManager.handleReceivedMessage(socket, message);
                }
                catch (IOException e) {
                    closeSocket();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    
        listener.start();
    }
    
    public boolean hasSocket(Socket otherSocket) {
        return socket.equals(otherSocket);
    }
    
    private ComponentMessages getMessage() throws IOException, ClassNotFoundException {
        return (ComponentMessages) new ObjectInputStream(socket.getInputStream()).readObject();
       // return new DataInputStream(socket.getInputStream()).readUTF();
    }



    public synchronized void sendMessage(ComponentMessages message) throws IOException {
        new ObjectOutputStream(socket.getOutputStream()).writeObject(message);
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

package com.tictaccode.messageapptest;

import java.io.IOException;
import java.net.Socket;

public interface SocketManager {
    void handleReceivedMessage(Socket socket, String message);
    
    void handleConnectionClosed(Socket socket, Connection connection);
}

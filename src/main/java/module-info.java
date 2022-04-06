module com.tictaccode.messageapptest {
    requires javafx.controls;
    requires javafx.fxml;
    
    
    opens com.tictaccode.messageapptest.server to javafx.fxml;
    exports com.tictaccode.messageapptest.server;
}
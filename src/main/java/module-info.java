module alvin.roe.scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens scheduler to javafx.fxml;
    exports scheduler;
    opens scheduler.controller to javafx.fxml;
    exports scheduler.controller;
    opens scheduler.model to javafx.fxml;
    exports scheduler.model;
    opens scheduler.helper to javafx.fxml;
    exports scheduler.helper;
}
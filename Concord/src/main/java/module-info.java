module concord.concord {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens concord.concord to javafx.fxml;
    opens concord.concord.controllers to javafx.fxml;
    opens concord.concord.models to javafx.base;
    exports concord.concord;
}
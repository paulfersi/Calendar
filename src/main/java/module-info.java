module com.example.calendar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens com.example.calendar to javafx.fxml;
    exports com.example.calendar;
}
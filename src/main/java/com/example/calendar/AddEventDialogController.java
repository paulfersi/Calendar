package com.example.calendar;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalTime;

public class AddEventDialogController {

    @FXML
    private Label dateLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField hourEndField;
    @FXML
    private TextField hourStartField;
    @FXML
    private TextField minuteEndField;
    @FXML
    private TextField minuteStartField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField titleField;

    Event event;

    void setIntestation(Matrix cell) {
        dayLabel.setText(cell.getDate().getDayOfWeek().toString());
        dateLabel.setText(cell.getDate().toString());
        startDatePicker.setValue(cell.getDate());
        endDatePicker.setValue(cell.getDate());
    }


    void update() {
        event.setTitle(titleField.getText());
        event.setStartDate(startDatePicker.getValue());
        event.setEndDate(endDatePicker.getValue());
        int sHours = -1;
        int sMinutes = -1;
        int eHours = -1;
        int eMinutes = -1;
        LocalTime startTime = LocalTime.MAX;
        LocalTime endTime = LocalTime.MAX;
        /*start time*/
        try {
            sHours = Integer.parseInt(hourStartField.getText());
            sMinutes = Integer.parseInt(minuteStartField.getText());
            startTime = LocalTime.of(sHours, sMinutes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.setStartTime(startTime);

        /*end time*/
        try {
            eHours = Integer.parseInt(hourEndField.getText());
            eMinutes = Integer.parseInt(minuteEndField.getText());
            endTime = LocalTime.of(eHours, eMinutes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.setEndTime(endTime);

        event.setDescription(descriptionField.getText());

        try {
            Event.checkEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Event getEvent() {
        return event;
    }
}

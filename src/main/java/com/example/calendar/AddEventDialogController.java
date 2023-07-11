package com.example.calendar;

import com.example.calendar.util.DataBaseUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
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
    @FXML
    private Button applyButton;
    @FXML
    private Button cancelButton;

    Connection connection = DataBaseUtil.getConnection();

    void setIntestation(Matrix cell) {
        dayLabel.setText(cell.getDate().getDayOfWeek().toString());
        dateLabel.setText(cell.getDate().toString());
        startDatePicker.setValue(cell.getDate());
        endDatePicker.setValue(cell.getDate());
    }

    private void insertData(String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String description) {
        String query = "INSERT INTO event (Title, StartDate, StartTime, EndDate, EndTime, Description) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setDate(2, Date.valueOf(startDate));
            preparedStatement.setTime(3, Time.valueOf(startTime));
            preparedStatement.setDate(4, Date.valueOf(endDate));
            preparedStatement.setTime(5, Time.valueOf(endTime));
            preparedStatement.setString(6, description);
            preparedStatement.executeUpdate(); // Execute the update query
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleApplyButton() {
        String title = titleField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        boolean correct = true;
        Event event = null;
        //per l'orario inserisco i due field "hours" e "minutes" in un LocalTime
        int sHours = -1;
        int sMinutes = -1;
        int eHours = -1;
        int eMinutes = -1;
        LocalTime startTime = null;
        LocalTime endTime = null;

        try {
            sHours = Integer.parseInt(hourStartField.getText());
        } catch (Exception e) {
            correct = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Incorrect Time Format");
            alert.setContentText("Start Hour is not valid!");
            alert.showAndWait();
        }
        if (correct) {
            try {
                sMinutes = Integer.parseInt(minuteStartField.getText());
            } catch (Exception e) {
                correct = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Incorrect Time Format");
                alert.setContentText("Start Minute is not valid!");
                alert.showAndWait();
            }
        }
        if (correct) {
            try {
                startTime = LocalTime.of(sHours, sMinutes);
            } catch (Exception e) {
                correct = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Incorrect Time Format");
                alert.setContentText("Start Time is out of range");
                alert.showAndWait();
            }
        }
        if (correct) {
            try {
                eHours = Integer.parseInt(hourEndField.getText());
            } catch (Exception e) {
                correct = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Incorrect Time Format");
                alert.setContentText("End Hour is not valid!");
                alert.showAndWait();
            }
        }

        if (correct) {
            try {
                eMinutes = Integer.parseInt(minuteEndField.getText());
            } catch (Exception e) {
                correct = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Incorrect Time Format");
                alert.setContentText("End Minute is not valid!");
                alert.showAndWait();
            }
        }

        if (correct) {
            try {
                endTime = LocalTime.of(eHours, eMinutes);
            } catch (Exception e) {
                correct = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Incorrect Time Format");
                alert.setContentText("End Time is out of range");
                alert.showAndWait();
            }
        }

        String description = descriptionField.getText();

        if (correct) {
            event = new Event(title, startDate, startTime, endDate, endTime, description);

            try {
                if (event.startDate.isAfter(event.endDate)) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                correct = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Incorrect Date/Time combination");
                alert.setContentText("Start Date is after End Date");
                alert.showAndWait();
            }
        }

        if(correct){

            try {
                if (event.startTime.isAfter(event.endTime) && event.getStartDate().isEqual(event.getEndDate())) {
                    /**verifico che non sia il caso in cui endDate sia un giorno diverso da startDate, solo in tal caso sarebbe accettabile che startTime sia dopo endTime **/
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                correct = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Incorrect Date/Time combination");
                alert.setContentText("Start Time is after End Time");
                alert.showAndWait();
            }
        }

        if (correct) {
            insertData(title, startDate, startTime, endDate, endTime, description);
        }
        clearFields();
        CalendarController.getDialogStage().close();
    }

    private void clearFields() {
        titleField.clear();
        startDatePicker.setValue(null);
        hourStartField.clear();
        minuteStartField.clear();
        endDatePicker.setValue(null);
        hourEndField.clear();
        minuteEndField.clear();
        descriptionField.clear();
    }

    @FXML
    void handleCancelButton(ActionEvent event) {
        CalendarController.getDialogStage().close();
    }


}

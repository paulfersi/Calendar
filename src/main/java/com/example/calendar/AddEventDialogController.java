package com.example.calendar;

import com.example.calendar.util.DataBaseUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.calendar.DailyViewController.refreshTable;
import static com.example.calendar.DailyViewController.tableView;

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
    private void handleApplyButton(){
        String title = titleField.getText();
        LocalDate startDate = startDatePicker.getValue();
        //per l'orario inserisco i due field "hours" e "minutes" in un LocalTime
        int sHours;
        int sMinutes;
        int eHours;
        int eMinutes;
        LocalTime startTime = null;
        LocalTime endTime = null;
        try {
            sHours = Integer.parseInt(hourStartField.getText());
            sMinutes = Integer.parseInt(minuteStartField.getText());
            startTime = LocalTime.of(sHours, sMinutes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalDate endDate = endDatePicker.getValue();

        try {
            eHours = Integer.parseInt(hourEndField.getText());
            eMinutes = Integer.parseInt(minuteEndField.getText());
            endTime = LocalTime.of(eHours, eMinutes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String description = descriptionField.getText();
        Event event = new Event(title,startDate,startTime,endDate,endTime,description);

        try {
            Event.checkEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        insertData(title, startDate, startTime, endDate, endTime, description);
        /*DEBUG*/
        System.out.println(title + " , " + startDate + " , " + startTime + " , " + endDate + " , " + endTime + " , " + description);


        titleField.clear();
        startDatePicker.setValue(null);
        hourStartField.clear();
        minuteStartField.clear();
        endDatePicker.setValue(null);
        hourEndField.clear();
        minuteEndField.clear();
        descriptionField.clear();

    }

}

package com.example.calendar;

import com.example.calendar.util.DataBaseUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

import java.time.LocalTime;
import java.util.Optional;

public class DailyViewController {


    private LocalDate selectedDate;
    static Connection connection = DataBaseUtil.getConnection();

    @FXML
    private TableView<Event> tableView;

    @FXML
    private TableColumn<Event,String> timeColumn;

    @FXML
    private TableColumn<Event,String> titleColumn;
    @FXML
    private Label headerLabel;
    void setIntestation(Matrix cell) {
        selectedDate = cell.getDate();
        headerLabel.setText(cell.getDate().getDayOfWeek().toString() + " , " + cell.getDate().toString());
    }

    @FXML
    void handleAddButton(){
        openDialogPane(new Matrix(selectedDate));
    }


    public static void openDialogPane(Matrix cell){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DailyViewController.class.getResource("add-event-view.fxml"));
            DialogPane view = loader.load();
            AddEventDialogController controller = loader.getController();

            controller.setIntestation(cell);

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("New Event");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            // Show the dialog and wait until the user closes it
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.APPLY) {
                controller.update();
                Event event = controller.getEvent();
                insertData(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void insertData(Event event) {
        String query = "INSERT INTO event (Title, StartDate, StartTime, EndDate, EndTime, Description) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, event.getTitle());
            preparedStatement.setDate(2, java.sql.Date.valueOf(event.getStartDate()));
            preparedStatement.setTime(3, Time.valueOf(event.getStartTime()));
            preparedStatement.setDate(4, Date.valueOf(event.getEndDate()));
            preparedStatement.setTime(5, Time.valueOf(event.getEndTime()));
            preparedStatement.setString(6, event.getDescription());
            preparedStatement.executeUpdate(); // Execute the update query
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}



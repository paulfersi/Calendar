package com.example.calendar;

import com.example.calendar.util.DataBaseUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

import java.time.LocalTime;

public class DailyViewController {


    private static LocalDate selectedDate;
    static Connection connection = DataBaseUtil.getConnection();

    @FXML
    public static TableView<Event> tableView;

    @FXML
    private TableColumn<Event,String> timeColumn;

    @FXML
    private static TableColumn<Event,String> titleColumn;
    @FXML
    private Label headerLabel;
    @FXML
    private Button refreshButton;



    void setIntestation(Matrix cell) {
        selectedDate = cell.getDate();
        headerLabel.setText(cell.getDate().getDayOfWeek().toString() + " , " + cell.getDate().toString());
    }

    @FXML
    void handleAddButton(){
        openDialogPane(new Matrix(selectedDate));
    }

    public static void update(){
        showTable(tableView);
    }

    @FXML
    void handleRefreshButton(){
        showTable(tableView);
    }


    public static void openDialogPane(Matrix cell) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DailyViewController.class.getResource("add-event-view.fxml"));
            AnchorPane view = loader.load();
            AddEventDialogController controller = loader.getController();

            controller.setIntestation(cell);

            // Create the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Event");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            // Set the content of the stage to the loaded AnchorPane
            Scene scene = new Scene(view);
            dialogStage.setScene(scene);

            // Show the stage and wait until the user closes it
            dialogStage.showAndWait();


            showTable(tableView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void showTable(TableView<Event> tableView){
        String query = "SELECT id, Title, StartDate, StartTime, EndDate, EndTime, Description FROM event " +
                "WHERE (StartDate <= ? AND EndDate >= ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(selectedDate));
            statement.setDate(2,Date.valueOf(selectedDate));

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                String title = resultSet.getString("Title");
                Event event = new Event(title);

                titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
                tableView.getItems().add(event);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void refreshTable(TableView<Event> tableView, LocalDate selectedDate){
        String query = "SELECT id, Title, StartDate, StartTime, EndDate, EndTime, Description FROM event " +
                "WHERE (StartDate <= ? AND EndDate >= ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Imposta i parametri per la query
            statement.setDate(1, Date.valueOf(selectedDate));
            statement.setDate(2, Date.valueOf(selectedDate));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Recupera i dati dal resultSet della query -> li recupero tutti perché ci servono per l'alert dopo
                int id = resultSet.getInt("id");
                String title = resultSet.getString("Title");
                LocalDate startDate = resultSet.getDate("StartDate").toLocalDate();
                LocalTime startTime = resultSet.getTime("StartTime").toLocalTime();
                LocalDate endDate = resultSet.getDate("EndDate").toLocalDate();
                LocalTime endTime = resultSet.getTime("EndTime").toLocalTime();
                String description = resultSet.getString("Description");

                Event event = new Event(id, title, startDate, startTime, endDate, endTime, description);


                titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));

                tableView.getItems().add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }





}



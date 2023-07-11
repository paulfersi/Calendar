package com.example.calendar;

import com.example.calendar.util.DataBaseUtil;
import com.example.calendar.util.LocalDateUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class CalendarController {

    Connection connection;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private Text yearField;
    @FXML
    private Text monthField;

    private LocalDate currentDate;

    final int NUM_CELLS_TOT = 42;

    public static Stage dialogStage;

    public void initialize() throws SQLException {
        connection = DataBaseUtil.getConnection();
        currentDate = LocalDate.now();
        updateCalendar();
        createTable();
    }

    @FXML
    private void handlePreviousMonth() {
        currentDate = currentDate.minusMonths(1);
        updateCalendar();
    }

    @FXML
    private void handleNextMonth() {
        currentDate = currentDate.plusMonths(1);
        updateCalendar();
    }

    @FXML
    private void handleAddButton() {
        openDialogPane(new Matrix(LocalDate.now()));
    }

    private void setSingleBox(Matrix cell, Text text, Color color) {
        Font font = Font.font("sfProFont", FontWeight.BOLD, 18);

        cell.prepareTableView();        //NUOVO

        text.setWrappingWidth(75);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setOnMouseClicked(event -> handleDayClick(cell));
        text.setFill(Color.WHITE);
        text.setFont(font);

        //creo un rettangolo per poter modificare il bordo della zona di testo
        Rectangle border = new Rectangle(75, 75);
        border.setArcWidth(10);
        border.setArcHeight(10);
        border.setStroke(color);
        border.setStrokeWidth(2);
        border.setFill(color);

        //aggiungo il rettangolo che fa da bordo e l'elemento di testo che corrisponde al giorno
        calendarGrid.add(border, cell.getCol(), cell.getRow());
        calendarGrid.add(text, cell.getCol(), cell.getRow());
    }

    private void updateCalendar() {
        yearField.setText(String.valueOf(currentDate.getYear()));
        monthField.setText(currentDate.getMonth().toString());
        calendarGrid.getChildren().clear();

        int day = 1;
        int day_FollowingMonth = 1;
        int day_previousMonth = LocalDateUtil.getDayPreviousMonth(currentDate);

        for (int row = 0; row < Matrix.NUM_ROWS; row++) {               //matrice
            for (int col = 0; col < Matrix.NUM_COLS; col++) {

                Matrix cell = new Matrix(row, col);

                if (cell.getIndex() < LocalDateUtil.getStartOffset(currentDate)) {
                    /*previous month*/
                    cell.setDate(currentDate.minusMonths(1).withDayOfMonth(day_previousMonth++));

                    Text text = new Text(String.valueOf(cell.getDate().getDayOfMonth()));

                    setSingleBox(cell, text, Color.web("#95A5A6"));
                } else if (day <= currentDate.lengthOfMonth()) {
                    /* current month*/
                    cell.setDate(currentDate.withDayOfMonth(day++));

                    Text text = new Text(String.valueOf(cell.getDate().getDayOfMonth()));
                    setSingleBox(cell, text, Color.web("#34495E"));
                } else if (day++ <= NUM_CELLS_TOT) {
                    /*next month*/
                    cell.setDate(currentDate.plusMonths(1).withDayOfMonth(day_FollowingMonth++));

                    Text text = new Text(String.valueOf(cell.getDate().getDayOfMonth()));

                    setSingleBox(cell, text, Color.web("#95A5A6"));
                }
            }
        }
    }

    private void handleDayClick(Matrix cell) {
        LocalDate selectedDate = cell.getDate();
        if (selectedDate != null) {

            refreshTable(cell);

            TableView<Event> eventTableView = cell.getEventTableView();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Events for " + selectedDate);
            dialog.getDialogPane().setContent(eventTableView);

            dialog.getDialogPane().setStyle("-fx-background-color: #24293E; -fx-border-color: #8EBBFF; -fx-border-width: 2;");

            eventTableView.setStyle("-fx-background-color: #34495E; -fx-border-color: #8EBBFF;");

            eventTableView.setStyle("-fx-text-fill: white;");

            ButtonType removeButton = new ButtonType("Remove", ButtonBar.ButtonData.APPLY);
            ButtonType addEventButton = new ButtonType("Add Event", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addEventButton, removeButton);

            /*STYLE*/
            Button removeButtonNode = (Button) dialog.getDialogPane().lookupButton(removeButton);
            Button addEventButtonNode = (Button) dialog.getDialogPane().lookupButton(addEventButton);
            removeButtonNode.setStyle("-fx-text-fill: black;");
            addEventButtonNode.setStyle("-fx-text-fill: black;");

            removeButtonNode.setStyle("-fx-background-color: #8EBBFF; -fx-text-fill: black;");
            addEventButtonNode.setStyle("-fx-background-color: #8EBBFF; -fx-text-fill: black;");
            /*-----------------------------------*/

            dialog.setResultConverter(buttonType -> {
                if (buttonType == addEventButton) {
                    openDialogPane(cell);
                    refreshTable(cell);
                } else if (buttonType == removeButton) {
                    removeEventDialog(cell);
                }
                return null;
            });

            // Gestione dell'evento di chiusura della finestra
            dialog.setOnCloseRequest(event -> dialog.close());

            // Aggiungi un listener per gestire l'evento di chiusura della finestra principale
            Stage primaryStage = (Stage) eventTableView.getScene().getWindow();
            primaryStage.setOnCloseRequest(event -> dialog.close());

            dialog.showAndWait();
        }
    }


    void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS event (id INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT, StartDate DATE, StartTime TIME, EndDate DATE, EndTime TIME, Description TEXT)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    private void openDialogPane(Matrix cell) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CalendarController.class.getResource("add-event-view.fxml"));
            AnchorPane view = loader.load();
            AddEventDialogController controller = loader.getController();

            controller.setIntestation(cell);

            dialogStage = new Stage();
            dialogStage.setTitle("New Event");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(view);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeEventDialog(Matrix cell) {
        TableView<Event> eventTableView = cell.getEventTableView();

        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Remove Event");
            alert.setContentText("Are you sure you want to remove this event?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int eventId = selectedEvent.getId();
                deleteEvent(eventId);
            }
        }
       refreshTable(cell);
    }

    private void refreshTable(Matrix cell) {
        TableView<Event> eventTableView = cell.getEventTableView();
        TableColumn<Event, String> titleColumn = cell.getTitleColumn();
        TableColumn<Event, String> startDateColumn = cell.getStartDateColumn();
        TableColumn<Event, String> startTimeColumn = cell.getStartTimeColumn();
        TableColumn<Event, String> endDateColumn = cell.getEndDateColumn();
        TableColumn<Event, String> endTimeColumn = cell.getEndTimeColumn();
        TableColumn<Event, String> descriptionColumn = cell.getDescriptionColumn();

        eventTableView.getItems().clear();

        LocalDate selectedDate = cell.getDate();
        String query = "SELECT * FROM event WHERE StartDate = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(selectedDate));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("Title");
                LocalDate startDate = resultSet.getDate("StartDate").toLocalDate();
                LocalTime startTime = resultSet.getTime("StartTime").toLocalTime();
                LocalDate endDate = resultSet.getDate("EndDate").toLocalDate();
                LocalTime endTime = resultSet.getTime("EndTime").toLocalTime();
                String description = resultSet.getString("Description");

                //System.out.println("Title for selected date: " + title);
                Event event = new Event(id, title, startDate, startTime, endDate, endTime, description);
                titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
                startDateColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
                startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
                endDateColumn.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
                endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));

                eventTableView.getItems().add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEvent(int eventId) {
        String query = "DELETE FROM event WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, eventId);
            preparedStatement.executeUpdate(); // Execute the update query
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Stage getDialogStage() {       //per poter chiudere l'AddEventDialog
        return dialogStage;
    }
}


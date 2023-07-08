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

        for (int row = 0; row < Matrix.numRows; row++) {               //matrice
            for (int col = 0; col < Matrix.numCols; col++) {

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
            TableView<Event> eventTableView = new TableView<>();
            TableColumn<Event, String> titleColumn = new TableColumn<>("Title");
            eventTableView.getColumns().add(titleColumn);

            String query = "SELECT Title FROM event WHERE StartDate = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, Date.valueOf(selectedDate));
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String title = resultSet.getString("Title");
                    System.out.println("Title for selected date: " + title);
                    Event event = new Event(title);
                    titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
                    eventTableView.getItems().add(event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Events for " + selectedDate);
            dialog.getDialogPane().setContent(eventTableView);

            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType addEventButton = new ButtonType("Add Event", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(closeButton, addEventButton);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == addEventButton) {
                    openDialogPane(cell);
                }
                return null;
            });

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

            // Create the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Event");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            // Set the content of the stage to the loaded AnchorPane
            Scene scene = new Scene(view);
            dialogStage.setScene(scene);

            // Show the stage and wait until the user closes it
            dialogStage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
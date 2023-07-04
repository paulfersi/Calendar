package com.example.calendar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;

import java.util.Optional;

public class DatabaseController {

    private LocalDate selectedDate;

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
            loader.setLocation(DatabaseController.class.getResource("add-event-view.fxml"));
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
                /*insertData(event);*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



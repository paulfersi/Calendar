package com.example.calendar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

public class DatabaseController {

    @FXML
    private Label headerLabel;
    void setIntestation(Matrix cell) {
        headerLabel.setText(cell.getDate().getDayOfWeek().toString() + " , " + cell.getDate().toString());
    }

    @FXML
    void handleAddButton(){
    }



}

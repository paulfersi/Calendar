package com.example.calendar;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CalendarController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
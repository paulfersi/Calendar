package com.example.calendar;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class Cell {
    int row;
    int col;
    LocalDate date;

    TableView<Event> eventTableView;
    TableColumn<Event, String> titleColumn;
    TableColumn<Event, String> startDateColumn;
    TableColumn<Event, String> startTimeColumn;
    TableColumn<Event, String> endDateColumn;
    TableColumn<Event, String> endTimeColumn;
    TableColumn<Event, String> descriptionColumn;
    static final int NUM_ROWS = 6;
    static final int NUM_COLS = 7;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }


    public Cell(LocalDate date) {
        this.date = date;
    }

    public int getRow() {
        return row;
    }


    public int getCol() {
        return col;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TableView<Event> getEventTableView() {
        return eventTableView;
    }

    public TableColumn<Event, String> getTitleColumn() {
        return titleColumn;
    }

    public TableColumn<Event, String> getStartDateColumn() {
        return startDateColumn;
    }

    public TableColumn<Event, String> getStartTimeColumn() {
        return startTimeColumn;
    }

    public TableColumn<Event, String> getEndDateColumn() {
        return endDateColumn;
    }

    public TableColumn<Event, String> getEndTimeColumn() {
        return endTimeColumn;
    }

    public TableColumn<Event, String> getDescriptionColumn() {
        return descriptionColumn;
    }

    public void prepareTableView(){
        this.eventTableView = new TableView<>();
        this.titleColumn = new TableColumn<>("Title");
        this.startDateColumn = new TableColumn<>("Start Date");
        this.startTimeColumn = new TableColumn<>("Start Time");
        this.endDateColumn = new TableColumn<>("End Date");
        this.endTimeColumn = new TableColumn<>("End Time");
        this.descriptionColumn = new TableColumn<>("Description");
        eventTableView.getColumns().add(titleColumn);
        eventTableView.getColumns().add(startDateColumn);
        eventTableView.getColumns().add(startTimeColumn);
        eventTableView.getColumns().add(endDateColumn);
        eventTableView.getColumns().add(endTimeColumn);
        eventTableView.getColumns().add(descriptionColumn);
    }

    public int getIndex() {
        int ADJUSTMENT = 1;
        /**
         * costante che fa in modo di partire dalla cella d'indice 1, più utile per
         * lavorare con le date (non esistendo il giorno 0)
         **/
        return row * NUM_COLS + col + ADJUSTMENT;
    }
}

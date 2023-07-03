package com.example.calendar;

import java.time.LocalDate;

public class Matrix {
    int row;
    int col;
    LocalDate Date;
    static final int numRows = 6;
    static final int numCols = 7;

    public Matrix(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Matrix(int row, int col, LocalDate date) {
        this.row = row;
        this.col = col;
        Date = date;
    }

    public Matrix(LocalDate date) {
        Date = date;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public int getIndex() {
        int ADJUSTMENT = 1;             //costante che fa si che si parta dalla cella d'indice 1, più utile per
        //lavorare con le date (non esistendo il giorno 0)
        return row * numCols + col + ADJUSTMENT;
    }
}

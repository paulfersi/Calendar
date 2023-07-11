package com.example.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {

    int id;
    String title;
    LocalDate startDate;
    LocalTime startTime;
    LocalDate endDate;
    LocalTime endTime;
    String description;

    public Event(int id, String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String description) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.description = description;
    }

    public Event(String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String description) {
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}



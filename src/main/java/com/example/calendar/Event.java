package com.example.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Event {

    String Title;
    LocalDate startDate;
    LocalTime startTime;
    LocalDate endDate;
    LocalTime endTime;
    String Description;

    public Event(String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String description) {
        Title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Event event = (Event) o;
        return Objects.equals(Title, event.Title) && Objects.equals(startDate, event.startDate) && Objects.equals(startTime, event.startTime) && Objects.equals(endDate, event.endDate) && Objects.equals(endTime, event.endTime) && Objects.equals(Description, event.Description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Title, startDate, startTime, endDate, endTime, Description);
    }

    public static void checkEvent(Event e) {
        if (e.startDate.isAfter(e.endDate)) {
            throw new IllegalArgumentException();
        }
        if (e.startTime.isAfter(e.endTime) && e.getStartDate().isEqual(e.getEndDate())) {   //verifico che non sia il
            // caso in cui enddate sia un giorno diverso da startdate, solo in tal caso sarebbe
            // accettabile
            // che starttime sia dopo endtime
            throw new IllegalArgumentException();
        }
    }
}


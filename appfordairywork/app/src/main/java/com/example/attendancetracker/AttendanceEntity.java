package com.example.attendancetracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendance")
public class AttendanceEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long dateMillis; // milliseconds since epoch
    private String status; // "Full", "Half", "Absent"
    private int inHour, inMinute;
    private int outHour, outMinute;
    private double advanceTaken;
    private String notes;

    // Per-day earnings computed later; stored for quick display
    private double dayEarnings;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public long getDateMillis() { return dateMillis; }
    public void setDateMillis(long dateMillis) { this.dateMillis = dateMillis; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getInHour() { return inHour; }
    public void setInHour(int inHour) { this.inHour = inHour; }

    public int getInMinute() { return inMinute; }
    public void setInMinute(int inMinute) { this.inMinute = inMinute; }

    public int getOutHour() { return outHour; }
    public void setOutHour(int outHour) { this.outHour = outHour; }

    public int getOutMinute() { return outMinute; }
    public void setOutMinute(int outMinute) { this.outMinute = outMinute; }

    public double getAdvanceTaken() { return advanceTaken; }
    public void setAdvanceTaken(double advanceTaken) { this.advanceTaken = advanceTaken; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public double getDayEarnings() { return dayEarnings; }
    public void setDayEarnings(double dayEarnings) { this.dayEarnings = dayEarnings; }
}
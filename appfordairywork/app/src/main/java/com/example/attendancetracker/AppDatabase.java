package com.example.attendancetracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AttendanceEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AttendanceDao attendanceDao();
}
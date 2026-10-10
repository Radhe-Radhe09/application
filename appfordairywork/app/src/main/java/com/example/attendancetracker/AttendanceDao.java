package com.example.attendancetracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AttendanceEntity entity);

    @Query("SELECT * FROM attendance ORDER BY dateMillis DESC")
    List<AttendanceEntity> getAll();

    @Query("SELECT * FROM attendance WHERE dateMillis >= :start AND dateMillis <= :end ORDER BY dateMillis DESC")
    List<AttendanceEntity> getRange(long start, long end);

    @Query("DELETE FROM attendance")
    void deleteAll();
}
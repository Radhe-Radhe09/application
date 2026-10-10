package com.example.attendancetracker;

import android.content.Context;
import android.os.AsyncTask;
import androidx.room.Room;
import java.util.List;

public class DatabaseHelper {

    private AppDatabase db;
    private AttendanceDao attendanceDao;

    public DatabaseHelper(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "attendance-db")
                .allowMainThreadQueries() // for brevity; in production use async
                .build();
        attendanceDao = db.attendanceDao();
    }

    public void addAttendance(AttendanceEntity entity) {
        new InsertAsyncTask(attendanceDao).execute(entity);
    }

    public List<AttendanceEntity> getAllAttendance() {
        new GetAllAsyncTask(attendanceDao).execute();
        return null; // placeholder
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(attendanceDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<AttendanceEntity, Void, Void> {
        private AttendanceDao dao;

        InsertAsyncTask(AttendanceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(AttendanceEntity... entities) {
            dao.insert(entities[0]);
            return null;
        }
    }

    private static class GetAllAsyncTask extends AsyncTask<Void, Void, List<AttendanceEntity>> {
        private AttendanceDao dao;

        GetAllAsyncTask(AttendanceDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<AttendanceEntity> doInBackground(Void... voids) {
            return dao.getAll();
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private AttendanceDao dao;

        DeleteAllAsyncTask(AttendanceDao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
}
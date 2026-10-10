package com.example.attendancetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.Calendar;

public class DailyEntryFragment extends Fragment {

    private Button btnSaveEntry;
    private DatabaseHelper dbHelper; // we'll use DatabaseHelper raw; or use AttendanceDao

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_entry, container, false);

        // Initialize DB helper
        dbHelper = new DatabaseHelper(requireContext());

        btnSaveEntry = view.findViewById(R.id.btn_save_entry);
        btnSaveEntry.setOnClickListener(v -> saveEntry());

        return view;
    }

    private void saveEntry() {
        // Gather values from views (we could pass references or find them)
        // For brevity, we'll retrieve from activity via interface; but here we'll just demonstrate inserting a dummy record.

        // Get date (default today)
        long todayMillis = System.currentTimeMillis();

        // Status - we need to get from chips; for now default Full
        String status = "Full";

        // In/Out times - placeholder
        int inHour = 9, inMin = 0, outHour = 17, outMin = 0;

        double advance = 0;
        String notes = "";

        // Compute earnings using SalaryCalculator - we need salary settings; assume stored somewhere
        // For demo, use hardcoded values or retrieve from SharedPref
        double baseSalary = 30000; // retrieve from preferences
        int workingDays = 26;
        double workHours = 8;

        SalaryCalculator calc = new SalaryCalculator(baseSalary, workingDays, workHours);
        double earnings = calc.computeEarnings(status);

        // Create entity
        AttendanceEntity entity = new AttendanceEntity();
        entity.setDateMillis(todayMillis);
        entity.setStatus(status);
        entity.setInHour(inHour);
        entity.setInMinute(inMin);
        entity.setOutHour(outHour);
        entity.setOutMinute(outMin);
        entity.setAdvanceTaken(advance);
        entity.setNotes(notes);
        entity.setDayEarnings(earnings);

        // Insert
        dbHelper.addAttendance(entity);

        Toast.makeText(requireContext(), "Entry saved", Toast.LENGTH_SHORT).show();

        // TODO: refresh dashboard
    }
}
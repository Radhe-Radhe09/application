package com.example.attendancetracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ProfileFragment.OnProfileSavedListener,
        SalaryFragment.OnSalarySavedListener {

    private TextView tvTotalDays, tvFull, tvHalf, tvAbsent, tvGross, tvAdvance, tvNet;
    private AttendanceAdapter adapter;
    private DatabaseHelper dbHelper;
    private SalaryCalculator calculator;
    private List<AttendanceEntity> allRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        tvTotalDays = findViewById(R.id.tv_total_days);
        tvFull = findViewById(R.id.tv_full);
        tvHalf = findViewById(R.id.tv_half);
        tvAbsent = findViewById(R.id.tv_absent);
        tvGross = findViewById(R.id.tv_gross);
        tvAdvance = findViewById(R.id.tv_advance);
        tvNet = findViewById(R.id.tv_net);

        Button btnProfile = findViewById(R.id.btn_profile);
        Button btnSalary = findViewById(R.id.btn_salary);
        Button btnDailyEntry = findViewById(R.id.btn_daily_entry);
        Button btnExport = findViewById(R.id.btn_export);

        dbHelper = new DatabaseHelper(this);

        // Load existing records and update UI
        updateSummary();

        // Set fragment transactions
        btnProfile.setOnClickListener(v -> replaceFragment(new ProfileFragment()));
        btnSalary.setOnClickListener(v -> replaceFragment(new SalaryFragment()));
        btnDailyEntry.setOnClickListener(v -> replaceFragment(new DailyEntryFragment()));
        btnExport.setOnClickListener(v -> showExportOptions());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    // ----- Listener callbacks from fragments -----

    @Override
    public void onProfileSaved(String company, String employee, String department) {
        Toast.makeText(this, "Profile saved: " + company, Toast.LENGTH_SHORT).show();
        // Could store in SharedPrefs already inside fragment; just refresh if needed.
        updateSummary();
    }

    @Override
    public void onSalarySaved(double baseMonthlySalary, int workingDays, double workHoursPerDay) {
        // Initialize calculator with provided values
        calculator = new SalaryCalculator(baseMonthlySalary, workingDays, workHoursPerDay);
        Toast.makeText(this, "Salary settings saved", Toast.LENGTH_SHORT).show();
        updateSummary();
    }

    // Helper to compute summary from DB records
    private void updateSummary() {
        allRecords = dbHelper.getAllAttendance(); // returns list (implementation may vary)

        int fullCount = 0, halfCount = 0, absentCount = 0;
        double totalEarnings = 0;
        double totalAdvance = 0;

        for (AttendanceEntity entity : allRecords) {
            switch (entity.getStatus()) {
                case "Full":
                    fullCount++;
                    break;
                case "Half":
                    halfCount++;
                    break;
                case "Absent":
                    absentCount++;
                    break;
            }
            totalEarnings += entity.getDayEarnings();
            totalAdvance += entity.getAdvanceTaken();
        }

        int totalDaysLogged = fullCount + halfCount + absentCount; // total entries

        double netReceivable = calculator != null
                ? calculator.computeNetSalary(totalEarnings, totalAdvance)
                : totalEarnings - totalAdvance;

        DecimalFormat df = new DecimalFormat("##.##");

        tvTotalDays.setText(String.valueOf(totalDaysLogged));
        tvFull.setText(String.valueOf(fullCount));
        tvHalf.setText(String.valueOf(halfCount));
        tvAbsent.setText(String.valueOf(absentCount));
        tvGross.setText("₹" + df.format(totalEarnings));
        tvAdvance.setText("₹" + df.format(totalAdvance));
        tvNet.setText("₹" + df.format(netReceivable));
    }

    private void showExportOptions() {
        // Simple dialog or direct export; here we just export CSV as example
        if (allRecords == null || allRecords.isEmpty()) {
            Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT).show();
            return;
        }

        ExportUtil exportUtil = new ExportUtil(this);
        Uri csvUri = exportUtil.exportCsv(allRecords);
        if (csvUri != null) {
            Toast.makeText(this, "CSV exported", Toast.LENGTH_SHORT).show();
            // Optionally open file intent
        } else {
            Toast.makeText(this, "Export failed", Toast.LENGTH_SHORT).show();
        }
    }
}
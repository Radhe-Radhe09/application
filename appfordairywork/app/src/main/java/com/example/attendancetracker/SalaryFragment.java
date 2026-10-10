package com.example.attendancetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class SalaryFragment extends Fragment {

    private EditText etBaseSalary, etWorkingDays, etWorkHours;
    private Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_salary_setup, container, false);

        etBaseSalary = view.findViewById(R.id.et_base_salary);
        etWorkingDays = view.findViewById(R.id.et_working_days);
        etWorkHours = view.findViewById(R.id.et_work_hours);
        btnSave = view.findViewById(R.id.btn_save_salary);

        btnSave.setOnClickListener(v -> {
            String baseStr = etBaseSalary.getText().toString().trim();
            String daysStr = etWorkingDays.getText().toString().trim();
            String hoursStr = etWorkHours.getText().toString().trim();

            if (baseStr.isEmpty() || daysStr.isEmpty() || hoursStr.isEmpty()) {
                // show error
                return;
            }

            double base = Double.parseDouble(baseStr);
            int days = Integer.parseInt(daysStr);
            double hours = Double.parseDouble(hoursStr);

            // We could store these in SharedPref or DB; for demo store in SharedPref
            SharedPrefHelper helper = new SharedPrefHelper(requireContext());
            // We'll store as a simple string combo; real app would use a POJO in Room.
            helper.setCompany(String.valueOf(base)); // reuse key or add new keys

            // TODO: forward to daily entry screen
        });

        return view;
    }
}
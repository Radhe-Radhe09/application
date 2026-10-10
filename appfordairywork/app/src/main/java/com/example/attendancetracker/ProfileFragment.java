package com.example.attendancetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ProfileFragment extends Fragment {

    private EditText etCompany, etEmployee, etDepartment;
    private Button btnSave;
    private SharedPrefHelper sharedPrefHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        etCompany = view.findViewById(R.id.et_company_name);
        etEmployee = view.findViewById(R.id.et_employee_name);
        etDepartment = view.findViewById(R.id.et_department);
        btnSave = view.findViewById(R.id.btn_save_profile);

        sharedPrefHelper = new SharedPrefHelper(requireContext());

        // Load existing defaults
        sharedPrefHelper.applyToFields(etCompany, etEmployee, etDepartment);

        btnSave.setOnClickListener(v -> {
            String company = etCompany.getText().toString().trim();
            String employee = etEmployee.getText().toString().trim();
            String department = etDepartment.getText().toString().trim();

            sharedPrefHelper.setCompany(company);
            sharedPrefHelper.setEmployee(employee);
            sharedPrefHelper.setDepartment(department);

            // Notify listener (activity) to proceed or show toast
            if (getTargetFragment() != null) {
                // communicate via interface if needed
            }
        });

        return view;
    }
}
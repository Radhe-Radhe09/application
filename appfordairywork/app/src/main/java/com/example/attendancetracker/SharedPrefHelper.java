package com.example.attendancetracker;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class SharedPrefHelper {

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    // Keys
    private static final String KEY_COMPANY = "key_company";
    private static final String KEY_EMPLOYEE = "key_employee";
    private static final String KEY_DEPARTMENT = "key_department";

    public SharedPrefHelper(Context context) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPrefs.edit();
    }

    public String getCompany() {
        return sharedPrefs.getString(KEY_COMPANY, "");
    }

    public void setCompany(String company) {
        editor.putString(KEY_COMPANY, company);
        editor.apply();
    }

    public String getEmployee() {
        return sharedPrefs.getString(KEY_EMPLOYEE, "");
    }

    public void setEmployee(String employee) {
        editor.putString(KEY_EMPLOYEE, employee);
        editor.apply();
    }

    public String getDepartment() {
        return sharedPrefs.getString(KEY_DEPARTMENT, "");
    }

    public void setDepartment(String department) {
        editor.putString(KEY_DEPARTMENT, department);
        editor.apply();
    }

    // Method to load all defaults into UI fields (example)
    public void applyToFields(android.widget.EditText etCompany,
                              android.widget.EditText etEmployee,
                              android.widget.EditText etDepartment) {
        etCompany.setText(getCompany());
        etEmployee.setText(getEmployee());
        etDepartment.setText(getDepartment());
    }
}
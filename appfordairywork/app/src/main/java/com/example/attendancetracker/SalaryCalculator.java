package com.example.attendancetracker;

/**
 * Utility class to compute per-day and per-hour salary,
 * and daily earnings based on attendance status.
 */
public class SalaryCalculator {

    private double baseMonthlySalary;
    private int workingDaysPerMonth;
    private double standardWorkHoursPerDay;

    public SalaryCalculator(double baseMonthlySalary, int workingDaysPerMonth, double standardWorkHoursPerDay) {
        this.baseMonthlySalary = baseMonthlySalary;
        this.workingDaysPerMonth = workingDaysPerMonth;
        this.standardWorkHoursPerDay = standardWorkHoursPerDay;
    }

    /**
     * Returns per-day salary (monthly / workingDays)
     */
    public double getPerDaySalary() {
        if (workingDaysPerMonth <= 0) return 0;
        return baseMonthlySalary / workingDaysPerMonth;
    }

    /**
     * Returns per-hour rate (perDaySalary / standardWorkHoursPerDay)
     */
    public double getPerHourRate() {
        double perDay = getPerDaySalary();
        if (standardWorkHoursPerDay <= 0) return 0;
        return perDay / standardWorkHoursPerDay;
    }

    /**
     * Compute earnings for a given status.
     * status: "Full", "Half", "Absent"
     */
    public double computeEarnings(String status) {
        double perDay = getPerDaySalary();
        switch (status) {
            case "Full":
                return perDay;
            case "Half":
                return perDay / 2.0;
            case "Absent":
                return 0.0;
            default:
                return 0.0;
        }
    }

    /**
     * Compute net salary after advance deduction.
     */
    public double computeNetSalary(double totalEarnings, double totalAdvance) {
        return totalEarnings - totalAdvance;
    }
}
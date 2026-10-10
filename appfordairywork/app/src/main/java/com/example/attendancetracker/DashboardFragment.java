package com.example.attendancetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private AttendanceAdapter adapter;
    private TextView tvTotalDays, tvFull, tvHalf, tvAbsent, tvGross, tvAdvance, tvNet;
    private Button btnExport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AttendanceAdapter();
        recyclerView.setAdapter(adapter);

        btnExport = view.findViewById(R.id.btn_export);
        btnExport.setOnClickListener(v -> exportData());

        tvTotalDays = view.findViewById(R.id.tv_total_days);
        tvFull = view.findViewById(R.id.tv_full);
        tvHalf = view.findViewById(R.id.tv_half);
        tvAbsent = view.findViewById(R.id.tv_absent);
        tvGross = view.findViewById(R.id.tv_gross); // note: id may differ; we have only one tv_gross in layout but need to map
        tvAdvance = view.findViewById(R.id.tv_advance);
        tvNet = view.findViewById(R.id.tv_net);

        loadAndUpdate();

        return view;
    }

    private RecyclerView recyclerView;

    private void loadAndUpdate() {
        // In a full implementation, we'd fetch from DB and compute summary.
        // For now, just placeholder update.
        updateSummary();
        adapter.setOnItemClickListener((position, entity) -> {
            // Handle edit/delete if needed
        });
    }

    private void updateSummary() {
        // Placeholder; real implementation would query DB and compute.
        DecimalFormat df = new DecimalFormat("##.##");
        tvTotalDays.setText("Total Days: 0");
        tvFull.setText("Full: 0");
        tvHalf.setText("Half: 0");
        tvAbsent.setText("Absent: 0");
        tvGross.setText("Gross: ₹0.00");
        tvAdvance.setText("Advance: ₹0.00");
        tvNet.setText("Net: ₹0.00");
    }

    private void exportData() {
        // similar to MainActivity.showExportOptions
        ExportUtil exportUtil = new ExportUtil(requireContext());
        // we need list of entities; for now just toast.
        Toast.makeText(requireContext(), "Export initiated", Toast.LENGTH_SHORT).show();
    }
}
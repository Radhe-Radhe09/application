package com.example.attendancetracker;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AttendanceAdapter extends ListAdapter<AttendanceEntity, AttendanceAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public AttendanceAdapter() {
        super(diffCallback);
    }

    private static final DiffCallback diffCallback = new DiffCallback<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate item layout
        android.view.View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AttendanceEntity entity = getItem(position);

        // Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String dateStr = entity.getDateMillis() > 0 ? sdf.format(new Date(entity.getDateMillis())) : "N/A";
        holder.tvDate.setText(dateStr);

        // Status
        String status = entity.getStatus();
        holder.tvStatus.setText(status);
        // Set badge color
        int color;
        switch (status) {
            case "Full":
                color = android.R.color.holo_green_dark;
                break;
            case "Half":
                color = android.R.color.holo_orange_dark;
                break;
            case "Absent":
                color = android.R.color.holo_red_dark;
                break;
            default:
                color = android.R.color.darker_gray;
        }
        holder.tvStatus.setTextColor(holder.itemView.getResources().getColor(color));

        // Earnings (day earnings stored in entity)
        double earnings = entity.getDayEarnings();
        holder.tvEarnings.setText(String.format("₹%.2f", earnings));

        // Advance
        double advance = entity.getAdvanceTaken();
        holder.tvAdvance.setText(String.format("₹%.2f", advance));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position, entity);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(int position, AttendanceEntity entity);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        android.widget.TextView tvDate;
        android.widget.TextView tvStatus;
        android.widget.TextView tvEarnings;
        android.widget.TextView tvAdvance;

        ViewHolder(android.view.View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvEarnings = itemView.findViewById(R.id.tv_earnings);
            tvAdvance = itemView.findViewById(R.id.tv_advance);
        }
    }
}
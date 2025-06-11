package com.example.alarmapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ClockAdapter extends RecyclerView.Adapter<ClockAdapter.ClockViewHolder> {

    private List<ClockItem> clockList;
    private OnClockLongClickListener longClickListener;

    public interface OnClockLongClickListener {
        void onClockLongClick(int position, ClockItem clockItem);
    }

    public ClockAdapter(List<ClockItem> clockList) {
        this.clockList = clockList;
    }

    public void setOnClockLongClickListener(OnClockLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public ClockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clock, parent, false);
        return new ClockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClockViewHolder holder, int position) {
        ClockItem clockItem = clockList.get(position);
        TimeZone tz = TimeZone.getTimeZone(clockItem.getTimeZoneId());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(tz);

        int offsetMillis = tz.getRawOffset();
        int hours = offsetMillis / (1000 * 60 * 60);
        String gmt = String.format("GMT%s%d", hours >= 0 ? "+" : "-", Math.abs(hours));

        String[] parts = tz.getID().split("/");
        String locationName = parts[parts.length - 1].replace('_', ' ');

        holder.tvTimeZone.setText(locationName + " (" + gmt + ")");
        holder.tvTime.setText(sdf.format(new Date()));

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onClockLongClick(holder.getAdapterPosition(), clockItem);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return clockList.size();
    }

    public void refreshTimes() {
        notifyDataSetChanged();
    }

    public static class ClockViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeZone, tvTime;

        public ClockViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeZone = itemView.findViewById(R.id.tvTimeZone);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}


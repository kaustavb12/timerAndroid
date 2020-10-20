package com.kaustav.timer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaustav.timer.R;
import com.kaustav.timer.roomdb.entity.Item;

import java.util.List;

public class TimerRunTimeAdapter extends RecyclerView.Adapter<TimerRunTimeAdapter.ViewHolder> {

    private List<Item> timeList;

    public TimerRunTimeAdapter(List<Item> timeList){
        this.timeList = timeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.timer_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rowCountTextView.setText(String.valueOf(position+1));
        holder.hrTextView.setText(timeList.get(position).getHrFormat());
        holder.minTextView.setText(timeList.get(position).getMinFormat());
        holder.secTextView.setText(timeList.get(position).getSecFormat());
        holder.itemActiveView.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView hrTextView, minTextView, secTextView, rowCountTextView;
        public ImageView itemActiveView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hrTextView = itemView.findViewById(R.id.hrTextView);
            minTextView = itemView.findViewById(R.id.minTextView);
            secTextView = itemView.findViewById(R.id.secTextView);
            rowCountTextView = itemView.findViewById(R.id.rowCountTextView);
            itemActiveView = (ImageView) itemView.findViewById(R.id.itemActiveView);
        }
    }
}

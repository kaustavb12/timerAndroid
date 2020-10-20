package com.kaustav.timer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaustav.timer.R;
import com.kaustav.timer.SetTimePopActivity;
import com.kaustav.timer.roomdb.entity.Item;

import java.util.List;

public class TimerItemAdapter extends  RecyclerView.Adapter<TimerItemAdapter.ViewHolder>{

    private List<Item> timeList;

    public TimerItemAdapter(List<Item> timeList){
        this.timeList = timeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.timer_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        private TextView hrTextView, minTextView, secTextView, rowCountTextView;
        private final Context context;
        public ImageView itemActiveView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            hrTextView = itemView.findViewById(R.id.hrTextView);
            minTextView = itemView.findViewById(R.id.minTextView);
            secTextView = itemView.findViewById(R.id.secTextView);
            rowCountTextView = itemView.findViewById(R.id.rowCountTextView);
            itemActiveView = (ImageView) itemView.findViewById(R.id.itemActiveView);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), SetTimePopActivity.class);
            intent.putExtra("hr", timeList.get(getAdapterPosition()).getHrFormat());
            intent.putExtra("min", timeList.get(getAdapterPosition()).getMinFormat());
            intent.putExtra("sec", timeList.get(getAdapterPosition()).getSecFormat());
            intent.putExtra("itemPos", getAdapterPosition());
            ((Activity)context).startActivityForResult(intent, 1);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 101, 0, "Delete");
        }
    }

}

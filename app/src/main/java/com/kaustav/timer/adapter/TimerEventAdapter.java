package com.kaustav.timer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaustav.timer.R;
import com.kaustav.timer.StartTimerActivity;
import com.kaustav.timer.roomdb.entity.Event;

import java.util.List;

public class TimerEventAdapter extends RecyclerView.Adapter<TimerEventAdapter.ViewHolder> {

    private List<Event> eventList;

    public TimerEventAdapter(List<Event> eventList){
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.timer_event, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventNoTextView.setText(String.valueOf(position+1));
        holder.eventNameTextView.setText(eventList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        private TextView eventNoTextView, eventNameTextView;
        private final Context context;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            eventNoTextView = itemView.findViewById(R.id.eventNoTextView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), StartTimerActivity.class);
            intent.putExtra("eventId",eventList.get(getAdapterPosition()).id);
            context.startActivity(intent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 101, 0, "Edit");
            menu.add(getAdapterPosition(), 102,1,"Delete");
        }


    }

}

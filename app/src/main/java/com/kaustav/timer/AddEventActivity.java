package com.kaustav.timer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaustav.timer.adapter.TimerEventAdapter;
import com.kaustav.timer.roomdb.database.EventDB;
import com.kaustav.timer.roomdb.entity.Event;

import java.util.List;

public class AddEventActivity extends AppCompatActivity {

    private List<Event> eventList;
    private EventDB db;
    private TimerEventAdapter adapter;

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 101:
                Intent intent = new Intent(this, EditEventActivity.class);
                intent.putExtra("eventId",eventList.get(item.getGroupId()).id);
                intent.putExtra("eventName",eventList.get(item.getGroupId()).name);
                startActivityForResult(intent, 1);
                return true;
            case 102:
                Event event = eventList.get(item.getGroupId());
                db.itemDAO().deleteItemForEvent(event.id);
                db.eventDAO().deleteEvent(event);
                eventList.remove(event);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        FloatingActionButton button = findViewById(R.id.createEventButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditEventActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        db = EventDB.getDbInstance(getApplicationContext());
        eventList = db.eventDAO().loadAllEvents();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        adapter = new TimerEventAdapter(eventList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                int eventId = data.getIntExtra("eventId", -1);
                String eventName = data.getStringExtra("eventName");

                if (eventId != -1){
                    boolean existing = false;
                    for (int i=0; i<eventList.size(); i++){
                        if(eventList.get(i).id == eventId){
                            eventList.get(i).name = eventName;
                            existing = true;
                            break;
                        }
                    }
                    if(!existing){
                        Event event = new Event();
                        event.id = eventId;
                        event.name = eventName;
                        eventList.add(event);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
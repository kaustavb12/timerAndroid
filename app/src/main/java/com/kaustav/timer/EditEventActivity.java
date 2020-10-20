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
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaustav.timer.adapter.TimerItemAdapter;
import com.kaustav.timer.roomdb.database.EventDB;
import com.kaustav.timer.roomdb.entity.Event;
import com.kaustav.timer.roomdb.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class EditEventActivity extends AppCompatActivity {

    List<Item> itemList;
    private TimerItemAdapter adapter;
    private int eventId;
    private EventDB db;
    List<Item> deleteList = new ArrayList<Item>();

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 101:
                Item timerItem = itemList.get(item.getGroupId());
                if(timerItem.id != 0){
                    deleteList.add(timerItem);
                }
                itemList.remove(timerItem);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        FloatingActionButton fab = findViewById(R.id.addTimerButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SetTimePopActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Intent intent = getIntent();
        String eventName = intent.getStringExtra("eventName");
        eventId = intent.getIntExtra("eventId", -1);

        EditText nameText = (EditText) findViewById(R.id.editEventText);
        nameText.setText(eventName);

        db = EventDB.getDbInstance(getApplicationContext());

        if(eventId != -1){
            itemList = db.itemDAO().loadItemsforEvent(eventId);
        } else {
            itemList = new ArrayList<Item>();
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

        adapter = new TimerItemAdapter(itemList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.timerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String hr = data.getStringExtra("hr");
                String min = data.getStringExtra("min");
                String sec = data.getStringExtra("sec");
                int itemPos = data.getIntExtra("itemPos", -1);
                if(itemPos == -1){
                    Item item = new Item();
                    item.setHrFormat(hr);
                    item.setMinFormat(min);
                    item.setSecFormat(sec);
                    itemList.add(item);
                } else {
                    itemList.get(itemPos).setHrFormat(hr);
                    itemList.get(itemPos).setMinFormat(min);
                    itemList.get(itemPos).setSecFormat(sec);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void saveEvent(View view){
        Intent intent = new Intent();

        String eventName = ((EditText) findViewById(R.id.editEventText)).getText().toString();
        eventName = (eventName.isEmpty()) ? "Unnamed" :  eventName;

        if(eventId != -1 || itemList.size() != 0){
            if(eventId == -1){
                Event event = new Event();
                event.name = eventName;
                eventId = (int) db.eventDAO().insertEvent(event);
            } else {
                Event event = new Event();
                event.name = eventName;
                event.id = eventId;
                db.eventDAO().updateEvent(event);
            }

            if(itemList.size() != 0){
                for (int i=0; i<itemList.size(); i++){
                    if(itemList.get(i).id == 0){
                        itemList.get(i).eventId = eventId;
                        db.itemDAO().insertItem(itemList.get(i));
                    } else {
                        db.itemDAO().updateItem(itemList.get(i));
                    }
                }
            }

            if (deleteList.size() != 0){
                for (int i=0; i<deleteList.size(); i++){
                    db.itemDAO().deleteItem(deleteList.get(i));
                }
            }
        }

        intent.putExtra("eventId", eventId);
        intent.putExtra("eventName", eventName);

        setResult(RESULT_OK, intent);
        finish();
    }
}
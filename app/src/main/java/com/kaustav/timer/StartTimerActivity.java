package com.kaustav.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaustav.timer.adapter.TimerRunTimeAdapter;
import com.kaustav.timer.helper.AlertTone;
import com.kaustav.timer.roomdb.database.EventDB;
import com.kaustav.timer.roomdb.entity.Event;
import com.kaustav.timer.roomdb.entity.Item;

import java.util.List;

public class StartTimerActivity extends AppCompatActivity {

    private Boolean started = false;
    private CountDownTimer timer;
    private long fullTime = 0;
    private Boolean repeat = false;
    List<Item> itemList;
    private RecyclerView recyclerView;
    private int itemNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_timer);

        Intent intent = getIntent();
        int eventId = intent.getIntExtra("eventId", -1);

        EventDB db = EventDB.getDbInstance(getApplicationContext());
        Event event = db.eventDAO().loadEventById(eventId);

        if(event != null){
            TextView nameText = findViewById(R.id.eventNameTxtView);
            nameText.setText(event.name);

            itemList = db.itemDAO().loadItemsforEvent(eventId);
            if(itemList.size() == 0){
                finish();
            }
        } else {
            finish();
        }



        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        TimerRunTimeAdapter adapter = new TimerRunTimeAdapter(itemList);

        recyclerView = (RecyclerView) findViewById(R.id.runTimerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void startTimer(View view){
        TextView timerView = findViewById(R.id.timerView);
        Button button = (Button) view;
        if(!started){
            itemNo = 0;
            startItem(button);
            started = true;
            button.setText(R.string.buttonStop);
        } else {
            timer.cancel();
            stopItem();
            fullStopAction(timerView, button);
        }
    }

    private void fullStopAction(TextView timerView, Button button){
        fullTime = 0;
        timerView.setText(getFormatTime(fullTime));
        started = false;
        button.setText(R.string.buttonStart);
    }

    private void startItem(Button button){
        setFullTime(itemList.get(itemNo));
        TextView timerView = findViewById(R.id.timerView);
        timerView.setText(getFormatTime(fullTime));

        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForLayoutPosition(itemNo);
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.itemActiveView);
        imageView.setVisibility(View.VISIBLE);

        initializeTimer(timerView, button);
        timer.start();
    }

    private void stopItem(){
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForLayoutPosition(itemNo);
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.itemActiveView);
        imageView.setVisibility(View.INVISIBLE);
    }

    private void initializeTimer(TextView timerView, Button button){
            timer = new CountDownTimer(fullTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerView.setText(getFormatTime(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    Handler handler = new Handler();
                    AlertTone tone = new AlertTone();
                    final Thread thread = new Thread(new Runnable() {
                        public void run() {
                            handler.post(new Runnable() {

                                public void run() {
                                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    tone.playSound();
                                    vibrator.vibrate(VibrationEffect.createWaveform(new long[]{50,500,500,500},-1));
                                }
                            });
                        }
                    });
                    thread.start();
                    stopItem();
                    itemNo++;
                    if(itemNo < itemList.size()){
                        startItem(button);
                    } else if(repeat) {
                        itemNo = 0;
                        startItem(button);
                    } else {
                        fullStopAction(timerView, button);
                    }
                }
            };
    }

    public void repeatToggle(View view){
        ImageButton button = (ImageButton) view;
        if(repeat){
            button.setImageResource(R.drawable.ic_repeat_once);
            repeat = false;
        } else {
            button.setImageResource(R.drawable.ic_repeat);
            repeat = true;
        }
    }

    private void setFullTime(Item item){
        fullTime = ((((item.hr * 60) + item.min) * 60) + item.sec) * 1000;
    }

    private String getFormatTime(long milliSec){
        long hr = (milliSec / 3600000);
        milliSec -= hr * 3600000;
        long min = milliSec / 60000;
        milliSec -= min * 60000;
        long sec = milliSec / 1000;
        return addFormat(hr)+":"+addFormat(min)+":"+addFormat(sec);
    }

    private String addFormat(long time){
        if(time < 10){
            return "0"+ Long.toString(time);
        } else {
            return Long.toString(time);
        }
    }
}
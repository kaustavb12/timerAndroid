package com.kaustav.timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class SetTimePopActivity extends Activity {

    private int itemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.2));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        Intent intent = getIntent();
        String hr = intent.getStringExtra("hr");
        String min = intent.getStringExtra("min");
        String sec = intent.getStringExtra("sec");
        itemPos = intent.getIntExtra("itemPos", -1);
        EditText hrText = (EditText) findViewById(R.id.hrTextNumber);
        hrText.setText(hr);

        EditText minText = (EditText) findViewById(R.id.minTextNumber);
        minText.setText(min);

        EditText secText = (EditText) findViewById(R.id.secTextNumber);
        secText.setText(sec);
    }

    public void setTime(View view){
        Intent intent = new Intent();

        String hr = checkBlank(((EditText) findViewById(R.id.hrTextNumber)).getText().toString());
        String min = checkBlank(((EditText) findViewById(R.id.minTextNumber)).getText().toString());
        String sec = checkBlank(((EditText) findViewById(R.id.secTextNumber)).getText().toString());

        intent.putExtra("hr", hr);
        intent.putExtra("min", min);
        intent.putExtra("sec", sec);
        intent.putExtra("itemPos", itemPos);

        setResult(RESULT_OK, intent);
        finish();
    }

    private String checkBlank(String time){
        if(time.isEmpty()){
            return "00";
        }
        return time;
    }
}
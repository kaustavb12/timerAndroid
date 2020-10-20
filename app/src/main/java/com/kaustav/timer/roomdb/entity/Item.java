package com.kaustav.timer.roomdb.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int eventId;
    public int hr;
    public int min;
    public int sec;

    public void setHrFormat(String hr){
        this.hr = Integer.parseInt(hr);
    }

    public void setMinFormat(String min){
        this.min = Integer.parseInt(min);
    }

    public void setSecFormat(String sec){
        this.sec = Integer.parseInt(sec);
    }

    public String getHrFormat(){
        return addFormat(hr);
    }

    public String getMinFormat(){
        return addFormat(min);
    }

    public String getSecFormat(){
        return addFormat(sec);
    }

    private String addFormat(long time){
        if(time < 10){
            return "0"+ Long.toString(time);
        } else {
            return Long.toString(time);
        }
    }

}

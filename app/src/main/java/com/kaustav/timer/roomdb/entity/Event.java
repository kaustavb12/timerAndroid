package com.kaustav.timer.roomdb.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
}

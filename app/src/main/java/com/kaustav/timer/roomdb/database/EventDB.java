package com.kaustav.timer.roomdb.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kaustav.timer.roomdb.dao.EventDAO;
import com.kaustav.timer.roomdb.dao.ItemDAO;
import com.kaustav.timer.roomdb.entity.Event;
import com.kaustav.timer.roomdb.entity.Item;

@Database(entities = {Event.class, Item.class}, version = 1)
public abstract class EventDB extends RoomDatabase {
    public abstract EventDAO eventDAO();
    public abstract ItemDAO itemDAO();

    private static EventDB dbInstance;

    public static EventDB getDbInstance(Context context) {
        if(dbInstance == null){
            dbInstance = Room.databaseBuilder(context, EventDB.class, "event_database").allowMainThreadQueries().build();
        }

        return dbInstance;
    }
}

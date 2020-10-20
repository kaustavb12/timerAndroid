package com.kaustav.timer.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kaustav.timer.roomdb.entity.Event;

import java.util.List;

@Dao
public interface EventDAO {
    @Insert
    public long insertEvent(Event event);

    @Update
    public void updateEvent(Event event);

    @Delete
    public void deleteEvent(Event event);

    @Query("SELECT * FROM event")
    public List<Event> loadAllEvents();

    @Query("SELECT * FROM event WHERE id = :eventId")
    public Event loadEventById(int eventId);
}

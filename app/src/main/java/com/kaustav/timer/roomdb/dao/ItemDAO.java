package com.kaustav.timer.roomdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kaustav.timer.roomdb.entity.Item;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    public long insertItem(Item item);

    @Update
    public void updateItem(Item item);

    @Delete
    public void deleteItem(Item item);

    @Query("DELETE FROM item WHERE eventId = :eventId")
    public void deleteItemForEvent(int eventId);

    @Query("SELECT * FROM item WHERE eventId = :eventId")
    public List<Item> loadItemsforEvent(int eventId);
}

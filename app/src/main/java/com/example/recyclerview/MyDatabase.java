package com.example.recyclerview;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();

    public static MyDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, MyDatabase.class, "my_database").build();
    }
}

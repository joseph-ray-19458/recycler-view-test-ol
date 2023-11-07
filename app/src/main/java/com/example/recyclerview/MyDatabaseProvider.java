package com.example.recyclerview;

import android.content.Context;

import androidx.room.Room;

public class MyDatabaseProvider {

    private static volatile MyDatabase INSTANCE;

    public static MyDatabase getDatabase(Context context) {
//        if (database == null) {
//            database = MyDatabase.getInstance(context);
//        }
//        return database;


        if (INSTANCE == null){
            synchronized (MyDatabase.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class,
                            "Todo").build();
                }
            }
        }

        return INSTANCE ;
    }

}

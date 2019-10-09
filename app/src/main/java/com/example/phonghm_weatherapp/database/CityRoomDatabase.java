package com.example.phonghm_weatherapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.phonghm_weatherapp.model.KeyData;
import com.example.phonghm_weatherapp.myinterface.CityAccess;

@Database(entities = {KeyData.class}, version = 1, exportSchema = false)
public abstract class CityRoomDatabase extends RoomDatabase {
    public abstract CityAccess cityAccess();

    private static CityRoomDatabase INSTANCE;

    public static CityRoomDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), CityRoomDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}



package com.example.phonghm_weatherapp.myinterface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.phonghm_weatherapp.model.KeyData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CityAccess {
    @Insert(onConflict = REPLACE)
    void insertCity(KeyData city);

    @Update(onConflict = REPLACE)
    void updateCity(KeyData city);

    @Delete
    void deleteCity(KeyData city);

    @Query("SELECT * FROM KeyData")
    List<KeyData> findAllCitySync();

}

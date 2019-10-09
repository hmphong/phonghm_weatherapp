package com.example.phonghm_weatherapp.viewmodel;

import androidx.databinding.BaseObservable;

import com.example.phonghm_weatherapp.model.City;

public class ShowViewModel extends BaseObservable {
    private City city;

    public ShowViewModel(City city) {
        this.city = city;
    }

    public String getNameCity() {
        return city.getmName();
    }

    public String getAirIndex() {
        return city.getmIndex();
    }

    public String getRanking() {
        return city.getmRanking();
    }

    public String getTime() {
        return city.getmTime();
    }

}

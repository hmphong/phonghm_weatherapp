package com.example.phonghm_weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.phonghm_weatherapp.adapter.MyAdapter;
import com.example.phonghm_weatherapp.database.CityRoomDatabase;
import com.example.phonghm_weatherapp.databinding.ActivityMainBinding;
import com.example.phonghm_weatherapp.model.City;
import com.example.phonghm_weatherapp.model.KeyData;
import com.example.phonghm_weatherapp.viewmodel.ShowViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSON_REQUEST = 1;
    public static final int REQ_CODE_SPEECH_INPUT = 100;
    CityRoomDatabase cityRoomDatabase;
    String city = "";
    String path = "";
    public static List<City> arrC = new ArrayList<>();
    public List<KeyData> arrDb;
    ActivityMainBinding activityMainBinding;

    RecyclerView mRecycler;
    MyAdapter adapter;
    List<ShowViewModel> list ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) && (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQ_CODE_SPEECH_INPUT);
            }
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            city = removeAccent(myLocation(MainActivity.this, location.getLatitude(), location.getLongitude()));
            path = "https://api.waqi.info/feed/" + city + "/?token=d2e7e890a2014c900396dc1f505b4eba3d890bd1";
            cityRoomDatabase = CityRoomDatabase.getInMemoryDatabase(MainActivity.this);
            getCityInfo(path);

        }
        cityRoomDatabase = CityRoomDatabase.getInMemoryDatabase(getApplicationContext());

        mRecycler = findViewById(R.id.reCycler);

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permisson granted", Toast.LENGTH_SHORT).show();
                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        city = removeAccent(myLocation(MainActivity.this, location.getLatitude(), location.getLongitude()));
                        path = "https://api.waqi.info/feed/" + city + "/?token=d2e7e890a2014c900396dc1f505b4eba3d890bd1";
                        cityRoomDatabase = CityRoomDatabase.getInMemoryDatabase(MainActivity.this);
                        getCityInfo(path);
                    } else {
                        Toast.makeText(this, "No permisson granted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }
    }

    public void getCityInfo(String path) {

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, path, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String ranking = "";
                        String cityn;
                        String airIndex;
                        String time;
                        try {

                            JSONObject jsonObject = response.getJSONObject("data");
                            JSONObject cityObject = jsonObject.getJSONObject("city");
                            JSONObject timeObject = jsonObject.getJSONObject("time");
                            cityn = cityObject.getString("name");
                            airIndex = jsonObject.getString("aqi");
                            time = timeObject.getString("s");
                            if (Integer.parseInt(airIndex) >= 0) {
                                ranking = "Good";
                            } else if (Integer.parseInt(airIndex) >= 51) {
                                ranking = "Moderate";
                            } else if (Integer.parseInt(airIndex) >= 101) {
                                ranking = "Unhealthy for sensitive";
                            } else if (Integer.parseInt(airIndex) >= 151) {
                                ranking = "Unhealthy";
                            } else if (Integer.parseInt(airIndex) >= 201) {
                                ranking = "Very unhealthy";
                            } else if (Integer.parseInt(airIndex) >= 301) {
                                ranking = "Hazardous";
                            }
                            KeyData keyData = new KeyData();
                            keyData.name = city;
                            keyData.air_index = airIndex;
                            keyData.ranking = ranking;
                            keyData.time = time;


                            City c = new City(city, airIndex, ranking, time);
                            ShowViewModel showViewModel = new ShowViewModel(c);

                            arrDb = cityRoomDatabase.cityAccess().findAllCitySync();
                            if (arrDb.size() == 0
                            ) {
                                cityRoomDatabase.cityAccess().insertCity(keyData);
                            }

                            Log.e("sizeee", arrDb.size() + "");
                            list = new ArrayList<>();
                            for (int i = 0; i < arrDb.size(); i++) {
                                list.add(new ShowViewModel(new City(arrDb.get(i).name, arrDb.get(i).air_index, arrDb.get(i).ranking, arrDb.get(i).time)));
                            }
                            adapter = new MyAdapter(list);
                            mRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

                            for (int i = 0; i <arrDb.size() ; i++) {
                                if ((arrDb.get(i).time).equals(list.get(i).getTime())){
                                    Log.e("s4","");
                                } else {
                                    cityRoomDatabase.cityAccess().insertCity(keyData);
                                }
                            }

                            mRecycler.setAdapter(adapter);

                            activityMainBinding.setViewmodel(showViewModel);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public String myLocation(Context context, double lat, double lon) {
        String cityName = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> arrAddr;

        try {
            arrAddr = geocoder.getFromLocation(lat, lon, 10);
            if (arrAddr.size() > 0) {
                for (Address adr : arrAddr) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        cityName = adr.getAdminArea();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }

    public static String removeAccent(String s) {
        String a = s.replace(" ", "").toUpperCase
                ();
        String temp = Normalizer.normalize(a, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    public void getAllDatabase() {
        arrDb = cityRoomDatabase.cityAccess().findAllCitySync();
        for (int i = 0; i < arrC.size(); i++) {
            arrC.add(new City(arrDb.get(i).name,
                    arrDb.get(i).air_index,
                    arrDb.get(i).ranking,
                    arrDb.get(i).time));
        }
        Log.e("gegege", " " + arrDb.size());
    }

}

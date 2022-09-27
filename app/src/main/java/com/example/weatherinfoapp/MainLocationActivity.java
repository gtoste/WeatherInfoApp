package com.example.weatherinfoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainLocationActivity extends AppCompatActivity {

    WeatherAPI apiInterface;
    TextView tv_cityName;
    TextView tv_temp;
    TextView tv_weather;
    TextView tv_humudity;
    TextView tv_maxTemp;
    TextView tv_minTemp;
    TextView tv_pressure;
    TextView tv_windspeed;
    ImageView icon_weather;


    LocationManager locationManager;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_location);


        findViewById(R.id.changeView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainLocationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mContext = this;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    500,
                    50, locationListenerGPS);
        }




        apiInterface = RetrofitWeather.getClient().create(WeatherAPI.class);

        tv_cityName = findViewById(R.id.cityname);
        tv_temp = findViewById(R.id.tv_temp);
        icon_weather = findViewById(R.id.weather_icon);
        tv_weather = findViewById(R.id.tv_weather);
        tv_humudity = findViewById(R.id.humidity);
        tv_maxTemp = findViewById(R.id.max_temp);
        tv_minTemp = findViewById(R.id.min_temp);
        tv_pressure = findViewById(R.id.pressure);
        tv_windspeed = findViewById(R.id.wind_speed);





    }

    LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();

            Call<OpenWeatherApp> getCityInfo = apiInterface.getWeatherWithLocation(latitude, longitude);

            getCityInfo.enqueue(new Callback<OpenWeatherApp>() {
                @Override
                public void onResponse(Call<OpenWeatherApp> call, Response<OpenWeatherApp> response) {
                    OpenWeatherApp weather = response.body();
                    String name = weather.getName();

                    String weatherD = weather.getWeather().get(0).getDescription();
                    Main main = weather.getMain();
                    String humudity = String.valueOf(main.getHumidity());
                    String tempMax = Double.toString(main.getTempMax());
                    String temp =   Double.toString(main.getTemp());
                    String tempMin = Double.toString(main.getTempMin());
                    String pressure = String.valueOf(main.getPressure());
                    String windspeed = Double.toString(weather.getWind().getSpeed());
                    String srcRaw = "https://openweathermap.org/img/wn/" +weather.getWeather().get(0).getIcon() + "@2x.png";
                    Log.d("ddd", srcRaw);
                    Picasso.get()
                            .load(srcRaw)
                            .resize(100,100).into(icon_weather);

                    tv_cityName.setText(name);
                    tv_temp.setText(temp);
                    tv_weather.setText(weatherD);
                    tv_humudity.setText(humudity);
                    tv_maxTemp.setText(tempMax);
                    tv_minTemp.setText(tempMin);
                    tv_pressure.setText(pressure);
                    tv_windspeed.setText(windspeed);
                }

                @Override
                public void onFailure(Call<OpenWeatherApp> call, Throwable t) {

                }
            });
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListenerGPS);
        }
    }
}
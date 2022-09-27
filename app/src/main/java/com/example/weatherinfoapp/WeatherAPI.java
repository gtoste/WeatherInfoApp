package com.example.weatherinfoapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather?appid=e607b4cb7f8e88d338d04c47243c3676&units=metric")
    Call<OpenWeatherApp> getWeatherWithLocation(
            @Query("lat")double lat,
            @Query("lon")double lon
    );
    @GET("weather?appid=e607b4cb7f8e88d338d04c47243c3676&units=metric")
    Call<OpenWeatherApp> getWeatherWithName(@Query("q")String name);

}

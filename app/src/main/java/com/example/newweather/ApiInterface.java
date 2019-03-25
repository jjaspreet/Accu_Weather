package com.example.newweather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("weather")
    Call<CurrentWeather> getdetails(@Query("lat") Double lat, @Query("lon") Double lon, @Query("appid") String appid);
}


//2500d94ca16a5ed83df0eafb00b35e7a
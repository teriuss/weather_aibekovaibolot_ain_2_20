package com.example.weathercontrol;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyGetter {
    @GET("/v1/forecast.json?key=05dbb88ed6bf44bfad4185023221305&aqi=no")
    Call<GetWeatherModel> getWeather(@Query("q") String city);
}

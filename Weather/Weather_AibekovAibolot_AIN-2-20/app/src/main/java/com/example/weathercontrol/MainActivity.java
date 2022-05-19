package com.example.weathercontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout slideUp;
    private LinearLayout slide;
    private TextView cityNameTextView;
    private TextView localtime;
    private TextView condition;
    private TextView temp_c;
    private TextView max_min_temp;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView cloud;
    private ImageView noon;
    private ImageView night;
    private EditText city;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slideUp = findViewById(R.id.slideUp);
        slide = findViewById(R.id.slide);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        localtime = findViewById(R.id.localtime);
        condition = findViewById(R.id.condition);
        temp_c = findViewById(R.id.temp_c);
        max_min_temp = findViewById(R.id.max_min_temp);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        wind = findViewById(R.id.wind);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        cloud = findViewById(R.id.cloud);
        noon = findViewById(R.id.noon);
        night = findViewById(R.id.night);
        city = findViewById(R.id.city);
        search = findViewById(R.id.search);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.weatherapi.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        MyGetter myGetter = retrofit.create(MyGetter.class);
        Call<GetWeatherModel> call = myGetter.getWeather("Bishkek");
        call.enqueue(new Callback<GetWeatherModel>() {
            @Override
            public void onResponse(Call<GetWeatherModel> call, Response<GetWeatherModel> response) {
                cityNameTextView.setText(response.body().getLocation().getCityName());
                localtime.setText(response.body().getLocation().getLocaltime().toString());
                condition.setText(response.body().getCurrent().getCondition().getCondition());
                temp_c.setText(response.body().getCurrent().getTemp_c() + "°C");
                max_min_temp.setText(response.body().getForecast().getForecastdays().get(0).getDay().getMaxtemp() + "°C↑ \n" + response.body().getForecast().getForecastdays().get(0).getDay().getMintemp() + "°C↓ ");
                humidity.setText(response.body().getCurrent().getHumidity() + "%");
                pressure.setText(response.body().getCurrent().getPressure() + "\nmBar");
                wind.setText(response.body().getCurrent().getWind() + " km/h");
                sunrise.setText(response.body().getForecast().getForecastdays().get(0).getAstro().getSunrise());
                sunset.setText(response.body().getForecast().getForecastdays().get(0).getAstro().getSunset());
                cloud.setText(response.body().getCurrent().getCloud() + "%");
                if(response.body().getCurrent().isDay() == 0) {
                    noon.setVisibility(View.GONE);
                    night.setVisibility(View.VISIBLE);
                } else {
                    noon.setVisibility(View.VISIBLE);
                    night.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetWeatherModel> call, Throwable t) {
                System.out.println(t.getMessage());
            }

        });

        slideUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slide.getVisibility() == View.GONE){
                    slide.setVisibility(View.VISIBLE);
                } else{
                    slide.setVisibility(View.GONE);
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!city.getText().toString().isEmpty()) {
                    Call<GetWeatherModel> call = myGetter.getWeather(city.getText().toString());
                    call.enqueue(new Callback<GetWeatherModel>() {
                        @Override
                        public void onResponse(Call<GetWeatherModel> call, Response<GetWeatherModel> response) {
                            cityNameTextView.setText(response.body().getLocation().getCityName());
                            localtime.setText(response.body().getLocation().getLocaltime().toString());
                            condition.setText(response.body().getCurrent().getCondition().getCondition());
                            temp_c.setText(response.body().getCurrent().getTemp_c() + "°C");
                            max_min_temp.setText(response.body().getForecast().getForecastdays().get(0).getDay().getMaxtemp() + "°C↑ \n" + response.body().getForecast().getForecastdays().get(0).getDay().getMintemp() + "°C↓ ");
                            humidity.setText(response.body().getCurrent().getHumidity() + "%");
                            pressure.setText(response.body().getCurrent().getPressure() + "\nmBar");
                            wind.setText(response.body().getCurrent().getWind() + " km/h");
                            sunrise.setText(response.body().getForecast().getForecastdays().get(0).getAstro().getSunrise());
                            sunset.setText(response.body().getForecast().getForecastdays().get(0).getAstro().getSunset());
                            cloud.setText(response.body().getCurrent().getCloud() + "%");
                            if (response.body().getCurrent().isDay() == 0) {
                                noon.setVisibility(View.GONE);
                                night.setVisibility(View.VISIBLE);
                            } else {
                                noon.setVisibility(View.VISIBLE);
                                night.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<GetWeatherModel> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }

                    });
                    slide.setVisibility(View.GONE);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Заполните поле!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
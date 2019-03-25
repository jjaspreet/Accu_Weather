package com.example.newweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7,textView8,textView9;
    ImageView imageView;

    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.txtcity);
     //   textView2 = findViewById(R.id.txttemp);
        textView3 = findViewById(R.id.txtdescription);
        textView4 = findViewById(R.id.txtdate);

        textView6 = findViewById(R.id.txtpressure);
        textView7 = findViewById(R.id.txthumidity);
        textView8 = findViewById(R.id.txtsunrise);
        textView9 = findViewById(R.id.txtsunset);
        imageView = findViewById(R.id.imageview);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy,HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        textView4.setText(date);
        Glide.with(this)
                .load("http://openweathermap.org/img/w/10d.png")
                .into(imageView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                } else {
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        ApiInterface service = ApiService.getClient().create(ApiInterface.class);
        Call<CurrentWeather> call = service.getdetails(location.getLatitude(), location.getLongitude(),"2500d94ca16a5ed83df0eafb00b35e7a");
        call.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                Log.e(" mainAction", "  response "+ response.body().toString());
                Log.e(" mainAction", "  rom - "+ response.body().getName().toString());
                textView1.setText(response.body().getName().toString());
                Log.e(" mainAction", "  rom - "+ response.body().getMain().getTemp().toString());
                textView6.setText(response.body().getMain().getTemp().toString() + "Pa");
                Log.e(" mainAction", "  rom - "+ response.body().getWeather().get(0).getDescription().toString());
                textView3.setText(response.body().getWeather().get(0).getDescription().toString());
                Log.e(" mainAction", "  rom - "+ response.body().getMain().getHumidity().toString());
                textView7.setText(response.body().getMain().getHumidity().toString() + "%");

                Log.e(" mainAction", "  rom - "+ response.body().getCoord().getLon().toString());
                textView8.setText(response.body().getCoord().getLon().toString() + "°");
                Log.e(" mainAction", "  rom - "+ response.body().getCoord().getLat().toString());
                textView9.setText(response.body().getCoord().getLat().toString() +"°");
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Oops!Something went wrong", Toast.LENGTH_SHORT).show();
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
}

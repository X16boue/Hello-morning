package project.hellomorning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        (findViewById(R.id.jumpToForm)).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        });

        (findViewById(R.id.APItest)).setOnClickListener(v -> {
            String JSONcurrent =  GetMethod.getCurrentWeather("Pohang");
            Map<String, String> currentResult = GetMethod.extractCurrentFromJSON(JSONcurrent);
            Log.w("current", currentResult.toString());
            String JSONForecast = GetMethod.getForecastWeather("Pohang");
            Map<String, String> forecastResult = GetMethod.extractForecastFromJSON(JSONForecast);
            Log.w("forecast results", forecastResult.toString());
        });
    }
}
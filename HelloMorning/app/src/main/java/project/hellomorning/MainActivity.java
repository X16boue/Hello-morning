package project.hellomorning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String filename = "userdata";

        List<String> materials = fetchMaterialFromFile(filename);
        String userName = materials.get(0);
        String workCityName = materials.get(1);

        TextView helloUser = (findViewById(R.id.helloUser));
        helloUser.setText("Hello " + userName);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        (findViewById(R.id.jumpToForm)).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        });

        (findViewById(R.id.APItest)).setOnClickListener(v -> {
            String JSONcurrent =  GetMethod.getCurrentWeather(workCityName);
            Map<String, String> currentResult = GetMethod.extractCurrentFromJSON(JSONcurrent);
            Log.w("current", currentResult.toString());
            String JSONForecast = GetMethod.getForecastWeather(workCityName);
            Map<String, String> forecastResult = GetMethod.extractForecastFromJSON(JSONForecast);
            Log.w("forecast results", forecastResult.toString());
            List<String> recommendations = recommendationDecision(currentResult, forecastResult);
            Log.w("recommendations", recommendations.toString());
        });
    }

    protected List<String> fetchMaterialFromFile(String filename){
        Context context = getApplicationContext();
        ArrayList<String> output = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(filename)){
            ObjectInputStream ois = new ObjectInputStream(fis);
            output = (ArrayList<String>) ois.readObject();
            ois.close();
            Toast.makeText(getApplicationContext(),"Materials loaded", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
        }
        return output;
    }

    protected List<String> recommendationDecision(Map<String, String> weatherData, Map<String, String> weatherForecast){
        List<String> recommendation = new ArrayList<>();
        recommendation.add("mask");
        if(Double.valueOf(weatherData.get("temperature")) > 28.0){
            if(weatherData.get("description").equals("Clear")){
                if(Double.valueOf(weatherData.get("wind")) < 2.0){
                    if(Double.valueOf(weatherData.get("humidity")) < 70.0){
                        recommendation.add("parasol");
                        recommendation.add("umbrella");
                        recommendation.add("sunglasses");
                        recommendation.add("water botttle");
                    }
                    else{
                        recommendation.add("stay indoor");
                    }
                }
            }
        }
        return recommendation;
    }
}
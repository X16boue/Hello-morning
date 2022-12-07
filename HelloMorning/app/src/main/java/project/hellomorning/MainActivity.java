package project.hellomorning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
}
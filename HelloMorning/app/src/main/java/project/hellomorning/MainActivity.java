package project.hellomorning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
        Log.w("materials", materials.toString());

        String userName = "Unknown user";
        String workCityName = "Pohang";
        int equipmentOffset=0;
        //try catch statement because on first opening the file will have no data
        try{
            if((materials.get(0) != null) && (!(materials.get(0).equals("")))){
                userName = materials.get(0);
            }
            if((materials.get(1) != null) && (!(materials.get(1).equals("")))){
                workCityName = materials.get(1);
            }
            equipmentOffset = Integer.parseInt(materials.get(2));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        List<String> userEquipment = new ArrayList<>();
        List<String> userTransportation = new ArrayList<>();

        if (materials.size() > 3){
            if ((materials.get(3).equals("")) || (materials.get(3) == null)){
                userEquipment = new ArrayList<>();
            } else {
                userEquipment = materials.subList(3, equipmentOffset);
            }
        } else{
            Toast.makeText(getApplicationContext(),"No equipment please fill the form", Toast.LENGTH_SHORT).show();
        }

        if(equipmentOffset > 0){
            userTransportation = materials.subList(equipmentOffset, materials.size());
        }

        Log.w("citname", workCityName);
        Log.w("userEquipment", userEquipment.toString());
        Log.w("userTransportation", userTransportation.toString());

        TextView helloUser = (findViewById(R.id.helloUser));
        helloUser.setText("Hello " + userName);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        List<String> recommendations = makeFetchingAndRecommendation(workCityName, userEquipment);
        buildToTakeList(recommendations);

        (findViewById(R.id.jumpToForm)).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
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

    protected List<String> makeFetchingAndRecommendation(String workCityName, List<String> userPossesion){
        String JSONcurrent = GetMethod.getCurrentWeather(workCityName);
        Map<String, String> currentResult = GetMethod.extractCurrentFromJSON(JSONcurrent);
        Log.w("current", currentResult.toString());
        String JSONForecast = GetMethod.getForecastWeather(workCityName);
        Map<String, String> forecastResult = GetMethod.extractForecastFromJSON(JSONForecast);
        Log.w("forecast results", forecastResult.toString());
        List<String> recomendation = recommendationDecision(currentResult, forecastResult);
        Log.w("recommmendations before trim", String.valueOf(recomendation));
        return trimRecommendation(recomendation, userPossesion);
    }

    protected List<String> recommendationDecision(Map<String, String> weatherData, Map<String, String> weatherForecast){
        List<String> recommendation = new ArrayList<>();
        recommendation.add("mask");
        if((Double.valueOf(weatherData.get("temperature")) > 28.0) || (Double.valueOf(weatherForecast.get("temperature")) > 28.0)){
            if(weatherData.get("description").equals("Clear")){
                recommendation.add("sunglasses");
                recommendation.add("water botttle");
                if(Double.valueOf(weatherData.get("wind")) < 2.0){
                    if(Double.valueOf(weatherData.get("humidity")) < 70.0){
                        recommendation.add("parasol");
                        recommendation.add("umbrella");
                    }
                } else if(Double.valueOf(weatherData.get("wind") )< 10.0){
                    //srong wind
                }
            } else if(weatherData.get("description").equals("Rain")) {
                if(Double.valueOf(weatherData.get("wind")) < 2.0) {
                    recommendation.add("umbrella");
                } else {
                    recommendation.add("raincoat");
                }
            } else if(weatherData.get("description").equals("Cloudy")) {
                recommendation.add("portable fan");
            }
        } else if((Double.valueOf(weatherData.get("temperature")) > 12.0) || (Double.valueOf(weatherForecast.get("temperature")) > 12.0)) {
            if(weatherData.get("description").equals("Clear")) {
                recommendation.add("sunglasses");
                recommendation.add("cap");
            } else if(weatherData.get("description").equals("Rain")) {
                if (Double.valueOf(weatherData.get("wind")) < 2.0) {
                    recommendation.add("umbrella");
                } else {
                    recommendation.add("raincoat");
                }
            }
        } else {
            if(weatherData.get("description").equals("Clear")) {
                recommendation.add("hat");
                recommendation.add("scarf");
                recommendation.add("gloves");

            } else if(weatherData.get("description").equals("Rain")) {
                if (Double.valueOf(weatherData.get("wind")) < 2.0) {
                    recommendation.add("umbrella");
                } else {
                    recommendation.add("raincoat");
                }
            }
            if((Double.valueOf(weatherData.get("temperature")) < 5.0) || (Double.valueOf(weatherForecast.get("temperature")) < 5.0)) {
                recommendation.add("thermo mask");
            }
        }
            return recommendation;

    }

    protected List<String> trimRecommendation(List<String> recommendation, List<String> userPossesion){
        List<String> trimRecommendation = new ArrayList<>();
        for(String reco : recommendation){
            Log.w("reco", reco);
            if(userPossesion.contains(reco)){
                trimRecommendation.add(reco);
                Log.w(reco, "conservé");
            } else  Log.w(reco, "non");

        }
        return trimRecommendation;
    }

    protected void buildToTakeList(List<String> recommendations){
        ArrayList<View> childrenToAdd = new ArrayList<>();
        LinearLayout layout = (LinearLayout) findViewById(R.id.checklist);

        for(String item : recommendations){
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(item);
            childrenToAdd.add(cb);
            layout.addView(cb);

        }
        Log.w("recommendations", recommendations.toString());
        Log.w("children", String.valueOf(childrenToAdd.size()));
        //layout.addChildrenForAccessibility(childrenToAdd);

    }

}
package project.hellomorning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> userEquipment = new ArrayList<>();
        List<String> userTransportation = new ArrayList<>();
        //user data extraction
        String filename = "userdata";
        String userName = "Unknown user";
        String workCityName = "Pohang";
        int equipmentOffset=0;

        List<String> materials = fetchMaterialFromFile(filename);
        Log.w("materials", materials.toString());

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


        //weather fetching
        String JSONcurrent = GetMethod.getCurrentWeather(workCityName);
        Map<String, String> currentResult = GetMethod.extractCurrentFromJSON(JSONcurrent);
        Log.w("current", currentResult.toString());
        String JSONForecast = GetMethod.getForecastWeather(workCityName);
        Map<String, String> forecastResult = GetMethod.extractForecastFromJSON(JSONForecast);
        Log.w("forecast results", forecastResult.toString());
        displayWeatherInfo(currentResult, forecastResult, workCityName);


        //recommendation making
        List<String>[] recommendations = makeFetchingAndRecommendation(workCityName, userEquipment, userTransportation, currentResult, forecastResult);
        List<String> recommmendedEquipment = recommendations[0];
        List<String> recommmendedTransportation = recommendations[1];

        //display of recommendation
        buildEquipmentList(recommmendedEquipment);
        buildTransportationList(recommmendedTransportation);


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

    protected List<String>[] makeFetchingAndRecommendation(String workCityName, List<String> userEquipment, List<String> userTransportation, Map<String, String> currentResult, Map<String, String> forecastResult){

        List<String> recomendation = recommendationDecision(currentResult, forecastResult);
        Log.w("recommmendations before trim", String.valueOf(recomendation));

        List<String> trimmedEquipment = trimRecommendation(recomendation, userEquipment);
        List<String> trimmedTransportation = trimRecommendation(recomendation, userTransportation);
        //trick to return two variables
        List<String>[] output = new List[2];
        output[0] = trimmedEquipment;
        output[1] = trimmedTransportation;
        return output ;
    }

    protected List<String> recommendationDecision(Map<String, String> weatherData, Map<String, String> weatherForecast){
        List<String> recommendation = new ArrayList<>();
        recommendation.add("mask");

        //general case for rain
        if(weatherData.get("description").equals("Rain")) {
            recommendation.add("car");
            recommendation.add("public transportation");
            if (Double.valueOf(weatherData.get("wind")) < 2.0) {
                recommendation.add("umbrella");
            } else {
                recommendation.add("raincoat");
            }
        }




        if((Double.valueOf(weatherData.get("temperature")) > 28.0) || (Double.valueOf(weatherForecast.get("temperature")) > 28.0)){
            if(weatherData.get("description").equals("Clear")){
                recommendation.add("sunglasses");
                recommendation.add("water botttle");
                if(Double.valueOf(weatherData.get("wind")) < 2.0){
                    recommendation.add("bike");
                    recommendation.add("scooter");
                    recommendation.add("walk");
                    if(Double.valueOf(weatherData.get("humidity")) < 70.0){
                        recommendation.add("parasol");
                    }
                } else if(Double.valueOf(weatherData.get("wind") )< 10.0){
                    //srong wind
                    recommendation.add("car");
                    recommendation.add("public transportation");
                }
            } else if(weatherData.get("description").equals("Cloudy")) {
                recommendation.add("portable fan");
                recommendation.add("bike");
                recommendation.add("scooter");
                recommendation.add("walk");
            }
        } else if((Double.valueOf(weatherData.get("temperature")) > 12.0) || (Double.valueOf(weatherForecast.get("temperature")) > 12.0)) {
            if(weatherData.get("description").equals("Clear")) {
                recommendation.add("sunglasses");
                recommendation.add("cap");
                recommendation.add("bike");
                recommendation.add("scooter");
                recommendation.add("walk");
            }
        } else {
            if(weatherData.get("description").equals("Clear")) {
                recommendation.add("hat");
                recommendation.add("scarf");
                recommendation.add("gloves");

            }
            if((Double.valueOf(weatherData.get("temperature")) < 5.0) || (Double.valueOf(weatherForecast.get("temperature")) < 5.0)) {
                recommendation.add("thermo mask");
                recommendation.add("car");
                recommendation.add("public transportation");
            } else{
                recommendation.add("bike");
                recommendation.add("scooter");
                recommendation.add("walk");
            }
        }
            return recommendation;

    }

    protected List<String> trimRecommendation(List<String> recommendation, List<String> userPossesion){
        List<String> trimRecommendation = new ArrayList<>();
        for(String reco : recommendation){
            if(userPossesion.contains(reco)){
                trimRecommendation.add(reco);
            }

        }
        return trimRecommendation;
    }

    protected void buildEquipmentList(List<String> equipment){
        LinearLayout layout = (LinearLayout) findViewById(R.id.checklist);

        for(String item : equipment){
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(item);
            layout.addView(cb);
        }
    }

    protected void buildTransportationList(List<String> tranportation){
        LinearLayout layout = (LinearLayout) findViewById(R.id.tranportationOptions);
        StringBuilder toDisplay = new StringBuilder();
        for(String item : tranportation){
            toDisplay.append(item);
            toDisplay.append("\n");
        }
        TextView viewToDisplay = new TextView(getApplicationContext());
        viewToDisplay.setText(toDisplay);
        layout.addView(viewToDisplay);
    }

    protected void displayWeatherInfo(Map<String, String> currentResult, Map<String, String> forecastresult, String workCityName){
        TextView presentationView = findViewById(R.id.weatherAnnoucement);
        TextView descriptionView = findViewById(R.id.weatherDescription);
        TextView temperatureView = findViewById(R.id.weatherTemperature);
        TextView windView = findViewById(R.id.weatherWind);
        TextView humidityView = findViewById(R.id.weatherHumidity);
        presentationView.setText("Here is the weather for today in " + workCityName);
        descriptionView.setText(String.format("Description : %s then %s", currentResult.get("description"), forecastresult.get("description")));
        temperatureView.setText(String.format("Temperature : %,.1f °C then %,.1f °C", Double.parseDouble(currentResult.get("temperature")), Double.parseDouble(forecastresult.get("temperature"))));
        windView.setText(String.format("Wind : %s m/s then %s m/s", currentResult.get("wind"), forecastresult.get("wind")));
        humidityView.setText(String.format("Humidity : %s %% then %s %%", currentResult.get("humidity"), forecastresult.get("humidity")));
    }


}
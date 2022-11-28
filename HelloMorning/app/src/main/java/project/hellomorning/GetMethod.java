package project.hellomorning;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GetMethod {
    Map<String, String> forecast = new HashMap<String, String>();


    public static String getCurrentWeather(String city) {
        BufferedReader in;
        String data = null;
        HttpURLConnection urlConnection = null;

        try {
            String stringUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=35eabfbc88074474775d676e2f0fc2ef";
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String l;
            String nl = System.getProperty("line.separator");
            while ((l = in.readLine()) != null) {
                sb.append(l).append(nl);
            }
            in.close();
            data = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }

    public static String getForecastWeather(String latitude, String longitude) {
        BufferedReader in;
        String data = null;
        HttpURLConnection urlConnection = null;

        try {
            String stringUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&cnt=2&appid=35eabfbc88074474775d676e2f0fc2ef";
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String l;
            String nl = System.getProperty("line.separator");
            while ((l = in.readLine()) != null) {
                sb.append(l).append(nl);
            }
            in.close();
            data = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }


    public static Map<String, String> extractForecastFromJSON(String json){
        Map<String, String> forecast = new HashMap<String, String>();
        try{
            JSONObject jsonResponse = new JSONObject(json);
            JSONArray jsonArray = jsonResponse.getJSONArray("list");
            JSONObject jsonObject3hours = jsonArray.getJSONObject(1);

            JSONObject jsonObjectMain = jsonObject3hours.getJSONObject("main");
            double temp = jsonObjectMain.getDouble("temp") - 273.15;
            forecast.put("temperature", String.valueOf(temp));

            JSONObject jsonObjectWind = jsonObject3hours.getJSONObject("wind");
            double wind = jsonObjectWind.getDouble("speed");
            forecast.put("wind", String.valueOf(wind));

            JSONArray jsonWeatherArray = jsonObject3hours.getJSONArray("weather");
            JSONObject jsonObjectWeather = jsonWeatherArray.getJSONObject(0);
            String description = jsonObjectWeather.getString("main");
            forecast.put("description", description);

            int humidity = jsonObject3hours.getInt("humidity");
            forecast.put("humidity", String.valueOf(humidity));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return forecast;
    }


    public static double getTemperature(String json){
        double temp = 0.0;
        try {
            JSONObject jsonResponse = new JSONObject(json);
            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
            temp = jsonObjectMain.getDouble("temp") - 273.15;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static double getWindSpeed(String json){
        double wind = 0.0;
        try {
            JSONObject jsonResponse = new JSONObject(json);
            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
            wind = jsonObjectWind.getDouble("speed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wind;
    }

    public static int getHumidity(String json){
        int humid = 0;
        try {
            JSONObject jsonResponse = new JSONObject(json);
            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
            humid = jsonObjectMain.getInt("humidity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return humid;
    }

    public static String getDescription(String json){
        String description = "";
        try {
            JSONObject jsonResponse = new JSONObject(json);
            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
            description = jsonObjectWeather.getString("main");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return description;
    }

}
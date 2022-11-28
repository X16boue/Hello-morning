package project.hellomorning;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetMethod {


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
            String stringUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&appid=35eabfbc88074474775d676e2f0fc2ef";
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
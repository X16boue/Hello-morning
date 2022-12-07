package project.hellomorning;

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

    public static String getForecastWeather(String cityname) {

        String latLonJSONResponse = getLatLonFromCityName(cityname);
        String[] latLon = latLonFromJSON(latLonJSONResponse);
        String latitude = latLon[0];
        String longitude = latLon[1];

        return getForecastWeather(latitude, longitude);
    }


    public static String getLatLonFromCityName(String cityName) {
        BufferedReader in;
        String data = null;
        HttpURLConnection urlConnection = null;

        try {
            String stringUrl = "https://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&appid=35eabfbc88074474775d676e2f0fc2ef";
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

    public static String[] latLonFromJSON(String json){
        String[] latLon = new String[2];
        try{
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonResponse = jsonArray.getJSONObject(0);
            double lat = jsonResponse.getDouble("lat");
            double lon = jsonResponse.getInt("lon");
            latLon[0] = String.valueOf(lat);
            latLon[1] = String.valueOf(lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return latLon;
    }


    public static Map<String, String> extractForecastFromJSON(String json){
        Map<String, String> forecast = new HashMap<>();
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

            int humidity = jsonObjectMain.getInt("humidity");
            forecast.put("humidity", String.valueOf(humidity));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return forecast;
    }


    public static Map<String, String> extractCurrentFromJSON(String json){
        Map<String, String> current = new HashMap<>();
        try{
            JSONObject jsonResponse = new JSONObject(json);
            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");

            double temp = jsonObjectMain.getDouble("temp") - 273.15;
            current.put("temperature", String.valueOf(temp));

            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
            double wind = jsonObjectWind.getDouble("speed");
            current.put("wind", String.valueOf(wind));

            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
            String description = jsonObjectWeather.getString("main");
            current.put("description", description);

            int humidity = jsonObjectMain.getInt("humidity");
            current.put("humidity", String.valueOf(humidity));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return current;
    }

}
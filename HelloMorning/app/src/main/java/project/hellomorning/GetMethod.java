package project.hellomorning;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class GetMethod {


    public static String getData(String city) {
        BufferedReader in = null;
        String data = null;
        HttpURLConnection urlConnection = null;

        try {
            String stringUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=35eabfbc88074474775d676e2f0fc2ef";
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String l = "";
            String nl = System.getProperty("line.separator");
            while ((l = in.readLine()) != null) {
                sb.append(l + nl);
            }
            in.close();
            data = sb.toString();
            Log.w("", data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }

    public static String getAsString(String response) {
        DecimalFormat df = new DecimalFormat("#.##");

        String output = "";
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
            //JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
            double temp = jsonObjectMain.getDouble("temp") - 273.15;
            double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
            int humidity = jsonObjectMain.getInt("humidity");
            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
            String wind = jsonObjectWind.getString("speed");
            JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
            String clouds = jsonObjectClouds.getString("all");
            JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
            String cityName = jsonResponse.getString("name");

            output += "Current weather of " + cityName
                    + "\n Temp: " + df.format(temp) + " °C"
                    + "\n Feels Like: " + df.format(feelsLike) + " °C"
                    + "\n Humidity: " + humidity + "%"
                    + "\n Wind Speed: " + wind + "m/s (meters per second)"
                    + "\n Cloudiness: " + clouds + "%";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

}
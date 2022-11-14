package project.hellomorning;

import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.scheme.Scheme;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class GetMethod {

    public static String getData() {
        BufferedReader in = null;
        String data = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Seoul&appid=35eabfbc88074474775d676e2f0fc2ef");
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
        } catch(Exception e) {
            e.printStackTrace();
        }finally
        {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }



}
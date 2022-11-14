package project.hellomorning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        ((Button)findViewById(R.id.jumpToForm)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

        ((Button)findViewById(R.id.APItest)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.e("","method call");
                String result =  GetMethod.getData("Pohang");
                String sumup = GetMethod.getAsString(result);
                Log.w("", sumup);
                Log.e("","method called");
            }
        });
    }
}
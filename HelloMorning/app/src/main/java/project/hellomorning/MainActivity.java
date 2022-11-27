package project.hellomorning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String filename = "userdata";

        List<String> materials = fetchMaterialFromFile(filename);
        String userName = materials.get(0);
        String workCityName = materials.get(1);


        ((Button)findViewById(R.id.jumpToForm)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }
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
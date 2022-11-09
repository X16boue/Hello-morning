package project.hellomorning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        List<String> checked = new ArrayList<>();

        CheckBox checkUmbrella = findViewById(R.id.umbrella);
        CheckBox checkBike = findViewById(R.id.bike);
        CheckBox checkScooter = findViewById(R.id.scooter);
        CheckBox checkCar = findViewById(R.id.car);
        Button submitButton = findViewById(R.id.submitForm);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkUmbrella.isChecked()){
                    checked.add((String) checkUmbrella.getText());
                }
                if(checkBike.isChecked()){
                    checked.add((String) checkBike.getText());
                }
                if(checkScooter.isChecked()){
                    checked.add((String) checkScooter.getText());
                }
                if(checkCar.isChecked()){
                    checked.add((String) checkCar.getText());
                }

                if (!(checked.isEmpty())){
                    saveListToFile(checked);
                    Toast.makeText(getApplicationContext(),"Answers saved", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ((Button)findViewById(R.id.jumpToHome)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(FormActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }

    protected void saveListToFile(List<String> checked)  {
        try{
            FileOutputStream fos = new FileOutputStream("userdata");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(checked);
            oos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
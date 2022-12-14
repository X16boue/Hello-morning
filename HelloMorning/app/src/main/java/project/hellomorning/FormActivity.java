package project.hellomorning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

        Button submitButton = findViewById(R.id.submitForm);

        EditText nameInput = findViewById(R.id.name_input);
        EditText workCityInput = findViewById(R.id.work_city_input);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = "";
                String workCityName= "";
                List<CheckBox> checkBoxes = getAllCheckBoxes(findViewById(R.id.checkboxSet));
                Log.w("checkboxes", checkBoxes.toString());
                for(CheckBox checkBox : checkBoxes){
                    if(checkBox.isChecked()){
                        checked.add((String) checkBox.getText());
                    }
                }

                if(nameInput.length() > 0 || !(nameInput.equals(""))){
                    userName = String.valueOf(nameInput.getText());
                }
                if(workCityInput.length() > 0 || !(workCityInput.equals(""))){
                    workCityName = String.valueOf(workCityInput.getText());
                }
                checked.add(0, userName);
                checked.add(1, workCityName);

                if (!(checked.isEmpty())){
                    saveListToFile(checked);
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

    public ArrayList<CheckBox> getAllCheckBoxes(View view) {

        ArrayList<CheckBox> returnViews = new ArrayList<>();

        LinearLayout layout = (LinearLayout) findViewById(R.id.checkboxSet);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof CheckBox) {
                returnViews.add((CheckBox) child);
            }
        }

        return returnViews;
    }

    protected void saveListToFile(List<String> checked)  {
        Context context = getApplicationContext();
        String filename = "userdata";
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)){
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(checked);
            oos.close();
            Toast.makeText(getApplicationContext(),"Answers saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
package com.android.wellenssq;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PatientActivity extends AppCompatActivity {

    String phone;
    TextView nametxt;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);


        Bundle bundle = getIntent().getExtras();
        name=bundle.get("name").toString();
         phone= bundle.getString("phone");
       nametxt=findViewById(R.id.welcome);
        nametxt.setText("Hi "+name);



    }
    public void P1_bay(View view) {

        Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);

        intent.putExtra("name",  name);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }
}
package com.android.wellenssq;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";
    boolean flag= false;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Bundle bundle = getIntent().getExtras();
        RadioGroup questions1 = (RadioGroup) findViewById(R.id.question1);
        final RadioButton q1yes= (RadioButton) findViewById(R.id.rad1yes);
        RadioButton q1no= (RadioButton) findViewById(R.id.rad1no);
        Button btnSt= (Button)findViewById(R.id.btnSt);
        final Button btntest= (Button)findViewById(R.id.btnTakeTest);
        btntest.setVisibility(View.INVISIBLE);
         name=bundle.get("name").toString();
        final String phone=bundle.getString("phone");
        final TextView nametxt=findViewById(R.id.welcome);
        nametxt.setText("Hi "+name);


        final TextView status= (TextView) findViewById(R.id.txtStatus);
        status.setVisibility(View.INVISIBLE);


        questions1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "=================>in on checked changed" );

                if(q1yes.isChecked())
                   flag=true;

            }
        });

        RadioGroup questions2 = (RadioGroup) findViewById(R.id.question2);
        final RadioButton q2yes= (RadioButton) findViewById(R.id.rad2yes);
        RadioButton q2no= (RadioButton) findViewById(R.id.rad2no);


        questions1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "=================>in on checked changed" );

                if(q2yes.isChecked())
                    flag=true;

            }
        });




       btnSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(q1yes.isChecked()==true || q2yes.isChecked()==true)
               {
                   status.setText("Based on the answers you provided, you may need COVID-19 testing.");
                   btntest.setVisibility(View.VISIBLE);

               }
               else
               {
                   status.setText("You are Safe");
                   status.setVisibility(View.VISIBLE);
                   btntest.setVisibility(View.INVISIBLE);
               }

               flag=false;


            }
        });


        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "=================>clicked test covid:");

                Intent intent = new Intent(getApplicationContext(), SearchDoctorActivity.class);
                intent.putExtra("name",name );
                intent.putExtra("category","covid" );
                intent.putExtra("phone",phone );

                startActivity(intent);


            }
        });



    }
}
package com.android.wellenssq;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Model.Doctor;
import rest.Api;
import rest.RestCalls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctoryActivity extends AppCompatActivity {

    private static final String TAG = "DoctorActivity";
    FirebaseAuth auth;
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctory);
        TextView welcomename= (TextView) findViewById(R.id.welcome);

        Spinner spinner = findViewById(R.id.Spinner01);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.timeslots, R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        Bundle bundle = getIntent().getExtras();
        String name=bundle.get("name").toString();
        String phone= bundle.getString("phone");
        String slot=spinner.getSelectedItem().toString();
        Button btnNext=(Button)findViewById(R.id.btnNext);

        Button btnSearch=(Button)findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot=spinner.getSelectedItem().toString();
                getMySlotDetails(phone,slot);
            }}
        );

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot=spinner.getSelectedItem().toString();
                Doctor doc= new Doctor();
                doc.setPhoneNumber(phone);
                nextToken(slot,doc);

            }}
        );

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                Log.d(TAG, "=================>Calling timer");
                String slot=spinner.getSelectedItem().toString();
                getMySlotDetails(phone,slot);
            }
        };


        timer.schedule(task, new Date(), 3000);


    }

    public void getMySlotDetails(String phone,String timeSlot)
    {

        Log.d(TAG, "=================>Inside Rest Call");

        try {
            //creating the api interface
            Api api = RestCalls.getApi();

            Log.d(TAG, "=================>Inside Rest Call1");


            Call<Integer> call = api.getDoctorQueue(phone,timeSlot);
            Log.d(TAG, "=================>Inside Rest Call2");


            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {


                    if(response.isSuccessful()) {
                        //  loadingProgressBar.setVisibility(View.GONE);

                        Log.d(TAG, "=================>Inside Rest Call3");
                        Integer queue= response.body();
                        Log.d(TAG, "=================>Inside Rest Call3"+queue);
                        TextView txtLabel= (TextView) findViewById(R.id.txtTokenlbl);
                        TextView txtToken= (TextView) findViewById(R.id.token);
                        Button btnNext=(Button)findViewById(R.id.btnNext);


                        txtLabel.setVisibility(View.VISIBLE);
                        txtToken.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                        txtToken.setText(""+queue);



                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.d(TAG, "=================>Call failed"+t.getMessage());

                }


            });

        }catch (Exception e) {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}



    }
 //next token
 public void nextToken(String slot,Doctor doc)
 {

     Log.d(TAG, "=================>Inside Rest Call");

     try {
         //creating the api interface
         Api api = RestCalls.getApi();

         Log.d(TAG, "=================>Inside Rest Call1");


         Call<Integer> call = api.decrementQueue(slot,doc);
         Log.d(TAG, "=================>Inside Rest Call2");


         call.enqueue(new Callback<Integer>() {
             @Override
             public void onResponse(Call<Integer> call, Response<Integer> response) {


                 if(response.isSuccessful()) {
                     //  loadingProgressBar.setVisibility(View.GONE);

                     Log.d(TAG, "=================>Inside Rest Call3");
                     Integer queue= response.body();
                     Log.d(TAG, "=================>Inside Rest Call3"+queue);

                     if(queue == 0)
                     {
                         Toast.makeText(getApplicationContext(),"No More people in queue",Toast.LENGTH_LONG).show();
                     }
                     else {

                         TextView txtLabel = (TextView) findViewById(R.id.txtTokenlbl);
                         TextView txtToken = (TextView) findViewById(R.id.token);
                         Button btnNext = (Button) findViewById(R.id.btnNext);


                         txtLabel.setVisibility(View.VISIBLE);
                         txtToken.setVisibility(View.VISIBLE);
                         btnNext.setVisibility(View.VISIBLE);
                         txtToken.setText("" + queue);
                     }


                 }
             }

             @Override
             public void onFailure(Call<Integer> call, Throwable t) {
                 Log.d(TAG, "=================>Call failed"+t.getMessage());

             }


         });

     }catch (Exception e) {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}



 }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }
}

package com.android.wellenssq;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushException;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPSimplePushNotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import Model.Doctor;
import rest.Api;
import rest.RestCalls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientDashboardActivity extends AppCompatActivity {

    private static final String TAG = "PatientDashboard";
    TextView txtdocName,txtyourToken;
    String dname,pname,docphone,category,timeSlot,latitude,longitude,patPhone;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
        Log.d(TAG, "=================>111111111111");
        txtdocName= findViewById(R.id.textdocname);
        txtyourToken= findViewById(R.id.urToken);
        Log.d(TAG, "=================>11111112222");
        Bundle bundle = getIntent().getExtras();
        dname=bundle.get("dname").toString();
        pname=bundle.get("pname").toString();
        category=bundle.get("category").toString();
        latitude=bundle.get("latitude").toString();
        longitude=bundle.get("longitude").toString();
        timeSlot=bundle.get("timeslot").toString();
        docphone= bundle.getString("docPhone");
        patPhone= bundle.getString("patPhone");
        Log.d(TAG, "=================>111133333");
        TextView nametxt=findViewById(R.id.welcome);

        nametxt.setText("Hi "+patPhone);
       // getMyToken(patPhone);
        txtdocName.setText(dname);
        Doctor doc= new Doctor();
        doc.setPhoneNumber(docphone);


        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                Log.d(TAG, "=================>Calling timer");

                getMyToken(patPhone);
            }
        };


        timer.schedule(task, new Date(), 3000);

        queuein(timeSlot,patPhone,doc);


    }

    public void getMyToken(String phone)
    {

        Log.d(TAG, "=================>Inside Rest Call");

        try {
            //creating the api interface
            Api api = RestCalls.getApi();

            Log.d(TAG, "=================>Inside Rest Call1");


            Call<Integer> call = api.getPatientToken(phone);
            Log.d(TAG, "=================>Inside Rest Call2");


            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {


                    if(response.isSuccessful()) {
                        //  loadingProgressBar.setVisibility(View.GONE);

                        Log.d(TAG, "=================>Inside Rest Call3");
                        Integer queue= response.body();
                        Log.d(TAG, "=================>Inside Rest Call3"+queue);

                        TextView txtToken= (TextView) findViewById(R.id.urToken);




                        txtToken.setText(""+queue);



                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.d(TAG, "=================>Call failed"+t.getMessage());

                }


            });

        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}



    }

    public void queuein(String slot,String patPhone, Doctor doc)
    {

        Log.d(TAG, "=================>Inside Rest Call");

        try {
            //creating the api interface
            Api api = RestCalls.getApi();

            Log.d(TAG, "=================>Inside Rest Call1");


            Call<Integer> call = api.incrementQueue(slot,patPhone,doc);
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
                            Toast.makeText(getApplicationContext(),"Slot Full",Toast.LENGTH_LONG).show();
                        }
                        else {


                            TextView txtToken= (TextView) findViewById(R.id.urToken);

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
package com.android.wellenssq;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
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


public class SearchDoctorActivity extends AppCompatActivity {

    private static final String TAG = "searchdoctoractivity";
    String  category,phone;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RestCalls restcall;
    String pname;
    Intent intent;
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);

        Bundle bundle = getIntent().getExtras();
        pname=bundle.get("name").toString();
        phone= bundle.getString("phone");
        String category= bundle.getString("category");

        TextView welcome= findViewById(R.id.welcome);
        Button btnSearch= findViewById(R.id.btnSearch);



        recyclerView = (RecyclerView) findViewById(R.id.myrecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(this);
        // StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(layoutManager);





        welcome.setText("Hi "+phone);




        Spinner spinner = findViewById(R.id.Spinner01);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.timeslots, R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        getMyToken(phone);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             try {


                Log.d(TAG, "=================>clicked test covid:");
                restcall = new RestCalls();



                String slot=spinner.getSelectedItem().toString();

                 listAvailableDoctors("Covid",slot);


                 Log.d(TAG, "=================>Got Response from call");


                // doctorAdaptor.notifyDataSetChanged();
             }catch (Exception e){
                 e.printStackTrace();
                 Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();}

            }
        });


     /*   btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "=================>clicked test covid:");
                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);


                startActivity(intent);


            }
        }); */


    }


    public void addListenerOnSpinnerItemSelection() {

    }

    public void listAvailableDoctors(String category,String timeSlot)
    {

        Log.d(TAG, "=================>Inside Rest Call");

        try {
            //creating the api interface
            Api api = RestCalls.getApi();

            Log.d(TAG, "=================>Inside Rest Call1");


            Call<List<Doctor>> call = api.getAvailableDocs(category, timeSlot);
            Log.d(TAG, "=================>Inside Rest Call2");


            call.enqueue(new Callback<List<Doctor>>() {
                @Override
                public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {


                    if(response.isSuccessful()) {
                        //  loadingProgressBar.setVisibility(View.GONE);
                        List<Doctor> docList= new ArrayList<Doctor>();
                        Log.d(TAG, "=================>Inside Rest Call3");
                        docList= response.body();

                        if(docList.size()==0)
                        {
                            Toast.makeText(getApplicationContext(),"Not Available for given Time slot",Toast.LENGTH_LONG).show();
                            recyclerView.removeAllViewsInLayout();

                        }
                        else {

                            DoctorAdaptor doctorAdaptor = new DoctorAdaptor(docList,pname,timeSlot,phone);
                            recyclerView.setAdapter(doctorAdaptor);
                        }


                    }
                }

                @Override
                public void onFailure(Call<List<Doctor>> call, Throwable t) {
                    Log.d(TAG, "=================>Call failed"+t.getMessage());

                }


            });

        }catch (Exception e) {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}




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
                        if(queue!=0)
                        {
                            TextView txtToken= (TextView) findViewById(R.id.txturto);
                            TextView mytoken= (TextView) findViewById(R.id.mytoken);
                            mytoken.setText(""+queue);

                            txtToken.setVisibility(View.VISIBLE);
                            mytoken.setVisibility(View.VISIBLE);
                            TimerTask task = new TimerTask() {

                                @Override
                                public void run() {
                                    Log.d(TAG, "=================>Calling timer");

                                    getMyToken1(phone);
                                }
                            };


                            timer.schedule(task, new Date(), 3000);

                        }

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

    public void getMyToken1(String phone)
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
                           TextView txtToken= (TextView) findViewById(R.id.txturto);
                            TextView mytoken= (TextView) findViewById(R.id.mytoken);
                            mytoken.setText(""+queue);
                            txtToken.setVisibility(View.VISIBLE);
                            mytoken.setVisibility(View.VISIBLE);
                        Button btnSearch= findViewById(R.id.btnSearch);
                        btnSearch.setVisibility(View.INVISIBLE);


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

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }
}

 class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {



    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {


        ((TextView) view).setTextColor(Color.WHITE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
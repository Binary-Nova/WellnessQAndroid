package com.android.wellenssq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.concurrent.TimeUnit;

import Model.Doctor;
import Model.Patient;
import rest.Api;
import rest.RestCalls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PhoneAuthActivity";
    Button btnGenerateOTP, btnSignIn;

    EditText etPhoneNumber, etOTP;
    RadioButton doc;
    RadioButton patient;
    String codeSent;
    String otp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    private String verificationCode;
    PhoneAuthProvider pap;
    String   phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Log.d(TAG, "=================>on Create" );
        auth = FirebaseAuth.getInstance();
        pap=PhoneAuthProvider.getInstance();
        btnGenerateOTP =findViewById(R.id.btnGenerateOtp);
        btnSignIn=findViewById(R.id.btnlogin);
        etPhoneNumber= findViewById(R.id.txtPhoneno);
        etOTP=findViewById(R.id.txtOtp);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radGroup);

        doc = (RadioButton) findViewById(R.id.radDoctor); // initiate a radio button
        patient=(RadioButton)findViewById(R.id.radPatient);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    System.out.println("User logged in");
                }
                else{
                    System.out.println("User not logged in");
                }
            }
        };



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "=================>in on checked changed" );

                if(doc.isChecked())
                    Log.d(TAG, "=================>doc checked" );
                if(patient.isChecked())
                    Log.d(TAG, "=================>patient checked" );
            }
        });



        Log.d(TAG, "=================>Getting phone number form edit text"+etPhoneNumber.getText() );
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mCallback= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "=================>code verified" );
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d(TAG, "=================>code sent failed" );
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSent = s;
                Log.d(TAG, "=================>code sent" +codeSent);
            }
        };

        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "=================>clicked generate otp:");
               // FirebaseAuth.getInstance().signOut();
               sendVerificationCode();


            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name= findViewById(R.id.txtloginName);

                Intent intent;
                if(doc.isChecked()) {
                    Doctor doc= new Doctor();
                    doc.setPhoneNumber(etPhoneNumber.getText().toString());
                    doc.setName(name.getText().toString());
                    doc.setQueueCapacity(50);
                    saveDoctor( doc);

                }
                else
                {
                    Patient patient= new Patient();
                    patient.setName(name.getText().toString());
                    patient.setPhoneNumber(etPhoneNumber.getText().toString());
                    savePatient(patient);


                }



            }
        });



    }

    private void savePatient(Patient patient) {

        Log.d(TAG, "=================>Inside Rest Call");

        try {
            //creating the api interface
            Api api = RestCalls.getApi();

            Log.d(TAG, "=================>Inside Rest Call1");


            Call<Void> call = api.savePatient(patient);
            Log.d(TAG, "=================>Inside Rest Call2");


            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {


                    if(response.isSuccessful()) {
                        //  loadingProgressBar.setVisibility(View.GONE);
                        EditText name= findViewById(R.id.txtloginName);
                        Intent  intent = new Intent(getApplicationContext(), PatientActivity.class);
                        intent.putExtra("name",name.getText().toString() );
                        intent.putExtra("phone",  etPhoneNumber.getText().toString());
                        startActivity(intent);


                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"logging failed",Toast.LENGTH_LONG).show();

                }


            });

        }catch (Exception e) {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}




    }

    private void saveDoctor(Doctor doc) {


            Log.d(TAG, "=================>Inside Rest Call");

            try {
                //creating the api interface
                Api api = RestCalls.getApi();

                Log.d(TAG, "=================>Inside Rest Call1");


                Call<Doctor> call = api.saveDoctor(doc);
                Log.d(TAG, "=================>Inside Rest Call2");


                call.enqueue(new Callback<Doctor>() {
                    @Override
                    public void onResponse(Call<Doctor> call, Response<Doctor> response) {


                        if(response.isSuccessful()) {
                            //  loadingProgressBar.setVisibility(View.GONE);
                            EditText name= findViewById(R.id.txtloginName);
                            Intent  intent = new Intent(getApplicationContext(), DoctoryActivity.class);
                            intent.putExtra("name",name.getText().toString() );
                            intent.putExtra("phone",  etPhoneNumber.getText().toString());
                            startActivity(intent);


                        }
                    }

                    @Override
                    public void onFailure(Call<Doctor> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"logging failed",Toast.LENGTH_LONG).show();

                    }


                });

            }catch (Exception e) {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}



        }



    private void verifySignInCode() {
        String code= etOTP.getText().toString();




        if(code.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter correct OTP", Toast.LENGTH_SHORT).show();
        }
        else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(),"login successful",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), PatientActivity.class);
                            EditText name= findViewById(R.id.txtloginName);
                           // doc = (RadioButton) findViewById(R.id.radDoctor); // initiate a radio button
                           // patient=(RadioButton)findViewById(R.id.radPatient);
                            if(doc.isChecked()) {
                                intent.putExtra("doc/patient", name.getText().toString());
                            }
                            else
                            {
                                intent.putExtra("doc/patient", name.getText().toString());
                            }
                            String user= auth.getCurrentUser().getPhoneNumber();
                            Toast.makeText(getApplicationContext(),user,Toast.LENGTH_LONG).show();
                            intent.putExtra("name", name.getText().toString());
                            intent.putExtra("phone",  etPhoneNumber.getText().toString());
                            startActivity(intent);


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"login failed",Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }

  private void sendVerificationCode()
  {
     phoneNumber = "+91"+etPhoneNumber.getText().toString();

      Log.d(TAG, "=================>Phone number is "+phoneNumber);
      pap.verifyPhoneNumber(
              phoneNumber,                     // Phone number to verify
              60,                           // Timeout duration
              TimeUnit.SECONDS,                // Unit of timeout
              MainActivity.this,        // Activity (for callback binding)
              mCallback);
      Log.d(TAG, "=================>inside send verification after pap"+phoneNumber+ " pap "+pap);



  }


    public void onStart(){
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);

        }
    }
}
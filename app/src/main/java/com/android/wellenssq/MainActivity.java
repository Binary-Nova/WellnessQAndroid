package com.android.wellenssq;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PhoneAuthActivity";
    Button btnGenerateOTP, btnSignIn;
    String smsCode = "123456";
    EditText etPhoneNumber, etOTP;
    String codeSent;
        String otp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    private String verificationCode;
    PhoneAuthProvider pap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.d(TAG, "=================>on Create" );
        auth = FirebaseAuth.getInstance();
        pap=PhoneAuthProvider.getInstance();
        btnGenerateOTP =findViewById(R.id.btnGenerateOtp);
        btnSignIn=findViewById(R.id.btnlogin);
        etPhoneNumber= findViewById(R.id.txtPhoneno);
        etOTP=findViewById(R.id.txtOtp);
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
                sendVerificationCode();


            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "=================>clicked login:");
                verifySignInCode();

            }
        });

    }

    private void verifySignInCode() {
        String code= etOTP.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
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

   String   phoneNumber="+917892859033";
      Log.d(TAG, "=================>inside send verification"+phoneNumber+ " pap "+pap);
      pap.verifyPhoneNumber(
              phoneNumber,                     // Phone number to verify
              60,                           // Timeout duration
              TimeUnit.SECONDS,                // Unit of timeout
              MainActivity.this,        // Activity (for callback binding)
              mCallback);
      Log.d(TAG, "=================>inside send verification after pap"+phoneNumber+ " pap "+pap);



  }
}
package com.android.hmh.docpal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpPage extends AppCompatActivity {
    EditText uotp;
    Button letsstart;
    String phoneNumber;
    FirebaseAuth mAuth;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        uotp = findViewById(R.id.enterOTP);
        letsstart = findViewById(R.id.letsstart);
        phoneNumber = getIntent().getStringExtra("phone");
        sendOTP(phoneNumber);

        letsstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEnteredOTP = uotp.getText().toString();
                if(userEnteredOTP.isEmpty()){
                    uotp.setError("OTP cannot be Empty");
                    return;
                }
                Toast.makeText(OtpPage.this, "All Good Brooooooooo", Toast.LENGTH_LONG).show();
                Intent i = new Intent(OtpPage.this, HomePagePatient.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void sendOTP(String phoneNumber) {
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OtpPage.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        openHomePage();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OtpPage.this, "Verification Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verify Failes", e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationCode = s;
                        resendingToken = forceResendingToken;
                        Toast.makeText(OtpPage.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                        super.onCodeSent(s, forceResendingToken);
                    }
                });
        PhoneAuthProvider.verifyPhoneNumber(builder.build());
    }

    private void openHomePage() {
        // go to next actvity
        Intent i = new Intent(OtpPage.this, HomePagePatient.class);
        startActivity(i);
        finish();
    }
}
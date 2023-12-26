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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PhoneNumber extends AppCompatActivity {

    EditText number;
    Button ContinueOtp;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        number = findViewById(R.id.entermobileNumber);
        ContinueOtp = findViewById(R.id.sendotp);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String name = getIntent().getStringExtra("name");
        String mail = getIntent().getStringExtra("mail");
        String gender = getIntent().getStringExtra("gender");
        String dob = getIntent().getStringExtra("dob");
        int age = getIntent().getIntExtra("age", 0);
        String password = getIntent().getStringExtra("password");


        ContinueOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send OTP
                String unumber = number.getText().toString().trim();
                if(unumber.isEmpty()){
                    number.setError("Number cannot be empty");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(PhoneNumber.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PhoneNumber.this, "User Authenticated...", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(PhoneNumber.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                            Log.d("errorrrrrr", String.valueOf(task.getException()));
                        }
                    }
                });
                saveUserDataToFirestore(name, mail, gender, unumber, dob, password, age);
                Intent openOtpPage = new Intent(PhoneNumber.this, OtpPage.class);
                openOtpPage.putExtra("phone", "+91"+unumber);
                startActivity(openOtpPage);

            }
        });



    }


    private void saveUserDataToFirestore(String name, String mail, String gender, String unumber, String dob, String password, int age) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("mail", mail);
        user.put("gender", gender);
        user.put("age", age);
        user.put("phoneNumber", unumber);
        user.put("dob", dob);
        user.put("password", password);

        usersRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(PhoneNumber.this, "user Data saved Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PhoneNumber.this, "Error Saving to Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.android.hmh.docpal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

public class DoctorLogin extends AppCompatActivity {
    EditText enterdocusername, enterdocpass;
    Button submitButtonDoctor;
    private static final String DOC_PREF_NAME = "doc_prefs";
    private static final String DOC_KEY_NAME = "doc_name";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        enterdocpass = findViewById(R.id.enterdocpass);
        enterdocusername = findViewById(R.id.enterdocusername);
        submitButtonDoctor = findViewById(R.id.submitButtonDoctor);

        submitButtonDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docUsername = enterdocusername.getText().toString();
                String docPass = enterdocpass.getText().toString();

                saveUserInfo(docUsername);
                if(!docUsername.isEmpty() && !docPass.isEmpty()){
                    startService(docUsername);

                    Intent i = new Intent(DoctorLogin.this, HomePageDoctor.class);
                    startActivity(i);

                }
            }
        });
    }

    private void saveUserInfo(String docUsername) {
        SharedPreferences preferences = getSharedPreferences(DOC_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DOC_KEY_NAME, docUsername);
        editor.apply();
    }


    public static String getDoctorName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(DOC_PREF_NAME, MODE_PRIVATE);
        return preferences.getString(DOC_KEY_NAME, "");
    }

    private void startService(String userID) {
        Application application = getApplication() ; // Android's application context
        long appID = 55023617;   // yourAppID
        String appSign = "e06f4dc7104a44c637b0e76d1e5e16fdb31c01b9208cd49901375dd3b904de01";  // yourAppSign
        String userName = userID;
        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }
}
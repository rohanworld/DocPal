package com.android.hmh.docpal;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChaaatPage extends AppCompatActivity {
    public static final int STORAGE_PERMISSION_CODE = 1;
    public static final int PDF_REQUEST_CODE = 2;
    TextView doctorNameHereForNewChat;
    ImageButton btnSendMsg;
    ImageView attachFileBtn;
    EditText etMsg;

    ListView lvDiscussion;
    ArrayList<String> listConversation = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    String UserName, SelectedTopic, user_msg_key;

    private DatabaseReference dbr;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chaaat_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        doctorNameHereForNewChat = findViewById(R.id.doctorNameHereForNewChat);
        attachFileBtn = findViewById(R.id.attachFileBtn);
        UserName = Registration.getUserName(ChaaatPage.this);
        btnSendMsg = findViewById(R.id.btnSendMsg);
        etMsg = (EditText) findViewById(R.id.etMessage);

        String sendingMsgTo = getIntent().getStringExtra("doctorName");
        doctorNameHereForNewChat.setText(sendingMsgTo);

        lvDiscussion = (ListView) findViewById(R.id.lvConversation);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listConversation);
        lvDiscussion.setAdapter(arrayAdpt);
        SelectedTopic = "allNewMessages";
        dbr = FirebaseDatabase.getInstance().getReference().child(sendingMsgTo);

        attachFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePdfUpload();
            }
        });

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                user_msg_key = dbr.push().getKey();
                dbr.updateChildren(map);

                DatabaseReference dbr2 = dbr.child(user_msg_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("msgContent", etMsg.getText().toString());
                map2.put("sentBy", UserName);
                map2.put("sentTo", sendingMsgTo);
                dbr2.updateChildren(map2);

                etMsg.setText("");
            }
        });

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void handlePdfUpload() {
//        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//        }else{
//            openFilePicker();
//        }
//    }
//private void handlePdfUpload() {
//    if (ContextCompat.checkSelfPermission(ChaaatPage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//        ActivityCompat.requestPermissions(ChaaatPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//    } else {
//        openFilePicker();
//    }
//}

    private void handlePdfUpload() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            openFilePicker();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openFilePicker();
            }else {
                Toast.makeText(this, "Permission Required broo", Toast.LENGTH_SHORT).show();
            }
        }
    }


    ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode()==Activity.RESULT_OK){
            Uri pdfUri = result.getData().getData();
            uploadFileToFirebase(pdfUri);
        }
    });

    private void uploadFileToFirebase(Uri pdfUri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("pdfs/"+pdfUri.getLastPathSegment());
        storageReference.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String pdfDownloadUrl = uri.toString();
                        String urlMessage = "Your Report: "+pdfDownloadUrl;
                        etMsg.setText(urlMessage);
                        //set message
                        DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference().child("reports");
                        reportRef.setValue(pdfDownloadUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChaaatPage.this, "Failed to upload PDf", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFilePicker() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName()));
            startActivity(i);
            return;
        }

        Intent iPdf = new Intent(Intent.ACTION_GET_CONTENT);
        iPdf.setType("application/pdf");
        iPdf.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(iPdf);
    }




    //        @Override
        public void updateConversation(DataSnapshot dataSnapshot) {
            String msg, sentTo, sentBy;
            Iterator i = dataSnapshot.getChildren().iterator();
            while (i.hasNext()) {
                msg = (String) ((DataSnapshot) i.next()).getValue();
                sentBy = (String) ((DataSnapshot) i.next()).getValue();
                sentTo = (String) ((DataSnapshot) i.next()).getValue();

                String conversation;
                if (sentBy.equals(UserName)) {
                    // Message sent by the user
                    conversation = "You: \n" + msg + " \n";
                    addMessageToAdapter(conversation, R.layout.message_sent);
                } else {
                    // Message received from others
                    conversation = sentBy + ": \n" + msg + " \n";
                    addMessageToAdapter(conversation, R.layout.message_received);
                }
            }
        }

        private void addMessageToAdapter(String message, int layoutResId) {
            View view = getLayoutInflater().inflate(layoutResId, null);
            TextView textView;
            if (layoutResId == R.layout.message_sent) {
                textView = view.findViewById(R.id.textMessageSent);
            } else {
                textView = view.findViewById(R.id.textMessageReceived);
            }
            textView.setText(message);
            arrayAdpt.insert(message, arrayAdpt.getCount());
            arrayAdpt.notifyDataSetChanged();
        }


//    public void updateConversation(DataSnapshot dataSnapshot){
//        String msg, sentTo, sentBy, conversation;
//        Iterator i = dataSnapshot.getChildren().iterator();
//        while(i.hasNext()){
//            msg = (String) ((DataSnapshot)i.next()).getValue();
//            sentBy = (String) ((DataSnapshot)i.next()).getValue();
//            sentTo = (String) ((DataSnapshot)i.next()).getValue();
//
////            conversation = sentBy+" -> "+sentTo + ": \n" + msg+" \n";
//            conversation = "You -> "+sentTo + ": \n" + msg+" \n";
//            arrayAdpt.insert(conversation, arrayAdpt.getCount());
//            arrayAdpt.notifyDataSetChanged();
//        }
//    }
}



//public class ChatPage extends AppCompatActivity {
//    private static final String TAG = "ChatPage";
//    private static final String DB_NODE_MESSAGES = "messages";
//
//    private TextView doctorNameHereForNewChat;
//    private Button btnSendMsg;
//    private EditText etMsg;
//    private ListView lvDiscussion;
//    private ArrayList<String> listConversation = new ArrayList<>();
//    private ArrayAdapter<String> arrayAdpt;
//    private String userName, selectedTopic, userMsgKey;
//
//    private DatabaseReference dbRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat_page);
//
//        doctorNameHereForNewChat = findViewById(R.id.doctorNameHereForNewChat);
//        btnSendMsg = findViewById(R.id.btnSendMsg);
//        etMsg = findViewById(R.id.etMessage);
//        lvDiscussion = findViewById(R.id.lvConversation);
//
//        userName = Registration.getUserName(ChatPage.this);
//        String sendingMsgTo = getIntent().getStringExtra("doctorName");
//        doctorNameHereForNewChat.setText(sendingMsgTo);
//
//        arrayAdpt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listConversation);
//        lvDiscussion.setAdapter(arrayAdpt);
//
//        selectedTopic = DB_NODE_MESSAGES;
//        dbRef = FirebaseDatabase.getInstance().getReference().child(selectedTopic);
//
//        btnSendMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = etMsg.getText().toString().trim();
//                if (!TextUtils.isEmpty(message)) {
//                    Map<String, Object> messageMap = new HashMap<>();
//                    userMsgKey = dbRef.push().getKey();
//                    dbRef.updateChildren(messageMap);
//
//                    DatabaseReference messageRef = dbRef.child(userMsgKey);
//                    Map<String, Object> messageDetails = new HashMap<>();
//                    messageDetails.put("msgContent", message);
//                    messageDetails.put("sentBy", userName);
//                    messageDetails.put("sentTo", sendingMsgTo);
//                    messageRef.updateChildren(messageDetails);
//
//                    etMsg.setText("");
//                }
//            }
//        });
//
//        dbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                listConversation.clear();
//                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
//                    String msgContent = messageSnapshot.child("msgContent").getValue(String.class);
//                    String sentBy = messageSnapshot.child("sentBy").getValue(String.class);
//                    String sentTo = messageSnapshot.child("sentTo").getValue(String.class);
//
//                    String conversation = sentBy + " -> " + sentTo + ": \n" + msgContent;
//                    listConversation.add(conversation);
//                }
//                arrayAdpt.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "onCancelled: ", databaseError.toException());
//            }
//        });
//    }
//}
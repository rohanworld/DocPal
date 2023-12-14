package com.android.hmh.docpal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ChattingPage extends AppCompatActivity {

    ListView listofmessages;
    EditText userInputMessage;
    TextView doctorNameHere;
    FloatingActionButton sendMsgButton;
    FirebaseListAdapter<ChatMessage> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_page);
        listofmessages = findViewById(R.id.list_of_messages);
        userInputMessage = findViewById(R.id.userInputMessage);
        sendMsgButton = findViewById(R.id.sendMsgButton);
        String doctorrName = getIntent().getStringExtra("doctorName");
        doctorNameHere=findViewById(R.id.doctorNameHere);
        doctorNameHere.setText(doctorrName);

        FirebaseApp.initializeApp(this);


        displayChatMessages();
        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the current user is not null
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                     FirebaseDatabase.getInstance()
                            .getReference().child("allMessages")
                            .push()
                            .setValue(new ChatMessage(userInputMessage.getText().toString(),
                                    Registration.getUserName(ChattingPage.this))
                            );
                    // Clear the input
                    userInputMessage.setText("");
                    adapter.notifyDataSetChanged();
                    listofmessages.smoothScrollToPosition(adapter.getCount() - 1);

             }
        });


    }

    private void displayChatMessages() {

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("allMessages"), ChatMessage.class)
                .setLayout(R.layout.message)
                .build();


            adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
//                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View messageView = inflater.inflate(R.layout.message, null, true);

                Log.d("ChattingPage", "populateView called. Message: " + model.getMessageText());

                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text1);
                TextView messageUser = v.findViewById(R.id.message_user1);
                TextView messageTime = v.findViewById(R.id.message_time1);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
//                ((ViewGroup) v).addView(messageView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        };

        listofmessages.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        listofmessages.smoothScrollToPosition(adapter.getCount() - 1);


    }

     
}
package com.example.instashare.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instashare.Adapter.MessageAdapter;
import com.example.instashare.Model.Message;
import com.example.instashare.Model.User;
import com.example.instashare.R;
import com.example.instashare.Utils.FirebaseUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MessageActivity extends AppCompatActivity{
    private String cuid;
    private User cuser;
    private String idchatroom;
    private ImageButton btn_send, btn_back;
    private TextView input_message;
    private List<Message> list_message;
    private RecyclerView rcv_message;
    private MessageAdapter messageAdapter;
    private TextView tv_name;
    private String name;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_messages);

        cuser = getIntent().getParcelableExtra("user");
        idchatroom = getIntent().getStringExtra("idchatroom");
        name = getIntent().getStringExtra("name");

        cuid = cuser.getUid();
        input_message = findViewById(R.id.edt_message);
        btn_send = findViewById(R.id.btn_send);
        btn_back = findViewById(R.id.btnBack);
        tv_name = findViewById(R.id.tv_name);

        rcv_message = findViewById(R.id.rcv_message);
        list_message = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, cuser.getUid());
        messageAdapter.setData(list_message);

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcv_message.setLayoutManager(linearLayoutManager);

        setupListMessage();

        tv_name.setText(name);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = String.valueOf(input_message.getText());
                if(message.isEmpty() || message.length() < 1)
                    return;
                sendMessage(message);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, ListChatRoomsActivity.class);
                intent.putExtra("user", cuser);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                finish();
            }
        });
    }
    private void sendMessage(String in_message){
        Timestamp timesend = Timestamp.now();
        Message message = new Message(in_message, cuid, timesend, Uri.parse("null"));
        FirebaseUtils.Instance().sendMessage(idchatroom, message);
        input_message.setText("");
    }

    private void setupListMessage(){
        FirebaseUtils.Instance().getChats(idchatroom)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                list_message.clear();
                rcv_message.setAdapter(messageAdapter);
                if (e != null) {
                    return;
                }
                for (QueryDocumentSnapshot doc : value) {
                    Message message1= new Message(doc);
                    for (Message msg : list_message) {
                        if(msg.getUri().toString().equals(message1.getUri().toString())){
                            message1.setUri(Uri.parse("null"));
                            break;
                        }
                    }
                    list_message.add(message1);
                    rcv_message.setAdapter(messageAdapter);
                }
            }
        });
    }
}

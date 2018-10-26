package com.grechur.imdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    TextView tv_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        tv_message  = findViewById(R.id.tv_message);
        parseNotifyIntent(getIntent());
    }

    private void parseNotifyIntent(Intent intent){
        ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
        if(messages != null && messages.size() > 0 ){
            String msg = "";
            for (IMMessage message : messages) {
                msg += message.getFromAccount()+":"+message.getContent();
            }
            tv_message.setText(msg);
        }
    }
}

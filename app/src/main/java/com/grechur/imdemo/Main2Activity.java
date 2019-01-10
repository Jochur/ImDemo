package com.grechur.imdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;

import com.grechur.imdemo.view.AuthStatus;
import com.grechur.imdemo.view.NodeProgressView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    NodeProgressView npv_identification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        npv_identification = findViewById(R.id.npv_identification);
        ArrayList<AuthStatus> authList = new ArrayList<>();
        AuthStatus authStatus = new AuthStatus();
        authStatus.authNoticeInfo = "";
        authStatus.authSuccessImg = "";
        authStatus.authingImg = "";
        authStatus.dictCode = "6501";
        authStatus.dictName = "身份识别";
        authStatus.dictValue = 1;
        authStatus.gotoUrl = "";
        authStatus.noAuthImg = "";
        authStatus.status = 0;
        authList.add(authStatus);
        AuthStatus authStatus1 = new AuthStatus();
        authStatus1.authNoticeInfo = "";
        authStatus1.authSuccessImg = "";
        authStatus1.authingImg = "";
        authStatus1.dictCode = "6503";
        authStatus1.dictName = "刷脸";
        authStatus1.dictValue = 1;
        authStatus1.gotoUrl = "";
        authStatus1.noAuthImg = "";
        authStatus1.status = 0;
        authList.add(authStatus1);
        npv_identification.setTitles(authList);
        npv_identification.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                npv_identification.setProgressByNode(10);
                npv_identification.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}

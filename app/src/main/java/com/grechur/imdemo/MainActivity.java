package com.grechur.imdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.grechur.imdemo.auth.CrawlerTaobaoActivity;
import com.grechur.imdemo.utils.Preferences;
import com.grechur.imdemo.utils.YunxinCache;
import com.grechur.imdemo.utils.state.UserPreferences;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.BroadcastMessage;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.settings.SettingsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText et_enter_name;
    EditText et_to_name;
    TextView tv_message;
    String msg = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_enter_name = findViewById(R.id.et_enter_name);
        et_to_name = findViewById(R.id.et_to_name);
        tv_message = findViewById(R.id.tv_message);



    }
    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("out", kickOut);
        context.startActivity(intent);
    }

    /**
     * 登陆
     * @param view
     */
    public void login(View view) {
        final LoginInfo info = new LoginInfo(et_enter_name.getText().toString(), "111111"); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        Preferences.setUserAccount(param.getAccount());
                        Preferences.setUserToken(param.getToken());
                        YunxinCache.setAccount(param.getAccount());
                        // 加载状态栏配置
                        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
                        if (statusBarNotificationConfig == null) {
                            statusBarNotificationConfig = YunxinCache.getNotificationConfig();
                            UserPreferences.setStatusConfig(statusBarNotificationConfig);
                        }
                        // 更新配置
                        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
                        startActivity(new Intent(MainActivity.this,ListActivity.class));
                    }

                    @Override
                    public void onFailed(int code) {
                        Toast.makeText(getApplicationContext(), "登录失败"+code, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onException(Throwable exception) {

                    }

                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);

    }

    /**
     * 发送消息
     * @param view
     */
    public void send(View view){
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        String text = "this is an example";
        // 创建一个文本消息
        IMMessage textMessage = MessageBuilder.createTextMessage(et_to_name.getText().toString(), sessionType, text);
        textMessage.setPushContent(text);
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(textMessage, false);
    }


    public void sendNotification(View view){
        // 构造自定义通知，指定接收者
        CustomNotification notification = new CustomNotification();
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        notification.setSessionId(et_to_name.getText().toString());
        notification.setSessionType(sessionType);

// 构建通知的具体内容。为了可扩展性，这里采用 json 格式，以 "id" 作为类型区分。
// 这里以类型 “1” 作为“正在输入”的状态通知。
        JSONObject json = new JSONObject();
        json.put("id", "2");
//        notification.setContent(json.toString());
        JSONObject data = new JSONObject();
        data.put("body", "the_content_for_display");
        data.put("url", "url_of_the_game_or_anything_else");
        json.put("data", data);
        notification.setContent(json.toString());

// 设置该消息需要保证送达
        notification.setSendToOnlineUserOnly(false);

// 设置 APNS 的推送文本
        notification.setApnsText("the_content_for_apns");

// 自定义推送属性
        Map<String,Object> pushPayload = new HashMap<>();
        pushPayload.put("key1", "payload 1");
        pushPayload.put("key2", 2015);
        notification.setPushPayload(pushPayload);


// 发送自定义通知
        NIMClient.getService(MsgService.class).sendCustomNotification(notification);
    }


    public void taobao(View view){
        Random random = new Random();
        int position = random.nextInt(6);
//        int position = 1;
        startActivity(new Intent(this, CrawlerTaobaoActivity.class).putExtra("position",position));
    }

}

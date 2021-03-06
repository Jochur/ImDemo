package com.grechur.imdemo;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.fastjson.JSONObject;
import com.grechur.imdemo.auth.CrawlerTaobaoActivity;
import com.grechur.imdemo.bean.AppBean;
import com.grechur.imdemo.utils.Base64;
import com.grechur.imdemo.utils.Preferences;
import com.grechur.imdemo.utils.YunxinCache;
import com.grechur.imdemo.utils.glide.GlideCircleTransform;
import com.grechur.imdemo.utils.glide.GlideImageLoaderStrategy;
import com.grechur.imdemo.utils.glide.GlideImageConfigImpl;
import com.grechur.imdemo.utils.glide.ImageLoader;
import com.grechur.imdemo.utils.glide.PicassoImageConfigImpl;
import com.grechur.imdemo.utils.glide.PicassoImageLoaderStrategy;
import com.grechur.imdemo.utils.state.UserPreferences;
import com.grechur.imdemo.view.MyTextSwitcher;
import com.grechur.imdemo.view.TextSwitchManager;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.widget.SearchView;
import android.widget.ViewSwitcher;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText et_enter_name;
    EditText et_to_name;
    TextView tv_message;
    EditText et_input;
    MyTextSwitcher mTextSwitcher;
    LottieAnimationView load_animation_view;

//    SearchView search;
    LoadingView loading;
    String msg = "";
    private SearchView searchView;
    ImageView iv_image;
    private String imgurl = "https://www.baidu.com/img/bd_logo1.png";
    private ImageView iv_image2;


    String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    private SoundPool mSoundPool;

    //文字垂直滚动
    private int index = 0;//textview上下滚动下标
    private boolean isFlipping = false; // 是否启用预警信息轮播
    private List<String> mWarningTextList = new ArrayList<>();
    TextSwitchManager textSwitchManager;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.nav_icon_back_nor));
        et_enter_name = findViewById(R.id.et_enter_name);
        et_to_name = findViewById(R.id.et_to_name);
        tv_message = findViewById(R.id.tv_message);
        et_input = findViewById(R.id.et_input);
//        search = findViewById(R.id.search);
        loading = findViewById(R.id.loading);
        mTextSwitcher = findViewById(R.id.text_switcher);
        load_animation_view = findViewById(R.id.load_animation_view);
//        String url = "bsdlks://polymerShopCar/bsdlks://polymerShopCar/h5-dev.xiaoxiangyoupin.com/polymerShopCar/";
//        Uri mParse = Uri.parse(url);
//        Toast.makeText(this,mParse.toString(),Toast.LENGTH_SHORT).show();


//        String cookie = "sdk_param=abtest%3Anouse%2Cmcid%3Atunion4android%40866952031696243%2Ccid%3Atunion4android%40866952031696243_1541557007663; cna=hm5oFEnYtgkCAYzPKEKu+ola; t=a474de465630c28f170067829b78e1d5; cookie2=19f0f854d4369a616407cf379a0961ea; v=0; _tb_token_=fa4f5345a5e35; ockeqeudmj=l%2BP6m2o%3D; munb=1035552794; WAPFDFDTGFG=%2B4cMKKP%2B8PI%2BuiiRalXO60DzEqDxdg%3D%3D; _w_app_lg=19; unb=1035552794; sg=64c; _l_g_=Ug%3D%3D; skt=e5d7226d9fbec9aa; uc1=cookie21=W5iHLLyFe3xm&cookie15=UtASsssmOIJ0bQ%3D%3D&cookie14=UoTYN4TRKQea7w%3D%3D; cookie1=ACOwjgR2YClwcAfB4teTwuhK1ytavZyKlGg6I99TrIw%3D; csg=b03b183d; uc3=vt3=F8dByR%2FL7ZNHkWPDiBg%3D&id2=UoH8WASWWSZ2%2Bw%3D%3D&nk2=2nQiTLnEXW4%3D&lg2=UIHiLt3xD8xYTw%3D%3D; tracknick=%5Cu679C%5Cu679C%5Cu580266; lgc=%5Cu679C%5Cu679C%5Cu580266; _cc_=VFC%2FuZ9ajQ%3D%3D; dnk=%5Cu679C%5Cu679C%5Cu580266; _nk_=%5Cu679C%5Cu679C%5Cu580266; cookie17=UoH8WASWWSZ2%2Bw%3D%3D; ntm=0; isg=BL6-x1O3aYsP4b2v06hzzv1IBNbAV6q-l6QgVmjHLoH8C1rlxY8qiLvqh5FlfnqR; _m_h5_tk=4284fb7443a128c0c6fc7dcd41de2a0c_1541567642159; _m_h5_tk_enc=b7f86a3e87229856c1ff61db69e25774";
//        String c64 = Base64.encode(cookie.getBytes());
//        Log.e("base64",c64);
        iv_image = findViewById(R.id.iv_image);
        iv_image2 = findViewById(R.id.iv_image2);
//        Glide.with(this).load(imgurl).into(iv_image);
//        search.startSearch();
//.transformation(new CropCircleTransformation(this))
        ImageLoader.getInstance().getService(GlideImageLoaderStrategy.class).loadImage(this,
            GlideImageConfigImpl.builder()
                .url(imgurl)
                .hasCache(true)
                .transformation(new CropCircleTransformation(this))
                .placeholder(R.drawable.message_plus_snapchat_normal)
                .imageView(iv_image)
                .build()
        );

//        ImageLoader.getInstance().getService(PicassoImageLoaderStrategy.class).loadImage(this,
//                PicassoImageConfigImpl.builder()
//                        .url(imgurl)
//                        .hasCache(true)
//                        .placeholder(R.drawable.message_plus_snapchat_normal)
////                        .transformation(new BlurTransformation(this))
//                        .imageView(iv_image2)
//                        .build()
//        );
        ActivityCompat.requestPermissions(this,permissions,1000);

        final long USAGE_STATS_PERIOD = 1000 * 60 * 60 * 24 * 1;
        long now = System.currentTimeMillis();
        long beginTime = now - USAGE_STATS_PERIOD;

        UsageStatsManager manager=(UsageStatsManager)getApplicationContext().getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> stats=manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,beginTime,now);
        if(stats==null||stats.size()==0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            List<AppBean> appBeans = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (UsageStats us : stats) {
                try {
                    PackageManager pm = getApplicationContext().getPackageManager();
                    ApplicationInfo applicationInfo = pm.getApplicationInfo(us.getPackageName(), PackageManager.GET_META_DATA);
                    if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) <= 0) {
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        String t = format.format(new Date(us.getLastTimeUsed()));
                        if (us.getTotalTimeInForeground() > 0) {
                            AppBean appBean = new AppBean();
                            appBean.icon=pm.getApplicationIcon(applicationInfo);
                            appBean.logo=pm.getApplicationLogo(applicationInfo);
                            appBean.label=pm.getApplicationLabel(applicationInfo).toString();
                            appBean.time = us.getTotalTimeInForeground();
                            appBeans.add(appBean);
                            sb.append(pm.getApplicationLabel(applicationInfo) + "\t" + t + "\t" + getString(us.getTotalTimeInForeground()) + "\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            tv_message.setText(sb.toString());
            AppBean bean = Collections.max(appBeans, new Comparator<AppBean>() {
                @Override
                public int compare(AppBean o1, AppBean o2) {
                    return (o1.time < o2.time ? -1 : (o1.time == o2.time? 0 : 1));
                }
            });
            iv_image2.setImageDrawable(bean.icon);

        }

        createSoundPoolIfNeeded();
        mWarningTextList.add("签到");
        mWarningTextList.add("+20");
        textSwitchManager = new TextSwitchManager(this,mTextSwitcher,handler);
        textSwitchManager.setData(mWarningTextList);

        load_animation_view.setImageAssetsFolder("lottieimg/");
        load_animation_view.setAnimation("data.json");
        load_animation_view.loop(true);
        load_animation_view.playAnimation();
    }
    /**
     * 创建SoundPool ，注意 api 等级
     */
    private void createSoundPoolIfNeeded(){
        if (mSoundPool == null) {
            // 5.0 及 之后
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = null;
                audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();

                mSoundPool = new SoundPool.Builder()
                        .setMaxStreams(1)
                        .setAudioAttributes(audioAttributes)
                        .build();
            } else { // 5.0 以前
                mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);  // 创建SoundPool
            }
        }
    }
    private String getString(long totalTimeInForeground) {
        String time = "";
        long temp = totalTimeInForeground/1000;
        int hour = (int) (temp/3600);
        int second = (int) ((temp%3600)/60);
        int mill = (int) ((temp%3600)%60);
        time = hour+"小时"+second+"分"+mill+"秒";
        return time;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private static Handler handler = new Handler();
    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.global_search_menu, menu);
//        final MenuItem item = menu.findItem(R.id.action_search);
//        getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                MenuItemCompat.expandActionView(item);
//            }
//        });
//        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                finish();
//                return false;
//            }
//        });
//
//        searchView = (SearchView) MenuItemCompat.getActionView(item);
//        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
//        textView.setTextColor(Color.RED);
//        textView.setHintTextColor(Color.BLUE);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
//
//        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String text) {
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String text) {
//                return true;
//            }
//        });
//        return true;
//    }

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
//        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
//        String text = "this is an example";
//        // 创建一个文本消息
//        IMMessage textMessage = MessageBuilder.createTextMessage(et_to_name.getText().toString(), sessionType, text);
//        textMessage.setPushContent(text);
//        // 发送给对方
//        NIMClient.getService(MsgService.class).sendMessage(textMessage, false);
        NimUIKitImpl.startP2PSession(this,et_to_name.getText().toString());
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


    public void toPlate(View view){
//        Random random = new Random();
//        int position = random.nextInt(6);
        String input =et_input.getText().toString();
        int position = Integer.valueOf(input);
        startActivity(new Intent(this, CrawlerTaobaoActivity.class).putExtra("position",position));
    }

    public void startAnimal(View v){
        loading.startAnimal();
        if(mSoundPool != null){
            int id = mSoundPool.load(this,R.raw.push,5);
            mSoundPool.play(id,1, 1, 0,0,1);
        }
        mWarningTextList.add("genxin");
        textSwitchManager.setData(mWarningTextList);
//        startActivity(new Intent(this,Main2Activity.class));
//        picFragment = PickPicFragment.getInstance();
//        picFragment.creatPickPicDialog(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        textSwitchManager.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        textSwitchManager.stopFlipping();
    }
}

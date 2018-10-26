package com.grechur.imdemo;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.grechur.imdemo.utils.Preferences;
import com.grechur.imdemo.utils.PrivatizationConfig;
import com.grechur.imdemo.utils.YunxinCache;
import com.grechur.imdemo.utils.local.NimDemoLocationProvider;
import com.grechur.imdemo.utils.state.OnlineStateEventManager;
import com.grechur.imdemo.utils.state.SelfOnlineStateContentProvider;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.wrapper.NimUserInfoProvider;
import com.grechur.imdemo.utils.chat.session.SessionHelper;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.ServerAddresses;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.MixPushConfig;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.BroadcastMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;


public class DemoApplication extends Application{
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        YunxinCache.setContext(this);
        Preferences.init(this);
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), options());

        // ... your codes
        if (NIMUtil.isMainProcess(this)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            NimUIKit.init(this);
            // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
            NimUIKit.setLocationProvider(new NimDemoLocationProvider());
            // IM 会话窗口的定制初始化。
            SessionHelper.init();
            // 2、相关Service调用
            registerNimBroadcastMessage(true);
            NIMClient.getService(MixPushService.class).enable(true).setCallback(new RequestCallback<Void>(){

                @Override
                public void onSuccess(Void aVoid) {
                    LogUtil.e("MixPushService onSuccess"," onSuccess");
                }

                @Override
                public void onFailed(int i) {
                    LogUtil.e("MixPushService onFailed",i+"");
                }

                @Override
                public void onException(Throwable throwable) {
                    LogUtil.e("MixPushService onFailed",throwable.getMessage());
                }
            });

            NimUIKit.setOnlineStateContentProvider(new SelfOnlineStateContentProvider());
            // 初始化在线状态事件
            OnlineStateEventManager.init();
//            NIMClient.getService(SettingsService.class)
//                    .updateMultiportPushConfig(true)
//                    .setCallback(new RequestCallback<Void>() {
//
//                        @Override
//                        public void onSuccess(Void param) {
//                            Toast.makeText(getApplicationContext(), "SettingsService", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onFailed(int code) {
//                            Toast.makeText(getApplicationContext(), "onFailed"+code, Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onException(Throwable exception) {
//                            Toast.makeText(getApplicationContext(), "onException"+exception.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
        }
    }



    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = ListActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.drawable.ic_stat_notify_msg;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;



        // 通知栏显示用户昵称和头像
        options.userInfoProvider = new NimUserInfoProvider(YunxinCache.getContext());
        // 采用异步加载SDK
        options.asyncInitSDK = true;
        // 打开消息撤回未读数-1的开关
        options.shouldConsiderRevokedMessageUnreadCount = true;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用采用默认路径作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName()  + "/nim"; // 可以不设置，那么将采用默认路径
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = 1080;;
        options.mixPushConfig = buildMixPushConfig();
        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(SessionTypeEnum sessionType, String sessionId) {
                return null;
            }
        };
        // 云信私有化配置项
//        configServerAddress(options);
        return options;
    }


    private static void configServerAddress(final SDKOptions options) {
        String appKey = PrivatizationConfig.getAppKey();
        if (!TextUtils.isEmpty(appKey)) {
            options.appKey = appKey;
        }

        ServerAddresses serverConfig = PrivatizationConfig.getServerAddresses();
        if (serverConfig != null) {
            options.serverConfig = serverConfig;
        }
    }

    private static MixPushConfig buildMixPushConfig() {

        // 第三方推送配置
        MixPushConfig config = new MixPushConfig();

        // 华为推送
        config.hwCertificateName = "HWPUSH";

        return config;
    }
    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        // 从本地读取上次登录成功时保存的用户登录信息
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    /**
     * 注册云信全服广播接收器
     *
     * @param register
     */
    private void registerNimBroadcastMessage(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeBroadcastMessage(new Observer<BroadcastMessage>() {
            @Override
            public void onEvent(BroadcastMessage broadcastMessage) {
                // 处理
                String content = broadcastMessage.getContent();
                Toast.makeText(DemoApplication.mContext,content,Toast.LENGTH_SHORT).show();
            }
        }, register);
    }

    public static Context getContext() {
        return mContext;
    }
}

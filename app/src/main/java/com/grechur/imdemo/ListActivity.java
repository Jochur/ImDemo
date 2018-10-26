package com.grechur.imdemo;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.grechur.imdemo.utils.ReminderManager;
import com.grechur.imdemo.utils.chat.attachment.StickerAttachment;
import com.grechur.imdemo.utils.chat.attachment.GuessAttachment;
import com.grechur.imdemo.utils.chat.attachment.SnapChatAttachment;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.session.module.MsgRevokeFilter;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListActivity extends UI {
    private RecentContactsFragment fragment;
    // 同时在线的其他端的信息
    private List<OnlineClient> onlineClients;
    TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        status = new TextView(this);
        //注册监听
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
        // 开启/关闭通知栏消息提醒   在当前页面不提示
        NIMClient.toggleNotification(false);
        addRecentContactsFragment();
    }

    public void addRecentContactsFragment(){
        fragment = new RecentContactsFragment();
        fragment.setContainerId(R.id.messages_fragment);
// 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
        fragment = (RecentContactsFragment) addFragment(fragment);

        fragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {
                // 最近联系人列表加载完毕
            }

            @Override
            public void onUnreadCountChange(int unreadCount) {
                ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
            }

            @Override
            public void onItemClick(RecentContact recent) {
                // 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
                switch (recent.getSessionType()) {
                    case P2P:
                        startP2PSession(ListActivity.this, recent.getContactId());
                        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
                            @Override
                            public boolean shouldIgnore(IMMessage message) {
                                return false;
                            }
                        });
                        NimUIKit.setMsgRevokeFilter(new MsgRevokeFilter() {
                            @Override
                            public boolean shouldIgnore(IMMessage message) {
                                return false;
                            }
                        });
                        break;

                    default:
                        break;
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                // 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
                // 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
                if (attachment instanceof GuessAttachment) {
                    GuessAttachment guess = (GuessAttachment) attachment;
                    return guess.getValue().getDesc();
                } else if (attachment instanceof StickerAttachment) {
                    return "[贴图]";
                } else if (attachment instanceof SnapChatAttachment) {
                    return "[阅后即焚]";
                }
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                String msgId = recent.getRecentMessageId();
                List<String> uuids = new ArrayList<>(1);
                uuids.add(msgId);
                List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (msgs != null && !msgs.isEmpty()) {
                    IMMessage msg = msgs.get(0);
                    Map<String, Object> content = msg.getRemoteExtension();
                    if (content != null && !content.isEmpty()) {
                        return (String) content.get("content");
                    }
                }

                return null;
            }
        });
    }

    public static void startP2PSession(Context context, String account) {
        startP2PSession(context, account, null);
    }

    public static void startP2PSession(Context context, String account, IMMessage anchor) {
        if (!NimUIKit.getAccount().equals(account)) {
            NimUIKit.startP2PSession(context, account, anchor);
        }
    }

    @Override
    protected void onResume() {
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);

        super.onResume();
    }

    //消息监听
    Observer<List<IMMessage>> incomingMessageObserver =
            new Observer<List<IMMessage>>() {
                @Override
                public void onEvent(List<IMMessage> messages) {
                    // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。

                    for (IMMessage message : messages) {
                        message.isRemoteRead();
                    }
                }
            };

    Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
        @Override
        public void onEvent(List<OnlineClient> onlineClients) {
            ListActivity.this.onlineClients = onlineClients;
            if (onlineClients == null || onlineClients.size() == 0) {
            } else {

                OnlineClient client = onlineClients.get(0);
                switch (client.getClientType()) {
                    case ClientType.Windows:
                    case ClientType.MAC:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.computer_version));
                        break;
                    case ClientType.Web:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.web_version));
                        break;
                    case ClientType.iOS:
                    case ClientType.Android:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.mobile_version));
                        break;
                    default:
                        break;
                }
            }
        }
    };
    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
//                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {
//                    notifyBar.setVisibility(View.VISIBLE);
//                    notifyBarText.setText(R.string.net_broken);
                } else if (code == StatusCode.UNLOGIN) {
//                    notifyBar.setVisibility(View.VISIBLE);
//                    notifyBarText.setText(R.string.nim_status_unlogin);
                } else if (code == StatusCode.CONNECTING) {
//                    notifyBar.setVisibility(View.VISIBLE);
//                    notifyBarText.setText(R.string.nim_status_connecting);
                } else if (code == StatusCode.LOGINING) {
//                    notifyBar.setVisibility(View.VISIBLE);
//                    notifyBarText.setText(R.string.nim_status_logining);
                } else {
//                    notifyBar.setVisibility(View.GONE);
                }
            }
        }
    };
    @Override
    protected void onPause() {
        // 退出聊天界面或离开最近联系人列表界面，建议放在onPause中
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //注销监听
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);
        // 开启/关闭通知栏消息提醒  离开聊天页提示消息
        NIMClient.toggleNotification(true);
        super.onDestroy();

    }
}

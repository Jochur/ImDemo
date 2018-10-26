package com.grechur.imdemo.utils.chat.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.grechur.imdemo.R;
import com.grechur.imdemo.utils.YunxinCache;
import com.grechur.imdemo.utils.chat.attachment.StickerAttachment;
import com.grechur.imdemo.utils.chat.attachment.CustomAttachParser;
import com.grechur.imdemo.utils.chat.attachment.CustomAttachment;
import com.grechur.imdemo.utils.chat.attachment.GuessAction;
import com.grechur.imdemo.utils.chat.attachment.GuessAttachment;
import com.grechur.imdemo.utils.chat.attachment.SnapChatAction;
import com.grechur.imdemo.utils.chat.attachment.SnapChatAttachment;
import com.grechur.imdemo.utils.chat.viewholder.MsgViewHolderDefCustom;
import com.grechur.imdemo.utils.chat.viewholder.MsgViewHolderGuess;
import com.grechur.imdemo.utils.chat.viewholder.MsgViewHolderSnapChat;
import com.grechur.imdemo.utils.chat.viewholder.MsgViewHolderSticker;
import com.grechur.imdemo.utils.chat.viewholder.MsgViewHolderTip;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.recent.RecentCustomization;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.api.wrapper.NimMessageRevokeObserver;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.helper.MessageListPanelHelper;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.session.module.MsgRevokeFilter;
import com.netease.nim.uikit.business.team.model.TeamExtras;
import com.netease.nim.uikit.business.team.model.TeamRequestCode;
import com.grechur.imdemo.utils.chat.session.MessageHistoryActivity;
import com.grechur.imdemo.utils.chat.session.SearchMessageActivity;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.popupmenu.NIMPopupMenu;
import com.netease.nim.uikit.common.ui.popupmenu.PopupMenuItem;
import com.netease.nim.uikit.impl.customization.DefaultRecentCustomization;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.LocalAntiSpamResult;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;

import java.util.ArrayList;
import java.util.List;

/**
 * UIKit自定义消息界面用法展示类
 */
public class SessionHelper {
    private static final int ACTION_HISTORY_QUERY = 0;
    private static final int ACTION_SEARCH_MESSAGE = 1;
    private static final int ACTION_CLEAR_MESSAGE = 2;

    private static SessionCustomization p2pCustomization;
    private static SessionCustomization normalTeamCustomization;
    private static SessionCustomization advancedTeamCustomization;
    private static SessionCustomization myP2pCustomization;
    private static SessionCustomization robotCustomization;
    private static RecentCustomization recentCustomization;

    private static NIMPopupMenu popupMenu;
    private static List<PopupMenuItem> menuItemList;

    public static final boolean USE_LOCAL_ANTISPAM = true;


    public static void init() {
        // 注册自定义消息附件解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());

        // 注册各种扩展消息类型的显示ViewHolder
        registerViewHolders();

        // 设置会话中点击事件响应处理
        setSessionListener();

        // 注册消息转发过滤器
        registerMsgForwardFilter();

        // 注册消息撤回过滤器
        registerMsgRevokeFilter();

        // 注册消息撤回监听器
        registerMsgRevokeObserver();

        NimUIKit.setCommonP2PSessionCustomization(getP2pCustomization());

        NimUIKit.setRecentCustomization(getRecentCustomization());
    }

    public static void startP2PSession(Context context, String account) {
        startP2PSession(context, account, null);
    }

    public static void startP2PSession(Context context, String account, IMMessage anchor) {
        if (!YunxinCache.getAccount().equals(account)) {
            NimUIKit.startP2PSession(context, account, anchor);
        } else {
            NimUIKit.startChatting(context, account, SessionTypeEnum.P2P, getMyP2pCustomization(), anchor);
        }
    }

    // 定制化单聊界面。如果使用默认界面，返回null即可
    private static SessionCustomization getP2pCustomization() {
        if (p2pCustomization == null) {
            p2pCustomization = new SessionCustomization() {
                // 由于需要Activity Result， 所以重载该函数。
                @Override
                public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(activity, requestCode, resultCode, data);

                }

                @Override
                public boolean isAllowSendMessage(IMMessage message) {
                    return checkLocalAntiSpam(message);
                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            // 背景
//            p2pCustomization.backgroundColor = Color.BLUE;
//            p2pCustomization.backgroundUri = "file:///android_asset/xx/bk.jpg";
//            p2pCustomization.backgroundUri = "file:///sdcard/Pictures/bk.png";
//            p2pCustomization.backgroundUri = "android.resource://com.netease.nim.demo/drawable/bk"

            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
            actions.add(new SnapChatAction());
            actions.add(new GuessAction());
            p2pCustomization.actions = actions;
            p2pCustomization.withSticker = true;

            // 定制ActionBar右边的按钮，可以加多个
            ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
            SessionCustomization.OptionsButton cloudMsgButton = new SessionCustomization.OptionsButton() {
                @Override
                public void onClick(Context context, View view, String sessionId) {
                    initPopuptWindow(context, view, sessionId, SessionTypeEnum.P2P);
                }
            };
            cloudMsgButton.iconId = R.drawable.nim_ic_messge_history;

//            SessionCustomization.OptionsButton infoButton = new SessionCustomization.OptionsButton() {
//                @Override
//                public void onClick(Context context, View view, String sessionId) {
//
//                    MessageInfoActivity.startActivity(context, sessionId); //打开聊天信息
//                }
//            };


//            infoButton.iconId = R.drawable.nim_ic_message_actionbar_p2p_add;

            buttons.add(cloudMsgButton);
//            buttons.add(infoButton);
            p2pCustomization.buttons = buttons;
        }

        return p2pCustomization;
    }

    private static SessionCustomization getMyP2pCustomization() {
        if (myP2pCustomization == null) {
            myP2pCustomization = new SessionCustomization() {
                // 由于需要Activity Result， 所以重载该函数。
                @Override
                public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                    if (requestCode == TeamRequestCode.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                        String result = data.getStringExtra(TeamExtras.RESULT_EXTRA_REASON);
                        if (result == null) {
                            return;
                        }

                    }
                }

                @Override
                public boolean isAllowSendMessage(IMMessage message) {
                    return checkLocalAntiSpam(message);
                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
            actions.add(new SnapChatAction());
            actions.add(new GuessAction());
            myP2pCustomization.actions = actions;
            myP2pCustomization.withSticker = true;
            // 定制ActionBar右边的按钮，可以加多个
            ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
            SessionCustomization.OptionsButton cloudMsgButton = new SessionCustomization.OptionsButton() {
                @Override
                public void onClick(Context context, View view, String sessionId) {
                    initPopuptWindow(context, view, sessionId, SessionTypeEnum.P2P);
                }
            };

            cloudMsgButton.iconId = R.drawable.nim_ic_messge_history;

            buttons.add(cloudMsgButton);
            myP2pCustomization.buttons = buttons;
        }
        return myP2pCustomization;
    }

    private static boolean checkLocalAntiSpam(IMMessage message) {
        if (!USE_LOCAL_ANTISPAM) {
            return true;
        }
        LocalAntiSpamResult result = NIMClient.getService(MsgService.class).checkLocalAntiSpam(message.getContent(), "**");
        int operator = result == null ? 0 : result.getOperator();

        switch (operator) {
            case 1: // 替换，允许发送
                message.setContent(result.getContent());
                return true;
            case 2: // 拦截，不允许发送
                return false;
            case 3: // 允许发送，交给服务器
                message.setClientAntiSpam(true);
                return true;
            case 0:
            default:
                break;
        }

        return true;
    }


    private static RecentCustomization getRecentCustomization() {
        if (recentCustomization == null) {
            recentCustomization = new DefaultRecentCustomization() {
                @Override
                public String getDefaultDigest(RecentContact recent) {

                    return super.getDefaultDigest(recent);
                }
            };
        }

        return recentCustomization;
    }


    private static void registerViewHolders() {
        NimUIKit.registerMsgItemViewHolder(GuessAttachment.class, MsgViewHolderGuess.class);
        NimUIKit.registerMsgItemViewHolder(CustomAttachment.class, MsgViewHolderDefCustom.class);
        NimUIKit.registerMsgItemViewHolder(StickerAttachment.class, MsgViewHolderSticker.class);
        NimUIKit.registerMsgItemViewHolder(SnapChatAttachment.class, MsgViewHolderSnapChat.class);
        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
    }

    private static void setSessionListener() {
        SessionEventListener listener = new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                // 一般用于打开用户资料页面

//                UserProfileActivity.start(context, message.getFromAccount());
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
            }

            @Override
            public void onAckMsgClicked(Context context, IMMessage message) {
                // 已读回执事件处理，用于群组的已读回执事件的响应，弹出消息已读详情
//                AckMsgInfoActivity.start(context, message);
            }
        };

        NimUIKit.setSessionListener(listener);
    }


    /**
     * 消息转发过滤器
     */
    private static void registerMsgForwardFilter() {
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getMsgType() == MsgTypeEnum.custom && message.getAttachment() != null
                        && (message.getAttachment() instanceof SnapChatAttachment)) {
                    // 白板消息和阅后即焚消息，红包消息 不允许转发
                    return true;
                } else if (message.getMsgType() == MsgTypeEnum.robot && message.getAttachment() != null && ((RobotAttachment) message.getAttachment()).isRobotSend()) {
                    return true; // 如果是机器人发送的消息 不支持转发
                }
                return false;
            }
        });
    }

    /**
     * 消息撤回过滤器
     */
    private static void registerMsgRevokeFilter() {
        NimUIKit.setMsgRevokeFilter(new MsgRevokeFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (YunxinCache.getAccount().equals(message.getSessionId())) {
                    // 发给我的电脑 不允许撤回
                    return true;
                }
                return false;
            }
        });
    }

    private static void registerMsgRevokeObserver() {
        NIMClient.getService(MsgServiceObserve.class).observeRevokeMessage(new NimMessageRevokeObserver(), true);
    }


    private static void initPopuptWindow(Context context, View view, String sessionId, SessionTypeEnum sessionTypeEnum) {
        if (popupMenu == null) {
            menuItemList = new ArrayList<>();
            popupMenu = new NIMPopupMenu(context, menuItemList, listener);
        }
        menuItemList.clear();
        menuItemList.addAll(getMoreMenuItems(context, sessionId, sessionTypeEnum));
        popupMenu.notifyData();
        popupMenu.show(view);
    }

    private static NIMPopupMenu.MenuItemClickListener listener = new NIMPopupMenu.MenuItemClickListener() {
        @Override
        public void onItemClick(final PopupMenuItem item) {
            switch (item.getTag()) {
                case ACTION_HISTORY_QUERY:
                    MessageHistoryActivity.start(item.getContext(), item.getSessionId(), item.getSessionTypeEnum()); // 漫游消息查询
                    break;
                case ACTION_SEARCH_MESSAGE:
                    SearchMessageActivity.start(item.getContext(), item.getSessionId(), item.getSessionTypeEnum());
                    break;
                case ACTION_CLEAR_MESSAGE:
                    EasyAlertDialogHelper.createOkCancelDiolag(item.getContext(), null, "确定要清空吗？", true, new EasyAlertDialogHelper.OnDialogActionListener() {
                        @Override
                        public void doCancelAction() {

                        }

                        @Override
                        public void doOkAction() {
                            NIMClient.getService(MsgService.class).clearChattingHistory(item.getSessionId(), item.getSessionTypeEnum());
                            MessageListPanelHelper.getInstance().notifyClearMessages(item.getSessionId());
                        }
                    }).show();
                    break;
            }
        }
    };

    private static List<PopupMenuItem> getMoreMenuItems(Context context, String sessionId, SessionTypeEnum sessionTypeEnum) {
        List<PopupMenuItem> moreMenuItems = new ArrayList<PopupMenuItem>();
        moreMenuItems.add(new PopupMenuItem(context, ACTION_HISTORY_QUERY, sessionId,
                sessionTypeEnum, YunxinCache.getContext().getString(R.string.message_history_query)));
        moreMenuItems.add(new PopupMenuItem(context, ACTION_SEARCH_MESSAGE, sessionId,
                sessionTypeEnum, YunxinCache.getContext().getString(R.string.message_search_title)));
        moreMenuItems.add(new PopupMenuItem(context, ACTION_CLEAR_MESSAGE, sessionId,
                sessionTypeEnum, YunxinCache.getContext().getString(R.string.message_clear)));
        return moreMenuItems;
    }
}

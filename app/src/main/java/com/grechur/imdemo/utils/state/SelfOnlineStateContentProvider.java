package com.grechur.imdemo.utils.state;

import android.text.TextUtils;

import com.grechur.imdemo.utils.YunxinCache;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.main.OnlineStateContentProvider;

public class SelfOnlineStateContentProvider implements OnlineStateContentProvider {
    @Override
    public String getSimpleDisplay(String account) {
        String content = getDisplayContent(account, true);
        if (!TextUtils.isEmpty(content)) {
            content = "[" + content + "]";
        }
        return content;
    }

    @Override
    public String getDetailDisplay(String account) {
        return getDisplayContent(account, false);
    }

    private String getDisplayContent(String account, boolean simple) {
        if (account == null || account.equals(NimUIKit.getAccount())) {
            return "";
        }

        // 被过滤掉的直接显示在线，如机器人
        if (OnlineStateEventSubscribe.subscribeFilter(account)) {
            return "在线";
        }

        // 检查是否订阅过
        OnlineStateEventManager.checkSubscribe(account);

        OnlineState onlineState = OnlineStateEventCache.getOnlineState(account);
        return OnlineStateEventManager.getOnlineClientContent(YunxinCache.getContext(), onlineState, simple);
    }
}

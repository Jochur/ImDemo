package com.netease.nim.uikit.business.recent;

import java.io.Serializable;

public interface ConversationTopCallback extends Serializable{
    void onCall(boolean isTagSet);//是否置顶
}

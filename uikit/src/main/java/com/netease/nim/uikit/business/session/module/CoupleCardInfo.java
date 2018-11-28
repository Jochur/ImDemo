package com.netease.nim.uikit.business.session.module;


import com.netease.nim.uikit.business.recent.ConversationTopCallback;

import java.io.Serializable;

public class CoupleCardInfo implements Serializable {
    private CoupleInfo data;

    public CoupleInfo getData() {
        return data;
    }

    public void setData(CoupleInfo data) {
        this.data = data;
    }

    public static class CoupleInfo implements Serializable {
        private boolean loversCardFlag;//是否开通情侣卡 true：是；false：否
        private String loversCardOtherUserId;//情侣卡中另一个用户的加密id
        private String loversCardIcon;//情侣卡标记图片url
        private String top;//是否置顶
//        private ConversationTopCallback conversationTopCallback;

        public boolean isLoversCardFlag() {
            return loversCardFlag;
        }

        public void setLoversCardFlag(boolean loversCardFlag) {
            this.loversCardFlag = loversCardFlag;
        }

        public String getLoversCardOtherUserId() {
            return loversCardOtherUserId;
        }

        public void setLoversCardOtherUserId(String loversCardOtherUserId) {
            this.loversCardOtherUserId = loversCardOtherUserId;
        }

        public String getLoversCardIcon() {
            return loversCardIcon;
        }

        public void setLoversCardIcon(String loversCardIcon) {
            this.loversCardIcon = loversCardIcon;
        }

        public String getTop() {
            return top;
        }

        public void setTop(String top) {
            this.top = top;
        }

//        public ConversationTopCallback getConversationTopCallback() {
//            return conversationTopCallback;
//        }
//
//        public void setConversationTopCallback(ConversationTopCallback conversationTopCallback) {
//            this.conversationTopCallback = conversationTopCallback;
//        }
    }
}

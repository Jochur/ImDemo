package com.grechur.imdemo;

import java.io.Serializable;
import java.util.List;

public class CookieModel implements Serializable{
    private String platform;
    private int platformType;
    private String isCacheCookies;
    private List<PageModel> pageModels;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public String getIsCacheCookies() {
        return isCacheCookies;
    }

    public void setIsCacheCookies(String isCacheCookies) {
        this.isCacheCookies = isCacheCookies;
    }

    public List<PageModel> getPageModels() {
        return pageModels;
    }

    public void setPageModels(List<PageModel> pageModels) {
        this.pageModels = pageModels;
    }

    public static class PageModel implements Serializable{
        private String pageUrl;
        private String pageJs;
        private String isVisible;
        private String isCookieUrl;

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public String getPageJs() {
            return pageJs;
        }

        public void setPageJs(String pageJs) {
            this.pageJs = pageJs;
        }

        public String getIsVisible() {
            return isVisible;
        }

        public void setIsVisible(String isVisible) {
            this.isVisible = isVisible;
        }

        public String getIsCookieUrl() {
            return isCookieUrl;
        }

        public void setIsCookieUrl(String isCookieUrl) {
            this.isCookieUrl = isCookieUrl;
        }
    }
}

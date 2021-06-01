package com.xuexiang.templateproject.utils;

import android.content.Context;

import com.xuexiang.templateproject.R;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.data.BaseSPUtil;

/**
 * SharedPreferences管理工具基类
 *
 */
public class SettingSPUtils extends BaseSPUtil {

    private static volatile SettingSPUtils sInstance = null;

    private SettingSPUtils(Context context) {
        super(context);
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static SettingSPUtils getInstance() {
        if (sInstance == null) {
            synchronized (SettingSPUtils.class) {
                if (sInstance == null) {
                    sInstance = new SettingSPUtils(XUtil.getContext());
                }
            }
        }
        return sInstance;
    }


    private final String IS_FIRST_OPEN_KEY = "is_first_open_key";

    private final String IS_AGREE_PRIVACY_KEY = "is_agree_privacy_key";

    private final String IS_USE_CUSTOM_THEME_KEY = "is_use_custom_theme_key";

    private final String IS_USE_CUSTOM_FONT_KEY = "is_use_custom_font_key";
    private final String HTTP_URL = "http://localhost:8080/";
    /**
     * 是否是第一次启动
     */
    public boolean isFirstOpen() {
        return getBoolean(IS_FIRST_OPEN_KEY, true);
    }

    /**
     * 设置是否是第一次启动
     */
    public void setIsFirstOpen(boolean isFirstOpen) {
        putBoolean(IS_FIRST_OPEN_KEY, isFirstOpen);
    }

    /**
     * @return 是否同意隐私政策
     */
    public boolean isAgreePrivacy() {
        return getBoolean(IS_AGREE_PRIVACY_KEY, false);
    }

    public void setIsAgreePrivacy(boolean isAgreePrivacy) {
        putBoolean(IS_AGREE_PRIVACY_KEY, isAgreePrivacy);
    }

    public boolean isUseCustomTheme() {
        return getBoolean(IS_USE_CUSTOM_THEME_KEY, false);
    }

    public void setIsUseCustomTheme(boolean isUseCustomTheme) {
        putBoolean(IS_USE_CUSTOM_THEME_KEY, isUseCustomTheme);
    }

    public boolean isUseCustomFont() {
        return getBoolean(IS_USE_CUSTOM_FONT_KEY, false);
    }

    public void setIsUseCustomFont(boolean isUseCustomFont) {
        putBoolean(IS_USE_CUSTOM_FONT_KEY, isUseCustomFont);
    }


    /**
     * 获取服务器地址
     *
     * @return
     */
    public String getApiURL() {
        return getString(getString(R.string.service_api_key), getString(R.string.default_service_api));
    }

    /**
     * 获取服务器地址
     *
     * @return
     */
    public boolean setApiURL(String apiUrl) {
        return putString(getString(R.string.service_api_key), apiUrl);
    }
}
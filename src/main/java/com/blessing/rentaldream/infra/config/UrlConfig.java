package com.blessing.rentaldream.infra.config;

public class UrlConfig {
    // 계정 관련 URL
    public final static String REDIRECT_URL = "redirect:";
    public final static String HOME_URL = "/";
    public final static String SIGN_UP_URL = "/sign-up";
    public final static String LOGIN_URL = "/login";

    public final static String ACCOUNT_SETTING_URL = "/account/settings";
    public final static String ACCOUNT_SETTING_PROFILE_URL = ACCOUNT_SETTING_URL + "/profile";
    public final static String ACCOUNT_SETTING_TAG_URL = ACCOUNT_SETTING_URL + "/tag";
    public final static String ACCOUNT_SETTING_ZONE_URL = ACCOUNT_SETTING_URL + "/zone";
    public final static String ACCOUNT_SETTING_PASSWORD_URL = ACCOUNT_SETTING_URL + "/password";

    //계시글 관련 URL
    public final static String POST_URL = "/post";
    public final static String POST_NEW_POST_URL = "/new-post";
    public final static String POST_EDIT_URL = "/edit-post";
    public static final String POST_LIST_URL = "/posts";

    //관심상품 관련
    public static final String FAVORITE_URL = "/account/favorite";

    //댓글
    public static final String COMMENT_URL = "/comment";
    public static final String COMMENT_EDIT_URL = COMMENT_URL + "/edit";

    //알림
    public static final String NOTIFICATION_URL = "/notifications";
    public static final String NOTIFICATION_LIST_URL = "/notifications";
    public static final String NOTIFICATION_ALL_URL = NOTIFICATION_LIST_URL + "/all";

    //검색
    public static final String SEARCH_URL = "/search";


}

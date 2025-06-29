package com.tipo.main;

import io.github.cdimascio.dotenv.Dotenv;

public class Constants {
    private static final boolean IS_PROD = true; //prod
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    public static final String CRAWLER_DOMAIN="https://www.38.co.kr";
    public static final String CRAWLER_MENU="/html/fund/index.htm?o=k";
    public static final String IPO_SUBSCRIPTION_SCHEDULE = "공모주 청약일정";
    public static final String COMPANY_OVERVIEW = "기업개요";
    public static final String IPO_INFO = "공모정보";
    public static final String IPO_SCHEDULE = "공모청약일정";

    //kakao
    public static final String KAKAO_API_KEY = getEnv("KAKAO_API_KEY");
    public static final String KAKAO_ADMIN_KEY = getEnv("KAKAO_ADMIN_KEY");
    public static final String KAKAO_TOKEN_API="https://kauth.kakao.com/oauth/token";
    public static final String KAKAO_REDIRECT_URI="http://localhost:8080/login-callback";
    public static final String KAKAO_FRIENDS_API="https://kapi.kakao.com/v1/api/talk/friends";
    public static final String KAKAO_SEND_API = "https://kapi.kakao.com/v1/api/talk/friends/message/default/send";
    public static final String KAKAO_SEND_SELF = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    //notion
    public static final String NOTION_API="https://api.notion.com/v1/pages";
    public static final String NOTION_VERSION="2022-06-28";
    public static final String NOTION_TOKEN = getEnv("NOTION_TOKEN");
    public static final String DATABASE_ID = getEnv("NOTION_DATABASE_ID");

    public static String getEnv(String key) {
        String value;
        if(IS_PROD) {
            value = System.getenv(key);
        } else {
            System.out.println("dotenv = " + dotenv); // null이면 로딩 자체 실패
            value = dotenv.get(key);
        }
        return value;
    }
}

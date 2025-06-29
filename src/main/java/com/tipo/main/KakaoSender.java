package com.tipo.main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
public class KakaoSender {
    public void getToken() throws ParseException{
        String authorizationCode = "";
        // HttpClient 생성
        HttpClient client = HttpClient.newHttpClient();

        // POST 요청에 보낼 JSON 데이터
        // POST 요청 생성
        String requestBody = String.format(
                "grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
                Constants.KAKAO_API_KEY, Constants.KAKAO_REDIRECT_URI, authorizationCode
        );

        // POST 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Constants.KAKAO_TOKEN_API)) // 요청할 API URL
                .header("Content-Type", "application/x-www-form-urlencoded") // 요청 헤더 설정
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // POST 요청
                .build();

        // 요청 보내고 응답 받기
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(KakaoSender::setToken) // 결과 출력
                .join(); // 비동기 작업 완료 대기
    }

    private static String setToken(String body) {
        String accessToken = "";

        try {
            System.out.println(body);
            JSONParser jp = new JSONParser();
            JSONObject jsonObj = (JSONObject) jp.parse(body);
            accessToken = jsonObj.get("access_token").toString();

            // 친구에게 보내기
            // getFriendList(accessToken);

            // 나에게 보내기
            //sendMyself(accessToken);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("JSON 파싱 오류: " + e.getMessage());
        }
        return accessToken;
    }

    public boolean sendMyself(String notionUrl) throws Exception {
        String accessToken = "";
        Boolean result = false;

        // HttpClient 생성
        HttpClient client = HttpClient.newHttpClient();

        JSONObject links = new JSONObject();
        links.put("web_url", "https://developers.kakao.com");
        links.put("mobile_web_url", "https://developers.kakao.com");

        JSONObject obj = new JSONObject();
        obj.put("object_type", "text");
        obj.put("text", notionUrl);
        obj.put("link",links);
        obj.put("button_title","바로 확인");

        String templateObject = URLEncoder.encode(obj.toJSONString(), StandardCharsets.UTF_8);

        // POST 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken) // 요청 헤더 설정
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(Constants.KAKAO_SEND_SELF)) // 요청할 API URL정
                .POST(HttpRequest.BodyPublishers.ofString("template_object=" + templateObject)) // POST 요청
                .build();

        // 요청 보내고 응답 받기
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(KakaoSender::callback) // 결과 출력
                .join(); // 비동기 작업 완료 대기

        return result;
    }
    private static void callback(String body) {
        System.out.println(body);
    }

    public static void getFriendList(String accessToken) {
        // HttpClient 생성
        HttpClient client = HttpClient.newHttpClient();

        // GET 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken) // 요청 헤더 설정
                .uri(URI.create(Constants.KAKAO_FRIENDS_API)) // 요청할 API URL
                .GET()
                .build();

        // 요청 보내고 응답 받기
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(KakaoSender::setFriends) // 결과 출력
                .join(); // 비동기 작업 완료 대기
    }

    private static void setFriends(String body) {
        System.out.println("---setfriends---");
        System.out.println(body);
    }


}

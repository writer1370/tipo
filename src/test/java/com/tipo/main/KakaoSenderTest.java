package com.tipo.main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@SpringBootTest
public class KakaoSenderTest {
    @Value("${rest.api.key}")
    private String restApiKey;

    @Value("${redirect.uri}")
    private String redirectUri;

    @Value("${token.api}")
    private String tokenApi;

    @Value("${friends.api}")
    private String friendsApi;

    @Test
    public void getToken() throws ParseException{
        String authorizationCode = "MC64uIP_MC8dwnSCcJYo5KADFMzDGsv9BiZFkS7Bp0j53_O8Cj_VywAAAAQKKcjZAAABkxPx2KNDz1szkZmFRA";
        // HttpClient 생성
        HttpClient client = HttpClient.newHttpClient();

        // POST 요청에 보낼 JSON 데이터
        // POST 요청 생성
        String requestBody = String.format(
                "grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
                restApiKey, redirectUri, authorizationCode
        );

        // POST 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenApi)) // 요청할 API URL
                .header("Content-Type", "application/x-www-form-urlencoded") // 요청 헤더 설정
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // POST 요청
                .build();

        // 요청 보내고 응답 받기
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(KakaoSenderTest::setToken) // 결과 출력
                .join(); // 비동기 작업 완료 대기
    }

    private static void setToken(String body) {
        try {
            System.out.println(body);
            JSONParser jp = new JSONParser();
            JSONObject jsonObj = (JSONObject) jp.parse(body);
            String accessToken = jsonObj.get("access_token").toString();

            // 친구에게 보내기
            // getFriendList(accessToken);

            // 나에게 보내기
            sendMyself(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("JSON 파싱 오류: " + e.getMessage());
        }
    }

    public static void sendMyself2(String accessToken) throws Exception {
        // HttpClient 생성
        HttpClient client = HttpClient.newHttpClient();

        IpoCrawlerTest ipoCrawlerTest = new IpoCrawlerTest();
        ArrayList<StringBuffer> ipoInfoList = ipoCrawlerTest.crawlIpoData();

        JSONObject links = new JSONObject();
        links.put("web_url", "https://developers.kakao.com");
        links.put("mobile_web_url", "https://developers.kakao.com");

        if(ipoInfoList.size() > 0) {
            for(StringBuffer info : ipoInfoList) {
                JSONObject obj = new JSONObject();
                obj.put("object_type", "text");
                obj.put("text", info);
                obj.put("link",links);
                obj.put("button_title","바로 확인");

                String templateObject = URLEncoder.encode(obj.toJSONString(), StandardCharsets.UTF_8);

                // POST 요청 생성
                HttpRequest request = HttpRequest.newBuilder()
                        .header("Authorization", "Bearer " + accessToken) // 요청 헤더 설정
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .uri(URI.create("https://kapi.kakao.com/v2/api/talk/memo/default/send")) // 요청할 API URL정
                        .POST(HttpRequest.BodyPublishers.ofString("template_object=" + templateObject)) // POST 요청
                        .build();

                // 요청 보내고 응답 받기
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAccept(KakaoSenderTest::callback) // 결과 출력
                        .join(); // 비동기 작업 완료 대기
            }

        }
    }
    public static void sendMyself(String accessToken) throws Exception {
        // HttpClient 생성
        HttpClient client = HttpClient.newHttpClient();
        String templateObject = URLEncoder.encode("""
                {
                    "object_type": "text",
                    "text": "[기업개요]
                                     종목명: 뉴엔에이아이(구.알에스엔)
                                     업종: 컴퓨터시스템 통합 자문 및 구축 서비스업
                                     기업구분: 중소일반
                                     홈페이지: www.realsn.com
                                     매출액: 17,880 (백만원)
                                     순이익: 1,105 (백만원)
                                     
                                     [공모정보]
                                     총공모주식수: 2,220,000 주
                                     희망공모가액: 13,000 ~ 15,000 원
                                     확정공모가: 15,000 원
                                     주간사: NH투자증권,신한투자증권
                                     
                                     [공모청약일정]
                                     공모청약일: 2025.06.23 ~ 2025.06.24
                                     환불일: 2025.06.26
                                     상장일: 2025.07.04
                                     
                                     상세정보 확인 : https://www.38.co.kr/html/fund/?o=v&no=2192&l=&page=1",
                    "link": {
                        "web_url": "https://developers.kakao.com",
                        "mobile_web_url": "https://developers.kakao.com"
                    },
                    "button_title": "바로 확인"
                }
                """, StandardCharsets.UTF_8);

        System.out.println(templateObject);

        // POST 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken) // 요청 헤더 설정
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create("https://kapi.kakao.com/v2/api/talk/memo/default/send")) // 요청할 API URL정
                .POST(HttpRequest.BodyPublishers.ofString("template_object=" + templateObject)) // POST 요청
                .build();

        // 요청 보내고 응답 받기
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(KakaoSenderTest::callback) // 결과 출력
                .join(); // 비동기 작업 완료 대기

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
                .uri(URI.create("https://kapi.kakao.com/v1/api/talk/friends")) // 요청할 API URL
                .GET()
                .build();

        // 요청 보내고 응답 받기
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(KakaoSenderTest::setFriends) // 결과 출력
                .join(); // 비동기 작업 완료 대기
    }

    private static void setFriends(String body) {
        System.out.println("---setfriends---");
        System.out.println(body);
    }


}

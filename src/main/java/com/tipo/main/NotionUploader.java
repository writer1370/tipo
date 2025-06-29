package com.tipo.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

@Component
public class NotionUploader {
    public String uploader(ArrayList<StringBuffer> ipoDataList) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        JSONObject json = new JSONObject();
        JSONArray childrenArray = new JSONArray();

        JSONObject parent = new JSONObject();
        parent.put("database_id", Constants.DATABASE_ID);

        JSONObject properties = new JSONObject();
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        String[] cuties = {"´∇｀","･ω･","·ᴥ·","´ᴥ`","ˆﻌˆ","˘ﻌ˘"};

        Random random = new Random();
        int index = random.nextInt(cuties.length);

        properties.put("Name",makeProperties("TIPO(" + cuties[index] + ") " + date + " " + time));

        for(StringBuffer data : ipoDataList) {
            String[] contents = data.toString().split("\n");
            for(String content : contents) {
                childrenArray.add(makeBlock(content));
            }
        }

        json.put("parent", parent);
        json.put("properties", properties);
        json.put("children", childrenArray);

        System.out.println(json.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Constants.NOTION_API))
                .header("Authorization", "Bearer " + Constants.NOTION_TOKEN)
                .header("Content-Type", "application/json")
                .header("Notion-Version", Constants.NOTION_VERSION)
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser jp = new JSONParser();
        JSONObject jsonObj = (JSONObject) jp.parse(response.body());
        String url = jsonObj.get("url").toString();

        System.out.println("응답 코드: " + response.statusCode());
        System.out.println("응답 내용: " + response.body());
        return url;
    }

    public JSONObject makeProperties(String title) throws Exception {
        JSONObject name = new JSONObject();

        // JSONObject 만들기
        JSONObject content = new JSONObject();
        content.put("content", title);

        JSONObject text = new JSONObject();
        text.put("text", content);

        JSONArray titles = new JSONArray();
        titles.add(text);

        name.put("title", titles);

        return name;
    }
    public JSONObject makeBlock(String content) throws Exception {

        JSONObject text = new JSONObject();
        text.put("content",content);

        JSONObject richText = new JSONObject();
        richText.put("type","text");
        richText.put("text",text);

        JSONArray richTexts = new JSONArray();
        richTexts.add(richText);

        JSONObject paragraph = new JSONObject();
        paragraph.put("rich_text",richTexts);

        JSONObject block = new JSONObject();
        block.put("object","block");
        block.put("type","paragraph");
        block.put("paragraph",paragraph);

        return block;
    }
}

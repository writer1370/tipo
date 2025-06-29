package com.tipo.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class NotionUploaderTest {
    private static final String NOTION_API_URL = "https://api.notion.com/v1/pages";
    private static final String TOKEN = "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"; // Notion Integration Token
    private static final String DATABASE_ID = "21ac5bdbbe7180569b90e41ade2fb151"; // Notion DB ID
    private static final String NOTION_VERSION = "2022-06-28";

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
    public void uploader(ArrayList<StringBuffer> ipoDataList) throws Exception {

        //IpoCrawler ipoCrawler = new IpoCrawler();
        //ArrayList<StringBuffer> ipoDataList = ipoCrawler.crawlIpoData();

        HttpClient client = HttpClient.newHttpClient();
        JSONObject json = new JSONObject();
        JSONArray childrenArray = new JSONArray();

        JSONObject parent = new JSONObject();
        parent.put("database_id", DATABASE_ID);

        JSONObject properties = new JSONObject();
        properties.put("Name",makeProperties("제목"));

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
                .uri(URI.create(NOTION_API_URL))
                .header("Authorization", "Bearer " + TOKEN)
                .header("Content-Type", "application/json")
                .header("Notion-Version", NOTION_VERSION)
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("응답 코드: " + response.statusCode());
        System.out.println("응답 내용: " + response.body());
        }

}

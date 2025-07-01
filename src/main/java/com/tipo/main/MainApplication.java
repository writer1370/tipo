package com.tipo.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class MainApplication implements CommandLineRunner {

	@Autowired
	private IpoCrawler ipoCrawler;

	@Autowired
	private NotionUploader notionUploader;

	@Autowired
	private KakaoSender kakaoSender;

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// IPO 데이터 크롤링
		ArrayList<StringBuffer> ipoDataList = ipoCrawler.crawlIpoData();

		// NOITON에 업로드
		String messageText = "";
		if(ipoDataList != null && ipoDataList.size() > 0) {
			messageText = notionUploader.uploader(ipoDataList);
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("신규 IPO가 없습니다.");
			ipoDataList.add(sb);
			messageText = notionUploader.uploader(ipoDataList);
		}

		// NOTION URL 카카오톡으로 전송
		// kakaoSender.sendMyself(messageText);
	}


}

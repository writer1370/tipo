package com.tipo.main;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest(classes = MainApplicationTests.class)
class MainApplicationTests {

	@Test
	void contextLoads() throws Exception{
		IpoCrawlerTest ipoCrawlerTest = new IpoCrawlerTest();
		ArrayList<StringBuffer> ipoDataList = ipoCrawlerTest.crawlIpoData();

		NotionUploaderTest notionUploaderTest = new NotionUploaderTest();
		if(ipoDataList != null && ipoDataList.size() > 0) {
			notionUploaderTest.uploader(ipoDataList);
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("TEST");
			ipoDataList.add(sb);
			notionUploaderTest.uploader(ipoDataList);
			System.out.print("신규 IPO가 없습니다.");
		}

		//SendTest test = new SendTest();
		//test.sendMyself("WzlkRU8FJvIadRvo4jw6awHXRizzTWbvAAAAAQoNFKMAAAGXltT4Ca3XznpenZPe");
	}

}

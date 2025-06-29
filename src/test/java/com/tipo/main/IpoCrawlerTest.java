package com.tipo.main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IpoCrawlerTest {

    public ArrayList<StringBuffer> crawlIpoData() throws Exception {

        // 예정 공모주 상세 정보 링크 가져오기
        ArrayList<String> confirmedIPOLinks = getUpcomingIpoDetailLinks();
        ArrayList<StringBuffer> ipoInfoList = new ArrayList<>();

        if(confirmedIPOLinks.size() > 0) {
            int seq = 1;
            for(String link : confirmedIPOLinks) {
                // 공모주 상세페이지 문서 가져오기
                String IPODetailLink = ConstantsTest.TARGET_DOMAIN + link;
                Document document = Jsoup.connect(IPODetailLink).get();

                if (document != null) {
                    StringBuffer summary = new StringBuffer();
                    summary.append("=============== 공모주" + seq + " ===============").append("\n");
                    // 기업개요 정보 가져오기
                    StringBuffer companyOverview = getIpoDetail(document, ConstantsTest.COMPANY_OVERVIEW);

                    // 공모정보 정보 가져오기
                    StringBuffer ipoInfo = getIpoDetail(document, ConstantsTest.IPO_INFO);

                    // 공모청약일정 정보 가져오기
                    StringBuffer ipoSchedule = getIpoDetail(document, ConstantsTest.IPO_SCHEDULE);

                    summary.append(companyOverview).append("\n");
                    summary.append(ipoInfo).append("\n");
                    summary.append(ipoSchedule).append("\n");
                    summary.append("상세정보 확인 : " + IPODetailLink).append("\n");
                    seq++;
                    System.out.println(summary);
                    ipoInfoList.add(summary);
                }
            }
        }

        return ipoInfoList;
    }

    /**
     * 예정 공모주 상세 정보 링크 가져오기
     * @return
     * @throws Exception
     */
    public ArrayList<String> getUpcomingIpoDetailLinks() throws Exception{
        ArrayList<String> confirmedIPOLinks = new ArrayList<String>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        Document document = Jsoup.connect(ConstantsTest.TARGET_DOMAIN + ConstantsTest.TARGET_MENU).get();
        Element table = document.selectFirst("table[summary='" + ConstantsTest.IPO_SUBSCRIPTION_SCHEDULE + "']");

        if(table != null) {
            Elements rows = table.selectFirst("tbody").select("tr");

            for(Element row : rows) {
                Elements cells = row.select("td");

                String IPOPrice = cells.get(IPOInfoTest.getIndexByName("확정공모가")).text();
                String[] IPODates = cells.get(IPOInfoTest.getIndexByName("공모주일정")).text().split("~");
                Boolean isConfirmedIPO = LocalDate.parse(IPODates[0],dateFormat).isBefore(LocalDate.now());

                // 확정공모가가 있고, 시작일이 오늘 또는 이후인 공모주
                if(!isConfirmedIPO && !IPOPrice.equals("-")) {
                    Element confirmedIPO = cells.get(IPOInfoTest.getIndexByName("종목명"));
                    String link = confirmedIPO.select("a").attr("href");
                    if(!link.isBlank()) {
                        confirmedIPOLinks.add(link);
                    }
                }
            }
        }
        return confirmedIPOLinks;
    }

    /**
     * 예정 공모주 상세 정보 가져오기
     * @return
     * @throws Exception
     */
    public StringBuffer getIpoDetail(Document document, String subject) throws Exception {
        Element table = document.selectFirst("table[summary='" + subject + "']");
        StringBuffer sb = new StringBuffer();

        if(table != null) {
            sb.append("[" + subject + "]").append("\n");
            List<Map<String,Object>> list = IPODetailsTest.getMatrix(subject);
            if(list.size() > 0) {
                for(Map<String, Object> map : list) {
                    int[][] matrix = (int[][]) map.get("matrix");
                    String data = table.select("tr").get(matrix[0][0]).select("td").get(matrix[0][1]).text();
                    sb.append(map.get("name")).append(": ").append(data).append("\n");
                }
            }
        }
        return sb;
    }


}

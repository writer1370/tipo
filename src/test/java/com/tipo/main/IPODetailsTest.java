package com.tipo.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum IPODetailsTest {
    종목명(ConstantsTest.COMPANY_OVERVIEW,0,1),
    업종(ConstantsTest.COMPANY_OVERVIEW,2, 1),
    기업구분(ConstantsTest.COMPANY_OVERVIEW,3,3),
    홈페이지(ConstantsTest.COMPANY_OVERVIEW,5, 1),
    매출액(ConstantsTest.COMPANY_OVERVIEW,7, 1),
    순이익(ConstantsTest.COMPANY_OVERVIEW,8,1),
    총공모주식수(ConstantsTest.IPO_INFO,0,1),
    희망공모가액(ConstantsTest.IPO_INFO,2, 1),
    확정공모가(ConstantsTest.IPO_INFO,3, 1),
    주간사(ConstantsTest.IPO_INFO,4, 1),
    공모청약일(ConstantsTest.IPO_SCHEDULE,1, 1),
    환불일(ConstantsTest.IPO_SCHEDULE,4,1),
    상장일(ConstantsTest.IPO_SCHEDULE,5, 1);

    public final String subject;
    public final Integer row;
    public final Integer col;
    IPODetailsTest(String subject, int row, int col) {
        this.subject = subject;
        this.row = row;
        this.col= col;
    }

    public String getSubject(){
        return subject;
    }

    public int getCol(){
        return col;
    }

    public int getRow(){
        return row;
    }

    public static List getMatrix(String subject){
       List<Map<String,Object>> list = new ArrayList<>();
       for(IPODetailsTest info : IPODetailsTest.values()) {
           if(info.getSubject().equals(subject)) {
               Map<String,Object> map = new HashMap<>();
               map.put("name",info.name());

               int[][] matrix = new int[1][2];
               matrix[0][0] = info.getRow();
               matrix[0][1] = info.getCol();
               map.put("matrix", matrix);
               list.add(map);
           }
       }
        return list;
    }
}

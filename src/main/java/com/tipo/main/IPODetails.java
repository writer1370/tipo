package com.tipo.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum IPODetails {
    종목명(Constants.COMPANY_OVERVIEW,0,1),
    업종(Constants.COMPANY_OVERVIEW,2, 1),
    기업구분(Constants.COMPANY_OVERVIEW,3,3),
    홈페이지(Constants.COMPANY_OVERVIEW,5, 1),
    매출액(Constants.COMPANY_OVERVIEW,7, 1),
    순이익(Constants.COMPANY_OVERVIEW,8,1),
    총공모주식수(Constants.IPO_INFO,0,1),
    희망공모가액(Constants.IPO_INFO,2, 1),
    확정공모가(Constants.IPO_INFO,3, 1),
    주간사(Constants.IPO_INFO,4, 1),
    공모청약일(Constants.IPO_SCHEDULE,1, 1),
    환불일(Constants.IPO_SCHEDULE,4,1),
    상장일(Constants.IPO_SCHEDULE,5, 1);

    public final String subject;
    public final Integer row;
    public final Integer col;
    IPODetails(String subject, int row, int col) {
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
       for(IPODetails info : IPODetails.values()) {
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

package com.tipo.main;

public enum IPOInfo {
    종목명(0),
    공모주일정(1),
    확정공모가(2),
    희망공모가(3),
    청약경쟁률(4),
    주간사(5),
    분석(6);

    private final int index;
    IPOInfo(int index) {
        this.index = index;
    }

    public int getIndex(){
        return index;
    }
    public static Integer getIndexByName(String name){
        for (IPOInfo col : IPOInfo.values()) {
            if(col.name().equals(name)) {
                return col.getIndex();
            }
        }
        return null;
    }
}

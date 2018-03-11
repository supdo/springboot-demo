package com.supdo.sb.demo.common;

import java.util.ArrayList;
import java.util.List;

public class StringUtility {

    public static List<Long> toList(String str){
        List<Long> ids = new ArrayList();

        String[] strList = new String[]{};
        if(str.length() > 0) {
            strList = str.split("\\|");
        }
        for(String s : strList){
            ids.add(Long.parseLong(s));
        }
        return ids;
    }
}

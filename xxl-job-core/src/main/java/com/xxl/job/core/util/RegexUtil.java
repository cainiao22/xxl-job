package com.xxl.job.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexUtil {

    /** 匹配${param}这种形式的参数 **/
    private static Pattern paramPattern = Pattern.compile("\\$\\{(.*?)\\}");

    public static List<List<String>> getContentLimit(String regex, String string, int limit) {
        Pattern p = Pattern.compile(regex,Pattern.DOTALL);
        Matcher m = p.matcher(string);
        List<List<String>> resultList = new ArrayList<List<String>>();
        int count = 0;
        while (m.find()) {
            if (count >= limit) {
                break;
            }
            List<String> list = new ArrayList<String>();
            for (int i = 1; i <= m.groupCount(); i++) {
                list.add(m.group(i));
            }
            resultList.add(list);
            count++;
        }
        return resultList;
    }

    public static List<List<String>> getAllContent(String regex, String string) {
        return getContentLimit(regex, string, Integer.MAX_VALUE);
    }

    public static List<String> getFirstCotent(String regex, String string) {
        List<List<String>> list = getContentLimit(regex, string, 1);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return new ArrayList<String>();
        }

    }

    /**
     * 获取内容中的动态参数
     * @param content
     * @return
     */
    public static List<String> getDynamicParams(String content){
        Matcher matcher = paramPattern.matcher(content);
        List<String> result = new ArrayList<String>();
        while (matcher.find()){
            String group = matcher.group(1);
            if(!result.contains(group)){
                result.add(group);
            }
        }
        return result;
    }

}

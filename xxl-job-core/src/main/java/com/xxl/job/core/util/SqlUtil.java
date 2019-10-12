package com.xxl.job.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class SqlUtil {

	public static String parse(String etlJobSql, Date scheduleTime) {

		List<List<String>> allMatchedSegmentList = RegexUtil.getAllContent("(\\$\\{(.+?)\\})", etlJobSql);
        for (List<String> matchedItem : allMatchedSegmentList) {
			String oriSegment = matchedItem.get(0);
			String expression = matchedItem.get(1).replaceAll("\\s+", "");
			List<String> matchedExpressionList = RegexUtil.getFirstCotent("^(当天|当月|当年){1}(([+|\\-]){1}(\\d+)){0,1}$",
					expression);
			if (matchedExpressionList.size() != 4) {
				continue;
			}
			int offset = 0;
			if (StringUtils.isNotBlank(matchedExpressionList.get(1))) {
				offset = Integer.parseInt(matchedExpressionList.get(1));
			}
			if (matchedExpressionList.get(0).equals("当天")) {
				etlJobSql = etlJobSql.replace(oriSegment, "'" + DateUtil.getDayByN(scheduleTime, offset) + "'");
			}
			if (matchedExpressionList.get(0).equals("当月")) {
				etlJobSql = etlJobSql.replace(oriSegment, "'" + DateUtil.getMonthByN(scheduleTime, offset) + "'");
			}
			if (matchedExpressionList.get(0).equals("当年")) {
				etlJobSql = etlJobSql.replace(oriSegment, "'" + (scheduleTime.getYear() + 1900 + offset) + "'");
			}
		}

		return etlJobSql.trim();
	}

}
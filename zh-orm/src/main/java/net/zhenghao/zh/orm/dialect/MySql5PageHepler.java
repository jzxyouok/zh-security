package net.zhenghao.zh.orm.dialect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date  :2017年12月5日 下午3:50:51
 * MySql5PageHepler.java
 */
public class MySql5PageHepler {

	/**
	 * 得到查询总数的SQL
	 * @param querySelect
	 * @return
	 */
	public static String getCountString(String querySelect) {
		querySelect = getLineSql(querySelect);
		int orderIndex = getLastOrderInsertPoint(querySelect);
		int formIndex = getAfterFormInsertPoint(querySelect);
		String select = querySelect.substring(0, formIndex);
		
		// 如果SELECT 中包含 DISTINCT 或者querySelect有 group by 则只能在外层包含COUNT
		if (select.toLowerCase().indexOf("select distinct") != -1 || querySelect.toLowerCase().indexOf("group by") != -1) {
			return new StringBuffer(querySelect.length()).append("select count(1) count from (").append(querySelect.substring(0, orderIndex)).append("  ) t").toString();
		} else {
			//用于优化SQL,减少查询条件,避免order by 提高查询效率
			return new StringBuffer(querySelect.length()).append("select count(1) count ").append(querySelect.substring(formIndex, orderIndex)).toString();
		}
	}
	
	/**
	 * 得到分页sql
	 * @param querySelect
	 * @param offset 偏移起始位置
	 * @param limit 偏移量
	 * @return 分页SQL
	 */
	public static String getLimitString(String querySelect, int offset, int limit) {
		querySelect = getLineSql(querySelect);
		String sql = querySelect + " limit " + offset + " ," + limit;
		return sql;
	}
	
	
	/**
	 * 将SQL语句变成一条语句,并且每个单词的间隔都是1个空格
	 * @param sql
	 * @return 如果sql是NULL返回空,否则返回转化后的SQL
	 */
	private static String getLineSql(String sql) {
		return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
	}
	
	
	/**
	 * 得到最后一个Order By的插入点位置
	 * @param querySelect
	 * @return
	 */
	private static int getLastOrderInsertPoint(String querySelect) {
		int orderIndex = querySelect.toLowerCase().lastIndexOf("order by");
		if (orderIndex == -1) {
			orderIndex = querySelect.length();
		}
		if (!isBracketCanPartnership(querySelect.substring(orderIndex, querySelect.length()))) {
			throw new RuntimeException("My SQL 分页必须要有Order by 语句!");
		}
		return orderIndex;
	}
	
	/**
	 * 判断括号"()"是否匹配,并不会判断排列顺序是否正确
	 * @param text
	 * @return 如果匹配返回TRUE,否则返回FALSE
	 */
	private static boolean isBracketCanPartnership(String text) {
		if (text == null || (getIndexOfCount(text, '(') != getIndexOfCount(text, ')'))) {
			return false;
		}
		return true;
	}
	
	/**
	 * 得到一个字符在另一个字符串中出现的次数
	 * @param text
	 * @param ch
	 * @return
	 */
	private static int getIndexOfCount(String text, char ch) {
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			count = (text.charAt(i) == ch) ? count + 1 : count;
		}
		return count;
	}
	
	/**
	 * 得到SQL第一个正确的FROM的插入点
	 * @param querySelect
	 * @return
	 */
	private static int getAfterFormInsertPoint(String querySelect) {
		String regex = "\\s+FROM\\s+";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(querySelect);
		while (matcher.find()) {
			int fromStartIndex = matcher.start(0);
			String text = querySelect.substring(0, fromStartIndex);
			if (isBracketCanPartnership(text)) {
				return fromStartIndex;
			}
		}
		return 0;
	}
}

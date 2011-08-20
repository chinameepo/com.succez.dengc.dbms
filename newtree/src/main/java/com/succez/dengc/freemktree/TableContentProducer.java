package com.succez.dengc.freemktree;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-19 上午08:24:06
 * @since jdk1.6 产生一个表的字段以及内容。
 */
public class TableContentProducer {
	private String url;
	private String database;
	private String table;
	private StringBuffer allString;
	/**
	 * 这个表一共有几列
	 */
	private int clum;
	private ResultSet resultSet;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ApplicationContext aplcxt = new ClassPathXmlApplicationContext(
			"spring.xml");

	public TableContentProducer(String url) {
		this.url = url;
	}

	public void produceContent() {
		getDataBaseInfo(url);
		getTableContent();

	}

	/**
	 * 这样可以获得数据库名字，表名字。其实最关键的是url中包含了==号。
	 * 
	 * @param url
	 * @return
	 */
	public void getDataBaseInfo(String url) {
		int key = url.lastIndexOf('/');
		String keyString = url.substring(key + 1);
		String[] infoStrings = keyString.split("==");
		database = infoStrings[0];
		int indexOfDot = infoStrings[1].indexOf('.');
		table = infoStrings[1].substring(0, indexOfDot);
	}
	
	/**
	 * 获得表的字段名称
	 */
	public void getTableContent() {
		try {
			SQLBean bean = (SQLBean) aplcxt.getBean("sqlBeans");
			bean.setDatabaseName(database);
			bean.connect();
			bean.statement();
			if ((database != null) && (!"".equals(database))) {
				bean.ecxecute("use  " + database);
			}
			resultSet = bean.query("select * from " + table);
			ResultSetMetaData metaData = resultSet.getMetaData();
			clum = metaData.getColumnCount();
			allString =new StringBuffer();
			//字段作为第一行先上去。
			allString.append(" <table id=customers >  <tr>");
			for (int i = 0; i < clum; i++) {
				 allString.append("<th>"+metaData.getColumnName(i + 1)+"</th>");
			}
			allString.append("</tr>");
			// 因为第0行被用作字段了，所以我获取内容从第一行开始填装
			int rowCount = 1;
			while (resultSet.next()) {
				if(rowCount%2==0)
				    allString.append("<tr>");
				else
					allString.append("<tr class="+"alt>");
				for (int i = 0; i < clum; i++) {
					allString.append("<td>"+resultSet.getString(i + 1)+"</td>");
				}
				rowCount++;
				allString.append("</tr>");
			}
			allString.append("</table>");
			bean.close();
			resultSet.close();
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
	}

	public StringBuffer getAllString() {
		return allString;
	}

}

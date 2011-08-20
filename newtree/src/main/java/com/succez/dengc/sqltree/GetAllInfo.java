package com.succez.dengc.sqltree;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-17 下午03:14:39
 * @since jdk1.6 这次要把我的所有的数据库，有的的表，所有的字段都显示出来。
 */

public class GetAllInfo {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ResultSet resultSet;
	private SQLBean bean;
	/**
	 * 其实数据的名字不要也可以的，为空也没关系，一样可以连接上数据库。
	 */
	private String dataBaseName;
	private String[] dataBases;
	private static int id =1;
	/**
	 * 这个印射的原理是：从dataBases中取出一个元素，然后根据元素的值来印射到指定的table表的字符串数组。
	 */
	private Map<String, String[]> dataBase2Tables = new HashMap<String, String[]>();
	
	/**
	 * 为了保证唯一性，我必须要让key的值是唯一的，所一我要让key的值为：database+table
	 */
    private ArrayList<TreeBean> list = new ArrayList<TreeBean>();
	/**
	 * @param databaseName
	 *            ,数据库的名称，无所谓了。
	 */
	public GetAllInfo(String databaseName) {
		this.dataBaseName = databaseName;
	}
	public ArrayList<TreeBean> getList() {
		return list;
	}

	/**
	 * 工程的核心函数，它来执行获取数据库名字，表名字，字段名字的操作。
	 */
	public void getAll() {
		dataBases = getOneClomResult("show databases");
		getTbles(dataBases, dataBase2Tables);
		//showTables(dataBases, dataBase2Tables);
		addNode();
	}

	/**
	 * 这个函数的作用：1，获取所有的数据库名字。2，获取某个指定数据库中的所有视图和表单的名称。
	 * 
	 * @param sql
	 * @return String[],一个字符串数组，都是一些数据库和表的名称。
	 */
	public String[] getOneClomResult(String sql) {
		String[] resultStrings;
		bean = new SQLBean(dataBaseName);
		bean.connect();
		bean.statement();
		resultSet = bean.query(sql);
		if (resultSet == null)
			return null;
		try {
			resultStrings = new String[getRows(resultSet)];
			int i = 0;
			while (resultSet.next()) {
				resultStrings[i] = resultSet.getString(1);
				i++;
			}
			bean.close();
			return resultStrings;
		} catch (SQLException e) {
			logger.error(e.toString());
			return null;
		} 
	}

	/**
	 * 获取这个查询结果集的行数。
	 * 
	 * @param resultSet
	 * @return
	 */
	public int getRows(ResultSet resultSet) {
		if (resultSet == null)
			return 0;
		int row = 0;
		try {
			resultSet.last();
			row = resultSet.getRow();
			// 注意一定要把游标跳回到最前面
			resultSet.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}

	/**
	 * @param strings
	 *            数据库名称的数组
	 * @param map
	 *            数据库名称到表名称数组的印射
	 */
	public void getTbles(String[] strings, Map<String, String[]> map) {
		for (int i = 0; i < strings.length; i++) {
			map.put(strings[i], getOneClomResult("show tables from   "
					+ strings[i]));
		}
	}

	/**
	 * 获得指定数据库下面一张表的字段和里面的内容。
	 * 
	 * @param dataBases
	 * @param map
	 */
	public void showTables(String[] dataBases, Map<String, String[]> map) {
		for (int i = 0; i <dataBases.length; i++) {
			String[] tables = map.get(dataBases[i]);
			logger.info("数据库{}下的目录结构：", dataBases[i]);
			for (int j =0; j <tables.length; j++) {
				logger.info("表{}下的目录结构：", tables[j]);
				getTableInfo(dataBases[i], tables[j]);
			}
		}
	}
	
    /**
     * 获取指定数据库下面的指定表的内容
     * */
	public void getTableInfo(String database, String table) {
		bean = new SQLBean(database);
		bean.connect();
		bean.statement();
		bean.ecxecute("use " + database );
		String sql ="select * from " +table+";";
		resultSet = bean.query(sql);
		seeFields(resultSet);
		bean.close();
	}

	/**
	 * 获取字段名称
	 * 
	 * @param resultSet
	 */
	public void seeFields(ResultSet resultSet) {
		try {
			// 看字段
			ResultSetMetaData metaData = resultSet.getMetaData();
			int cloumes = metaData.getColumnCount();
			StringBuffer buffer = new StringBuffer();
			for (int i = 1; i <=cloumes; i++) {
				buffer.append(metaData.getColumnName(i) + ",    ");
			}
			logger.info(buffer.toString());
			// 看内容
			StringBuffer bufferReusltBuffer =new StringBuffer();
			while(resultSet.next()){
				StringBuffer buffer2 = new StringBuffer();
				for(int i=1;i<=cloumes;i++){
					buffer2.append(resultSet.getString(i)+",	");
				}
				bufferReusltBuffer.append(buffer2+"\n");
			}
			logger.info(bufferReusltBuffer.toString());
		} catch (SQLException e) {
			logger.error(e.toString());
			return;
		}
	}
	
	public static void setId(int id) {
		GetAllInfo.id = id;
	}
	public void addNode(){
		//添加数据库的 根节点
		for(int i=0;i<dataBases.length;i++){
			list.add(new TreeBean(id,0, dataBases[i]));
			id= id+1;
		}
		//添加表单
		for(int i=0;i<dataBases.length;i++){
			String []tables =dataBase2Tables.get(dataBases[i]);
			for(int j=0;j<tables.length;j++){
				list.add(new TreeBean(id, i+1, tables[j],"template.ftl"));
				id= id+1;
			}
		}
	}
	public static void main(String[] args) {
		GetAllInfo allInfo = new GetAllInfo("deng");
		allInfo.getAll();
	}
}

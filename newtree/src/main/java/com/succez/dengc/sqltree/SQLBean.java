package com.succez.dengc.sqltree;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-17 下午02:57:31
 * @since jdk1.6 
 *它数据库的连接的操作，只要调用它就可以完成一些基本操作。目前还不打算使用spring注入。
 */
public class SQLBean {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private String user = "root";
	private String pass = "dengchao";
	private String url = "jdbc:mysql://127.0.0.1:3306/";
	private String className = "com.mysql.jdbc.Driver";
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 我们只需要指定是在用哪个数据库即可。
	 * 
	 * @param dataBaseName
	 */
	public SQLBean(String dataBaseName) {
		this.url = this.url + dataBaseName;
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error("驱动加载失败，来自方法：SQLBean【SQLBean】");
			return;
		} 
	}

	public SQLBean() {
		this("");
	}

	/**
	 * 连接指定的数据库。其实数据库的名字没有，也可以连接成功。
	 */
	public void connect() {
		try {
			connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("来自方法：SQLBean【connect()】"+e.toString());
			return;
		}
	}

	/**
	 * 获取陈述对象，注意这里的两个参数，TYPE_SCROLL_INSENSITIVE,是可以保证来回滚动，而不是一次性滚动
	 * 之后就不能呢个滚回来了。CONCUR_UPDATABLE，持续更新。
	 */
	public void statement() {
		try {
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("来自方法：SQLBean【statement()】"+e.toString());
			return;
		}
	}

	public ResultSet  query(String sql) {
		try {
			resultSet = statement.executeQuery(sql);
			return resultSet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("来自方法：SQLBean【 query(String sql)】"+e.toString());
			return null;
		}
	}
	public void  ecxecute(String sql){
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			logger.info("来自方法：SQLBean【ecxecute(String sql】"+e.toString());
		}
	}

	public void close() {
		try {
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception e) {
			logger.error("来自方法：SQLBean【close()】"+e.toString());
			return;
		}
	}

	/**
	 * getter &&setter
	 */
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}

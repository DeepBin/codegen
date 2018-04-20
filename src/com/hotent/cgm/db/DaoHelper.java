
package com.hotent.cgm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hotent.cgm.exception.CodegenException;

/**
 * 对jdbc进行封装实现数据库访问。<br>
 * 采用模版模式实现。
 */
public class DaoHelper<T> {
	
	private String url = "";
	
	private String userName = "";
	
	private String pwd = "";
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	/**
	 * 传入数据库url，用户名和密码。
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	public DaoHelper( String url, String username, String password ) {
		this.url = url;
		this.userName = username;
		this.pwd = password;
	}
	
	/**
	 * 查询一个对象。<br/>
	 * 使用方法：<br/>
	 * 
	 * <pre>
	 * DaoHelper<String> dao = new DaoHelper<String>(this.url, this.username, this.password);
	 * 
	 * String sql = String.format(sqlTableComment, tableName);
	 * 
	 * String comment = dao.queryForObject(sql, new MapCmd<String>() {
	 * 	public String getObjecFromRs(ResultSet rs) throws SQLException {
	 * 		return rs.getString("comment");
	 * 	}
	 * });
	 * </pre>
	 * 
	 * @param <T>
	 * @param sql
	 * @param cmd
	 * @return
	 * @throws CodegenException
	 */
	public T queryForObject(String sql, MapCmd<T> cmd) throws CodegenException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection(this.url, this.userName, this.pwd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) { return cmd.getObjecFromRs(rs); }
			System.out.println("没有到数据:" + sql);
			return null;
		} catch (SQLException e) {
			throw new CodegenException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new CodegenException(e);
			}
		}
		
	}
	
	/**
	 * 查询列表对象。 使用方法如下：
	 * 
	 * <pre>
	 * DaoHelper<ColumnModel> dao = new DaoHelper<ColumnModel>(this.url, this.username, this.password);
	 * 
	 * String sql = String.format(sqlColumn, tableName);
	 * 
	 * List<ColumnModel> list = dao.queryForList(sql, new OracleMapCmd());
	 * </pre>
	 * 
	 * @param sql
	 * @param cmd
	 * @return
	 * @throws CodegenException
	 */
	public List<T> queryForList(String sql, MapCmd<T> cmd) throws CodegenException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<T> list = new ArrayList<T>();
		try {
			conn = DriverManager.getConnection(this.url, this.userName, this.pwd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(cmd.getObjecFromRs(rs));
			}
		} catch (SQLException e) {
			throw new CodegenException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new CodegenException(e);
			}
		}
		return list;
	}
	
}

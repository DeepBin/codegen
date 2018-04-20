
package com.hotent.cgm.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.hotent.cgm.db.DaoHelper;
import com.hotent.cgm.db.IDbHelper;
import com.hotent.cgm.db.MapCmd;
import com.hotent.cgm.exception.CodegenException;
import com.hotent.cgm.model.ColumnModel;
import com.hotent.cgm.model.TableModel;
import com.hotent.cgm.util.StringUtil;

/**
 * 取得数据库表接口IDbHelper，达梦 的实现
 */
public class DMHelper implements IDbHelper {
	
	// 取得主键
	private String SQL_GET_PK = "SELECT  CONS_C.COLUMN_NAME FROM \"SYS\".\"USER_CONSTRAINTS\" CONS, \"SYS\".\"USER_CONS_COLUMNS\" CONS_C    WHERE  CONS.CONSTRAINT_NAME=CONS_C.CONSTRAINT_NAME  AND CONS.CONSTRAINT_TYPE='P'  AND CONS_C.POSITION=1  AND   CONS.TABLE_NAME='%s'";
	
	// 取得注释
	private String SQL_GET_TABLE_COMMENT = "SELECT TABLE_NAME,COMMENTS FROM (SELECT A.TABLE_NAME AS TABLE_NAME,DECODE(B.COMMENT$,NULL, A.TABLE_NAME,B.COMMENT$) AS COMMENTS FROM \"SYS\".\"USER_TABLES\" A LEFT JOIN \"SYS\".\"SYSTABLECOMMENTS\" B ON  A.TABLE_NAME=B.TVNAME) WHERE  TABLE_NAME ='%s'";
	
	/**
	 * 取得列表
	 */
	private final String SQL_GET_COLUMNS = "SELECT T.TABLE_NAME TABLE_NAME, T.NAME NAME,T.TYPENAME TYPENAME, T.LENGTH LENGTH, "
			+ " T.PRECISION PRECISION,T.SCALE SCALE,T.DATA_DEFAULT DATA_DEFAULT,T.NULLABLE NULLABLE,T.DESCRIPTION DESCRIPTION, "
			+ " (SELECT  COUNT(*)   FROM    \"SYS\".\"USER_CONSTRAINTS\" CONS, \"SYS\".\"USER_CONS_COLUMNS\" CONS_C    WHERE "
			+ " CONS.CONSTRAINT_NAME=CONS_C.CONSTRAINT_NAME  AND CONS.CONSTRAINT_TYPE='P'  AND CONS_C.POSITION=1 "
			+ " AND   CONS.TABLE_NAME=T.TABLE_NAME  AND CONS_C.COLUMN_NAME= T.NAME) AS  IS_PK"
			+ " FROM (SELECT A.COLUMN_ID COLUMN_ID, A.TABLE_NAME TABLE_NAME, A.COLUMN_NAME NAME, A.DATA_TYPE TYPENAME, A.DATA_LENGTH LENGTH,"
			+ " A.DATA_PRECISION PRECISION, A.DATA_SCALE SCALE, A.DATA_DEFAULT, A.NULLABLE,"
			+ " DECODE(B.COMMENT$,NULL, A.TABLE_NAME,B.COMMENT$) AS DESCRIPTION "
			+ " FROM \"SYS\".\"USER_TAB_COLUMNS\" A LEFT JOIN \"SYS\".\"SYSCOLUMNCOMMENTS\" B ON  A.COLUMN_NAME=B.COLNAME AND  A.TABLE_NAME=B.TVNAME  AND B.SCHNAME=user() ) T  WHERE TABLE_NAME='%S' "
			+ " ORDER BY COLUMN_ID ";
	
	// 取得数据库所有表
	private String SQL_GET_ALL_TABLES = "SELECT TABLE_NAME,COMMENTS FROM (SELECT A.TABLE_NAME AS TABLE_NAME,DECODE(B.COMMENT$,NULL, A.TABLE_NAME,B.COMMENT$) AS COMMENTS FROM \"SYS\".\"USER_TABLES\" A LEFT JOIN \"SYS\".\"SYSTABLECOMMENTS\" B ON  A.TABLE_NAME=B.TVNAME) WHERE 1=1";
	
	private String url;
	
	private String username;
	
	private String password;
	
	public DMHelper() throws CodegenException {
		try {
			Class.forName("dm.jdbc.driver.DmDriver");
		} catch (ClassNotFoundException e) {
			throw new CodegenException("找不到 达梦 驱动!", e);
		}
	}
	
	public void setUrl(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 根据表名取得列列表
	 * 
	 * @param tableName
	 * @return
	 * @throws CodegenException
	 */
	private List<ColumnModel> getColumnsByTable(String tableName) throws CodegenException {
		tableName = tableName.toUpperCase();
		DaoHelper<ColumnModel> dao = new DaoHelper<ColumnModel>(this.url, this.username, this.password);
		String sql = String.format(SQL_GET_COLUMNS, tableName);
		List<ColumnModel> list = dao.queryForList(sql, new DMMapCmd());
		return list;
	}
	
	/**
	 * 取得表注释
	 * 
	 * @param tableName
	 * @return
	 * @throws CodegenException
	 */
	private String getTableComment(String tableName) throws CodegenException {
		tableName = tableName.toUpperCase();
		
		String sql = String.format(SQL_GET_TABLE_COMMENT, tableName);
		DaoHelper<String> dao = new DaoHelper<String>(url, username, password);
		String comment = dao.queryForObject(sql, new MapCmd<String>() {
			public String getObjecFromRs(ResultSet rs) throws SQLException {
				return rs.getString("COMMENTS");
			}
		});
		if (comment == null)
			comment = tableName;
		String[] aryComment = comment.split("\n");
		return aryComment[0];
		
	}
	
	/**
	 * 取得表的主键
	 * 
	 * @param tableName
	 * @return
	 * @throws CodegenException
	 */
	private String getPk(String tableName) throws CodegenException {
		tableName = tableName.toUpperCase();
		String sql = String.format(SQL_GET_PK, tableName);
		DaoHelper<String> dao = new DaoHelper<String>(url, username, password);
		String pk = "";
		try {
			pk = dao.queryForObject(sql, new MapCmd<String>() {
				public String getObjecFromRs(ResultSet rs) throws SQLException {
					return rs.getString("COLUMN_NAME");
					
				}
			});
		} catch (Exception e) {
			throw new CodegenException("从表中取得主键出错,请检查表是否设置主键");
		}
		return pk;
		
	}
	
	/**
	 * 设置主键
	 * 
	 * @param list
	 * @param pk
	 */
	private void setPk(List<ColumnModel> list, String pk) {
		for (ColumnModel model : list) {
			if (pk.toLowerCase().equals(model.getColumnName().toLowerCase()))
				model.setIsPK(true);
		}
	}
	
	/**
	 * 根据表名取得表对象
	 * 
	 * @param tableName
	 */
	public TableModel getByTable(String tableName) throws CodegenException {
		tableName = tableName.toUpperCase();
		// 取得注释
		String tabComment = getTableComment(tableName);
		String pk = getPk(tableName);
		
		TableModel tableModel = new TableModel();
		tableModel.setTableName(tableName);
		tableModel.setTabComment(tabComment);
		// 取得表的列表数据
		List<ColumnModel> list = getColumnsByTable(tableName);
		// 设置主键
		if (StringUtil.isNotEmpty(pk)) {
			setPk(list, pk);
		}
		tableModel.setColumnList(list);
		
		return tableModel;
	}
	
	/**
	 * 取得数据库的所有表
	 */
	public List<String> getAllTable() throws CodegenException {
		
		DaoHelper<String> dao = new DaoHelper<String>(url, username, password);
		return dao.queryForList(SQL_GET_ALL_TABLES, new MapCmd<String>() {
			public String getObjecFromRs(ResultSet rs) throws SQLException {
				return rs.getString("TABLE_NAME");
			}
		});
	}
	
	public static void main(String[] args) throws CodegenException {
		
		DMHelper helper = new DMHelper();
		helper.setUrl("jdbc:oracle:thin:@localhost:1521:zyp", "zyp", "zyp");
		String pk = helper.getPk("TEST");
		
	}
	
}

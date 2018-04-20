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
 * 取得数据库表接口IDbHelper，H2 的实现
 *
 */
public class H2Helper implements IDbHelper {
	// 取得注释
	private String sqlTableComment = 
			"SELECT "+
				"TABLE_NAME, "+
				"REMARKS "+
			"FROM "+
				"INFORMATION_SCHEMA.TABLES T "+
			"WHERE "+
				"T.TABLE_SCHEMA=SCHEMA() "+
				"AND UPPER(TABLE_NAME) = UPPER('%s') ";

	// 取得列表
	private String sqlColumn = 
			"SELECT "+
					"A.TABLE_NAME, "+
					"A.COLUMN_NAME, "+
					"A.IS_NULLABLE, "+
					"A.TYPE_NAME, "+
					"A.CHARACTER_OCTET_LENGTH LENGTH, "+
					"A.NUMERIC_PRECISION PRECISIONS, "+
					"A.NUMERIC_SCALE SCALE, "+
					"B.COLUMN_LIST, "+
					"A.REMARKS "+ 
				"FROM "+
					"INFORMATION_SCHEMA.COLUMNS A  "+
					"JOIN INFORMATION_SCHEMA.CONSTRAINTS B ON A.TABLE_NAME=B.TABLE_NAME "+
				"WHERE  "+
					"A.TABLE_SCHEMA=SCHEMA() "+ 
					"AND B.CONSTRAINT_TYPE='PRIMARY KEY' "+
					"AND UPPER(A.TABLE_NAME) = UPPER('%s') ";
			
	// 取得数据库所有表
	private String sqlAllTables =
			"SELECT "+
					"TABLE_NAME, "+
					"REMARKS "+
				"FROM "+
					"INFORMATION_SCHEMA.TABLES T "+
				"WHERE "+
					"T.TABLE_SCHEMA=SCHEMA() ";

	private String url;
	private String username;
	private String password;

	public H2Helper() throws CodegenException {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new CodegenException("找不到H2驱动!", e);
		}
	}

	public void setUrl(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 根据表名取得列列表
	 * @param tableName
	 * @return
	 * @throws CodegenException
	 */
	private List<ColumnModel> getColumnsByTable(String tableName) throws CodegenException
	{
		tableName=tableName.toUpperCase();
		DaoHelper<ColumnModel> dao=new DaoHelper<ColumnModel>(this.url, this.username, this.password);
		String sql=String.format(sqlColumn,tableName);
		List<ColumnModel> list=dao.queryForList(sql, new H2MapCmd());
		return list;
	}



	/**
	 * 取得表注释
	 * @param tableName
	 * @return
	 * @throws CodegenException
	 */
	private String getTableComment(String tableName) throws CodegenException {
		tableName=tableName.toUpperCase();
		
		String sql = String.format(sqlTableComment, tableName);
		DaoHelper<String> dao=new DaoHelper<String>(url, username, password);
		String comment= dao.queryForObject(sql,new MapCmd<String>() {
			public String getObjecFromRs(ResultSet rs) throws SQLException {
				return rs.getString("REMARKS");
			}
		});
		return comment==null?tableName:comment;

	}
	
	/**
	 * 取得表的主键
	 * @param tableName
	 * @return
	 * @throws CodegenException
	 */
//	private String getPk(String tableName) throws CodegenException
//	{
//		tableName=tableName.toUpperCase();
//		String sql = String.format(sqlPk, tableName);
//		DaoHelper<String> dao=new DaoHelper<String>(url, username, password);
//		String pk="";
//		try {
//			pk= dao.queryForObject(sql,new MapCmd<String>() {
//				public String getObjecFromRs(ResultSet rs) throws SQLException{
//					return rs.getString("COLNAME");
//					
//				}
//			});
//		} catch (Exception e) {
//			throw new CodegenException("从表中取得主键出错,请检查表是否设置主键");
//		}
//		return pk;
//		
//	}

//	/**
//	 * 设置主键
//	 * @param list
//	 * @param pk
//	 */
//	private void setPk(List<ColumnModel> list ,String pk)
//	{
//		for(ColumnModel model :list){
//			if(pk.toLowerCase().equals(model.getColumnName().toLowerCase()))
//				model.setIsPK(true);
//		}
//	}
	
	/**
	 * 根据表名取得表对象
	 * @param tableName
	 */
	public TableModel getByTable(String tableName) throws CodegenException {
		tableName=tableName.toUpperCase();
		//取得注释
		String tabComment=getTableComment(tableName);
//		String pk=getPk(tableName);
		
		TableModel tableModel = new TableModel();
		tableModel.setTableName(tableName);
		tableModel.setTabComment(tabComment);
		//取得表的列表数据
		List<ColumnModel> list=getColumnsByTable(tableName);
		tableModel.setColumnList(list);
 
		return tableModel;
	}

	/**
	 * 取得数据库的所有表
	 */
	public List<String> getAllTable() throws CodegenException {

		DaoHelper<String> dao=new DaoHelper<String>(url, username, password);
		return dao.queryForList(sqlAllTables,new  MapCmd<String>() {
			public String getObjecFromRs(ResultSet rs) throws SQLException {
				return rs.getString("TABLE_NAME");
			}
		});
	}
}

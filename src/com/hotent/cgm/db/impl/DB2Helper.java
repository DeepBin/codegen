
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
 * 取得数据库表接口IDbHelper，DB2 的实现
 */
public class DB2Helper implements IDbHelper {
	
	// 取得主键
	private String sqlPk = "SELECT " + "TABNAME TAB_NAME, " + "COLNAME COL_NAME, " + "KEYSEQ  " + "FROM  "
			+ "SYSCAT.COLUMNS " + "WHERE  " + "TABSCHEMA='BPMX380' AND KEYSEQ>0 AND UPPER(TABNAME) = UPPER('%s')";
	
	// 取得注释
	private String sqlTableComment = "SELECT  " + "TABNAME, " + "REMARKS " + "FROM  " + "SYSCAT.TABLES " + "WHERE "
			+ "TABSCHEMA IN (SELECT CURRENT SCHEMA FROM SYSIBM.DUAL) " + "AND UPPER(TABNAME) = UPPER('%s') ";
	
	// 取得列表
	private String sqlColumn = "SELECT " + "TABNAME, " + "COLNAME, " + "TYPENAME, " + "REMARKS, " + "NULLS, "
			+ "LENGTH, " + "SCALE, " + "KEYSEQ  " + "FROM  " + "SYSCAT.COLUMNS " + "WHERE  "
			+ "TABSCHEMA IN (SELECT CURRENT SQLID FROM SYSIBM.DUAL) " + "AND UPPER(TABNAME) = UPPER('%s') ";
	
	// 取得数据库所有表
	private String sqlAllTables = "SELECT  " + "TABNAME, " + "REMARKS " + "FROM  " + "SYSCAT.TABLES " + "WHERE  "
			+ "TABSCHEMA IN (SELECT CURRENT SQLID FROM SYSIBM.DUAL) ";
	
	private String url;
	
	private String username;
	
	private String password;
	
	public DB2Helper() throws CodegenException {
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
		} catch (ClassNotFoundException e) {
			throw new CodegenException("找不到DB2驱动!", e);
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
		String sql = String.format(sqlColumn, tableName);
		List<ColumnModel> list = dao.queryForList(sql, new DB2MapCmd());
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
		
		String sql = String.format(sqlTableComment, tableName);
		DaoHelper<String> dao = new DaoHelper<String>(url, username, password);
		String comment = dao.queryForObject(sql, new MapCmd<String>() {
			public String getObjecFromRs(ResultSet rs) throws SQLException {
				return rs.getString("REMARKS");
			}
		});
		return comment == null ? tableName : comment;
		
	}
	
	/**
	 * 取得表的主键
	 * 
	 * @param tableName
	 * @return
	 * @throws CodegenException
	 */
	// private String getPk(String tableName) throws CodegenException
	// {
	// tableName=tableName.toUpperCase();
	// String sql = String.format(sqlPk, tableName);
	// DaoHelper<String> dao=new DaoHelper<String>(url, username, password);
	// String pk="";
	// try {
	// pk= dao.queryForObject(sql,new MapCmd<String>() {
	// public String getObjecFromRs(ResultSet rs) throws SQLException{
	// return rs.getString("COLNAME");
	//
	// }
	// });
	// } catch (Exception e) {
	// throw new CodegenException("从表中取得主键出错,请检查表是否设置主键");
	// }
	// return pk;
	//
	// }
	
	// /**
	// * 设置主键
	// * @param list
	// * @param pk
	// */
	// private void setPk(List<ColumnModel> list ,String pk)
	// {
	// for(ColumnModel model :list){
	// if(pk.toLowerCase().equals(model.getColumnName().toLowerCase()))
	// model.setIsPK(true);
	// }
	// }
	
	/**
	 * 根据表名取得表对象
	 * 
	 * @param tableName
	 */
	public TableModel getByTable(String tableName) throws CodegenException {
		tableName = tableName.toUpperCase();
		// 取得注释
		String tabComment = getTableComment(tableName);
		// String pk=getPk(tableName);
		
		TableModel tableModel = new TableModel();
		tableModel.setTableName(tableName);
		tableModel.setTabComment(tabComment);
		// 取得表的列表数据
		List<ColumnModel> list = getColumnsByTable(tableName);
		tableModel.setColumnList(list);
		
		return tableModel;
	}
	
	/**
	 * 取得数据库的所有表
	 */
	public List<String> getAllTable() throws CodegenException {
		
		DaoHelper<String> dao = new DaoHelper<String>(url, username, password);
		return dao.queryForList(sqlAllTables, new MapCmd<String>() {
			public String getObjecFromRs(ResultSet rs) throws SQLException {
				return rs.getString("TABNAME");
			}
		});
	}
	
	public static void main(String[] args) throws CodegenException {
		
		DB2Helper helper = new DB2Helper();
		helper.setUrl("jdbc:db2://192.168.1.17:50000/bpmx:currentSchema=BPMX380;", "db2admin", "123456");
		// String pk= helper.getPk("TEST");
		// System.out.println(helper.getAllTable().size());
		System.out.println(helper.getByTable("TEST1"));
		
	}
	
}

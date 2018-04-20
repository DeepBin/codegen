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
 * 取得数据库表接口IDbHelper，ORACLE 的实现
 *
 */
public class OracleHelper implements IDbHelper {

	// 取得主键
	private String sqlPk = "select column_name from user_constraints c,user_cons_columns col where c.constraint_name=col.constraint_name and c.constraint_type='P' and c.table_name='%s'";

	// 取得注释
	private String sqlTableComment = "select * from user_tab_comments  where table_type='TABLE' AND table_name ='%s'";

	// 取得列表
	private String sqlColumn = "select    A.column_name NAME,A.data_type TYPENAME,A.data_length LENGTH,A.data_precision PRECISION,    A.Data_Scale SCALE,A.Data_default, A.NULLABLE, B.comments DESCRIPTION "
			+ " from  user_tab_columns A,user_col_comments B where a.COLUMN_NAME=b.column_name and    A.Table_Name = B.Table_Name and A.Table_Name='%s' order by A.column_id";

	// 取得数据库所有表
	private String sqlAllTables = "select table_name from user_tables where status='VALID'";

	private String url;
	private String username;
	private String password;

	public OracleHelper() throws CodegenException {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			throw new CodegenException("找不到oracle驱动!", e);
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
		List<ColumnModel> list=dao.queryForList(sql, new OracleMapCmd());
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
				return rs.getString("COMMENTS");
			}
		});
		if(comment==null) comment=tableName;
		String[] aryComment=comment.split("\n");
		return aryComment[0];

	}
	
	/**
	 * 取得表的主键
	 * @param tableName
	 * @return
	 * @throws CodegenException
	 */
	private String getPk(String tableName) throws CodegenException
	{
		tableName=tableName.toUpperCase();
		String sql = String.format(sqlPk, tableName);
		DaoHelper<String> dao=new DaoHelper<String>(url, username, password);
		String pk="";
		try {
			pk= dao.queryForObject(sql,new MapCmd<String>() {
				public String getObjecFromRs(ResultSet rs) throws SQLException{
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
	 * @param list
	 * @param pk
	 */
	private void setPk(List<ColumnModel> list ,String pk)
	{
		for(ColumnModel model :list){
			if(pk.toLowerCase().equals(model.getColumnName().toLowerCase()))
				model.setIsPK(true);
		}
	}
	
	/**
	 * 根据表名取得表对象
	 * @param tableName
	 */
	public TableModel getByTable(String tableName) throws CodegenException {
		tableName=tableName.toUpperCase();
		//取得注释
		String tabComment=getTableComment(tableName);
		String pk=getPk(tableName);
		
		TableModel tableModel = new TableModel();
		tableModel.setTableName(tableName);
		tableModel.setTabComment(tabComment);
		//取得表的列表数据
		List<ColumnModel> list=getColumnsByTable(tableName);
		//设置主键
		if(StringUtil.isNotEmpty(pk)){
			setPk(list,pk);
		}
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
	
	public static void main(String[] args) throws CodegenException
	{
		
		OracleHelper helper=new OracleHelper();
		helper.setUrl("jdbc:oracle:thin:@localhost:1521:zyp", "zyp", "zyp");
		String pk= helper.getPk("TEST");
		

	}


}

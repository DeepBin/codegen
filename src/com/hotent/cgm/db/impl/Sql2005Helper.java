
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
 * 取得数据库表接口IDbHelper，SQL2005 的实现
 */
public class Sql2005Helper implements IDbHelper {
	
	private String url = "";
	
	private String username = "";
	
	private String password = "";
	
	private String sqlPk = "sp_pkeys N'%s'";
	
	/// <summary>
	/// 取得注释
	/// </summary>
	private String sqlTableComment = "select cast(b.value as varchar) comment from sys.tables a, sys.extended_properties b where a.type='U' and a.object_id=b.major_id and b.minor_id=0 and a.name='%s'";
	
	/// <summary>
	/// 取得列表
	/// </summary>
	private String sqlColumn = "select a.name, c.name typename, a.max_length length, a.is_nullable,a.precision,a.scale,"
			+ "(select count(*) from sys.identity_columns where sys.identity_columns.object_id = a.object_id and a.column_id = sys.identity_columns.column_id) as autoGen,"
			+ "(select cast(value as varchar) from sys.extended_properties where sys.extended_properties.major_id = a.object_id and sys.extended_properties.minor_id = a.column_id) as description"
			+ " from sys.columns a, sys.tables b, sys.types c where a.object_id = b.object_id and a.system_type_id=c.system_type_id and b.name='%s' and c.name<>'sysname' order by a.column_id";
	
	/// <summary>
	/// 取得数据库所有表
	/// </summary>
	private String sqlAllTables = "select name from sys.tables where type='U' and name<>'sysdiagrams'";
	
	public Sql2005Helper() throws CodegenException {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			throw new CodegenException("找不到sqlserver驱动!", e);
		}
	}
	
	@Override
	public void setUrl(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
		
	}
	
	/**
	 * 根据表明取得tablemodel对象
	 */
	@Override
	public TableModel getByTable(String tableName) throws CodegenException {
		String comment = getComment(tableName);
		String pk = getPk(tableName);
		TableModel tableModel = new TableModel();
		tableModel.setTableName(tableName);
		tableModel.setTabComment(comment);
		
		List<ColumnModel> colList = getColumnsByTable(tableName);
		// 设置主键
		if (StringUtil.isNotEmpty(pk)) {
			setPk(colList, pk);
		}
		tableModel.setColumnList(colList);
		return tableModel;
	}
	
	private void setPk(List<ColumnModel> list, String pk) {
		for (ColumnModel model : list) {
			if (pk.toLowerCase().equals(model.getColumnName().toLowerCase()))
				model.setIsPK(true);
		}
	}
	
	/**
	 * 根据表名取得表的列数据
	 * 
	 * @param tableName
	 * @return
	 * @throws CodegenException
	 */
	private List<ColumnModel> getColumnsByTable(String tableName) throws CodegenException {
		DaoHelper<ColumnModel> dao = new DaoHelper<ColumnModel>(this.url, this.username, this.password);
		String sql = String.format(sqlColumn, tableName);
		List<ColumnModel> list = dao.queryForList(sql, new Sql2005MapCmd());
		return list;
	}
	
	/**
	 * 取得当前数据库中的所有表名
	 */
	public List<String> getAllTable() throws CodegenException {
		DaoHelper<String> dao = new DaoHelper<String>(this.url, this.username, this.password);
		List<String> list = dao.queryForList(sqlAllTables, new MapCmd<String>() {
			@Override
			public String getObjecFromRs(ResultSet rs) throws SQLException {
				return rs.getString("name");
			}
		});
		return list;
	}
	
	/**
	 * 取得表注释
	 * 
	 * @param tableName
	 * @return
	 * @throws CodegenException
	 */
	private String getComment(String tableName) throws CodegenException {
		DaoHelper<String> dao = new DaoHelper<String>(this.url, this.username, this.password);
		String sql = String.format(sqlTableComment, tableName);
		String comment = dao.queryForObject(sql, new MapCmd<String>() {
			public String getObjecFromRs(ResultSet rs) throws SQLException {
				return rs.getString("comment");
			}
		});
		comment = (comment == null) ? tableName : comment;
		
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
		DaoHelper<String> dao = new DaoHelper<String>(this.url, this.username, this.password);
		String sql = String.format(sqlPk, tableName);
		System.out.println(sql);
		String columnName = dao.queryForObject(sql, new MapCmd<String>() {
			@Override
			public String getObjecFromRs(ResultSet rs) throws SQLException {
				
				return rs.getString("column_name");
			}
		});
		return columnName;
	}
	
	public static void main(String[] args) throws CodegenException {
		Sql2005Helper helper = new Sql2005Helper();
		helper.setUrl("jdbc:sqlserver://192.168.1.111:1433; DatabaseName=gzrs", "sa", "sasa");
		List<ColumnModel> list = helper.getColumnsByTable("Doc_ArchivesResource");
	}
}

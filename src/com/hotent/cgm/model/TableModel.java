package com.hotent.cgm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据表对象
 * 
 * @author hotent
 * 
 */
public class TableModel {

	// 表名
	private String tableName;
	// 表注释
	private String tabComment;
	//外键关联
	private String foreignKey = "";
	
	private Map<String, String> variables=new HashMap<String, String>();

	// 表所有的列对象列表
	private List<ColumnModel> columnList = new ArrayList<ColumnModel>();

	// 子表数据
	private List<TableModel> subTableList = new ArrayList<TableModel>();
	//是否是子表
	private boolean sub;

	/**
	 * 表名
	 * 
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 表注释
	 * 
	 * @return
	 */
	public String getTabComment() {
		return tabComment.replaceAll("\r\n", "");
	}

	public void setTabComment(String tabComment) {
		this.tabComment = tabComment;
	}
	

	public boolean isSub() {
		return sub;
	}

	public void setSub(boolean sub) {
		this.sub = sub;
	}
	
	public boolean getSub(){
		return sub;
	}

	/**
	 * 表的数据库列
	 * 
	 * @return
	 */
	public List<ColumnModel> getColumnList() {
		return columnList;
	}
	
	/**
	 * 取得主键列表
	 * @return
	 */
	public List<ColumnModel> getPkList()
	{
		List<ColumnModel> list=new ArrayList<ColumnModel>();
		for(ColumnModel col :columnList){
			if(col.getIsPK())
				list.add(col);
		}
		return list;
	}
	
	/**
	 * 取得主键对象。
	 * @return
	 */
	public ColumnModel getPkModel()
	{
		for(ColumnModel col :columnList){
			if(col.getIsPK()){
				return col;
			}
		}
		return null;
	}
	
	/**
	 * 取得普通列的列表
	 * @return
	 */
	public List<ColumnModel> getCommonList()
	{
		List<ColumnModel> list=new ArrayList<ColumnModel>();
		for(ColumnModel col :columnList){
			if(!col.getIsPK())
				list.add(col);
		}
		return list;
	}
	

	public void setColumnList(List<ColumnModel> columnList) {
		this.columnList = columnList;
	}

	/**
	 * 子表数据
	 * 
	 * @return
	 */
	public List<TableModel> getSubTableList() {
		return subTableList;
	}

	public void setSubTableList(List<TableModel> subTableList) {
		this.subTableList = subTableList;
	}
	
	/**
	 * 相对于主表的外键
	 * @return
	 */
	public String getForeignKey() {
		return foreignKey;
	}
	
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}
	
	
	public Map<String, String> getVariables() {
		return variables;
	}


	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}

	@Override
	public String toString() {
		return "TableModel [tableName=" + tableName + ", tabComment=" + tabComment + ", foreignKey=" + foreignKey + ", variables=" + variables
				+ ", columnList=" + columnList + ", subTableList=" + subTableList + ", sub=" + sub + "]";
	}
	
	
	
	

}

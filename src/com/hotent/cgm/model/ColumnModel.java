package com.hotent.cgm.model;

/**
 * 数据库列对象。<br>
 * 包括列名，列注释，列类型，数据库类型，主键，字段长度。
 * @author hotent
 *
 */
public class ColumnModel {

	// 列名
	private String columnName = "";
	// 列注释
	private String comment = "";
	// 列类型
	private String colType = "";
	// 列数据库类型
	private String colDbType = "";
	// 是否主键
	private boolean isPK = false;
	// 字段长度
	private long length = 0;
	// 精度
	private int precision = 0;
	// 小数位
	private int scale = 0;
	// 自增长字段
	private int autoGen = 0;
	//不为空
	private boolean isNotNull = false;
	
	private String displayDbType="";
	
	/**
	 * 列名称。<br>
	 * 在模版中的使用方法：<br>
	 * 
	 * <#list model.columnList as columnModel><br>
	 *&nbsp;&nbsp;&nbsp;&nbsp;${columnModel.columnName}<br>
	 * &lt;/#list>
	 * 
	 * @return
	 */
	public String getColumnName() {
		return columnName;
	}
	
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	
	
	/**
	 * 列注释。<br>
	 * 在模版中使用方法：<br>
	 * 
	 * <#list model.columnList as columnModel><br>
	 *&nbsp;&nbsp;&nbsp;&nbsp;		${columnModel.comment}<br>
	 * &lt;/#list>
	 * 
	 * @return
	 */
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * 列类型。<br>
	 * 在模版中使用方法：<br>
	 * 
	 * <#list model.columnList as columnModel><br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;		${columnModel.colType}<br>
	 * &lt;/#list>
	 * 
	 * @return
	 */
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	

	/**
	 * 数据库实际的列类型。<br>
	 * 在模版中使用方法：<br>
	 * 
	 * <#list model.columnList as columnModel><br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;		${columnModel.colDbType}<br>
	 * &lt;/#list>
	 * 
	 * @return
	 */
	public String getColDbType() {
		return colDbType;
	}
	public void setColDbType(String colDbType) {
		this.colDbType = colDbType;
	}
	
	/**
	 * 是否是主键。<br>
	 * 在模版中使用方法：<br>
	 * 
	 * <#list model.columnList as columnModel><br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;		<#if !columnModel.isPK><br>
	 *
	 * &nbsp;&nbsp;&nbsp;&nbsp;		&lt;/#if><br>
	 * &lt;/#list>
	 * 
	 * @return
	 */
	public boolean getIsPK() {
		return isPK;
	}
	public void setIsPK(boolean isPK) {
		this.isPK = isPK;
	}
	
	/**
	 * 数据库列的长度，字符串列类型长度。<br>
	 * 在模版中使用方法：<br>
	 * 
	 * <#list model.columnList as columnModel><br>
	 *&nbsp;&nbsp;&nbsp;&nbsp;		${columnModel.length}<br>
	 * &lt;/#list><br>
	 * 
	 * @return
	 */
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	
	/**
	 * 数字列精度。<br>
	 * 在模版中使用方法：<br>
	 * 
	 * <#list model.columnList as columnModel><br>
	 *	&nbsp;&nbsp;&nbsp;&nbsp;	${columnModel.precision}<br>
	 * &lt;/#list><br>
	 * 
	 * @return
	 */
	public int getPrecision() {
		return precision;
	}
	
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	
	/**
	 * 数字列小数的位数。<br>
	 * 在模版中使用方法：<br>
	 * 
	 * <#list model.columnList as columnModel><br>
	 *	&nbsp;&nbsp;&nbsp;&nbsp;	${columnModel.scale}<br>
	 * &lt;/#list><br>
	 * 
	 * @return
	 */
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	/**
	 * 字段自增长（适用SQL2005) 1,为自增长。<br>
	 * 在模版中使用方法：<br>
	 * 
	 * 
	 * <#list model.columnList as columnModel><br>
	 *	&nbsp;&nbsp;&nbsp;&nbsp;	${columnModel.autoGen}<br>
	 * &lt;/#list>
	 * 
	 * @return
	 */
	public int getAutoGen() {
		return autoGen;
	}
	public void setAutoGen(int autoGen) {
		this.autoGen = autoGen;
	}
	
	/**
	 * 字段是否非空。<br>
	 * 在模版中使用方法：<br>
	 * 
	 * <#list model.columnList as columnModel><br>
	 *	&nbsp;&nbsp;&nbsp;&nbsp;	<#if !columnModel.isNotNull><br>
	 *		&lt;/#if><br>
	 * &lt;/#list>
	 * 
	 * @return
	 */
	public boolean getIsNotNull() {
		return isNotNull;
	}
	public void setIsNotNull(boolean isNotNull) {
		this.isNotNull = isNotNull;
	}
	
	/**
	 * 显示的字段类型。<br>
	 * 在模版中使用方法：<br>
	 * 
	 * <#list model.columnList as columnModel><br>
	 *	&nbsp;&nbsp;&nbsp;&nbsp;	<#if !columnModel.displayDbType><br>
	 *		&lt;/#if><br>
	 * &lt;/#list>
	 * 
	 * @return
	 */
	public String getDisplayDbType() {
		return displayDbType;
	}
	public void setDisplayDbType(String displayDbType) {
		this.displayDbType = displayDbType;
	}
	
	/**
	 * 测试。<br>
	 * <pre>
	 * 	&lt;var>
	 *		&lt;var-name>regex&lt;/var-name>
	 *		&lt;var-value>正则表达式&lt;/var-value>
	 *	&lt;/var>
	 * </pre>
	 * @return
	 */
	public String getDisplay()
	{
		return "";
	}


	@Override
	public String toString() {
		return "ColumnModel [columnName=" + columnName + ", comment=" + comment + ", colType=" + colType + ", colDbType=" + colDbType + ", isPK=" + isPK
				+ ", length=" + length + ", precision=" + precision + ", scale=" + scale + ", autoGen=" + autoGen + ", isNotNull=" + isNotNull
				+ ", displayDbType=" + displayDbType + "]";
	}
	
	
}

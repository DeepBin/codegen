
package com.hotent.cgm.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hotent.cgm.db.MapCmd;
import com.hotent.cgm.model.ColumnModel;
import com.hotent.cgm.util.StringUtil;

/**
 * 功能 ：根据rs对象构建columnmodel对象，MYSQL实现类。
 */
public class MySqlMapCmd implements MapCmd<ColumnModel> {
	
	/**
	 * 构建columnmodel对象
	 */
	@Override
	public ColumnModel getObjecFromRs(ResultSet rs) throws SQLException {
		// column_name,is_nullable,data_type,character_maximum_length,numeric_precision,
		// NUMERIC_scale,column_type,column_key,column_comment from information_schema.columns
		ColumnModel model = new com.hotent.cgm.model.ColumnModel();
		String colName = rs.getString("column_name");
		String column_key = rs.getString("column_key");
		long character_length = rs.getLong("character_octet_length");
		String nullable = rs.getString("is_nullable");
		boolean is_nullable = nullable.equals("YES");
		int precision = rs.getInt("numeric_precision");
		int scale = rs.getInt("NUMERIC_scale");
		String comment = rs.getString("column_comment");
		comment = StringUtil.isEmpty(comment) ? colName : comment;
		boolean isPk = column_key.equals("PRI");
		String data_type = rs.getString("data_type");
		
		String javaType = getJavaType(data_type, precision, scale);
		String displayDbType = getDisplayDbType(data_type, character_length, precision, scale);
		
		String[] aryComment = comment.split("\n");
		
		model.setColumnName(colName);
		model.setIsNotNull(!is_nullable);
		model.setPrecision(precision);
		model.setScale(scale);
		model.setLength(character_length);
		model.setComment(aryComment[0]);
		model.setIsPK(isPk);
		model.setColDbType(data_type);
		model.setColType(javaType);
		model.setDisplayDbType(displayDbType);
		
		return model;
	}
	
	/**
	 * 取得显示的数据库字段类型
	 * 
	 * @param dbtype
	 * @param character_length
	 * @param precision
	 * @param scale
	 * @return
	 */
	private String getDisplayDbType(String dbtype, long character_length, int precision, int scale) {
		if (dbtype.equals("varchar"))
			return "varchar(" + character_length + ")";
		if (dbtype.equals("decimal"))
			return "decimal(" + precision + "," + scale + ")";
		
		return dbtype;
	}
	
	/**
	 * 取得字段的java类型
	 * 
	 * @param dbtype
	 * @param precision
	 * @param scale
	 * @return
	 */
	private String getJavaType(String dbtype, int precision, int scale) {
		if (dbtype.equals("bigint"))
			return "Long";
		if (dbtype.equals("int"))
			return "Integer";
		if (dbtype.equals("tinyint") || dbtype.equals("smallint"))
			return "Short";
		if (dbtype.equals("varchar") || dbtype.endsWith("text"))
			return "String";
		
		if (dbtype.equals("varchar") || dbtype.endsWith("text"))
			return "String";
		
		if (dbtype.equals("double"))
			return "Double";
		
		if (dbtype.equals("float"))
			return "Float";
		
		if (dbtype.endsWith("blob"))
			return "byte[]";
		
		if (dbtype.equals("decimal")) {
			if (scale == 0) {
				if (precision <= 10)
					return "Integer";
				else return "Long";
			} else {
				return "Double";
			}
		}
		
		if (dbtype.startsWith("date")) { return "java.util.Date"; }
		
		return dbtype;
	}
	
}

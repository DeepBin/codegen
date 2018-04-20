
package com.hotent.cgm.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hotent.cgm.db.MapCmd;
import com.hotent.cgm.model.ColumnModel;
import com.hotent.cgm.util.StringUtil;

/**
 * 功能 ：根据rs对象构建columnmodel对象，Oracle实现类。
 * 
 * @author hotent
 */
public class DB2MapCmd implements MapCmd<ColumnModel> {
	
	@Override
	public ColumnModel getObjecFromRs(ResultSet rs) throws SQLException {
		ColumnModel model = new ColumnModel();
		String name = rs.getString("COLNAME");
		String typename = rs.getString("TYPENAME");
		int length = Integer.parseInt(rs.getString("LENGTH"));
		int precision = length;
		int scale = rs.getInt("SCALE");
		String description = rs.getString("REMARKS");
		int primaryKey = string2Int(rs.getString("KEYSEQ"), 0);
		description = (description == null) ? name : description;
		String NULLABLE = rs.getString("NULLS");
		
		String displayDbType = getDisplayDbType(typename, length, precision, scale);
		String javaType = getJavaType(typename, precision, scale);
		
		boolean isNotNull = "N".equalsIgnoreCase(NULLABLE);
		
		model.setColumnName(name);
		model.setColDbType(typename);
		model.setComment(description);
		model.setIsNotNull(isNotNull);
		model.setLength(length);
		model.setPrecision(length);
		model.setScale(scale);
		model.setDisplayDbType(displayDbType);
		model.setColType(javaType);
		model.setIsPK(primaryKey > 0 ? true : false);
		return model;
	}
	
	private String getDisplayDbType(String dbtype, long length, int precision, int scale) {
		if ("CHAR".equalsIgnoreCase(dbtype) || "VARCHAR".equalsIgnoreCase(dbtype)
				|| "LONG VARCHAR".equalsIgnoreCase(dbtype)) {
			
			return dbtype + "(" + length + ")";
		} else if ("DECIMAL".equalsIgnoreCase(dbtype)) {
			
			return "DECIMAL(" + (precision - scale) + "," + scale + ")";
		} else if ("BIGINT".equalsIgnoreCase(dbtype) || "DOUBLE".equalsIgnoreCase(dbtype)
				|| "INTEGER".equalsIgnoreCase(dbtype) || "REAL".equalsIgnoreCase(dbtype)
				|| "SMALLINT".equalsIgnoreCase(dbtype)) {
			
			return dbtype;
		} else {
			return dbtype;
		}
		
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
		dbtype = dbtype.toUpperCase();
		if ("BLOB".equals(dbtype) || "GRAPHIC".equals(dbtype) || "LONG VARGRAPHIC".equals(dbtype)
				|| "VARGRAPHIC".equals(dbtype)) {
			return "byte[]";
		} else if ("CLOB".equals(dbtype) || "XML".equals(dbtype) || "DBCLOB".equals(dbtype)) {
			return "String";
		} else if ("CHARACTER".equals(dbtype) || "LONG VARCHAR".equals(dbtype) || "VARCHAR".equals(dbtype)) {
			return "String";
		} else if ("TIMESTAMP".equals(dbtype) || "TIME".equals(dbtype) || "DATE".equals(dbtype)) {
			return "java.util.Date";
		} else if ("BIGINT".equalsIgnoreCase(dbtype)) {
			return "Long";
		} else if ("INTEGER".equalsIgnoreCase(dbtype) || "SMALLINT".equalsIgnoreCase(dbtype)) {
			return "Integer";
		} else if ("DOUBLE".equalsIgnoreCase(dbtype) || "REAL".equalsIgnoreCase(dbtype)) {
			return "Double";
		} else if (dbtype.indexOf("DECIMAL") > 0) {
			return "Double";
		} else {
			return "String";
		}
	}
	
	/**
	 * String到Int的类型转换，如果转换失败，返回默认值。
	 * 
	 * @param str
	 *            要转换的String类型的值
	 * @param def
	 *            默认值
	 * @return
	 */
	private int string2Int(String str, int def) {
		int retval = def;
		if (StringUtil.isNotEmpty(str)) {
			try {
				retval = Integer.parseInt(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retval;
	}
	
}

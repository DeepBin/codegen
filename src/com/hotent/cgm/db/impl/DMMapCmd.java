
package com.hotent.cgm.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hotent.cgm.db.MapCmd;
import com.hotent.cgm.model.ColumnModel;

/**
 * 功能 ：根据rs对象构建columnmodel对象，达梦 实现类。
 * 
 * @author hotent
 */
public class DMMapCmd implements MapCmd<ColumnModel> {
	
	@Override
	public ColumnModel getObjecFromRs(ResultSet rs) throws SQLException {
		ColumnModel model = new ColumnModel();
		String name = rs.getString("NAME");
		String typename = rs.getString("TYPENAME");
		int length = Integer.parseInt(rs.getString("LENGTH"));
		int precision = rs.getInt("PRECISION");
		int scale = rs.getInt("SCALE");
		String description = rs.getString("DESCRIPTION");
		description = (description == null) ? name : description;
		String NULLABLE = rs.getString("NULLABLE");
		
		String displayDbType = getDisplayDbType(typename, length, precision, scale);
		String javaType = getJavaType(typename, precision, scale);
		
		boolean isNotNull = "N".equals(NULLABLE);
		
		model.setColumnName(name);
		model.setColDbType(typename);
		model.setComment(description);
		model.setIsNotNull(isNotNull);
		model.setLength(length);
		model.setPrecision(precision);
		model.setScale(scale);
		model.setDisplayDbType(displayDbType);
		model.setColType(javaType);
		
		return model;
	}
	
	private String getDisplayDbType(String type, long character_length, int precision, int scale) {
		String dbtype = type.trim().toUpperCase();
		if (dbtype.indexOf("CHAR") > -1) {
			return dbtype + "(" + character_length + ")";
		} else if (dbtype.equals("NUMBER")) {
			if (scale == 0 && precision > 0) {
				return "NUMBER(" + precision + ")";
			} else {
				return "NUMBER(" + precision + "," + scale + ")";
			}
		}
		
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
	private String getJavaType(String type, int precision, int scale) {
		String dbtype = type.trim().toUpperCase();
		if (dbtype.indexOf("BLOB") != -1)
			return "byte[]";
		if ((dbtype.indexOf("CHAR") != -1) || (dbtype.indexOf("CLOB") != -1))
			return "String";
		
		if (dbtype.indexOf("DATE") != -1)
			return "java.util.Date";
		
		if (dbtype.equals("NUMBER")) {
			if (scale > 0) {
				return "Float";
			} else if (precision < 10) {
				return "Integer";
			} else {
				return "Long";
			}
		}
		
		return "String";
	}
	
}

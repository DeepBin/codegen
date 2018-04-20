
package com.hotent.cgm.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hotent.cgm.db.MapCmd;
import com.hotent.cgm.model.ColumnModel;

/**
 * ���� �����rs���󹹽�columnmodel����SQL2005ʵ���ࡣ
 * 
 * @author hotent
 */
public class Sql2005MapCmd implements MapCmd<ColumnModel> {
	
	@Override
	public ColumnModel getObjecFromRs(ResultSet rs) throws SQLException {
		ColumnModel model = new ColumnModel();
		String name = rs.getString("name");
		String typename = rs.getString("typename");
		long length = rs.getLong("length");
		int nullable = rs.getInt("is_nullable");
		int precision = rs.getInt("precision");
		int scale = rs.getInt("scale");
		int autogen = rs.getInt("autoGen");
		String comment = rs.getString("description");
		comment = (comment == null) ? name : comment;
		boolean isNotnull = (nullable == 0);
		
		String displayDbType = getDisplayDbType(typename, length, precision, scale);
		String javatype = getJavaType(typename, precision, scale);
		
		model.setColumnName(name);
		model.setComment(comment);
		model.setAutoGen(autogen);
		model.setIsNotNull(isNotnull);
		model.setColDbType(typename);
		model.setLength(length);
		model.setPrecision(precision);
		model.setScale(scale);
		model.setDisplayDbType(displayDbType);
		model.setColType(javatype);
		
		return model;
	}
	
	private String getJavaType(String dbtype, int precision, int scale) {
		if (dbtype.equals("int"))
			return "Integer";
		if (dbtype.equals("bigint"))
			return "Long";
		if (dbtype.equals("smallint") || dbtype.equals("tinyint"))
			return "Short";
		if (dbtype.equals("bit"))
			return "Boolean";
		
		if (dbtype.indexOf("char") != -1 || dbtype.endsWith("text") || dbtype.equals("xml"))
			return "String";
		
		if (dbtype.equals("double") || dbtype.equals("money") || dbtype.equals("real"))
			return "Double";
		
		if (dbtype.equals("float"))
			return "Float";
		
		if (dbtype.endsWith("image"))
			return "byte[]";
		
		if (dbtype.equals("decimal") || dbtype.equals("numeric")) {
			if (scale == 0) {
				if (precision <= 10)
					return "Integer";
				else return "Long";
			} else {
				return "Double";
			}
		}
		
		if (dbtype.startsWith("datetime") || dbtype.equals("date")) { return "java.util.Date"; }
		
		return dbtype;
	}
	
	private String getDisplayDbType(String dbtype, long length, int precision, int scale) {
		if (dbtype.equals("xml"))
			return "xml";
		
		if (dbtype.indexOf("char") != -1) {
			if (length == -1) {
				return dbtype + "(max)";
			} else {
				return dbtype + "(" + length + ")";
			}
		}
		if (dbtype.equals("decimal"))
			return "decimal(" + precision + "," + scale + ")";
		
		return dbtype;
	}
}

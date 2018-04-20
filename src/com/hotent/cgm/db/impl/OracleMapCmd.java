package com.hotent.cgm.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hotent.cgm.db.MapCmd;
import com.hotent.cgm.model.ColumnModel;


/**
 * ���� �����rs���󹹽�columnmodel����Oracleʵ���ࡣ
 * @author hotent
 *
 */
public class OracleMapCmd implements MapCmd<ColumnModel> {

	@Override
	public ColumnModel getObjecFromRs(ResultSet rs) throws SQLException {
		ColumnModel model=new ColumnModel();
		String name = rs.getString("NAME");
		String typename = rs.getString("TYPENAME");
		int length = Integer.parseInt(rs.getString("LENGTH"));
		int precision = rs.getInt("PRECISION");
		int scale = rs.getInt("SCALE");
		String description = rs.getString("DESCRIPTION");
		description=(description==null) ?name :description;
		String NULLABLE=rs.getString("NULLABLE");
		
		String displayDbType=getDisplayDbType(typename,length,precision,scale);
		String javaType=getJavaType(typename, precision, scale);
		boolean isNotNull =  "N".equals(NULLABLE);
		
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
	
	
	private String getDisplayDbType(String dbtype ,long character_length,int precision,int scale )
	{
		if(dbtype.equals("CHAR") || dbtype.equals("VARCHAR2"))
			return  dbtype+  "(" + character_length +")";
		if( dbtype.equals("NVARCHAR2"))
			return "NVARCHAR2(" + character_length /2 +")";
		
		if(dbtype.equals("NUMBER"))
		{
			if(scale==0 && precision>0)
				return "NUMBER(" +precision +")";
			else
				return  "NUMBER(" +  precision  +"," +scale +")";
		}
		
		return dbtype;
	}
	
	/**
	 * ȡ���ֶε�java����
	 * @param dbtype
	 * @param precision
	 * @param scale
	 * @return
	 */
	private String getJavaType(String dbtype,int precision,int scale)
	{
		if(dbtype.equals("BLOB"))
			return "byte[]";
		if((dbtype.indexOf("CHAR")!=-1) || (dbtype.indexOf("CLOB")!=-1) )
			return "String";
		
		if(dbtype.equals("DATE")||dbtype.indexOf("TIMESTAMP")!=-1)
			return "java.util.Date";
		
		if (dbtype.equals("NUMBER")) {
			if (scale > 0) {
				return "Float" ;
			} else if (precision < 10) {
				return "Integer";
			} else {
				return "Long";
			}
		}
//		if(dbtype.equals("SMALLINT")||dbtype.indexOf("SMALLINT")!=-1){
//			return "Short";
//		}
		
		return "String";
	}

}

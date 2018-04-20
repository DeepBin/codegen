package com.hotent.cgm.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hotent.cgm.db.MapCmd;
import com.hotent.cgm.model.ColumnModel;
import com.hotent.cgm.util.StringUtil;


/**
 * 功能 ：根据rs对象构建columnmodel对象，H2实现类。
 * @author hotent
 *
 */
public class H2MapCmd implements MapCmd<ColumnModel> {

	@Override
	public ColumnModel getObjecFromRs(ResultSet rs) throws SQLException {
		ColumnModel model=new ColumnModel();
		
		String name = rs.getString("COLUMN_NAME");
		String is_nullable = rs.getString("IS_NULLABLE");
		String data_type = rs.getString("TYPE_NAME");
		String length = rs.getString("LENGTH");
		String precisions = rs.getString("PRECISIONS");
		String scale = rs.getString("SCALE");
		String column_list = rs.getString("COLUMN_LIST");
		String column_comment = rs.getString("REMARKS");
//		String table_name = rs.getString("TABLE_NAME");
		int iLength=string2Int(length,0);
		int iPrecisions = string2Int(precisions,0);
		int iScale = string2Int(scale,0);
		
		String displayDbType=getDisplayDbType(data_type,iLength,iPrecisions,iScale);
		String javaType=getJavaType(data_type, iPrecisions, iScale);

		boolean isNotNull =  "NO".equalsIgnoreCase(is_nullable);
		
		model.setColumnName(name);
		model.setColDbType(data_type);
		model.setComment(column_comment);
		model.setIsNotNull(isNotNull);
		model.setLength(iLength);
		model.setPrecision(iPrecisions);
		model.setScale(iScale);
		model.setDisplayDbType(displayDbType);
		model.setColType(javaType);
		boolean isPkColumn=false;
		if (StringUtil.isNotEmpty(column_list)){
			String[] pkColumns=column_list.split(",");
			for(String pkColumn:pkColumns){
				if(name.trim().equalsIgnoreCase(pkColumn.trim())){
					isPkColumn=true;
					break;
				}
			}
		}
		model.setIsPK(isPkColumn);
		return model;
	}
	
	
	private String getDisplayDbType(String dbtype ,long length,int precision,int scale )
	{
		String type=dbtype.toUpperCase();
		if(dbtype.indexOf("CHAR")>0){
			return  type +  "(" + length +")";
		}else if("DECIMAL".equalsIgnoreCase(type)
				||"NUMBER".equalsIgnoreCase(type)
				||"DEC".equalsIgnoreCase(type)
				||"NUMERIC".equalsIgnoreCase(type)){
			return type+ "(" +  (precision-scale)  +"," +scale +")";
		}else{
			return type;
		}
	}
	
	/**
	 * 取得字段的java类型
	 * @param dbtype
	 * @param precision
	 * @param scale
	 * @return
	 */
	private String getJavaType(String dbtype,int precision,int scale)
	{
		dbtype=dbtype.toUpperCase();
		if("BLOB".equals(dbtype)
			||"TINYBLOB".equals(dbtype)
			||"MEDIUMBLOB".equals(dbtype)
			||"LONGBLOB".equals(dbtype)
			||"IMAGE".equals(dbtype)
			||"OID".equals(dbtype)
			||"BINARY".equals(dbtype)
			||"VARBINARY".equals(dbtype)
			||"LONGVARBINARY".equals(dbtype)
			||"RAW".equals(dbtype)
			||"BYTEA".equals(dbtype)){
			return "byte[]";
		}else if("TIMESTAMP".equals(dbtype)
				||"TIME".equals(dbtype)
				||"DATE".equals(dbtype)
				||"DATETIME".equals(dbtype)
				||"SMALLDATETIME".equals(dbtype)){
			return "java.util.Date";
		}else if ("BIGINT".equalsIgnoreCase(dbtype)
				||"INT8".equalsIgnoreCase(dbtype)
				||"IDENTITY".equalsIgnoreCase(dbtype)){
			return "Long";
		}else if("INTEGER".equalsIgnoreCase(dbtype)
				||"INT".equalsIgnoreCase(dbtype)
				||"MEDIUMINT".equalsIgnoreCase(dbtype)
				||"INT4".equalsIgnoreCase(dbtype)
				||"SIGNED".equalsIgnoreCase(dbtype)
				||"SMALLINT".equalsIgnoreCase(dbtype)
				||"YEAR".equalsIgnoreCase(dbtype)
				||"INT2".equalsIgnoreCase(dbtype)
				||"TINYINT".equalsIgnoreCase(dbtype)){
			return "Integer";
		}else if("DOUBLE".equalsIgnoreCase(dbtype)
				||"FLOAT".equalsIgnoreCase(dbtype)
				||"FLOAT4".equalsIgnoreCase(dbtype)
				||"FLOAT8".equalsIgnoreCase(dbtype)
				||"DECIMAL".equalsIgnoreCase(dbtype)
				||"NUMBER".equalsIgnoreCase(dbtype)
				||"DEC".equalsIgnoreCase(dbtype)
				||"NUMERIC".equalsIgnoreCase(dbtype)
				||"REAL".equalsIgnoreCase(dbtype)){
			return "Double";
		}else{
			return "String";
		}
	}
	/**
	 * String到Int的类型转换，如果转换失败，返回默认值。
	 * @param str 要转换的String类型的值
	 * @param def 默认值
	 * @return
	 */
	private int string2Int(String str,int def){
		int retval=def;
		if(StringUtil.isNotEmpty(str)){
			try{
				retval=Integer.parseInt(str);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retval;
	}

}

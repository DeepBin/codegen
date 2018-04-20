
package com.hotent.cgm.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSet 对象映射接口，用户可以通过类或匿名类进行实现。
 * 
 * @author hotent
 * @param <T>
 */
public interface MapCmd<T> {
	
	/**
	 * 根据ResultSet 记录集对象返回对象T 。
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public T getObjecFromRs(ResultSet rs) throws SQLException;
	
}

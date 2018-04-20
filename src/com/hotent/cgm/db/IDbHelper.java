
package com.hotent.cgm.db;

import java.util.List;

import com.hotent.cgm.exception.CodegenException;
import com.hotent.cgm.model.TableModel;

/**
 * ��ȡ���ݿ����б�ͱ��TableMode�ӿ��ࡣ
 * 
 * @author hotent
 */
public interface IDbHelper {
	
	/**
	 * ����URL,username,password
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	void setUrl(String url, String username, String password);
	
	/**
	 * ���ݱ���ȡ��TableModel
	 * 
	 * @param tableName
	 * @return
	 */
	TableModel getByTable(String tableName) throws CodegenException;
	
	/**
	 * ȡ�����еı���
	 * 
	 * @return
	 */
	List<String> getAllTable() throws CodegenException;
}

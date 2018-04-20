
package com.hotent.cgm.util;

import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XmlUtil {
	/**
	 * 根据给定的schema校验xml,并返回是否校验成功.
	 * 
	 * <pre>
	 * 成功返回空串。
	 * 失败返回 错误信息。
	 * </pre>
	 * 
	 * @param xsdPath
	 *            schema文件路径
	 * @param xmlPath
	 *            待验证的xsd文件路径。
	 * @return
	 */
	public static String validXmlBySchema(String xsdPath, String xmlPath) {
		// 建立schema工厂
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		
		// 建立验证文档文件对象，利用此文件对象所封装的文件进行schema验证
		File schemaFile = new File(xsdPath);
		
		// 利用schema工厂，接收验证文档文件对象生成Schema对象
		Schema schema = null;
		try {
			schema = schemaFactory.newSchema(schemaFile);
		} catch (SAXException e) {
			e.printStackTrace();
		}
		// 通过Schema产生针对于此Schema的验证器，利用schenaFile进行验证
		Validator validator = schema.newValidator();
		
		// 得到验证的数据源
		Source source = new StreamSource(FileHelper.getInputStream(xmlPath));
		
		try {
			validator.validate(source);
		} catch (Exception ex) {
			return ex.getMessage();
		}
		return "";
	}
}

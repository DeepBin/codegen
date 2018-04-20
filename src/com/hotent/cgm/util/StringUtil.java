package com.hotent.cgm.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hotent.cgm.exception.CodegenException;

public class StringUtil {
	
	public static boolean isEmpty(String str)
	{
		if(str==null) return true;
		if(str.trim().equals("")) return true;
		return false;
	}
	
	public static boolean isNotEmpty(String str)
	{
		
		return !isEmpty(str);
	}
	
	/**
	 * 替换标量。<br>
	 * <pre>
	 * 使用方法如下：
	 * String template="com/hotent/{path}/model/{class}";
	 * Map<String,String> map=new HashMap<String,String>();
	 * map.put("path","platform");
	 * map.put("class","Role");
	 * String tmp=replaceVariable(template,map);
	 * </pre>
	 * @param template
	 * @param map
	 * @return
	 * @throws CodegenException 
	 */
	public static String replaceVariable(String template,Map<String,String> map) throws CodegenException
	{
		Pattern regex = Pattern.compile("\\{(.*?)\\}");
		Matcher regexMatcher = regex.matcher(template);
		while (regexMatcher.find()) {
			String key=regexMatcher.group(1);
			String toReplace=regexMatcher.group(0);
			String value=(String)map.get(key);
			if(value!=null)
				template=template.replace(toReplace, value);
			else
				throw new CodegenException("没有找到["+ key +"]对应的变量值，请检查表变量配置!");
		}  
		
		return template;
	}
	
	/**
	 * 替换表变量
	 * @param template
	 * @param tableName
	 * @return
	 */
	public static String replaceVariable(String template,String tableName) 
	{
		Pattern regex = Pattern.compile("\\{(.*?)\\}");
		Matcher regexMatcher = regex.matcher(template);
		if (regexMatcher.find()) {
			String toReplace=regexMatcher.group(0);
			template=template.replace(toReplace, tableName);
		}
		return template;
	}
	
	/**
	 * 去掉前面的字符
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trimPrefix(String toTrim,String trimStr)
	{
		while(toTrim.startsWith(trimStr))
		{
			toTrim=toTrim.substring(trimStr.length());
		}
		return toTrim;
	}
	
	/**
	 * 删除后面的字符
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trimSufffix(String toTrim,String trimStr)
	{
		while(toTrim.endsWith(trimStr))
		{
			toTrim=toTrim.substring(0,toTrim.length()-trimStr.length());
		}
		return toTrim;
	}
	
	/**
	 * 删除指定的字符
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trim(String toTrim,String trimStr)
	{
		return trimSufffix(trimPrefix(toTrim, trimStr), trimStr);
	}
	
	public static String combilePath(String baseDir,String dir)
	{
		baseDir=trimSufffix(baseDir, File.separator) ;
		dir=trimPrefix(dir,File.separator);
		
		return baseDir + File.separator + dir;
	}
	
	/**
	 * 使用字符换替换内容
	 * @param content 内容
	 * @param startTag 开始标签
	 * @param endTag 结束标签
	 * @param repalceWith 使用替换
	 * @return
	 */
	public static String replace(String content,String startTag,String endTag,String repalceWith)
	{
		String tmp=content.toLowerCase();
		String tmpStartTag=startTag.toLowerCase();
		String tmpEndTag=endTag.toLowerCase();
		
		
		StringBuffer sb=new StringBuffer();
		int beginIndex=tmp.indexOf(tmpStartTag);
		int endIndex=tmp.indexOf(tmpEndTag);
		while(beginIndex!=-1 && endIndex!=-1 && beginIndex<endIndex)
		{
			String pre=content.substring(0,tmp.indexOf(tmpStartTag)+tmpStartTag.length());
			String suffix=content.substring(tmp.indexOf(tmpEndTag));
			
			tmp=suffix.toLowerCase();
			content=suffix.substring(endTag.length());
			
			beginIndex=tmp.indexOf(tmpStartTag);
			endIndex=tmp.indexOf(tmpEndTag);
			String replaced=pre + "\r\n" +  repalceWith  +"\r\n" + endTag;
			sb.append(replaced);
		}
		sb.append(content);
		return sb.toString();
	}
	
	/**
	 * 判断指定的内容是否存在
	 * @param content 内容
	 * @param begin 开始标签
	 * @param end 结束标签
	 * @return
	 */
	public static boolean isExist(String content,String begin,String end)
	{
		String tmp=content.toLowerCase();
		begin=begin.toLowerCase();
		end=end.toLowerCase();
		int beginIndex=tmp.indexOf(begin);
		int endIndex=tmp.indexOf(end);
		if(beginIndex!=-1  && endIndex!=-1 && beginIndex<endIndex)
			return true;
		return false;
	}
	
	/**
	 * 截取字符串中的 指定两字串 中间的 字符串
	 * @param start
	 * @param end
	 * @return
	 */
	public static String subString(String content,String start,String end){
		String str=content;
		if(content.indexOf(start)!=-1){
			if(content.indexOf(end)!=-1){
				str=content.substring(content.indexOf(start)+start.length(),content.lastIndexOf(end));
			}else{
				str=content.substring(content.indexOf(start)+start.length());
			}
		}
		
		return str;
	}
	
	/**
	 * 字符串首字母小写
	 * @param str
	 * @return
	 */
	public static String toFirstLowerCase(String str){
		Character head=str.charAt(0);
		String lower=head.toString().toLowerCase();
		return str.replaceFirst(head.toString(),lower);
	}
	
	
	public static void main(String[] args) throws CodegenException
	{
		toFirstLowerCase("ABC");
	}
	

}

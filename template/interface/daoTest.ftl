<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<#assign baseClass=model.variables.baseClass>
package com.hotent.${system}.${package}.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.hotent.${system}.${package}.dao.${class}Dao;
import com.hotent.${system}.${package}.model.${class};
import com.hotent.${system}.${package}.${baseClass?cap_first}BaseTest;

public class ${class}DaoTest extends ${baseClass?cap_first}BaseTest{
	@Resource
	private ${class}Dao<String,${class}> ${classVar}Dao;
	
	@Test
	@Rollback(true)
	public void testCrud(){
		Default${class} ${classVar}=new Default${class}();
		Integer randId=new Double(100000*Math.random()).intValue();
		<#list model.columnList as col>
			<#assign columnName=func.convertUnderLine(col.columnName)>
		<#if col.isPK>
		${classVar}.setId(idGenerator.getSuid());
		<#else>
		<#if col.isNotNull>
		<#if col.colType="java.util.Date">
		${classVar}.set${columnName?cap_first}(new Date());
		<#elseif col.colType="Float">
		${classVar}.set${columnName?cap_first}(Float.parseFloat(randId+""));
		<#elseif col.colType="Short">
		${classVar}.set${columnName?cap_first}(new Short("1"));
		<#elseif col.colType="Integer">
		${classVar}.set${columnName?cap_first}(randId);
		<#elseif col.colType="Long">
		${classVar}.set${columnName?cap_first}(Long.parseLong(randId+""));
		<#elseif col.colType="String">
		${classVar}.set${columnName?cap_first}("${classVar}" + randId);
		</#if>
		</#if>
		</#if>
		</#list>
		
		//创建一实体
		${classVar}Dao.create(${classVar});
        Assert.assertNotNull(${classVar}.getId());
        logger.debug("${classVar}1:"+ ${classVar}.getId());
		//获取一实体
		Default${class} ${classVar}2=(Default${class})${classVar}Dao.get(${classVar}.getId());
		Assert.assertNotNull(${classVar}2);
		logger.debug("${classVar}2:" + ${classVar}2.toString());
		Integer randId2=new Double(100000*Math.random()).intValue();
		<#list model.columnList as col>
			<#assign columnName=func.convertUnderLine(col.columnName)>
			<#if !col.isPK>
		<#if col.isNotNull>
		<#if col.colType="java.util.Date">
		${classVar}2.set${columnName?cap_first}(new Date());
		<#elseif col.colType="Float">
		${classVar}2.set${columnName?cap_first}(Float.parseFloat(randId2+""));
		<#elseif col.colType="Short">
		${classVar}2.set${columnName?cap_first}(new Short("1"));
		<#elseif col.colType="Integer">
		${classVar}2.set${columnName?cap_first}(randId2);
		<#elseif col.colType="Long">
		${classVar}2.set${columnName?cap_first}(Long.parseLong(randId2+""));
		<#elseif col.colType="String">
		${classVar}2.set${columnName?cap_first}("${classVar}" + randId2);
		</#if>
		</#if>
			</#if>
		</#list>
		//更新一实体
		${classVar}Dao.update(${classVar}2);
		
		${class} ${classVar}3=${classVar}Dao.get(${classVar}.getId());
		System.out.println("${classVar}3:"+${classVar}3.toString());
		//删除一实体
		//${classVar}Dao.remove(${classVar}.getId());
	}
	
}

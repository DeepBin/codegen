<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign baseClass=model.variables.baseClass>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
package com.hotent.${system}.${package}.manager;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.hotent.${system}.${package}.model.${class};
import com.hotent.${system}.${package}.manager.${class}Manager;
import com.hotent.${system}.${package}.${baseClass}BaseTest;

public class ${class}ManagerTest extends ${baseClass}BaseTest{
	@Resource
	${class}Manager ${classVar}Manager;
	
	@Test
	@Rollback(true)
	public void testCreate(){
		${class} ${classVar}=new ${class}();
		Integer randId=new Double(100000*Math.random()).intValue();
		<#list model.columnList as col>
			<#assign columnName=func.convertUnderLine(col.columnName)>
		<#if col.isPK>
		${classVar}.set${pkVar?cap_first}(idGenerator.getSuid());
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
		${classVar}Manager.create(${classVar});
	}
}

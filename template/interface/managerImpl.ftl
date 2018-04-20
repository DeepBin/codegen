<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign subtables=model.subTableList>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
package com.hotent.${system}.${package}.manager.impl;

import java.io.Serializable;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
<#if subtables?exists && subtables?size != 0>
import java.util.List;
<#list subtables as subtable>
import com.hotent.${system}.${package}.dao.${subtable.variables.class}Dao;
import com.hotent.${system}.${package}.model.${subtable.variables.class};
</#list>
</#if>
import com.hotent.base.db.api.Dao;
import com.hotent.base.manager.impl.AbstractManagerImpl;
import com.hotent.${system}.${package}.dao.${class}Dao;
import com.hotent.${system}.${package}.model.Default${class};
import com.hotent.${system}.${package}.manager.${class}Manager;

@Service("${classVar}Manager")
public class ${class}ManagerImpl extends AbstractManagerImpl<String, Default${class}> implements ${class}Manager<String,Default${class}>{
	@Resource
	${class}Dao<String,Default${class}> ${classVar}Dao;
	<#if subtables?exists && subtables?size != 0>
		<#list subtables as table>
	@Resource
	${table.variables.class}Dao ${table.variables.classVar}Dao;
		</#list>
	</#if>
	@Override
	protected Dao<String, Default${class}> getDao() {
		return ${classVar}Dao;
	}
<#if subtables?exists && subtables?size != 0>
	/**
	 * 创建实体包含子表实体
	 */
	public void create(${class} ${classVar}){
    	super.create(${classVar});
    	<#list subtables as subtable>
    	<#assign subclass=subtable.variables.class>
    	<#assign subclassvar=subtable.variables.classVar>
    	${subclassvar}Dao.delByMainId(${classVar}.get${pkVar?cap_first}());
    	List<${subclass}> ${subclassvar}List=${classVar}.get${subclass}List();
    	for(${subclass} ${subclassvar}:${subclassvar}List){
    		${subclassvar}Dao.create(${subclassvar});
    	}
    	</#list>
    }
	
	/**
	 * 删除记录包含子表记录
	 */
	public void remove(String entityId){
		super.remove(entityId);
		<#list subtables as subtable>
    	<#assign subclass=subtable.variables.class>
    	<#assign subclassvar=subtable.variables.classVar>
    	${subclassvar}Dao.delByMainId(entityId);
    	</#list>
		
	}
	
	/**
	 * 批量删除包含子表记录
	 */
	public void removeByIds(String[] entityIds){
		for(String id:entityIds){
			this.remove(id);
		}
	}
    
	/**
	 * 获取实体
	 */
    public ${class} get(String entityId){
    	${class} ${classVar}=super.get(entityId);
    	<#list subtables as subtable>
    	<#assign subclass=subtable.variables.class>
    	<#assign subclassvar=subtable.variables.classVar>
    	List<${subclass}> ${subclassvar}List=${subclassvar}Dao.getByMainId(entityId);
    	${classVar}.set${subclass}List(${subclassvar}List);
    	</#list>
    	return ${classVar};
    }
    
    /**
     * 更新实体同时更新子表记录
     */
    public void update(${class} ${classVar}){
    	super.update(${classVar});
    	<#list subtables as subtable>
    	<#assign subclass=subtable.variables.class>
    	<#assign subclassvar=subtable.variables.classVar>
    	${subclassvar}Dao.delByMainId(${classVar}.get${pkVar?cap_first}());
    	List<${subclass}> ${subclassvar}List=${classVar}.get${subclass}List();
    	for(${subclass} ${subclassvar}:${subclassvar}List){
    		${subclassvar}Dao.create(${subclassvar});
    	}
    	</#list>
    }
	
	</#if>
}

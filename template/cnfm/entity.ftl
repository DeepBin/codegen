<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign comment=model.tabComment>
<#assign system=vars.system>
<#assign subtables=model.subTableList>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<#assign pkType=func.getPkType(model)>
<#assign fkType=func.getFkType(model)>
package com.sky.${system}.${package}.persistence.model;
<#if subtables?exists && subtables?size!=0>
import java.util.ArrayList;
import java.util.List;
</#if>
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.sky.common.model.AbstractModel;


/**
 * 
 * <pre> 
 * 描述：[${comment}]实体类
 * 构建组：x5-bpmx-platform
 <#if vars.developer?exists>
 * 作者:${vars.developer}
 * 邮箱:${vars.email}
 </#if>
 * 日期:${date?string("yyyy-MM-dd HH:mm:ss")}
 * 版权：${vars.company}
 * </pre>
 */
public class ${class} extends AbstractModel<${pkType}>{
	<#list model.columnList as col>
	
	/**
	* ${col.comment}
	*/
	protected ${col.colType} ${func.convertUnderLine(col.columnName)}; 
	</#list>
	
	<#if subtables?exists && subtables?size!=0>
	<#list subtables as table>
	<#assign vars=table.variables>
	/**
	*${table.tabComment}列表
	*/
	protected List<${vars.class}> ${vars.classVar}List=new ArrayList<${vars.class}>(); 
	</#list>
	</#if>
<#if (pkVar!="id")>
	@Override
	public void setId(String ${pkVar}) {
		this.${pkVar} = ${pkVar}.toString();
	}
	@Override
	public String getId() {
		return ${pkVar}.toString();
	}	
</#if>
<#list model.columnList as col>
	<#assign colName=func.convertUnderLine(col.columnName)>
	
	public void set${colName?cap_first}(${col.colType} ${colName}) {
		this.${colName} = ${colName};
	}
	
	/**
	 * 返回 ${col.comment}
	 * @return
	 */
	public ${col.colType} get${colName?cap_first}() {
		return this.${colName};
	}
</#list>
<#if subtables?exists && subtables?size!=0>
<#list subtables as table>
<#assign vars=table.variables>

	public void set${vars.classVar?cap_first}List(List<${vars.class}> ${vars.classVar}List) {
		this.${vars.classVar}List = ${vars.classVar}List;
	}
	
	/**
	 * 返回 ${table.tabComment}列表
	 * @return
	 */
	public List<${vars.class}> get${vars.classVar?cap_first}List() {
		return this.${vars.classVar}List;
	}
</#list>
</#if>
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		<#list model.columnList as col>
		<#assign colName=func.convertUnderLine(col.columnName)>
		.append("${colName}", this.${colName}) 
		</#list>
		.toString();
	}
}
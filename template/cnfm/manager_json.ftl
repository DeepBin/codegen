<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign comment=model.tabComment>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign pkType=func.getPkType(model)>

package com.sky.${system}.${package}.persistence.manager;

import java.util.Map;

import com.hotent.base.db.mybatis.domain.PageList;
import com.hotent.base.manager.api.Manager;
import com.sky.${system}.${package}.persistence.model.${class};


import com.hotent.base.api.Page;
import com.hotent.base.api.query.QueryFilter;

/**
 * 
 * <pre> 
 * 描述：[${comment}]逻辑处理接口
 * 构建组：x5-bpmx-platform
 <#if vars.developer?exists>
 * 作者:${vars.developer}
 * 邮箱:${vars.email}
 </#if>
 * 日期:${date?string("yyyy-MM-dd HH:mm:ss")}
 * 版权：${vars.company}
 * </pre>
 */
public interface ${class}Manager extends Manager<String, ${class}> {
	/**
	 * 根据主键得到[${comment}]信息
	 * @param id
	 * @return ${class}
	 * @author ${vars.developer} 
	 * @Create ${date?string("yyyy-MM-dd HH:mm:ss")}
	 */
	public ${class} get${class}(String id);
	
	/**
	 * 保存[${comment}]对象
	 * @param ${classVar}
	 * @return ${class}
	 * @author ${vars.developer} 
	 * @Create ${date?string("yyyy-MM-dd HH:mm:ss")}
	 */
	public ${class} save${class}(${class} ${classVar});
	
	/**
	 * 执行删除[${comment}]对象
	 * @param aryIds
	 * 			<li>主键值，以逗号隔开 </li>
	 * @return ${class}
	 * 			<li>如果是删除一条[${comment}]数据，会返回删除的[${comment}]信息。批量删除，会返回null </li>
	 * @author ${vars.developer} 
	 * @Create ${date?string("yyyy-MM-dd HH:mm:ss")}
	 */
	public ${class} doRemove(String[] aryIds);
	
	/**
	 * 执行列表查询
	 * @param paramMaps
	 * @return PageList<${class}>
	 * @author ${vars.developer} 
	 * @Create ${date?string("yyyy-MM-dd HH:mm:ss")}
	 */
	public PageList<${class}>  ${classVar}ListJson(Map<String ,Object> paramMap);
}

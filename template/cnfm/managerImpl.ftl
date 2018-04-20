<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign comment=model.tabComment>
<#assign subtables=model.subTableList>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<#assign pkType=func.getPkType(model)>
<#assign fkType=func.getFkType(model)>
package com.sky.${system}.${package}.persistence.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sky.api.sys.log.constant.Globals;
import com.sky.api.sys.log.service.ISysLogService;

import com.hotent.base.db.api.Dao;
import com.hotent.base.manager.impl.AbstractManagerImpl;
import com.sky.common.constant.ConstSystem;
import com.sky.common.util.StaticMethod;
import com.sky.${system}.${package}.persistence.dao.${class}Dao;
import com.sky.${system}.${package}.persistence.manager.${class}Manager;
import com.sky.${system}.${package}.persistence.model.${class};

/**
 * 
 * <pre> 
 * 描述：[${comment}]逻辑处理实现类
 * 构建组：x5-bpmx-platform
 <#if vars.developer?exists>
 * 作者:${vars.developer}
 * 邮箱:${vars.email}
 </#if>
 * 日期:${date?string("yyyy-MM-dd HH:mm:ss")}
 * 版权：${vars.company}
 * </pre>
 */
@Service("${classVar}Manager")
public class ${class}ManagerImpl extends AbstractManagerImpl<String, ${class}> implements ${class}Manager{
	@Resource
	${class}Dao ${classVar}Dao;
	
	@Resource
	ISysLogService sysLogService;
	
	@Override
	protected Dao<String, ${class}> getDao() {
		return ${classVar}Dao;
	}
	/**
	 * 根据主键得到[${comment}]信息
	 * @param id
	 * @return ${class}
	 * @author ${vars.developer} 
	 * @Create ${date?string("yyyy-MM-dd HH:mm:ss")}
	 */
	@Override
	public ${class} get${class}(String id) {
		if (!StaticMethod.isNotEmpty(id)) { return null; }
		${class} ${classVar} = this.get(id);
		return ${classVar};
	}
	
	/**
	 * 保存[${comment}]对象
	 * @param ${classVar}
	 * @return ${class}
	 * @author ${vars.developer} 
	 * @Create ${date?string("yyyy-MM-dd HH:mm:ss")}
	 */
	@Override
	public ${class} saveOrUpdate(${class} ${classVar}) {
		//${classVar}.setIsDeleted(ConstSystem.NO);
		if (!StaticMethod.isNotEmpty(this.get${class}(${classVar}.getId()))) {
			this.create(${classVar});
			sysLogService.addSysLog("新增${comment}成功", Globals.Log_Type_CREATE, Globals.Log_Leavel_INFO,${classVar}.getId());
		} else {
			this.update(${classVar});
			sysLogService.addSysLog("更新${comment}成功", Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO,${classVar}.getId());
		}
		return ${classVar};
	}
	
	/**
	 * 执行删除[${comment}]对象
	 * @param aryIds
	 * 			<li>主键值，以逗号隔开 </li>
	 * @return ${class}
	 * 			<li>如果是删除一条[${comment}]数据，会返回删除的[${comment}]信息。批量删除，会返回null </li>
	 * @author ${vars.developer} 
	 * @Create ${date?string("yyyy-MM-dd HH:mm:ss")}
	 */
	@Override
	public ${class} doRemove(String[] aryIds) {
		for(String id:aryIds){
			${class} ${classVar} = this.get${class}(id);
			//${classVar}.setIsDeleted(ConstSystem.YES);
			update(${classVar});
			sysLogService.addSysLog("删除${comment}成功", Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO,${classVar}.getId());
		}
		if (StaticMethod.isNotEmpty(aryIds) && aryIds.length == 1) { return this.get${class}(aryIds[0]); }
		return null;
	}
}

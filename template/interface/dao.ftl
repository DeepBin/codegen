<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign sub=model.sub>
<#assign foreignKey=func.convertUnderLine(model.foreignKey)>
package com.hotent.${system}.${package}.dao;
<#if sub?exists && sub>
import java.util.HashMap;
import java.util.List;
import java.util.Map;
</#if>
import java.io.Serializable;
import com.hotent.base.db.api.Dao;
import com.hotent.${system}.${package}.model.${class};


public interface ${class}Dao <PK extends Serializable,T extends ${class}> extends Dao<PK, T> {
	<#if sub?exists && sub>
	/**
	 * 根据外键获取子表明细列表
	 * @param ${foreignKey}
	 * @return
	 */
	public List<${class}> getByMainId(String ${foreignKey});
	
	/**
	 * 根据外键删除子表记录
	 * @param ${foreignKey}
	 * @return
	 */
	public void delByMainId(String ${foreignKey});
	
	</#if>	
}



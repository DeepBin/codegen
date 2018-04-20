<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign sub=model.sub>
<#assign foreignKey=func.convertUnderLine(model.foreignKey)>
package com.hotent.${system}.${package}.dao.impl;

<#if sub?exists && sub>
import java.util.HashMap;
import java.util.List;
import java.util.Map;
</#if>
import java.io.Serializable;
import org.springframework.stereotype.Repository;

import com.hotent.base.db.impl.MyBatisDaoImpl;
import com.hotent.${system}.${package}.dao.${class}Dao;
import com.hotent.${system}.${package}.model.${class};
import com.hotent.${system}.${package}.model.Default${class};

@Repository
public class ${class}DaoImpl<PK extends Serializable,T extends ${class} > extends MyBatisDaoImpl<PK,T> implements ${class}Dao<PK,T>{

    @Override
    public String getNamespace() {
        return Default${class}.class.getName();
    }
<#if sub?exists && sub>
	/**
	 * 根据外键获取子表明细列表
	 * @param ${foreignKey}
	 * @return
	 */
	public List<${class}> getByMainId(String ${foreignKey}) {
		Map<String,Object>params=new HashMap<String, Object>();
		params.put("${foreignKey}", ${foreignKey});
		return this.getByKey("get${class}List", params);
	}
	
	/**
	 * 根据外键删除子表记录
	 * @param ${foreignKey}
	 * @return
	 */
	public void delByMainId(String ${foreignKey}) {
		Map<String,Object>params=new HashMap<String, Object>();
		params.put("${foreignKey}", ${foreignKey});
		this.deleteByKey("delByMainId", params);
	}
	
</#if>	
	
}


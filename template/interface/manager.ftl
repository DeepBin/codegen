<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
package com.hotent.${system}.${package}.manager;

import java.io.Serializable;
import com.hotent.base.manager.api.Manager;
import com.hotent.${system}.${package}.model.${class};


public interface ${class}Manager <PK extends Serializable,T extends ${class}> extends Manager<PK, T>{
	
}


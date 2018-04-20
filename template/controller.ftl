
package com.sky.${system}.${package}.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.base.api.query.QueryFilter;
import com.hotent.base.core.web.RequestUtil;
import com.hotent.base.db.mybatis.domain.PageJson;
import com.hotent.base.db.mybatis.domain.PageList;
import com.sky.common.controller.BaseController;
import com.sky.${system}.${package}.persistence.manager.${class}Manager;
import com.sky.${system}.${package}.persistence.model.${class};

/**
 * 
 * <pre> 
 * 描述：[${comment}]控制器类
 * 构建组：x5-bpmx-platform
 <#if vars.developer?exists>
 * 作者:${vars.developer}
 * 邮箱:${vars.email}
 </#if>
 * 日期:${date?string("yyyy-MM-dd HH:mm:ss")}
 * 版权：${vars.company}
 * </pre>
 */
@Controller
@RequestMapping("/${system}/${package}")
public class ${class}Controller extends BaseController {
	
	@Resource
	${class}Manager ${classVar}Manager;
	
	/**
	 * 执行查询 ,系统自动封装查询条件 
	 * @return ModelAndView
	 * @throws Exception 
	 * @author wjg 
	 * @Create Date 2017 下午4:33:31
	 */
	@RequestMapping("${classVar}List")
	public ModelAndView ${classVar}List() throws Exception {
		ModelAndView modelAndView = new ModelAndView("${system}/${package}/${classVar}List");//跳转的页面
		QueryFilter queryFilter = getQueryFilter(getRequest());//封装查询条件
		PageList<${class}> ${classVar}List = (PageList<${class}>) ${classVar}Manager.query(queryFilter);//执行查询
		PageJson pageJson = new PageJson(${classVar}List);
		modelAndView.addObject("pageJson", pageJson);
		return modelAndView;
	}
	
	/**
	 * 通过ajax方式查询[${comment}]信息 
	 * @return ${class}
	 * @throws Exception 
	 * @author wjg 
	 * @Create Date 2017 下午4:33:57
	 */
	@RequestMapping("get${class}Json")
	public @ResponseBody ${class} get${class}Json() throws Exception {
		String id = RequestUtil.getString(this.getRequest(), "id");//主键
		${class} ${classVar} = ${classVar}Manager.get${class}(id);
		return ${classVar};
	}
	
	/**
	 * 新增[${comment}]信息，跳转新增[${comment}]页面  
	 * @return ModelAndView
	 * @throws Exception 
	 * @author wjg 
	 * @Create Date 2017 下午4:53:16
	 */
	@RequestMapping("add${class}")
	public ModelAndView add${class}() throws Exception {
		ModelAndView modelAndView = new ModelAndView("${system}/${package}/${classVar}Edit");
		modelAndView.addObject("id", idGenerator.getSuid());
		return modelAndView;
	}
	
	/**
	 * 执行新增或者更新[${comment}]信息
	 * @param ${classVar}
	 * @return ModelAndView
	 * @throws Exception 
	 * @author wjg 
	 * @Create Date 2017 下午4:54:14
	 */
	@RequestMapping("save${class}")
	public ModelAndView saveOrUpdate(${class} ${classVar}) throws Exception {
		${classVar} = ${classVar}Manager.saveOrUpdate(${classVar});
		ModelAndView modelAndView = new ModelAndView("redirect:/${system}/${package}/${classVar}Result?id=" + ${classVar}.getId() );
		return modelAndView;
	}
	
	/**
	 * 执行删除[${comment}]对象
	 * @return ModelAndView
	 * @throws Exception 
	 * @author wjg 
	 * @Create Date 2017 下午5:03:29
	 */
	@RequestMapping("do${class}Remove")
	public ModelAndView do${class}Remove() throws Exception {
		ModelAndView modelAndView = new ModelAndView("${system}/${package}/${classVar}Result");
		String[] aryIds = RequestUtil.getStringAryByStr(this.getRequest(), "id");
		${class} ${classVar} = ${classVar}Manager.doRemove(aryIds);
		modelAndView.addObject("obj", ${classVar});
		return modelAndView;
	}
}

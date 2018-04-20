<#import "function.ftl" as func>
<#assign comment=model.tabComment>
<#assign class=model.variables.class>
<#assign package=model.variables.package>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign commonList=model.commonList>
<#assign pkModel=model.pkModel>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<%@include file="/commons/include/list.jsp" %>
	</head>
	<body  class="easyui-layout" fit="true"   scroll="no">
		<div data-options="region:'center',border:false"  >
			<!-- 顶部查询操作 -->
		    <div  class="toolbar-search col-md-13 ">
				<div class="toolbar-box border">
					<div class="toolbar-head">
						<!-- 顶部按钮 -->
						<div class="buttons"> 		
							<a class="btn btn-primary fa-search"  href="javascript:void(0);" ><span>搜索</span></a>
							<a class="btn btn-primary fa-add"     href="edit.ht" ><span>添加</span></a>
					        <a class="btn btn-primary fa-edit"    href="javascript:void(0);"  action="edit.ht" ><span>编辑</span></a>	      
					        <a class="btn btn-primary fa-detail"  href="javascript:void(0);"  action="get.ht"><span>明细</span></a>
					        <a class="btn btn-primary fa-remove"  href="javascript:void(0);"  action="remove.ht"><span>删除</span></a>
						</div>
			 			<div class="tools">
							<a href="javascript:void(0);" class="collapse">
								<i class="bigger-190 fa  fa-angle-double-up"></i>
							</a>
						</div>
					</div>
					<div class="toolbar-body" >
						<form  id="searchForm" class="search-form" >
							<ul>
							<#list commonList as col>
							<#assign colName=func.convertUnderLine(col.columnName)>
							<#if (col.colType=="java.util.Date")>
								<li>
									<span>${col.comment} 从</span>:<input  name="Q^${col.columnName}^${func.getDataType("Date","1")}"  class="inputText date" />
								</li>
								<li>
									<span>至: </span><input  name="Q^${col.columnName}^${func.getDataType("Date","0")}" class="inputText date" />
								</li>
							<#else>
								<li><span>${col.comment}:</span><input class="inputText" type="text" name="Q^${col.columnName}^${func.getDataType("${col.colType}","0")}"></li>
							</#if>
							</#list>
							</ul>
						</form>
					</div>
			    </div>
			</div>
			<table  class="easyui-datagrid" idField="${pkVar}"  data-options="fitColumns:false,checkOnSelect:true,pagination:true,toolbar:'.toolbar-search'" fit="true" 
				 url="<#noparse>${</#noparse>ctx}/${system }/${package}/${classVar}/listJson.ht">
			    <thead>
				    <tr>
						<th field="${pkVar}" checkbox="true" class="pk"></th>
				    	<#list commonList as col>
						<#assign colName=func.convertUnderLine(col.columnName)>
						<#if colName!=pkVar>
								<#if (col.colType=="java.util.Date")>
						<th field="${colName}" sortable="true" sortName="${col.columnName}" title="${col.getComment()}" formatter="ht" dateFormat="YYYY-MM-DD HH:mm:ss"></th>
								<#else>
						<th field="${colName}" sortable="true" sortName="${col.columnName}" title="${col.getComment()}" ></th>
								</#if>
						</#if>
						</#list>
						<th manager="true"  width="40" style="display: none;">	
					    	<f:a alias="${classVar}_detail"  href="get.ht?${pkVar}={${pkVar}}"   css="btn btn-default fa-detail" >详细</f:a>
					     	<f:a alias="${classVar}_edit"    href="edit.ht?${pkVar}={${pkVar}}"  css="btn btn-default fa-edit" >编辑</f:a>
					     	<f:a alias="${classVar}_remove"  href="javascript:void(0);"  action="remove.ht?${pkVar}={${pkVar}}"   css="btn btn-default fa-remove" >删除</f:a>
					    </th>
				    </tr>
			    </thead>
		    </table>
		  </div>
	</body>
</html>
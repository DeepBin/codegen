<#import "function.ftl" as func>
<#assign comment=model.tabComment>
<#assign class=model.variables.class>
<#assign package=model.variables.package>
<#assign comment=model.tabComment>
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
		<%@include file="/commons/include/get.jsp" %>
		<script type="text/javascript" src="${ctx}/js/common/form/commonController.js"></script>
	</head>
	<body class="easyui-layout">
		<!-- 顶部按钮 -->
		<div class="toolbar-panel">
			<div class="buttons">
				<a class="btn btn-primary fa-back" href="<#noparse>${</#noparse>returnUrl}" ><span>返回</span></a>
			</div>
		</div>
		<table class="table-form"   cellspacing="0">
			<#list commonList as col>
				<#assign colName=func.convertUnderLine(col.columnName)>
				<#if colName!=pkVar>
					<tr>								
						<th>${col.getComment()}:</th>
					<#if (col.colType=="java.util.Date")>
						<td><fmt:formatDate value="<#noparse>${</#noparse>${classVar}.${colName}}" /></td>		
					<#else>	
						<td><#noparse>${</#noparse>${classVar}.${colName}}</td>
					</#if>				
					</tr>
				</#if>
			</#list>
		</table>
	</body>
</html>
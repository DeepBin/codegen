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
<#assign subtables=model.subTableList>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html ng-app="app">
	<head>
		<%@include file="/commons/include/ngEdit.jsp" %>
		<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/js/${system}/${package}/${classVar}/${classVar}GetController.js"></script>
	</head>
	<body class="easyui-layout" ng-controller="ctrl">
		<!-- 顶部按钮 -->
		<div class="toolbar-panel">
			<div class="buttons">
				<a class="btn btn-primary fa-back" onClick="HT.window.closeEdit();"><span>返回</span></a>
			</div>
		</div>
		<form name="form" ht-load="getJson?id=<#noparse>${param.id}</#noparse>"   ng-model="data">
		<table class="table-form"   cellspacing="0">
			<#list commonList as col>
				<#assign colName=func.convertUnderLine(col.columnName)>
				<#if colName!=pkVar>
					<tr>								
						<th>${col.getComment()}:</th>
						<td> {{data.${colName} <#if (col.colType=="java.util.Date")>| date :'yyyy-MM-dd'</#if>}}</td>
					</tr>
				</#if>
			</#list>
		</table>
		
		<#if subtables?exists && subtables?size != 0>
				<#list subtables as table>
				<#assign foreignKey=func.convertUnderLine(table.foreignKey) >
				
				<table class="table-grid" cellspacing="0">
					<tr>
						<td colspan="${table.columnList?size-1}">
							${table.tabComment }
						</td>
					</tr>
					<tr>
						<#list table.columnList as col>
							<#assign colName=func.convertUnderLine(col.columnName?lower_case)>
							<#if !(col.isPK)&& colName?lower_case!=(foreignKey)?lower_case>							                              
								<th>${col.comment}</th>
							</#if>
						</#list>
					</tr>
					<tr ng-repeat="item in data.${table.variables.classVar}List track by $index">
						<#list table.columnList as col>
							<#assign colName=func.convertUnderLine(col.columnName)>
							<#if !(col.isPK)&& colName?lower_case!=(foreignKey)?lower_case>							                              
							<td>
								<#if (col.colType!="java.util.Date")>
								{{item.${colName}}}
								<#else>
								{{item.${colName} |date :'yyyy-MM-dd'}}
								</#if>
							</td>
							</#if>
						</#list>
						
					</tr>
				</table>
				</#list>
			</#if>
		
		</form>
	</body>
</html>
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
<!doctype html>
<html ng-app="app">
	<head>
		<%@include file="/commons/include/ngEdit.jsp" %>
		<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/js/${system}/${package}/${classVar}/${classVar}EditController.js"></script>
	</head>
	<body ng-controller="ctrl">
		
			<!-- 顶部按钮 -->
			<div class="toolbar-panel">
				<div class="buttons">
					<a class="btn btn-primary fa-save" ng-model="data" ht-save="save"><span>保存</span></a>
					<a class="btn btn-primary fa-back" onClick="HT.window.closeEdit();"><span>返回</span></a>
				</div>
			</div>
			<form name="form" method="post" ht-load="getJson?id=<#noparse>${param.id}</#noparse>"  ng-model="data">
				<table class="table-form"  cellspacing="0" >
						<#list commonList as col>
							<#assign colName=func.convertUnderLine(col.columnName)>
							<tr>								
								<th>${col.getComment()}:<#if col.isNotNull><span class="required">*</span></#if></th>
								<td>
									<#if (col.colType!="java.util.Date")>
									<input class="inputText" type="text" ng-model="data.${colName}"   ht-validate="{<#if col.isNotNull>required:true<#else>required:false</#if><#if col.colType=='String'&& col.length<1000>,maxlength:${col.length}</#if><#if col.colType=='Integer'|| col.colType=='Long'||col.colType=='Float'>,number:true<#if col.scale!=0>,maxDecimalLen:${col.scale}</#if><#if col.precision!=0>,maxIntLen:${col.precision}</#if></#if>}"  />
									<#else >
									<input class="inputText" type="text" ng-model="data.${colName}" ht-date  ht-validate="{<#if col.isNotNull>required:true<#else>required:false</#if>}"  />
									</#if>
								</td>								
							</tr>
						</#list>
				</table>
				
			<#if subtables?exists && subtables?size != 0>
				<#list subtables as table>
				<#assign foreignKey=func.convertUnderLine(table.foreignKey) >
				
				<table class="table-grid" cellspacing="0">
					<tr>
						<td colspan="${table.columnList?size-1}">
							<div class="buttons">
								<a class="btn btn-primary fa-add" ng-click="addRow('${table.variables.classVar}')" >
									<span>添加</span>
								</a>
							</div>
						</td>
					</tr>
					<tr>
						<#list table.columnList as col>
							<#assign colName=func.convertUnderLine(col.columnName?lower_case)>
							<#if !(col.isPK)&& colName?lower_case!=(foreignKey)?lower_case>							                              
								<th>${col.comment}</th>
							</#if>
						</#list>
						<th>操作</th>
					</tr>
					<tr ng-repeat="item in data.${table.variables.classVar}List track by $index">
						<#list table.columnList as col>
							<#assign colName=func.convertUnderLine(col.columnName)>
							<#if !(col.isPK)&& colName?lower_case!=(foreignKey)?lower_case>							                              
							<td>
								<#if (col.colType!="java.util.Date")>
								<input class="inputText" type="text" ng-model="item.${colName}"  ht-validate="{<#if col.isNotNull>required:true<#else>required:false</#if><#if col.colType=='String'&& col.length<1000>,maxlength:${col.length}</#if><#if col.colType=='Integer'|| col.colType=='Long'||col.colType=='Float'>,number:true<#if col.scale!=0>,maxDecimalLen:${col.scale}</#if><#if col.precision!=0>,maxIntLen:${col.precision}</#if></#if>}"  />
								<#else>
								<input class="inputText" type="text" ht-date ng-model="item.${colName}" />
								</#if>
							</td>
							</#if>
						</#list>
						<td>
							<a href="#" ng-click="ArrayTool.del($index,data.${table.variables.classVar}List)" class="btn btn-sm btn-default fa-times"></a>
						</td>
					</tr>
				</table>
				</#list>
			</#if>
				
			</form>
		
	</body>
</html>
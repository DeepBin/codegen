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
<div ng-show="objId.length>0">
	<input style="display: none;" name="id" ng-model="data.id" ng-init="data.id = objId">
</div>
<div class="row pr30">
	<div class="col-lg-12 mb20 pb30 line-xc">
		<a data-toggle="collapse" data-target="#content2" class="mode-jian"> <span><img src="${r"${"}webRoot}/cnfm/images/icon04.jpg"></span> <b class="f18">${comment}</b> <span class="glyphicon glyphicon-triangle-bottom blue"></span>
		</a>
		<div class="collapse in">
			<div class="collapse in" id="content2">
				<#list commonList as col>
					<#assign colName=func.convertUnderLine(col.columnName)>
				<div class="row mt20">
					<div class="col-lg-2 col-md-2 col-xs-2 col-sm-2 f16 pt5 text-right">${col.getComment()}<#if col.isNotNull><span class="text-danger">*</span></#if></div>
					<div class="col-lg-4 col-md-4 col-xs-4 col-sm-4">
						<input class="form-control border-ra0<#if col.isNotNull> required</#if><#if (col.colType=="java.util.Date")> datepicker</#if>" <#if (col.colType=="java.util.Date")> cnfm-date="yyyy-MM-dd" </#if> ng-model="data.${colName}"  name="${colName}" type="text"  id="${colName}" placeholder="${col.getComment()}"  <#if col.isNotNull>required</#if> >
					</div>
				</div>
				</#list>
			</div>
		</div>
	</div>
</div>
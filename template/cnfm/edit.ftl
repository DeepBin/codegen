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
<%@include file="/WEB-INF/view/context/mytags.jsp"%>
<!DOCTYPE html>
<html ng-app="app" ng-init="objId = '${r"${"}id}'" ng-cloak>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,post-scalable=no" />
<title>${comment}录入</title>
<cnfm:base type="common,tools,system,model,from"></cnfm:base>
<script src="${r"${"}webRoot}/cnfm/js/${system}/${package}/${classVar}.js"></script>
</head>
<body style="overflow-x:hidden;" ng-controller="ctrl" ht-load="get${class}Json?id=${r"${"}param.id}" ng-model="data">
	<form id="form-${package}-edit" action="#" name="form" method="post">
		<div>
			<%@ include file="/WEB-INF/view/${system}/${package}/${classVar}CommonView.jsp"%>
			<div class="row mt20">
				<div class="col-md-12 text-center mt20 buttons">
					<button type="button" class="btn btn-info border-ra0 btn-lg plr-80" ng-model="data" ht-saveform="save${class}" ng-disabled="form.$invalid">
                    	提交
                	</button>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
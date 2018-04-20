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
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,post-scalable=no" />
<title>${comment}查询页面</title>
<cnfm:base type="common,tools,system,model,from"></cnfm:base>
</head>
<body>
	<div class="tab-content mb-10">
		<div class="tab-pane active" id="t6" ng-controller="ctrl">
			<form name="form" method="post" action="${classVar}List" class="ng-pristine ng-valid" novalidate="novalidate">
				<div class="row pr30">
					<div class="col-lg-12 pb30">
						<span><img src="${r"${"}webRoot}/cnfm/images/icon06.jpg"></span> <b class="f18">${comment}/查询</b>
					</div>
					<div class="col-lg-12">						
						<#list commonList as col>
							<#assign colName=func.convertUnderLine(col.columnName)>
						<div class="row mt20">
							<div class="col-lg-2 col-md-2 col-xs-2 f16 pt5 text-right">${col.comment}</div>
							<div class="col-lg-6 col-md-6 col-xs-6">
								<input class="form-control border-ra0 " type="text" name="Q^${col.columnName}^${func.getDataType("${col.colType}","0")}" id="Q^${col.columnName}^${func.getDataType("${col.colType}","0")}" placeholder="请输入${col.comment}名称">
							</div>
						</div>
						</#list>
						<div class="row mt50">
							<div class="col-lg-10 col-md-10 text-center">
								<input type="submit" value="查询" class="btn btn-info border-ra0 mr20 plr-80">
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>

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
<html ng-app="app">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,post-scalable=no" />
<title>${comment}操作结果</title>
<cnfm:base type="common,system,model"></cnfm:base>
</head>
<body>
	<div class="row pr30">
		<div class="col-lg-12 pb30">
			<span><img src="${r"${"}webRoot}/cnfm/images/icon08.jpg"></span> <b class="f18 ml5">录入结果</b>
		</div>
		<div class="col-lg-12">
			<div class="row">
				<div class="col-lg-12 text-center">
					<img src="${r"${"}webRoot}/cnfm/images/3.jpg">
					<div class="blue f30 mt30">操作成功！</div>
					<div class="row mt50">
						<div class="col-lg-2"></div>
						<div class="col-lg-8">
							<div class="row">
								<div class="col-lg-4">
									<button class="btn btn-default btn-blue border-ra0 pl50 pr50" onclick="location.href = '${classVar}Edit'">
										<span class="glyphicon glyphicon-menu-left"></span> 返回录入
									</button>
								</div>
								<div class="col-lg-4">
									<button class="btn btn-info border-ra0 plr-70 pr50 f16" onclick="location.href = '${classVar}View?id=${r"${"}param.id}'">查看</button>
								</div>
								<div class="col-lg-4">
									<button class="btn btn-default btn-blue plr-70 border-ra0" onclick="location.href = '${classVar}Search'">搜索</button>
								</div>
							</div>
						</div>
						<div class="col-lg-2"></div>
					</div>
				</div>
			</div>
		</div>
	</div>





</body>
</html>

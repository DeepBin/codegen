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
<html ng-app="listApp">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,post-scalable=no" />
<title>${comment}查询列表</title>
<cnfm:base type="common,tools,system,model,from"></cnfm:base>
<body>
	<div class="screen_x row" style="">
		<div class="col-lg-12 col-md-12">
			<div class="row">
				<div class="col-lg-12 col-md-12 mb20 pb30 line-xc">
					<a data-toggle="collapse" data-target="#content5" class="mode-jian"> 
						<span><img src="${r"${"}webRoot}/cnfm/images/icon09.jpg"></span> 
						<b class="f18">${comment}管理-列表</b> 
						<span class="glyphicon glyphicon-triangle-bottom blue"></span>
					</a>
					<div class="collapse in mt10" id="content5">
						<div class="row mb20">
							<div class="col-lg-12 col-md-12">
								<a class="pull-right btn btn-info ba0 mr30" onclick="location.href = '${classVar}Search'">查询 </a>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-12 col-md-12">
								<div class="row" ng-controller="listcontroller">
									<div class="col-lg-12 col-md-12">
										<div class="bs-example" data-example-id="striped-table">
											<table class="table table-striped table-mode table-bordered" style="width: 96%; margin-left: 2%;">
												<thead>
													<tr class="table-top table-top-blue">
														<th style="width: 6%;" class="text-center" data-toggle="buttons">
															<lable class="btn" style="padding:0;box-shadow:none;color: #fff;"> 
																<input type="checkbox" name="fa" autocomplete="off">
																	选择/全选 
															</lable>
														</th>
														<th style="width: 18%;" class=" text-center">操作</th>
														
														<#list commonList as col>
														<th class="text-center">${col.comment}</th>
														</#list>
														
													</tr>
												</thead>
												<tbody ng-init='listjsondata = ${r"${"}pageJson.jsonList}'>
													<tr ng-repeat="x in listjsondata" ng-cloak>
														<td class="f14 text-center">
															<div class="checkbox">
																<label> <input type="hidden" value="{{x.id}}" /> <input type="checkbox" name="optionsCheckbox">
																</label>
															</div>
														</td>
														<td class="text-center">
															<a href="${classVar}Edit?id={{x.id}}" class="blue mr20"> <span><img src="${r"${"}webRoot}/cnfm/images/icon01.png"></span> <span>修改</span></a> 
															<a href="${classVar}View?id={{x.id}}&view=true" class="blue mr20"> <span><img src="${r"${"}webRoot}/cnfm/images/icon09.jpg"></span> <span>查看</span></a> 
															<a href="${classVar}Remove?id={{x.id}}&view=true" class="blue "> <span><img src="${r"${"}webRoot}/cnfm/images/icon07.jpg"></span> <span>删除</span></a>
														</td>
														<#list commonList as col>
														<#assign colName=func.convertUnderLine(col.columnName)>

														<td class="text-center"><#if (col.colType=="java.util.Date")>{{x.${colName} | date:'yyyy-MM-dd'}}</#if><#if (col.colType!="java.util.Date")>{{x.${colName}}}</#if></td>
														</#list>
													</tr>
												</tbody>
											</table>
										</div>
										<cnfm:paging jsonName="listjsondata" attrName="pageJson"></cnfm:paging>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
</body>
</html>
<script>
	$(function() {
		$('table').tableCheckbox({ });
	});
</script>
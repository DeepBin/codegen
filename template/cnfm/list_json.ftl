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
<div class="shou_row">
    <div class="hidden_row">
		<div class="row mt10">
			<#list commonList as col>
			<#if (col_index < 3)> 
			<#assign colName=func.convertUnderLine(col.columnName)>
			<div class="col-lg-1 col-md-1 col-xs-1 f16 pt5 text-right">${col.comment}</div>
			<div class="col-lg-2 col-md-2 col-xs-2">
				<input class="form-control border-ra0" query="true" type="text" name="Q^${col.columnName}^${func.getDataType("${col.colType}","0")}" id="Q^${col.columnName}^${func.getDataType("${col.colType}","0")}" placeholder="请输入${col.comment}的条件">
			</div>
			</#if>
			</#list>
			<div class="col-lg-3 col-md-3 col-xs-3 text-center pull-right mt5">
				<a class="pull-right btn btn-info ba0 mr30 modal_cx"  data-toggle="modal" data-target="#modal">更多</a> 
				<a class="pull-right btn btn-info ba0 mr30 modal_cx" onclick="searchDo()">查询</a> 
			</div>
		</div>
	</div>
</div>	
	
	<div class="screen_x row mt5" style="">
		<div class="col-lg-12 col-md-12">
			<div class="row">
				<div class="col-lg-12 col-md-12 mb20 pb30 line-xc">
					<a data-toggle="collapse" data-target="#content5" class="mode-jian"> <span><img src="${r"${"}webRoot}/cnfm/images/icon09.jpg"></span> <b class="f18">${comment}-列表</b> <span class="glyphicon glyphicon-triangle-bottom blue"></span>
					</a>
					<div class="collapse in mt10" id="content5">
						<div class="row">
							<div class="col-lg-12 col-md-12">
								<ul>
									<li class="pull-left mr20">
										<span class="btn btn-default btn-blue" onclick="openUrl('新增','${classVar}Edit','','');">
											<span class="glyphicon glyphicon-plus-sign"></span> 
											新增
										</span>
									</li>
								 
									<li class="pull-left mr20">
										<span class="btn btn-default btn-blue" onclick="openEdit('修改','${classVar}Edit','','');">
											<span class="glyphicon glyphicon-edit"></span> 
											修改
										</span>
									</li>
								 
									<li class="pull-left mr20">
										<span class="btn btn-default btn-blue" onclick="openEdit('查看','${classVar}View','','');">
											<span class="glyphicon glyphicon-search"></span> 
											查看 
										</span>
									</li>
								 	
									<li class="pull-left mr20">
										<span class="btn btn-default btn-blue" onclick="openEdit('删除','${classVar}Remove','','');">
											<span class="glyphicon glyphicon-remove"></span> 
											删除 
										</span>
									</li>
								</ul>
							</div>
						</div>
						<div class="row mt5">
							<div class="col-lg-12 col-md-12">
								<div class="row" ng-controller="listcontroller">
									<div class="col-lg-12 col-md-12">
										<div class="bs-example pr10" data-example-id="striped-table">
											<table class="table table-striped table-mode table-bordered" style="width:100%;">
												<thead>
													<tr class="table-top table-top-blue">
														<th style="width: 6%;" class="text-center no-sort" data-toggle="buttons"><lable class="btn" style="padding:0;box-shadow:none;color: #fff;"> <input type="checkbox" name="fa" autocomplete="off"> 选择 </lable></th>
														<#list commonList as col>
														<th class="text-center">${col.comment}</th>
														</#list>
													</tr>
												</thead>
												<tbody ng-init='listjsondata'>
													<tr ng-repeat="x in listjsondata" ng-cloak>
														<td class="f14 text-center">
															<div class="checkbox">
																<label> <input type="hidden" value="{{x.id}}" /> <input type="checkbox" name="optionsCheckbox" value="{{x.id}}" >
																</label>
															</div>
														</td>
														<#list commonList as col>
														<#assign colName=func.convertUnderLine(col.columnName)>
														<td class="text-center"><#if (col.colType=="java.util.Date")>{{x.${colName} | date:'yyyy-MM-dd'}}</#if><#if (col.colType !="java.util.Date")>{{x.${colName}}}</#if></td>
														</#list>
													</tr>
												</tbody>
											</table>
										</div>
										<cnfm:paging ajaxUrl="/${system}/${package}/${classVar}ListJson" dateType="json"></cnfm:paging>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
		
<!-- 弹出更多查询条件 -->
 <div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="modal" >
    <div class="modal-dialog" role="document" style="width:80%;">
        <div class="modal-content" >
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                   <span><img src="${r"${"}webRoot}/cnfm/images/icon06.jpg"></span> <b class="f18">${comment}/查询</b>
                </h4>
            </div>
            <div class="modal-body">
                <div class="row pr30">
                    <div class="col-lg-12">
					
						<#list commonList as col>
							<#assign colName=func.convertUnderLine(col.columnName)>
                       <div class="row mt5">
							<div class="col-lg-2 col-md-2 col-xs-2 f16 text-right">${col.comment}</div>
							<div class="col-lg-4 col-md-4 col-xs-4">
								<input class="form-control border-ra0" query="true" type="text" name="Q^${col.columnName}^${func.getDataType("${col.colType}","0")}" id="Q^${col.columnName}^${func.getDataType("${col.colType}","0")}" placeholder="请输入${col.comment}条件">
							</div>
						</div>
						</#list>
					
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-info border-ra0 mr20 plr-80" onclick="searchDo()" data-dismiss="modal">查询</button>
            </div>
        </div>
    </div>
 </div>
<!-- 弹出更多查询条件结束 -->
</body>
</html>
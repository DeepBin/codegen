<#import "function.ftl" as func>
<#assign comment=model.tabComment>
<#assign class=model.variables.class>
<#assign package=model.variables.package>
<#assign classVar=model.variables.classVar>
<#assign commonList=model.commonList>
<#assign pkModel=model.pkModel>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<#assign system=vars.system>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>${comment}</title>
<%@include file="/commons/include/list.jsp"%>
</head>
<body>
	<div class="easyui-layout" fit="true" scroll="no">
		<div data-options="region:'center',border:false" style="text-align: center;">
			<div id="gridSearch" class="toolbar-search ">
				<div class="toolbar-box border">
					<div class="toolbar-head">
						<!-- 顶部按钮 -->
						<div class="buttons">
							<a class="btn btn-sm btn-primary fa-search" href="#">
								<span>搜索</span>
							</a>
							<a class="btn btn-sm btn-primary fa-add" href="#" onclick="openDetail('','add')">
								<span>添加</span>
							</a>
							<a class="btn btn-sm btn-primary fa-remove" href="#" action="/${system}/${package}/${classVar}/remove">
								<span>删除</span>
							</a>
						</div>
						<div class="tools">
							<a href="javascript:;" class="collapse">
								<i class="bigger-190 fa  fa-angle-double-up"></i>
							</a>
						</div>
					</div>
					<div class="toolbar-body">
						<form id="searchForm" class="search-form">
							<ul style="margin-left: -26px">
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
			<div id="grid" class="my-easyui-datagrid"></div>
		</div>
	</div>
</body>
</html>
<script>
	$(function() {
		loadGrid();
	});
		
	function openDetail(id, action) {
		var title = action == "edit" ? "编辑${comment}" : action == "add" ? "添加${comment}" : "查看${comment}";
		action =action == "edit" ? "Edit" : action == "add" ? "Edit" : "Get";
		var url="${classVar}" + action;
		if(!$.isEmpty(id)){
			url+='?id=' + id;
		}
		HT.window.openEdit(url, title, action, 'grid', 500, 500, null, null, id, true);
	}
	
	
	
	function loadGrid() {
		$('#grid').datagrid($.extend($defaultOptions,{
			url :  "listJson",
			idField : "${pkVar}",
			sortName : '${pk}',
			sortOrder : 'desc',
			striped : true,
			columns : [ [
			<#list commonList as col>
			<#assign colName=func.convertUnderLine(col.columnName)>
			{field : '${colName}',sortName : "${col.columnName}",title : '${col.getComment()}',width : 250,align : 'center',sortable : 'true'
			<#if (col.colType=="java.util.Date")>,formatter:dateTimeFormatter</#if>
			
			}, 
			</#list>
			{
				field : 'colManage',
				title : '操作',
				width : 80,
				align : 'center',
				formatter : function(value, row, index) {
					var result = "<a class='btn btn-default fa fa-edit' onClick='openDetail(\"" + row.id + "\",\"edit\")' herf='#'>编辑</a>";
					result += "<a class='btn btn-default fa fa-remove' onClick='openDetail(\"" + row.id + "\",\"get\")' herf='#'>明细</a>";
					result += "<a class='rowInLine btn btn-default fa fa-remove' action='/${system}/${package}/${classVar}/remove?${pkVar}=" + row.id + "' herf='#'>删除</a>";
					return result;
				}
			} ] ],
			onLoadSuccess : function() {
				handGridLoadSuccess();
			}
		}));
	}
</script>

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
		<script type="text/javascript">
		$(function() {
			var frm = <#noparse>$(</#noparse>'#${classVar}Form').form();
			$("a.btn.fa-save").click(function() {
				frm.ajaxForm({success:showResponse});
				if (frm.valid()) {
					<#noparse>$(</#noparse>'#${classVar}Form').submit();
				}
			});
		});

		function showResponse(responseText) {
			var resultMessage=new com.hotent.form.ResultMessage(responseText);
			if(resultMessage.isSuccess()){
				$.topCall.confirm("温馨提示",resultMessage.getMessage()+',是否继续操作',function(r){
					if(r){
						window.location.reload(true);
					}else{
						window.location.href="<#noparse>${</#noparse>ctx}/${system}/${package}/${classVar}/list.ht";
					}
				});
			}else{
				$.topCall.error(resultMessage.getMessage(),resultMessage.getCause());
			}
		}
		</script>
	</head>
	<body class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<!-- 顶部按钮 -->
			<div class="toolbar-panel col-md-13 ">
				<div class="buttons">
					<a class="btn btn-primary fa-save" href="javascript:void(0);"><span>保存</span></a>
					<a class="btn btn-primary fa-back" href="list.ht" ><span>返回</span></a>
				</div>
			</div>
			<form id="${classVar}Form" action="save.ht" method="post">
				<table class="table-form"  cellspacing="0" >
					<#list commonList as col>
						<#assign colName=func.convertUnderLine(col.columnName)>
						<tr>								
							<th><span>${col.getComment()}:</span><#if col.isNotNull><span class="required">*</span></#if></th>
							<td>
								<input class="inputText" type="text" id="${colName}" name="${colName}" value="<#noparse>${</#noparse>${classVar}.${colName}}" validate="{<#if col.isNotNull>required:true<#else>required:false</#if><#if col.colType=='String'&& col.length<1000>,maxlength:${col.length}</#if><#if col.colType=='Integer'|| col.colType=='Long'||col.colType=='Float'>,number:true<#if col.scale!=0>,maxDecimalLen:${col.scale}</#if><#if col.precision!=0>,maxIntLen:${col.precision}</#if></#if>}"  />
							</td>								
						</tr>
					</#list>
					<input type="hidden" name="${pkVar}" value="<#noparse>${</#noparse>${classVar}.${pkVar}}" />
				</table>
			</form>
		</div>
	</body>
</html>
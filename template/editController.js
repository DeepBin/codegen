<#import "function.ftl" as func>
<#assign subtables=model.subTableList>

var app = angular.module('app', ['formDirective','arrayToolService']);
app.controller("ctrl", [ '$scope', 'baseService','ArrayToolService', function($scope, baseService,ArrayTool) {
	$scope.ArrayTool=ArrayTool;
	
	var data={
	<#if subtables?exists && subtables?size != 0>
	<#list subtables as table>
	"${table.variables.classVar}":{<#assign foreignKey=func.convertUnderLine(table.foreignKey) ><#list table.columnList as col><#assign colName=func.convertUnderLine(col.columnName?lower_case)><#if !(col.isPK)&& colName?lower_case!=(foreignKey)?lower_case>"${colName}":""<#if col_has_next>,</#if></#if></#list>}
	</#list>
	</#if>
	}
	
	$scope.addRow=function(classVar){
		$scope.data[classVar +"List"].push(angular.copy(obj[classVar]))
	}

	disabledForm($scope.view);
}]);
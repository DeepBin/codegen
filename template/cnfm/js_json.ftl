<#import "function.ftl" as func>
<#assign subtables=model.subTableList>
var app = angular.module('app', [ 'formDirective', 'arrayToolService' ]);
app.controller("ctrl", [ '$scope', 'baseService', 'ArrayToolService', function($scope, baseService, ArrayTool) {
	$scope.ArrayTool = ArrayTool;
	$scope.data = {}
	disabledForm($scope.view);
	$scope.saveForm = function(url) {
		//校验表单
		if(!$("form").valid())  return false;
		// 保存数据
		baseService.saveData(url,JSON.stringify($scope.data), function(data) {
			if (data.success) {
				baseService.closeLayerAndReload();
			} else {
				msg.alert(data.msg);
			}
		});
	}; 
	$scope.closeLayer = function () {
		baseService.closeLayer();
	};
} ]);
 
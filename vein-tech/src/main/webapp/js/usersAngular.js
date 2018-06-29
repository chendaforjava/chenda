/**
 * Created by lijinduo on 2017/11/15.
 */
/**
 * Created by lijinduo on 2017/11/8.
 */
var app = angular.module('myApp', ['ui.bootstrap']);
app.controller('myCtrl',['$log', '$http', '$scope', function ($log, $http, $scope) {
    $scope.reportData = [];
    $scope.maxSize = 7;
    $scope.currentPage =0;
    $scope.totalItems = 0;
    var number = $scope.currentPage;


    $scope.pageChanged = function () {
    	
    	var deptId = $("#bumenselect option:selected").val();
	    var userName = $("#InquireNameId").val();
	    if(userName.length==0){
	    	userName='';
	    }
	    var parame ={"page":$scope.currentPage,"number":number,"deptId":deptId,"userName":userName}; 

    	
        //showLoading("正在查询");
        $http({
            method: "POST",
            url: "user/sysuserlist",
            data:JSON.stringify(parame),
            dataType : "json",
            contentType: "application/json; charset=utf-8"
        })
            .then(function (result) {
                $scope.reportData = result.data.sysuserList;
                
                $scope.totalItems = result.data.totalCount;
            });
    };
    $scope.Inital = function () {
    	 var deptId = $("#bumenselect option:selected").val();
	    var userName = $("#InquireNameId").val();
	    if(userName.length==0){
	    	userName='';
	    }
	    var parame ={"page":$scope.currentPage,"number":number,"deptId":deptId,"userName":userName}; 
        //showLoading("正在查询");
        $http({
            method: "POST",
            url: "user/sysuserlist",
            data:JSON.stringify(parame),
            dataType : "json",
            contentType: "application/json; charset=utf-8"
        }).then(function (result) {

            $scope.reportData = result.data.sysuserList;
            
            console.log(result)
            

            $scope.totalItems = result.data.totalCount;
            
            alert(2);
       
            //closeLoading();

        });
    };
    $scope.Inital();
}]);


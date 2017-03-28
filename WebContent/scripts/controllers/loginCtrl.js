angular.module('myApp').controller("loginCtrl", function ($scope, $location, $routeParams, $http, commonService) {
     
		
		var type = $routeParams.userCode;
		if (type==1){
    		$scope.userType = "Customer";
    		$scope.loginImage = "http://localhost:8080/coupons/images/customer.png";
		} 
		else if(type==2){
			$scope.userType = "Company";
			$scope.loginImage = "http://localhost:8080/coupons/images/company.png";
		}
		else if(type==3){
			$scope.userType = "Admin";
			$scope.loginImage = "http://localhost:8080/coupons/images/admin.jpg";
		}
	
	
    $scope.login = function() {
        $scope.user.clientCode = $routeParams.userCode;
        commonService.username = $scope.user.username;
        $http.post("http://localhost:8080/CouponsPhase2/rest/login", $scope.user)
        .then(
				function successCallback() {
					 if ($scope.user.clientCode==1){
		            	$location.path('/customer');	
					 } 
					 else if($scope.user.clientCode==2){
						$location.path('/company');
					 }
					 else if($scope.user.clientCode==3){
						$location.path('/admin');
					 }
			           
				},
			function errorCallback(response){
					commonService.errorFunction(response);
				}	
			);
    }
                                                                                   
});

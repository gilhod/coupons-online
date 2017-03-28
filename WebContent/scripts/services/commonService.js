angular.module('myApp').service('commonService', function($http, $location) {
	
	var thisService = this;
	var username;
	
	thisService.errorFunction = function(response){
		if(response.status==700){
			bootbox.alert(response.data.message);
		}
		else if(response.status==401){
			bootbox.alert("no valid login was found. please login to continue");
			$location.path('/');
		}
		else if(response.status==403){
			bootbox.alert("Access Denied! You are not autorzied to access this section");
		}
	};
	
	thisService.logout = function(){
		bootbox.confirm("Are you sure you want to log out?", function (result) {
			 if(result){ 
				$http.delete("http://localhost:8080/CouponsPhase2/rest/login/logout")
				 .then(
					function successCallback() {
						$location.path('/');
					},
					function errorCallback(response) {
						thisService.errorFunction(response);
					});
			 }
		 });
	};
	

		
})
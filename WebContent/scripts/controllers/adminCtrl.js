angular.module('myApp').controller("adminCtrl", function ($scope, $location, $routeParams, $http, commonService) {
    
	// initial parameters set-up
	var thisCtrl = this;
	$scope.name = commonService.username;
	$scope.showSearchResults=false;
	$scope.showUserDetails=false;
	
	// This function submit a new user to the system.
	$scope.newUserSubmit = function(isInvalid){
		if(isInvalid){
			bootbox.alert("Cannot add user. One or more fields are missing or invalid");
		}
		else{
			if($scope.userTypeAdd=="company"){
				$http.post("http://localhost:8080/CouponsPhase2/rest/company", $scope.newUser)
				.then(
					function successCallback() {
						bootbox.alert("Company was added successfully");
			            $scope.newUser={};
					},
				function errorCallback(response){
						commonService.errorFunction(response);
					}	
				);
			}	
			else if($scope.userTypeAdd=="customer"){
				$http.post("http://localhost:8080/CouponsPhase2/rest/customer", $scope.newUser)
				.then(
					function successCallback() {
						bootbox.alert("Customer was added successfully");
		            $scope.newUser={};
					},
				  	function errorCallback(response) {
						commonService.errorFunction(response);
					});
			}
		}
	};
		
		// This function gets users list from the server
		$scope.viewUsers = function() {
			$scope.userType=$scope.userTypeView;
			if($scope.userTypeView=="company"){
				$http.get("http://localhost:8080/CouponsPhase2/rest/company")
				.then(
			function successCallback(response) {
				$scope.showSearchResults=true;	
				$scope.viewShow = 2;
				$scope.users = thisCtrl.buildCompaniesArrayFromResponse(response.data);
				$scope.hideEmailViewTable = false;
				$scope.numberOfUsers = $scope.users.length; 
			},
			function errorCallback(response) {
				commonService.errorFunction(response);
				});
		}
		else if($scope.userTypeView=="customer"){
			$http.get("http://localhost:8080/CouponsPhase2/rest/customer")
			.then(
			function successCallback(response) {
				$scope.showSearchResults=true;	
				$scope.viewShow = 2;
				$scope.users = thisCtrl.buildCustomersArrayFromResponse(response.data);
				$scope.hideEmailViewTable = false;
				$scope.numberOfUsers = $scope.users.length;
			},
			function errorCallback(response) {
				commonService.errorFunction(response);
			});
		}
  };
		
		/*
		 * This function gets the user's details from the server and displays
		 * them. Input parameters: userId: ID of the user obtained from users
		 * list index: index of the user in the users list displayed on the
		 * screen. this parameter will be passed as an argument the the delete
		 * function in order to remove the user from the list, if the user is
		 * deleted.
		 */
		$scope.displayUser = function(userId, index) {
			if($scope.userType=="company"){
				$http.get("http://localhost:8080/CouponsPhase2/rest/company/"+userId)
				.then(
				function successCallback(response) {
					$scope.showUserDetails=true;
					$scope.editShow=2;
					$scope.user = response.data;
					$scope.indexToRemove = index; 
				},
			  	function errorCallback(response) {
					commonService.errorFunction(response);
				});
			}
			else if($scope.userType=="customer"){
				$http.get("http://localhost:8080/CouponsPhase2/rest/customer/"+userId)
				.then(
				function successCallback(response) {
					$scope.showUserDetails=true;
					$scope.editShow=2;
					$scope.user = response.data;
					$scope.indexToRemove = index;
				},
			  	function errorCallback(response) {
					commonService.errorFunction(response);
				});
			}
			
		  };
		  
		 //change the view to edit password view
		  $scope.editPassword=function(){
			 $scope.editShow = 3;
			 $scope.newPassword = $scope.user.password;
		 };
		  
		 
		// This function updates the user password in the system
		$scope.updatePassword = function(isEmpty, isInvalid) {
			if(isEmpty){
				bootbox.alert("Cannot update empty password");
			}
			else if(isInvalid){
				bootbox.alert("Cannot update password. Please make sure password pattern is vaild ");
			}
			else{
				if($scope.userType=="company"){
						$http.put("http://localhost:8080/CouponsPhase2/rest/company/password/"+$scope.user.id, $scope.newPassword)
						.then(
						function successCallback() {
							bootbox.alert("Password was updated successfully");
							$scope.editShow = 2;
							$scope.user.password = $scope.newPassword;
						},
						function errorCallback(response) {
							commonService.errorFunction(response);
						})
				}
				else if($scope.userType=="customer"){
					$http.put("http://localhost:8080/CouponsPhase2/rest/customer/updatePassword/"+$scope.user.id, $scope.newPassword)
					.then(
						function successCallback() {
							bootbox.alert("Password was updated successfully");
							$scope.editShow = 2;
							$scope.user.password = $scope.newPassword;
						},
						function errorCallback(response) {
							commonService.errorFunction(response);
					});
			}
		}
	  };
	  
	//change the view to edit email view
	  $scope.editEmail=function(){
		 $scope.editShow = 4;
		 $scope.newEmail = $scope.user.email;
	 };
	  
	  // This function updates the user email in the system
		$scope.updateEmail = function(isEmpty, isInvalid) {
			if(isEmpty){
				bootbox.alert("Cannot update empty email");
			}
			else if(isInvalid){
				bootbox.alert("Cannot update email. Please make sure email address is vaild ");
			}
			else{
			
				$http.put("http://localhost:8080/CouponsPhase2/rest/company/email/"+$scope.user.id, $scope.newEmail)
				.then(
				function successCallback() {
					bootbox.alert("email was updated successfully");
					$scope.editShow = 2;
					$scope.user.email = $scope.newEmail;
				},
				function errorCallback(response) {
					commonService.errorFunction(response);
					});
			}
	  };

	  	// This function delete a user from the system
		$scope.deleteUser = function() {
			bootbox.confirm("Are you sure you want to delete: " + $scope.user.name +"?", function (result) {
				 if(result){
					if($scope.userType=="company"){
						$http.delete("http://localhost:8080/CouponsPhase2/rest/company/"+$scope.user.id)
						.then(
					function successCallback() {
						bootbox.alert($scope.user.name + " was deleted successfully");
			            $scope.user = {};
			            $scope.showUserDetails=false;
			            $scope.users.splice($scope.indexToRemove, 1); // remove
																		// the
																		// user
																		// from
																		// the
																		// users
																		// list
					},
					function errorCallback(response) {
						commonService.errorFunction(response);
						});
					}
					else if($scope.userType=="customer"){
						$http.delete("http://localhost:8080/CouponsPhase2/rest/customer/"+$scope.user.id)
						.then(
						function successCallback() {
							bootbox.alert($scope.user.name + " was deleted successfully");
				            $scope.user = {};
				            $scope.showUserDetails=false;
				            $scope.users.splice($scope.indexToRemove, 1); // remove
																			// the
																			// user
																			// from
																			// the
																			// users
																			// list
						},
						function errorCallback(response) {
							commonService.errorFunction(response);
							});
					}
				 }
			 });
	  };
	  
	  // This function log out the user
	  $scope.logout = function(){
		  commonService.logout();
	  };
	  
	  // helper function used by getUsers method for companies
	  // This function make sure that the response is arrange in an array,
		// even in
	  // cases where there is only one company or no companies at all
	  thisCtrl.buildCompaniesArrayFromResponse = function(data){
		  var companiesArray;
		  // no companies in response
		   if(data==null){
			  companiesArray = [];
				return companiesArray;
		  	}
		   // more than one company in response. already array
		   else if(angular.isArray(data.company)){
			  companiesArray = data.company;
				return companiesArray;
			}
		  // one company in response
		  else{
			  companiesArray = [data.company];
				return companiesArray;
			}
	  };
	  
	  // helper function used by getUsers method for customers
	  // This function make sure that the response is arrange in an array,
		// even in
	  // cases where there is only one customer or no customers at all
	  thisCtrl.buildCustomersArrayFromResponse = function(data){
		  var customersArray;
		  
		  // no customers in response
		  if(data==null){
			  customersArray = [];
			  return customersArray;
		  	}
		  
		  // more than one customer in response. already array
		  else if(angular.isArray(data.customer)){
			  customersArray = data.customer;
			  return customersArray;
			}
		  	
		  // one customer in response
		  else{
			  customersArray = [data.customer];
			  return customersArray;
			}
	  };
	  
	  
	  
});
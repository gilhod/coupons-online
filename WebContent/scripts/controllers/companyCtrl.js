angular.module('myApp').controller("companyCtrl", function ($scope, $location, $routeParams, $http, commonService) {

	// initial parameters set-up
	var thisCtrl = this;
	$scope.name = commonService.username;
	$scope.showSearchResults=false;
	$scope.showCouponDetails=false;
	$scope.showAddCouponImage = false;
	
	//current time (used in start date validation)
	$scope.today = new Date();
	
	// get coupon types from the server for select-list
	$http.get("http://localhost:8080/CouponsPhase2/rest/list-coupon-types")
	.then(
	function successCallback(response) {
		$scope.types = response.data;
		},
	function errorCallback(response){
			commonService.errorFunction(response);
		}	
	);
	
	// This function submit a new coupon to the system.
	$scope.addCouponSubmit = function(isInvalid){
		if(isInvalid){
			bootbox.alert("Cannot add coupon. One or more fields are missing or invalid");
		}
		else{
		// convert dates to miliseconds before assigning to coupon
		$scope.coupon.startDate = $scope.startDate.getTime();
		$scope.coupon.endDate = $scope.endDate.getTime();
		
		//set coupon image to default image
		$scope.coupon.image="http://localhost:8080/coupons/images/default.png";
		
		$http.post("http://localhost:8080/CouponsPhase2/rest/coupon", $scope.coupon)
		.then(
			function successCallback(response) {
				$scope.couponId = response.data.couponId;		 		        
				$scope.showAddCouponImage = true;
				},
			function errorCallback(response){
					commonService.errorFunction(response);
				}	
	);
		}
	};
		
	  $scope.uploadCouponImage = function(){
		if($scope.imageSelect=="default"){
			$scope.coupon={};
			$scope.showAddCouponImage = false;
		}
		else if($scope.imageSelect=="upload"){
		  var file = $scope.image;
		  var fd = new FormData();
		  fd.append('file', file);
	      $http.post("http://localhost:8080/CouponsPhase2/rest/coupon/image/"+$scope.couponId, fd, {
	             transformRequest: angular.identity,
	             headers: {'Content-Type': undefined}
	          })
	          .then(
	  				function successCallback() {
	  					$scope.coupon={};
	  					bootbox.alert("image uploded successfully!");
	  					$scope.showAddCouponImage = false;
	  				},
	  			function errorCallback(response){
	  					commonService.errorFunction(response);
	  				}	
	  			);
		  }
	  }
		
		// This function gets and display coupon list from the server, according
		// to filters the user apply
		$scope.getCoupons = function() {
			// no filter
			if($scope.searchType ==="1"){ 
				 $http.get("http://localhost:8080/CouponsPhase2/rest/coupon/company")
				.then(
				function successCallback(response) {
					$scope.showSearchResults=true;
					$scope.coupons=thisCtrl.buildCouponsArrayFromResponse(response.data);
					$scope.numberOfCoupons = $scope.coupons.length;
				},
				function errorCallback(response){
					commonService.errorFunction(response);
				}	
				);
			 }
			  else if($scope.searchType ==="2"){
				  	$http.get("http://localhost:8080/CouponsPhase2/rest/coupon/company/type/"+$scope.type)
				  	.then(
					function successCallback(response) {
						$scope.showSearchResults=true;
						$scope.coupons=thisCtrl.buildCouponsArrayFromResponse(response.data);
						$scope.numberOfCoupons = $scope.coupons.length;
					},
					function errorCallback(response){
						commonService.errorFunction(response);
					}	
					);
			  }
			  else if($scope.searchType ==="3"){
				  $http.get("http://localhost:8080/CouponsPhase2/rest/coupon/company/price/"+$scope.price)
				  .then(
					function successCallback(response) {
						$scope.showSearchResults=true;
						$scope.coupons=thisCtrl.buildCouponsArrayFromResponse(response.data);
						$scope.numberOfCoupons = $scope.coupons.length;
					},
					function errorCallback(response){
						commonService.errorFunction(response);
					}	
					);
			  }
			  else if($scope.searchType ==="4"){
				  var expirationMilis = $scope.expiration.getTime();
				  $http.get("http://localhost:8080/CouponsPhase2/rest/coupon/company/date/"+expirationMilis)
				  .then(
					function successCallback(response) {
						$scope.showSearchResults=true;
						$scope.coupons=thisCtrl.buildCouponsArrayFromResponse(response.data);
						$scope.numberOfCoupons = $scope.coupons.length;
					},
					function errorCallback(response){
						commonService.errorFunction(response);
					}	
					);
			  }
		  };
		
		/* This function gets the coupons's details from the server and displays them.
		Input parameters:
		id: ID of the coupon obtained from users list
		index: index of the coupon in the coupons list displayed on the screen. 
		this parameter will be passed as an argument the the delete function in order
		to remove the coupon from the list, if the coupon is deleted. */
		$scope.getCoupon = function(id, index) {
			$http.get("http://localhost:8080/CouponsPhase2/rest/coupon/"+id)
			.then(
				function successCallback(response) {
					$scope.showCouponDetails=true;
					$scope.editShow = 2;
					$scope.coupon = response.data;
					$scope.indexToRemove = index;
					
					//set minimum date for end-date updating
					if($scope.today.getTime() >  $scope.coupon.startDate){
						$scope.minEndDate = $scope.today.getTime();
					}
					else{
						$scope.minEndDate = $scope.coupon.startDate;
					}
				},
				function errorCallback(response){
					commonService.errorFunction(response);
				}	
				);
		  };
		  
		  //change to edit end date view
		  $scope.editEndDate = function(){
			  $scope.editShow = 3;
			  $scope.newEndDate = $scope.coupon.endDate;
		  };
		  
		  
		  $scope.updateEndDate = function(isEmpty){
		  		if(isEmpty){
					bootbox.alert("Cannot update empty end date");
				}
				else{
			  		var endDateMilis = $scope.newEndDate.getTime();
			  		$http.put("http://localhost:8080/CouponsPhase2/rest/coupon/end-date/"+$scope.coupon.id, endDateMilis)
			  		.then(
					function successCallback(response) {
						bootbox.alert("End date updated successfully");
						$scope.editShow = 2;
						$scope.coupon.endDate = endDateMilis;
					},
					function errorCallback(response){
						commonService.errorFunction(response);
					}	
					);
				}
		  	};
		  	
		  	//change to edit price view
		  	$scope.editPrice = function(){
				  $scope.editShow = 4;
				  $scope.newPrice = $scope.coupon.price;
			  };
		  	
		  	$scope.updatePrice = function(isEmpty, isValid){
		  		if(isEmpty){
					bootbox.alert("Cannot update empty price");
				}
				else if(isInvalid){
					bootbox.alert("Cannot update price. Please make sure price is a positive number ");
				}
				else{
			  		$http.put("http://localhost:8080/CouponsPhase2/rest/coupon/price/"+$scope.coupon.id, $scope.newPrice)
					.then(
					function successCallback(response) {
						bootbox.alert("price updated successfully");
						$scope.editShow = 2;
						$scope.coupon.price = $scope.newPrice;
					},
					function errorCallback(response){
						commonService.errorFunction(response);
					}	
					);
				}
		  	};
			
			$scope.deleteCoupon = function(id) {
				
				bootbox.confirm("Are you sure you want to delete: " + $scope.coupon.title +"?", function (result) {
					 if(result){
						 $http.delete("http://localhost:8080/CouponsPhase2/rest/coupon/"+id)
							.then(
							function successCallback(response) {
								bootbox.alert($scope.coupon.title + " was deleted successfully");
					            $scope.coupon = {};
					            $scope.showCouponDetails=false;
					            $scope.coupons.splice($scope.indexToRemove, 1);
							},
							function errorCallback(response){
								commonService.errorFunction(response);
							}	
							);
					 }
				 });
				
				
		  };
		  

		  
		  
		  
		  //This function log out the user
		  $scope.logout = function(){
			  commonService.logout();
		  };
		  
		  //helper function used by getCoupons method
		  //This function make sure that the response is arrange in an array, even in 
		  //cases where there is only one coupon or no coupons
		  thisCtrl.buildCouponsArrayFromResponse = function(data){
			  var couponsArray;
			  
			  //no coupons in response	
			  if(data==null){
			  		couponsArray = [];
			  		return couponsArray; 
			  	}
			  
			  //more than one coupon in response. already array
			  else if(angular.isArray(data.coupon)){
			  		couponsArray = data.coupon;
			  		return couponsArray;
				}
			  	
			  //one coupon in response
			  else{
				  couponsArray = [data.coupon];
				  return couponsArray;
				}
			  	
		  };
		  
});
angular.module('myApp').controller("customerCtrl", function ($scope, $location, $routeParams, $http, commonService) {
	
		// initial parameters set-up
		var thisCtrl = this;	
		$scope.name = commonService.username;
		$scope.showSearchResults=false;
		$scope.showPurchaseDetails=false;
		
		// This function gets all the coupons the customer hasn't purchased yet
		$http.get("http://localhost:8080/CouponsPhase2/rest/purchase/coupons-for-customer")
		.then(
			function successCallback(response) {
				$scope.coupons = thisCtrl.buildCouponsArrayFromResponse(response.data);
				},
			function errorCallback(response){
					commonService.errorFunction(response);
				}	
			);
		
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
  
	
    /*
	 * This function commits a purchase of a coupon by a customer. Fields
	 * parameters: - couponID: id number of the coupon to be purchased - title:
	 * title of the coupon to be purchased - index: index in the ng-repeat, in
	 * order to be removed from the screen after purchase.
	 */
    $scope.buy = function(couponID, title, index) {
    	bootbox.confirm("buy coupon: " + title +"?", function (result) {
			 if(result){
				 $http.post("http://localhost:8080/CouponsPhase2/rest/purchase/"+couponID)
			    	.then(
					function successCallback(response) {
						$scope.coupons.splice(index, 1); // remove purchased coupon from
															// display list
						bootbox.alert("coupon purchase completed sccessfully");
						},
					function errorCallback(response){
							commonService.errorFunction(response);
						}	
					);
			 }
		 });
    };
    
    
    
  
    // This function gets and display purchased coupon list from the server,
	// according
	// to filters the user apply
    $scope.getPurchases = function() {
		  
    	// no filter
    	if($scope.searchType ==="1"){
			  $http.get("http://localhost:8080/CouponsPhase2/rest/purchase")
			  	.then(
				function successCallback(response) {
					$scope.showSearchResults=true;
					$scope.purchases = thisCtrl.buildPurchasesArrayFromResponse(response.data);
					$scope.numberOfPurchases = $scope.purchases.length;
					},
				function errorCallback(response){
						commonService.errorFunction(response);
					}	
				);
		  }
    	
    	// filter by type
		else if($scope.searchType ==="2"){
			  $http.get("http://localhost:8080/CouponsPhase2/rest/purchase/type/"+$scope.type)
			  .then(
				function successCallback(response) {
					$scope.showSearchResults=true;
					$scope.purchases = thisCtrl.buildPurchasesArrayFromResponse(response.data);
					$scope.numberOfPurchases = $scope.purchases.length;
					},
				function errorCallback(response){
						commonService.errorFunction(response);
					}	
				);
			 		  }
    		
    		// filter by price
		  else if($scope.searchType ==="3"){
			  $http.get("http://localhost:8080/CouponsPhase2/rest/purchase/price/"+$scope.price)
			  .then(
				function successCallback(response) {
					$scope.showSearchResults=true;
					$scope.purchases = thisCtrl.buildPurchasesArrayFromResponse(response.data);
					$scope.numberOfPurchases = $scope.purchases.length;
					},
				function errorCallback(response){
						commonService.errorFunction(response);
					}	
				);
		  }
	  };
	  
	  	/* 
	   	This function gets the purchase's details from the server and displays them.
		Input parameters:
		purchaseNumber: number of the purchase to be obtained from users list
		 */
	  $scope.getPurchase = function(purchaseNumber){
	    	$http.get("http://localhost:8080/CouponsPhase2/rest/purchase/"+purchaseNumber)
			  .success(function (data) {
				  $scope.purchase = data;
				  $scope.showPurchaseDetails=true;
			  }).error(function () {
					alert("Error!");
				});
	    };
	  
	    
	  //This function log out the user
		  $scope.logout = function(){
			  commonService.logout();
		  };
	  
		  //helper function - coupons for purchase 
		  //This function make sure that the response is arrange in an array, even in 
		  //cases where there is only one item or no items at all
		  thisCtrl.buildCouponsArrayFromResponse = function(data){
			  var couponsArray;
			  //more than one coupon in response. already array
			  if(angular.isArray(data.couponThumbnail)){
			  		couponsArray = data.couponThumbnail;
				}
			  //no coupons in response	
			  else if(data==null){
			  		couponsArray = [];
			  	}	
			  //one coupon in response
			  else{
				  couponsArray = [data.couponThumbnail];
				}
			  	return couponsArray;
		  };
		  
		  
	// helper function used by getCoupons method
	  // This function make sure that the response is arrange in an array,
		// even in
	  // cases where there is only one coupon or no coupons
	  thisCtrl.buildPurchasesArrayFromResponse = function(data){
		  var purchasesArray;
		  
		// no purchases in response
		  if(data==null){
			  purchasesArray = [];return purchasesArray;
		  	}
		  
		  // more than one purchase in response. already array
		  else if(angular.isArray(data.purchase)){
			  purchasesArray = data.purchase;
			  return purchasesArray;
			}
		  	
		  // one purchase in response
		  else{
			  purchasesArray = [data.purchase];
			  return purchasesArray;
			}
		  	
	  };
	  

	
});
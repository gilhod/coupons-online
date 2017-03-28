angular.module('myApp').config(function($routeProvider) {
    
	$routeProvider
	
    .when("/", {
        templateUrl : "templates/main.htm",
    })
    
    .when("/login/:userCode", {
    	templateUrl : "templates/login.htm",
        controller : "loginCtrl"
    })
    
    .when("/admin", {
    	templateUrl : "templates/admin/users.htm",
        controller : "adminCtrl"
    })
    
    .when("/admin/add", {
    	templateUrl : "templates/admin/addUser.htm",
        controller : "adminCtrl"
    })
    .when("/company", {
    	templateUrl : "templates/company/coupons.htm",
        controller : "companyCtrl"
    })
    .when("/company/add", {
    	templateUrl : "templates/company/addCoupon.htm",
        controller : "companyCtrl"
    })
    .when("/customer", {
    	templateUrl : "templates/customer/store.htm",
        controller : "customerCtrl"
    })
    .when("/customer/purchases", {
    	templateUrl : "templates/customer/purchases.htm",
        controller : "customerCtrl"
    });
    
    
    
});
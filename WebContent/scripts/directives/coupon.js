angular.module('myApp').directive('coupon', function() {
	return{
		restrict: 'E',
		templateUrl: 'templates/customer/couponThumbnail.htm',
		replace: true
	}
})
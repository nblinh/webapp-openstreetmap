var app = angular.module('mainApp', ['ngRoute','rootController', 'zoningController', 'projectionController',
    'travelTimeController', 'travelDataController', 'zonalDataController']);

app.config([ '$routeProvider', function($routeProvider) {

	$routeProvider.
	when('/zoning', {
		templateUrl : "app/zoning/zoning.html",
		controller : 'zoningController'
	}).
	when('/projection', {
        templateUrl : "app/projection/projection.html",
        controller : 'projectionController'
    }).
    when('/travelTime', {
        templateUrl : "app/travelTime/travelTime.html",
        controller : 'travelTimeController'
    }).
    when('/travelData', {
        templateUrl : "app/travelData/travelData.html",
        controller : 'travelDataController'
    }).
    when('/zonalData', {
        templateUrl : "app/zonalData/zonalData.html",
        controller : 'zonalDataController'
    }).
	otherwise({
		redirectTo : ''
	});
}]);

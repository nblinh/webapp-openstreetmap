var app = angular.module('rootController', ['menuController', 'menubarController']);
app.controller('rootController', function($scope, $rootScope){

    $scope.init = function(){
    }

    // Méthode de blocage et déblocage de l'IHM
   /*$rootScope.block = function(id, text, attributes){
       var view = id ? id : '#carsharing';
       $rootScope.blockedView = view;
       var message = text ? text : '<div><h2 style="width: 100%;">Please wait...</h2><img src="./assets/images/loading.gif" /></div>';
       var options = {message: message, showOverlay: true};
       if(attributes) options = $.extend(attributes, options);
       $(view).block(options);
   };

   $rootScope.unblockView = function(view){
       $(view).unblock();
   };

   $rootScope.unblock = function(){
       $($rootScope.blockedView).unblock();
   };*/
});
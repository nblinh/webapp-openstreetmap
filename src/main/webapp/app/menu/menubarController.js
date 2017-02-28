var app =angular.module('menubarController', ['generationProjectionController', 'projectionODMatrixController']);
app.controller('menubarController', function($scope) {
    $scope.projectGenCalibration = function(){
        $('#myModal').foundation('reveal', 'open');
    }

    $scope.projectODMatrix = function(){
        $('#projectionODMatrixModal').foundation('reveal', 'open');
    }
});
var app = angular.module('menuController', ['menuService', 'angularTreeview']);
app.controller('menuController', function($scope, $rootScope, $location, getMenu, shareObject, $route){

    $scope.init = function(){
        getMenu.charge({},function(data){
            $scope.tree = [];
            $scope.tree.push(data.rootNode);
            $rootScope.projections = data.generationProjections;
            $scope.zonalDatas = data.zonalDatas;
            $scope.interZonalDatas = data.interZonalDatas;
            $rootScope.travelDatas = data.travelDatas;
            $location.path( "/zoning" );
        });
    }

    $scope.getZonalData=function(name){
        shareObject.ongletActif="zonalData";
        shareObject.zonalDataName = name;
        shareObject.attributeNameIndex = "-1";
        if($location.path()=="/zonalData"){
            //$route.reload();
            $rootScope.getZonningId();
        }else{
            $location.path( "/zonalData" );
        }
    }

    $scope.getTravelTime=function(name){
        shareObject.ongletActif="travelTime";
        shareObject.travelTimeName = name;
        if($location.path()=="/travelTime"){
            $route.reload();
        }else{
            $location.path( "/travelTime" );
        }
    }

    $scope.getTravelData=function(name){
        shareObject.ongletActif="odMatrices";
        shareObject.travelDataName = name;
        if($location.path()=="/travelData"){
            $route.reload();
        }else{
            $location.path( "/travelData" );
        }
    }


    $scope.getProjection=function(name){
        shareObject.ongletActif="projection";
        shareObject.projectionName = name;
        if($location.path()=="/projection"){
            $route.reload();
        }else{
            $location.path( "/projection" );
        }
    }
});
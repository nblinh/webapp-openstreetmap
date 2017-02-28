var app = angular.module('zoningController', ['zoningModel', 'zoningService', 'menuService', 'utilService']);
app.controller('zoningController', function($scope, $rootScope, $http, $timeout,$compile,
    getZoning, shareObject, MapUtils){

    $scope.init=function(){

        if(shareObject.zoningId==undefined){
            shareObject.map = MapUtils.initMap();
            shareObject.info =  MapUtils.initInfo(shareObject.map);
            shareObject.zoningId="-1";
        }
        $rootScope.getZoning();
    };

    $rootScope.getZoning=function(){
        getZoning.charge({'id':shareObject.zoningId},function(data){
            $scope.features=data.features;
            MapUtils.setPolygon($scope.features, shareObject, onEachFeature, styleZoning);
            if(shareObject.zoningId=="-1"){
                shareObject.zoningId=data.id;
            }
        });
    }

    function onEachFeature(feature, layer,data) {
        layer.on({
            mouseover: highlightFeature,
            mouseout: resetHighlight,
        });
    }


    function highlightFeature(e) {
        var layer = e.target;

        layer.setStyle({
            weight: 1.5,
            color: 'black',
        });

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.bringToFront();
        }

        shareObject.info.update(layer.feature.properties);
    }

    function resetHighlight(e) {
        shareObject.geojson.resetStyle(e.target);
        shareObject.info.update();
    }

    function styleZoning(feature) {
        return { weight: 0.5};
    }

});
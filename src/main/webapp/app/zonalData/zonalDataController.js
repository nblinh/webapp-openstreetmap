var app = angular.module('zonalDataController', ['zoningService', 'menuService', 'utilService']);
app.controller('zonalDataController', function($scope, $rootScope, $http, $timeout, $compile,
    getZonningZonalData, shareObject, MapUtils){

    var grades = [0, 1000, 2000, 5000, 10000, 20000, 50000, 100000];

    $scope.init=function(){
        shareObject.map = MapUtils.initMap();
        shareObject.info =  MapUtils.initInfo(shareObject.map);
        $scope.attributeNameIndex="0";
        $rootScope.getZonningId();
        MapUtils.addLegend(grades, shareObject.map);
    };

    $rootScope.getZonningId = function(){
        if(shareObject.attributeNameIndex==undefined||shareObject.attributeNameIndex=="-1"){
            getZonningZonalData.charge({'id':shareObject.zoningId, 'zonalDataName':shareObject.zonalDataName},function(data){
                $scope.features=data.features;
                MapUtils.setPolygon($scope.features, shareObject, onEachFeature, style);
                $scope.attributeNames = data.attributeNames;
            });
        }
    }

    $scope.changeParam = function(){
        getZonningZonalData.charge({'id':shareObject.zoningId, 'zonalDataName':shareObject.zonalDataName,
            'attributeNameIndex': parseInt($scope.attributeNameIndex)},function(data){
            for(var i=0;i<$scope.features.length;i++){
                $scope.features[i].properties = data.features[i].properties;
            }
            MapUtils.setPolygon($scope.features, shareObject, onEachFeature, style);
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

    function style(feature) {
        return {
            fillColor: MapUtils.getColor(feature.properties.attribute, grades),
            weight: 1,
            opacity: 1,
            fillOpacity: 0.7
        };
    }
});
var app = angular.module('projectionController', ['generationService', 'menuService', 'utilService']);
app.controller('projectionController', function($scope, $rootScope, $timeout,$compile,
    getProjection, shareObject, MapUtils){

    var grades = [0, 100000, 200000, 500000, 1000000, 2000000, 5000000, 10000000];

    $scope.init=function(){
        shareObject.map = MapUtils.initMap();
        shareObject.info =  MapUtils.initInfo(shareObject.map);
        $scope.emissionAttractionList=["emission", "attraction"];
        $scope.emissionAttraction="emission";
        $scope.purposeIndex="0";
        getGenerationProjection();
        MapUtils.addLegend(grades, shareObject.map);
    };

    $scope.changeParam = function(){
        getProjection.charge({'name':shareObject.projectionName,
            'emisAttrType':$scope.emissionAttraction, 'purposeIndex':parseInt($scope.purposeIndex)},function(data){
            for(var i=0;i<data.features.length;i++){
                $scope.features[i].properties = data.features[i].properties;
            }
            MapUtils.setPolygon($scope.features, shareObject, onEachFeature, style);
        });
    }

    function getGenerationProjection(){
        getProjection.charge({'name':shareObject.projectionName, 'purposeIndex':0},function(data){
            $scope.features=data.features;
            $scope.purposes = data.purposes;
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
        shareObject.info.addInfo($scope.emissionAttraction, layer.feature.properties.emissionAttraction);
    }

    function resetHighlight(e) {
        shareObject.geojson.resetStyle(e.target);
        shareObject.info.update();
    }


    function style(feature) {
        return {
            fillColor: MapUtils.getColor(feature.properties.emissionAttraction, grades),
            weight: 1.5,
            opacity: 1,
            fillOpacity: 0.7
        };
    }

});
var app = angular.module('travelTimeController', ['travelTimeService', 'menuService', 'utilService']);
app.controller('travelTimeController', function($scope, $rootScope, $timeout,$compile, getTravelTime, shareObject, MapUtils, TimeUtils){

    var grades = [0, 900, 1800, 3600, 7200];
    var hours = ['0h', '15m','30m', '1h', '2h'];

    $scope.init=function(){
        shareObject.map = MapUtils.initMap();
        shareObject.info =  MapUtils.initInfo(shareObject.map);
        $scope.segmentIndex="0";
        getTravelTimeValue();
        MapUtils.addLegendLine(grades, hours, shareObject.map);
    };

    $scope.changeParam = function(){
        getTravelTime.charge({'name':shareObject.travelTimeName, 'segmentIndex':parseInt($scope.segmentIndex)},function(data){
            for(var i=0;i<data.features.length;i++){
                $scope.features[i].properties = data.features[i].properties;
            }
            MapUtils.setPolygon($scope.features, shareObject, onEachFeature, style);
        });
    }


    function getTravelTimeValue(){
        getTravelTime.charge({'name':shareObject.travelTimeName},function(data){
            $scope.features=data.features;
            $scope.mapIdPoint = data.mapIdPoint;
            $scope.segments = data.segments;
            MapUtils.setPolygon($scope.features, shareObject, onEachFeature, style);
        });
    }


    var zoneSelected=0;

    function onEachFeature(feature, layer,data) {
        // does this feature have a property named popupContent?
        /*if (feature.properties && feature.properties.name) {
            layer.bindPopup(feature.properties.name);
        }*/
        layer.on({
           // mouseover: highlightFeature,
            mouseover: highlightZone,
            mouseout: resetHighlight,
            //click: zoomToFeature
            click: selectZone
        });
    }

    var line = null;
    function highlightZone(e){
        if(zoneSelected==0){
            highlightFeature(e);
        }else{
            line=MapUtils.drawLine(e.target.feature.properties.XY[1], e.target.feature.properties.XY[0],
                $scope.mapIdPoint[zoneSelected][1], $scope.mapIdPoint[zoneSelected][0],
                e.target.feature.properties.mapPointValue[zoneSelected], grades);
            shareObject.map.addControl(line);
            shareObject.info.update(e.target.feature.properties);
            shareObject.info.addInfo('time', TimeUtils.secondsToHms(e.target.feature.properties.mapPointValue[zoneSelected]));
            if(zoneSelected!=e.target.feature.properties.id){
                e.target.setStyle({
                    color: 'red',
                });
            }
        }
    }

    function selectZone(e) {
        highlightFeature(e)
        if(zoneSelected==e.target.feature.properties.id){
            zoneSelected=0;
        }else{
            zoneSelected=e.target.feature.properties.id;
        }

    }

    var lines = [];
    function highlightFeature(e) {
        var layer = e.target;

        angular.forEach($scope.mapIdPoint, function(value, key) {
            var line=MapUtils.drawLine(layer.feature.properties.XY[1], layer.feature.properties.XY[0], value[1], value[0],
                layer.feature.properties.mapPointValue[key], grades);
            lines.push(line);
            shareObject.map.addControl(line);
        });

        layer.setStyle({
            //weight: 5,
            color: 'black',
            //dashArray: '',
            //fillOpacity: 0.7
        });

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.bringToFront();
        }

        shareObject.info.update(layer.feature.properties);
    }

    function resetHighlight(e) {
        if(zoneSelected==0||zoneSelected!=e.target.feature.properties.id){
            for(var i=0;i<lines.length;i++){
                shareObject.map.removeControl(lines[i]);
            }
            shareObject.geojson.resetStyle(e.target);
            shareObject.info.update();
        }
        if(zoneSelected!=0&&line!=null){
            shareObject.map.removeControl(line);
        }

    }

    /*function zoomToFeature(e) {
        shareObject.map.fitBounds(e.target.getBounds());
    }*/

    function style(feature) {
        return {
            weight: 1,
        };
    }
});
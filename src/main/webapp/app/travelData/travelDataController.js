var app = angular.module('travelDataController', ['travelDataService', 'menuService', 'utilService']);
app.controller('travelDataController', function($scope, $rootScope, $timeout,$compile, $location,
        getZoningTravelData, shareObject, MapUtils){

    var grades = [0, 100, 200, 400, 800];
    var colors = ["Blue", "Chartreuse", "Crimson", "Cyan", "DarkGreen", "HotPink ", "Lime", "Orange", "Red", "Yellow"];

    $scope.init=function(){
        $scope.PC=true;
        $scope.SM=true;
        $scope.PT=true;
        shareObject.map = MapUtils.initMap();
        shareObject.info =  MapUtils.initInfo(shareObject.map);
        MapUtils.addLegendLine(grades, grades, shareObject.map);
        $scope.purposeIndex="0";
        $scope.selectModes=[];
        gettravelDataValue();
    };

    $scope.changeParam = function(){
        getZoningTravelData.charge({'id':shareObject.zoningId, 'travelDataName':shareObject.travelDataName, 'purposeIndex':parseInt($scope.purposeIndex)},function(data){
            for(var i=0;i<data.features.length;i++){
                $scope.features[i].properties = data.features[i].properties;
            }
            MapUtils.setPolygon($scope.features, shareObject, onEachFeature, style);
        });
    }

    function gettravelDataValue(){
        getZoningTravelData.charge({'id':shareObject.zoningId, 'travelDataName':shareObject.travelDataName},function(data){
            $scope.features=data.features;
            $scope.mapIdPoint = data.mapIdPoint;
            $scope.purposes = data.purposes;
            $scope.modes = data.modes;
            MapUtils.setPolygon($scope.features, shareObject, onEachFeature, style);
            transformeSelect();
        });
    }
    $scope.test = function(){
        //console.log($scope.selectModes);

        /*angular.forEach(angular.element("select"), function(value, key){
            var elm = angular.element(value);
            if(elm.context.id.indexOf("modeTransport")==0){
                $(elm).select2();
                $(".select2-container--default .select2-selection--multiple .select2-selection__choice").append(" background: Blue; ")
                //$(".select2-selection__rendered" ).remove();
                //$(".select2-selection--multiple").append("<span style='color:#cccccc'>Category</span><span class='select2_arrow' role='presentation'><b role='presentation'></b></span>");

                //.select2-container--default .select2-selection--multiple .select2-selection__choice:nth-child(1) { background: Blue; }
            }
        });*/
    }

    function transformeSelect(){
        angular.forEach(angular.element("select"), function(value, key){
            var elm = angular.element(value);
            if(elm.context.id.indexOf("modeTransport")==0){
                $(elm).select2();
                //$(".select2-selection__rendered" ).remove();
                //$(".select2-selection--multiple").append("<span style='color:#cccccc'>Category</span><span class='select2_arrow' role='presentation'><b role='presentation'></b></span>");

                //.select2-container--default .select2-selection--multiple .select2-selection__choice:nth-child(1) { background: Blue; }
            }
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
        line = null;
        if(zoneSelected==0){
            highlightFeature(e);
        }
    }

    function selectZone(e) {
        //highlightFeature(e)
        if(zoneSelected==e.target.feature.properties.id){
            zoneSelected=0;
        }else{
            zoneSelected=e.target.feature.properties.id;
        }

    }

    var lines = [];
    var lineMode=null;
    function highlightFeature(e) {
        var layer = e.target;

        for(var i=0;i<$scope.selectModes.length;i++){
            angular.forEach(layer.feature.properties.mapPointValue[parseInt($scope.selectModes[i])], function(value, key) {
                lineMode=MapUtils.drawColorLine(layer.feature.properties.XY[1], layer.feature.properties.XY[0],
                        $scope.mapIdPoint[key][1], $scope.mapIdPoint[key][0], value, grades, colors[parseInt($scope.selectModes[i])]);
                lines.push(lineMode);
                shareObject.map.addControl(lineMode);
            });
        }
        layer.setStyle({
            color: 'black',
        });

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.bringToFront();
        }

        shareObject.info.update(layer.feature.properties);
    }

    function resetHighlight(e) {
        if(zoneSelected==0){
            for(var i=0;i<lines.length;i++){
                shareObject.map.removeControl(lines[i]);
            }
            lines = [];
            shareObject.geojson.resetStyle(e.target);
            //shareObject.info.update();
        }
        /*if(zoneSelected!=0&&line!=null){
            shareObject.map.removeControl(line);
        }*/

    }

    function zoomToFeature(e) {
        shareObject.map.fitBounds(e.target.getBounds());
    }

    function style(feature) {
        return {
            //fillColor: getColor(feature.properties.emissionAttraction),
            weight: 1,

        };
    }
});
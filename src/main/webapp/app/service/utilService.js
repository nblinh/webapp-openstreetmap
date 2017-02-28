angular.module("utilService", ['menuService'])
.factory('MapUtils', function(){
    var MapUtils =  {
        initMap: function(){
            var map = L.map('mapid').setView([48.712766, 2.1907383], 9);
            L.tileLayer('https://api.mapbox.com/styles/v1/mapbox/outdoors-v9/tiles/256/{z}/{x}/{y}?access_token={accessToken}',{
                attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
                maxZoom: 18,
                id: 'your.mapbox.project.id',
                accessToken: 'pk.eyJ1Ijoib3dueW91IiwiYSI6ImNpcnhodTB3eTAwMTYydHAycWRpeXlpdTAifQ.NEBQ4bbBO6i7bAs-IgMy3A'
            }).addTo(map);
            return map;
        },

        initInfo: function(map){
            var info = L.control();
            info.onAdd = function (map) {
                this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
                this.update();
                return this._div;
            };

            // method that we will use to update the control based on feature properties passed
            info.update = function (props) {
                this._div.innerHTML = '<h4>Region informations</h4>' +  (props ?
                    '<b> name : ' + props.name.substring(0,15) + '</b><br /> region : ' + props.superZone +
                    '<br/> id : '+props.id
                    : 'Hover over a region');
            };

            info.addInfo = function(name, value){
                this._div.innerHTML = this._div.innerHTML+'<br/> '+name+' : '+value;
            }
            info.addTo(map);

            return info;
        },

        setPolygon: function(geojsonFeature, shareObject, onEachFeature, style){
            if(shareObject.geojson!=undefined){
                shareObject.map.removeControl(shareObject.geojson);
            }
            shareObject.geojson = L.geoJSON(geojsonFeature, {
                    onEachFeature: onEachFeature,
                    style: style
                }
            );
            shareObject.map.addControl(shareObject.geojson);
        },

        circleTest: function(point, map){
            var circle = L.circle(point, {
                color: 'red',
                fillColor: '#f03',
                fillOpacity: 0.5,
                radius: 500
            }).addTo(shareObject.map);
        },

        getColor: function(d, grades) {
            /*return d > 100000 ? '#800026' :
                     d > 50000  ? '#BD0026' :
                     d > 20000  ? '#E31A1C' :
                     d > 10000  ? '#FC4E2A' :
                     d > 5000   ? '#FD8D3C' :
                     d > 2000   ? '#FEB24C' :
                     d > 1000   ? '#FED976' :
                                  '#FFEDA0';*/
            return d > grades[7] ? '#F70707' :
                   d > grades[6] ? '#F75B07' :
                   d > grades[5] ? '#F79F07' :
                   d > grades[4] ? '#F7D707' :
                   d > grades[3] ? '#D3F707' :
                   d > grades[2] ? '#8BF707' :
                   d > grades[1] ? '#43F707' :
                   d > grades[0] ? '#07F70F':
                                   '#FFEDA0';
        },

        addLegend: function( grades, map){
            var legend = L.control({position: 'bottomright'});

            legend.onAdd = function (map) {

                var div = L.DomUtil.create('div', 'info legend');
                   // grades = [0, 1000, 200, 500, 1000, 2000, 5000, 10000],
                    //labels = [];

                // loop through our density intervals and generate a label with a colored square for each interval
                for (var i = 0; i < grades.length; i++) {
                    div.innerHTML +=
                        '<i style="background:' + MapUtils.getColor((grades[i] + 1), grades) + '"></i> ' +
                        grades[i] + (grades[i + 1] ? '&ndash;' + grades[i + 1] + '<br>' : '+');
                }

                return div;
            };

            legend.addTo(map);
        },

        addLegendLine: function(grades, hours, map){
            var legend = L.control({position: 'bottomright'});

                legend.onAdd = function (map) {

                    var div = L.DomUtil.create('div', 'info legend');

                    // loop through our density intervals and generate a label with a colored square for each interval
                    for (var i = 0; i < grades.length; i++) {
                        div.innerHTML +=
                            '<hr style="border-top: solid red ' + MapUtils.getWeight(grades[i] + 1, grades) + 'px; margin:0;" width="50px"> ' +
                            hours[i] + (hours[i + 1] ? '&ndash;' + hours[i + 1]  : '+');
                    }

                    return div;
                };

            legend.addTo(map);
        },

        getWeight:function(d, grades) {
            return d > grades[4] ? 8 :
                   d > grades[3] ? 4 :
                   d > grades[2] ? 2 :
                   d > grades[1] ? 1 :
                   d > grades[0] ?0.5:
                   0;
        },

        drawLine:function(x1, y1, x2, y2, d, grades){
            var pointA = new L.LatLng(x1, y1);
            var pointB = new L.LatLng(x2, y2);
            var pointList = [pointA, pointB];

            var firstpolyline = new L.Polyline(pointList, {
                color: 'red',
                weight: MapUtils.getWeight(d, grades),
            });
            return firstpolyline;
        },

        drawColorLine:function(x1, y1, x2, y2, d, grades, cl){
            var pointA = new L.LatLng(x1, y1);
            var pointB = new L.LatLng(x2, y2);
            var pointList = [pointA, pointB];

            var firstpolyline = new L.Polyline(pointList, {
                color: cl,
                weight: MapUtils.getWeight(d, grades),
            });
            return firstpolyline;
        }

    }
    return MapUtils;
})
.factory('TimeUtils', function(){
    return{
        secondsToHms: function(d) {
            d = Number(d);
            var h = Math.floor(d / 3600);
            var m = Math.floor(d % 3600 / 60);
            var s = Math.floor(d % 3600 % 60);
            return ((h > 0 ? h + ":" + (m < 10 ? "0" : "") : "") + m + ":" + (s < 10 ? "0" : "") + s);
        }
    }
})
;
angular.module("travelDataService", ['ngResource'])
.factory('getZoningTravelData', function($resource){
    return $resource('rest/getZoningTravelData',{},{
        charge: {'method':'GET'}
    });
})
.factory('projectionODMatrixDistribution', function($resource){
    return $resource('rest/projectionODMatrixDistribution',{},{
        charge: {'method':'GET'}
    });
})
;